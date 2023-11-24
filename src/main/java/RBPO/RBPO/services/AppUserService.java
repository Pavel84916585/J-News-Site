package RBPO.RBPO.services;

import RBPO.RBPO.entity.AppUser;
import RBPO.RBPO.entity.Article;
import RBPO.RBPO.repositories.AppUserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserService {
    private final AppUserRepository appUserRepository;

    //хэширует пароль пользователя
    @Autowired
    private BCryptPasswordEncoder encoder(){return new BCryptPasswordEncoder();}

   /* public List<AppUser> listAppUser(String email) {
        if (email != null) return appUserRepository.findByEmail(email);
        return appUserRepository.findAll();
    }*/
    public void saveAppUser(AppUser appUser) {
        //берем введенный пароль и хэшируем его, перезаписывая вместо "чистого" пароля
        appUser.setPasswordHash(encoder().encode(appUser.getPasswordHash()));

        log.info("Saving new {}", appUser);
        //сохраняем юхера в бд
        appUserRepository.save(appUser);
    }
    public void deleteAppUser(Long id) {
        appUserRepository.deleteById(id);
    }
    public AppUser getAppUserById(long id) {
        return appUserRepository.findById(id).orElse(null);
    }


    /*public AppUser findByEmail(String email)
    {

        return
    }*/

    /*
    *
    *
    */

}
