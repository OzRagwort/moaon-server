package com.ozragwort.moaon.springboot.domain.videos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideosRelationsRepository extends JpaRepository<VideosRelations, Long> {

    @Query("SELECT p FROM VideosRelations p WHERE p.idx = :idx")
    VideosRelations findOne(@Param("idx") Long idx);

    @Query("SELECT p FROM VideosRelations p WHERE p.videoId = :videoId")
    List<VideosRelations> findByVideoId(@Param("videoId") String videoId);

}
