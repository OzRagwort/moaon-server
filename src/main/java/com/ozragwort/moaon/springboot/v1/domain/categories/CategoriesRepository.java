package com.ozragwort.moaon.springboot.v1.domain.categories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {

    @Query("SELECT p FROM Categories p ORDER BY p.idx DESC")
    List<Categories> findAllDesc();

    @Query("SELECT p FROM Categories p WHERE p.idx = :idx")
    Categories findOne(@Param("idx") Long idx);

    @Query("SELECT p FROM Categories p WHERE p.idx = :idx")
    List<Categories> findByIdx(@Param("idx") Long idx);

}
