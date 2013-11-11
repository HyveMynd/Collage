package edu.uofu.cs4862.collage;

import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Andres on 11/5/13.
 */
public class CollageModel {
    private ArrayList<ImageData> images;
    private static CollageModel instance;

    private CollageModel (){
        this.images = new ArrayList<ImageData>();
    }

    public static CollageModel getCollageModelInstance(){
        if (instance == null){
            instance = new CollageModel();
        }
        return instance;
    }

    public ArrayList<ImageData> getImages(){
        return images;
    }

    public void add(ImageData data){
        synchronized (images){
            images.add(data);
        }
    }

    public int count(){
        return images.size();
    }

    public ImageData get(int position){
        return images.get(position);
    }

    public void setImages(Collection<ImageData> images){
        synchronized (images){
            this.images = new ArrayList<ImageData>(images);
        }
    }
}
