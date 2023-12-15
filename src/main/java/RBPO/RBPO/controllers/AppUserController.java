package RBPO.RBPO.controllers;

import RBPO.RBPO.entity.AppUser;
import RBPO.RBPO.services.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    @PostMapping("/appUser/create")
    public String createAppUser(AppUser appUser) {
        appUserService.saveAppUser(appUser);
        return "redirect:/";
    }
    @PostMapping("/appUser/delete/{id}")
    public String deleteAppUser(@PathVariable Long id) {
        appUserService.deleteAppUser(id);
        return "redirect:/";
    }
}
