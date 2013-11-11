package edu.uofu.cs4862.collage;

import android.app.Application;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

/**
 * Created by Andres on 11/5/13.
 */
public class CollageApplication extends Application {
    private  CollageModel model;
    private static final int[] IMAGES = { R.drawable.m74hubble, R.drawable.catarina };

    public CollageApplication(){
        model = CollageModel.getCollageModelInstance();
    }

    public void loadTestData(Resources resources){
        ArrayList<ImageData> images = new ArrayList<ImageData>();
        for (int i = 0; i < IMAGES.length; i++) {
            int image = IMAGES[i];

            images.add(new ImageData(BitmapFactory.decodeResource(resources, image), "01/01/01"));
        }
        model.setImages(images);
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public void saveInstance(){
        if (model != null){
            model.getImages();
        }
    }

    public CollageModel getModel(){
        return model;
    }
}
