//package com.schwarzsword.pip.coursework;
//
//import com.schwarzsword.pip.coursework.bot.BotBean;
//import com.schwarzsword.pip.coursework.entity.EndDateEntity;
//import com.schwarzsword.pip.coursework.entity.LotEntity;
//import com.schwarzsword.pip.coursework.entity.PaintingEntity;
//import com.schwarzsword.pip.coursework.entity.UsersEntity;
//import com.schwarzsword.pip.coursework.exceptions.NotEnoughMoneyException;
//import com.schwarzsword.pip.coursework.repository.EndDateRepository;
//import com.schwarzsword.pip.coursework.repository.RolesRepository;
//import com.schwarzsword.pip.coursework.repository.UsersRepository;
//import com.schwarzsword.pip.coursework.service.*;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class CourseworkApplicationTests {
//    private Logger log = LoggerFactory.getLogger(CourseworkApplicationTests.class);
//    @Autowired
//    EmailService emailService;
//    @Autowired
//    RegistrationService registrationService;
//    @Autowired
//    UsersRepository usersRepository;
//    @Autowired
//    RolesRepository rolesRepository;
//    @Autowired
//    AdminService adminService;
//    @Autowired
//    UserService userService;
//    @Autowired
//    ExpertService expertService;
//    @Autowired
//    LotsService lotsService;
//    @Autowired
//    BotBean bot;
//    @Autowired
//    EndDateRepository endDateRepository;
//
//    @Before
//    public void prepare() {
//        UsersEntity admin = registrationService.signUp("Kirill", "Black",  "123", "schwarsword@gmail.com");
//        admin.addRole(rolesRepository.getByRole("ADMIN"));
//        userService.addMoney(100500L, admin);
//        UsersEntity commonUser = registrationService.signUp("Kirill", "Black",  "123", "schwarzkv@gmail.com");
//        userService.addMoney(50000L, commonUser);
//        UsersEntity expert = registrationService.signUp("Kirill", "Black",  "123", "123");
//        expert.addRole(rolesRepository.getByRole("EXPERT"));
//        PaintingEntity painting1 = userService.downloadPainting("qwe1", "qwe", "qwe", "qwe", "qwe", "qwe");
//        PaintingEntity painting2 = userService.downloadPainting("qwe2", "qwe", "qwe", "qwe", "qwe", "qwe");
//        PaintingEntity painting3 = userService.downloadPainting("qwe3", "qwe", "qwe", "qwe", "qwe", "qwe");
//        lotsService.addLot(painting1, 100L, usersRepository.findByUsername("schwarzkv@gmail.com").get(), 3);
//        lotsService.addLot(painting2, 100L, usersRepository.findByUsername("schwarzkv@gmail.com").get(), 3);
//        lotsService.addLot(painting3, 100L, usersRepository.findByUsername("schwarzkv@gmail.com").get(), 3);
//
//        lotsService.findForExpert().forEach(e -> expertService.setCertificate(e, usersRepository.findByUsername("123").get()));
//        LotEntity lot = lotsService.findAvailableLots().iterator().next();
//        userService.doBet(lot, 500L, usersRepository.findByUsername("schwarsword@gmail.com").get());
//        EndDateEntity endDateById = lot.getEndDateById();
//        endDateById.setExpectingDate(Timestamp.valueOf(LocalDateTime.now().minusHours(1)));
//        endDateRepository.save(endDateById);
//    }
//
//    @After
//    public void clean() {
//
//    }
//
//    @Transactional
//    @Test
//    public void mailTest() {
//        emailService.sendSimpleMessage("schwarsword@gmail.com", "test", "test...");
//    }
//
//
//    @Test
//    @Transactional
//    public void signUpTest() {
//        registrationService.signUp("Kirill", "Black", "123", "schwarsword@gmail.com");
//        UsersEntity user = usersRepository.findByUsername("schwarsword@gmail.com").get();
//        log.info(user.toString());
//
//    }
//
//    @Test
//    @Transactional
//    public void signInTest() {
//        registrationService.signIn("schwarzkv@gmail.com", "123");
//    }
//
//    @Test
//    @Transactional
//    public void banUnbanTest() {
//        UsersEntity user = usersRepository.findByUsername("schwarzkv@gmail.com").get();
//        adminService.banUser(user);
//        try {
//            signInTest();
//        } catch (UserDeniedAuthorizationException ex) {
//            ex.printStackTrace();
//        }
//        adminService.unbanUser(user);
//        signInTest();
//        log.info("test passed");
//    }
//
//    @Test
//    @Transactional
//    public void addMoneyTest() {
//        UsersEntity user = usersRepository.findByUsername("schwarzkv@gmail.com").get();
//        userService.addMoney(500L, user);
//        log.info(user.getWalletById().getBalance().toString());
//    }
//
//    @Test
//    @Transactional
//    public void addLotTest() {
//        PaintingEntity paintingEntity = userService.downloadPainting("qwe", "qwe", "qwe", "qwe", "qwe", "qwe");
//        lotsService.addLot(paintingEntity, 100L, usersRepository.findByUsername("schwarzkv@gmail.com").get(), 3);
//        log.info(lotsService.findForExpert().stream().map(LotEntity::getId).collect(Collectors.toList()).toString());
//    }
//
//    @Test
//    @Transactional
//    public void setCertificateTest() {
//        lotsService.findForExpert().forEach(e -> expertService.setCertificate(e, usersRepository.findByUsername("123").get()));
//        log.info(lotsService.findAvailableLots().stream().map(LotEntity::getId).collect(Collectors.toList()).toString());
//    }
//
//    @Test
//
//    public void betTest() {
//        LotEntity lot = lotsService.findAvailableLots().iterator().next();
//        userService.doBet(lot, 500L, usersRepository.findByUsername("schwarsword@gmail.com").get());
//        log.info("текущая ставка1 " + lot.getStartPrice().toString());
//        try {
//            userService.doBet(lot, 500L, usersRepository.findByUsername("schwarzkv@gmail.com").get());
//        } catch (NotEnoughMoneyException ex) {
//            log.info(ex.getMessage());
//        }
//        log.info("текущая ставка2 " + lot.getStartPrice().toString());
//        try {
//            userService.doBet(lot, 501L, usersRepository.findByUsername("schwarzkv@gmail.com").get());
//        } catch (NotEnoughMoneyException ex) {
//            log.info(ex.getMessage());
//        }
//        log.info("текущая ставка3 " + lot.getStartPrice().toString());
//        try {
//            userService.doBet(lot, 1000L, usersRepository.findByUsername("123").get());
//        } catch (NotEnoughMoneyException ex) {
//            log.info(ex.getMessage());
//        }
//        log.info("текущая ставка4 " + lot.getStartPrice().toString());
//    }
//
//    @Test
//    @Transactional
//    public void similarTest() {
//        LotEntity lot = lotsService.findAvailableLots().iterator().next();
//        log.info("testlotid = " + lot.getId());
//        List<LotEntity> list1 = lotsService.findSimilarByAuthor(lot);
//        log.info(list1.stream().map(LotEntity::getId).collect(Collectors.toList()).toString());
//        List<LotEntity> list2 = lotsService.findSimilarByGenre(lot);
//        log.info(list2.stream().map(LotEntity::getId).collect(Collectors.toList()).toString());
//        List<LotEntity> list3 = lotsService.findSimilarByTechnique(lot);
//        log.info(list3.stream().map(LotEntity::getId).collect(Collectors.toList()).toString());
//    }
//
//    @Test
//    @Transactional
//    public void botTest() {
//        bot.setResults();
//        UsersEntity schwarzsword = usersRepository.findByUsername("schwarsword@gmail.com").get();
//        UsersEntity schwarzkv = usersRepository.findByUsername("schwarzkv@gmail.com").get();
//        log.info(lotsService.findOwnedLotsByCustomersUser(schwarzsword).stream().map(LotEntity::getId).collect(Collectors.toList()).toString());
//        log.info(lotsService.findSoldLotsBySellersUser(schwarzkv).stream().map(LotEntity::getId).collect(Collectors.toList()).toString());
//    }
//
//}
//
