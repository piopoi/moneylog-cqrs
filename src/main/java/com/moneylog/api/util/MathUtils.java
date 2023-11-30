package com.moneylog.api.util;

public class MathUtils {

    public static Long floorHundredBelow(Long number) {
        return number / 100 * 100;
    }
}
