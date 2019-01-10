package com.schwarzsword.pip.coursework.service;

import com.schwarzsword.pip.coursework.entity.WalletEntity;

public interface UserService {
    WalletEntity addMoney(Long value, String username);

    Integer downloadPainting(
            String name, String author, String description, String img, String genre, String technique);
}
