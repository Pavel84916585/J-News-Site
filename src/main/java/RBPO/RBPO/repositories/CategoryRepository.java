package RBPO.RBPO.repositories;


import RBPO.RBPO.entity.Category;

import java.util.List;

public interface CategoryRepository {
    List<Category> findByName(String name);

}
