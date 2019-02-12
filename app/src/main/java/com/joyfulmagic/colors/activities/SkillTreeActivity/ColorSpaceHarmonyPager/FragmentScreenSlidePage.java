package com.joyfulmagic.colors.activities.SkillTreeActivity.ColorSpaceHarmonyPager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joyfulmagic.colors.R;
import com.joyfulmagic.colors.activities.SkillTreeActivity.SkillTreeActivity;

/**
 * Screen slide page fragment
 */
public class FragmentScreenSlidePage extends Fragment {

    // links
    private FragmentManager fragmentManager; // simple manager of fragments of activity
    private SkillTreeActivity skillTreeActivity; // main activity where fragment is

    // and setters
    public void setFragmentManager(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;

    }
    public void setActivity(SkillTreeActivity skillTreeActivity){
        this.skillTreeActivity = skillTreeActivity;
    }

    /**
     * Main method
     */
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.skill_tree_pager, container, false);

        // initialize pager and adapter
        PagerAdapter pagerAdapter = new ScreenSliderPagerAdapter(fragmentManager, skillTreeActivity);
        final ViewPager pager=(ViewPager)result.findViewById(R.id.pagerSkill);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(skillTreeActivity.pageIdx);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                skillTreeActivity.pageIdx = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        });

        // some adjusting
        pager.setClipToPadding(false); // disable clip to padding
        pager.setPadding(40, 0, 40, 0); // set padding manually, the more you set the padding the more you see of prev & next page
        pager.setPageMargin(20); // sets a margin b/w individual pages to ensure that there is a gap b/w them

        return result;
    }
}
