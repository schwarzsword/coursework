package com.schwarzsword.pip.coursework.service;

import com.schwarzsword.pip.coursework.entity.UsersEntity;
import com.sun.deploy.security.UserDeclinedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;

public interface RegistrationService {
    UsersEntity signIn(String username, String password) throws UserDeniedAuthorizationException;

    UsersEntity signUp(String name, String surname, String password, String mail)
            throws UserDeclinedException;

    UsersEntity getUserByUsername(String username) throws UsernameNotFoundException;

    UsersEntity authentication(String mail, String name, String surname);
}
