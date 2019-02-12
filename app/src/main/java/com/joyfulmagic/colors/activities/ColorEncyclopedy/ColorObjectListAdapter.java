package com.joyfulmagic.colors.activities.ColorEncyclopedy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.joyfulmagic.colors.R;
import com.joyfulmagic.colors.activities.SettingsActivity.Settings;

import java.util.ArrayList;

/**
 * Adapter of color objects list.
 */
public class ColorObjectListAdapter extends BaseAdapter{

    LayoutInflater layoutInflater;
    Context context;
    ArrayList<ColorObject> colors;

    public ColorObjectListAdapter(Context context, ArrayList<ColorObject> myColors){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        colors = myColors;
    }

    @Override
    public int getCount() {
        return colors.size();
    }

    @Override
    public Object getItem(int position) {
        return colors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        view = layoutInflater.inflate(R.layout.color_encyclopedy_color_list_element, parent, false);

        ColorObject color = (ColorObject)getItem(position);

        // make image with color
        int size = (int) context.getResources().getDimension(R.dimen.color_field_encylopedy_list);
        Bitmap colorField = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);
        colorField.eraseColor(Color.parseColor(color.hex));

        // show color on screen
        ImageView imageView = (ImageView) view.findViewById(R.id.colorImage);
        imageView.setImageBitmap(colorField);

        // fill the text
        TextView colorNameField = (TextView) view.findViewById(R.id.colorName);
        colorNameField.setText(color.names[Settings.language]);

        view.setTag(position);

        return view;
    }

}
