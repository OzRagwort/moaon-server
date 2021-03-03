package com.ozragwort.moaon.springboot.domain.videos;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
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

    @Query("SELECT p FROM Videos p WHERE p.channels IN (:channels)")
    List<Videos> findByChannelId(@Param("channels") List<Channels> channels, Pageable pageable);

    @Query("SELECT p FROM Videos p WHERE p.channels IN (:channels) ORDER BY RAND()")
    List<Videos> findRandByChannelId(@Param("channels") List<Channels> channels, Pageable pageable);

    @Query("SELECT p FROM Videos p WHERE p.channels.categories IN (:categories)")
    List<Videos> findByCategoryIdx(@Param("categories") List<Categories> categories, Pageable pageable);

    @Query("SELECT p FROM Videos p WHERE p.channels.categories IN (:categories) ORDER BY RAND()")
    List<Videos> findRandByCategoryIdx(@Param("categories") List<Categories> categories, Pageable pageable);

    // 특정 tag를 가진 category의 비디오 가져오기
    @Query("SELECT p FROM Videos p WHERE p.channels.categories.idx IN (:categoryIdx) and p.idx IN (select distinct p.idx from p.tags WHERE :keywords member p.tags)")
    List<Videos> findTagsByKeyword(@Param("keywords") String keywords, @Param("categoryIdx") List<Long> categoryIdx, Pageable pageable);

    @Query("SELECT p FROM Videos p WHERE p.channels.categories.idx IN (:categoryIdx) and p.idx IN (select distinct p.idx from p.tags WHERE :keywords member p.tags) ORDER BY RAND()")
    List<Videos> findRandTagsByKeyword(@Param("keywords") String keywords, @Param("categoryIdx") List<Long> categoryIdx, Pageable pageable);

    @Query("SELECT DISTINCT t FROM Videos p join p.tags t WHERE p.channels.channelId = :channelId GROUP BY t ORDER BY count(t) DESC")
    List<String> getTagsByChannelId(@Param("channelId") String channelId, Pageable pageable);

    @Query("SELECT p FROM Videos p WHERE p.videoPublishedDate > :time and p.channels.categories.idx IN (:categoryIdx)")
    List<Videos> findByPublishedDate(@Param("time") LocalDateTime time, @Param("categoryIdx") List<Long> categoryIdx, Pageable pageable);

    @Query("SELECT p FROM Videos p WHERE p.videoPublishedDate > :time and p.channels.categories.idx IN (:categoryIdx) ORDER BY RAND()")
    List<Videos> findRandByPublishedDate(@Param("time") LocalDateTime time, @Param("categoryIdx") List<Long> categoryIdx, Pageable pageable);

    @Query("SELECT avg(p.score) FROM Videos p WHERE p.channels.categories.idx IN (:categoryIdx)")
    double getScoreAvg(@Param("categoryIdx") List<Long> categoryIdx);

    @Query("SELECT avg(p.score) FROM Videos p WHERE p.channels.channelId = :channelId GROUP BY p.channels.channelId")
    double getScoreAvgByChannelId(@Param("channelId") String channelId);

    @Query("SELECT p FROM Videos p WHERE p.score >= :score and p.channels.categories.idx IN (:categoryIdx)")
    List<Videos> findByScore(@Param("score") double score, @Param("categoryIdx") List<Long> categoryIdx, Pageable pageable);

    @Query("SELECT p FROM Videos p WHERE p.score >= :score and p.channels.categories.idx IN (:categoryIdx) ORDER BY RAND()")
    List<Videos> findRandByScore(@Param("score") double score, @Param("categoryIdx") List<Long> categoryIdx, Pageable pageable);

}
