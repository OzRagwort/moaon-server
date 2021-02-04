package com.ozragwort.moaon.springboot.domain.videos;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideosQualityRepository extends JpaRepository<VideosQuality, Long> {

    @Query("SELECT p FROM VideosQuality p WHERE p.videos.videoId = :videoId")
    VideosQuality findByVideoId(@Param("videoId") String videoId);

    @Query("SELECT avg(p.score) FROM VideosQuality p ")
    double getScoreAvg();

    @Query("SELECT avg(p.score) FROM VideosQuality p WHERE p.channelId = :channelId GROUP BY p.channelId")
    double getScoreAvgByChannelId(@Param("channelId") String channelId);

    @Query("SELECT p FROM VideosQuality p WHERE p.score >= :score")
    List<VideosQuality> findByScore(@Param("score") double score, Pageable pageable);

    // test 필요
    @Query(value = "SELECT * FROM videos_quality WHERE videos_quality_score >= :score ORDER BY rand() LIMIT :count", nativeQuery = true)
    List<VideosQuality> findOverAvgRandByScore(@Param("score") double score, @Param("count") int count);

}
