package com.joyfulmagic.colors.views.Checkable;

import android.content.Context;
import android.view.View;
import android.widget.Checkable;

/**
 * Simple checkable view
 */
public class CheckableView extends View implements Checkable {

    private static final int[] CheckedStateSet = { android.R.attr.state_checked };
    private boolean checked = false;

    public CheckableView(Context context) {
        super(context);
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
        refreshDrawableState();
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        checked = !checked;
        refreshDrawableState();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CheckedStateSet);
        }
        return drawableState;
    }
}