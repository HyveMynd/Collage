package edu.uofu.cs4862.collage;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by andresmonroy on 11/8/13.
 */
public class PhotoCollageView extends View implements MultiTouchController.MultiTouchObjectCanvas<MultiTouchEntity> {

    private ArrayList<MultiTouchEntity> mImages;
    private MultiTouchController<MultiTouchEntity> multiTouchController;
    private MultiTouchController.PointInfo currTouchPoint;
    private boolean mShowDebugInfo = false;
    private static final int UI_MODE_ROTATE = 1, UI_MODE_ANISOTROPIC_SCALE = 2;
    private int mUIMode = UI_MODE_ROTATE;
    private Paint mLinePaintTouchPointCircle;
    private static final float SCREEN_MARGIN = 100;
    private int width, height, displayWidth, displayHeight;

    public PhotoCollageView(Context context) {
        super(context);

        mImages = new ArrayList<MultiTouchEntity>();
        multiTouchController = new MultiTouchController<MultiTouchEntity>(this);
        currTouchPoint = new MultiTouchController.PointInfo();
        mLinePaintTouchPointCircle = new Paint();
    }

    public PhotoCollageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoCollageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        Resources res = context.getResources();

        mLinePaintTouchPointCircle.setColor(Color.YELLOW);
        mLinePaintTouchPointCircle.setStrokeWidth(5);
        mLinePaintTouchPointCircle.setStyle(Paint.Style.STROKE);
        mLinePaintTouchPointCircle.setAntiAlias(true);
        setBackgroundColor(Color.DKGRAY);

        DisplayMetrics metrics = res.getDisplayMetrics();
        this.displayWidth = res.getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE ? Math.max(metrics.widthPixels,
                metrics.heightPixels) : Math.min(metrics.widthPixels, metrics.heightPixels);
        this.displayHeight = res.getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE ? Math.min(metrics.widthPixels,
                metrics.heightPixels) : Math.max(metrics.widthPixels, metrics.heightPixels);
    }

    /** Called by activity's onResume() method to load the images */
    public void loadImages(Context context) {

        Log.d("PhotoSortrView", "Width: " + this.getWidth());
        Log.d("PhotoSortrView", "Height: " + this.getHeight());

        int n = mImages.size();
        if (n == 1)
        {

            mImages.get(0).load(context, displayWidth / 2, displayHeight / 2);
        }
        else
        {
            for (int i = 0; i < n; i++) {
                float cx = SCREEN_MARGIN + (float)
                        (Math.random() * (displayWidth - 2 * SCREEN_MARGIN));
                float cy = SCREEN_MARGIN + (float)
                        (Math.random() * (displayHeight - 2 * SCREEN_MARGIN));
                mImages.get(i).load(context, cx, cy);
            }
        }
    }

    /** Called by activity's onPause() method to free memory used for loading the images */
    public void unloadImages() {
        int n = mImages.size();
        for (int i = 0; i < n; i++)
            mImages.get(i).unload();
    }

    // ---------------------------------------------------------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int n = mImages.size();
        for (int i = 0; i < n; i++)
            mImages.get(i).draw(canvas);
        if (mShowDebugInfo)
            drawMultitouchDebugMarks(canvas);
    }

    // ---------------------------------------------------------------------------------------------------

    private void drawMultitouchDebugMarks(Canvas canvas) {
        if (currTouchPoint.isDown()) {
            float[] xs = currTouchPoint.getXs();
            float[] ys = currTouchPoint.getYs();
            float[] pressures = currTouchPoint.getPressures();
            int numPoints = Math.min(currTouchPoint.getNumTouchPoints(), 2);
            for (int i = 0; i < numPoints; i++)
                canvas.drawCircle(xs[i], ys[i], 50 + pressures[i] * 80, mLinePaintTouchPointCircle);
            if (numPoints == 2)
                canvas.drawLine(xs[0], ys[0], xs[1], ys[1], mLinePaintTouchPointCircle);
        }
    }

    // ---------------------------------------------------------------------------------------------------

    /** Pass touch events to the MT controller */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return multiTouchController.onTouchEvent(event);
    }

    /** Get the image that is under the single-touch point, or return null (canceling the drag op) if none */
    public MultiTouchEntity getDraggableObjectAtPoint(MultiTouchController.PointInfo pt) {
        float x = pt.getX(), y = pt.getY();
        int n = mImages.size();
        for (int i = n - 1; i >= 0; i--) {
            ImageEntity im = (ImageEntity) mImages.get(i);
            if (im.containsPoint(x, y))
                return im;
        }
        return null;
    }

    /**
     * Select an object for dragging. Called whenever an object is found to be under the point (non-null is returned by getDraggableObjectAtPoint())
     * and a drag operation is starting. Called with null when drag op ends.
     */
    public void selectObject(MultiTouchEntity img, MultiTouchController.PointInfo touchPoint) {
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
    public void getPositionAndScale(MultiTouchEntity img, MultiTouchController.PositionAndScale objPosAndScaleOut) {
        // FIXME affine-izem (and fix the fact that the anisotropic_scale part requires averaging the two scale factors)
        objPosAndScaleOut.set(img.getCenterX(), img.getCenterY(), (mUIMode & UI_MODE_ANISOTROPIC_SCALE) == 0,
                (img.getScaleX() + img.getScaleY()) / 2, (mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0, img.getScaleX(), img.getScaleY(),
                (mUIMode & UI_MODE_ROTATE) != 0, img.getAngle());
    }

    /** Set the position and scale of the dragged/stretched image. */
    public boolean setPositionAndScale(MultiTouchEntity img,
                                       MultiTouchController.PositionAndScale newImgPosAndScale, MultiTouchController.PointInfo touchPoint) {
        currTouchPoint.set(touchPoint);
        boolean ok = ((ImageEntity)img).setPos(newImgPosAndScale);
        if (ok)
            invalidate();
        return ok;
    }

    public boolean pointInObjectGrabArea(MultiTouchController.PointInfo pt, MultiTouchEntity img) {
        return false;
    }
}
