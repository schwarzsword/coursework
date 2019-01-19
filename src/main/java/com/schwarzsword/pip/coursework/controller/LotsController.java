package com.schwarzsword.pip.coursework.controller;

import com.schwarzsword.pip.coursework.entity.DealEntity;
import com.schwarzsword.pip.coursework.entity.LotEntity;
import com.schwarzsword.pip.coursework.entity.UsersEntity;
import com.schwarzsword.pip.coursework.service.JsonService;
import com.schwarzsword.pip.coursework.service.LotsService;
import com.schwarzsword.pip.coursework.service.RegistrationService;
import com.schwarzsword.pip.coursework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/lots")
public class LotsController {
    private final
    UserService userService;
    final private
    LotsService lotsService;
    final private
    JsonService jsonService;
    final private
    RegistrationService registrationService;

    @Autowired
    public LotsController(LotsService lotsService, JsonService jsonService, RegistrationService registrationService, UserService userService) {
        this.lotsService = lotsService;
        this.jsonService = jsonService;
        this.registrationService = registrationService;
        this.userService = userService;
    }

    @RequestMapping(value = "/available", method = RequestMethod.GET)
    public ResponseEntity showAvailableLots() {
        return ResponseEntity.ok(jsonService.toJson(lotsService.findAvailableLots()));
    }

    @RequestMapping(value = "/expert", method = RequestMethod.GET)
    @Secured("ROLE_EXPERT")
    public ResponseEntity showToExpert() {
        return ResponseEntity.ok(jsonService.toJson(lotsService.findForExpert()));
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity getLot(@RequestParam String lotId) {
        try {

            return ResponseEntity.ok(jsonService.toJson(lotsService.findLotById(lotId)));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @RequestMapping(value = "/selled", method = RequestMethod.GET)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity showSold(@AuthenticationPrincipal User user) {
        UsersEntity usersEntity = registrationService.getUserByUsername(user.getUsername());
        return ResponseEntity.ok(jsonService.toJson(lotsService.findSoldLotsBySellersUser(usersEntity)));
    }

    @RequestMapping(value = "/selling", method = RequestMethod.GET)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity showSelling(@AuthenticationPrincipal User user) {
        UsersEntity usersEntity = registrationService.getUserByUsername(user.getUsername());
        return ResponseEntity.ok(jsonService.toJson(lotsService.findSellingLotsBySellersUser(usersEntity)));
    }

    @RequestMapping(value = "/bought", method = RequestMethod.GET)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity showOwned(@AuthenticationPrincipal User user) {
        UsersEntity usersEntity = registrationService.getUserByUsername(user.getUsername());
        return ResponseEntity.ok(jsonService.toJson(lotsService.findOwnedPaintings(usersEntity)));
    }
    @RequestMapping(value = "/owned", method = RequestMethod.GET)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity showBought(@AuthenticationPrincipal User user) {
        UsersEntity usersEntity = registrationService.getUserByUsername(user.getUsername());
        return ResponseEntity.ok(jsonService.toJson(lotsService.findBoughtLots(usersEntity)));
    }

    @RequestMapping(value = "/similarAuthor", method = RequestMethod.GET)
    public ResponseEntity showSimilarByAuthor(@RequestParam String lotId) {
        try {
            LotEntity lot = lotsService.findLotById(lotId);
            return ResponseEntity.ok(jsonService.toJson(lotsService.findSimilarByAuthor(lot)));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @RequestMapping(value = "/similarGenre", method = RequestMethod.GET)
    public ResponseEntity showSimilarByGenre(@RequestParam String lotId) {
        try {
            LotEntity lot = lotsService.findLotById(lotId);
            return ResponseEntity.ok(jsonService.toJson(lotsService.findSimilarByGenre(lot)));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @RequestMapping(value = "/similarTech", method = RequestMethod.GET)
    public ResponseEntity showSimilarByTechnique(@RequestParam String lotId) {
        try {
            LotEntity lot = lotsService.findLotById(lotId);
            return ResponseEntity.ok(jsonService.toJson(lotsService.findSimilarByTechnique(lot)));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity showHistory(@AuthenticationPrincipal User user) {
        UsersEntity user1 = registrationService.getUserByUsername(user.getUsername());
        List<DealEntity> dealEntities = userService.showHistory(user1);
        return ResponseEntity.ok(jsonService.toJson(dealEntities));
    }
}
