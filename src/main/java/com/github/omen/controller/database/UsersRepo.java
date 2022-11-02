package com.github.omen.controller.database;

import com.github.omen.controller.database.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UsersRepo extends CrudRepository<User, Integer> {
    List<User> findById(int id);

    User findUserByUserNameEquals(String username);

    boolean existsUserByUserNameEquals(String username);
}
