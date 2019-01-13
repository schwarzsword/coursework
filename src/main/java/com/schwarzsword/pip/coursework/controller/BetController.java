package com.schwarzsword.pip.coursework.controller;

import com.schwarzsword.pip.coursework.entity.LotEntity;
import com.schwarzsword.pip.coursework.entity.UsersEntity;
import com.schwarzsword.pip.coursework.exceptions.LotAlreadySoldException;
import com.schwarzsword.pip.coursework.exceptions.NotEnoughMoneyException;
import com.schwarzsword.pip.coursework.exceptions.SelfBetException;
import com.schwarzsword.pip.coursework.service.LotsService;
import com.schwarzsword.pip.coursework.service.RegistrationService;
import com.schwarzsword.pip.coursework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;

@Controller
public class BetController {
    final private
    UserService userService;
    final private
    RegistrationService registrationService;
    final private
    LotsService lotsService;

    @Autowired
    public BetController(UserService userService, RegistrationService registrationService, LotsService lotsService) {
        this.userService = userService;
        this.registrationService = registrationService;
        this.lotsService = lotsService;
    }

    @MessageMapping("/bet")
    @SendTo("/topic/price")
    public String doBet(String priceStr, String lotId, @AuthenticationPrincipal User user){
        Long price = Long.parseLong(priceStr);
        UsersEntity user1 = registrationService.getUserByUsername(user.getUsername());
        LotEntity lot = lotsService.findLotById(lotId);
        LotEntity lotEntity;
        try {
             lotEntity = userService.doBet(lot, price, user1);
        } catch (SelfBetException | NotEnoughMoneyException | LotAlreadySoldException e) {
           return e.getMessage();
        }
        return lotEntity.getStartPrice().toString();
    }

}
