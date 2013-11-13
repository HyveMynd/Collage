package edu.uofu.cs4862.collage;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * Created by Andres on 11/5/13.
 */
public class ImageLibraryFragment extends ListFragment {

    private CollageModel model;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = ((CollageApplication)this.getActivity().getApplication()).getModel();
        this.setListAdapter(new CollageImageAdapter(this.getActivity(), model));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

}
