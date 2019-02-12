package com.joyfulmagic.colors.activities.ColorEncyclopedy;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.joyfulmagic.colors.databases.ColorDatabase.ColorString;
import com.joyfulmagic.colors.databases.DataBasesHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This fragment shows a list of colors,
 * which may to choose for detailed learning
 * in color info fragment
 */
public class FragmentColorList extends ListFragment implements ColorListener{

    private ColorEncyclopediaActivity colorEncyclopediaActivity; // main activity for fragment
    private ColorListenersHolder colorListenersHolder; // holder of listeners of color change event

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();

    }

    private void init(){

        // Load colors from database to cursor
        Cursor colorCursor = DataBasesHolder.colorDatabase.getAllBasicColors();
        colorCursor.moveToFirst();

        // create list of colors
        ArrayList<ColorObject> colorObjects = new ArrayList<ColorObject>();
        while (colorCursor.moveToNext() != false){
            colorObjects.add(new ColorObject(colorCursor));
        }

        // then sort they by hue first
        Collections.sort(colorObjects, new Comparator<ColorObject>() {
            @Override
            public int compare(ColorObject o1, ColorObject o2) {
                //return 0;
                int val1 = o1.getHue() - o2.getHue();
                int val2 = o1.getSat() - o2.getSat();
                int val3 = o1.getVal() - o2.getVal();

                return val1;
            }});
        // by another params second
        Collections.sort(colorObjects, new Comparator<ColorObject>() {
            @Override
            public int compare(ColorObject o1, ColorObject o2) {
                //return 0;
                int val1 = o1.getHue() - o2.getHue();
                int val2 = o1.getSat() - o2.getSat();
                int val3 = o1.getVal() - o2.getVal();

                return val2;
            }});

        // create and initialize adapter
        ColorObjectListAdapter colorObjectListAdapter = new ColorObjectListAdapter(getContext(), colorObjects);
        setListAdapter(colorObjectListAdapter);
    }


    //
    public void onListItemClick(ListView l, View v, int position, long id) {

        // Get color object from list position
        ColorObject color = (ColorObject) ((ColorObjectListAdapter) l.getAdapter()).getItem(position);

        // Get color fields from object
        ColorString c = (ColorString) color;

        // Send color to listeners to update color views and fields
        getColorListenersHolder().sendColor(c);

        // Show color info fragment with that color
        if(colorEncyclopediaActivity != null) {
            colorEncyclopediaActivity.setFragment(1);
        }
    }

    // useless interface for what todo?
    @Override
    public void onChooseColor(ColorString c) {}

    /**
     * Getter of holder
     * @return color listeners holder
     */
    public ColorListenersHolder getColorListenersHolder() {
        if(colorListenersHolder == null){
            colorListenersHolder = new ColorListenersHolder();
        }
        return colorListenersHolder;
    }

    // Setter of main activity of fragment
    public void setColorEncyclopediaActivity(ColorEncyclopediaActivity colorEncyclopediaActivity) {
        this.colorEncyclopediaActivity = colorEncyclopediaActivity;
    }


}
