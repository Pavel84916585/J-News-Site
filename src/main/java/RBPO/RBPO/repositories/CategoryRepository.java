package RBPO.RBPO.repositories;


import RBPO.RBPO.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    public Category findByName(String name);

}
