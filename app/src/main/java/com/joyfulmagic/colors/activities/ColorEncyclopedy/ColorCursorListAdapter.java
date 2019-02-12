package com.joyfulmagic.colors.activities.ColorEncyclopedy;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.joyfulmagic.colors.activities.SettingsActivity.Settings;
import com.joyfulmagic.colors.databases.ColorDatabase.ColorString;
import com.joyfulmagic.colors.R;

/**
 * Adapter of color cursor from color database.
 */
public class ColorCursorListAdapter extends CursorAdapter {


    public ColorCursorListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.color_encyclopedy_color_list_element, viewGroup, false);
    }

    /**
     * Main method of this Adapter
     * @param view View of color list
     * @param context bla
     * @param cursor bla-bla
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // get color
        ColorString c = new ColorString(cursor);

        // make image with color
        int size = (int) context.getResources().getDimension(R.dimen.color_field_encylopedy_list);
        Bitmap colorField = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);
        colorField.eraseColor(c.getColor());

        // show color on screen
        ImageView imageView = (ImageView) view.findViewById(R.id.colorImage);
        imageView.setImageBitmap(colorField);

        // fill the text
        TextView colorNameField = (TextView) view.findViewById(R.id.colorName);
        colorNameField.setText(c.names[Settings.language]);
        colorNameField.setGravity(Gravity.CENTER_VERTICAL);

    }
}
