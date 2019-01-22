package com.schwarzsword.pip.coursework.service;

import com.schwarzsword.pip.coursework.entity.*;
import com.schwarzsword.pip.coursework.exceptions.LotAlreadySoldException;
import com.schwarzsword.pip.coursework.exceptions.NotEnoughMoneyException;
import com.schwarzsword.pip.coursework.exceptions.SelfBetException;

import java.util.List;
import java.util.List;

public interface UserService {

    PaintingEntity downloadPainting(
            String name, String author, String description, String img, String genre, String technique);

    LotEntity doBet(LotEntity lot, Long value, UsersEntity user)
            throws LotAlreadySoldException, NotEnoughMoneyException, SelfBetException;

    List<DealEntity> showHistory(UsersEntity user);
}
