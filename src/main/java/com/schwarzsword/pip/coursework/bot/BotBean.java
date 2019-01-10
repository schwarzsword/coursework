package com.schwarzsword.pip.coursework.bot;

import com.schwarzsword.pip.coursework.entity.*;
import com.schwarzsword.pip.coursework.repository.*;
import com.schwarzsword.pip.coursework.service.LotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Scope("periodical")
public class BotBean {
    private final LotsService lotsService;
    private final EndDateRepository endDateRepository;
    private final LotRepository lotRepository;
    private final WalletRepository walletRepository;
    private final DealRepository dealRepository;
    final private PaymentRepository paymentRepository;

    @Autowired
    public BotBean(LotsService lotsService, EndDateRepository endDateRepository, LotRepository lotRepository, WalletRepository walletRepository, DealRepository dealRepository, PaymentRepository paymentRepository) {
        this.lotsService = lotsService;
        this.endDateRepository = endDateRepository;
        this.lotRepository = lotRepository;
        this.walletRepository = walletRepository;
        this.dealRepository = dealRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    @PostConstruct
    public void setResults() {
        List<LotEntity> list = lotsService.findAvailableLots();
        list.forEach(lot -> {
            EndDateEntity endDate = lot.getEndDateById();
            if (endDate.getExpectingDate().before(Timestamp.valueOf(LocalDateTime.now())) && lot.getUsersByLastBet() != null) {
                UsersEntity customer = lot.getUsersByLastBet();
                UsersEntity seller = lot.getUsersBySeller();
                WalletEntity custWallet = customer.getWalletById();
                WalletEntity sellWallet = seller.getWalletById();
                endDate.setState(false);
                lot.setState("sold");
                sellWallet.setBalance(sellWallet.getBalance() + lot.getStartPrice());
                PaymentEntity paymentEntity = new PaymentEntity(lot.getStartPrice(), custWallet, sellWallet);
                DealEntity deal = new DealEntity(endDate, customer, paymentEntity);

                lotRepository.save(lot);
                endDateRepository.save(endDate);
                walletRepository.save(sellWallet);
                paymentRepository.save(paymentEntity);
                dealRepository.save(deal);
            } else
                endDate.setExpectingDate(Timestamp.valueOf(endDate.getExpectingDate().toLocalDateTime().plusDays(3)));
        });
    }
}
