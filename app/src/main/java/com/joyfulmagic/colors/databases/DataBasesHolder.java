package com.joyfulmagic.colors.databases;

import android.content.Context;

import com.joyfulmagic.colors.databases.ColorDatabase.ColorDatabase;
import com.joyfulmagic.colors.databases.UserDatabase.UserDatabase;

/**
 * Holder of color and user databases
 * for share all databases between all activities/
 */
public class DataBasesHolder {

    public static ColorDatabase colorDatabase;
    public static UserDatabase userDatabase;

    // need to be created at once
    public DataBasesHolder(Context context){

        // creation of DBses
        ColorDatabase colorDatabase = new ColorDatabase(context);
        UserDatabase userDatabase = new UserDatabase(context);

        // linkage
        DataBasesHolder.colorDatabase = colorDatabase;
        DataBasesHolder.userDatabase = userDatabase;
    }
}
