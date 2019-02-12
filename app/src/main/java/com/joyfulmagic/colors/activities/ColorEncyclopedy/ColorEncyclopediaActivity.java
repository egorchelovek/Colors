package com.joyfulmagic.colors.activities.ColorEncyclopedy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.joyfulmagic.colors.activities.BaseActivity;
import com.joyfulmagic.colors.R;
import com.joyfulmagic.colors.activities.HarmonyActivty.HarmonyActivity;
import com.joyfulmagic.colors.activities.SettingsActivity.Settings;
import com.joyfulmagic.colors.databases.ColorDatabase.ColorString;
import com.joyfulmagic.colors.utils.ColorConverter;


/*
 * This class should to provide interesting information about
 * any colour. If you want to learn about some color or
 * find some color by name, or want to know which colors
 * is in harmony of this color, or to view different variation of
 * this color saturation and lightness.
 */
public class ColorEncyclopediaActivity extends BaseActivity {

    private FragmentManager manager;
    private FragmentTransaction transaction;

    private FragmentColorInfo fragmentColorInfo;
    private FragmentColorList fragmentColorList;
    private int followFragmentIdx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitles(getResources().getString(R.string.activity_encyclopedia));

        setContentView(R.layout.color_encyclopedy);

        // initialization of fragments
        fragmentColorInfo = new FragmentColorInfo();
        fragmentColorList = new FragmentColorList();

        fragmentColorList.getColorListenersHolder().addListener(fragmentColorInfo);
        fragmentColorInfo.getColorListenersHolder().addListener(fragmentColorList);

        fragmentColorInfo.setColorEncyclopediaActivity(this);
        fragmentColorList.setColorEncyclopediaActivity(this);



        // load fragments to layout
        manager = getSupportFragmentManager();

        followFragmentIdx = 0;
        transaction = manager.beginTransaction();
        transaction.add(R.id.encyclopedyLayout, fragmentColorList);
        transaction.commit();

        transaction = manager.beginTransaction();
        transaction.add(R.id.encyclopedyLayout, fragmentColorInfo);
        transaction.hide(fragmentColorInfo);
        transaction.commit();



        // set first fragment
        if(getIntent() != null) {

            int color = getIntent().getIntExtra("color", Color.TRANSPARENT);
            if(color != Color.TRANSPARENT) {
                ColorString nearestColor = ColorConverter.nearestColor(color);
                String strColor = String.format("#%06X", 0xFFFFFF & color);
                nearestColor.hex = strColor;
                nearestColor.bad_hex = strColor;

                nearestColor.names[0] = getString(R.string.nearest_color) + "\n" + nearestColor.names[0];
                nearestColor.names[1] = getString(R.string.nearest_color) + "\n" + nearestColor.names[1];

                fragmentColorInfo.onChooseColor(nearestColor);
                setFragment(1);
            }

        } else {
            setFragment(0);
        }


    }

    /**
     * Set fragment by id
     * @param id number of fragment
     */
    public void setFragment(int id){

        if(followFragmentIdx != id){

            transaction = manager.beginTransaction();
            switch (id) {
                case 0:
                    transaction.hide(fragmentColorInfo).show(fragmentColorList);
                    break;
                default:
                    transaction.hide(fragmentColorList).show(fragmentColorInfo);
            }
            transaction.commit();

            followFragmentIdx = id;
        }
    }

    /**
     * Back to primary activity
     */
    @Override
    public void onBackPressed() {
        if(getIntent() != null){

            this.finish();
//            Intent intent = new Intent(this, HarmonyActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
        } else {
            setFragment(0);
        }
    }


}
