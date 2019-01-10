package com.schwarzsword.pip.coursework.service;

import com.schwarzsword.pip.coursework.entity.LotEntity;
import com.schwarzsword.pip.coursework.entity.WalletEntity;
import com.schwarzsword.pip.coursework.exceptions.LotAlreadySoldException;
import com.schwarzsword.pip.coursework.exceptions.NotEnoughMoneyException;

public interface UserService {
    WalletEntity addMoney(Long value, String username);

    Integer downloadPainting(
            String name, String author, String description, String img, String genre, String technique);

    LotEntity doBet(String lotId, Long value, String username) throws LotAlreadySoldException, NotEnoughMoneyException;
}
