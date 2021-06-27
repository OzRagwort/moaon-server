package com.ozragwort.moaon.springboot.v1.domain.categories;

import com.ozragwort.moaon.springboot.v1.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
