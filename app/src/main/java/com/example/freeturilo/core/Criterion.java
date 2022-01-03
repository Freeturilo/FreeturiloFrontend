package com.example.freeturilo.core;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.freeturilo.R;

/**
 * Criterion of {@code Route} calculating.
 * Values of this enum represent different optimization techniques used when
 * calculating routes.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #COST
 * @see #TIME
 * @see #HYBRID
 * @see #getCriterionText
 * @see Route
 * @see RouteParameters
 */
public enum Criterion {
    /**
     * Represents optimization of cost when calculating a route.
     */
    COST,
    /**
     * Represents optimization of time when calculating a route.
     */
    TIME,
    /**
     * Represents balanced optimization of both time and cost
     * when calculating a route.
     */
    HYBRID;

    /**
     * Gets the resource identifier of the text representation of
     * a {@code Criterion}.
     * @param criterion     a criterion
     * @return              an integer identifying the text representing
     *                      the criterion
     */
    private static int getCriterionTextId(@NonNull Criterion criterion) {
        switch(criterion){
            case COST:
                return R.string.cost_criterion_text;
            case TIME:
                return R.string.time_criterion_text;
            default:
                return R.string.hybrid_criterion_text;
        }
    }

    /**
     * Gets the text representation of a {@code Criterion}.
     * @param context       the context of the application providing all
     *                      global information
     * @param criterion     a criterion
     * @return              a string representing the criterion
     */
    @NonNull
    public static String getCriterionText(@NonNull Context context, @NonNull Criterion criterion) {
        return context.getString(getCriterionTextId(criterion));
    }
}
