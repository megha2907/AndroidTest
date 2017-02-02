package in.sportscafe.nostragamus.module.coachmarker;

import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * TODO: document your custom view class.
 */
public class FrameLayoutWithHole extends FrameLayout {
    private TextPaint mTextPaint;
    private TourGuide.MotionType mMotionType;
    private Paint mEraser;

    Bitmap mEraserBitmap;
    private Canvas mEraserCanvas;
    private Paint mPaint;
    private Paint transparentPaint;
    private View mViewHole; // This is the targeted view to be highlighted, where the hole should be placed
    private int mRadius;
    private int[] mPos;
    private float mDensity;

    private ArrayList<AnimatorSet> mAnimatorSetArrayList;

    public void setViewHole(View viewHole) {
        this.mViewHole = viewHole;
        enforceMotionType();
    }

    public void addAnimatorSet(AnimatorSet animatorSet) {
        if (mAnimatorSetArrayList == null) {
            mAnimatorSetArrayList = new ArrayList<AnimatorSet>();
        }
        mAnimatorSetArrayList.add(animatorSet);
    }

    private void enforceMotionType() {
        Log.d("tourguide", "enforceMotionType 1");
        if (mViewHole != null) {
            Log.d("tourguide", "enforceMotionType 2");
            if (mMotionType != null && mMotionType == TourGuide.MotionType.CLICK_ONLY) {
                Log.d("tourguide", "enforceMotionType 3");
                Log.d("tourguide", "only Clicking");
                mViewHole.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        mViewHole.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });
            } else if (mMotionType != null && mMotionType == TourGuide.MotionType.SWIPE_ONLY) {
                Log.d("tourguide", "enforceMotionType 4");
                Log.d("tourguide", "only Swiping");
                mViewHole.setClickable(false);
            }
        }
    }

    public FrameLayoutWithHole(Context context, View view, TourGuide.MotionType motionType) {
        super(context);
        mViewHole = view;
        init();
        enforceMotionType();

        int[] pos = new int[2];
        mViewHole.getLocationOnScreen(pos);
        mPos = pos;

        mDensity = context.getResources().getDisplayMetrics().density;
        int padding = (int) (20 * mDensity);

        if (mViewHole.getHeight() > mViewHole.getWidth()) {
            mRadius = mViewHole.getHeight() / 2 + padding;
        } else {
            mRadius = mViewHole.getWidth() / 2 + padding;
        }
        mMotionType = motionType;
    }

    private void init() {
        setWillNotDraw(false);
        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        Point size = new Point();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        size.x = displayMetrics.widthPixels;
        size.y = displayMetrics.heightPixels;

        mEraserBitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888);
        mEraserCanvas = new Canvas(mEraserBitmap);

        mPaint = new Paint();
        mPaint.setColor(0xcc000000);
        transparentPaint = new Paint();
        transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        mEraser = new Paint();
        mEraser.setColor(0xFFFFFFFF);
        mEraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mEraser.setFlags(Paint.ANTI_ALIAS_FLAG);

        Log.d("tourguide", "getHeight: " + size.y);
        Log.d("tourguide", "getWidth: " + size.x);

    }

    private boolean mCleanUpLock = false;

    protected void cleanUp() {
        if (getParent() != null) {
            ((ViewGroup) this.getParent()).removeView(this);
        }
    }

    /* comment this whole method to cause a memory leak */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        /* cleanup reference to prevent memory leak */
        mEraserCanvas.setBitmap(null);
        mEraserBitmap = null;

        if (mAnimatorSetArrayList != null && !mAnimatorSetArrayList.isEmpty()) {
            for (int i = 0; i < mAnimatorSetArrayList.size(); i++) {
                mAnimatorSetArrayList.get(i).end();
                mAnimatorSetArrayList.get(i).removeAllListeners();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mEraserCanvas.drawColor(Color.TRANSPARENT);

        /*mEraserBitmap.eraseColor(Color.TRANSPARENT);
        int padding = (int) (10 * mDensity);
        Log.i("TOURGUIDE", String.format("**********PADDING: %s**********", padding));

        mEraserCanvas.drawCircle(
                mPos[0] + mViewHole.getWidth() / 2,
                mPos[1] + mViewHole.getHeight() / 2,
                mRadius, mEraser);

        canvas.drawBitmap(mEraserBitmap, 0, 0, null);*/
    }
}
