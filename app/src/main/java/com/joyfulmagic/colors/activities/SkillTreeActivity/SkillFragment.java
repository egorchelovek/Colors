package com.joyfulmagic.colors.activities.SkillTreeActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;

import com.joyfulmagic.colors.R;

/**
 * Simple skill fragment.
 * For more info view the adapter class.
 */
public class SkillFragment extends ListFragment{

    // objects to transfer to adapter
    private SkillTreeActivity skillTreeActivity;
    private int type;
    private boolean reduced;

    public void setAdapterParams(SkillTreeActivity skillTreeActivity, int type, boolean reduced){
        this.skillTreeActivity = skillTreeActivity;
        this.type = type;
        this.reduced = reduced;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // create and adjust skill fragment adapter
        SkillFragmentAdapter skillFragmentAdapter = new SkillFragmentAdapter(getContext(), skillTreeActivity, type, reduced);
        setListAdapter(skillFragmentAdapter);
        getListView().setClickable(true);
        getListView().setDivider(null);

        // set margin
        int margin = (int) getContext().getResources().getDimension(R.dimen.standart_margin);
        getListView().setDividerHeight(margin);

        // remove selector color
        getListView().setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

}
