package com.schwarzsword.pip.coursework.serviceimpl;

import com.schwarzsword.pip.coursework.entity.EndDateEntity;
import com.schwarzsword.pip.coursework.entity.LotEntity;
import com.schwarzsword.pip.coursework.entity.UsersEntity;
import com.schwarzsword.pip.coursework.entity.WalletEntity;
import com.schwarzsword.pip.coursework.repository.*;
import com.schwarzsword.pip.coursework.service.AdminService;
import com.schwarzsword.pip.coursework.service.EmailService;
import com.schwarzsword.pip.coursework.service.LotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    private final UsersRepository usersRepository;

   final private EmailService emailService;

    private final RolesRepository rolesRepository;

    @Autowired
    public AdminServiceImpl(EmailService emailService, LotsService lotsService, EndDateRepository endDateRepository, LotRepository lotRepository, WalletRepository walletRepository, UsersRepository usersRepository, RolesRepository rolesRepository) {
        this.lotsService = lotsService;
        this.endDateRepository = endDateRepository;
        this.lotRepository = lotRepository;
        this.walletRepository = walletRepository;
        this.usersRepository = usersRepository;
        this.emailService = emailService;
        this.rolesRepository = rolesRepository;
    }

    @Transactional
    @Override
    public LotEntity deleteLot(String lotId) throws NoSuchElementException {
        LotEntity lot = lotsService.findLotById(lotId);
        EndDateEntity endDateEntity = lot.getEndDateById();
        endDateRepository.delete(endDateEntity);
        lot.setState("deleted");
        lotRepository.save(lot);
        UsersEntity seller = lot.getUsersBySeller();
        UsersEntity customer = lot.getUsersByLastBet();
        emailService.sendSimpleMessage(seller.getMail(), "Удаление лота", "Ваш лот был удален с аукциона.\n" +
                " Возможно подозрение в мошенничестве. \n" +
                "Если вы считаете, что удаление необосновано, то можете написать в поддержку\n" +
                "С уважением, администрация интернет-аукциона.");
        if (!(customer == null)) {
            WalletEntity walletEntity = customer.getWalletById();
            walletEntity.setBalance(walletEntity.getBalance() + lot.getStartPrice());
            walletRepository.save(walletEntity);
        }
        return lot;
    }

    @Transactional
    @Override
    public UsersEntity banUser(UsersEntity user) {
        addRole(user, "ROLE_BANNED");
        emailService.sendSimpleMessage(user.getMail(), "Блокировка пользователя", "Ваш аккаунт был заблокирован администрацией интернет-аукциона.\n" +
                "Возможно, была замечена подозрительная активность.\n" +
                "По всем вопросам вы можете обратиться в поддержкку\n" +
                "С уважением, администрация интернет-аукциона.");
        return user;
    }

    @Transactional
    @Override
    public UsersEntity unbanUser(UsersEntity user) {
        removeRole(user, "ROLE_BANNED");
        emailService.sendSimpleMessage(user.getMail(), "Разблокировка пользователя", "Ваш аккаунт был разблокирован администрацией " +
                "интернет-аукциона после вашего обращения.\n" +
                "Приносим свои извенения за временные неудобства.");
        return user;
    }

    @Override
    public UsersEntity addRole(UsersEntity user, String role) {
        user.addRole(rolesRepository.getByRole(role));
        return user;
    }

    @Override
    public UsersEntity removeRole(UsersEntity user, String role) {
        user.removeRole(rolesRepository.getByRole(role));
        return user;
    }

    @Override
    public List<UsersEntity> showBanned() {
        return usersRepository.findAllByRoles(rolesRepository.getByRole("ROLE_BANNED"));
    }
}
