package com.qhg.bk.dao;

import com.qhg.bk.model.RealEstate;
import com.qhg.bk.model.XiaoquInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RealEstateRepository extends JpaRepository<RealEstate, Long> {
    <S extends RealEstate> S save(S entity);

    <S extends RealEstate> List<S> saveAll(Iterable<S> entities);

}
