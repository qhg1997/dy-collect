package com.qhg.sgp.repo;

import com.qhg.sgp.model.PaiFilm;
import com.qhg.sgp.model.PaiMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PaiFilmRepository extends JpaRepository<PaiFilm, Integer>, JpaSpecificationExecutor<PaiFilm> {
    <S extends PaiFilm> S save(S entity);

    <S extends PaiFilm> List<S> saveAll(Iterable<S> entities);
}
