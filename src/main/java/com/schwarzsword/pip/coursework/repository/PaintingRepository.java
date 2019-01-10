package com.schwarzsword.pip.coursework.repository;

import com.schwarzsword.pip.coursework.entity.CertificateEntity;
import com.schwarzsword.pip.coursework.entity.PaintingEntity;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.springframework.data.repository.CrudRepository;

public interface PaintingRepository extends CrudRepository<PaintingEntity, Integer> {
    boolean existsByCertificateByCertificate(CertificateEntity certificateEntity);
    PaintingEntity findByAuthorAndName(String author, String name);

}
