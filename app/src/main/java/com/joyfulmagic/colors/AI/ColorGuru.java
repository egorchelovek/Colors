package com.joyfulmagic.colors.AI;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.joyfulmagic.colors.databases.ColorDatabase.ColorDatabase;
import com.joyfulmagic.colors.databases.ColorDatabase.ColorString;
import com.joyfulmagic.colors.databases.UserDatabase.ColorCard;
import com.joyfulmagic.colors.activities.UserActivity.Skill;
import com.joyfulmagic.colors.activities.UserActivity.User;
import com.joyfulmagic.colors.databases.UserDatabase.UserDatabase;
import com.joyfulmagic.colors.utils.ColorConverter;
import com.joyfulmagic.colors.utils.ColorGenerator;
import com.joyfulmagic.colors.activities.SettingsActivity.Settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


/**
 * This class will keeping information about
 * sanyasin or user of this app. He must to know
 * information about him. And he must know some
 * methods of teaching (because he is Guru).
 * He should to give useful lessons for user.
 */
public class ColorGuru {

    // DBses
    private ColorDatabase colorsDatabase; // here colors
    private UserDatabase usersDatabase; // here colors and skills with some info

    private Cursor colorsForLearn; // cursor from DB

    private User user; // user model
    public ArrayList<ColorTask> cards; // task cards

    // Guru constructor
    public ColorGuru(ColorDatabase colorDatabase, UserDatabase userDatabase){

        // link bases
        colorsDatabase = colorDatabase;
        usersDatabase = userDatabase;

        // alloc mem
        cards = new ArrayList<ColorTask>();
        user = new User();
    }

    // User getter
    public User getUser() {
        return user;
    }

    public void makeUserTable(String userName){

        // set user name
        user.name = userName;

        // write new user to DB
        if(!usersDatabase.userNameExist(userName)){

            // new username load to users DB
            usersDatabase.writeNewUser(userName);

            // load color set for user
            colorsForLearn = colorsDatabase.getAllBasicColors();
            usersDatabase.loadNewColorSet(user.name, colorsForLearn);

            // load skills notes
            for(int i = 1; i < Settings.skills.length; i++) {
                ColorString colorString = new ColorString("skill", Settings.skills[i]);
                usersDatabase.loadColorString(user.name, colorString);
            }

        }

        // !
        updateUserInfo();
    }

    /**
     * Update info about user
     */
    public void updateUserInfo(){

        Skill s = user.getSkill("Color");
        s.level = 0;
        s.maxLevel = 0;

        Cursor c = usersDatabase.getUserAllColors(user.name);

        if(c!=null) {
            c.moveToFirst();

            while (c.moveToNext() == true) {
                if(c.getCount() > 0) {
                    ColorCard note = new ColorCard(c);

                    // color
                    if (!note.isSkill()) {
                        s = user.getSkill("Color");
                        if (s != null) {
                            // get last repeat date
                            if (s.lastRepeatDate.before(note.date))
                                s.lastRepeatDate = note.date;

                            // get number of colors which is already learned
                            if (!note.status_learn) {
                                s.level++;
                            }
                            s.maxLevel++;

                            // another doesn't matter
                        }
                    } else {
                        // skill

                        s = user.getSkill(note.bad_hex);
                        if (s != null) {

                            // get last repeat date
                            if (s.lastRepeatDate.before(note.date))
                                s.lastRepeatDate = note.date;

                            // temporary hack
                            s.level = note.answers;
                            s.maxLevel = note.shows;
                        }
                    }
                }
            }
        }

        // then calculate combined skills
        // ...

    }

    /**
     * Make task consist of some color cards...
     * @return true or false
     */
    public boolean makeUserTask() {

        if(cards != null) {

            cards.clear();

            colorsForLearn = usersDatabase.getUserLearnColors(user.name);
            colorsForLearn.moveToFirst();

            // create task cards with basic colors or skills from DB
            for (int i = 0; i < Settings.maxTaskNumber; i++) {
                int j = Settings.train;
                if (j == 0) {
                    j = 1 + ColorGenerator.randMax(3);
                }

                cards.add(makeTaskByType(j));
            }

            // after that fulfill additional colors of cards
            // (post-processing of cards)
            for (ColorTask t : cards) {
                if (t.train == 1 && t.status == true) {

                    int numAddColors = 0;
                    // var. 1
                    // it's bad cause copy constructor is needed
                    // cause we need to change status of color
                    // and that is impossible if object is one
                    // (I don't wanna do this stuff...)
//                for (ColorTask t1 : cards){
//                    if(t1.train == 1) {
//                        if (!t1.colorSkill.get(0).equals(t.colorSkill.get(0))){
//                            t.colorSkill.add(t1.colorSkill.get(0));
//                            numAddColors++;
//                            if(numAddColors == 3) break;
//                        }
//                    }
//                }

                    // var. 2
                    while (numAddColors < 3) {

                        ColorCard cc = getNextColorCardFromDBInSetedRange();

                        if (cc != null) {
                            if (cc.bad_hex != t.colorSkill.get(0).bad_hex) {
                                t.colorSkill.add(cc);
                                numAddColors++;
                            }
                        }
                    }

                } else if (Settings.useOnlyDBColors) {
                    // if only DB colors is used
                    ColorCard cc = getNextColorCardFromDBInSetedRange();
                    if (cc != null) t.colorSkill.add(cc);
                }
            }

        }

        return true;
    }

    // switcher of task creation
    public ColorTask makeTaskByType(int type) {
        ColorTask t = null;
        switch (type) {

            case 2:
                t = makeSpaceTask();
                break;
            case 3:
                t = makeHarmonyTask();
                break;
            default:
                t = makeColorTask();

        }
        return t;
    }

    // HSL-space task maker
    public ColorTask makeSpaceTask(){
        ColorTask t = new ColorTask();

        // set train type
        t.train = 2;
        t.type = Settings.parameter;

        // load color space parameters to the task
        switch (t.type){
            case 1:
                t.colorSkill.add(getColorSkill("Hue"));
                break;
            case 2:
                t.colorSkill.add(getColorSkill("Saturation"));
                break;
            case 3:
                t.colorSkill.add(getColorSkill("Lightness"));
                break;
            case 4:
                t.colorSkill.add(getColorSkill("Hue"));
                t.colorSkill.add(getColorSkill("Saturation"));
                break;
            case 5:
                t.colorSkill.add(getColorSkill("Hue"));
                t.colorSkill.add(getColorSkill("Lightness"));
                break;
            case 6:
                t.colorSkill.add(getColorSkill("Saturation"));
                t.colorSkill.add(getColorSkill("Lightness"));
                break;
            default:
                t.colorSkill.add(getColorSkill("Hue"));
                t.colorSkill.add(getColorSkill("Saturation"));
                t.colorSkill.add(getColorSkill("Lightness"));
        }

        // set status for showing help message
        t.status = true;
        for (ColorCard c: t.colorSkill) {
            if(c.isNew()) {t.status = false; break;}
        }

        // load additional colors
        if(Settings.useOnlyDBColors == false){
            t.colors.add(ColorGenerator.randColor());
        }

        return t;
    }

    // Harmony task maker
    public ColorTask makeHarmonyTask(){

        ColorTask t = new ColorTask();

        // set train type
        t.train = 3;
        t.type = Settings.harmony;

        // load color space parameters to the task
        if(t.type == 0){
            // drop the bones when type is undefined zero
            t.type = 1 + ColorGenerator.randMax(6);
            // result is from 1 to 6
        }
        switch (t.type){
            case 2:
                t.colorSkill.add(getColorSkill("Analogous"));
                break;
            case 3:
                t.colorSkill.add(getColorSkill("Triad"));
                break;
            case 4:
                t.colorSkill.add(getColorSkill("Split-Complementary"));
                break;
            case 5:
                t.colorSkill.add(getColorSkill("Tetradic"));
                break;
            case 6:
                t.colorSkill.add(getColorSkill("Square"));
                break;
            default:
                t.colorSkill.add(getColorSkill("Complementary"));
        }

        // set status for showing help message
        t.status = true;
        for (ColorCard c: t.colorSkill) {
            if(c.isNew()) {t.status = false; break;}
        }

        // load additional colors
        if(Settings.useOnlyDBColors == false){
            t.colors.add(ColorGenerator.randColor());
        }

        return t;
    }
    // satellite of Harmony task maker
    public ColorCard getColorSkill(String hex){
        return new ColorCard(usersDatabase.getColorByHex(user.name, hex));
    }

    // Color task maker
    public ColorTask makeColorTask(){

        ColorTask t = new ColorTask();

        // set train type
        t.train = 1;
        t.type = Settings.subset;
        t.status = true;

        // add color card to task
        ColorCard cc = getNextColorCardFromDBInSetedRange();
        cc.type = true; // for update info after task completion
        if(cc != null) t.colorSkill.add(cc);

        return t;
    }
    // get next color from DB in seted range
    private ColorCard getNextColorCardFromDBInSetedRange(){
        ColorCard cc = null;

        // check colors set loaded
        if(colorsForLearn == null){
            // and load if it is necessary
            colorsForLearn = usersDatabase.getUserLearnColors(user.name);
//            colorsForLearn.moveToFirst();
        } else if (colorsForLearn.isLast()){
            // back to begin position if it is necessary
            colorsForLearn.moveToFirst();
        }


        // get color from DB
        boolean colorCatched = false;
        while(!colorsForLearn.isLast() && !colorCatched){

            // some filtering of of colors...
            ColorCard tcc = new ColorCard(colorsForLearn);

            // if is not user's skill note but color
            if(!tcc.isSkill()) {

                // if color is in right range
                //
                System.out.println("CCGCC: " + ColorConverter.getColor(tcc.hex));
                System.out.println("CCGCS: " + Settings.getColor());
                if (ColorConverter.getColor(tcc.hex) == Settings.getColor()
                        || Settings.getColor() == Settings.getColor(0)) {
                    cc = tcc;
                    colorCatched = true;
                }

            }

            //
            colorsForLearn.moveToNext();
        }

        return cc;
    }



    /**
     * tasks was completed,
     * there is time to simple update user table in database
     */
    public void checkTrainingResults() {

        for (ColorTask task : cards) {
            // for every right card in the task update the row
            for(ColorCard cc : task.colorSkill){
                if(cc.type == true){

                    // write color card info to base
                    ColorCard updateCard = new ColorCard(usersDatabase.getColorByHex(user.name, cc.bad_hex));
                    updateCard.update();
                    usersDatabase.updateColor(user.name, updateCard);
                }
            }
        }

        // then delete task
        cards.clear();

        updateUserInfo();
    }
}
