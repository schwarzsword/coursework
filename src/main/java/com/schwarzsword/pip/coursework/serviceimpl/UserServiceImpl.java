package com.schwarzsword.pip.coursework.serviceimpl;

import com.schwarzsword.pip.coursework.entity.LotEntity;
import com.schwarzsword.pip.coursework.entity.PaintingEntity;
import com.schwarzsword.pip.coursework.entity.UsersEntity;
import com.schwarzsword.pip.coursework.entity.WalletEntity;
import com.schwarzsword.pip.coursework.exceptions.LotAlreadySoldException;
import com.schwarzsword.pip.coursework.exceptions.NotEnoughMoneyException;
import com.schwarzsword.pip.coursework.repository.LotRepository;
import com.schwarzsword.pip.coursework.repository.PaintingRepository;
import com.schwarzsword.pip.coursework.repository.WalletRepository;
import com.schwarzsword.pip.coursework.service.LotsService;
import com.schwarzsword.pip.coursework.service.RegistrationService;
import com.schwarzsword.pip.coursework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service("userService")
public class UserServiceImpl implements UserService {
    private final RegistrationService registrationService;

    private final WalletRepository walletRepository;

    private final PaintingRepository paintingRepository;

    private final LotsService lotsService;

    private final LotRepository lotRepository;

    @Autowired
    public UserServiceImpl(RegistrationService registrationService, WalletRepository walletRepository, PaintingRepository paintingRepository, LotsService lotsService, LotRepository lotRepository) {
        this.registrationService = registrationService;
        this.walletRepository = walletRepository;
        this.paintingRepository = paintingRepository;
        this.lotsService = lotsService;
        this.lotRepository = lotRepository;
    }

    @Transactional
    @Override
    public WalletEntity addMoney(Long value, String username) {
        UsersEntity user = registrationService.getUserByUsername(username);
        WalletEntity walletEntity = user.getWalletById();
        walletEntity.setBalance(walletEntity.getBalance() + value);
        walletRepository.save(walletEntity);
        return walletEntity;
    }

    @Transactional
    @Override
    public Integer downloadPainting(String name, String author, String description, String img, String genre, String technique) {
        PaintingEntity paintingEntity = new PaintingEntity(name, author, description, img, genre, technique);
        paintingRepository.save(paintingEntity);
        return paintingRepository.findByAuthorAndName(author, name).getId();
    }

    @Transactional
    @Override
    public LotEntity doBet(String lotId, Long value, String username) throws LotAlreadySoldException, NotEnoughMoneyException {
        UsersEntity user = registrationService.getUserByUsername(username);
        WalletEntity wallet = user.getWalletById();
        Long balance = wallet.getBalance();
        LotEntity lot = lotsService.findLotById(lotId);
        if (balance < value) throw new NotEnoughMoneyException("На счету недостаточно денег");
        if(lot.getEndDateById().getExpectingDate().before(Timestamp.valueOf(LocalDateTime.now())))
            throw new LotAlreadySoldException("Время для ставок истекло");
        wallet.setBalance(balance - value);
        walletRepository.save(wallet);
        lot.setStartPrice(value);
        lot.setUsersByLastBet(user);
        lotRepository.save(lot);
        return lot;
    }
}
