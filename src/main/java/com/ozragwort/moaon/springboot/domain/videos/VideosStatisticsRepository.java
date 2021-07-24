package com.ozragwort.moaon.springboot.domain.videos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideosStatisticsRepository extends JpaRepository<VideosStatistics, Long> {

    Optional<VideosStatistics> findByVideosSnippet(VideosSnippet videosSnippet);

}
