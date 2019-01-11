package com.schwarzsword.pip.coursework.service;

import com.schwarzsword.pip.coursework.entity.LotEntity;
import com.schwarzsword.pip.coursework.entity.UsersEntity;

import java.util.NoSuchElementException;

public interface ExpertService {
    LotEntity setCertificate(LotEntity lot, UsersEntity user) throws NoSuchElementException;
}
