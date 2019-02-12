package com.joyfulmagic.colors.views.HSLColorPicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.joyfulmagic.colors.databases.ColorDatabase.ColorString;
import com.joyfulmagic.colors.activities.ColorEncyclopedy.ColorListener;
import com.joyfulmagic.colors.R;
import com.joyfulmagic.colors.utils.ColorConverter;
import com.joyfulmagic.colors.utils.Constant;

/**
 * HSLColorPicker for drawer
 */
public class HSLColorPicker extends ImageView implements View.OnTouchListener, ColorListener{

    int sizeX;
    int sizeY;

    // properties of color circle
    private int centerX;
    private int centerY;
    private int radiusInner;
    private int radius;
    private int radiusMiddle;
    private int radiusSub;

    // properties of rectangle
    private int squareSize;
    private int actualSize;
    private int halfSize;
    private int left;
    private int right;
    private int top;
    private int bottom;

    private Bitmap gradients;
    private Bitmap markers;
    private int color;
    private float[] hsl;

    private int toneX;
    private int toneY;

    private int satX;
    private int satY;

    private boolean touchable;

    public HSLColorPicker(Context context) {
        super(context);

        init();
    }

    public HSLColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {

        touchable = true;

        hsl = new float[3];

        int size = (int) getContext().getResources().getDimension(R.dimen.color_field_encyclopedy_info_main);
        gradients = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_4444);
        markers = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_4444);

        // init gradient map
        Canvas c = new Canvas();
        c.setBitmap(gradients);
        gradients.eraseColor(Color.argb(0,0,0,0));

        calCircleDraw(c);
        drawPhiCircle(c);

        calcRectDraw();
        drawGradientSquareInTheMiddle(c);

        setImageBitmap(markers);
        setOnTouchListener(this);
        setColor(Color.RED);
        setNewTone(radiusMiddle, 0);
        setNewSatLight(right - 1, centerY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Canvas c = new Canvas();
        c.setBitmap(gradients);
        drawGradientSquareInTheMiddle(c);

        markers.eraseColor(Color.argb(0,0,0,0));
        c.setBitmap(markers);
        c.drawBitmap(gradients, 0, 0, null);
        drawToneMarker(c);
        drawSatHueMarker(c);
    }

    private void drawGradientSquareInTheMiddle(Canvas c) {

        // adjust color
        float [] hsl = new float[3];
        hsl[0] = this.hsl[0];

        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);

        // check this out
        Rect re = new Rect(left, top, right, bottom);
//        c.drawRect(r,p);

        int gradations = squareSize / 16;
        int xStep = squareSize / gradations;
        int yStep = squareSize / gradations;
        actualSize = gradations * xStep;
        float gradX = 0;
        float gradStepX = 1.0f / gradations;
        float gradY;
        float gradStepY = 1.0f / gradations;
        for(int i = left; i < right - xStep; i += xStep){

            gradY = 0;
            for(int j = top; j <  bottom - yStep; j += yStep){

                // hsl[0] must be seted by color
                hsl[1] = gradX;
                hsl[2] = gradY;
                p.setColor(ColorConverter.hslToColor(hsl));

                // update coords
                re.set(i, j, i + xStep, j + yStep);

                c.drawRect(re, p);
//                c.drawPoint(i, j, p);

                gradY += gradStepY;
            }
            gradX += gradStepX;
        }
    }

    public void drawFullCircle(Canvas c){
        drawColorCircle(c, centerX, centerY, radius, 0, radius);
    }

    public void drawPhiCircle(Canvas c){
        drawColorCircle(c, centerX, centerY, radius, radiusInner, radius);
    }

    public void calCircleDraw(Canvas c){

        radius = Math.min(c.getWidth(), c.getHeight()) / 2; // radius
        radiusInner = (int) (Constant.phi * radius);
        centerX = c.getWidth() / 2; // center X
        centerY = c.getHeight() / 2; // center Y

        radiusSub = (radius - radiusInner) / 2;
        radiusMiddle = radiusInner + radiusSub;


    }

    public void calcRectDraw(){
        squareSize = (int) (Math.sqrt(2) * radiusInner);
        halfSize = squareSize / 2;
        left = centerX - halfSize;
        right = centerX + halfSize;
        top = centerY - halfSize;
        bottom = centerY + halfSize;
    }

    public void drawColorCircle(Canvas c, int x, int y, int radius1, int radius2, int gradations){

        // init color
        float []hsl = new float[3];
        hsl[0] = 0.0f;
        hsl[1] = 1.0f;
        hsl[2] = 0.5f;
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);

        // init segment for draw between circles
        Path fourangle = new Path();

        // radiuses of circles
        double r = Math.max(radius1, radius2);
        double r2 = Math.min(radius1, radius2);

        // polar coordinate
        double phiMax = 2 * Math.PI;
        double phiStep = phiMax / gradations;
        double phi0 = 0;

        // scale between hue gradations and coordinate in polar
        double scale = 1.0 / phiMax;

        double cosinus = Math.cos(phi0);
        double sinus = Math.sin(phi0);

        // this segment coordinates
        int xpolar;
        int ypolar;
        int xpolar2;
        int ypolar2;

        // next segment coordinates
        int xpolarNext = (int) (r * cosinus);
        int ypolarNext = (int) (r * sinus);
        int xpolar2Next = (int) (r2 * cosinus);
        int ypolar2Next = (int) (r2 * sinus);

        for(double phi = phi0 + phiStep; phi < phiMax + phiStep; phi += phiStep){

            cosinus = Math.cos(phi);
            sinus = Math.sin(phi);

            xpolar = xpolarNext;
            ypolar = ypolarNext;
            xpolar2 = xpolar2Next;
            ypolar2 = ypolar2Next;

            // translate polar to cartesian
            xpolarNext = (int) (r * cosinus);
            ypolarNext = (int) (r * sinus);
            xpolar2Next = (int) (r2 * cosinus);
            ypolar2Next = (int) (r2 * sinus);

            // moving circle to center
            int x1 = x + xpolar;
            int y1 = y + ypolar;
            int x2 = x + xpolar2;
            int y2 = y + ypolar2;
            int x3 = x + xpolarNext;
            int y3 = y + ypolarNext;
            int x4 = x + xpolar2Next;
            int y4 = y + ypolar2Next;

            // make segment figure
            fourangle.reset();
            fourangle.moveTo(x1, y1);
            fourangle.lineTo(x3, y3);
            fourangle.lineTo(x4, y4);
            fourangle.lineTo(x2, y2);
            fourangle.lineTo(x1, y1);

            // get color
            hsl[0] = (float) (phi * scale);
            p.setColor(ColorConverter.hslToColor(hsl));

            // and draw
            c.drawPath(fourangle,p);
        }
    }

    public void drawToneMarker(Canvas c){
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.WHITE);
        p.setStrokeWidth(radiusSub / 8);
        c.drawCircle(toneX, toneY, radiusSub / 2, p);

        p.setStyle(Paint.Style.FILL);
        float[] hslClear = new float[3];
        hslClear[0] = hsl[0];
        hslClear[1] = 1.0f;
        hslClear[2] = 0.5f;
        p.setColor(ColorConverter.hslToColor(hslClear));
        c.drawCircle(toneX, toneY, radiusSub / 2 , p);

    }
    public void drawSatHueMarker(Canvas c){
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.WHITE);
        p.setStrokeWidth(radiusSub / 8);
        c.drawCircle(satX, satY, radiusSub / 2, p);

        p.setStyle(Paint.Style.FILL);
        p.setColor(getColor());
        c.drawCircle(satX, satY, radiusSub / 2, p);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(touchable) {

            int xTouch = (int) event.getX();
            int yTouch = (int) event.getY();

            int xc = xTouch - centerX;
            int yc = yTouch - centerY;
            int polaR = (int) Math.sqrt(xc * xc + yc * yc);

            if (polaR <= radius - radiusSub / 2) {
                // we are in cirlce

                if (polaR >= radiusInner + radiusSub / 2) {
                    // whe are at tone segment
                    // so change tone

                    setNewTone(xc, yc);

                } else {
                    // check rect

                    if (xTouch > left && xTouch < right) {
                        if (yTouch > top && yTouch < bottom) {

                            setNewSatLight(xTouch, yTouch);
                        }
                    }
                }

                invalidate();
            }
        }
        return true;
    }

    private void setNewTone(int x, int y) {

        double angle = - Math.atan2(x, y) + Math.PI / 2;

        toneX = (int) (radiusMiddle * Math.cos(angle)) + centerX;
        toneY = (int) (radiusMiddle * Math.sin(angle)) + centerY;

        hsl[0] = ColorConverter.colorToHsl(gradients.getPixel(toneX, toneY))[0];
    }

    private void setNewSatLight(int x, int y) {
        satX = x;
        satY = y;

        float actInv =  1 / (float)actualSize;
        hsl[1] = actInv * (float) satX;
        hsl[2] = actInv * (float) satY;
    }

    public void setColor(int color){
        this.color = color;

        hsl = ColorConverter.colorToHsl(color);

        int x, y;
        // get right angle
        float angle = (float) (hsl[0] * Math.PI * 2);
        if(angle >= Math.PI * 2) angle -= Math.PI * 2;
        float radius = radiusInner + radiusSub / 2 + 1;
        // then translate angle to x, y...
        x = (int) (radius * Math.cos(angle));
        y = (int) (radius * Math.sin(angle));
        setNewTone(x, y);

        // then get x and y of intency-saturation square
        // x is saturation

        satX = (int) ((actualSize) * hsl[1]) + left;
        satY = (int) ((actualSize) * hsl[2]) + top;

        invalidate();
    }

    public int getColor(){
        return ColorConverter.hslToColor(hsl);
    }

    @Override
    public void onChooseColor(ColorString colorString) { setColor(Color.parseColor(colorString.hex)); }

    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }
}