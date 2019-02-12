package com.joyfulmagic.colors.views.DrawersColorPicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.joyfulmagic.colors.views.OnParameterChange;
import com.joyfulmagic.colors.utils.ColorConverter;

/**
 * Simple color scroll bar
 * to view or set color parameter
 * in HSL-model (hue, saturation, lightness).
 * It's like simple scroll bar...
 */
public class ColorScrollBar extends ImageView implements View.OnTouchListener, OnParameterChange {

    public String TAG;

    ColorScrollBarManager colorScrollBarManager;

    private boolean bitmapSeted;
    private boolean parameterSeted;
    private boolean subsizesCalced;

    // bitmap values
    private Bitmap bitmap;
    private int sizex, sizey;
    // and subsizes
    private int stepx, stepy;
    private int brickSizex;
    private int brickSizey;
    private int upEdge;
    private int downEdge;

    // parameter values
    private int gradationsNumber;
    private float [] parameterGradations;
    private float parameterMin;
    private float parameterMax;
    private int parameterIdx;
    private int intervalIdx;
    private boolean intervalSeted;

    // color values
    private int color;
    private float [] colorHsl;
    private int [] colorRgb;
    private float [] hsl;
    private int [] rgb;

    // touch values
    private float xTouch, yTouch;

    // atributes
    private boolean mayScroll;

    public ColorScrollBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ColorScrollBar(Context context) {
        super(context);

        init();
    }

    private void init(){
        this.setOnTouchListener(this);
        dropFlags();
        allocateColorArrays();
        setScrollable(true);
    }

    public void dropFlags(){
        bitmapSeted = false;
        parameterSeted = false;
        subsizesCalced = false;
        intervalSeted = false;
    }
    
    public void allocateColorArrays(){
        hsl = new float[3];
        rgb = new int [3];
        colorHsl = new float[3];
        colorRgb = new int [3];
    }

    public void initColorBar(Bitmap bm, String parameterName){
        setImageBitmap(bm);
        setParameter(parameterName);
        calcSubsizes();
        setColor(Color.WHITE);
    }

    public boolean checkAllReady(){
        if(bitmapSeted && parameterSeted && subsizesCalced) return true;
        return false;
    }

    public  void setManager(ColorScrollBarManager manager){
        this.colorScrollBarManager = manager;
    }



    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);

        bitmap = bm;

        sizex = bm.getWidth();
        sizey = bm.getHeight();

        bitmapSeted = true;
    }


    public void setColor(int color){
        this.color = color;

        colorHsl = ColorConverter.rgbToHsl(Color.red(color), Color.green(color), Color.blue(color));

        this.invalidate();
    }

    private void setParameter(String parameterName){

        int defaultGradationsNumber = 12;
        int correction = -1;

        switch (parameterName){
            default: // Hue
                parameterIdx = 0;
                gradationsNumber = defaultGradationsNumber;
                correction = 0; // for hue 1.0 value tone equal 0.0
                TAG = "Hue";
                break;

            case "Saturation":
                parameterIdx = 1;
                gradationsNumber = defaultGradationsNumber;
                TAG = "Saturation";
                break;

            case "Lightness":
                parameterIdx = 2;
                gradationsNumber = defaultGradationsNumber;
                TAG = "Lightness";
                break;
        }

        parameterMin = 0.0f;
        parameterMax = 1.0f;
        generateParameterValues(gradationsNumber, correction);

        parameterSeted = true;
    }

    private void calcSubsizes(){
        if(bitmapSeted && parameterSeted){
            stepx = sizex / parameterGradations.length;
            stepy = sizey;
            brickSizex = stepx;
            brickSizey = stepx;
            upEdge = sizey / 2 - brickSizey / 2;
            downEdge = sizey / 2 + brickSizey / 2;

            subsizesCalced = true;
        }
    }

    public void generateParameterValues(int gradationsNumber, int correct){
        parameterGradations = new float[gradationsNumber];
        
        float parameterStep = (parameterMax - parameterMin) / (parameterGradations.length + correct);

        parameterGradations[0] = parameterMin;

        for(int i = 1; i < parameterGradations.length; i++){
            parameterGradations[i] = parameterGradations[i - 1] + parameterStep;
        }
    }

    private void drawGrade(Canvas canvas, float[] parameterValues, int parameterIdx){

        switch (parameterIdx){
            default:
                hsl[1] = 1.0f;
                hsl[2] = 0.5f;
                break;
            case 1:
                hsl[0] = colorHsl[0];
                hsl[2] = 0.5f;
                break;
            case 2:
                hsl[0] = colorHsl[0];
                hsl[1] = colorHsl[1];

        }

        boolean colorSeted = false;
        boolean next = false;
        int y = upEdge;

        int i = 0;
        for(int x = 0; x < sizex - stepx; x += stepx){
            Rect r = new Rect(x, y, x + brickSizex - 1 , y + brickSizey );
            Paint p = new Paint();

            hsl[parameterIdx] = parameterValues[i]; i++;
            rgb = ColorConverter.hslToRgb(hsl[0], hsl[1], hsl[2]);
            p.setColor(Color.rgb(rgb[0], rgb[1], rgb[2]));

            canvas.drawRect(r, p);

            if(!colorSeted) {

                float edge0 = parameterValues[i - 1];
                float edge1 = 1.0f;
                if (i < parameterValues.length) {
                    edge1 = parameterValues[i];
                }

                boolean mark = false;
                if(!next) {
                    if (colorHsl[parameterIdx] >= edge0 && colorHsl[parameterIdx] < edge1) {

                        float dif1 = colorHsl[parameterIdx] - edge0;
                        float dif2 = colorHsl[parameterIdx] - edge1;

                        if(Math.abs(dif1) <= Math.abs(dif2)){
                            mark = true;
                        } else {
                            next = true;
                        }

                        mark = true;
                    } else if(i == parameterValues.length){
                        mark = true;
                    }
                }
                else {
                    mark = true;
                }

                if (mark) {
                    colorSeted = true;

                    intervalSeted = true;
                    intervalIdx = i - 1;

                    Paint p1 = new Paint();
                    float correct = (float) (0.618 * (float) (brickSizey) / 2);
                    Rect r1 = new Rect(x, (int) (y - correct), x + brickSizex - 1, (int) (y + brickSizey + correct));

                    canvas.drawRect(r1, p);
                }
            }


        }
    }

    public float checkColorInterval(int c){

        float error = 1.0f;
        boolean mark = false;
        int idx = 0;
        float [] hsl = ColorConverter.rgbToHsl(Color.red(c), Color.green(c), Color.blue(c));

        for(int i = 0; i < parameterGradations.length - 1; i++){

            if(hsl[parameterIdx] >= parameterGradations[i]
                    && hsl[parameterIdx] < parameterGradations[i + 1]){

                // let's check difs
                float dif1 = Math.abs(parameterGradations[i] - hsl[parameterIdx]);
                float dif2 = Math.abs(parameterGradations[i + 1] - hsl[parameterIdx]);

                idx = i;
                if(dif1 > dif2){
                    idx++;
                }

                mark = true;

            } else if(i == parameterGradations.length - 2){

                idx = i + 1;

                mark = true;
            }

            if(mark){
                if(intervalSeted){
                    float distance = Math.abs(intervalIdx - idx);
                    if(distance == parameterGradations.length - 1)
                        distance = 1;
                    error = distance / (float)(parameterGradations.length / 2);
                }
                break;
            }

        }
        return error;
    }

    public float getMinError(){
        return (1.0f / (float)(parameterGradations.length / 2));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(checkAllReady()){
            bitmap.eraseColor(Color.argb(0,0,0,0));
            Canvas canva = new Canvas();
            canva.setBitmap(bitmap);

            drawGrade(canva, parameterGradations, parameterIdx);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        int topParam =  this.getPaddingTop();
        int rightParam =  this.getPaddingRight();
        int maxTopParam = topParam+sizey;
        int maxRightParam = rightParam + brickSizex * parameterGradations.length;

        xTouch = event.getX();
        yTouch = event.getY();

        if(xTouch > rightParam && xTouch < maxRightParam){
            if(yTouch > topParam && yTouch < maxTopParam){


                if(mayScroll){
                    // next answer what is the value of the parameter square
                    // x coordinate to parameter value

                    int gradIdx = (int) Math.floor((float)(xTouch) / (float)(stepx));
                    if(gradIdx > parameterGradations.length - 1) gradIdx = parameterGradations.length - 1;
                    changeParameter(this.parameterIdx, parameterGradations[gradIdx]);
                }

            }
        }

        return true;
    }

    public float getParameter(){
        return colorHsl[parameterIdx];
    }

    public int getIdx(){
        return parameterIdx;
    }

    @Override
    public void changeParameter(int parameterIdx, float parameterValue) {
        if(parameterIdx == this.parameterIdx) {
            if (colorScrollBarManager != null) {
                colorScrollBarManager.changeParameter(parameterIdx, parameterValue);
            }
        }

        colorHsl[parameterIdx] = parameterValue;
        this.invalidate();
    }

    public void setScrollable(boolean scrollable) {
        mayScroll = scrollable;
    }
}
