package com.ozragwort.moaon.springboot.domain.videos;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideosRepository extends JpaRepository<Videos, Long> {

    @Query("SELECT p FROM Videos p ORDER BY p.idx DESC")
    List<Videos> findAllDesc();

    @Query("SELECT p FROM Videos p WHERE p.idx = :idx")
    Videos findOne(@Param("idx") Long idx);

    @Query("SELECT p FROM Videos p WHERE p.videoId = :videoId")
    Videos findByVideoId(@Param("videoId") String videoId);

    @Query("SELECT p FROM Videos p WHERE p.channels = :channels")
    List<Videos> findByChannelId(@Param("channels") Channels channels, Pageable pageable);

    @Query("SELECT p FROM Videos p WHERE p.channels.categories = :categories")
    List<Videos> findByCategoryIdx(@Param("categories") Categories categories, Pageable pageable);

}
