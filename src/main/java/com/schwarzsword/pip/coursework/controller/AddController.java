package com.schwarzsword.pip.coursework.controller;

import com.schwarzsword.pip.coursework.entity.LotEntity;
import com.schwarzsword.pip.coursework.entity.PaintingEntity;
import com.schwarzsword.pip.coursework.entity.UsersEntity;
import com.schwarzsword.pip.coursework.repository.PaintingRepository;
import com.schwarzsword.pip.coursework.service.JsonService;
import com.schwarzsword.pip.coursework.service.LotsService;
import com.schwarzsword.pip.coursework.service.RegistrationService;
import com.schwarzsword.pip.coursework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/add")
public class AddController {
    @Autowired
    private
    UserService userService;
    @Autowired
    private
    JsonService jsonService;
    @Autowired
    private
    LotsService lotsService;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private PaintingRepository paintingRepository;

    @Value("${upload.path}")
    String path;

    @RequestMapping(value = "/painting", method = RequestMethod.POST)
    @Secured({"ADMIN", "USER"})
    public ResponseEntity setPainting(@RequestParam String name, @RequestParam String author,
                                      @RequestParam String description, @RequestParam String genre,
                                      @RequestParam String technique, @RequestParam("file") MultipartFile file) {
        File uploadDir = new File(path);
        if (!uploadDir.exists()) uploadDir.mkdir();

        String fileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename();

        try {
            file.transferTo(new File(path + "/" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PaintingEntity paintingEntity = userService.downloadPainting(name, author, description, fileName, genre, technique);
        return ResponseEntity.ok(jsonService.toJson(paintingEntity));
    }

    @RequestMapping(value = "/lot", method = RequestMethod.POST)
    @Secured({"ADMIN", "USER"})
    public ResponseEntity setLot(@AuthenticationPrincipal User user, @RequestParam Long startPrice,
                                 @RequestParam Integer paintingId, @RequestParam Integer policy) {
        UsersEntity usersEntity = registrationService.getUserByUsername(user.getUsername());
        PaintingEntity paintingEntity = paintingRepository.findById(paintingId).get();
        LotEntity lot = lotsService.addLot(paintingEntity, startPrice, usersEntity, policy);
        return ResponseEntity.ok(jsonService.toJson(lot));
    }
}
