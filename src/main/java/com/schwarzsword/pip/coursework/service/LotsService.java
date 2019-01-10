package com.schwarzsword.pip.coursework.service;

import com.schwarzsword.pip.coursework.entity.LotEntity;
import com.schwarzsword.pip.coursework.exceptions.IllegalCertificateException;

import java.util.List;
import java.util.NoSuchElementException;

public interface LotsService {
    List<LotEntity> findAvailableLots();

    LotEntity findLotById(String id) throws NoSuchElementException;

    List<LotEntity> findOwnedLotsByCustomersUsername(String username);

    List<LotEntity> findSelledLotsBySellersUsername(String username);

    List<LotEntity> findSellingLotsBySellersUsername(String username);

    LotEntity addLot(Integer paintingId, Long startPrice, String username) throws IllegalCertificateException;

    List<LotEntity> findSimilarByTechnique(String lotId) throws NoSuchElementException;

    List<LotEntity> findSimilarByGenre(String lotId) throws NoSuchElementException;

    List<LotEntity> findSimilarByAuthor(String lotId) throws NoSuchElementException;

    List<LotEntity> findForExpert();
}
