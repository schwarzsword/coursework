package com.schwarzsword.pip.coursework.repository;

import com.schwarzsword.pip.coursework.entity.EndDateEntity;
import com.schwarzsword.pip.coursework.entity.LotEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EndDateRepository extends CrudRepository<EndDateEntity, Integer> {
    List<EndDateEntity> findAllByStateIsTrue();
}
