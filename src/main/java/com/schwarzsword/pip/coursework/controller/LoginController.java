package com.schwarzsword.pip.coursework.controller;

import com.schwarzsword.pip.coursework.entity.UsersEntity;
import com.schwarzsword.pip.coursework.service.JsonService;
import com.schwarzsword.pip.coursework.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    private final RegistrationService registrationService;

    final private JsonService jsonService;

    @Autowired
    public LoginController(RegistrationService registrationService, JsonService jsonService) {
        this.registrationService = registrationService;
        this.jsonService = jsonService;
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public ResponseEntity signUp(@RequestParam String name, @RequestParam String surname,
                                 @RequestParam String password, @RequestParam String mail) {
        try {

            UsersEntity user = registrationService.signUp(name, surname, password, mail);
        } catch (UserDeniedAuthorizationException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return ResponseEntity.ok(true);
    }

    @RequestMapping(value = "/role", method = RequestMethod.GET)
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_EXPERT"})
    public ResponseEntity getRole(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(jsonService.toJson(registrationService.getUserByUsername(user.getUsername()).getRoles()));
    }
}
