package RBPO.RBPO.services;

import RBPO.RBPO.entity.AppUser;
import RBPO.RBPO.entity.Article;
import RBPO.RBPO.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserService {
    private final AppUserRepository appUserRepository;

    public List<AppUser> listAppUser(String email) {
        if (email != null) return appUserRepository.findByEmail(email);
        return appUserRepository.findAll();
    }
    public void saveAppUser(AppUser appUser) {
        log.info("Saving new {}", appUser);
        appUserRepository.save(appUser);
    }
    public void deleteAppUser(Long id) {
        appUserRepository.deleteById(id);
    }
    public AppUser getAppUserById(long id) {
        return appUserRepository.findById(id).orElse(null);
    }

}
