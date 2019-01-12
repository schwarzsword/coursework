package com.schwarzsword.pip.coursework.config;

import com.schwarzsword.pip.coursework.entity.UsersEntity;
import com.schwarzsword.pip.coursework.repository.RolesRepository;
import com.schwarzsword.pip.coursework.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableOAuth2Sso
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RolesRepository rolesRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("/login/google").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }

    @Bean
    public PrincipalExtractor principalExtractor(UsersRepository usersRepository) {
        return map -> {
            String mail = (String) map.get("email");
            UsersEntity usersEntity = usersRepository.findByUsername(mail).orElseGet(
                    () -> {
                        String name = (String) map.get("given_name");
                        String surname = (String) map.get("family_name");
                        return new UsersEntity(name, surname, "", mail, rolesRepository.getByRole("USER"));
                    }
            );
            return usersEntity;
        };
    }
}