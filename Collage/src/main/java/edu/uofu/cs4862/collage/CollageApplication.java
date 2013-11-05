package edu.uofu.cs4862.collage;

import android.app.Application;

/**
 * Created by Andres on 11/5/13.
 */
public class CollageApplication extends Application {
    private CollageModel model = null;

    public CollageModel getModelInstance(){
        if (model == null){
            model = new CollageModel();
        }
        return model;
    }

    public void saveInstance(){
        if (model != null){
            model.persist();
        }
    }
}
