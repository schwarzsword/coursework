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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
                lot.setState("sold");
                lotRepository.save(lot);
                sellWallet.setBalance(sellWallet.getBalance() + lot.getStartPrice());
                walletRepository.save(sellWallet);
                PaymentEntity paymentEntity = new PaymentEntity(lot.getStartPrice(), custWallet, sellWallet);
                DealEntity deal = new DealEntity(endDate, customer, paymentEntity);
                paymentRepository.save(paymentEntity);
                if (sellWallet.getPaymentsBySeller() != null) {
                    List<PaymentEntity> sellWalletPaymentsBySeller = new ArrayList<>(sellWallet.getPaymentsBySeller());
                    sellWalletPaymentsBySeller.add(paymentEntity);
                    sellWallet.setPaymentsBySeller(sellWalletPaymentsBySeller);
                } else {
                    ArrayList<PaymentEntity> paymentEntities = new ArrayList<>();
                    paymentEntities.add(paymentEntity);
                    sellWallet.setPaymentsBySeller(paymentEntities);
                }
                if (custWallet.getPaymentsByCustomer() != null) {
                    List<PaymentEntity> custWalletPaymentsBySeller = new ArrayList<>(custWallet.getPaymentsBySeller());
                    custWalletPaymentsBySeller.add(paymentEntity);
                    custWallet.setPaymentsByCustomer(custWalletPaymentsBySeller);
                } else {
                    ArrayList<PaymentEntity> paymentEntities = new ArrayList<>();
                    paymentEntities.add(paymentEntity);
                    custWallet.setPaymentsBySeller(paymentEntities);
                }
                dealRepository.save(deal);
                endDate.setDealById(deal);
                endDate.setState(false);
                endDateRepository.save(endDate);
                if (customer.getDealsById() != null) {
                    List<DealEntity> dealsById = new ArrayList<>(customer.getDealsById());
                    dealsById.add(deal);
                    customer.setDealsById(dealsById);
                } else {
                    ArrayList<DealEntity> dealEntities = new ArrayList<>();
                    dealEntities.add(deal);
                    customer.setDealsById(dealEntities);
                }
            } else if (lot.getPolicy() != null)
                endDate.setExpectingDate(Timestamp.valueOf(endDate.getExpectingDate().toLocalDateTime().plusDays(lot.getPolicy())));
            else {
                endDate.setState(false);
                lot.setState("time expired");
            }
        });
    }
}
