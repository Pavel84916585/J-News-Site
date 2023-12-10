package RBPO.RBPO.services;

import RBPO.RBPO.entity.AppUser;
import RBPO.RBPO.entity.Article;
import RBPO.RBPO.entity.Roles;
import RBPO.RBPO.repositories.AppUserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import RBPO.RBPO.services.MailSender;

import javax.management.relation.Role;
import java.util.Collections;
import java.util.UUID;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j

public class AppUserService {
    boolean testEmail (String email) {

        String regex = "^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;
        return pattern.matcher(email).matches();
        //.out.println(email +" : "+ matcher.matches());

    }



    boolean TestPassword (String psw) {
        if(psw.length()>30)
            return false;
        int balls = 0;
        balls += psw.length() >= 8? 10: 0;
        System.out.println(balls);// более 8 знаков
        balls += psw.chars()                // 2 буквы
                .filter(i -> "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".contains("" + (char) i)).count() > 1? 10: 0;
        System.out.println(balls);
        balls += psw.chars()                // 2 цифры
                .filter(i -> "0123456789".contains("" + (char) i)).count() > 1? 10: 0;
        System.out.println(balls);
        balls += psw.chars()                // одна или больше больших букв
                .anyMatch(i -> "ABCDEFGHIJKLMNOPQRSTUVWXYZ".contains("" + (char) i))? 10: 0;
        System.out.println(balls);
        balls += psw.chars()                // одна или больше маленьких букв
                .anyMatch(i -> "abcdefghijklmnopqrstuvwxyz".contains("" + (char) i))? 10: 0;
        System.out.println(balls);
        balls += psw.chars()                // содержатся спецсимволы (дополнить список символов по желанию)
                .anyMatch(i -> "!@#$%^&*[]-".contains("" + (char) i))? 10: 0;
        System.out.println(balls);
        if (balls >=50)
            return true;

        return false;
    }


    private final AppUserRepository appUserRepository;

    //хэширует пароль пользователя
    @Autowired
    private BCryptPasswordEncoder encoder(){return new BCryptPasswordEncoder();}

    @Autowired
    private MailSender mailSender;

    public boolean saveAppUser(AppUser appUser)
    {


        System.out.println(appUser.getPasswordHash());
        if (testEmail(appUser.getEmail())  && TestPassword(appUser.getPasswordHash())) {


            //ищем юзера в базе
            AppUser userFromDb = appUserRepository.findByEmail(appUser.getEmail());
            if (userFromDb != null) {
                return false;
            }


            appUser.setActive(false);
            appUser.setRoles(Collections.singleton(Roles.USER));
            appUser.setActivationCode(UUID.randomUUID().toString());



            //берем введенный пароль и хэшируем его, перезаписывая вместо "чистого" пароля
            appUser.setPasswordHash(encoder().encode(appUser.getPasswordHash()));
            log.info("Saving new {}", appUser);

            //сохраняем юзера в бд
            appUserRepository.save(appUser);

            String message = String.format(
                    "Hello, %s! \n" +
                            "Добро пожаловать на сайт Хабр 2.0 \nПожалуйста подтвердите регистрацию: http://localhost:8080/activate/%s",
                    appUser.getUsername(),
                    appUser.getActivationCode()
            );

            //отправляем письмо по почте
            mailSender.send(appUser.getEmail(), "Activation code", message);

            return true;
        }
        else {
            System.out.println("пароль недостаточно сильный или почта не почта");
        }
        return false;
    }



    public boolean activateUser(String code) {
        AppUser user = appUserRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        user.setActive(true);

        appUserRepository.save(user);

        return true;
    }


    public void deleteAppUser(Long id) {
        appUserRepository.deleteById(id);
    }
    public AppUser getAppUserById(long id) {
        return appUserRepository.findById(id).orElse(null);
    }


}
