package com.pertamina.portal.iam.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private String inputPattern, outputPattern;
    private Object date;

    public DateUtils setInputPattern(String inputPattern) {
        this.inputPattern = inputPattern;
        return this;
    }

    public DateUtils setOutputPattern(String outputPattern) {
        this.outputPattern = outputPattern;
        return this;
    }

    public DateUtils setInputDate(Object date) {
        this.date = date;
        return this;
    }

    public String build() {
        if (date == null) {
            return "date is null!";
        }

        if (date instanceof Date) {
            return new SimpleDateFormat(outputPattern).format(date);
        }

        try {
            return new SimpleDateFormat(outputPattern).format(new SimpleDateFormat(inputPattern).parse((String)date));
        } catch (Exception e) {
            if (inputPattern == null)
                return "input pattern is null!";
            else if (outputPattern == null)
                return "output pattern is null!";
            else
                return "";
        }
    }

}
