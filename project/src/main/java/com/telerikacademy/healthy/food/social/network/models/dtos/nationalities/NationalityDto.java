package com.telerikacademy.healthy.food.social.network.models.dtos.nationalities;

import javax.persistence.Column;
import javax.validation.constraints.Size;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;

public class NationalityDto {
    @Size(min = NATIONALITY_NAME_MIN_LEN,
            max = NATIONALITY_NAME_MAX_LEN,
            message = NATIONALITY_NAME_MESSAGE_ERROR)
    @Column(name = "nationality")
    private String nationality;

    public NationalityDto() {
        // Empty constructor
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
