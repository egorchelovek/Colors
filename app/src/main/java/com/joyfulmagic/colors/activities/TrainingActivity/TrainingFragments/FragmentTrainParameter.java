package com.joyfulmagic.colors.activities.TrainingActivity.TrainingFragments;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joyfulmagic.colors.AI.ColorTask;
import com.joyfulmagic.colors.utils.ColorConverter;
import com.joyfulmagic.colors.views.DrawersColorPicker.ColorScrollBar;
import com.joyfulmagic.colors.views.DrawersColorPicker.ColorScrollBarManager;
import com.joyfulmagic.colors.R;
import com.joyfulmagic.colors.activities.TrainingActivity.TrainingFragment;
import com.joyfulmagic.colors.databases.UserDatabase.ColorCard;

/**
 * Amazing HSL train fragment to
 * know basic parameters of color:
 * Hue,
 * Saturation,
 * Lightness...
 */
public class FragmentTrainParameter extends TrainingFragment {

    private int followColor; // setted color

    // images of colors
    private ImageView imageColor;
    private ImageView imageColorChoosed;
    private Bitmap colorFieldMain;
    private Bitmap colorFieldAnswer;

    // scroll bars for setting parameters
    private ColorScrollBar colorScrollBarHue;
    private ColorScrollBar colorScrollBarSat;
    private ColorScrollBar colorScrollBarLight;
    private ColorScrollBarManager colorScrollBarManager;

    private boolean init_flag; // initialization flag

    // initialization function
    private void init(){

        init_flag = false;

        // initialize images of colors

        // get image field
        imageColor = (ImageView) getActivity().findViewById(R.id.imageColorTrain);
        imageColorChoosed = (ImageView) getActivity().findViewById(R.id.imageColorTrainChoosed);
        int sizeImageColor = (int)getResources().getDimension(R.dimen.color_field_encyclopedy_info_main) / 2;

        // create images
        colorFieldMain = Bitmap.createBitmap(sizeImageColor, sizeImageColor, Bitmap.Config.RGB_565);
        colorFieldAnswer = Bitmap.createBitmap(sizeImageColor, sizeImageColor, Bitmap.Config.RGB_565);

        // link it
        imageColor.setImageBitmap(colorFieldMain);
        imageColorChoosed.setImageBitmap(colorFieldAnswer);

        // initialize bars

        // then create field for bars
        sizeImageColor *= 2;
        Bitmap hueField = Bitmap.createBitmap(sizeImageColor, sizeImageColor / 3, Bitmap.Config.ARGB_4444);
        Bitmap satField = Bitmap.createBitmap(sizeImageColor, sizeImageColor / 3, Bitmap.Config.ARGB_4444);
        Bitmap lightField = Bitmap.createBitmap(sizeImageColor, sizeImageColor / 3, Bitmap.Config.ARGB_4444);

        // ...
        colorScrollBarHue = (ColorScrollBar) getActivity().findViewById(R.id.colBarHueTrain);
        colorScrollBarHue.initColorBar(hueField,"Hue");
        colorScrollBarSat = (ColorScrollBar) getActivity().findViewById(R.id.colBarSatTrain);
        colorScrollBarSat.initColorBar(satField,"Saturation");
        colorScrollBarLight = (ColorScrollBar) getActivity().findViewById(R.id.colBarLightTrain);
        colorScrollBarLight.initColorBar(lightField,"Lightness");

        // link with manager and image of color
        colorScrollBarManager = new ColorScrollBarManager();
        colorScrollBarManager.addScrollBar(colorScrollBarHue);
        colorScrollBarManager.addScrollBar(colorScrollBarSat);
        colorScrollBarManager.addScrollBar(colorScrollBarLight);
        colorScrollBarManager.addColorImage(colorFieldAnswer);

        // check initialization succeed
        if(        imageColor != null
                && colorScrollBarHue != null
                && colorScrollBarSat != null
                && colorScrollBarLight != null)
            init_flag = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.training_fragment_train_parameter, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init(); // !!!

        setTask(getTask());
    }

    /**
     * Set fragment task
     * @param task some parameter task
     * @return succeed
     */
    @Override
    public boolean setTask(ColorTask task) {
        boolean taskSetedBit = false;

        if(super.setTask(task)){
            if(init_flag){

                boolean errorFlag = false;

                // get color from card
                followColor = getColorFromTask();

                // hide & show right parameters bars
                if(task.colorSkill.size() > 0) {
                    colorScrollBarManager.hideAllBars();
                    for (ColorCard cc : task.colorSkill) {
                        if (cc.isSkill()) {
                            colorScrollBarManager.setVisibility(cc.bad_hex, true);
                        }
                    }
                } else {
                    errorFlag = true;
                }

                // update color field
                colorFieldMain.eraseColor(followColor);

                // drop markers
                int color = Color.GREEN;
                if(colorScrollBarManager.getVisibility("Hue") == false){
                    float[] hsl = ColorConverter.colorToHsl(Color.GREEN);
                    float[] hslFollowColor = ColorConverter.colorToHsl(followColor);
                    hsl[0] = hslFollowColor[0];
                    color = ColorConverter.hslToColor(hsl);
                }
                colorScrollBarManager.setColor(color);
                colorFieldAnswer.eraseColor(color);


                // update flag
                taskSetedBit = !errorFlag;
            }
        }

        return taskSetedBit;
    }

    /**
     * Getter of result
     * @return right or wrong
     */
    @Override
    public boolean getResult(){

        boolean result = false;

        ColorTask t = getTask();
        if(t != null){
            t.result = colorScrollBarManager.checkColorInRange(followColor);
            result = t.result;
        }

        return result;
    }

}
