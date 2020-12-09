package com.ozragwort.moaon.springboot.domain.videos;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideosRepository extends JpaRepository<Videos, Long> {

    // find all
    @Query("SELECT p FROM Videos p ORDER BY p.idx DESC")
    List<Videos> findAllDesc();

    // find ID
    @Query("SELECT p FROM Videos p WHERE p.idx = :idx")
    Videos findOne(@Param("idx") Long idx);

    @Query("SELECT p FROM Videos p WHERE p.videoId = :videoId")
    Videos findByVideoId(@Param("videoId") String videoId);

    @Query("SELECT p FROM Videos p WHERE p.channels = :channels")
    List<Videos> findByChannelId(@Param("channels") Channels channels);

    @Query("SELECT p FROM Videos p WHERE p.channels = :channels")
    List<Videos> findByChannelId(@Param("channels") Channels channels, Pageable pageable);

    @Query("SELECT p FROM Videos p WHERE p.channels.categories = :categories")
    List<Videos> findByCategoryIdx(@Param("categories") Categories categories);

    @Query("SELECT p FROM Videos p WHERE p.channels.categories = :categories")
    List<Videos> findByCategoryIdx(@Param("categories") Categories categories, Pageable pageable);

    // find random(native)
    @Query(value = "SELECT videos.* FROM moaon.videos, moaon.channels WHERE moaon.channels.channels_idx = moaon.videos.channels_idx and moaon.channels.channels_idx = :channels Order by rand() LIMIT :count", nativeQuery = true)
    List<Videos> findRandByChannelId(@Param("channels") Channels channels, @Param("count") int count);

    @Query(value = "SELECT videos.* FROM moaon.videos, moaon.channels WHERE moaon.channels.channels_idx = moaon.videos.channels_idx and moaon.channels.categories_idx = :categories Order by rand() LIMIT :count", nativeQuery = true)
    List<Videos> findRandByCategoryIdx(@Param("categories") Categories categories, @Param("count") int count);

    // find Sorting
    @Query("SELECT p FROM Videos p WHERE p.channels = :channels ORDER BY p.videoPublishedDate ASC")
    List<Videos> findByChannelIdSortDateAsc(@Param("channels") Channels channels, Pageable pageable);

    @Query("SELECT p FROM Videos p WHERE p.channels = :channels ORDER BY p.videoPublishedDate DESC")
    List<Videos> findByChannelIdSortDateDesc(@Param("channels") Channels channels, Pageable pageable);

}
