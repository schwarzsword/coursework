package com.schwarzsword.pip.coursework.security.service;

import com.schwarzsword.pip.coursework.entity.UsersEntity;
import com.schwarzsword.pip.coursework.service.RegistrationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    Logger log = LogManager.getLogger(MyUserDetailsService.class);


    private final RegistrationService registrationService;

    @Autowired
    public MyUserDetailsService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {



        UsersEntity usersEntity = registrationService.getUserByUsername(username);

        if(usersEntity==null){
            log.info("User not found");
            throw new UsernameNotFoundException("Username not found");
        }

        return new User(usersEntity.getUsername(), usersEntity.getPassword(), getGrantedAuthorities(usersEntity));
    }

    private List<GrantedAuthority> getGrantedAuthorities(UsersEntity usersEntity) {

        log.info("User was found " + usersEntity.getUsername());




        List<GrantedAuthority> authorities = new ArrayList<>();

        usersEntity.getRoles().forEach(
                e -> {
                    log.info(e.getRole());
                    authorities.add(new SimpleGrantedAuthority(e.getRole()));
                }
        );
        log.info("Count of authorities of user:"+authorities.size());

        return authorities;

    }
}