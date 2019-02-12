package com.joyfulmagic.colors.activities.TrainingActivity;

import android.graphics.Color;
import android.support.v4.app.Fragment;

import com.joyfulmagic.colors.AI.ColorTask;
import com.joyfulmagic.colors.databases.UserDatabase.ColorCard;
import com.joyfulmagic.colors.utils.ColorGenerator;

/**
 * Training fragment base class
 * for unified setting tasks and checking the results
 */
public abstract class TrainingFragment extends Fragment {

    private ColorTask task;



    public boolean setTask(ColorTask task) {
        if(task != null) {
            this.task = task;
            return true;
        }
        return false;
    }

    public ColorTask getTask() {
        return task;
    }



    public void setResult(boolean result){
        if(task != null)
            task.result = result;
    }

    public boolean getResult(){
        if(task != null)
        return task.result;
        return false;
    }



    public int getColorFromTask(){
        int followColor = Color.RED;
        if(task!= null) {
            if (task.colors.isEmpty()) {
                ColorCard cc = task.colorSkill.get(task.colorSkill.size() - 1);
                if (cc.isSkill()) {
                    followColor = ColorGenerator.randColor();
                } else {
                    followColor = Color.parseColor(cc.hex);
                }
            } else {
                followColor = task.colors.get(0);
            }
        }
        return followColor;
    }
}
