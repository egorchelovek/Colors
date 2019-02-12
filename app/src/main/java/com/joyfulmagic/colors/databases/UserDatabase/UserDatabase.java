package com.joyfulmagic.colors.databases.UserDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.joyfulmagic.colors.databases.ColorDatabase.ColorDatabase;
import com.joyfulmagic.colors.databases.ColorDatabase.ColorString;
import com.joyfulmagic.colors.utils.Timer;

/**
 * User database for saving progress of user
 */
public class UserDatabase extends SQLiteOpenHelper {

    // Database
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;

    // Table USERS
    private static final String TABLE_NAME_USERS = "USERS";
    private static final String COLUMN_NAME_NAME_USER = "NAME";

    private static final String USERS_CREATE_TABLE_QUERY =
            "create table " + TABLE_NAME_USERS +" ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME_NAME_USER +" TEXT" + ");";

    // Table of USERNAME
    public static final String[] COLUMN_NAME_NAMES_COLORS = ColorDatabase.COLUMN_NAME_NAMES_COLORS;
    public static final String COLUMN_NAME_CODE_HEX = ColorDatabase.COLUMN_NAME_CODE_HEX;
    public static final String COLUMN_NAME_STATUS_LEARN = "STATUS_LEARN";
    public static final String COLUMN_NAME_NUMBER_SHOWS = "NUMBER_SHOWS";
    public static final String COLUMN_NAME_NUMBER_ANSWERS = "NUMBER_ANSWERS";
    public static final String COLUMN_NAME_DATE_SHOW = "DATE_SHOW";

    // creation of table query getter
    private String getUsernameCreateTableQuery(String userName){
        return "create table " + userName + " ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME_NAMES_COLORS[0] +" TEXT,"
                + COLUMN_NAME_NAMES_COLORS[1] +" TEXT,"
                + COLUMN_NAME_CODE_HEX +" TEXT,"
                + COLUMN_NAME_STATUS_LEARN + " BOOLEAN,"
                + COLUMN_NAME_NUMBER_SHOWS + " INTEGER,"
                + COLUMN_NAME_NUMBER_ANSWERS + " INTEGER,"
                + COLUMN_NAME_DATE_SHOW + " TIMESTAMP" + ");";
    }

    // app context
    private Context context;
    
    
    
    // Constructor
    public UserDatabase(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USERS_CREATE_TABLE_QUERY);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    /**
     * Add new user to database
     * @param userName name of user
     */
    public void writeNewUser(String userName){
        
        SQLiteDatabase db = this.getWritableDatabase();

        // insert new user in table USERS
        ContentValues newUser = new ContentValues();
        newUser.put(COLUMN_NAME_NAME_USER, userName);
        db.insert(TABLE_NAME_USERS, null, newUser);

        // making new table for USERNAME
        db.execSQL(getUsernameCreateTableQuery(userName));

    }

    /**
     * Check for user exist
     * @param userName name of user
     * @return yes or no
     */
    public boolean userNameExist(String userName){

        boolean result = false;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_NAME_USERS, new String[]{COLUMN_NAME_NAME_USER}, null, null, null, null, null);
        
        if(c != null) {
            if(c.getCount() > 0) {
                c.moveToFirst();
                boolean exitFlag = false;
                while (exitFlag != true) {

                    String userNameFromTable = c.getString(c.getColumnIndex(COLUMN_NAME_NAME_USER));

                    if (userNameFromTable.equals(userName)) {

                        result = true;
                        break;
                    }

                    exitFlag = !c.moveToNext();
                }
            }
        }

        return result;
    }

    /**
     * Get all colors which user is should to learn
     * @param userName name of user
     * @return cursor with colors
     */
    public Cursor getUserLearnColors(String userName){

        Cursor c = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            c = db.query(userName, null,
                    COLUMN_NAME_STATUS_LEARN + " != ?",
                    new String[]{"false"},
                    null, null, "random()");

            // hm, there is no function RAND(), try random() what if in different versions this function absent
            
            c.moveToFirst();
        }
        catch (IllegalStateException e){
            e.printStackTrace();
        }
        return c;
    }


    /**
     * All users color getter
     * @param userName name of dude
     * @return colors
     */
    public Cursor getUserAllColors(String userName){

        Cursor c = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            c = db.query(userName, null,
                    null,
                    null,
                    null, null, null);
            // hm, there is no function RAND(), try random() what if in different versions this function absent
            
            c.moveToFirst();
        }
        catch (IllegalStateException e){
            e.printStackTrace();
        }
        return c;
    }
    
    // return cursor of color from user name and hex code
    public Cursor getColorByHex(String userName, String hex){
        Cursor c = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            c = db.query(userName, null,
                    COLUMN_NAME_CODE_HEX + " = ?",
                    new String[]{hex},
                    null, null, null);
            
            c.moveToFirst();
        } catch (IllegalStateException e){
            e.printStackTrace();
        }
        return c;
    }
    
    /**
     * Load new color set for learning to user table
     * @param userName name of man
     * @param colorSet set of color
     */
    public void loadNewColorSet(String userName, Cursor colorSet){

        SQLiteDatabase db = this.getWritableDatabase();

        boolean stopFlag = colorSet.moveToFirst();

        while(stopFlag != false){

            ColorString colorString = new ColorString(colorSet);

            db.insert(userName, null, makeNewColorContentValue(colorString));

            // next color
            stopFlag = colorSet.moveToNext();
        }
    }

    /**
     * Make content value from color string
     * @param colorString color name + hex
     * @return content value
     */
    public ContentValues makeNewColorContentValue(ColorString colorString){

        ContentValues newColor = new ContentValues();
        newColor.put(COLUMN_NAME_NAMES_COLORS[0], colorString.names[0]);
        newColor.put(COLUMN_NAME_NAMES_COLORS[1], colorString.names[1]);
        newColor.put(COLUMN_NAME_CODE_HEX, colorString.hex);
        newColor.put(COLUMN_NAME_STATUS_LEARN, "true");
        newColor.put(COLUMN_NAME_NUMBER_SHOWS, String.valueOf(0));
        newColor.put(COLUMN_NAME_NUMBER_ANSWERS, String.valueOf(0));
        newColor.put(COLUMN_NAME_DATE_SHOW, Timer.getTime().toString());

        return newColor;
    }

    /**
     * Load color string to to DB
     * @param userName name of user
     * @param colorString string of color
     */
    public void loadColorString(String userName,ColorString colorString){

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(userName, null, makeNewColorContentValue(colorString));
    }

    /**
     * Update color in DB by color card
     * @param userName name of man
     * @param color color card
     */
    public void updateColor(String userName, ColorCard color){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues updateFields = new ContentValues();
        updateFields.put(COLUMN_NAME_STATUS_LEARN, String.valueOf(color.status_learn));
        updateFields.put(COLUMN_NAME_NUMBER_SHOWS, String.valueOf(color.shows));
        updateFields.put(COLUMN_NAME_NUMBER_ANSWERS, String.valueOf(color.answers));
        updateFields.put(COLUMN_NAME_DATE_SHOW, String.valueOf(color.date));

        String selectString = COLUMN_NAME_CODE_HEX + " =?";
        db.update(
                userName,
                updateFields,
                selectString,
                new String[]{color.bad_hex}
        );
    }

    /**
     * Column is there?
     * @param userName name of lamer
     * @param columnName name of column
     * @return true or false
     */
    boolean checkNameRight(String userName, String columnName){
        
        SQLiteDatabase db = this.getWritableDatabase();
        
        try{

            Cursor c = db.rawQuery(
                    "SELECT * FROM " + userName 
                            + " WHERE " + columnName 
                            + " IS NULL OR " + columnName 
                            + "='';", null);
            
            c.moveToFirst();
            
            if (c.getCount() > 0) {
//                System.out.println("UD: need to be updated..");
                return false;
            }
            else {
//                System.out.println("UD: alll right..");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    // the same fun up here
    boolean checkColumnExist(String userName, String columnName){

        SQLiteDatabase db = this.getWritableDatabase();

        try{
            Cursor c = db.rawQuery("SELECT * FROM " + userName + " LIMIT 0;", null);
            if (c.getColumnIndex(columnName) != -1)
                return true;

        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    // !
    public void deleteUsersDatabase(){
        if(context != null)
        context.deleteDatabase(DATABASE_NAME);
        // isn't work correctly
        // TODO make delete fun
    }
}