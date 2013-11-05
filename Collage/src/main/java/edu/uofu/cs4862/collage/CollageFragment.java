package edu.uofu.cs4862.collage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * Created by Andres on 11/5/13.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CollageFragment extends Fragment {
    private ImageView collageAreaLayout;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        collageAreaLayout = new ImageView(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        collageAreaLayout.setLayoutParams(new Gallery.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
