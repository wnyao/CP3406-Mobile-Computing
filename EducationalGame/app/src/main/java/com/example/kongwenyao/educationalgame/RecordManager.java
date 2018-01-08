package com.example.kongwenyao.educationalgame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kongwenyao on 1/7/18.
 */

public class RecordManager {

    private List<Object> values;    //TODO: set lifespan
    private int score = 0;

    public RecordManager() {
        values = new ArrayList<>();
    }

    public void addValue(String value) {
        values.add(value);
    }

    public void addValue(int value) {
        values.add(value);
    }

    public Total isTotal(int matchValue) {    //addition
        Total result;

        Integer total = calculateTotal();

        if (matchValue == total) {   //addition
            result = Total.IS_TOTAL;
            score += 1;
        } else if (matchValue < total) {
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
    /**
    public int calculateTotal() {   //with digits and arithmetic symbols
        Integer total = 0;

        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) instanceof Integer) {
                //total += Integer.valueOf(values.get(i).toString());
            } else {
                String value = values.get(i).toString();

                switch (value) {
                    case "+":
                        //do sth
                        break;
                    case "-":
                        //do sth
                        break;
                }
            }
        }

        return total;
    }
     */

    public List<Object> getValues() {   //TODO: remove this shit
        return values;
    }


    public void clearRecordValues() {
        values.clear();
    }

    public int getScore() {
        return score;
    }
}
