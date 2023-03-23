package com.qhg.sgp.repo;

import com.qhg.sgp.model.PaiLibDetail;
import com.qhg.sgp.model.PaiMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PaiMovieRepository extends JpaRepository<PaiMovie, Integer>, JpaSpecificationExecutor<PaiMovie> {
    <S extends PaiMovie> S save(S entity);

    <S extends PaiMovie> List<S> saveAll(Iterable<S> entities);
}
