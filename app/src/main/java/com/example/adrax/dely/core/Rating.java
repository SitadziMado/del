package com.example.adrax.dely.core;

import android.support.annotation.NonNull;

/**
 * Создан Максимом Сунцевым 18.08.2017.
 */

public class Rating {
    public Rating(
            Integer stars,
            @NonNull String feedback
    ) {
        if (stars < MIN_STARS) {
            stars = MIN_STARS;
        } else if (stars > MAX_STARS) {
            stars = MAX_STARS;
        }

        m_stars = stars;
        m_feedback = feedback;
    }

    public Integer getStars() {
        return m_stars;
    }

    public String getFeedback() {
        return m_feedback;
    }

    private static final Integer MIN_STARS = 1;
    private static final Integer MAX_STARS = 5;

    private Integer m_stars;
    private String m_feedback;
}
