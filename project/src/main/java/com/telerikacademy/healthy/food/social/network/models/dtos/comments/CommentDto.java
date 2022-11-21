package com.telerikacademy.healthy.food.social.network.models.dtos.comments;

import javax.validation.constraints.NotBlank;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;

public class CommentDto {
    @NotBlank(message = COMMENT_MESSAGE_ERROR)
    private String comment;

    public CommentDto() {
        // Empty constructor
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
