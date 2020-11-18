package com.ozragwort.moaon.springboot.domain.channels;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChannelsRepository extends JpaRepository<Channels, Long> {

    @Query("SELECT p FROM Channels p ORDER BY p.idx DESC")
    List<Channels> findAllDesc();

    @Query("SELECT p FROM Channels p WHERE p.idx = :idx")
    Channels findOne(@Param("idx") Long idx);

    @Query("SELECT p FROM Channels p WHERE p.channelId = :channelId")
    Channels findByChannelId(@Param("channelId") String channelId);

    @Query("SELECT p FROM Channels p WHERE p.categories = :categories")
    List<Channels> findByCategoryIdx(@Param("categories") Categories categories, Pageable pageable);

}
