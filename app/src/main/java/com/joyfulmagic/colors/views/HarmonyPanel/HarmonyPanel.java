package com.joyfulmagic.colors.views.HarmonyPanel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joyfulmagic.colors.activities.HarmonyActivty.ColorFragment;
import com.joyfulmagic.colors.databases.ColorDatabase.ColorString;
import com.joyfulmagic.colors.activities.ColorEncyclopedy.ColorListener;
import com.joyfulmagic.colors.R;
import com.joyfulmagic.colors.activities.SettingsActivity.Settings;

import java.util.ArrayList;

/**
 * Harmony Panel for watch Harmony variants
 */
public class HarmonyPanel implements ColorListener {

    private ViewGroup layout;
    private final Context context;
    private HarmonyViewManager harmonyViewManager;
    private ArrayList<View> viewsList;
    private ArrayList<HarmonyView> harmonyViewsList;

    private int activeHarmony = 0;
    private ColorFragment onColorChoosedListener;
    public int choosedColor;
    private OnColorChoose listener;
    private HarmonyView activeView;

    public HarmonyPanel(Context context) {
        this.context = context;

        init();
    }

    public HarmonyPanel(Context context, int sizeX, int sizeY) {
        this.context = context;

        init(sizeX,sizeY);
    }

    private void init() {

        viewsList = new ArrayList<View>();
        harmonyViewsList = new ArrayList<HarmonyView>();
        harmonyViewManager = new HarmonyViewManager();

        int size = (int) context.getResources().getDimension(R.dimen.color_field_encyclopedy_info_main);
        int margin = (int) context.getResources().getDimension(R.dimen.standart_margin);

        for(int i = 0; i < Settings.harmonies.length; i++){
            Bitmap bitmap = Bitmap.createBitmap(size, size / Settings.harmonies.length, Bitmap.Config.ARGB_4444);
            HarmonyView harmonyView = new HarmonyView(context);
            harmonyView.initHarmonyView(bitmap, Color.GREEN, i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, margin);
            harmonyView.setLayoutParams(params);
            harmonyView.setFocusable(true);
            harmonyView.setFocusableInTouchMode(true);
            harmonyView.requestFocus();
            harmonyViewsList.add(harmonyView);
            viewsList.add(harmonyView);
            harmonyView.setVisibility(View.INVISIBLE);
            harmonyViewManager.addHarmonyView(harmonyView);
        }

        setHarmonyType(1);
    }

    private void init(int sizeX, int sizeY) {

        viewsList = new ArrayList<View>();
        harmonyViewsList = new ArrayList<HarmonyView>();

        harmonyViewManager = new HarmonyViewManager();

        for(int i = 0; i < Settings.harmonies.length; i++){
            Bitmap bitmap = Bitmap.createBitmap(sizeX, sizeY, Bitmap.Config.ARGB_4444);
            HarmonyView harmonyView = new HarmonyView(context);
            harmonyView.initHarmonyView(bitmap, Color.GREEN, i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            harmonyView.setLayoutParams(params);
            harmonyView.setHarmonyPanel(this);
            harmonyView.setClickable(true);
            harmonyView.setFocusableInTouchMode(true);
            harmonyView.setFocusable(true);
            harmonyViewsList.add(harmonyView);
            viewsList.add(harmonyView);
            harmonyView.setVisibility(View.INVISIBLE);
            harmonyViewManager.addHarmonyView(harmonyView);
        }

        setHarmonyType(1);
    }

    public void setHarmonyType(int type) {

        viewsList.get(activeHarmony).setVisibility(View.INVISIBLE);
        viewsList.get(type).setVisibility(View.VISIBLE);
        activeHarmony = type;
    }

    public int getHarmoniesNumber(){
        return viewsList.size();
    }

    public void loadLay(ViewGroup lay){

        layout = lay;

        for(View v : viewsList){
            layout.addView(v);
        }
    }

    public ArrayList<View> getViewList(){
        return viewsList;
    }

    public void setColor(int color){
        choosedColor = color;
        harmonyViewManager.setColor(color);
    }

    @Override
    public void onChooseColor(ColorString colorString) {
        setColor(Color.parseColor(colorString.hex));
    }

    public void setVisibility(int visibility) {
        if(layout != null) layout.setVisibility(visibility);
    }

    public void setClickable(boolean clickable) {
        for(View v : viewsList){
            v.setClickable(clickable);
        }
    }

    public void chooseColor(int choosedColor) {
        this.choosedColor = choosedColor;
        if(listener != null){
            listener.onColorChoose(choosedColor);
        }
    }

    public void setColorChoosedListener(OnColorChoose onColorChoose){
        listener = onColorChoose;
    }

    public void setOnTouchListener(View.OnTouchListener onTouchListener){
        for(View v : viewsList){
            v.setOnTouchListener(onTouchListener);
        }
    }

    public HarmonyView getActiveView() {
        if(harmonyViewsList != null)
        return harmonyViewsList.get(activeHarmony);
        return null;
    }
}
