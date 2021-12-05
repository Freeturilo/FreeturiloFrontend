package com.example.freeturilo.core;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.freeturilo.R;

public class CriterionTools {

    @NonNull
    public static String getCriterionText(@NonNull Context context, @NonNull Criterion criterion) {
        switch(criterion){
            case COST:
                return context.getString(R.string.cost_criterion_text);
            case TIME:
                return context.getString(R.string.time_criterion_text);
            default:
                return context.getString(R.string.hybrid_criterion_text);
        }
    }
}
