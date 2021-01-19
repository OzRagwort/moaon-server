package com.ozragwort.moaon.springboot.domain.videos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideosTagsPopularityRepository extends JpaRepository<VideosTagsPopularity, Long> {

    @Query("SELECT p FROM VideosTagsPopularity p WHERE p.categoryId = :categoryId")
    List<VideosTagsPopularity> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT p FROM VideosTagsPopularity p WHERE p.tags = :tags")
    List<VideosTagsPopularity> findByTags(@Param("tags") String tags);

}
