package com.joyfulmagic.colors.activities.HarmonyActivty;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.joyfulmagic.colors.R;
import com.joyfulmagic.colors.activities.CameraActivity.Camera2Activity;
import com.joyfulmagic.colors.activities.CameraActivity.CameraActivity;
import com.joyfulmagic.colors.activities.ColorEncyclopedy.ColorEncyclopediaActivity;
import com.joyfulmagic.colors.activities.SettingsActivity.Settings;
import com.joyfulmagic.colors.utils.AdvancedMetrics.AdvancedDisplayMetrics;
import com.joyfulmagic.colors.utils.ColorConverter;
import com.joyfulmagic.colors.utils.ColorHarmonizer;
import com.joyfulmagic.colors.utils.Constant;
import com.joyfulmagic.colors.views.HarmonyPanel.HarmonyPanel;
import com.joyfulmagic.colors.views.HarmonyPanel.HarmonyView;
import com.joyfulmagic.colors.views.HarmonyPanel.OnColorChoose;

import org.w3c.dom.Text;

/**
 * Fragment for picking harmony for any color.
 * Harmony is a combination of some colors
 * which placed in right positions of color circle.
 * This positions or point usually composes regular polygons
 * such as equilateral triangle, square, and other shapes.
 */
public class ColorFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, OnColorChoose{

    int colorSeted; // follow color
    int colorOnScreen; // viewed color
    int colorOnScreenAdd; // additional color

    // main color field on full screen
    Bitmap colorBit;
    ImageView colorField;
    TextView nearestColorText;

    // seek bars for HSL-parameters correction
    LinearLayout seekBars;
    SeekBar Hue;
    SeekBar Saturation;
    SeekBar Lightness;
    int gradNumber = 256; // levels for each SeekBar
    float[] hsl; // array with HSL parameters values
    boolean automatic = false;
    private TextView hueText;

    // harmony panel for viewing harmony
    LinearLayout harmonyLayout;
    HarmonyPanel harmonyPanel; // for viewing harmony variants
    int harmonyIdx = 0; // index of setted harmony
    TextView harmonyName;
    TextView saturationText;
    TextView lightnessText;

    // harmony layout container for seek bars or for harmony panel
    LinearLayout harLay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.harmony, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // initialize color field
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int sizeX = display.getWidth();
        int sizeY = display.getHeight();
        //int sizeX = AdvancedDisplayMetrics.link.getHalfsizes(0)[0] * 2;
        //int sizeY = AdvancedDisplayMetrics.link.getHalfsizes(0)[1] * 2;
        colorBit = Bitmap.createBitmap(sizeX, sizeY, Bitmap.Config.ARGB_4444);
        colorField = (ImageView) getActivity().findViewById(R.id.colorFragmentImage);
        colorField.setImageBitmap(colorBit);
        colorField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEncyclopedia();
            }


        });
        colorField.setScaleType(ImageView.ScaleType.FIT_XY);
//        nearestColorText = (TextView) getActivity().findViewById(R.id.nearestColorText);

        // initialize seek bars
        Hue = new SeekBar(getContext());
        String hueName = getString(R.string.parameter_hue);
        hueText = new TextView(getContext());
        hueText.setText(hueName);
        hueText.setGravity(Gravity.CENTER);
        Hue.setTag(hueName);
        Hue.setOnSeekBarChangeListener(this);
        Hue.setMax(gradNumber);

        Saturation = new SeekBar(getContext());
        String saturationName = getString(R.string.parameter_saturation);
        saturationText = new TextView(getContext());
        saturationText.setText(saturationName);
        saturationText.setGravity(Gravity.CENTER);
        Saturation.setTag(saturationName);
        Saturation.setOnSeekBarChangeListener(this);
        Saturation.setMax(gradNumber);

        Lightness = new SeekBar(getContext());
        String lightnessName =  getString(R.string.parameter_lightness);
        lightnessText = new TextView(getContext());
        lightnessText.setText(lightnessName);
        lightnessText.setGravity(Gravity.CENTER);
        Lightness.setTag(lightnessName);
        Lightness.setOnSeekBarChangeListener(this);
        Lightness.setMax(gradNumber);

        seekBars = new LinearLayout(getContext());
        seekBars.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity= Gravity.CENTER;
        lp.weight = 1.0f;
        seekBars.addView(hueText, lp);
        seekBars.addView(Hue,lp);
        seekBars.addView(saturationText, lp);
        seekBars.addView(Saturation,lp);
        seekBars.addView(lightnessText, lp);
        seekBars.addView(Lightness,lp);
        seekBars.setBackgroundColor(getResources().getColor(R.color.transparent_black50));
        hsl = new float[3]; // allocate array



        // initialize harmony objects
        harmonyIdx = 1;
        int sizeX2 = AdvancedDisplayMetrics.link.getSubsizes(0)[0];
        int sizeY2 = (int) (sizeX2 / Constant.getPhi());
        harmonyPanel = new HarmonyPanel(getContext(), sizeX2, sizeY2 );
        harmonyPanel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                HarmonyView hv = harmonyPanel.getActiveView();
                hv.onTouch(hv, event);

                if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    setColor(CameraActivity.color);
                } else{
                    setColor(Camera2Activity.color);
                }

                updateColorImage(colorSeted);
                updateHarmonyName(harmonyIdx);
                updateBarsValues(true);
                return true;
            }
        });

        final RelativeLayout harmonyPanelView = new RelativeLayout(getContext());
        harmonyPanelView.setGravity(Gravity.CENTER);
        harmonyPanel.loadLay(harmonyPanelView);
        harmonyPanelView.setFocusable(true);
        harmonyPanelView.setFocusableInTouchMode(true);
        harmonyPanel.setColorChoosedListener(this);


        // initialize buttons of choosing harmony
        Button btnHarmonyBack = new Button(getContext());
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp1.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
        lp1.weight=1.0f;
        btnHarmonyBack.setLayoutParams(lp1);
        btnHarmonyBack.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        btnHarmonyBack.setText("<");
        btnHarmonyBack.setTextSize(btnHarmonyBack.getTextSize());
        btnHarmonyBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(harmonyIdx > 0) harmonyIdx--;
                harmonyPanel.setHarmonyType(harmonyIdx);

                // set color to fragment
                setColorFull(harmonyPanel.getActiveView().getHarmony()[0]);

                updateHarmonyName(harmonyIdx);
            }
        });
        Button btnHarmonyForward = new Button(getContext());
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp2.gravity = Gravity.RIGHT|Gravity.CENTER_VERTICAL;
        lp2.weight=1.0f;
        btnHarmonyForward.setLayoutParams(lp2);
        btnHarmonyForward.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
        btnHarmonyForward.setText(">");
        btnHarmonyForward.setTextSize(btnHarmonyForward.getTextSize());
        btnHarmonyForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(harmonyIdx < harmonyPanel.getHarmoniesNumber() - 1) harmonyIdx++;
                harmonyPanel.setHarmonyType(harmonyIdx);

                // set color to fragment
                setColorFull(harmonyPanel.getActiveView().getHarmony()[0]);

                updateHarmonyName(harmonyIdx);
            }
        });

        harmonyLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp3.gravity=Gravity.CENTER;
        harmonyLayout.setLayoutParams(lp3);
        harmonyLayout.setOrientation(LinearLayout.HORIZONTAL);
        harmonyLayout.addView(btnHarmonyBack);
        harmonyLayout.addView(harmonyPanelView);
        harmonyLayout.addView(btnHarmonyForward);
        harmonyLayout.setFocusableInTouchMode(true);
        harmonyLayout.setFocusable(true);
//        harmonyLayout.setClickable(true);


        // initialize harmony layout container
        harLay = (LinearLayout) getActivity().findViewById(R.id.harmonyLayContainer);
//        harLay.setBackgroundColor(getResources().getColor(R.color.transparent_black50));
        harLay.addView(seekBars);

        // initialize harmony text
        harmonyName = new TextView(getContext());
        LinearLayout.LayoutParams tvp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tvp.gravity = Gravity.CENTER|Gravity.TOP;
        tvp.setMargins(0,(int) getResources().getDimension(R.dimen.standart_margin),0,0);
        harmonyName.setLayoutParams(tvp);
        updateHarmonyName(1);
        harmonyName.setGravity(Gravity.CENTER);
        harmonyName.setBackgroundColor(getResources().getColor(R.color.transparent_black50));

        // initialize buttons of correction of color and viewing harmony (two modes)
        Button btnCorrect = (Button) getActivity().findViewById(R.id.buttonCorrect);
        btnCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                harLay.removeAllViews();
                harLay.addView(seekBars);
                updateHarmonyName(harmonyIdx);

            }

        });
        Button btnHarmony = (Button) getActivity().findViewById(R.id.buttonHarmony);
        btnHarmony.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                harLay.removeAllViews();
                harLay.addView(harmonyLayout);
                harLay.addView(harmonyName);
                updateHarmonyName(harmonyIdx);
            }
        });
//        Button btnSetup = (Button) getActivity().findViewById(R.id.buttonSetup);
//        btnSetup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                setColor(colorOnScreen);
//                updateColorImage(colorSeted);
//                updateHarmonyPanel(colorSeted);
//                updateBarsValues(true);
//            }
//        });


        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            setColorFull(CameraActivity.color);
        } else{
            setColorFull(Camera2Activity.color);
        }
    }

    /**
     * Update text field with harmony name
     * @param harmonyIdx number of Harmony
     */
    private void updateHarmonyName(int harmonyIdx) {

        String text = getString(R.string.harmony_type) + "\n";

        if(harmonyIdx > 0 && harmonyIdx < Settings.harmonies.length)
            text += Settings.harmonies[harmonyIdx];

        else if (harmonyIdx == 0)
            text += getString(R.string.rainbow);

        else
            text += getString(R.string.unknown);

        harmonyName.setText(text);
//        harmonyName.setTextColor(colorOnScreenAdd);
    }


    /**
     * Set color to fragment
     * @param color
     */
    public void setColorFull(int color){

        setColor(color);
        updateColorImage(color);
        updateHarmonyPanel(color);
        updateBarsValues(true);
//        updateParametersText();
    }

    /**
     * Color setter
     * @param color
     */
    public void setColor(int color){
        hsl = ColorConverter.colorToHsl(color);
        colorSeted = color;
    }

    /**
     * Update color field by color
     */
    private void
    updateColorImage(int color){
        colorOnScreen = color;
        colorOnScreenAdd = ColorHarmonizer.getCompletary(colorOnScreen)[1];
        if (colorBit != null) colorBit.eraseColor(color);
        if (colorField != null) colorField.setImageBitmap(colorBit);

        if(nearestColorText != null) nearestColorText.setText(getString(R.string.nearest_color) + "\n"+ ColorConverter.nearestColor(color).names[Settings.language]);
    }

    /**
     * Update color of harmony panel
     */
    private void updateHarmonyPanel(int color){
        if (harmonyPanel != null) harmonyPanel.setColor(color);
    }

    /**
     * Update bars values by new HSL parameters
     * @param auto flag to inhibit setColor(...) after setProgress(...)
     */
    private void updateBarsValues(boolean auto) {
        automatic = auto;
        if (Hue != null) Hue.setProgress((int) (hsl[0] * gradNumber));
        if (Saturation != null) Saturation.setProgress((int) (hsl[1] * gradNumber));
        if (Lightness != null) Lightness.setProgress((int) (hsl[2] * gradNumber));
        automatic = false;
    }

    /**
     * Start Encyclopedia activity
     */
    private void startEncyclopedia() {

        Intent intent = new Intent(getActivity(), ColorEncyclopediaActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("color", colorSeted); // send color from main field (for explore)
        startActivity(intent);
    }

    /**
     * Listener of seek bars values changing
     * @param seekBar HSL seek bar
     * @param progress progress in int
     * @param fromUser excess variable
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        float value = (float)progress / (float)seekBar.getMax();
        if(hsl != null) {
            if((String) seekBar.getTag() == (String) Hue.getTag()){
                hsl[0] = value;
            } else if((String) seekBar.getTag() == (String) Saturation.getTag()){
                hsl[1] = value;
            } else{
                hsl[2] = value;
            }

            if(!automatic){
                setColor(ColorConverter.hslToColor(hsl));
                updateColorImage(colorSeted);
                updateHarmonyPanel(colorSeted);
//                updateParametersText();
            }
        }
    }

    /**
     * Not very useful function
     */
    private void updateParametersText() {
        try {
            if (hueText != null)
                hueText.setTextColor(colorOnScreenAdd);
            if (saturationText != null)
                saturationText.setTextColor(colorOnScreenAdd);
            if (lightnessText != null)
                lightnessText.setTextColor(colorOnScreenAdd);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    /**
     * Color getter
     * @return color from main field
     */
    public int getColor() {
        return colorSeted;
    }



    /**
     * Element of interface
     * @param color color for set from onColorChoose event
     * @return the same color (for what todo?)
     */
    @Override
    public int onColorChoose(int color) {
        // Nothing to do - better variant
        return  color;
    }
}
