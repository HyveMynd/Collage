package edu.uofu.cs4862.collage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by Andres on 11/5/13.
 */
public class CollageActivity extends Activity {
    private CollageModel model;
    private CollageLibraryFragment libraryFragment;
    private CollageFragment collageFragment;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // news
        model = ((CollageApplication)this.getApplication()).getModelInstance();
        libraryFragment = new CollageLibraryFragment();
        collageFragment = new CollageFragment();
        LinearLayout mainLayout = new LinearLayout(this);
        FrameLayout imageListLayout = new FrameLayout(this);
        FrameLayout collageAreaLayout = new FrameLayout(this);

//        FragmentManager fragmentManager = this.getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(imageList.getId(), libraryFragment);
//        fragmentTransaction.add(collageArea.getId(), collageFragment);
//        fragmentTransaction.commit();

        // Setup main layout
        mainLayout.setOrientation(LinearLayout.HORIZONTAL);
        mainLayout.addView(collageAreaLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        mainLayout.addView(imageListLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 2));

        // Setup collage area layouts
        collageAreaLayout.setBackgroundColor(Color.BLUE);
        collageAreaLayout.setId(10);

        // Setup list
        imageListLayout.setBackgroundColor(Color.RED);
        imageListLayout.setId(11);

        setContentView(mainLayout);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
