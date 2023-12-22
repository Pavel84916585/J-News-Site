package RBPO.RBPO.controllers;

import RBPO.RBPO.services.AppUserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import RBPO.RBPO.entity.AppUser;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;

import static RBPO.RBPO.security.GoogleAuthenticator.*;


@Controller
@AllArgsConstructor
public class AuthorizationController {
    AppUserService userService;

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

        return "redirect: /";
    }






    @PostMapping("/registration")
    public String postRegistration(@ModelAttribute("AppUser") AppUser appuser, Model model, HttpSession session) {


        if (userService.saveAppUser(appuser)){

            String email = appuser.getEmail();
            String Name = appuser.getUsername();
            String barCodeUrl = getGoogleAuthenticatorBarCode(appuser.getSecretOauthCode(), email, Name);

            String base64QRCode = getBase64QRCode(barCodeUrl);

            //выводим qr на страницу html
            System.out.println(appuser.getSecretOauthCode());
            session.setAttribute("appuser", appuser);
            session.setAttribute("base64QRCode", base64QRCode);

            model.addAttribute("base64QRCode", base64QRCode);

            return "/oauth";
    }
        String errors = new String("Невозможно создать пользователя!\n");
        String link = new String ("");
        if (!userService.testEmail(appuser.getEmail()))
            errors += "Пользователь с такой почтой уже зарегестрирован.\n" +
                     "Используйте другую почту;\n" +
                     "Перейдите по ссылке для сброса пароля от привязанного аккаунта: ";

        //сделать восстановление пароля
            link += String.format(
                    "https://localhost/reset/%s", appuser.getEmail());
        if (!userService.TestPassword(appuser.getPasswordHash()))
            errors += "Введённый Вами парль недостаточно сложен.\n"+
                     "Убедитесь, что он содержит минимум:\n" +
                     "1. 2 или более цифр;" +
                     "2. 3 или более Заглавных латинских символа;\n" +
                     "2. 3 или более строчных латинских символа;\n" +
                     "5. 2 или более спец. teсимволов;\n";
        model.addAttribute("errors", errors);
        model.addAttribute("link", link);


        //System.out.println("COOOOOOODDEEEEEEEEEE" + barCodeUrl);

        return "RegistrationPage";
    }
    @GetMapping("/reset/{email}")
    public String resetPassword(Model model, @PathVariable String email) {
        return "reset";
    }

    @GetMapping("/oauth")
    public String GetOauth(@ModelAttribute("AppUser") AppUser appuser, Model model) {
        return "oauth";
    }


    @PostMapping("/oauth")
    public String PostOauth(@RequestParam("oauthCode") String oauthCode, Model model, HttpSession session) {
        System.out.println(oauthCode);
        System.out.println(session);

        AppUser appuser = (AppUser) session.getAttribute("appuser");

        model.addAttribute("base64QRCode",(String) session.getAttribute("base64QRCode"));

        if(oauthCode.equals(getTOTPCode(appuser.getSecretOauthCode())))
        {
            System.out.println("ВСЁ ГУД, КОД подходит");

            userService.activateUserOaut(appuser.getEmail());
            return "redirect:/";
        }

        System.out.println("КОД НЕ ПОДХОДИТ");
        model.addAttribute("errors", "КОД НЕ ПОДХОДИТ");
        return "oauth";
    }
}
