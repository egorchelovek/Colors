package com.joyfulmagic.colors.activities.CameraActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joyfulmagic.colors.AI.GuruIncarnation;
import com.joyfulmagic.colors.R;
import com.joyfulmagic.colors.activities.BaseActivity;
import com.joyfulmagic.colors.activities.HarmonyActivty.HarmonyActivity;
import com.joyfulmagic.colors.activities.SettingsActivity.Settings;
import com.joyfulmagic.colors.databases.DataBasesHolder;
import com.joyfulmagic.colors.utils.AdvancedMetrics.AdvancedDisplayMetrics;
import com.joyfulmagic.colors.utils.ColorConverter;
import com.joyfulmagic.colors.views.GradientsHolder;

/**
 * This class for capture any color from camera.
 * Unfortunately here the whole program is initialized.
 */
public class CameraActivity extends BaseActivity implements Camera.PreviewCallback {

    public static int color; // captured color

    // camera object
    Camera camera;
    final int CAMERA_ID = 0;
    final boolean FULL_SCREEN = true;

    // preview objects
    SurfaceView sv;
    SurfaceHolder holder;
    HolderCallback holderCallback;

    // region of color capture objects
    private int interestSize;
    private int interestSize2;
    private int centerX;
    private int centerY;
    private ImageView centerFrame;

    // color preview objects
    ImageView colorField; // field with color image
    Bitmap bitmap; // image of the color
    TextView colorName; // name of nearest color of the color

    boolean fix = false; // fixation or freeze flag to catch the color

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //setTitles(getString(R.string.activity_camera));

        setContentView(R.layout.camera);

        // initalization of holder
        sv = (SurfaceView) findViewById(R.id.surfaceView);
        holder = sv.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holderCallback = new HolderCallback();
        holder.addCallback(holderCallback);

        // initialization of color objects
        colorField = (ImageView) findViewById(R.id.colorPreview);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = (int) getResources().getDimension(R.dimen.standart_margin);
        param.setMargins(margin, margin, margin, margin);
        colorField.setLayoutParams(param);
        int sizeField = AdvancedDisplayMetrics.link.getSubsizes(2)[0];
        bitmap = Bitmap.createBitmap(sizeField,sizeField, Bitmap.Config.ARGB_4444);
        colorName = (TextView) findViewById(R.id.colorPreviewText);
        
        // initialization of buttons
        Button buttonFix = (Button) findViewById(R.id.buttonPreviewFix);
        buttonFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fix == false){
                    ((Button) v).setText(getString(R.string.unfix));
                } else {
                    ((Button) v).setText(getString(R.string.fix));
                }
                fix = !fix;
            }
        });
        Button buttonOk = (Button) findViewById(R.id.buttonPreviewOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHarmonyActivity();
            }

        });
        
        // initialization of color pick area
        centerFrame = (ImageView) findViewById(R.id.centerFrame);
        int depth = 5;
        interestSize = AdvancedDisplayMetrics.link.getSubsizes(depth)[0];
        interestSize2 = interestSize / 2;
        int lineSize = AdvancedDisplayMetrics.link.getSubsizes(depth + 1)[0] / 2;
        Bitmap bitFrame = Bitmap.createBitmap(interestSize + 1, interestSize + 1, Bitmap.Config.ARGB_4444);
        bitFrame.eraseColor(Color.TRANSPARENT);
        Canvas c = new Canvas();
        c.setBitmap(bitFrame);
        Paint p = new Paint();
        p.setColor(Color.GREEN);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth((float) (0.1 * interestSize));
        c.drawRect(new Rect(0, 0, interestSize, interestSize), p);
        centerFrame.setImageBitmap(bitFrame);
    }

    private void startHarmonyActivity(){
        onPause();

        Intent intent = new Intent(this, HarmonyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // put picked color information
        intent.putExtra("source", 0);
        intent.putExtra("name", ColorConverter.nearestColor(color).names[Settings.language]);
        intent.putExtra("color", color);

        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        while(camera == null){
            camera = Camera.open();
        }

        if (camera != null) {
            camera.setPreviewCallback(this);
            setPreviewSize(FULL_SCREEN);
        }


        holder.addCallback(holderCallback);
        sv.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(camera !=null)
            camera.setPreviewCallback(null);

        if(holder != null && holderCallback != null)
            holder.removeCallback(holderCallback);

        if (camera != null)
            camera.release();

        camera = null;

        sv.setVisibility(View.INVISIBLE);
    }


    /**
     * Here we get color from preview,
     * show it on the screen,
     * and save it for next usage.
     * @param data raw image is needed to convert
     * @param camera our camera
     */
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

        if(camera != null) {
            // get camera params
            Camera.Parameters parameters = camera.getParameters();
            int width = parameters.getPreviewSize().width;
            int height = parameters.getPreviewSize().height;

            // convert raw image data to bitmap (very significant code!)
            YuvImage yuv = new YuvImage(data, parameters.getPreviewFormat(), width, height, null);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            yuv.compressToJpeg(new Rect(0, 0, width, height), 50, out);
            byte[] bytes = out.toByteArray();
            final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            // then show it and save it
            if (bm != null && !fix) {

                // capture color from little square in center of screen
                centerX = bm.getWidth() / 2;
                centerY = bm.getHeight() / 2;
                Bitmap cutBit = Bitmap.createBitmap(bm, centerX - interestSize2, centerY - interestSize2, interestSize, interestSize);

                // averaging it
                color = ColorConverter.averageColor(cutBit);

                // update color of bitmap by average color
                bitmap.eraseColor(color);

                // and showing it in the screen
                this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        colorField.setImageBitmap(bitmap);
                        colorName.setText(ColorConverter.nearestColor(color).names[Settings.language]);
                    }
                });
            }
        }
    }

    class HolderCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                if(camera!=null) {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            if(camera!=null) {
                camera.stopPreview();
                setCameraDisplayOrientation(CAMERA_ID);
                try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {}

    }

    void setPreviewSize(boolean fullScreen) {

        // get screen sizes
        Display display = getWindowManager().getDefaultDisplay();
        boolean widthIsMax = display.getWidth() > display.getHeight();

        // get preview size
        Size size = camera.getParameters().getPreviewSize();

        RectF rectDisplay = new RectF();
        RectF rectPreview = new RectF();

        // RectF screen is equal to size screen
        rectDisplay.set(0, 0, display.getWidth(), display.getHeight());

        // RectF preview
        if (widthIsMax) {
            // in horizontal
            rectPreview.set(0, 0, size.width, size.height);
        } else {
            // in vertical
            rectPreview.set(0, 0, size.height, size.width);
        }

        // matrix transforming of screen
        Matrix matrix = new Matrix();
        if (!fullScreen) {
            // preview size to screen size adjusting
            matrix.setRectToRect(rectPreview, rectDisplay,
                    Matrix.ScaleToFit.START);
        } else {
            // screen size to preview size adjusting
            matrix.setRectToRect(rectDisplay, rectPreview,
                    Matrix.ScaleToFit.START);
            matrix.invert(matrix);
        }
        // transforming
        matrix.mapRect(rectPreview);

        // set surface size from transforming
        sv.getLayoutParams().height = (int) (rectPreview.bottom);
        sv.getLayoutParams().width = (int) (rectPreview.right);
    }



    void setCameraDisplayOrientation(int cameraId) {

        // get screen angle from normal state angle
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result = 0;

        // get camera info from cameraId
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(cameraId, info);

        // back camera
        if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
            result = ((360 - degrees) + info.orientation);
        } else
            // front camera
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                result = ((360 - degrees) - info.orientation);
                result += 360;
            }
        result = result % 360;
        camera.setDisplayOrientation(result);
    }


}
