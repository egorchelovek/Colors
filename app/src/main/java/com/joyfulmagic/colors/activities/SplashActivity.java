package com.joyfulmagic.colors.activities;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.joyfulmagic.colors.AI.GuruIncarnation;
import com.joyfulmagic.colors.R;
import com.joyfulmagic.colors.activities.CameraActivity.Camera2Activity;
import com.joyfulmagic.colors.activities.CameraActivity.CameraActivity;
import com.joyfulmagic.colors.activities.SettingsActivity.Settings;
import com.joyfulmagic.colors.databases.DataBasesHolder;
import com.joyfulmagic.colors.utils.AdvancedMetrics.AdvancedDisplayMetrics;
import com.joyfulmagic.colors.utils.ColorGenerator;
import com.joyfulmagic.colors.utils.ColorHarmonizer;
import com.joyfulmagic.colors.views.GradientsHolder;

import java.sql.Time;

/**
 * Splash activity is viewed on startup:
 * app name + some color animation
 * and initialization at the same time...
 */
public class SplashActivity extends Activity {


    public static Typeface beautifulFont;
    public static SpannableStringBuilder harmonyColoredText;
    public static int[] colorArray = ColorHarmonizer.getSquare(Color.GREEN);
//    int[] colorArray = {
//            Color.MAGENTA,
//            Color.BLUE,
//            Color.CYAN,
//            Color.GREEN,
//            Color.YELLOW,
//            Color.RED
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        beautifulFont = Typeface.createFromAsset(getAssets(), "fonts/Nickainley-Normal.ttf");

        // colorful text
        final TextView textView = (TextView) findViewById(R.id.textViewSplash);
        harmonyColoredText = new SpannableStringBuilder(textView.getText());

        int j = 0;
        for(int i = 0; i < textView.getText().length(); i++){
            harmonyColoredText.setSpan(new ForegroundColorSpan(colorArray[j]), i, i + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            j++;
            if(j >= colorArray.length) j = 0;
        }
        textView.setTextSize(textView.getTextSize() * 2);
        textView.setText(harmonyColoredText);
        textView.setTypeface(beautifulFont);

        // startup animation
        ImageView imageView = (ImageView) findViewById(R.id.imageViewSplash);
        Animation animation = AnimationUtils.loadAnimation(getApplication(), R.anim.rotate);
        imageView.startAnimation(animation); // animation works with lags

        Thread initializer = new Thread(new Runnable() {
            public void run() {

                // initialize settings
                Settings s = new Settings(getApplicationContext());
                Settings.loadSettings(getPreferences(MODE_PRIVATE));

                // create objects to work with databases
                DataBasesHolder dbh = new DataBasesHolder(getApplicationContext());

                // then make incarnation of guru
                if (GuruIncarnation.colorGuru == null) {
                    GuruIncarnation guruIncarnation = new GuruIncarnation(DataBasesHolder.colorDatabase, DataBasesHolder.userDatabase);
                    GuruIncarnation.colorGuru.makeUserTable("Man");
                }

                // get advanced metrics
                AdvancedDisplayMetrics advancedDisplayMetrics = new AdvancedDisplayMetrics(getApplicationContext());
                int size = AdvancedDisplayMetrics.link.getSubsizes(0)[0];

                // initialize some gradients with holder...
                GradientsHolder gh = new GradientsHolder(size, 12);


                // start main activity
                Intent intent;
                if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    intent = new Intent(getApplicationContext(), CameraActivity.class);
                } else{
                    intent = new Intent(getApplicationContext(), Camera2Activity.class);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }


        });
        initializer.setPriority(Thread.MIN_PRIORITY);
        initializer.start();

    }
}
