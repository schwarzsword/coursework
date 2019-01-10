package com.schwarzsword.pip.coursework.service;

import com.schwarzsword.pip.coursework.entity.LotEntity;
import com.schwarzsword.pip.coursework.entity.UsersEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.NoSuchElementException;

public interface AdminService {
    LotEntity deleteLot(String lotId) throws NoSuchElementException;
    UsersEntity banUser(String username) throws UsernameNotFoundException;
}
