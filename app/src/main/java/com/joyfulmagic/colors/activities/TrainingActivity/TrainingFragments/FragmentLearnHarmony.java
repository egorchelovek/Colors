package com.joyfulmagic.colors.activities.TrainingActivity.TrainingFragments;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joyfulmagic.colors.AI.ColorTask;
import com.joyfulmagic.colors.activities.SettingsActivity.Settings;
import com.joyfulmagic.colors.utils.Shuffler;
import com.joyfulmagic.colors.views.Palete.PaleteArrayAdapter;
import com.joyfulmagic.colors.views.Palete.PaleteView;
import com.joyfulmagic.colors.R;
import com.joyfulmagic.colors.activities.TrainingActivity.TrainingFragment;
import com.joyfulmagic.colors.utils.AdvancedMetrics.AdvancedDisplayMetrics;
import com.joyfulmagic.colors.views.Checkable.CheckedInt;
import com.joyfulmagic.colors.utils.ColorHarmonizer;
import com.joyfulmagic.colors.utils.ColorGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Fragment for learn harmony.
 */
public class FragmentLearnHarmony extends TrainingFragment {

    // main views
    private TextView taskText; // text of task
    private ImageView taskColor; private Bitmap imageColor; // image view of color
    private PaleteView paletteView; // color palette

    // color objects
    private int followColor;
    private int[] followHarmony;

    // objects for colors selection
    private ArrayList<Integer> colorsList;
    private PaleteArrayAdapter paleteArrayAdapter;
    private CheckedInt[] colors;
    private ArrayList<Integer> selectedColorsIdxs;
    private int selectCounter;
    private int maxSelect;

    boolean initFlag; // initialization flag of fragment

    // initialization of memory arrays
    public boolean init(){

        initFlag = false;

        // init color
        followColor = Color.WHITE;

        // init image
        int size = AdvancedDisplayMetrics.link.getSubsizes(2)[0];
        imageColor = Bitmap.createBitmap(size,size, Bitmap.Config.ARGB_4444);

        // init arrays and lists
        colors = new CheckedInt[]{
                new CheckedInt(Color.YELLOW,false),
                new CheckedInt(Color.YELLOW,false),
                new CheckedInt(Color.YELLOW,false),
                new CheckedInt(Color.YELLOW,false),
//                new CheckedInt(Color.YELLOW,false),
//                new CheckedInt(Color.YELLOW,false),
//                new CheckedInt(Color.YELLOW,false),
//                new CheckedInt(Color.YELLOW,false),
        };
        selectedColorsIdxs = new ArrayList<Integer>();

        if(imageColor != null && colors != null && selectedColorsIdxs != null)
            initFlag =true;

        return initFlag;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.training_fragment_harmony, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();

        // init palette adapter
        int level = 2;
        int size = AdvancedDisplayMetrics.link.getSubsizes(level)[0];
        int subSize = AdvancedDisplayMetrics.link.getSubsizes(level+1)[0] / 2;
        paleteArrayAdapter = new PaleteArrayAdapter(
                getContext(),
                R.layout.palete_element,
                colors, size
        );

        // init task text and image
        taskText = (TextView) getActivity().findViewById(R.id.harmonyTaskText);
        taskColor = (ImageView) getActivity().findViewById(R.id.harmonyTaskColor);
        // then adjust image view of color
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity=Gravity.CENTER;
        lp.setMargins(0, 0, 0, subSize);
        taskColor.setLayoutParams(lp);
        taskColor.setImageBitmap(imageColor);

        // create and adjust palete view
        paletteView = new PaleteView(getContext());
        paletteView.setAdapter(paleteArrayAdapter);
        paletteView.setNumColumns(colors.length / 2);
        paletteView.setHorizontalSpacing(subSize);
        paletteView.setVerticalSpacing(subSize);
        GridView.LayoutParams p = new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT, GridView.LayoutParams.WRAP_CONTENT);
        paletteView.setLayoutParams(p);
        paletteView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        paletteView.setClickable(true);
        paletteView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        paletteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

                // regulate the selection of colors
                if(colors[position].getState() == false){
                    if(selectCounter < maxSelect) {
                        colors[position].setState(true);
                        selectCounter++;
                    }
                } else {
                    selectCounter--;
                    colors[position].setState(false);

                }
                paleteArrayAdapter.notifyDataSetChanged();
            }
        });
        GridView.LayoutParams params = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.WRAP_CONTENT);
        paletteView.setLayoutParams(params);
        paletteView.setGravity(Gravity.CENTER);

        // add palette view to layout
        LinearLayout paleteContainer = (LinearLayout) getActivity().findViewById(R.id.paleteContainer);
        paleteContainer.addView(paletteView);
        paleteContainer.setClickable(true);

        // !
        setTask(getTask());
    }

    /**
     * Set task to fragment
     * @param task some task
     * @return succeed
     */
    @Override
    public boolean setTask(ColorTask task) {

        boolean succeed = false;

        if(super.setTask(task)) {
            if (initFlag) {
                if (task.colorSkill != null) {

                    // get color
                    followColor = getColorFromTask();

                    // and update main image field
                    imageColor.eraseColor(followColor);

                    // get harmony colors array
                    followHarmony = ColorHarmonizer.getHarmony(followColor,task.type);
                    if(followHarmony.length <= colors.length) {

                        // fulfill array binded with view by harmony colors
                        int i = 0;
                        for (i = 0; i < followHarmony.length - 1; i++) {
                            colors[i].setNumber(followHarmony[i + 1]);
                            colors[i].setState(false);
                        }
                        // the fulfill random colors
                        while(i < colors.length){
                            colors[i].setNumber(ColorGenerator.randColor());
                            colors[i].setState(false);
                            i++;
                        }
                    }

                    Shuffler.shuffle(colors);

                    // then we must mix colors

                    // split and set variables
                    maxSelect = followHarmony.length - 1;
                    selectCounter = 0;

                    // and update views
                    paleteArrayAdapter.notifyDataSetChanged();
                    paletteView.invalidateViews();
                    paletteView.setAdapter(paleteArrayAdapter);

                    try {
                        taskText.setText(getString(R.string.fragment_learn_harmony) + "\n (" + Settings.harmonies[task.type] + ")");
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    // ok
                    succeed = true;
                }
            }
        }

        return succeed;
    }

    /**
     * Result getter
     * @return wrong or right
     */
    @Override
    public boolean getResult() {
        boolean result = true;
        for(int i = 0; i < colors.length; i++){

            // get only selected colors
            if(colors[i].getState() == true){

                // check color in harmony array
                boolean here = false;
                for(int j = 0; j < followHarmony.length; j++){
                    if(followHarmony[j] == colors[i].getNumber()){
                        here = true;
                        break;
                    }
                }

                // if color is absent
                if(!here){
                    result = false;
                    // cause all selected colors must be in harmony
                }
            }
        }

        // set task answer
        getTask().result = result;

        return result;
    }
}
