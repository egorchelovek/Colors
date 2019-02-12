package com.joyfulmagic.colors.views.Palete;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.joyfulmagic.colors.R;
import com.joyfulmagic.colors.utils.AdvancedMetrics.AdvancedDisplayMetrics;
import com.joyfulmagic.colors.views.Checkable.CheckedInt;

import java.util.List;

/**
 * Palette of colors array adapter
 */
public class PaleteArrayAdapter extends ArrayAdapter<CheckedInt> {

    CheckedInt[] colors;
    int elementSize;

    public PaleteArrayAdapter(Context context, int resource, int textViewResourceId, List<CheckedInt> colors) {
        super(context, resource, textViewResourceId, colors);
    }

    public PaleteArrayAdapter(Context context, int resource, CheckedInt[] colors, int elementSize) {
        super(context, resource, colors);

        this.colors = colors;
        this.elementSize = elementSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.palete_element, null);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.paleteColor);

        int size = AdvancedDisplayMetrics.link.getSubsizes(3)[0];
        if(elementSize > 0) size = elementSize;

        Bitmap bitmap = Bitmap.createBitmap(size,size, Bitmap.Config.ARGB_4444);
        imageView.setImageBitmap(bitmap);

        bitmap.eraseColor(getItem(position).getNumber());
        convertView.setBackgroundColor(Color.argb(0,0,0,0));

        if (colors[position].getState() == true) {

            Canvas c = new Canvas(bitmap);
            Rect r = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
            Paint p = new Paint();
            p.setColor(Color.WHITE);
            p.setStyle(Paint.Style.STROKE);

            p.setStrokeWidth((float) (bitmap.getWidth() * 0.05));
            c.drawRect(r,p);
        }

        return convertView;
    }

    int getElementSize(){
        return elementSize;
    }
}

