package com.telerikacademy.healthy.food.social.network.models;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "gender")
@SQLDelete(sql = "update gender set enabled = false where gender_id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "enabled <> false")
public class Gender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gender_id")
    private int id;

    @Column(name = "type")
    private String type;

    public Gender() {
        // Empty constructor
    }

    public Gender(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
