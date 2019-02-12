package com.joyfulmagic.colors.views;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.joyfulmagic.colors.utils.ColorConverter;

import java.util.ArrayList;

/**
 * Created by egorchan on 24.12.16.
 */
public class HSVChart {


    private Bitmap bitmap;
    private Canvas canvas;
    private int sizex;
    private int sizey;
    private int stepx;
    private int stepy;
    private int brickSizex;
    private int brickSizey;

    private int color;
    private float [] colorHsl;
    private int [] colorRGB;
    private float [] hsl;
    private int [] rgb;
    private float [] hues;
    private float [] sats;
    private float [] ligthnesses;

    float [] hsv = new float[3];


    public HSVChart(int sizex, int sizey){


        bitmap = Bitmap.createBitmap(sizex, sizey, Bitmap.Config.ARGB_4444);
        canvas = new Canvas(bitmap);

        this.sizex = sizex;
        this.sizey = sizey;
        stepx = sizex / 12;
        brickSizex = stepx;
        stepy = sizey / 4;
        brickSizey = stepx;

        hues = new float[12];
        sats = new float[12];
        ligthnesses = new float[12];

        hsl = new float[3];
        rgb = new int [3];
        colorHsl = new float[3];
        colorRGB = new int [3];


        float huestep = (float) (1.0 / (float)(hues.length - 1));
        float satstep = huestep;
        float lightstep = huestep;

        float hue = 0;
        float sat = 0;
        float light = 0;
        int i;
        for(i = 0; i < hues.length - 1; i++){
            hues[i] = hue;
            sats[i] = sat;
            ligthnesses[i] = light;

            hue += huestep;
            sat += satstep;
            light += lightstep;
        }
        hues[i] = 1.0f;
        sats[i] = 1.0f;
        ligthnesses[i] = 1.0f;



    }

    public void setColor(int color){
        this.color = color;
        colorHsl = ColorConverter.rgbToHsl(Color.red(color), Color.green(color), Color.blue(color));
        updateChart();
    }

    private void updateChart() {

        bitmap.eraseColor(Color.argb(0, 0, 0, 0));

        // hue
        int y = stepy - brickSizey / 2;
        hsl[1] = 1.0f;
        hsl[2] = 0.5f;
        drawGrade(hues, 0, hsl, y);
        y+= stepy;

        // sat
        Color.colorToHSV(color,hsv);
        hsl[0]=colorHsl[0];
        hsl[1] = 1.0f;
        drawGrade(sats, 1, hsl, y);
        y+= stepy;

        // val
        drawGrade(ligthnesses, 2, hsl, y);
    }

    public void drawGrade(float[] parameterValues, int parameterIdx, float[] hsl, int y){

        boolean marked = false;
        boolean next = false;

        int i = 0;
        for(int x = 0; x < sizex - stepx; x += stepx){
            Rect r = new Rect(x, y, x + brickSizex - 1 , y + brickSizey );
            Paint p = new Paint();

            hsl[parameterIdx] = parameterValues[i]; i++;
            rgb = ColorConverter.hslToRgb(hsl[0], hsl[1], hsl[2]);
            p.setColor(Color.rgb(rgb[0], rgb[1], rgb[2]));

            canvas.drawRect(r, p);

            if(!marked) {
                float edge0 = parameterValues[i - 1];
                float edge1 = 1.0f;
                if (i < parameterValues.length) {
                    edge1 = parameterValues[i];
                }

                boolean mark = false;
                if(!next) {
                    if (colorHsl[parameterIdx] >= edge0 && colorHsl[parameterIdx] <= edge1) {

                        float dif1 = colorHsl[parameterIdx] - edge0;
                        float dif2 = colorHsl[parameterIdx] - edge1;

                        if(Math.abs(dif1) < Math.abs(dif2)){
                            mark = true;
                        } else {
                            next = true;
                        }
                    }
                } else {
                    mark = true;
                }

                if (mark) {
                    marked = true;

                    Paint p1 = new Paint();
                    float correct = (float) (0.618 * (float) (brickSizey) / 2);
                    Rect r1 = new Rect(x, (int) (y - correct), x + brickSizex - 1, (int) (y + brickSizey + correct));
//                    p1.setStrokeWidth(1);
//                    p1.setStyle(Paint.Style.STROKE);
//                    canvas.drawArc(x, y, x + brickSizex - 1, y + brickSizey - 1, 0, 180,true,p1);
                    canvas.drawRect(r1, p);
                }
            }


        }
    }

    public Bitmap getBitmap(){
        return bitmap;
    }
}
