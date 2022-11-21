package com.telerikacademy.healthy.food.social.network.models;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "categories")
@SQLDelete(sql = "update categories set enabled = false where category_id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "enabled <> false")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int id;

    @Column(name = "name")
    private String category;

    @Column(name = "emoji")
    private String emoji;

    public Category() {
        // Empty constructor
    }

    public Category(int id, String category, String emoji) {
        this.id = id;
        this.category = category;
        this.emoji = emoji;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }
}
