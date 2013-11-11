package edu.uofu.cs4862.collage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by andresmonroy on 11/9/13.
 */
public class CollageImageAdapter extends ArrayAdapter<ImageData> {

    private Context context;
    private CollageModel model;
    private static final int TEXT_SIZE = 11;

    public CollageImageAdapter (Context context, CollageModel model){
        super(context, 0);
        this.context = context;
        this.model = model;
    }

    @Override
    public int getCount() {
        return model.count();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageData data = model.get(position);
        ViewHolder holder;
        LinearLayout row = (LinearLayout)convertView;
        if (row == null){
            holder = new ViewHolder();
            row = new LinearLayout(context);
            row.setBackgroundColor(Color.BLACK);
            row.addView(createInnerRowLayout(holder));
            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }
        holder.thumbnail.setImageBitmap(data.getThumbnail());
        holder.sizeView.setText(data.getImageSize());
        holder.dateView.setText(data.getDate());
        return row;
    }

    private RelativeLayout createInnerRowLayout(ViewHolder holder) {
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        RelativeLayout.LayoutParams sizeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        sizeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        RelativeLayout.LayoutParams dateParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dateParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        RelativeLayout innerLayout = new RelativeLayout(context);
        innerLayout.addView(holder.thumbnail, imageParams);
        innerLayout.addView(holder.sizeView, sizeParams);
        innerLayout.addView(holder.dateView, dateParams);
        return innerLayout;
    }

    public void add(Bitmap image, String date){
        ImageData data = new ImageData(image, date);
        model.add(data);
        notifyDataSetChanged();
    }

    class ViewHolder{
        ImageView thumbnail;
        TextView sizeView;
        TextView dateView;

        ViewHolder() {
            this.thumbnail = new ImageView(context);
            this.sizeView = new TextView(context);
            sizeView.setTextSize(TEXT_SIZE);
            this.dateView = new TextView(context);
            dateView.setTextSize(TEXT_SIZE);
        }
    }
}
