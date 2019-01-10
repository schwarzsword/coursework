package com.schwarzsword.pip.coursework.repository;

import com.schwarzsword.pip.coursework.entity.UsersEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends CrudRepository<UsersEntity, Integer> {
    Optional<UsersEntity> findByUsername(String username);
    boolean existsByUsernameOrPhoneOrMail(String username, String phone, String mail);
}
