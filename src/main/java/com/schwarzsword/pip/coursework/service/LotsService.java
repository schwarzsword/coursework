package com.schwarzsword.pip.coursework.service;

import com.schwarzsword.pip.coursework.entity.LotEntity;
import com.schwarzsword.pip.coursework.entity.PaintingEntity;
import com.schwarzsword.pip.coursework.entity.UsersEntity;
import com.schwarzsword.pip.coursework.exceptions.IllegalCertificateException;

import java.util.List;
import java.util.NoSuchElementException;

public interface LotsService {
    List<LotEntity> findAvailableLots();

    LotEntity findLotById(String lotId) throws NoSuchElementException;

    List<LotEntity> findOwnedLotsByCustomersUser(UsersEntity user);

    List<LotEntity> findSoldLotsBySellersUser(UsersEntity user);

    List<LotEntity> findSellingLotsBySellersUser(UsersEntity user);

    LotEntity addLot(PaintingEntity paintingEntity, Long startPrice, UsersEntity user, Integer policy) throws IllegalCertificateException;

    List<LotEntity> findSimilarByTechnique(LotEntity lot);

    List<LotEntity> findSimilarByGenre(LotEntity lot) ;

    List<LotEntity> findSimilarByAuthor(LotEntity lot);

    List<LotEntity> findForExpert();
}
