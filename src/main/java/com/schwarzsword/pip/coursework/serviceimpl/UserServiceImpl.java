package com.schwarzsword.pip.coursework.serviceimpl;

import com.schwarzsword.pip.coursework.entity.PaintingEntity;
import com.schwarzsword.pip.coursework.entity.UsersEntity;
import com.schwarzsword.pip.coursework.entity.WalletEntity;
import com.schwarzsword.pip.coursework.repository.PaintingRepository;
import com.schwarzsword.pip.coursework.repository.WalletRepository;
import com.schwarzsword.pip.coursework.service.RegistrationService;
import com.schwarzsword.pip.coursework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {
    private final RegistrationService registrationService;

    private final WalletRepository walletRepository;

    private final PaintingRepository paintingRepository;

    @Autowired
    public UserServiceImpl(RegistrationService registrationService, WalletRepository walletRepository, PaintingRepository paintingRepository) {
        this.registrationService = registrationService;
        this.walletRepository = walletRepository;
        this.paintingRepository = paintingRepository;
    }

    @Override
    public WalletEntity addMoney(Long value, String username) {
        UsersEntity user = registrationService.getUserByUsername(username);
        WalletEntity walletEntity = user.getWalletById();
        walletEntity.setBalance(walletEntity.getBalance() + value);
        walletRepository.save(walletEntity);
        return walletEntity;
    }

    @Override
    public Integer downloadPainting(String name, String author, String description, String img, String genre, String technique) {
        PaintingEntity paintingEntity = new PaintingEntity(name, author, description, img, genre, technique);
        paintingRepository.save(paintingEntity);
        return paintingRepository.findByAuthorAndName(author, name).getId();
    }


}
