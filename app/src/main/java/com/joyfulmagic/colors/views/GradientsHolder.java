package com.joyfulmagic.colors.views;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.joyfulmagic.colors.utils.ColorConverter;
import com.joyfulmagic.colors.utils.ColorGenerator;
import com.joyfulmagic.colors.activities.SettingsActivity.Settings;

import java.util.ArrayList;

/**
 * Class for generate and use some basic gradients
 * for explain color parameters (such as hue, and so on...)
 */
public class GradientsHolder {

    public static ArrayList<Bitmap> gradients;

    public GradientsHolder(int size, int gradations){
        generateGradients(size, gradations);
    }

    private ArrayList<Bitmap> generateGradients(int size, int gradations){
        gradients = new ArrayList<Bitmap>();
        for(int i = 0; i < Settings.parameters.length; i++){
            Bitmap gradient = getGradient(i, size, gradations);
            gradients.add(gradient);
        }
        return gradients;
    }

    private Bitmap getGradient(int type, int imageSize, int numberOfGradations){

        int position = type;

        // make image with color
        int size = imageSize;
        Bitmap colorField = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_4444);
        colorField.eraseColor(Color.argb(0,0,0,0));


        // define sizes of map
        int base = numberOfGradations;
        int size1 = base + 2;
        int size2 = size1;
        float parGrad = 1.0f / (size1 - 1);
        float parGrad2 = parGrad;
        if(position == 1 || position == 4 || position == 5){

            size1 = base;
            size2 = base;
            parGrad = 1.0f / size1;
            parGrad2 = parGrad;

            if(position == 4) {
                size2 = base + 1;
                parGrad2 = 1.0f / (size2 - 1);
            } else if (position == 5){
                size2 = base + 2;
                parGrad2 = 1.0f / (size2 - 1);
            }
        }
        else if(position == 2){
            size1 = base + 1;
            size2 = size1;

            parGrad = 1.0f / (size1 - 1);
            parGrad2 = parGrad;
        }

        // allocate map
        int [][] pickColors = new int[size1][size2];



        // color seed
        int color;
        switch (position){
            case 2: color = Color.BLUE;
                break;
            case 3: color = Color.GREEN;
                break;
            case 6: color = Color.MAGENTA;
                break;
            default: color = Color.RED;
        }
        float [] hsl = ColorConverter.colorToHsl(color);
        hsl[1] = 1.0f;
        hsl[2] = 0.5f;

        // generate color map (or color gradient array, if you want)
        for(int i = 0; i < pickColors.length; i++){

            int parIdx1 = 0;
            switch (position) {
                case 2:
                case 6:
                    parIdx1 = 1;
                    break;
                case 3:
                    parIdx1 = 2;
                    break;

            }
            hsl[parIdx1] = i * parGrad;

            for(int j = 0; j < pickColors[0].length; j++) {

                int parIdx2 = 2;
                switch (position) {
                    case 4:
                        parIdx2 = 1;
                    case 5:
                    case 6:
                        hsl[parIdx2] = 1 - j * parGrad2;
                }
                pickColors[i][j] = ColorConverter.hslToColor(hsl);
            }

        }



        // draw on bitmap
        Canvas canvas = new Canvas(colorField);
        int sizey = canvas.getHeight();
        int sizex = canvas.getWidth();

        float stepx = (float) ((float) (sizex) / base);
        float stepy = (float) ((float) (sizex) / base);

        int colIdx = 0, colIdx1 = 0;
        int strt1 = 0,strt2 = 0;

        switch (position) {
            case 2:
            case 3:
                strt1 = 1;
                break;
            case 4:
                strt2 = 0;
                break;
            case 5:
                strt2 = 1;
                break;
            case 6:
                strt1 = 1;
                strt2 = 1;
                break;
        }

        colIdx = strt1;
        for(int x = 0; x < sizex - stepx; x +=stepx){

            colIdx1 = strt2;
            for(int y = 0; y < sizey - stepy; y +=stepy){

                Paint p = new Paint();
                p.setColor(pickColors[colIdx][colIdx1]);
                canvas.drawRect(x, y, x + stepx, y + stepy, p);

                colIdx1 += 1;
            }
            colIdx += 1;
        }

        return colorField;
    }

    public static Bitmap getRandomColorImage(int type, int imageSize, int numberOfPixels){

        // make image with color
        int size = imageSize;
        int position = type;
        Bitmap colorField = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_4444);
        colorField.eraseColor(Color.argb(0,0,0,0));



        // gen colors
        String colorString = Settings.colors[position];
        int [] pickColors = new int[numberOfPixels];
        for(int i = 0; i < pickColors.length; i++){
            if(colorString.equalsIgnoreCase(Settings.colors[0])){
                pickColors[i] = ColorGenerator.randColor();
            }
            else pickColors[i] = ColorGenerator.randColorInRange(colorString);
        }

        // draw
        Canvas canvas = new Canvas(colorField);
        int sizey = canvas.getHeight();
        int sizex = canvas.getWidth();
        float stepx = (float) ((float) (sizex) / Math.sqrt(pickColors.length));
        float stepy = (float) ((float) (sizey) / Math.sqrt(pickColors.length));
        int colIdx = 0;
        for(int x = 0; x < sizex - stepx; x +=stepx){
            for(int y = 0; y < sizey - stepy; y +=stepy){

                System.out.println("CSA2: " + x + " " + y);
                Paint p = new Paint();
                p.setColor(pickColors[colIdx]); colIdx++;

                canvas.drawRect(x, y, x + stepx, y + stepy, p);
            }
        }

        return colorField;
    }
}
