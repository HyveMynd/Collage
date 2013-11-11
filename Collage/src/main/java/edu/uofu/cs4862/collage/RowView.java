package edu.uofu.cs4862.collage;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by andresmonroy on 11/9/13.
 */
public class RowView extends View {

    private static final String SIZE_FORMAT_STRING = "%s X %s";
    private LinearLayout mainLayout;
    private ImageView thumbnailView;
    private TextView sizeView;
    private TextView dateView;

    public RowView(Context context) {
        super(context);
        mainLayout = new LinearLayout(context);
        thumbnailView = new ImageView(context);
        sizeView = new TextView(context);
        dateView = new TextView(context);
    }

    public RowView(Context context, Bitmap image, String date) {
        this(context);
        init(image, date);
    }

    private void init(Bitmap image, String date){
        mainLayout.setOrientation(LinearLayout.HORIZONTAL);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mainLayout.setPadding(3, 0, 3, 0);

        setThumbnailImage(image);
        setSizeText(image.getWidth(), image.getHeight());
        setDateText(date);

        mainLayout.addView(thumbnailView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mainLayout.addView(sizeView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mainLayout.addView(dateView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setSizeText(int width, int height){
        sizeView.setText(String.format(SIZE_FORMAT_STRING, width, height));
    }

    public void setDateText(String date){
        dateView.setText(date);
    }

    public void setThumbnailImage(Bitmap image){
        thumbnailView.setImageBitmap(image);
    }
}
