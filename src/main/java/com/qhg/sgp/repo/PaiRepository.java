package com.qhg.sgp.repo;

import com.qhg.bk.model.RealEstate;
import com.qhg.sgp.model.Pai;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaiRepository extends JpaRepository<Pai, Integer> {
    <S extends Pai> S save(S entity);

    <S extends Pai> List<S> saveAll(Iterable<S> entities);

    Pai findPaiByLibraryIdAndType(Integer libraryId, Integer type);
}
