package com.joyfulmagic.colors.databases.UserDatabase;

import android.database.Cursor;
import android.graphics.Color;

import com.joyfulmagic.colors.activities.SettingsActivity.Settings;
import com.joyfulmagic.colors.databases.ColorDatabase.ColorString;
import com.joyfulmagic.colors.utils.RegExp;
import com.joyfulmagic.colors.utils.Timer;

import java.sql.Timestamp;

/**
 * Expansion of ColorString with some info for UserDatabase records
 */
public class ColorCard extends ColorString{

    public boolean type;
    // 1 for DB update (checked skill or learned color)
    // 0 another color or skill which is not for update (default)
    public boolean status_learn;
    // 0 not for learn
    // 1 learn for now
    public int shows;
    // number of shows
    public int answers;
    // number of right answers
    public Timestamp date;
    // number of last show

    public ColorCard(Cursor cursor){
        super(cursor);

        if(cursor != null) {
            if(cursor.getCount() > 0) {

                // get data from database cursor
                status_learn = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(UserDatabase.COLUMN_NAME_STATUS_LEARN)));
                shows = Integer.valueOf(cursor.getString(cursor.getColumnIndex(UserDatabase.COLUMN_NAME_NUMBER_SHOWS)));
                answers = Integer.valueOf(cursor.getString(cursor.getColumnIndex(UserDatabase.COLUMN_NAME_NUMBER_ANSWERS)));
                date = Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(UserDatabase.COLUMN_NAME_DATE_SHOW)));

                // set type
                type = false;
                if(isSkill()) type = true;
            }
        }

    }

    // new color or viewed
    public boolean isNew(){
        if(shows == 0){
            return true;
        }
        return false;
    }

    // skill or color
    public boolean isSkill(){
        return RegExp.checkWithRegExp(bad_hex, "^[A-Za-z-]+$");
    }

    // update simple fun
    public void update(){
        shows++;
        answers++;
        date = Timer.getTime();

        float relation = ((float)answers)/((float)shows);
        if(answers >= 10 &&  relation > 0.66){
            status_learn = false;
        } else {
            status_learn = true;
        }
    }

    // for printing in Sys.out
    public String toString(){
        return new String(
                " " + hex +
                " " + status_learn +
                " shows " + String.valueOf(shows) +
                " answers " + String.valueOf(answers) +
                " date " + date +
                " " + names[Settings.language]);
    }

    // from hex to color!
    public int toColor(){
        int color = Color.GREEN;
        try{
            color = Color.parseColor(hex);
        } catch (Exception e){
            e.printStackTrace();
        }
        return color;
    }
}
