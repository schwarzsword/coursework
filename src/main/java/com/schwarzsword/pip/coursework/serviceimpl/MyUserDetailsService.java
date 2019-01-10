package com.schwarzsword.pip.coursework.serviceimpl;

import com.schwarzsword.pip.coursework.entity.UsersEntity;
import com.schwarzsword.pip.coursework.service.RegistrationService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {


    @Resource
    private RegistrationService registrationService;

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UsersEntity usersEntity = registrationService.getUserByUsername(username);

        return new User(usersEntity.getUsername(), usersEntity.getPassword(), getGrantedAuthorities(usersEntity));
    }

    private List<GrantedAuthority> getGrantedAuthorities(UsersEntity usersEntity) {


        List<GrantedAuthority> authorities = new ArrayList<>();

        usersEntity.getRoles().forEach(
                e -> {
                    authorities.add(new SimpleGrantedAuthority(e.getRole()));
                }
        );


        return authorities;

    }
}