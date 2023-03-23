package com.qhg.sgp.repo;

import com.qhg.sgp.model.Anchors;
import com.qhg.sgp.model.PaiFilm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AnchorsRepository extends JpaRepository<Anchors, Integer>, JpaSpecificationExecutor<Anchors> {
    <S extends Anchors> S save(S entity);

    <S extends Anchors> List<S> saveAll(Iterable<S> entities);
}
