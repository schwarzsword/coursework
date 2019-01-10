package com.schwarzsword.pip.coursework.service;

import com.schwarzsword.pip.coursework.entity.LotEntity;

import java.util.NoSuchElementException;

public interface ExpertService {
    LotEntity setCertificate(String lotId, String username) throws NoSuchElementException;
}
