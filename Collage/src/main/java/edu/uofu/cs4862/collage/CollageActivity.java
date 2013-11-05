package edu.uofu.cs4862.collage;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Andres on 11/5/13.
 */
public class CollageActivity extends Activity {
    private CollageModel model;
    private CollageLibraryFragment libraryFragment;
    private CollageFragment collageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ((CollageApplication)this.getApplication()).getModelInstance();
        libraryFragment = new CollageLibraryFragment();
        collageFragment = new CollageFragment();
    }
}
