package in.sportscafe.nostragamus.module.play.dummygame;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.module.play.tindercard.FlingCardListener;

/**
 * Created by Jeeva on 15/03/17.
 */
public class DummyCardAutoMover {

    private static final String TAG = "DummyCardAutoMover";

    private static final long ANIMATION_DURATION = 500;

    private FlingCardListener mFlingCardListener;

    private View mViewToBeAnimated;

    private float mMaxX;

    private float mMinX;

    private float mCenterX;

    private float mCenterY;

    private float mLastEndX;

    public DummyCardAutoMover(View view, FlingCardListener flingCardListener) {
        this.mViewToBeAnimated = view;
        this.mFlingCardListener = flingCardListener;

        float viewWidth = view.getMeasuredWidth();
        mCenterX = view.getX() + viewWidth / 2;
        mCenterY = view.getY() + view.getMeasuredHeight() / 2;
        mXMoveValue = mCenterX;

        mMinX = mCenterX - viewWidth / 5;
        mMaxX = mCenterX + viewWidth / 5;

        Log.d(TAG, "Before touch started --> " + mCenterX + ", " + mCenterY + ", " + mMinX + ", " + mMaxX);
    }

    public void startAnimate() {
        startViewTouch(mCenterX, mCenterY);

        startPropertyAnimation(mCenterX, mMinX);
    }

    private void startPropertyAnimation(float startX, float endX) {
        mLastEndX = endX;

        ValueAnimator va = ValueAnimator.ofFloat(startX, endX);
        va.setDuration(ANIMATION_DURATION);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.d(TAG, "Touch move --> " + animation.getAnimatedValue());
                moveView((float) animation.getAnimatedValue(), mCenterY);
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                checkNextAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        va.start();
    }

    private int mCount = 0;

    private void checkNextAnimation() {
        /*if (mLastEndX == mMinX) {
            startPropertyAnimation(mMinX, mCenterX - 1);
        } else if (mLastEndX == mCenterX - 1) {
            startPropertyAnimation(mCenterX, mMaxX);
        } else if (mLastEndX == mMaxX) {
            startPropertyAnimation(mMaxX, mCenterX);
        } else {
            endViewTouch(mCenterX, mCenterY);
        }*/

        mCount++;
        if (mCount == 1) {
            startPropertyAnimation(mMinX, mCenterX);
        } else /*if (mCount == 2) {
            startPropertyAnimation(mCenterX, mMaxX);
        } else if (mCount == 3) {
            startPropertyAnimation(mMaxX, mCenterX);
        } else*/ {
            endViewTouch(mCenterX, mCenterY);
        }
    }

    public void stopAnimate() {
        destroyAll();
    }

    private void startViewTouch(float x, float y) {
        MotionEvent motionEvent = MotionEvent.obtain(
                SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis() + 100,
                MotionEvent.ACTION_DOWN,
                x,
                y,
                0
        );

        mFlingCardListener.onTouch(mViewToBeAnimated, motionEvent);

        Log.d(TAG, "Touch started");
    }

    private void endViewTouch(float x, float y) {
        MotionEvent motionEvent = MotionEvent.obtain(
                SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis() + 100,
                MotionEvent.ACTION_UP,
                x,
                y,
                0
        );

        mFlingCardListener.onTouch(mViewToBeAnimated, motionEvent);

        Log.d(TAG, "Touch released");
    }

    private Float mOldX;

    private float mXMoveValue;

    private void moveView(float x, float y) {
        if(null != mOldX) {
            mXMoveValue += x - mOldX;
        }

        MotionEvent motionEvent = MotionEvent.obtain(
                0,
                0,
                MotionEvent.ACTION_MOVE,
                mXMoveValue,
                y,
                0
        );

        mFlingCardListener.onTouch(mViewToBeAnimated, motionEvent);

        mOldX = x;
    }

    private void destroyAll() {
        mViewToBeAnimated = null;
        mFlingCardListener = null;
    }
}