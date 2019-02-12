package com.joyfulmagic.colors.views.DrawersColorPicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.joyfulmagic.colors.R;

/**
 * Combine color scroll bars in to drawer's color picker,
 * which showing to you how to get any color from
 * basic tone, complimentary color and white or black ink.
 *
 * Step 1: get basic tone of color by hue value.
 * (You may mix it with analogous color for better precision.)
 * Step 2: mix basic tone with complimentary color for get such saturation.
 * (There is trick. If you will watch on the color for 30s,
 * you will see complimentary color on white background then.)
 * Step 3: Add white or black color to get needed lightness.
 */
public class DrawersColorPicker {

    private ColorScrollBarManager colorScrollBarManager;
    private Context context;

    public DrawersColorPicker(Context context){
        this.context = context;

        init();
    }

    private void init(){

        int sizeImageColor = (int)context.getResources().getDimension(R.dimen.color_field_encyclopedy_info_main);
        int margin = (int) context.getResources().getDimension(R.dimen.standart_margin);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        p.setMargins(0,0,0,margin);

        Bitmap hueField = Bitmap.createBitmap(sizeImageColor, sizeImageColor / 3, Bitmap.Config.ARGB_4444);
        Bitmap satField = Bitmap.createBitmap(sizeImageColor, sizeImageColor / 3, Bitmap.Config.ARGB_4444);
        Bitmap lightField = Bitmap.createBitmap(sizeImageColor, sizeImageColor / 3, Bitmap.Config.ARGB_4444);

        ColorScrollBar colorScrollBarHue = new ColorScrollBar(context);
        colorScrollBarHue.initColorBar(hueField,"Hue");
        colorScrollBarHue.setLayoutParams(p);

        ColorScrollBar colorScrollBarSat = new ColorScrollBar(context);
        colorScrollBarSat.initColorBar(satField,"Saturation");
        colorScrollBarSat.setLayoutParams(p);

        ColorScrollBar colorScrollBarLight = new ColorScrollBar(context);
        colorScrollBarLight.initColorBar(lightField,"Lightness");
        colorScrollBarLight.setLayoutParams(p);

        colorScrollBarManager = new ColorScrollBarManager();
        colorScrollBarManager.addScrollBar(colorScrollBarHue);
        colorScrollBarManager.addScrollBar(colorScrollBarSat);
        colorScrollBarManager.addScrollBar(colorScrollBarLight);
        colorScrollBarManager.setScrollable(false);
    }

    public ColorScrollBarManager getManager() {
        return colorScrollBarManager;
    }

    public void loadLay(LinearLayout lay){
        for(View v : colorScrollBarManager.getBars()){

            lay.addView(v);
        }
    }
}
