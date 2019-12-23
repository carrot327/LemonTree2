package com.sm.android.utils;

import java.util.List;

/**
 * Created on 22/07/2018.
 *
 * @author Qiang Lili.
 */
public class IntegerUtil {
    /**
     * Get int array from integer list.
     *
     * @param integerList integer list.
     * @return int array.
     */
    public static int[] convertIntegerListToArray(final List<Integer> integerList) {
        if (integerList.isEmpty()) {
            return new int[0];
        }

        int[] array = new int[integerList.size()];

        for (int index = 0; index < integerList.size(); index++) {
            array[index] = integerList.get(index);
        }

        return array;
    }
}
