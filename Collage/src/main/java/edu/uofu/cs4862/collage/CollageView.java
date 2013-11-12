package edu.uofu.cs4862.collage;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import edu.uofu.cs4862.collage.multitouch.ImageEntity;
import edu.uofu.cs4862.collage.multitouch.MultiTouchController;
import edu.uofu.cs4862.collage.multitouch.MultiTouchEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static edu.uofu.cs4862.collage.multitouch.MultiTouchController.PointInfo;
import static edu.uofu.cs4862.collage.multitouch.MultiTouchController.PositionAndScale;

/**
 * Created by andresmonroy on 11/8/13.
 */
public class CollageView extends View implements MultiTouchController.MultiTouchObjectCanvas<MultiTouchEntity> {

    private ArrayList<MultiTouchEntity> mImages = new ArrayList<MultiTouchEntity>();
    private MultiTouchController<MultiTouchEntity> multiTouchController =
            new MultiTouchController<MultiTouchEntity>(this);
    private PointInfo currTouchPoint = new PointInfo();
    private static final int UI_MODE_ROTATE = 1, UI_MODE_ANISOTROPIC_SCALE = 2;
    private int mUIMode = UI_MODE_ROTATE;
    private Paint mLinePaintTouchPointCircle = new Paint();
    private int displayWidth, displayHeight;

    // ---------------------------------------------------------------------------------------------------

    public CollageView(Context context) {
        this(context, null);
    }

    public CollageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        Resources res = context.getResources();
        mLinePaintTouchPointCircle.setColor(Color.YELLOW);
        mLinePaintTouchPointCircle.setStrokeWidth(5);
        mLinePaintTouchPointCircle.setStyle(Paint.Style.STROKE);
        mLinePaintTouchPointCircle.setAntiAlias(true);
        setBackgroundColor(context.getResources().getColor(R.color.dark_gray));
        DisplayMetrics metrics = res.getDisplayMetrics();
        this.displayWidth = res.getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE ? Math.max(metrics.widthPixels,
                metrics.heightPixels) : Math.min(metrics.widthPixels, metrics.heightPixels);
        this.displayHeight = res.getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE ? Math.min(metrics.widthPixels,
                metrics.heightPixels) : Math.max(metrics.widthPixels, metrics.heightPixels);
    }

    public void doOnResume(Context context){
        ExecutorService exe = Executors.newSingleThreadExecutor();
        exe.execute(new DoLoadImages(context));
        exe.shutdown();
        try {
            exe.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** Called by activity's onResume() method to load the images */
    public void loadImages(Context context) {
        for (MultiTouchEntity entity : mImages){
            entity.load(context, displayWidth / 2, displayHeight / 2);
        }
    }

    public void setImages(Collection<Bitmap> images){
        for (Bitmap image : images){
            mImages.add(new ImageEntity(image, getResources()));
        }
    }

    /** Called by activity's onPause() method to free memory used for loading the images */
    public void unloadImages() {
        for (MultiTouchEntity entity : mImages){
            entity.unload();
        }
    }

    // ---------------------------------------------------------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (MultiTouchEntity entity : mImages){
            entity.draw(canvas);
        }
    }

    // ---------------------------------------------------------------------------------------------------

    /** Pass touch events to the MT controller */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return multiTouchController.onTouchEvent(event);
    }

    /** Get the image that is under the single-touch point, or return null (canceling the drag op) if none */
    public MultiTouchEntity getDraggableObjectAtPoint(PointInfo pt) {
        float x = pt.getX(), y = pt.getY();
        for (MultiTouchEntity entity : mImages){
            ImageEntity im = (ImageEntity)entity;
            if (im.containsPoint(x, y))
                return im;
        }
        return null;
    }

    /**
     * Select an object for dragging. Called whenever an object is found to be under the point (non-null is returned by getDraggableObjectAtPoint())
     * and a drag operation is starting. Called with null when drag op ends.
     */
    public void selectObject(MultiTouchEntity img, PointInfo touchPoint) {
        currTouchPoint.set(touchPoint);
        if (img != null) {
            // Move image to the top of the stack when selected
            mImages.remove(img);
            mImages.add(img);
        } else {
            // Called with img == null when drag stops.
        }
        invalidate();
    }

    /** Get the current position and scale of the selected image. Called whenever a drag starts or is reset. */
    public void getPositionAndScale(MultiTouchEntity img, PositionAndScale objPosAndScaleOut) {
        // FIXME affine-izem (and fix the fact that the anisotropic_scale part requires averaging the two scale factors)
        objPosAndScaleOut.set(img.getCenterX(), img.getCenterY(), (mUIMode & UI_MODE_ANISOTROPIC_SCALE) == 0,
                (img.getScaleX() + img.getScaleY()) / 2, (mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0, img.getScaleX(), img.getScaleY(),
                (mUIMode & UI_MODE_ROTATE) != 0, img.getAngle());
    }

    /** Set the position and scale of the dragged/stretched image. */
    public boolean setPositionAndScale(MultiTouchEntity img,
                                       PositionAndScale newImgPosAndScale, PointInfo touchPoint) {
        currTouchPoint.set(touchPoint);
        boolean ok = img.setPos(newImgPosAndScale);
        if (ok)
            invalidate();
        return ok;
    }

    public boolean pointInObjectGrabArea(PointInfo pt, MultiTouchEntity img) {
        return false;
    }

    class DoLoadImages implements Runnable{
        Context context;

        DoLoadImages(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            loadImages(context);
        }
    }
}
