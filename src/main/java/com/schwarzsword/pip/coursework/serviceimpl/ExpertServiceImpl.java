package com.schwarzsword.pip.coursework.serviceimpl;

import com.schwarzsword.pip.coursework.entity.CertificateEntity;
import com.schwarzsword.pip.coursework.entity.LotEntity;
import com.schwarzsword.pip.coursework.entity.PaintingEntity;
import com.schwarzsword.pip.coursework.entity.UsersEntity;
import com.schwarzsword.pip.coursework.repository.CertificateRepository;
import com.schwarzsword.pip.coursework.repository.LotRepository;
import com.schwarzsword.pip.coursework.repository.PaintingRepository;
import com.schwarzsword.pip.coursework.repository.UsersRepository;
import com.schwarzsword.pip.coursework.service.ExpertService;
import com.schwarzsword.pip.coursework.service.LotsService;
import com.schwarzsword.pip.coursework.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service("expertService")
public class ExpertServiceImpl implements ExpertService {
    final private
    LotRepository lotRepository;

    final private
    CertificateRepository certificateRepository;


    final private
    PaintingRepository paintingRepository;

    @Autowired
    public ExpertServiceImpl(LotRepository lotRepository, CertificateRepository certificateRepository, PaintingRepository paintingRepository) {
        this.lotRepository = lotRepository;
        this.certificateRepository = certificateRepository;
        this.paintingRepository = paintingRepository;
    }

    @Override
    @Transactional
    public LotEntity setCertificate(LotEntity lot, UsersEntity user) throws NoSuchElementException, UsernameNotFoundException {
        CertificateEntity certificateEntity = new CertificateEntity(user);
        PaintingEntity paintingEntity = lot.getPaintingByPainting();
        paintingEntity.setCertificateByCertificate(certificateEntity);
        lot.setState("on market");
        certificateRepository.save(certificateEntity);
        paintingRepository.save(paintingEntity);
        lotRepository.save(lot);
        return lot;
    }
}
