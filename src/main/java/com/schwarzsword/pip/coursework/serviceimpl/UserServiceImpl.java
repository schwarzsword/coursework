package com.schwarzsword.pip.coursework.serviceimpl;

import com.schwarzsword.pip.coursework.entity.*;
import com.schwarzsword.pip.coursework.exceptions.LotAlreadySoldException;
import com.schwarzsword.pip.coursework.exceptions.NotEnoughMoneyException;
import com.schwarzsword.pip.coursework.exceptions.SelfBetException;
import com.schwarzsword.pip.coursework.repository.LotRepository;
import com.schwarzsword.pip.coursework.repository.PaintingRepository;
import com.schwarzsword.pip.coursework.repository.WalletRepository;
import com.schwarzsword.pip.coursework.service.LotsService;
import com.schwarzsword.pip.coursework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    private final WalletRepository walletRepository;

    private final PaintingRepository paintingRepository;

    private final LotsService lotsService;

    private final LotRepository lotRepository;

    @Autowired
    public UserServiceImpl(WalletRepository walletRepository, PaintingRepository paintingRepository, LotsService lotsService, LotRepository lotRepository) {
        this.walletRepository = walletRepository;
        this.paintingRepository = paintingRepository;
        this.lotsService = lotsService;
        this.lotRepository = lotRepository;
    }

    @Transactional
    @Override
    public WalletEntity addMoney(Long value, UsersEntity user) {
        WalletEntity walletEntity = user.getWalletById();
        walletEntity.setBalance(walletEntity.getBalance() + value);
        walletRepository.save(walletEntity);
        return walletEntity;
    }

    @Transactional
    @Override
    public PaintingEntity downloadPainting(String name, String author, String description, String img, String genre, String technique) {
        PaintingEntity paintingEntity = new PaintingEntity(name, author, description, img, genre, technique);
        paintingRepository.save(paintingEntity);
        return paintingEntity;
    }

    @Transactional
    @Override
    public LotEntity doBet(LotEntity lot, Long value, UsersEntity user)
            throws LotAlreadySoldException, NotEnoughMoneyException, SelfBetException {
        WalletEntity wallet = user.getWalletById();
        Long balance = wallet.getBalance();
        if (balance < value) throw new NotEnoughMoneyException("На счету недостаточно средств");
        if (lot.getEndDateById().getExpectingDate().before(Timestamp.valueOf(LocalDateTime.now())))
            throw new LotAlreadySoldException("Время для ставок истекло");
        if (lot.getStartPrice() >= value) throw new NotEnoughMoneyException("Ставку необходимо перебить");
        if (user.getId() == lot.getUsersBySeller().getId())
            throw new SelfBetException("Нельзя сделать ставку на свой лот");
        wallet.setBalance(balance - value);
        walletRepository.save(wallet);
        lot.setStartPrice(value);
        lot.setUsersByLastBet(user);
        lotRepository.save(lot);
        return lot;
    }

    @Override
    public ArrayList<DealEntity> showHistory(UsersEntity user) {
        return new ArrayList<>(user.getDealsById());
    }


}
