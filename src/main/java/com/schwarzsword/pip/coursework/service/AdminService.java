package com.schwarzsword.pip.coursework.service;

import com.schwarzsword.pip.coursework.entity.LotEntity;
import com.schwarzsword.pip.coursework.entity.UsersEntity;

import java.util.List;
import java.util.NoSuchElementException;

public interface AdminService {
    LotEntity deleteLot(String lotId) throws NoSuchElementException;

    UsersEntity banUser(UsersEntity user);

    UsersEntity unbanUser(UsersEntity user);

    UsersEntity addRole(UsersEntity user, String role);

    UsersEntity removeRole(UsersEntity user, String role);

    List<UsersEntity> showBanned();
}
