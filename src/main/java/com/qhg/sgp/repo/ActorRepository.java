package com.qhg.sgp.repo;

import com.qhg.sgp.model.Actor;
import com.qhg.sgp.model.Anchors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Integer>, JpaSpecificationExecutor<Actor> {
    <S extends Actor> S save(S entity);

    <S extends Actor> List<S> saveAll(Iterable<S> entities);

    Actor findByName(String name);
}
