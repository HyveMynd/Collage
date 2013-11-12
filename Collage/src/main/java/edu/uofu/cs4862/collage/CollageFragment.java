package edu.uofu.cs4862.collage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import edu.uofu.cs4862.collage.multitouch.MultiTouchImageView;

/**
 * Created by Andres on 11/5/13.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CollageFragment extends Fragment {
    private PhotoCollageView photoCollageView;
    private LinearLayout mainLayout;
    private FrameLayout innerLayout;
    private CollageModel model;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        model = ((CollageApplication)activity.getApplication()).getModel();
        photoCollageView = new PhotoCollageView(activity);
        mainLayout = new LinearLayout(activity);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        innerLayout = new FrameLayout(activity);
        photoCollageView.setImages(model.getImages());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainLayout.addView(innerLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
        innerLayout.addView(photoCollageView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return mainLayout;
    }

    @Override
    public void onPause() {
        photoCollageView.unloadImages();
        super.onPause();
    }

    @Override
    public void onResume() {
        photoCollageView.loadImages(this.getActivity());
        super.onResume();
    }
}
