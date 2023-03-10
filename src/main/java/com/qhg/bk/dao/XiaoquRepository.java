package com.qhg.bk.dao;

import com.qhg.bk.model.Xiaoqu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface XiaoquRepository extends JpaRepository<Xiaoqu, Long>, JpaSpecificationExecutor<Xiaoqu> {
    <S extends Xiaoqu> S save(S entity);

    <S extends Xiaoqu> List<S> saveAll(Iterable<S> entities);

}
