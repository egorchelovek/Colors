package com.joyfulmagic.colors.activities.SkillTreeActivity.ColorSpaceHarmonyPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.joyfulmagic.colors.activities.SkillTreeActivity.SkillFragment;
import com.joyfulmagic.colors.activities.SkillTreeActivity.SkillTreeActivity;

/**
 * Simple adapter of pager
 */
public class ScreenSliderPagerAdapter extends FragmentStatePagerAdapter {

    // links
    private FragmentManager fm;
    private final SkillTreeActivity skillTreeActivity;

    // constructor
    public ScreenSliderPagerAdapter(FragmentManager fragmentManager, SkillTreeActivity skillTreeActivity){
        super(fragmentManager);

        // init links
        fm = fragmentManager;
        this.skillTreeActivity = skillTreeActivity;
    }

    // return skill fragment
    @Override
    public Fragment getItem(int position) {

        SkillFragment out = new SkillFragment(); // make new fragment on page

        out.setAdapterParams(skillTreeActivity, position, true); // set right parameters of this fragment

        return out;
    }

    @Override
    public int getCount() {
        return SkillTreeActivity.numPages;
    }
}
