package com.joyfulmagic.colors.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.joyfulmagic.colors.activities.CameraActivity.Camera2Activity;
import com.joyfulmagic.colors.activities.CameraActivity.CameraActivity;
import com.joyfulmagic.colors.activities.ColorEncyclopedy.ColorEncyclopediaActivity;
import com.joyfulmagic.colors.R;
import com.joyfulmagic.colors.activities.SettingsActivity.Settings;
import com.joyfulmagic.colors.activities.SettingsActivity.SettingsActivity;
import com.joyfulmagic.colors.activities.SkillTreeActivity.SkillTreeActivity;
import com.joyfulmagic.colors.activities.UserActivity.UserActivity;

/**
 * Base activity contains action bar
 * to show in all activities of this app.
 */
public class BaseActivity extends FragmentActivity {

    public static int actionBarSize;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Settings.saveSettings(getPreferences(MODE_PRIVATE));
    }

    @Override
    protected void onPause() {
        super.onPause();

        Settings.saveSettings(getPreferences(MODE_PRIVATE));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Settings.loadSettings(getPreferences(MODE_PRIVATE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Selection menu handler
     * @param item selected item of action bar
     * @return be, me, what?
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Class<?> activityClass = null;
        Intent intent = null;

        switch (item.getItemId()){

            case R.id.itemActivityCamera:
                if(getTitle() != getString(R.string.activity_camera))
                    if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        activityClass = CameraActivity.class;
                    } else {
                        activityClass = Camera2Activity.class;
                    }
                break;

            case R.id.itemActivityEncyclopedia:
                if(getTitle() != getString(R.string.activity_encyclopedia))
                    activityClass = ColorEncyclopediaActivity.class;
                break;

            case R.id.itemActivitySkillTree:
                if(getTitle() != getString(R.string.activity_skill_tree))
                    activityClass = SkillTreeActivity.class;
                break;

//            case R.id.itemActivityProgress:
//                if(getTitle() != getString(R.string.activity_progress))
//                    activityClass = UserActivity.class;
//                break;

            case R.id.itemActivitySettings:
                if(getTitle() != getString(R.string.activity_settings))
                    activityClass = SettingsActivity.class;
                break;

            case R.id.itemActivityAppInfo:
                if(getTitle() != getString(R.string.activity_app_info))
                    activityClass = AppInfoActivity.class;
                break;

//            case R.id.itemActivityThank:
//                if(getTitle() != getString(R.string.activity_thank))
//                break;
        }

        // start intent
        if(activityClass != null) {
            intent = new Intent(this, activityClass);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Set common titles
     * @param title text of title
     */
    public void setTitles(String title){

        setTheme(android.R.style.Theme_Material);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActionBar().hide();
        getActionBarHeight();
        getActionBar().setBackgroundDrawable(null);
        getActionBar().setTitle(title);
        setTitle(title);
        getActionBar().show();

    }

    /**
     * It's function for prediction of some margins
     * @return action bar height
     */
    private int getActionBarHeight() {

        // var. 1
        int actionBarHeight = getActionBar().getHeight();
        if (actionBarHeight != 0) return actionBarHeight;

        // var. 2
        final TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        actionBarSize = actionBarHeight;
        return actionBarHeight;
    }
}
