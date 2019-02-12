package com.joyfulmagic.colors.activities.ColorEncyclopedy;

import android.database.Cursor;
import android.graphics.Color;

import com.joyfulmagic.colors.databases.ColorDatabase.ColorString;
import com.joyfulmagic.colors.utils.ColorConverter;

/**
 * Simple color object for sorting.
 */
public class ColorObject extends ColorString{

    float[] HSV;
    String range;

    public ColorObject(Cursor c) {
        super(c);

        try {

            int color = Color.parseColor(hex);
            HSV = new float[3];
            Color.colorToHSV(color, HSV);
            range = ColorConverter.getColor(hex);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    int getHue(){
        if(HSV != null)
        return (int)(HSV[0]);
        return 0;
    }

    int getSat(){
        if(HSV != null)
            return (int)(HSV[1]);
        return 0;
    }

    int getVal(){
        if(HSV != null)
            return (int)(HSV[2]);
        return 0;
    }

}
