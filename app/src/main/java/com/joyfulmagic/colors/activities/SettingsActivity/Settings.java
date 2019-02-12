package com.joyfulmagic.colors.activities.SettingsActivity;

import android.content.Context;
import android.content.SharedPreferences;
import com.joyfulmagic.colors.R;

import java.util.Locale;

/**
 * Settings of program class
 * for save, load, set and use
 * different regimes of program
 */
public class Settings {

    public static boolean initialized = false;

    private SharedPreferences sharedPreferences; // structure to saving settings after shutdown

    public static Locale locale; // current locale

    // arrays
    public static String[] languages = {"ENG", "RUS"}; // main languages connected with available color name languages
    public static String[] sets = {"Web", "12", "6", "3"}; // color sets connected with color tables

    // sets of...
    public static String[] colors; // set of color ranges
    public static String[] parameters; // set of parameters combinations
    public static String[] harmonies; // set of main harmony types
    public static String[] trains;
    public static String[] skills;

    // setted indexes of sets
    public static int language;
    public static int set, subset;
    public static int parameter;
    public static int harmony;
    public static int train;

    // database colors usage flag
    public static boolean useOnlyDBColors;

    // maximal number of cards in one task
    public static int maxTaskNumber = 10;

    // initialization with constructor
    public Settings(Context context){
        skills = context.getResources().getStringArray(R.array.skills);
        trains = context.getResources().getStringArray(R.array.trains);
        harmonies = context.getResources().getStringArray(R.array.harmonies);
        parameters = context.getResources().getStringArray(R.array.parameters);
        colors = context.getResources().getStringArray(R.array.colors);

        locale = context.getResources().getConfiguration().locale;
        if(locale != null && locale.getLanguage() == "ru_RU") language = 1;
    }

    // load settings at app start
    public static void loadSettings(SharedPreferences sharedPreferences){
        int perset = 0;
        if(locale != null && locale.getLanguage() == "ru_RU") perset = 1;
        language = sharedPreferences.getInt("language", perset);
        useOnlyDBColors = sharedPreferences.getBoolean("useDBColors", false);
    }

    // save setting at app pause
    public static void saveSettings(SharedPreferences sharedPreferences){
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt("language", language);
        ed.putBoolean("useDBColors", useOnlyDBColors);
        ed.apply();
    }

    /**
     * Strange methods... and what todo?
     */
    public static String getLanguage(){
        return languages[language];
    }
    public static String getSet(){
        return sets[set];
    }

    public static String getColor(){ return colors[subset]; }
    public static String getColor(int index){ return colors[index]; }

    public static String getTrain(){ return trains[train]; }
    public static String getTrain(int index){ return trains[index]; }

}
