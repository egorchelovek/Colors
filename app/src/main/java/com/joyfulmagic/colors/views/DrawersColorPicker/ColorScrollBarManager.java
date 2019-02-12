package com.joyfulmagic.colors.views.DrawersColorPicker;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;

import com.joyfulmagic.colors.views.OnParameterChange;
import com.joyfulmagic.colors.utils.ColorConverter;

import java.util.ArrayList;

/**
 * Color scroll bars with different parameters need to interact
 * with each other when color is changed from one of it.
 * That is function of this manager.
 */
public class ColorScrollBarManager implements OnParameterChange {

    private ArrayList<ColorScrollBar> bars;
    private ArrayList<Bitmap> colorImages;

    private boolean initOk;

    private int color;
    private float colorHsl[];
    private int hslIdxes[];

    public ColorScrollBarManager (){
        initOk = false;
        colorHsl = new float[3];
        bars = new ArrayList<ColorScrollBar>();
        colorImages = new ArrayList<Bitmap>();
        hslIdxes = new int[3];
    }

    public boolean addScrollBar(ColorScrollBar bar){
        if(bar != null && bars.size() <= 2) {
            boolean noSuchBar = true;
            for(ColorScrollBar b : bars){
                if(b.getIdx() == bar.getIdx()){
                    noSuchBar = false;
                    break;
                }
            }
            if(noSuchBar){
                bars.add(bar);
                bar.setManager(this);

                if(bars.size() == 3){
                    initOk = true;
                }

                return true;
            }

        }
        return false;
    }

    public void addColorImage(Bitmap img){
        if(img != null){
            colorImages.add(img);
        }
    }

    @Override
    public void changeParameter(int parameterIdx, float value) {



            for (ColorScrollBar b : bars) {
                if(b.getIdx() != parameterIdx){
                    b.changeParameter(parameterIdx, value);
                }
            }

        updateColor();
    }

    public void updateColor(){
        for(ColorScrollBar b : bars){
            colorHsl[b.getIdx()] = b.getParameter();
        }

        int [] rgb = ColorConverter.hslToRgb(colorHsl[0], colorHsl[1], colorHsl[2]);
        color = Color.rgb(rgb[0], rgb[1], rgb[2]);

        for(Bitmap img : colorImages){
            System.out.println("CSBM: update color");
            img.eraseColor(color);
        }
    }

    public void setColor(int color) {
        for(ColorScrollBar b : bars){
            b.setColor(color);
        }
    }

    public boolean checkColorInRange(int color){
        for(ColorScrollBar b : bars){
            if(b.getVisibility() == View.VISIBLE)
            if( b.checkColorInterval(color) > b.getMinError()){
                return false;
            }
        }
        return true;
    }

    public void setScrollable(boolean scrollable) {
        for(ColorScrollBar b : bars){
            b.setScrollable(scrollable);
        }
    }

    public void hideAllBars(){
        for(ColorScrollBar b : bars){
            b.setVisibility(View.GONE);
        }
    }

    public void setVisibility(String barName, boolean visible){
        for(ColorScrollBar b : bars){
            if(b.TAG.equals(barName)){
                if(visible) b.setVisibility(View.VISIBLE);
                else b.setVisibility(View.GONE);
            }
        }
    }

    public boolean getVisibility(String barName){

        boolean visible = false;

        for(ColorScrollBar b : bars){
            if(b.TAG.equals(barName)){
                if(b.getVisibility() == View.VISIBLE) visible = true;
            }
        }

        return visible;
    }

    public ArrayList<ColorScrollBar> getBars(){
        return bars;
    }
}
