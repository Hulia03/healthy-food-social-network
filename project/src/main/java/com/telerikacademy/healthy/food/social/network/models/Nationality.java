package com.telerikacademy.healthy.food.social.network.models;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Size;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;

@Entity
@Table(name = "nationalities")
@SQLDelete(sql = "update nationalities set enabled = false where nationality_id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "enabled <> false")
public class Nationality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nationality_id")
    private int id;

    @Size(min = NATIONALITY_NAME_MIN_LEN,
            max = NATIONALITY_NAME_MAX_LEN,
            message = NATIONALITY_NAME_MESSAGE_ERROR)
    @Column(name = "nationality")
    private String nationality;

    public Nationality() {
        // Empty constructor
    }

    public Nationality(int id, String nationality) {
       this.id = id;
       this.nationality = nationality;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
