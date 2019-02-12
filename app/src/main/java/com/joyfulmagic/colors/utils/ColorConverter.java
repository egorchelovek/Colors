package com.joyfulmagic.colors.utils;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.joyfulmagic.colors.activities.SettingsActivity.Settings;
import com.joyfulmagic.colors.databases.ColorDatabase.ColorDatabase;
import com.joyfulmagic.colors.databases.ColorDatabase.ColorString;
import com.joyfulmagic.colors.databases.DataBasesHolder;

/**
 * Extended color converter.
 * Possible variants of convertion:
 * HSL <--> RGB
 * HSL <--> int
 * int <--> String (0-6 range)
 */
public class ColorConverter {

    /**
     * The same as down here but from hex
     * @param hex color hex code
     * @return String of color range
     */
    public static String getColor(String hex){

        String out = "Unknown";

        try{
            int color = Color.parseColor(hex);
            out = getColor(color);
        } catch (Exception e){
            e.printStackTrace();
        }

        return out;
    }

    /**
     * Get color range from int color
     * @param color color
     * @return String of color range (Red, Yellow... so on)
     */
    public static String getColor(int color){

        String out = "Unknown";

            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);

            if(r >= g && g >= b){
                // From Red
                out = Settings.getColor(1); // to Yellow, and so on...
            }
            else if( g > r && r >= b){
                out = Settings.getColor(2);
            }
            else if( g >= b && b > r){
                out = Settings.getColor(3);
            }
            else if( b > g && g > r){
                out = Settings.getColor(4);
            }
            else if( b > r && r >= g){
                out = Settings.getColor(5);
            }
            else if( r >= b && b > g){
                out = Settings.getColor(6);
            }
        // then make division by hue, right?!

        return out;
    }

    /**
     * Converts an HSL color value to RGB. Conversion formula
     * adapted from http://en.wikipedia.org/wiki/HSL_color_space.
     * Assumes h, s, and l are contained in the set [0, 1] and
     * returns r, g, and b in the set [0, 255].
     *
     * @param h       The hue
     * @param s       The saturation
     * @param l       The lightness
     * @return int array, the RGB representation
     */
    public static int[] hslToRgb(float h, float s, float l){
        float r, g, b;

        if (s == 0f) {
            r = g = b = l; // achromatic
        } else {
            float q = l < 0.5f ? l * (1 + s) : l + s - l * s;
            float p = 2 * l - q;
            r = hueToRgb(p, q, h + 1f/3f);
            g = hueToRgb(p, q, h);
            b = hueToRgb(p, q, h - 1f/3f);
        }
        int[] rgb = {(int) (r * 255), (int) (g * 255), (int) (b * 255)};
        return rgb;
    }

    /** Helper method that converts hue to rgb */
    public static float hueToRgb(float p, float q, float t) {
        if (t < 0f)
            t += 1f;
        if (t > 1f)
            t -= 1f;
        if (t < 1f/6f)
            return p + (q - p) * 6f * t;
        if (t < 1f/2f)
            return q;
        if (t < 2f/3f)
            return p + (q - p) * (2f/3f - t) * 6f;
        return p;
    }

    /**
     * Converts an RGB color value to HSL. Conversion formula
     * adapted from http://en.wikipedia.org/wiki/HSL_color_space.
     * Assumes pR, pG, and bpBare contained in the set [0, 255] and
     * returns h, s, and l in the set [0, 1].
     *
     * @param pR       The red color value
     * @param pG       The green color value
     * @param pB       The blue color value
     * @return float array, the HSL representation
     */
    public static float[] rgbToHsl(int pR, int pG, int pB) {
        float r = pR / 255f;
        float g = pG / 255f;
        float b = pB / 255f;

        float max = (r > g && r > b) ? r : (g > b) ? g : b;
        float min = (r < g && r < b) ? r : (g < b) ? g : b;

        float h, s, l;
        l = (max + min) / 2.0f;

        if (max == min) {
            h = s = 0.0f;
        } else {
            float d = max - min;
            s = (l > 0.5f) ? d / (2.0f - max - min) : d / (max + min);

            if (r > g && r > b)
                h = (g - b) / d + (g < b ? 6.0f : 0.0f);

            else if (g > b)
                h = (b - r) / d + 2.0f;

            else
                h = (r - g) / d + 4.0f;

            h /= 6.0f;
        }

        float[] hsl = new float[3];
        hsl[0]=h;
        hsl[1]=s;
        hsl[2]=l;
        return hsl;
    }

    /**
     * HSL parameters getter from int color
     * @param color color
     * @return Hue-Saturation-Light values array
     */
    public static float[] colorToHsl(int color){
        return rgbToHsl(Color.red(color), Color.green(color), Color.blue(color));
    }

    /**
     * Convert HSL values to int color
     * @param hsl values of Hue, Saturation & Lightness
     * @return int color
     */
    public static int hslToColor(float[] hsl){
        int [] rgb = hslToRgb(hsl[0], hsl[1], hsl[2]);
        return Color.rgb(rgb[0], rgb[1], rgb[2]);
    }

    /**
     * Simple averaging of colors of bitmap
     * @param bitmap image seed
     * @return average color
     */
    public static int averageColor(Bitmap bitmap){

        int redBucket = 0;
        int greenBucket = 0;
        int blueBucket = 0;
        int pixelCount = 0;

        for (int y = 0; y < bitmap.getHeight(); y++)
        {
            for (int x = 0; x < bitmap.getWidth(); x++)
            {
                int c = bitmap.getPixel(x, y);

                pixelCount++;
                redBucket += Color.red(c);
                greenBucket += Color.green(c);
                blueBucket += Color.blue(c);
                // does alpha matter?
            }
        }

        int averageColor = Color.rgb(redBucket / pixelCount,
                greenBucket / pixelCount,
                blueBucket / pixelCount);

        return  averageColor;
    }

    /**
     * This function is searches nearest color in Color Database.
     * @param color base color
     * @return nearest color string with name and hex
     */
    public static ColorString nearestColor(int color){

        ColorString nearest = null;

        // get cursor with colors
        Cursor c = DataBasesHolder.colorDatabase.getAllBasicColorsRGB();
        c.moveToFirst();

        // diving into color database records
        if(c.getCount() > 0) {

            int rc = Color.red(color);
            int gc = Color.green(color);
            int bc = Color.blue(color);

            int r = Integer.valueOf(c.getString(c.getColumnIndex(ColorDatabase.COLUMN_NAME_VALUE_R)));
            int g = Integer.valueOf(c.getString(c.getColumnIndex(ColorDatabase.COLUMN_NAME_VALUE_G)));
            int b = Integer.valueOf(c.getString(c.getColumnIndex(ColorDatabase.COLUMN_NAME_VALUE_B)));

            float[] hsl = ColorConverter.rgbToHsl(rc, gc, bc);
            float[] nearestHsl = ColorConverter.rgbToHsl(r, g, b);

            float[] nearestDistances = new float[3];
            for(int i = 0; i < nearestDistances.length; i++){
                nearestDistances[i] = Math.abs(nearestHsl[i] - hsl[i]);
            }

            nearest = new ColorString(c);

            while (c.moveToNext() != false) {

                r = Integer.valueOf(c.getString(c.getColumnIndex(ColorDatabase.COLUMN_NAME_VALUE_R)));
                g = Integer.valueOf(c.getString(c.getColumnIndex(ColorDatabase.COLUMN_NAME_VALUE_G)));
                b = Integer.valueOf(c.getString(c.getColumnIndex(ColorDatabase.COLUMN_NAME_VALUE_B)));

                float[] thisHSL = ColorConverter.rgbToHsl(r, g, b);
                float[] distances = new float[3];
                for(int i = 0; i < nearestDistances.length; i++){
                    distances[i] = Math.abs(thisHSL[i] - hsl[i]);
                }

                boolean change = false;
                if(nearestDistances[0] > distances[0]){
                    nearestDistances = distances;
                    change = true;

                } else if(nearestDistances[1] == distances[1]){
//                    if(nearestDistances[1] > distances[1]){
//                        nearestDistances = distances;
//                        change = true;
//
//                    } else if(nearestDistances[2] == distances[2]) {
//                        if(nearestDistances[2] > distances[2]) {
//                            nearestDistances = distances;
//                            change = true;
//                        }
//                    }

                    float averDistance = (nearestDistances[1] + nearestDistances[2]) / 2;
                    float averDistance1 = (distances[1] + distances[2]) / 2;
                    if(averDistance > averDistance1){
                        nearestDistances = distances;
                        change = true;
                    }
                }

//                d = Math.sqrt((r - rc) ^ 2 + (g - gc) ^ 2 + (b - bc) ^ 2);
                if (change) {
                    nearest = new ColorString(c);
                }
            }
        }

        return nearest;
    }

    public static float[] colorToHsl(int color, float[] hsl) {
        float r = Color.red(color) / 255f;
        float g = Color.green(color) / 255f;
        float b = Color.blue(color) / 255f;

        float max = (r > g && r > b) ? r : (g > b) ? g : b;
        float min = (r < g && r < b) ? r : (g < b) ? g : b;

        float h, s, l;
        l = (max + min) / 2.0f;

        if (max == min) {
            h = s = 0.0f;
        } else {
            float d = max - min;
            s = (l > 0.5f) ? d / (2.0f - max - min) : d / (max + min);

            if (r > g && r > b)
                h = (g - b) / d + (g < b ? 6.0f : 0.0f);

            else if (g > b)
                h = (b - r) / d + 2.0f;

            else
                h = (r - g) / d + 4.0f;

            h /= 6.0f;
        }

        hsl[0]=h;
        hsl[1]=s;
        hsl[2]=l;
        return hsl;
    }
}
