package RBPO.RBPO.controllers;

import RBPO.RBPO.services.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import RBPO.RBPO.entity.AppUser;
import org.springframework.web.bind.annotation.*;

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

        return "redirect: /login";
    }
    @PostMapping("/registration")
    public String postRegistration(@ModelAttribute("AppUser") AppUser appuser, Model model){
        if (userService.saveAppUser(appuser))
            return "redirect: /login";
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
        return "RegistrationPage";
    }
    @GetMapping("/reset/{email}")
    public String resetPassword(Model model, @PathVariable String email) {
        return "reset";
    }
}
