package com.joyfulmagic.colors.utils;

/**
 * Phi number keeper
 */
public class Constant{

    public static double Phi = getPhi();
    public static double phi = 1 / Phi;

    public static double getPhi(){
        return (1 + Math.sqrt(5))/ 2;
    }
}
