package com.qhg.bk.dao;

import com.qhg.bk.model.XiaoquBigInfo;
import com.qhg.bk.model.XiaoquInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XiaoquBigInfoRepository extends JpaRepository<XiaoquBigInfo, Long> {
    <S extends XiaoquBigInfo> S save(S entity);

    <S extends XiaoquBigInfo> List<S> saveAll(Iterable<S> entities);
}
