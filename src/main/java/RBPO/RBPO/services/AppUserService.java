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
    boolean TestPassword (String psw) {
        if(psw.length()>30)
            return false;
        int balls = 0;
        balls += psw.length() >= 8? 10: 0;  // более 8 знаков
        balls += psw.chars()                // 2 буквы
                .filter(i -> "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".contains("" + (char) i)).count() > 1? 10: 0;
        balls += psw.chars()                // 2 цифры
                .filter(i -> "0123456789".contains("" + (char) i)).count() > 1? 10: 0;
        balls += psw.chars()                // одна или больше больших букв
                .anyMatch(i -> "ABCDEFGHIJKLMNOPQRSTUVWXYZ".contains("" + (char) i))? 10: 0;
        balls += psw.chars()                // одна или больше маленьких букв
                .anyMatch(i -> "abcdefghijklmnopqrstuvwxyz".contains("" + (char) i))? 10: 0;
        balls += psw.chars()                // содержатся спецсимволы (дополнить список символов по желанию)
                .anyMatch(i -> "!@#$%^&*[]".contains("" + (char) i))? 10: 0;
        if (balls >=50)
            return true;

        return false;
    }


    private final AppUserRepository appUserRepository;

    //хэширует пароль пользователя
    @Autowired
    private BCryptPasswordEncoder encoder(){return new BCryptPasswordEncoder();}

    /* public List<AppUser> listAppUser(String email) {
         if (email != null) return appUserRepository.findByEmail(email);
         return appUserRepository.findAll();
     }*/
    public void saveAppUser(AppUser appUser) {
        if (TestPassword(appUser.getPasswordHash())) {
            //берем введенный пароль и хэшируем его, перезаписывая вместо "чистого" пароля
            appUser.setPasswordHash(encoder().encode(appUser.getPasswordHash()));
            log.info("Saving new {}", appUser);
            appUserRepository.save(appUser);
            //сохраняем юзера в бд
        }
    }
    public void deleteAppUser(Long id) {
        appUserRepository.deleteById(id);
    }
    public AppUser getAppUserById(long id) {
        return appUserRepository.findById(id).orElse(null);
    }


}
