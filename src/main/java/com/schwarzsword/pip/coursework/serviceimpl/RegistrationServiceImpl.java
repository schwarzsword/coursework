package com.schwarzsword.pip.coursework.serviceimpl;

import com.schwarzsword.pip.coursework.entity.RolesEntity;
import com.schwarzsword.pip.coursework.entity.UsersEntity;
import com.schwarzsword.pip.coursework.entity.WalletEntity;
import com.schwarzsword.pip.coursework.repository.RolesRepository;
import com.schwarzsword.pip.coursework.repository.UsersRepository;
import com.schwarzsword.pip.coursework.repository.WalletRepository;
import com.schwarzsword.pip.coursework.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("registrationService")
public class RegistrationServiceImpl implements RegistrationService {
    private final
    UsersRepository usersRepository;

    private final
    WalletRepository walletRepository;

    private final RolesRepository rolesRepository;

    private String salt = BCrypt.gensalt();

    @Autowired
    public RegistrationServiceImpl(WalletRepository walletRepository, UsersRepository usersRepository, RolesRepository rolesRepository) {
        this.usersRepository = usersRepository;
        this.walletRepository = walletRepository;
        this.rolesRepository = rolesRepository;
    }

    @Override
    public UsersEntity signIn(String username, String password)
            throws UserDeniedAuthorizationException {
        UsersEntity usersEntity = getUserByUsername(username);
        List<String> rolesList = usersEntity.getRoles()
                .stream()
                .map(RolesEntity::getRole)
                .collect(Collectors.toList());
        if (BCrypt.checkpw(password, usersEntity.getPassword())) {
            if (rolesList.contains("ROLE_BANNED")) throw new UserDeniedAuthorizationException("Пользователь забанен");
            return usersEntity;
        } else throw new UserDeniedAuthorizationException("Неверное имя пользователя или пароль");
    }

    @Transactional
    @Override
    public UsersEntity signUp(String name, String surname, String password, String mail)
            throws UserDeniedAuthorizationException {
        if (!usersRepository.existsByMail(mail)) {
            String pwd = BCrypt.hashpw(password, salt);
            RolesEntity role = rolesRepository.getByRole("ROLE_USER");
            UsersEntity user = new UsersEntity(name, surname, pwd, mail, role);
            usersRepository.save(user);
            WalletEntity walletEntity = new WalletEntity(user);
            walletRepository.save(walletEntity);
            user.setWalletById(walletEntity);
            return user;
        } else throw new UserDeniedAuthorizationException("Данный пользователь уже существует");
    }

    @Override
    public UsersEntity getUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UsersEntity> optionalUsersEntity = usersRepository.findByUsername(username);
        if (optionalUsersEntity.isPresent()) {
            return optionalUsersEntity.get();
        } else throw new UsernameNotFoundException("Пользователь с данным именем не найден");
    }

    @Transactional
    @Override
    public UsersEntity authentication(String mail, String name, String surname) {
        final Optional<UsersEntity> byUsername = usersRepository.findByUsername(mail);
        return byUsername.orElseGet(() -> signUp(name, surname, "", mail));
    }

}
