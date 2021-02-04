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

    // 미사용
    @Query("SELECT p FROM Videos p WHERE p.channels = :channels")
    List<Videos> findByChannelId(@Param("channels") Channels channels);

    @Query("SELECT p FROM Videos p WHERE p.channels IN (:channels)")
    List<Videos> findByChannelId(@Param("channels") List<Channels> channels, Pageable pageable);

    // 미사용
    @Query("SELECT p FROM Videos p WHERE p.channels.categories = :categories")
    List<Videos> findByCategoryIdx(@Param("categories") Categories categories);

    @Query("SELECT p FROM Videos p WHERE p.channels.categories = :categories")
    List<Videos> findByCategoryIdx(@Param("categories") Categories categories, Pageable pageable);

    // find random(native)
    @Query(value = "SELECT videos.* FROM videos, channels WHERE channels.channels_idx = videos.channels_idx and channels.channels_idx = :channels Order by rand() LIMIT :count", nativeQuery = true)
    List<Videos> findRandByChannelId(@Param("channels") Channels channels, @Param("count") int count);

    @Query(value = "SELECT videos.* FROM videos, channels WHERE channels.channels_idx = videos.channels_idx and channels.categories_idx = :categories Order by rand() LIMIT :count", nativeQuery = true)
    List<Videos> findRandByCategoryIdx(@Param("categories") Categories categories, @Param("count") int count);

    // find Sorting
    @Query("SELECT p FROM Videos p WHERE p.channels IN (:channels)")
    List<Videos> findByChannelIdSort(@Param("channels") List<Channels> channels, Pageable pageable);

//    fulltext search 임시 보류
//    @Query(value = "SELECT * FROM videos WHERE match(video_name, video_description) against(':keyword') LIMIT :count", nativeQuery = true)
//    List<Videos> searchVideos(@Param("keyword") String keyword, @Param("count") int count);

    // 특정 tag를 가진 category의 비디오 가져오기
    @Query("SELECT p FROM Videos p WHERE p.channels.categories.idx = :categoryIdx and p.idx IN (select distinct p.idx from p.tags WHERE :keywords member p.tags)")
    List<Videos> findTagByKeyword(@Param("keywords") String keywords, @Param("categoryIdx") Long categoryIdx, Pageable pageable);

    @Query("SELECT p FROM Videos p WHERE p.videoPublishedDate > :time")
    List<Videos> findByPublishedDate(@Param("time") LocalDateTime time, Pageable pageable);

    @Query("SELECT avg(p.score) FROM Videos p ")
    double getScoreAvg();

    @Query("SELECT avg(p.score) FROM Videos p WHERE p.channels.channelId = :channelId GROUP BY p.channels.channelId")
    double getScoreAvgByChannelId(@Param("channelId") String channelId);

    @Query("SELECT p FROM Videos p WHERE p.score >= :score")
    List<Videos> findByScore(@Param("score") double score, Pageable pageable);

    @Query(value = "SELECT * FROM videos WHERE videos_score >= :score ORDER BY rand() LIMIT :count", nativeQuery = true)
    List<Videos> findOverAvgRandByScore(@Param("score") double score, @Param("count") int count);

}
