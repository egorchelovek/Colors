package com.joyfulmagic.colors.utils;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Tick-tok timer (Watches!!)
 */
public class Timer {

    // simple function to get timestamp
    public static Timestamp getTime(){
        Date date = new Date();
        Timestamp time;
        time = new Timestamp(date.getTime());
        return time;
    }
}
