package com.joyfulmagic.colors.views.HarmonyPanel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.joyfulmagic.colors.activities.CameraActivity.Camera2Activity;
import com.joyfulmagic.colors.activities.CameraActivity.CameraActivity;
import com.joyfulmagic.colors.activities.ColorEncyclopedy.ColorListener;
import com.joyfulmagic.colors.databases.ColorDatabase.ColorString;
import com.joyfulmagic.colors.utils.ColorHarmonizer;

/**
 * Simple harmony view
 */
public class HarmonyView extends ImageView implements View.OnTouchListener {

    private Bitmap bitmap;

    int color;
    int[] harmony;
    int type;

    boolean bitmapSeted;
    boolean colorSeted;
    boolean harmonySeted;

    public int choosedColor;
    private HarmonyPanel harPan;

    public HarmonyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        dropFlags();
    }

    public HarmonyView(Context context) {
        super(context);

        dropFlags();
    }

    public HarmonyView(Context context, int sizeX, int sizeY) {
        super(context);
        setImageBitmap(Bitmap.createBitmap(sizeX, sizeY, Bitmap.Config.ARGB_4444));

        dropFlags();
    }

    private void dropFlags(){
        bitmapSeted = false;
        colorSeted = false;
        harmonySeted = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(bitmapSeted && colorSeted && harmonySeted){
            Canvas canva = new Canvas();
            canva.setBitmap(bitmap);
            bitmap.eraseColor(Color.argb(0, 0, 0, 0));

            int idx = 0;
            float step = (float)(bitmap.getWidth()) / (harmony.length);
            for(int x = 0; x <= bitmap.getWidth() - step; x+= step){
                Rect r = new Rect(x, 0, (int) (x + step), canva.getHeight());
                Paint p = new Paint();
                p.setColor(harmony[idx]); idx++;
                canvas.drawRect(r, p);
            }
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);

        bitmap = bm;

        bitmapSeted = true;
    }


    public void setColor(int color){
        this.color = color;

        colorSeted = true;

        choosedColor = color;

        setHarmony(type);

        this.invalidate();
    }

    public  void setHarmony(int type){

        this.type = type;
        harmony = ColorHarmonizer.getHarmony(color, type);
        harmonySeted = true;
    }

    public void initHarmonyView(Bitmap bm, int color, int harmonyType){
        setImageBitmap(bm);
        setColor(color);
        setHarmony(harmonyType);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int xTouch = (int) event.getX() - v.getPaddingLeft();
        int yTouch = (int) event.getY()- v.getPaddingTop();

        if(bitmap != null){

            float step = (float)(bitmap.getWidth()) / (harmony.length);
            int j = 0;
            for(int x = 0; x <= bitmap.getWidth() - step; x+= step){
                if(xTouch >= x && xTouch<= (x + step)){
                    choosedColor = harmony[j];
                }
                j++;
            }

            if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
                CameraActivity.color = choosedColor;
            } else{
                Camera2Activity.color = choosedColor;
            }

            if(harPan != null) harPan.chooseColor(choosedColor);
        }

        return false;
    }

    public void setHarmonyPanel(HarmonyPanel hp){ harPan = hp; }

    public int[] getHarmony() {
        return harmony;
    }
}
