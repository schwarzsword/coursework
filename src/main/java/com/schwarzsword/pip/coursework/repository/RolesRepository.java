package com.schwarzsword.pip.coursework.repository;

import com.schwarzsword.pip.coursework.entity.RolesEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RolesRepository extends CrudRepository<RolesEntity, Integer> {
    RolesEntity getByRole(String role);
}
