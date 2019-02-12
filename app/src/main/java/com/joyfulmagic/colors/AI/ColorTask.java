package com.joyfulmagic.colors.AI;

import com.joyfulmagic.colors.databases.UserDatabase.ColorCard;

import java.util.ArrayList;

/**
 * Color Task class, contains some color cards for train.
 */
public class ColorTask {
    public int train; // Color Space Harmony
    public int type; // Subtype
    public boolean status; // 0 new or 1 repeat
    public ArrayList<ColorCard> colorSkill; // Colors or Skills
    public ArrayList<Integer> colors; // additional colors
    public boolean result; // fall or not

    public ColorTask(){
        result =false;

        colorSkill = new ArrayList<ColorCard>();
        colors = new ArrayList<Integer>();
    }
}
