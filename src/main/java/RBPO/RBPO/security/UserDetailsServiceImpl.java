package RBPO.RBPO.security;

import RBPO.RBPO.entity.AppUser;
import RBPO.RBPO.repositories.AppUserRepository;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    AppUserRepository userRepo;
    @Override
    @Transactional

    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser userEntity = (AppUser)this.userRepo.findByEmail(username);
        return UserDetailsImpl.build(userEntity);
    }
}