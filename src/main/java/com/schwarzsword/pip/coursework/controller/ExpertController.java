package com.schwarzsword.pip.coursework.controller;

import com.schwarzsword.pip.coursework.entity.LotEntity;
import com.schwarzsword.pip.coursework.entity.PaintingEntity;
import com.schwarzsword.pip.coursework.entity.UsersEntity;
import com.schwarzsword.pip.coursework.repository.PaintingRepository;
import com.schwarzsword.pip.coursework.service.ExpertService;
import com.schwarzsword.pip.coursework.service.JsonService;
import com.schwarzsword.pip.coursework.service.LotsService;
import com.schwarzsword.pip.coursework.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/expert")
public class ExpertController {
    private final ExpertService expertService;
    private final LotsService lotsService;
    private final RegistrationService registrationService;
    private final JsonService jsonService;
    private final PaintingRepository paintingRepository;

    @Autowired
    public ExpertController(ExpertService expertService, LotsService lotsService, RegistrationService registrationService, JsonService jsonService, PaintingRepository paintingRepository) {
        this.expertService = expertService;
        this.lotsService = lotsService;
        this.registrationService = registrationService;
        this.jsonService = jsonService;
        this.paintingRepository = paintingRepository;
    }

    @Secured("ROLE_EXPERT")
    @RequestMapping(value = "/set", method = RequestMethod.POST)
    public ResponseEntity serCertificate(@AuthenticationPrincipal User user, @RequestParam String lotId) {
        try {
            LotEntity lot = lotsService.findLotById(lotId);
            UsersEntity user1 = registrationService.getUserByUsername(user.getUsername());
            LotEntity lotEntity = expertService.setCertificate(lot, user1);
            return ResponseEntity.ok(jsonService.toJson(lotEntity));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Secured("ROLE_EXPERT")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity deleteInvalidPainting(@RequestParam String paintingId) {
        final Optional<PaintingEntity> paintingEntity = paintingRepository.findById(Integer.parseInt(paintingId));
        if (paintingEntity.isPresent()) {
            expertService.deleteInvalidPainting(paintingEntity.get());
        } else return ResponseEntity.badRequest().body("Такой картины не существует");

        return ResponseEntity.ok().build();
    }
}
