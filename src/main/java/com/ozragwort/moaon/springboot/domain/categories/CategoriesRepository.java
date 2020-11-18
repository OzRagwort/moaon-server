package com.ozragwort.moaon.springboot.domain.categories;

import com.ozragwort.moaon.springboot.domain.channels.Channels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {

    @Query("SELECT p FROM Categories p ORDER BY p.idx DESC")
    List<Categories> findAllDesc();

    @Query("SELECT p FROM Categories p WHERE p.idx = :idx")
    Categories findOne(@Param("idx") Long idx);

}
