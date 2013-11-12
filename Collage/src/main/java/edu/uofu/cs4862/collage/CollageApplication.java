package edu.uofu.cs4862.collage;

import android.app.Application;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Andres on 11/5/13.
 */
public class CollageApplication extends Application {
    private  CollageModel model;
    private static final int[] IMAGES = { R.drawable.catarina };
    private static final String IMAGES_FILE = "collageImages";
    private static final String ON_SCREEN_FILE = "collageOnScreen";

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
        model.setOnScreenImages(images);
    }

    public CollageModel getModel(){
        return model;
    }

    public void persist(){
        ExecutorService exe = Executors.newSingleThreadExecutor();
        exe.execute(new SaveRestoreState(true));
        exe.shutdown();
        try {
            exe.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void restoreState(){
        ExecutorService exe = Executors.newSingleThreadExecutor();
        exe.execute(new SaveRestoreState(false));
        exe.shutdown();
        try {
            exe.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void save(){
        try{
            Gson gson = new Gson();
            String imagesString = gson.toJson(model.getImages());
            String onScreeString = gson.toJson(model.getOnScreenImages());
            FileOutputStream fos = this.openFileOutput(IMAGES_FILE, MODE_PRIVATE);
            IOUtils.write(imagesString, fos);
            fos.close();

            fos = openFileOutput(ON_SCREEN_FILE, MODE_PRIVATE);
            IOUtils.write(onScreeString, fos);
            fos.close();
        } catch (IOException e){
            Log.e("Error saving", e.getMessage());
        }
    }

    private void restore(){
        try{
            Gson gson = new Gson();
            FileInputStream fis = openFileInput(IMAGES_FILE);
            String contents = IOUtils.toString(fis);
            ArrayList<ImageData> images = gson.fromJson(contents, new TypeToken<ArrayList<ImageData>>(){}.getType());
            fis.close();
            model.setImages(images);

            fis = openFileInput(ON_SCREEN_FILE);
            contents = IOUtils.toString(fis);
            images = gson.fromJson(contents, new TypeToken<ArrayList<ImageData>>(){}.getType());
            fis.close();
            model.setOnScreenImages(images);
        }catch (IOException e){
            Log.e("Error restoring", e.getMessage());
        }
    }

    class SaveRestoreState implements Runnable{
        private boolean isSave;

        SaveRestoreState(boolean isSave) {
            this.isSave = isSave;
        }

        @Override
        public void run() {
            if (isSave){
                save();
            } else {
                restore();
            }
        }
    }


}
