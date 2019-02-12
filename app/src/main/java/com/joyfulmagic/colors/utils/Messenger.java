package com.joyfulmagic.colors.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Short message maker
 */
public class Messenger {

    public static void show(Context c, String message){
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }
}
