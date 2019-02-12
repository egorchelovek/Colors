package com.joyfulmagic.colors.activities.UserActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joyfulmagic.colors.AI.ColorGuru;
import com.joyfulmagic.colors.R;
import com.joyfulmagic.colors.activities.BaseActivity;
import com.joyfulmagic.colors.AI.GuruIncarnation;

/**
 * User activity is for viewing User progress.
 * TODO make it more graphical.
 */
public class UserActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitles(getResources().getString(R.string.activity_progress));

        setContentView(R.layout.progress);

        // get user info
        ColorGuru colorGuru = GuruIncarnation.colorGuru;
        colorGuru.updateUserInfo();
        User u = colorGuru.getUser();

        // showing user info in text views
        LinearLayout l = (LinearLayout) findViewById(R.id.userLayout);
        for(int i = 0; i < u.skills.size(); i++){
            TextView textView = new TextView(getApplicationContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.gravity= Gravity.CENTER;
            lp.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.standart_margin));
            textView.setText(u.skills.get(i).name + " " + u.skills.get(i).level);
            l.addView(textView);
        }

    }
}
