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
    public String getRegistrationPage(Model model){
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

    @GetMapping("/success")
    public String getSuccessPage() {
        return "success";
    }


    @PostMapping("/check")
    public String check() {
        return "redirect:/login";
    }

    @PostMapping("/registration")
    public String postRegistration(@ModelAttribute("AppUser") AppUser appuser, Model model){
        userService.saveAppUser(appuser);
        return "redirect: /login";

    }
}
