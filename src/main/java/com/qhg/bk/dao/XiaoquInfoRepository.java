package com.qhg.bk.dao;

import com.qhg.bk.model.Xiaoqu;
import com.qhg.bk.model.XiaoquInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XiaoquInfoRepository extends JpaRepository<XiaoquInfo, Long> {
    <S extends XiaoquInfo> S save(S entity);

    <S extends XiaoquInfo> List<S> saveAll(Iterable<S> entities);

    boolean existsByXqId(String xqId);
}
