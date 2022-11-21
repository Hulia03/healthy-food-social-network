package com.telerikacademy.healthy.food.social.network.models.dtos.mappers;

import com.telerikacademy.healthy.food.social.network.models.UserDetails;

import java.util.stream.Stream;

public class Mapper {
    public static <T> T getNotNull(T a, T b) {
        if (b == null) {
            return a;
        }
        if (a == null) {
            return b;
        }
        return a.equals(b) ? a : b;
    }

    public static int getNotZero(int a, int b) {
        if (b == 0) {
            return a;
        }

        return b;
    }

    public static boolean isCommentLiked(String username, Stream<UserDetails> likedUsers) {
        return likedUsers.anyMatch(u -> u.getEmail().equals(username));
    }
}
