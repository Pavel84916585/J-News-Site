package RBPO.RBPO.controllers;

import RBPO.RBPO.services.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import RBPO.RBPO.entity.AppUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
