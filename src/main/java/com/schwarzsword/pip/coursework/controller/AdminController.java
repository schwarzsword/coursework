package com.schwarzsword.pip.coursework.controller;

import com.schwarzsword.pip.coursework.entity.UsersEntity;
import com.schwarzsword.pip.coursework.entity.WalletEntity;
import com.schwarzsword.pip.coursework.service.AdminService;
import com.schwarzsword.pip.coursework.service.JsonService;
import com.schwarzsword.pip.coursework.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/admin")
public class AdminController {
    final private
    JsonService jsonService;
    final private
    AdminService adminService;
    final private
    RegistrationService registrationService;

    @Autowired
    public AdminController(JsonService jsonService, AdminService adminService, RegistrationService registrationService) {
        this.jsonService = jsonService;
        this.adminService = adminService;
        this.registrationService = registrationService;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ResponseEntity delete(@RequestParam String lotId) {
        try {
            adminService.deleteLot(lotId);
            return ResponseEntity.ok(true);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/ban", method = RequestMethod.GET)
    public ResponseEntity ban(@RequestParam String username) {
        try {
            final UsersEntity user = registrationService.getUserByUsername(username);
            final UsersEntity banUser = adminService.banUser(user);
            return ResponseEntity.ok(jsonService.toJson(banUser));
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/unban", method = RequestMethod.GET)
    public ResponseEntity unBan(@RequestParam String username) {
        try {
            final UsersEntity user = registrationService.getUserByUsername(username);
            final UsersEntity unbanUser = adminService.unbanUser(user);
            return ResponseEntity.ok(jsonService.toJson(unbanUser));
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/addrole", method = RequestMethod.GET)
    public ResponseEntity addRole(@RequestParam String username, @RequestParam String role) {
        try {
            final UsersEntity user = registrationService.getUserByUsername(username);
            final UsersEntity usersEntity = adminService.addRole(user, role);
            return ResponseEntity.ok(jsonService.toJson(usersEntity));
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/deleterole", method = RequestMethod.GET)
    public ResponseEntity deleteRole(@RequestParam String username, @RequestParam String role) {
        try {
        final UsersEntity user = registrationService.getUserByUsername(username);
        final UsersEntity usersEntity = adminService.removeRole(user, role);
        return ResponseEntity.ok(jsonService.toJson(usersEntity));
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/banned", method = RequestMethod.GET)
    public ResponseEntity showBanned() {
        return ResponseEntity.ok(jsonService.toJson(adminService.showBanned()));
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/money", method = RequestMethod.GET)
    public ResponseEntity addMoney(String username, String value){
        UsersEntity user = registrationService.getUserByUsername(username);
        Long parseLong = Long.parseLong(value);
        WalletEntity wallet = adminService.addMoney(parseLong, user);
        return ResponseEntity.ok(jsonService.toJson(wallet.getBalance()));
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/normal", method = RequestMethod.GET)
    public ResponseEntity showNormal() {
        return ResponseEntity.ok(jsonService.toJson(adminService.showNormal()));
    }

}
