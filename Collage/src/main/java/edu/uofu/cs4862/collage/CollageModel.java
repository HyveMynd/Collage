package edu.uofu.cs4862.collage;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Andres on 11/5/13.
 */
public class CollageModel {
    private ArrayList<ImageData> images;
    private ArrayList<ImageData> onScreenImages;
    private static CollageModel instance;

    private CollageModel (){
        this.images = new ArrayList<ImageData>();
        onScreenImages = new ArrayList<ImageData>();
    }

    public static CollageModel getCollageModelInstance(){
        if (instance == null){
            instance = new CollageModel();
        }
        return instance;
    }

    public ArrayList<ImageData> getOnScreenImages(){
        return onScreenImages;
    }

    public ArrayList<ImageData> getImages() {
        return images;
    }

    public void addOnScreenImage(ImageData data){
        synchronized (onScreenImages){
            onScreenImages.add(data);
        }
    }

    public void addImage(ImageData data){
        synchronized (images){
            images.add(data);
        }
    }

    public void removeOnScreenImage(int position){
        synchronized (onScreenImages){
            onScreenImages.remove(position);
        }
    }

    public int totalImageCount(){
        return images.size();
    }

    public int onScreenImageCount(){
        return onScreenImages.size();
    }

    public ImageData getOnScreenImage(int position){
        return onScreenImages.get(position);
    }

    public ImageData getImage(int position){
        return images.get(position);
    }

    public void setImages(Collection<ImageData> images){
        synchronized (images){
            this.images = new ArrayList<ImageData>(images);
        }
    }

    public void setOnScreenImages(Collection<ImageData> images){
        synchronized (onScreenImages){
            onScreenImages = new ArrayList<ImageData>(images);
        }
    }
}
