package com.joyfulmagic.colors.activities.SkillTreeActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.joyfulmagic.colors.activities.BaseActivity;
import com.joyfulmagic.colors.R;
import com.joyfulmagic.colors.activities.SkillTreeActivity.ColorSpaceHarmonyPager.FragmentScreenSlidePage;
import com.joyfulmagic.colors.activities.SettingsActivity.Settings;

/**
 * Activity for viewing and choosing type of skill training.
 * There are 3 main skills:
 * 1) Knowing of colors;
 * 2) Train HSL-parameters definition;
 * 3) Seeing harmony in colors combinations.
 */
public class SkillTreeActivity extends BaseActivity {

//    ProgressBar progressBar; // progress ++ for each color card
    public static int numPages; // number of existing pages
    public int pageIdx; // index of choosed page

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitles(getResources().getString(R.string.activity_skill_tree));

        setContentView(R.layout.skill_tree);

//        // init progress bar
//        progressBar = new ProgressBar(getApplicationContext());
//        progressBar.setMax(5);
//        progressBar.setVisibility(View.VISIBLE);

        // set number of pages
        numPages = Settings.trains.length;

//        // load progress bar to layout
//        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.skillTreeActivityLayout);
//        linearLayout.addView(progressBar);

        // init first fragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        FragmentScreenSlidePage frag = new FragmentScreenSlidePage();
        frag.setActivity(this);
        frag.setFragmentManager(fm);
        transaction.add(R.id.skillTreeFragmentContainer, frag);
        transaction.commit();

        pageIdx = 0;
    }

    /**
     * Change fragment by idx
     * @param type index or type of fragment or train
     */
    public void changeFragment(int type){

        // define and create fragment by type
        SkillFragment f = new SkillFragment();
        switch (type){
            default:
                f.setAdapterParams(this, 1, false);
                break;
            case 2:
                f.setAdapterParams(this, 2, false);
                break;
            case 3:
                f.setAdapterParams(this, 3, false);
                break;
        }

        if(f != null){
            // change fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.skillTreeFragmentContainer,(Fragment)f);
            transaction.commit();

            // update page index
            pageIdx = type;
        }
    }

    /**
     * Return to initial fragment
     */
    @Override
    public void onBackPressed() {

        if(pageIdx != 0){
            // change fragment
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            FragmentScreenSlidePage frag = new FragmentScreenSlidePage();
            frag.setActivity(this);
            frag.setFragmentManager(fm);
            transaction.replace(R.id.skillTreeFragmentContainer, (Fragment)frag);
            transaction.commit();

            // update page index
            pageIdx = 0;
        }
    }
}
