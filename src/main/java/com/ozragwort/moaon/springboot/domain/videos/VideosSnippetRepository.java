package com.ozragwort.moaon.springboot.domain.videos;

import com.ozragwort.moaon.springboot.domain.channels.Channels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideosSnippetRepository extends JpaRepository<VideosSnippet, Long>, JpaSpecificationExecutor<VideosSnippet> {

    Optional<List<VideosSnippet>> findAllByChannels(Channels channels);

    Optional<VideosSnippet> findByVideoId(String videoId);

    @Query(value =
            "SELECT DISTINCT(videos_tags_tags) " +
            "FROM videos_tags " +
            "WHERE videos_snippet_idx IN (SELECT videos_snippet_idx FROM videos_snippet WHERE channels_idx = :channelsIdx) " +
            "GROUP BY videos_tags_tags " +
            "ORDER BY COUNT(videos_tags_tags) DESC", nativeQuery = true)
    Optional<List<String>> customFindTagsByChannels(@Param("channelsIdx") Long channelsIdx);

}
