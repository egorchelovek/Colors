package com.joyfulmagic.colors.activities.TrainingActivity.TrainingFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joyfulmagic.colors.R;
import com.joyfulmagic.colors.activities.TrainingActivity.TrainingFragment;

/**
 * Void baby, void!
 */
public class FragmentFourNames extends TrainingFragment implements View.OnClickListener{

    public final static String TAG = "FourNamesFrag";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.training_fragment_four_names,null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {}
}
