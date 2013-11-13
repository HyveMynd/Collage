package edu.uofu.cs4862.collage;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Andres on 11/5/13.
 */
public class CollageFragment extends Fragment {
    private CollageView photoCollageView;
    private LinearLayout mainLayout;
    private FrameLayout innerLayout;
    private CollageModel model;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        model = ((CollageApplication)activity.getApplication()).getModel();
        photoCollageView = new CollageView(activity);
        mainLayout = new LinearLayout(activity);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        innerLayout = new FrameLayout(activity);
    }

    private Collection<Bitmap> getBitmaps(){
        ExecutorService exe = Executors.newSingleThreadExecutor();
        Collection<Bitmap> bitmaps = null;
        try {
            bitmaps = exe.submit(new Callable<Collection<Bitmap>>() {
                @Override
                public Collection<Bitmap> call() throws Exception {
                    Collection<ImageData> imageDatas = model.getOnScreenImages();
                    ArrayList<Bitmap> images = new ArrayList<Bitmap>();
                    for (ImageData data : imageDatas) {
                        images.add(data.getImage());
                    }
                    return images;
                }
            }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return bitmaps;
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
        photoCollageView.setImages(getBitmaps());
        photoCollageView.doOnResume(this.getActivity());
        super.onResume();
    }
}
