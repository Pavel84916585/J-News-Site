package RBPO.RBPO.controllers;

import RBPO.RBPO.services.AppUserService;
import de.taimos.totp.TOTP;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import RBPO.RBPO.entity.AppUser;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;

import static RBPO.RBPO.security.GoogleAuthenticator.createQRCode;
import static RBPO.RBPO.security.GoogleAuthenticator.getGoogleAuthenticatorBarCode;
import static RBPO.RBPO.security.GoogleAuthenticator.getBase64QRCode;


@Controller
@AllArgsConstructor
public class AuthorizationController {
    AppUserService userService;

    //Герерация 32значного кода MFA
    /*Для Google Authenticator требуется 20-байтовый секретный ключ,
    закодированный в виде строки base32.
    Нам нужно сгенерировать этот ключ, используя следующий код:*/

    public static String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        //System.out.println(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }


    /*преобразует секретные ключи в кодировке Base32
    в шестнадцатеричные и использует TOTP для преобразования
    их в 6-значные коды на основе текущего времени.*/
   /* public static String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }


    //Для дебага (проверяли сходятся ли числа)
    public static void CodeGoogle(){
        System.out.println(generateSecretKey());

        String secretKey = "OY7DGUS2O35TCPAES4UX7FCU2Y2Y5O33";
        String lastCode = null;
        while (true) {
            String code = getTOTPCode(secretKey);
            if (!code.equals(lastCode)) {
                System.out.println(code);
            }
            lastCode = code;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {};
        }
    }*/

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(Model model) {
        if (!(model.containsAttribute("name") || model.containsAttribute("email") || model.containsAttribute("password")))
            model.addAttribute("AppUser", new AppUser());
        return "RegistrationPage";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);
        //model будем использовать для вывода сообщения на веб страницу

        if (isActivated) {
            model.addAttribute("message", "Пользователь успешно активировался");
        } else {
            model.addAttribute("message", "Код активации не найден!");
        }

        return "redirect: /login";
    }






    @PostMapping("/registration")
    public String postRegistration(@ModelAttribute("AppUser") AppUser appuser, Model model) {


        if (userService.saveAppUser(appuser)){
            String secretKey = generateSecretKey();
            String email = appuser.getEmail();
            String Name = appuser.getUsername();
            String barCodeUrl = getGoogleAuthenticatorBarCode(secretKey, email, Name);

            String base64QRCode = getBase64QRCode(barCodeUrl);

            //выводим qr на страницу html
            model.addAttribute("base64QRCode", base64QRCode);

            return "/login";
    }
        String errors = new String("Невозможно создать пользователя!\n");
        String link = new String ("");
        if (!userService.testEmail(appuser.getEmail()))
            errors += "Пользователь с такой почтой уже зарегестрирован.\n" +
                     "Используйте другую почту;\n" +
                     "Перейдите по ссылке для сброса пароля от привязанного аккаунта: ";
            link += String.format(
                    "http://localhost:8080/reset/%s", appuser.getEmail());
        if (!userService.TestPassword(appuser.getPasswordHash()))
            errors += "Введённый Вами парль недостаточно сложен.\n"+
                     "Убедитесь, что он содержит минимум:\n" +
                     "\n1. 2 или более цифр;" +
                     "2. 3 или более Заглавных латинских символа;\n" +
                     "2. 3 или более строчных латинских символа;\n" +
                     "5. 2 или более спецсимволов;\n";
        model.addAttribute("errors", errors);
        model.addAttribute("link", link);


        //System.out.println("COOOOOOODDEEEEEEEEEE" + barCodeUrl);

        return "RegistrationPage";
    }
    @GetMapping("/reset/{email}")
    public String resetPassword(Model model, @PathVariable String email) {
        return "reset";
    }
}
