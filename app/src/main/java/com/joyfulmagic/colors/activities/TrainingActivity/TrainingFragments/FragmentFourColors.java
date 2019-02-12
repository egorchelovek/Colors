package com.joyfulmagic.colors.activities.TrainingActivity.TrainingFragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.joyfulmagic.colors.AI.ColorTask;
import com.joyfulmagic.colors.R;
import com.joyfulmagic.colors.activities.SettingsActivity.Settings;
import com.joyfulmagic.colors.activities.TrainingActivity.TrainingFragment;
import com.joyfulmagic.colors.databases.UserDatabase.ColorCard;
import com.joyfulmagic.colors.utils.Messenger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * Four colors fragment to choose right color from it.
 */
public class FragmentFourColors extends TrainingFragment implements View.OnClickListener {

    TextView txt; // text of task and color

    // image buttons
    ImageButton btn1;
    ImageButton btn2;
    ImageButton btn3;
    ImageButton btn4;
    ArrayList<ImageButton> btns; // list of buttons

    // theirs images
    Bitmap bmp1;
    Bitmap bmp2;
    Bitmap bmp3;
    Bitmap bmp4;
    ArrayList<Bitmap> bmps; // list of images

    // initialisation of images
    public void init(){

        // set size of color field
        int size = (int)getActivity().getResources().getDimension(R.dimen.color_field_encylopedy_info_other);

        bmp1 = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);
        bmp2 = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);
        bmp3 = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);
        bmp4 = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);

        bmp1.eraseColor(Color.RED);
        bmp2.eraseColor(Color.YELLOW);
        bmp3.eraseColor(Color.GREEN);
        bmp4.eraseColor(Color.BLUE);

        bmps = new ArrayList<Bitmap>();
        bmps.add(bmp1);
        bmps.add(bmp2);
        bmps.add(bmp3);
        bmps.add(bmp4);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.training_fragment_four_colors, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // init text field
        txt = (TextView) getActivity().findViewById(R.id.fragment_four_colors_text_color_name);
        txt.setGravity(Gravity.CENTER_HORIZONTAL);

        // init buttons
        btn1 = (ImageButton) getActivity().findViewById(R.id.color_button_1);
        btn2 = (ImageButton) getActivity().findViewById(R.id.color_button_2);
        btn3 = (ImageButton) getActivity().findViewById(R.id.color_button_3);
        btn4 = (ImageButton) getActivity().findViewById(R.id.color_button_4);

        // their images
        init();

        // and so on
        btns = new ArrayList<ImageButton>();
        btns.add(btn1);
        btns.add(btn2);
        btns.add(btn3);
        btns.add(btn4);

        // then make little adjust
        int halfMargin = (int) (getResources().getDimension(R.dimen.standart_margin) / 2);
        Iterator<Bitmap> bmp = bmps.iterator();
        for (ImageButton btn:
             btns) {
            btn.setImageBitmap(bmp.next());
            btn.setOnClickListener(this);
            btn.setBackgroundColor(Color.argb(0,0,0,0));
        }

        // and set task
        setTask(getTask());
    }

    /**
     * Set task to fragment
     * @param task color task from Guru
     * @return true or false of succeed
     */
    @Override
    public boolean setTask(ColorTask task) {

        boolean taskSetedBit = false;

        if(super.setTask(task)) {

            boolean errorFlag = false;
            ImageButton button = null;

            if (bmp1 != null) {

                // set 4 colors images
                Iterator<Bitmap> bmp = bmps.iterator();
                Iterator<ColorCard> crd = task.colorSkill.iterator();
                Iterator<ImageButton> btn = btns.iterator();

                // for more difficulty
                Collections.shuffle(btns);

                while (crd.hasNext() && btn.hasNext() && bmp.hasNext()) {

                    // take all we need
                    ColorCard card = (ColorCard) crd.next();
                    button = (ImageButton) btn.next();
                    Bitmap bitmap = (Bitmap) bmp.next();

                    // redraw colors
                    int color;
                    try {
                        color = Color.parseColor(card.hex);
                    } catch (Exception e) {
                        color = Color.RED;
                        errorFlag = true;
                    }
                    bitmap.eraseColor(color);

                    // update tags
                    button.setTag(card);
                    button.setImageBitmap(bitmap);
                }

                // how to shuffle views?



                // set name of color message
                if (txt != null) {
                    try{
                        txt.setText(getString(R.string.fragment_four_colors) + " \"" + task.colorSkill.get(0).names[Settings.language]+ "\"");
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                } else {
                    errorFlag = true;
                }

                // check for error
                if(!errorFlag) taskSetedBit = true;
            }
        }

        return taskSetedBit;
    }

    /**
     * Check task on click
     * @param v selected color image
     */
    @Override
    public void onClick(View v) {

        // get color card of task
        ColorCard answer = (ColorCard) v.getTag();

        // remove all selections
        for (ImageButton btn:
                btns) {
            if(btn.isSelected()){
                btn.setSelected(false);

                Bitmap bmp = ((BitmapDrawable)btn.getDrawable()).getBitmap();
                int color = bmp.getPixel(bmp.getWidth() / 2,bmp.getHeight() / 2);
                bmp.eraseColor(color);
            }
        }

        // select color image
        v.setSelected(true);
        // and draw frame around it
        Bitmap bmp = ((BitmapDrawable)((ImageButton)v).getDrawable()).getBitmap();
        bmp.eraseColor(answer.toColor());
        Canvas c = new Canvas(bmp);
        Rect r = new Rect(0,0,bmp.getWidth(),bmp.getHeight());
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth((float) (bmp.getWidth() * 0.05));
        c.drawRect(r,p);

        // check answer and set result
        setResult(answer.type);
    }
}
