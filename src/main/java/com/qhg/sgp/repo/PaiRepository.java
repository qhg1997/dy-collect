package com.qhg.sgp.repo;

import com.qhg.bk.model.RealEstate;
import com.qhg.bk.model.Xiaoqu;
import com.qhg.sgp.model.Pai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PaiRepository extends JpaRepository<Pai, Integer>, JpaSpecificationExecutor<Pai> {
    <S extends Pai> S save(S entity);

    <S extends Pai> List<S> saveAll(Iterable<S> entities);

    Pai findPaiByLibraryIdAndType(Integer libraryId, Integer type);
}
