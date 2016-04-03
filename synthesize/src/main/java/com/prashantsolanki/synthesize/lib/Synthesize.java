package com.prashantsolanki.synthesize.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Synthesize extends RelativeLayout {

    View view;
    LayoutInflater inflater;
    Bitmap.CompressFormat compressionFormat = Bitmap.CompressFormat.PNG;
    @IntRange(from = 0,to = 100)
    int outputQuality = 100;
    File outputPath;
    String fileName;
    OnSaveListener onSaveListener =null;
    public Synthesize(Context context) {
        super(context);
        outputPath = Environment.getExternalStorageDirectory();
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Synthesize(Context context,@LayoutRes int layoutRes) {
        this(context);
        setLayout(layoutRes);
    }

    public void setLayout(@LayoutRes int layoutRes){
        view = inflater.inflate(layoutRes, this, true);
    }

    public void setLayout(View layout){
        view = layout;
    }

    public void setParams(int width, int height){
        measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
    }

    public View getLayout(){
        return view;
    }

    public Bitmap.CompressFormat getCompressionFormat() {
        return compressionFormat;
    }

    public void setCompressionFormat(Bitmap.CompressFormat compressionFormat) {
        this.compressionFormat = compressionFormat;
    }

    public int getOutputQuality() {
        return outputQuality;
    }

    public void setOutputQuality(@IntRange(from = 0,to = 100)int outputQuality) {
        this.outputQuality = outputQuality;
    }

    public File getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(File outputPath) {
        this.outputPath = outputPath;
    }

    public void addOnSaveListener(OnSaveListener listener){
        this.onSaveListener = listener;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    Bitmap bitmap = null;
    protected void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    @Nullable
    protected Bitmap getBitmap(){
        return bitmap;
    }

    public void generateBitmap(OnBitmapGenerationListener onBitmapGenerationListener){
        layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
        if(bitmap==null) {
            this.buildDrawingCache();
            bitmap = this.getDrawingCache().copy(Bitmap.Config.ARGB_8888,false);
        }
        if(bitmap!=null)
            onBitmapGenerationListener.onSuccess(bitmap);
        else
            onBitmapGenerationListener.onError(new RuntimeException("Unable to generate Bitmap"));
    }

    public void saveImage(){
        generateBitmap(generationListener);
    }

    public void saveImage(OnSaveListener listener){
        this.onSaveListener = listener;
        generateBitmap(generationListener);
    }

    private OnBitmapGenerationListener generationListener = new OnBitmapGenerationListener() {
        @Override
        public void onSuccess(final Bitmap bitmap) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    FileOutputStream out = null;
                    String postFix = ".png";
                    if(compressionFormat== Bitmap.CompressFormat.JPEG)
                        postFix = ".jpg";
                    else if(compressionFormat == Bitmap.CompressFormat.PNG)
                        postFix = ".png";
                    else if(Build.VERSION.SDK_INT >=19 && compressionFormat == Bitmap.CompressFormat.WEBP)
                        postFix = ".webp";

                    if(!outputPath.exists())
                        if(outputPath.mkdirs())
                            Log.e("Synthesize","Unable to make directories");

                    if(fileName==null)
                        fileName = String.valueOf(SystemClock.elapsedRealtime()).concat(postFix);
                    final File filePath = new File(outputPath,fileName);
                    try {

                        out = new FileOutputStream(filePath);
                        bitmap.compress(compressionFormat, 100, out); // bmp is your Bitmap instance
                    } catch (Exception e) {
                        e.printStackTrace();
                        if(onSaveListener !=null)
                            onSaveListener.onError(e);
                    } finally {
                        destroyDrawingCache();
                        if(onSaveListener !=null)
                            onSaveListener.onSuccess(bitmap,filePath);
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        }

        @Override
        public void onError(Exception e) {
            e.printStackTrace();
        }
    };

    public interface OnSaveListener{
        void onSuccess(Bitmap bitmap,File file);
        void onError(Exception e);
    }

    public interface OnBitmapGenerationListener{
        void onSuccess(Bitmap bitmap);
        void onError(Exception e);
    }
}