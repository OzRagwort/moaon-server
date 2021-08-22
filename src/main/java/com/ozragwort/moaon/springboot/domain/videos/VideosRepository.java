package com.ozragwort.moaon.springboot.domain.videos;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideosRepository extends JpaRepository<Videos, Long>, JpaSpecificationExecutor<Videos> {

    Optional<List<Videos>> findAllByChannels(Channels channels);

    Optional<Videos> findByVideoId(String videoId);

    @Query("SELECT DISTINCT t FROM Videos p join p.tags t WHERE p.channels = :channels GROUP BY t ORDER BY count(t) DESC")
    Optional<List<String>> getTagsByChannelId(@Param("channels") Channels channels);

    @Query("SELECT DISTINCT t FROM Videos p join p.tags t WHERE p.channels.categories = :categories GROUP BY t ORDER BY count(t) DESC")
    List<String> getTagsByCategoryId(@Param("categories") Categories categories, Pageable pageable);

}
