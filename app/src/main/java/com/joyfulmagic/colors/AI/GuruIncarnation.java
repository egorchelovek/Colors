package com.joyfulmagic.colors.AI;

import android.content.Context;

import com.joyfulmagic.colors.databases.ColorDatabase.ColorDatabase;
import com.joyfulmagic.colors.databases.UserDatabase.UserDatabase;

/**
 * Simple mechanism of incarnation of Guru (AI).
 * Simple holder of Guru.
 */
public class GuruIncarnation {

    public static ColorGuru colorGuru;

    public GuruIncarnation(ColorDatabase colorDatabase, UserDatabase userDatabase){
        colorGuru = new ColorGuru(colorDatabase, userDatabase);
    }
}
