package com.example.freeturilo.core;

import android.content.Context;

import com.example.freeturilo.R;

public class CriterionTools {
    public static String getCriterionText(Context context, Criterion criterion) {
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
