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

    public void saveInstance(){
        if (model != null){
            model.getImages();
        }
    }

    public CollageModel getModel(){
        return model;
    }
}
