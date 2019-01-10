package com.schwarzsword.pip.coursework.serviceimpl;

import com.schwarzsword.pip.coursework.entity.DealEntity;
import com.schwarzsword.pip.coursework.entity.EndDateEntity;
import com.schwarzsword.pip.coursework.entity.LotEntity;
import com.schwarzsword.pip.coursework.entity.PaintingEntity;
import com.schwarzsword.pip.coursework.exceptions.IllegalCertificateException;
import com.schwarzsword.pip.coursework.repository.EndDateRepository;
import com.schwarzsword.pip.coursework.repository.LotRepository;
import com.schwarzsword.pip.coursework.repository.PaintingRepository;
import com.schwarzsword.pip.coursework.service.LotsService;
import com.schwarzsword.pip.coursework.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<LotEntity> findOwnedLotsByCustomersUsername(String username) {
        return registrationService.getUserByUsername(username).getDealsById()
                .stream()
                .map(
                        DealEntity::getEndDateBySoldDate)
                .map(
                        EndDateEntity::getLotByLot
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<LotEntity> findSelledLotsBySellersUsername(String username) {
        return registrationService.getUserByUsername(username).getLotsById().stream().filter(
                lotEntity -> lotEntity
                        .getState()
                        .equals("sold"))
                .collect(Collectors.toList()
                );
    }

    @Override
    public List<LotEntity> findSellingLotsBySellersUsername(String username) {
        return registrationService.getUserByUsername(username).getLotsById().stream().filter(
                lotEntity -> lotEntity
                        .getState()
                        .equals("on market"))
                .collect(Collectors.toList()
                );
    }

    @Transactional
    @Override
    public LotEntity addLot(Integer paintingId, Long startPrice, String username) throws IllegalCertificateException {
        PaintingEntity painting = paintingRepository.findById(paintingId).get();
        findAvailableLots()
                .stream()
                .map(LotEntity::getPaintingByPainting)
                .forEach(e -> {
                    if (e.getCertificateByCertificate()
                            .equals(painting.getCertificateByCertificate()))
                        throw new IllegalCertificateException("Лот с данным сертификатом уже выставлен");
                });
        LotEntity lot = new LotEntity(painting, startPrice, registrationService.getUserByUsername(username));
        if (painting.getCertificateByCertificate() == null) {
            lot.setState("on expert verification");
        }
        paintingRepository.save(painting);
        lotRepository.save(lot);
        endDateRepository.save(new EndDateEntity(lot));
        return lot;
    }

    @Override
    public List<LotEntity> findSimilarByTechnique(String lotId) throws NoSuchElementException {
        String tech;
        Optional<LotEntity> optionalLotEntity = lotRepository.findById(Integer.parseInt(lotId));
        if (optionalLotEntity.isPresent()) {
            tech = optionalLotEntity.get().getPaintingByPainting().getTechnique();
            return findAvailableLots()
                    .stream()
                    .filter(
                            lotEntity ->
                                    lotEntity
                                            .getPaintingByPainting()
                                            .getTechnique()
                                            .equals(tech)
                    )
                    .collect(Collectors.toList());
        } else throw new NoSuchElementException("Такого лота не существует");
    }

    @Override
    public List<LotEntity> findSimilarByGenre(String lotId) throws NoSuchElementException {
        String genre;
        Optional<LotEntity> optionalLotEntity = lotRepository.findById(Integer.parseInt(lotId));
        if (optionalLotEntity.isPresent()) {
            genre = optionalLotEntity
                    .get()
                    .getPaintingByPainting()
                    .getGenre();
            return findAvailableLots().stream()
                    .filter(
                            lotEntity ->
                                    lotEntity
                                            .getPaintingByPainting()
                                            .getGenre()
                                            .equals(genre)
                    ).collect(Collectors.toList());
        } else throw new NoSuchElementException("Такого лота не существует");
    }

    @Override
    public List<LotEntity> findSimilarByAuthor(String lotId) throws NoSuchElementException {
        String author;
        Optional<LotEntity> optionalLotEntity = lotRepository.findById(Integer.parseInt(lotId));
        if (optionalLotEntity.isPresent()) {
            author = optionalLotEntity
                    .get()
                    .getPaintingByPainting()
                    .getAuthor();
            return findAvailableLots().stream()
                    .filter(
                            lotEntity ->
                                    lotEntity
                                            .getPaintingByPainting()
                                            .getAuthor()
                                            .equals(author)
                    ).collect(Collectors.toList());
        } else throw new NoSuchElementException("Такого лота не существует");
    }

    @Override
    public List<LotEntity> findForExpert() {
        return endDateRepository.findAllByStateIsTrue()
                .stream()
                .map(
                        EndDateEntity::getLotByLot
                )
                .filter(
                        lotEntity -> lotEntity
                                .getState()
                                .equals("on expert verification")
                ).collect(Collectors.toList());
    }
}
