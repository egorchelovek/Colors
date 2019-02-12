package com.joyfulmagic.colors.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.joyfulmagic.colors.R;
import com.joyfulmagic.colors.utils.ColorHarmonizer;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by egorchan on 01.04.17.
 *
 * Happy little joke from Osho for You, Dude!!!
 *
 * A woman in a supermarket to buy some broccoli.
 * She went up to the man in the vegetable department and said, 'Sir, do you have any broccoli?'
 * The man replied, 'No, ma'am, none today. Come back tomorrow. '
 * A few hours later, the woman was back again, asking the man, 'Sir, do you have any broccoli?'
 * 'Look, lady, I have told you, we do not have any broccoli today.'
 * The lady left, only to return again that same day.
 * By this time, the man was exasperated and said, 'What does t-o-m spell in the word tomato?'
 * She replied, 'Tom.'
 * 'And what does p-o-t spell in the word potato?' He asked.
 * 'Pot,' was the reply,
 * He then said, 'And what does f-u-c-k spell in the word broccoli?'
 * She looked puzzled and said, "There's no fuck in broccoli."
 * He sighed a deep sigh and exclaimed, 'Lady, that's what I've been trying to tell you all day!'
 */
public class AppInfoActivity extends BaseActivity implements View.OnTouchListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitles(getString(R.string.activity_app_info));
        setContentView(R.layout.app_info);

        // init my page link
        TextView link = (TextView) findViewById(R.id.linkCreatorPageText);
        link.setText(" "+link.getText());
        link.setClickable(true);
        link.setOnTouchListener(this);
        int colorChar = link.getCurrentTextColor();
        int addColor = ColorHarmonizer.getCompletary(colorChar)[1];
        link.setBackgroundColor(addColor);

        // init app name text
        TextView textView = (TextView) findViewById(R.id.textLogo);
        textView.setText(SplashActivity.harmonyColoredText);
        textView.setTypeface(SplashActivity.beautifulFont);
        textView.setTextSize(textView.getTextSize() * 2);

        // startup animation
        ImageView imageView = (ImageView) findViewById(R.id.imageLogo);
        Animation animation = AnimationUtils.loadAnimation(getApplication(), R.anim.rotate);
        animation.setDuration(5000);
        imageView.startAnimation(animation);

    }

    /**
     * Start facebook page with my profile
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final String urlFb = "fb://page/"+getString(R.string.my_page_id);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(urlFb));

        // If a Facebook app is installed, use it. Otherwise, launch
        // a browser
        final PackageManager packageManager = getPackageManager();
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() == 0) {
            final String urlBrowser = "https://www.facebook.com/pages/"+getString(R.string.my_page_id);
            intent.setData(Uri.parse(urlBrowser));
        }

        startActivity(intent);

        return false;
    }
}
