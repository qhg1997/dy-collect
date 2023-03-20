package com.qhg.sgp.repo;

import com.qhg.sgp.model.Columnist;
import com.qhg.sgp.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ColumnistRepository extends JpaRepository<Columnist, Integer> {
    <S extends Columnist> S save(S entity);

    <S extends Columnist> List<S> saveAll(Iterable<S> entities);

}
