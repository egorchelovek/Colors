package com.joyfulmagic.colors.activities.ColorEncyclopedy;

import com.joyfulmagic.colors.databases.ColorDatabase.ColorString;

import java.util.ArrayList;

/**
 * Holder of color listeners.
 */
public class ColorListenersHolder {

    private ArrayList<ColorListener> listeners; // list of listeners =)

    public ColorListenersHolder(){
        listeners = new ArrayList<ColorListener>();
    }

    /**
     * Add listener to holder.
     * @param colorListener ?!
     */
    public void addListener(ColorListener colorListener){
        listeners.add(colorListener);
    }

    /**
     * Send color for listeners of holder.
     * @param colorString color with hex and name
     */
    public void sendColor(ColorString colorString){
        for(ColorListener l : listeners){
            l.onChooseColor(colorString);
        }
    }
}
