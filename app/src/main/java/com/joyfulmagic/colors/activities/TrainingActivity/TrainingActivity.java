package com.joyfulmagic.colors.activities.TrainingActivity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;

import com.joyfulmagic.colors.activities.BaseActivity;
import com.joyfulmagic.colors.activities.ColorEncyclopedy.FragmentColorInfo;
import com.joyfulmagic.colors.AI.ColorGuru;
import com.joyfulmagic.colors.AI.ColorTask;
import com.joyfulmagic.colors.R;
import com.joyfulmagic.colors.activities.TrainingActivity.TrainingFragments.FragmentFourColors;
import com.joyfulmagic.colors.activities.TrainingActivity.TrainingFragments.FragmentFourNames;
import com.joyfulmagic.colors.activities.TrainingActivity.TrainingFragments.FragmentLearnHarmony;
import com.joyfulmagic.colors.activities.TrainingActivity.TrainingFragments.FragmentTrainParameter;
import com.joyfulmagic.colors.AI.GuruIncarnation;
import com.joyfulmagic.colors.utils.Messenger;
import com.joyfulmagic.colors.utils.RandGen;

import java.util.ArrayList;
import java.util.List;

/**
 * Training activity is showing task of Guru to User.
 * And then User may pass this task to increase his level
 * in some kind of color skill...
 */
public class TrainingActivity extends BaseActivity {

    private ColorGuru colorGuru; // main AI object link

    // fragment manager
    private FragmentManager manager;
    private FragmentTransaction transaction;

    // fragments
    private TrainingFragment followFragment;
    private FragmentColorInfo fragmentColorInfo;
    private FragmentFourColors fragmentFourColors;
    private FragmentFourNames fragmentFourNames;
    private FragmentTrainParameter fragmentTrainParameter;
    private TrainingFragment fragmentLearnHarmony;

    // list of task
    ArrayList<ColorTask> tasks;
    int taskIdx; // follow task idx
    int progressIdx; // follow progress idx

    // progress bar of training task
    ProgressBar horizontalProgressBar;

    // little happy answers messages
    private String[] messages_right_answers;
    private String[] messages_wrong_answers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitles(getResources().getString(R.string.activity_training));

        setContentView(R.layout.training);

        // then this class use color task to show task on the screen
        manager = getSupportFragmentManager();

        // create basic fragments
        fragmentFourColors = new FragmentFourColors();
        fragmentFourNames = new FragmentFourNames();
        fragmentColorInfo = new FragmentColorInfo();
        fragmentTrainParameter = new FragmentTrainParameter();
        fragmentLearnHarmony = new FragmentLearnHarmony();

        // init by first fragment
        // and what a first fragment?
        transaction = manager.beginTransaction();
        transaction.add(R.id.train_card_container, fragmentColorInfo);
        transaction.commit();

        // so, Color Guru must create 10 color task
        colorGuru = GuruIncarnation.colorGuru;
        if(colorGuru.makeUserTask()){

            tasks = colorGuru.cards;
            if(tasks != null){
                taskIdx = 0;
                progressIdx = 0;
                setTask(tasks.get(taskIdx));

                // some progress bar add here (next step I want to make rainbow)
                horizontalProgressBar = (ProgressBar) findViewById(R.id.train_progress);
                horizontalProgressBar.setMax(tasks.size());
            }

        } else{
            finish();
        }

        // init answers arrays values
        messages_right_answers = getResources().getStringArray(R.array.messages_right_answer);
        messages_wrong_answers = getResources().getStringArray(R.array.messages_wrong_answer);
    }

    /**
     * Check task, change task
     */
    public void onClickTaskOk(View view){

        boolean result = checkTask();
        if(taskIdx < tasks.size() - 1) {
            // hop, hop, hop, solving tasks
            taskIdx++;
            setTask(tasks.get(taskIdx));

            if(result) {
                progressIdx++;
                horizontalProgressBar.setProgress(progressIdx);
            }
        } else {
            // ok, we do all tasks
            // lets Guru update the Database
            colorGuru.checkTrainingResults();

            // and exit from training activity
            finish();
        }
    }

    /**
     * Set fragment by task
     */
    public void setTask(ColorTask task) {

        // knowing which fragment to use
        switch(task.train){
            case 2: // Space
                followFragment = fragmentTrainParameter;
                break;

            case 3: // Harmony
                followFragment = fragmentLearnHarmony;
                break;

            default: // Color
                if(task.status == false){
                    followFragment = fragmentColorInfo;
                }
                else {
                    followFragment = fragmentFourColors;
//                    if(Math.random() > 0.5f) {
//                        followFragment = fragmentFourColors;
//                    } else {
//                        followFragment = fragmentFourNames;
//                    }
                }
        }

        // adjust fragment to new task
        followFragment.setTask(task);

        // and replace follow fragment by new
        transaction = manager.beginTransaction();
        transaction.replace(R.id.train_card_container, followFragment);
        transaction.commit();
    }

    /**
     * Check task, show message (right or wrong)
     */
    public boolean checkTask() {

        // get follow fragment
        boolean result = followFragment.getResult();

        if(result){
            // result is right

            if(messages_right_answers != null) {
                int randIdx = RandGen.randMax(messages_right_answers.length - 1);
                Messenger.show(getApplicationContext(), messages_right_answers[randIdx]);
            }

        } else {
            // result is wrong
            // make new task in the end for repeat
            // it's not very clever but now it's not important

            tasks.add(followFragment.getTask());
            // better way is move this task to the end of list but how to make another?

            if(messages_wrong_answers != null) {
                int randIdx = RandGen.randMax(messages_wrong_answers.length - 1);
                Messenger.show(getApplicationContext(), messages_wrong_answers[randIdx]);
            }
        }

        return result;
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        //Handle the back button
//        if(keyCode == KeyEvent.KEYCODE_BACK ||
//                keyCode == KeyEvent.KEYCODE_HOME ||
//                keyCode == KeyEvent.KEYCODE_MOVE_HOME ||
//                keyCode == KeyEvent.KEYCODE_UNKNOWN) {
//            //Ask the user if they want to quit
//            new AlertDialog.Builder(this)
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .setTitle(R.string.quit)
//                    .setMessage(R.string.message_progress_lost)
//                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                            //Stop the activity
//                            TrainingActivity.this.finish();
//                        }
//
//                    })
//                    .setNegativeButton(R.string.cancel, null)
//                    .show();
//
//            return true;
//        }
//        else {
//            return super.onKeyDown(keyCode, event);
//        }
//    }
}
