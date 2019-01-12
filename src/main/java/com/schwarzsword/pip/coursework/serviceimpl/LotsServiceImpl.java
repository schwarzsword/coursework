package com.schwarzsword.pip.coursework.serviceimpl;

import com.schwarzsword.pip.coursework.entity.*;
import com.schwarzsword.pip.coursework.exceptions.IllegalCertificateException;
import com.schwarzsword.pip.coursework.repository.EndDateRepository;
import com.schwarzsword.pip.coursework.repository.LotRepository;
import com.schwarzsword.pip.coursework.repository.PaintingRepository;
import com.schwarzsword.pip.coursework.service.LotsService;
import com.schwarzsword.pip.coursework.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("lotsService")
public class LotsServiceImpl implements LotsService {
    final private
    EndDateRepository endDateRepository;

    final private
    LotRepository lotRepository;

    final private
    RegistrationService registrationService;

    final private PaintingRepository paintingRepository;

    @Autowired
    public LotsServiceImpl(PaintingRepository paintingRepository, EndDateRepository endDateRepository, LotRepository lotRepository, RegistrationService registrationService) {
        this.endDateRepository = endDateRepository;
        this.lotRepository = lotRepository;
        this.registrationService = registrationService;
        this.paintingRepository = paintingRepository;
    }

    @Override
    public List<LotEntity> findAvailableLots() {

        return lotRepository.findAllByState("on market");
    }

    @Override
    public LotEntity findLotById(String id) throws NoSuchElementException {
        LotEntity lot;
        Optional<LotEntity> tmp = lotRepository.findById(Integer.parseInt(id));
        if (tmp.isPresent()) {
            lot = tmp.get();
        } else throw new NoSuchElementException();
        return lot;
    }

    @Override
    public List<LotEntity> findOwnedLotsByCustomersUser(UsersEntity user) {
        return user.getDealsById()
                .stream()
                .map(
                        DealEntity::getEndDateBySoldDate)
                .map(
                        EndDateEntity::getLotByLot
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<LotEntity> findSoldLotsBySellersUser(UsersEntity user) {
        return user.getLotsById().stream().filter(
                lotEntity -> lotEntity
                        .getState()
                        .equals("sold"))
                .collect(Collectors.toList()
                );
    }

    @Override
    public List<LotEntity> findSellingLotsBySellersUser(UsersEntity user) {
        return user.getLotsById().stream().filter(
                lotEntity -> lotEntity
                        .getState()
                        .equals("on market"))
                .collect(Collectors.toList()
                );
    }

    @Transactional
    @Override
    public LotEntity addLot(PaintingEntity painting, Long startPrice, UsersEntity user, Integer policy) throws IllegalCertificateException {
        findAvailableLots()
                .stream()
                .map(LotEntity::getPaintingByPainting)
                .forEach(e -> {
                    if (e.getCertificateByCertificate()
                            .equals(painting.getCertificateByCertificate()))
                        throw new IllegalCertificateException("Лот с данным сертификатом уже выставлен");
                });
        LotEntity lot = new LotEntity(painting, startPrice, user, policy);
        if (painting.getCertificateByCertificate() == null) {
            lot.setState("on expert verification");
        }
        lotRepository.save(lot);
        EndDateEntity endDateEntity = new EndDateEntity(lot);
        paintingRepository.save(painting);
        endDateRepository.save(endDateEntity);
        lot.setEndDateById(endDateEntity);
        if (user.getLotsById() != null) {
            List<LotEntity> lotEntities = new ArrayList<>(user.getLotsById());
            lotEntities.add(lot);
            user.setLotsById(lotEntities);
        } else {
            ArrayList<LotEntity> list = new ArrayList<>();
            list.add(lot);
            user.setLotsById(list);
        }
        return lot;
    }

    @Override
    public List<LotEntity> findSimilarByTechnique(LotEntity lot) throws NoSuchElementException {
        String tech = lot.getPaintingByPainting().getTechnique();
        return findAvailableLots()
                .stream()
                .filter(
                        lotEntity ->
                                lotEntity
                                        .getPaintingByPainting()
                                        .getTechnique()
                                        .equals(tech)
                )
                .filter(e -> !e.equals(lot))
                .collect(Collectors.toList());
    }

    @Override
    public List<LotEntity> findSimilarByGenre(LotEntity lot) throws NoSuchElementException {
        String genre = lot.getPaintingByPainting().getTechnique();
        return findAvailableLots()
                .stream()
                .filter(
                        lotEntity ->
                                lotEntity
                                        .getPaintingByPainting()
                                        .getGenre()
                                        .equals(genre)
                )
                .filter(e -> !e.equals(lot))
                .collect(Collectors.toList());
    }

    @Override
    public List<LotEntity> findSimilarByAuthor(LotEntity lot) throws NoSuchElementException {
        String author = lot.getPaintingByPainting().getTechnique();
        return findAvailableLots()
                .stream()
                .filter(
                        lotEntity ->
                                lotEntity
                                        .getPaintingByPainting()
                                        .getAuthor()
                                        .equals(author)
                )
                .filter(e -> !e.equals(lot))
                .collect(Collectors.toList());
    }

    @Override
    public List<LotEntity> findForExpert() {
        return lotRepository.findAllByState("on expert verification");
    }
}
