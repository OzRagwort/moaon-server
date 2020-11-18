package com.ozragwort.moaon.springboot.domain.categories;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ozragwort.moaon.springboot.domain.BaseTimeEntity;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Categories extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categories_idx")
    private Long idx;

    private String categoryName;

    @Builder
    public Categories(String categoryName) {
        this.categoryName = categoryName;
    }

    public void update(String categoryName) {
        this.categoryName = categoryName;
    }

}
