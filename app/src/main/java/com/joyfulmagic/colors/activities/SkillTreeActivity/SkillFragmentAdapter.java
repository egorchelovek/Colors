package com.joyfulmagic.colors.activities.SkillTreeActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.joyfulmagic.colors.R;
import com.joyfulmagic.colors.activities.BaseActivity;
import com.joyfulmagic.colors.activities.TrainingActivity.TrainingActivity;
import com.joyfulmagic.colors.utils.AdvancedMetrics.AdvancedDisplayMetrics;
import com.joyfulmagic.colors.utils.ColorConverter;
import com.joyfulmagic.colors.utils.ColorHarmonizer;
import com.joyfulmagic.colors.views.GradientsHolder;
import com.joyfulmagic.colors.activities.SettingsActivity.Settings;
import com.joyfulmagic.colors.utils.ColorGenerator;



/**
 * Class for viewing different types of skill fragments...
 */
public class SkillFragmentAdapter extends BaseAdapter implements View.OnClickListener {

    public static String TAG;

    private Context context; // app context
    private LayoutInflater inflater;
    private SkillTreeActivity skillTreeActivity; // main activity of adapter

    // link of set to inflate
    private String[] set;

    // image and text
    private ImageView imageView;
    private TextView textView;

    private int type; // type of skill (Color, Space, Harmony)
    private boolean reduced; // root or all tree of skills flag
    private boolean special;

    /**
     * Constructor of skill fragment adapter
     * @param context app context
     * @param skillTreeActivity main activity link
     * @param type type of follow fragment
     * @param reduced root or branch of skill tree
     */
    public SkillFragmentAdapter(Context context, SkillTreeActivity skillTreeActivity, int type, boolean reduced){

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.skillTreeActivity = skillTreeActivity;

        // set variables
        this.type = type;
        this.reduced = reduced;
        special = false;

        // link set array
        switch (type){
            default: set = Settings.trains;
                special = true;
                break;
            case 1: set = Settings.colors;
                break;
            case 2: set = Settings.parameters;
                break;
            case 3: set = Settings.harmonies;
                break;
        }

        // set TAG
        TAG = Settings.getTrain(type);

        // if root, then inflate only set
        if(reduced){
            set = new String[]{TAG};
        }
    }

    /**
     * Usual functions for adapter
     */
    @Override
    public int getCount() {
        return set.length;
    }
    @Override
    public Object getItem(int position) {
        if(position < set.length)
        return set[position];
        return set[1];
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Main method of adapter
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        // get view first
        int check = position % 2;
        if(reduced || set == Settings.trains) {
            // for set of skill icon
            view = inflater.inflate(R.layout.skill_tree_element2, parent, false);
            imageView = (ImageView) view.findViewById(R.id.imageButtonSkill2);
            textView = (TextView) view.findViewById(R.id.textViewSkill2);

        }
        else{
            // for non reduced set of skill
            if (check == 0) {
                view = inflater.inflate(R.layout.skill_tree_element0, parent, false);
                imageView = (ImageView) view.findViewById(R.id.imageButtonSkill0);
                textView = (TextView) view.findViewById(R.id.textViewSkill0);

            } else {
                view = inflater.inflate(R.layout.skill_tree_element1, parent, false);
                imageView = (ImageView) view.findViewById(R.id.imageButtonSkill1);
                textView = (TextView) view.findViewById(R.id.textViewSkill1);

            }
        }

        // get bitmap for any skill tree brunch and add to view
        int size = AdvancedDisplayMetrics.link.getSubsizes(0)[0];
        Bitmap bm = getBitmap(type, position, size);
        imageView.setImageBitmap(bm);

        // make image clickable and binding listener
        imageView.setOnClickListener(this);
        imageView.setTag(new Integer(position));

        // set text
        textView.setText((String)getItem(position));
        if(type == 1 && !reduced && position != 0){
            textView.setText((String)getItem(position) + " — \n" +  (String)getItem(position + 1) );
        }
        textView.setGravity(Gravity.CENTER);

        // set margin if it is needed
        if(reduced || set == Settings.trains){
            // calc margin
            int margin = AdvancedDisplayMetrics.link.getHalfsizes(0)[1];
            margin -= (size + textView.getTextSize()) / 2;
            margin -= BaseActivity.actionBarSize;

            // set margin
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.topMargin = margin;
            imageView.setLayoutParams(params);
        }

        return view;
    }


    /**
     * Handle choosing of train type
     * @param v selected view associated with some kind of train
     */
    @Override
    public void onClick(View v) {

        // get selected position
        int position = ((Integer)v.getTag()).intValue();

        if(!reduced || special){

            // set type of train and...
            switch (type){
                case 1:
                    Settings.train = 1;
                    Settings.subset = position;
                    break;
                case 2:
                    Settings.train = 2;
                    Settings.parameter = position;
                    break;
                case 3:
                    Settings.train = 3;
                    Settings.harmony = position;
                    break;
                default:
                    Settings.train = 0;
            }

            // and run train activity with seted parameters...
            Intent intent = new Intent(context, TrainingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);

        } else if(skillTreeActivity != null){
            // showing fragment with skill subtree
            skillTreeActivity.changeFragment(type);
        }
    }

    /**
     * Create bitmap with image by fragment type
     * @param type type of fragment or train
     * @param position position or subtype
     * @param size size of image
     * @return bitmap with image
     */
    private Bitmap getBitmap(int type, int position, int size){

        Bitmap bm = null;
        switch (type){
            case 1:  bm = getBitColorPuzzle(position, size);
                break;
            case 2:
                bm = getBitColorSpace(position, size);
                break;
            case 3:
                bm = getBitColorHarm(position, size);
                break;
            default:
                bm = getBitAllColor(position, size);

        }
        return bm;
    }

    /**
     * Load scaled image from resource on bitmap
     * @param bm bitmap with seted size
     * @param resourceId ID of image resource
     * @return bitmap with scaled image
     */
    private Bitmap getScaledBitmapFromResources(Bitmap bm, int resourceId){

        BitmapFactory.Options thumbOpts = new BitmapFactory.Options();
        thumbOpts.inSampleSize = 4;
        Bitmap b2 = BitmapFactory.decodeResource(context.getResources(), resourceId, thumbOpts);


        Matrix m = new Matrix();
        m.postScale( (float)(bm.getWidth()) / b2.getWidth(), (float)(bm.getHeight()) / b2.getHeight());

        Bitmap scaledBitmap = Bitmap.createBitmap(b2, 0, 0, b2.getWidth(), b2.getHeight(), m, false);
        b2.recycle();
        bm = scaledBitmap;

        return bm;
    }

    /**
     * Create Penrose tilling
     */
    private Bitmap getBitAllColor(int position, int size){

        Bitmap bm = Bitmap.createBitmap(size,size, Bitmap.Config.ARGB_4444);

        bm = getScaledBitmapFromResources(bm, R.drawable.penrose_sphere);

        return bm;
    }

    /**
     * Create color squares bitmap cover
     */
    private Bitmap getBitColorPuzzle(int position, int size){

        return GradientsHolder.getRandomColorImage(position, size, 49);
    }

    /**
     * Create HSL-space bitmap cover
     */
    private Bitmap getBitColorSpace(int position, int size){

        Bitmap bm = GradientsHolder.gradients.get(position);

        // for funny rotation
//        if(orientation) {
//            Matrix m = new Matrix();
//            m.postRotate(90, b.getWidth()/2, b.getHeight()/2);
//            Bitmap rotatedBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
//            b = rotatedBitmap;
//        }

        // get a HSL-space color cube image for first position
        if(position == 0) {
            bm = getScaledBitmapFromResources(bm, R.drawable.hsl_cube_green);
        }

        return bm;
    }

    /**
     * Create harmony bitmap cover
     */
    private Bitmap getBitColorHarm(int position, int size){

        Bitmap bm = Bitmap.createBitmap(size,size, Bitmap.Config.ARGB_4444);

        // make clean random color
        int color = ColorGenerator.randColor();
        float[] hsl = ColorConverter.colorToHsl(color);
        hsl[1] = 1.0f;
        hsl[2] = 0.5f;
        color = ColorConverter.hslToColor(hsl);

        // get right harmony color set
        int pos = position;
        if(position == 0) pos = 7;
        int[] harm = ColorHarmonizer.getHarmony(color, pos);
        int step = size / harm.length;

        // let's draw harmony on canvas
        Canvas c = new Canvas();
        c.setBitmap(bm);
        bm.eraseColor(Color.argb(0,0,0,0));
        int i = 0;
        for(int j = 0; j < harm.length; j++){

            Rect r = new Rect(i, 0, i + step, size);
            i+= step;

            Paint p = new Paint();
            p.setColor(harm[j]);
            c.drawRect(r,p);
        }

        return bm;
    }
}
