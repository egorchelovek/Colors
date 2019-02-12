package com.joyfulmagic.colors.views.HarmonyPanel;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * HarmonyViewManager for bind all harmony views together
 */
public class HarmonyViewManager {

    ArrayList<HarmonyView> harmonyViews;
    ArrayList<Bitmap> bitmaps;


    public HarmonyViewManager() {
        harmonyViews = new ArrayList<HarmonyView>();
        bitmaps = new ArrayList<Bitmap>();
    }

    public void addHarmonyView(HarmonyView harmonyView){
        if(harmonyView != null)
        harmonyViews.add(harmonyView);
    }

    public void addBitmap(Bitmap bitmap){
        if(bitmap != null)
            bitmaps.add(bitmap);
    }

    public void setColor(int color) {
        for(HarmonyView v : harmonyViews){
            v.setColor(color);
        }

        for(Bitmap b: bitmaps){
            b.eraseColor(color);
        }
    }

    public void invalidate() {
        for(HarmonyView v : harmonyViews){
            v.invalidate();
        }
    }
}
