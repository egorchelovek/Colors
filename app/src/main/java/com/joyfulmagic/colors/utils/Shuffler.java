package com.joyfulmagic.colors.utils;

import com.joyfulmagic.colors.views.Checkable.CheckedInt;

import java.util.Random;

/**
 * Thank you, mister KitKat!
 */
public class Shuffler {

    private static Random random;

    /**
     * Code from method java.util.Collections.shuffle();
     */
    public static void shuffle(int[] array) {
        if (random == null) random = new Random();
        int count = array.length;
        for (int i = count; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static void shuffle(CheckedInt[] array) {
        if (random == null) random = new Random();
        int count = array.length;
        for (int i = count; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
    }

    private static void swap(CheckedInt[] array, int i, int j) {
        CheckedInt temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
