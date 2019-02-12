package com.joyfulmagic.colors.databases.ColorDatabase;

import android.database.Cursor;
import android.graphics.Color;

import com.joyfulmagic.colors.activities.SettingsActivity.Settings;

/**
 * ColorString for use Databases records
 */
public class ColorString {

    public String[] names; // names of color on different languages
    public String hex; // #000000 code of color
    public String bad_hex; // #abracadabra or such things

    public ColorString(Cursor c){
        names = new String[2];
        setColor(c);
    }

    public ColorString(String name, String hex){
        names = new String[2];
        setColor(name, hex);
    }

    /**
     * Set color string from cursor
     * @param c cursor from ColorDatabase
     */
    public void setColor(Cursor c){
        if(c != null) {
            if(c.getCount() > 0) {

                // then change this string for used language
                names[0] = c.getString(c.getColumnIndex(ColorDatabase.COLUMN_NAME_NAMES_COLORS[0]));
                names[1] = c.getString(c.getColumnIndex(ColorDatabase.COLUMN_NAME_NAMES_COLORS[1]));
                bad_hex = c.getString(c.getColumnIndex(ColorDatabase.COLUMN_NAME_CODE_HEX));

                // correction of hex code from DB
                hex = new String(bad_hex);
                if(hex.length() > 7)
                hex = hex.substring(0, 7);
            }
        }
        else{
            setColorByDefault();
        }
    }

    public void setColor(String name, String hex){
        this.names[Settings.language] = name;
        this.hex = hex;
    }

    public void setColorByDefault(){
        names[0] = "Red";
        names[1] = "Красный";
        hex = "#ff0000";
    }

    public int getColor(){
        if(hex != null) if(hex!="")
        return Color.parseColor(hex);
        return Color.RED;
    }
}
