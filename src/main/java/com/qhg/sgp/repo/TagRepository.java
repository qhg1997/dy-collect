package com.qhg.sgp.repo;

import com.qhg.sgp.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    <S extends Tag> S save(S entity);

    <S extends Tag> List<S> saveAll(Iterable<S> entities);

}
