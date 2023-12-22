package RBPO.RBPO.services;

import RBPO.RBPO.entity.AppUser;
import RBPO.RBPO.entity.Roles;
import RBPO.RBPO.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;


import static RBPO.RBPO.security.GoogleAuthenticator.generateSecretKey;

@Service
@RequiredArgsConstructor
@Slf4j

public class AppUserService {
    private final AppUserRepository appUserRepository;

    public AppUser getAppUserByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }


    public boolean testEmail (String email) {
        if (appUserRepository.findByEmail(email) != null)
            return false;
        return true;
    }



    public boolean TestPassword (String psw) {
        System.out.println(psw);

        if(psw.length()>30)
            return false;
        int balls = 0;
        balls += psw.length() >= 8? 10: 0;  // более 8 знаков
        balls += psw.chars()                // 2 буквы
                .filter(i -> "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".contains("" + (char) i)).count() > 1? 10: 0;
        balls += psw.chars()                // 2 цифры
                .filter(i -> "0123456789".contains("" + (char) i)).count() > 1 ? 10: 0;
        balls += psw.chars()                // одна или больше больших букв
                .filter(i -> "ABCDEFGHIJKLMNOPQRSTUVWXYZ".contains("" + (char) i)).count() > 2 ? 15: 0;
        balls += psw.chars()                // одна или больше маленьких букв
                .filter(i -> "abcdefghijklmnopqrstuvwxyz".contains("" + (char) i)).count() > 2 ? 15: 0;
        balls += psw.chars()                // содержатся 2 спецсимвола
                .filter(i -> "!@#$%^&*[]-".contains("" + (char) i)).count() > 1 ? 10: 0;
        if (balls >=50)
            return true;

        return false;
    }




    public void sendMessageToMail(AppUser appUser, String message) {
        mailSender.send(appUser.getEmail(), "Activation code", message);
    }

    //хэширует пароль пользователя
    @Autowired
    private BCryptPasswordEncoder encoder(){return new BCryptPasswordEncoder();}

    @Autowired
    private MailSender mailSender;

    public boolean saveAppUser(AppUser appUser)
    {
        if (testEmail(appUser.getEmail())  && TestPassword(appUser.getPasswordHash())) {
            //ищем юзера в базе
            AppUser userFromDb = appUserRepository.findByEmail(appUser.getEmail());
            if (userFromDb != null) {
                return false;
            }

            appUser.setSecretOauthCode(generateSecretKey());
            appUser.setActive(0);
            appUser.setRoles(Collections.singleton(Roles.USER));
            appUser.setActivationCode(UUID.randomUUID().toString());
            //System.out.println(appUser.getPasswordHash());
            //берем введенный пароль и хэшируем его, перезаписывая вместо "чистого" пароля
            appUser.setPasswordHash(encoder().encode(appUser.getPasswordHash()));
            //System.out.println(appUser.getPasswordHash());
            log.info("Saving new {}", appUser);

            //сохраняем юзера в бд
            appUserRepository.save(appUser);
            String message = String.format(
                    "Привет, %s! \n" +
                            "Добро пожаловать на сайт Хабр 2.0 \nПожалуйста подтвердите регистрацию: https://localhost/activate/%s",
                    appUser.getUsername(),
                    appUser.getActivationCode()
            );
            //отправляем письмо по почте

            sendMessageToMail(appUser, message);
            return true;
        }
        return false;
    }



    public boolean activateUser(String code) {
        AppUser user = appUserRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        user.setActive(1+user.getActive());

        appUserRepository.save(user);

        return true;
    }


    public boolean activateUserOaut(String email) {
        AppUser user = appUserRepository.findByEmail(email);

        if (user == null) {
            return false;
        }

        user.setActive(1+user.getActive());

        appUserRepository.save(user);

        return true;
    }


    public void deleteAppUser(Long id) {
        appUserRepository.deleteById(id);
    }


}
