package com.joyfulmagic.colors.activities.ColorEncyclopedy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joyfulmagic.colors.activities.CameraActivity.CameraActivity;
import com.joyfulmagic.colors.activities.HarmonyActivty.HarmonyActivity;
import com.joyfulmagic.colors.activities.SettingsActivity.Settings;
import com.joyfulmagic.colors.databases.ColorDatabase.ColorString;
import com.joyfulmagic.colors.AI.ColorTask;
import com.joyfulmagic.colors.views.HSLColorPicker.HSLColorPicker;
import com.joyfulmagic.colors.views.HarmonyPanel.HarmonyPanel;
import com.joyfulmagic.colors.R;
import com.joyfulmagic.colors.activities.TrainingActivity.TrainingFragment;
import com.joyfulmagic.colors.utils.AdvancedMetrics.AdvancedDisplayMetrics;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This fragment will show all interesting information
 * about color in few fields:
 * 1) image of color
 * 2) name of color
 * 4) basic color schemes /\ [X]
 * 5) hue, saturation and lightness levels of color
 */
public class FragmentColorInfo extends TrainingFragment implements ColorListener{

    private ColorEncyclopediaActivity colorEncyclopediaActivity; // main activity of fragment

    private ColorListenersHolder colorListenersHolder; // holder of color listeners

    ColorString followColor; // setted color

    private TextView textColor; // color name
    private ImageView imageColor; private Bitmap colorFieldMain; // color image
    private HSLColorPicker hslColorPicker; private TextView textPicker; // HSL parameters
    private HarmonyPanel harmonyPanel; private TextView textHarmony; // harmony set

    private ArrayList<View> views; // list of all views in fragment

    private boolean init_flag; // initialization flag
    private boolean from_inner; // from where fragment is started (out or from of Encylopedia activity)
    private Boolean color_seted = new Boolean(false); // color seted flag

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        init_flag = false;
        from_inner = false;
        return inflater.inflate(R.layout.color_encyclopedy_color_info, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();

        onChooseColor(followColor);
    }

    /**
     * Initialization of all views of color info fragment
     */
    private void init(){

        followColor = new ColorString("White","#FFFFFF");

        // Create text field with name
        textColor = new TextView(getContext());
        textColor.setGravity(Gravity.CENTER);

        // Create image field
        imageColor = new ImageView(getContext());
        int sizeImageColor = AdvancedDisplayMetrics.link.getSubsizes(0)[0];
        colorFieldMain = Bitmap.createBitmap(sizeImageColor, sizeImageColor, Bitmap.Config.RGB_565);
        imageColor.setImageBitmap(colorFieldMain);
        imageColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityHarmony();
            }
        });

        // text of HSL picker
        textPicker = new TextView((getContext()));
        textPicker.setText(getString(R.string.view_color_picker));
        textPicker.setGravity(Gravity.CENTER);

        // HSL picker to view position of color
        hslColorPicker = new HSLColorPicker(getContext());
        hslColorPicker.setTouchable(false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        hslColorPicker.setLayoutParams(params);

        // text of harmony panel
        textHarmony = new TextView((getContext()));
        textHarmony.setText(getString(R.string.view_harmony));
        textHarmony.setGravity(Gravity.CENTER);
        textHarmony.setVisibility(View.INVISIBLE);
        // and
        harmonyPanel = new HarmonyPanel(getContext());
        harmonyPanel.setVisibility(View.INVISIBLE);


        // make list of views
        views = new ArrayList<View>();
        views.add(new TextView(getContext()));
        views.add(textColor);
        views.add(imageColor);
        views.add(textPicker);
        views.add(hslColorPicker);
        views.add(textHarmony);
        //views.addAll(harmonyPanel.getViewList());

        // now load all views to common layout from list
        LinearLayout l = (LinearLayout) getActivity().findViewById(R.id.colorInfoLayout);
        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        int idx = 0;
        for(View v : views){
            v.setId(idx);
            l.addView(v, p); // right there
            p.bottomMargin = (int) getResources().getDimension(R.dimen.standart_margin);
            idx++;
        }



        // now check initialization
        if(imageColor != null
                && textColor != null
                && hslColorPicker != null
                && harmonyPanel != null)
            init_flag = true;
    }

    /**
     * Set color to all views from listener interface
     * @param c color string
     */
    @Override
    public void onChooseColor(final ColorString c) {

        color_seted = false;

        // set color task execution
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    while(!color_seted) {
                        setColor(c);
                        TimeUnit.MILLISECONDS.sleep(10);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    /**
     * Set color to all views
     * @param c color with name and hex
     * @return succeed flag
     */
    private boolean setColor(ColorString c){

        if(c != null){
            followColor = c;

            if(init_flag) {
                try {
                    int color = Color.parseColor(followColor.hex);

                    String text = followColor.names[Settings.language];// + "\n" + followColor.hex;
                    textColor.setText(text);
                    colorFieldMain.eraseColor(color);
                    hslColorPicker.setColor(color);
                    harmonyPanel.setColor(color);

                    color_seted = true;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return color_seted;
    }


    // Move to Harmony Activity
    private void startActivityHarmony() {


        Intent intent = new Intent(getActivity(), HarmonyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // put color info to intent
        CameraActivity.color = followColor.getColor();
        intent.putExtra("source", 1);
        intent.putExtra("name", followColor.names);
        intent.putExtra("color", followColor.getColor());

        startActivity(intent);
    }

    // Set main activity
    public void setColorEncyclopediaActivity(ColorEncyclopediaActivity colorEncyclopediaActivity) {
        this.colorEncyclopediaActivity = colorEncyclopediaActivity;
    }

    // Set task for learn
    @Override
    public boolean setTask(ColorTask task) {
        if(task.colorSkill.size() > 0) {
            from_inner = true;
            onChooseColor(task.colorSkill.get(0));
        }
        return true;
    }


    // Return holder of listeners
    public ColorListenersHolder getColorListenersHolder() {
        if(colorListenersHolder == null){
            colorListenersHolder = new ColorListenersHolder();
        }
        return colorListenersHolder;
    }
}
