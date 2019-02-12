package com.joyfulmagic.colors.activities.HarmonyActivty;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.WindowManager;

import com.joyfulmagic.colors.R;
import com.joyfulmagic.colors.activities.BaseActivity;
import com.joyfulmagic.colors.activities.CameraActivity.Camera2Activity;
import com.joyfulmagic.colors.activities.CameraActivity.CameraActivity;
import com.joyfulmagic.colors.activities.ColorEncyclopedy.ColorEncyclopediaActivity;

/**
 * Activity for picking harmony for any color.
 * Read more about it in ColorFragment.
 */
public class HarmonyActivity extends BaseActivity {


    ColorFragment cf = null; // color fragment where harmony is picked
    private int fromWhere; // from where activity is started (Camera or Encyclopedia)

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitles(getString(R.string.harmony));

        setContentView(R.layout.harmony_lay);


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction t = fm.beginTransaction();

        // initialize
        cf = new ColorFragment();
        Intent intent = getIntent();
        Log.d("intent",String.valueOf(cf != null));
        fromWhere = intent.getIntExtra("source", 0);
        if(cf != null) {
            Log.d("intent",String.valueOf(intent.getIntExtra("color", Color.GREEN)));
            cf.setColorFull(intent.getIntExtra("color", Color.GREEN));
        }

        t.add(R.id.harmonyLayout, (Fragment) cf);
        t.commit();
    }

    /**
     * Back to primary activity.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Class<?> cl = Camera2Activity.class;
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            cl = CameraActivity.class;
        }

        if(fromWhere == 1){
            cl = ColorEncyclopediaActivity.class;
        }

        Intent intent = new Intent(this, cl);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("color", cf.getColor());

        startActivity(intent);
    }
}
