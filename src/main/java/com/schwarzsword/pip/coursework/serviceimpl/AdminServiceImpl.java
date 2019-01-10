package com.schwarzsword.pip.coursework.serviceimpl;

import com.schwarzsword.pip.coursework.entity.*;
import com.schwarzsword.pip.coursework.repository.EndDateRepository;
import com.schwarzsword.pip.coursework.repository.LotRepository;
import com.schwarzsword.pip.coursework.repository.UsersRepository;
import com.schwarzsword.pip.coursework.repository.WalletRepository;
import com.schwarzsword.pip.coursework.service.AdminService;
import com.schwarzsword.pip.coursework.service.LotsService;
import com.schwarzsword.pip.coursework.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.NoSuchElementException;

@Service("adminService")
public class AdminServiceImpl implements AdminService {
    final private
    LotsService lotsService;

    final private
    EndDateRepository endDateRepository;

    final private
    LotRepository lotRepository;

    final private
    WalletRepository walletRepository;

    final private
    RegistrationService registrationService;

    private final UsersRepository usersRepository;

    @Autowired
    public AdminServiceImpl(RegistrationService registrationService, LotsService lotsService, EndDateRepository endDateRepository, LotRepository lotRepository, WalletRepository walletRepository, UsersRepository usersRepository) {
        this.lotsService = lotsService;
        this.endDateRepository = endDateRepository;
        this.lotRepository = lotRepository;
        this.walletRepository = walletRepository;
        this.registrationService = registrationService;
        this.usersRepository = usersRepository;
    }

    @Transactional
    @Override
    public LotEntity deleteLot(String lotId) throws NoSuchElementException {
        LotEntity lot = lotsService.findLotById(lotId);
        EndDateEntity endDateEntity = lot.getEndDateById();
        endDateRepository.delete(endDateEntity);
        lot.setState("deleted");
        lotRepository.save(lot);
        UsersEntity usersEntity = lot.getUsersByLastBet();
        if (!(usersEntity == null)) {
            WalletEntity walletEntity = usersEntity.getWalletById();
            walletEntity.setBalance(walletEntity.getBalance() + lot.getStartPrice());
            walletRepository.save(walletEntity);
        }
        return lot;
    }

    @Transactional
    @Override
    public UsersEntity banUser(String username) throws UsernameNotFoundException {
        UsersEntity user = registrationService.getUserByUsername(username);
        user.setRoles(Arrays.asList(new RolesEntity("BANNED")));
        usersRepository.save(user);
        return user;
    }
}
