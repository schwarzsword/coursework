package com.schwarzsword.pip.coursework.serviceimpl;

import com.schwarzsword.pip.coursework.entity.RolesEntity;
import com.schwarzsword.pip.coursework.entity.UsersEntity;
import com.schwarzsword.pip.coursework.entity.WalletEntity;
import com.schwarzsword.pip.coursework.repository.RolesRepository;
import com.schwarzsword.pip.coursework.repository.UsersRepository;
import com.schwarzsword.pip.coursework.repository.WalletRepository;
import com.schwarzsword.pip.coursework.service.RegistrationService;
import com.sun.deploy.security.UserDeclinedException;
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
            if (rolesList.contains("BANNED")) throw new UserDeniedAuthorizationException("Пользователь забанен");
            return usersEntity;
        } else throw new UserDeniedAuthorizationException("Неверное имя пользователя или пароль");
    }

    @Transactional
    @Override
    public UsersEntity signUp(String name, String surname, String password, String mail)
            throws UserDeclinedException {
        if (!usersRepository.existsByMail(mail)) {
            String pwd = BCrypt.hashpw(password, salt);
            RolesEntity role = rolesRepository.getByRole("USER");
            UsersEntity user = new UsersEntity(name, surname, pwd, mail, role);
            usersRepository.save(user);
            WalletEntity walletEntity = new WalletEntity(user);
            walletRepository.save(walletEntity);
            user.setWalletById(walletEntity);
            return user;
        } else throw new UserDeclinedException("Данный пользователь уже существует");
    }

    @Override
    public UsersEntity getUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UsersEntity> optionalUsersEntity = usersRepository.findByUsername(username);
        if (optionalUsersEntity.isPresent()) {
            return optionalUsersEntity.get();
        } else throw new UsernameNotFoundException("Пользователь с данным именем не найден");
    }

    @Override
    public UsersEntity authentication(String mail, String name, String surname) {
        return usersRepository.findByUsername(mail).orElseGet(
                () -> new UsersEntity(name, surname, "", mail, rolesRepository.getByRole("USER"))
        );
    }

}
