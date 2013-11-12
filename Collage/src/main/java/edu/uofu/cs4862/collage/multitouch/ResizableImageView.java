package edu.uofu.cs4862.collage.multitouch;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;

import edu.uofu.cs4862.collage.CollageActivity;
import edu.uofu.cs4862.collage.R;

/**
 * Created by Andres on 11/11/13.
 */
public class ResizableImageView extends View {
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    private static final int INVALID_POINTER_ID = -1;

    // The ‘active pointer’ is the one currently moving our object.
    private int mActivePointerId = INVALID_POINTER_ID;

    private Drawable mDrawable;
    private ArrayList<Drawable> mImages;

    private float mPosX, mPosY;
    private float mLastTouchX, mLastTouchY;

    private int mDisplayWidth, mDisplayHeight;

    public ResizableImageView(Context context) {
        this(context, null, 0);
    }

    public ResizableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ResizableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDrawable = context.getResources().getDrawable(R.drawable.lake);
    }

    public ResizableImageView(Context context, Bitmap image){
        this(context, null, 0);
        mDrawable = new BitmapDrawable(context.getResources(), image);
        mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());

        // Create our ScaleGestureDetector
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

    }

    public ResizableImageView(Context context, Collection<Bitmap> images){
        this(context, null, 0);
        init(context);
    }

    private void init(Context context){
        Resources res = context.getResources();
        DisplayMetrics metrics = res.getDisplayMetrics();
        mDisplayWidth = res.getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE ? Math.max(metrics.widthPixels,
                metrics.heightPixels) : Math.min(metrics.widthPixels, metrics.heightPixels);
        mDisplayHeight = res.getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE ? Math.min(metrics.widthPixels,
                metrics.heightPixels) : Math.max(metrics.widthPixels, metrics.heightPixels);

    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(mPosX, mPosY);
        mDrawable.draw(canvas);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();

                mLastTouchX = x;
                mLastTouchY = y;

                // Save the ID of this pointer
                mActivePointerId = ev.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float x = ev.getX(pointerIndex);
                final float y = ev.getY(pointerIndex);

                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                mPosX += dx;
                mPosY += dy;

                mLastTouchX = x;
                mLastTouchY = y;

                invalidate();
                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                // Extract the index of the pointer that left the touch sensor
                final int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                break;
            }
        }

        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

            invalidate();
            return true;
        }
    }
}
