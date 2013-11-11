package edu.uofu.cs4862.collage;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by andresmonroy on 11/9/13.
 */
public class CollageImageAdapter extends BaseAdapter {

    private Context context;
    private CollageModel model;

    public CollageImageAdapter (Context context, CollageModel model){
        this.context = context;
        this.model = model;
    }

    @Override
    public int getCount() {
        return model.count();
    }

    @Override
    public Object getItem(int position) {
        return model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageData data = model.get(position);
        RowView view;
        if (convertView == null && convertView instanceof RowView){
            view = (RowView)convertView;
            view.setDateText(data.getDate());
            view.setSizeText(data.getImage().getWidth(), data.getImage().getHeight());
            view.setThumbnailImage(data.getThumbnail());
        } else {
            view = new RowView(context, data.getImage(), data.getDate());
        }
        return view;
    }

    public void add(Bitmap image, String date){
        ImageData data = new ImageData(image, date);
        model.add(data);
    }
}
