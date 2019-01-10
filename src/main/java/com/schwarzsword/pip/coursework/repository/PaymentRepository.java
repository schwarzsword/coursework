package com.schwarzsword.pip.coursework.repository;

import com.schwarzsword.pip.coursework.entity.PaymentEntity;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<PaymentEntity, Integer> {
}
