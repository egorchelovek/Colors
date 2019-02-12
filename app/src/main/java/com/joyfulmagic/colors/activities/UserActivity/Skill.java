package com.joyfulmagic.colors.activities.UserActivity;

import java.sql.Timestamp;

/**
 * Simple color skill object.
 */
public class Skill {

    public String name;

    public Timestamp lastRepeatDate;
    public Timestamp nextRepeatDate;

    public int numberOfRepeat;
    public int numberOfCatches;

    public int level;
    public int maxLevel;

    public Skill(String name){

        this.name = name;

        lastRepeatDate = Timestamp.valueOf("2011-10-02 18:48:05.123456"); // =)
        nextRepeatDate = lastRepeatDate;

        numberOfRepeat = 0;
        numberOfCatches = 0;
        level = 0;
        maxLevel = 0;
    }
}
