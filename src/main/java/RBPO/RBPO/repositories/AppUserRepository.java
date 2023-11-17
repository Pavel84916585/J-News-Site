package RBPO.RBPO.repositories;

import RBPO.RBPO.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    List<AppUser> findByEmail(String email);

}
