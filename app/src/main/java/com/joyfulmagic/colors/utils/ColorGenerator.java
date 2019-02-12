package com.joyfulmagic.colors.utils;

import android.graphics.Color;

/**
 * Happy little random color generator.
 */
public class ColorGenerator extends RandGen {

    public static int randColor(){
        int color = Color.rgb(randMax(255), randMax(255), randMax(255));
        return color;
    }

    // use this in very accuracy (this is infinite loop, dude)
    public static int randColorInRange(String range){

        int color;
        while(true) {
            color = randColor();
            if(range.equalsIgnoreCase(ColorConverter.getColor(color))){
                break;
            }
        }
        return color;
    }
}
