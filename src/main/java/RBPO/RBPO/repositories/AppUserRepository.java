package RBPO.RBPO.repositories;

import RBPO.RBPO.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    public AppUser findByEmail(String email);
    AppUser findByActivationCode(String code);
}
