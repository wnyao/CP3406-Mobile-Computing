package com.example.kongwenyao.educationalgame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kongwenyao on 1/7/18.
 */

public class GameRecord {

    private List<Object> values;
    private int score = 0;
    private int chances = 5;

    public GameRecord() {
        values = new ArrayList<>();
    }

    public void addValue(int value) {
        values.add(value);
    }

    public Total isTotal(int matchValue) {
        Integer total = calculateTotal();
        Total result;

        if (total == matchValue) {
            result = Total.IS_TOTAL;
            score += 1;
        } else if (total > matchValue) {
            result = Total.MORE_THAN_TOTAL;
        } else {
            result = Total.LESS_THAN_TOTAL;
        }

        return result;
    }

    public int calculateTotal() {
        Integer total = 0;
        for (Object value: values) {
            total += Integer.valueOf(value.toString());
        }
        return total;
    }

    public void clearRecordValues() {
        values.clear();
    }

    public int getScore() {
        return score;
    }

    public void decrementChances() {
        if (chances > 0) {
            chances -= 1;
        }
    }

    public int getChances() {
        return chances;
    }

    public void setDefault() {
        chances = 5;
        score = 0;
    }
}
