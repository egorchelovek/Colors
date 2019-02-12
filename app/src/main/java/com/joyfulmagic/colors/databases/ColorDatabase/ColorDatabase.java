package com.joyfulmagic.colors.databases.ColorDatabase;

import com.joyfulmagic.colors.activities.SettingsActivity.Settings;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

/**
 * ColorDatabase shell of database in asset folder
 */
public class ColorDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "colors.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "basic_web_colors";

    public static final String[] COLUMN_NAME_NAMES_COLORS = {"NAME_ENG", "NAME_RUS"};
    public static final String COLUMN_NAME_CODE_HEX = "CODE_HEX";
    public static final String COLUMN_NAME_VALUE_R = "VALUE_R";
    public static final String COLUMN_NAME_VALUE_G = "VALUE_G";
    public static final String COLUMN_NAME_VALUE_B = "VALUE_B";

    public ColorDatabase(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

    // return
    public Cursor getAllBasicColors() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);

        Cursor c = qb.query(db, new String[]{
                "0 _id",
                COLUMN_NAME_NAMES_COLORS[0],
                COLUMN_NAME_NAMES_COLORS[1],
                COLUMN_NAME_CODE_HEX},
                null, null,
                null, null, null);

        c.moveToFirst();

        return c;
    }

    // variant with RGB values columns
    public Cursor getAllBasicColorsRGB() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);

        Cursor c = qb.query(db, new String[]{
                        "0 _id",
                        COLUMN_NAME_NAMES_COLORS[0],
                        COLUMN_NAME_NAMES_COLORS[1],
                        COLUMN_NAME_CODE_HEX,
                        COLUMN_NAME_VALUE_R,
                        COLUMN_NAME_VALUE_G,
                        COLUMN_NAME_VALUE_B},
                null, null,
                null, null, null);

        c.moveToFirst();

        return c;
    }
}