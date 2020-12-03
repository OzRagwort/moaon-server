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
    List<Videos> findByChannelId(@Param("channels") Channels channels);

    @Query("SELECT p FROM Videos p WHERE p.channels = :channels")
    List<Videos> findByChannelId(@Param("channels") Channels channels, Pageable pageable);

    @Query(value = "SELECT VIDEOS.* FROM VIDEOS, CHANNELS WHERE CHANNELS.CHANNELS_IDX = VIDEOS.CHANNELS_IDX and CHANNELS.CHANNEL_ID = ':channels' Order by rand() LIMIT :count", nativeQuery = true)
    List<Videos> findRandByChannelId(@Param("channels") Channels channels, @Param("count") int count);

    @Query("SELECT p FROM Videos p WHERE p.channels.categories = :categories")
    List<Videos> findByCategoryIdx(@Param("categories") Categories categories);

    @Query("SELECT p FROM Videos p WHERE p.channels.categories = :categories")
    List<Videos> findByCategoryIdx(@Param("categories") Categories categories, Pageable pageable);

    @Query(value = "SELECT VIDEOS.* FROM VIDEOS, CHANNELS WHERE CHANNELS.CHANNELS_IDX = VIDEOS.CHANNELS_IDX and CHANNELS.CATEGORIES_IDX = :categories Order by rand() LIMIT :count", nativeQuery = true)
    List<Videos> findRandByCategoryIdx(@Param("categories") Long categories, @Param("count") int count);

}
