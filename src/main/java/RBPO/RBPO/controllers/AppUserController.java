package RBPO.RBPO.controllers;

import RBPO.RBPO.services.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller("appUser")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;
   @PostMapping("/delete/{id}")
    public String deleteAppUser(@PathVariable Long id) {
        appUserService.deleteAppUser(id);
        return "redirect:/";
    }

}
