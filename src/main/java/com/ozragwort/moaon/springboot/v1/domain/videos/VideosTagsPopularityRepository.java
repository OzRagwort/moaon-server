package com.ozragwort.moaon.springboot.v1.domain.videos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideosTagsPopularityRepository extends JpaRepository<VideosTagsPopularity, Long> {

    @Query("SELECT p FROM VideosTagsPopularity p WHERE p.categoryId IN (:categoryId)")
    List<VideosTagsPopularity> findByCategoryId(@Param("categoryId") List<Long> categoryId);

    @Query("SELECT p FROM VideosTagsPopularity p WHERE p.categoryId IN (:categoryId) ORDER BY RAND()")
    List<VideosTagsPopularity> findRandByCategoryId(@Param("categoryId") List<Long> categoryId);

    @Query("SELECT p FROM VideosTagsPopularity p WHERE p.tags = :tags")
    List<VideosTagsPopularity> findByTags(@Param("tags") String tags);

    @Query("SELECT p FROM VideosTagsPopularity p WHERE p.tags = :tags and p.categoryId IN (:categoryId)")
    List<VideosTagsPopularity> findByTagsAndCategoryId(@Param("tags") String tags, @Param("categoryId") List<Long> categoryId);

}
