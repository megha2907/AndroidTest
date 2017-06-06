package in.sportscafe.nostragamus.module.navigation.help.dummygame;

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
public class DGCardAutoMover {

    private interface MoveType {
        int CENTER_TO_LEFT = 0;
        int LEFT_TO_CENTER = 1;
        int CENTER_TO_RIGHT = 2;
        int RIGHT_TO_CENTER = 3;
        int CENTER_TO_TOP = 4;
        int TOP_TO_CENTER = 5;
    }

    private static final String TAG = "DGCardAutoMover";

    private static final long ANIMATION_DURATION = 500;

    private OnDGAutoMoverListener mDGAutoMoverListener;

    private FlingCardListener mFlingCardListener;

    private View mViewToBeAnimated;

    private float mMaxX;

    private float mMinX;

    private float mMaxY;

    private float mMinY;

    private float mCenterX;

    private float mCenterY;

    private float mOldX;

    private float mOldY;

    private int mTypeCount;

    public DGCardAutoMover(View viewToBeAnimated, FlingCardListener flingCardListener, OnDGAutoMoverListener listener) {
        this.mViewToBeAnimated = viewToBeAnimated;
        this.mFlingCardListener = flingCardListener;
        this.mDGAutoMoverListener = listener;

        float viewWidth = viewToBeAnimated.getMeasuredWidth();
        mOldX = mCenterX = viewToBeAnimated.getX() + viewWidth / 2;
        mMinX = mCenterX - viewWidth / 2;
        mMaxX = mCenterX + viewWidth / 2;

        float viewHeight = viewToBeAnimated.getMeasuredHeight();
        mOldY = mCenterY = viewToBeAnimated.getY() + viewHeight / 2;
        mMinY = mCenterY - viewHeight / 2;
        mMaxY = mCenterY + viewHeight / 2;

        Log.d(TAG, "Before touch started --> " + mCenterX + ", " + mCenterY + ", " + mMinX + ", " + mMaxX);
    }

    public void startLeftRightAnimation() {
        startViewTouch(mCenterX, mCenterY);

        mTypeCount = MoveType.CENTER_TO_LEFT;
        checkLeftRightAnimation();
    }

    public void startNeitherAnimation() {
        startViewTouch(mCenterX, mCenterY);

        mTypeCount = MoveType.CENTER_TO_TOP;
        checkNeitherAnimation();
    }

    private void checkLeftRightAnimation() {
        if (MoveType.CENTER_TO_LEFT == mTypeCount) {
            startXMoveAnimation(mCenterX, mMinX);
        } else if (MoveType.LEFT_TO_CENTER == mTypeCount) {
            startXMoveAnimation(mMinX, mCenterX);
        } else if (MoveType.CENTER_TO_RIGHT == mTypeCount) {
            startXMoveAnimation(mCenterX, mMaxX);
        } else if (MoveType.RIGHT_TO_CENTER == mTypeCount) {
            startXMoveAnimation(mMaxX, mCenterX);
        } else {
            endViewTouch(mCenterX, mCenterY);
        }
        mTypeCount++;
    }

    private void checkNeitherAnimation() {
        if (MoveType.CENTER_TO_TOP == mTypeCount) {
            startYMoveAnimation(mCenterY, mMinY);
        } else if (MoveType.TOP_TO_CENTER == mTypeCount) {
            startYMoveAnimation(mMinY, mCenterY);
        } else {
            endViewTouch(mCenterX, mCenterY);
        }
        mTypeCount++;
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

        mDGAutoMoverListener.onAnimationStarted();
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

        mDGAutoMoverListener.onAnimationEnd();
        Log.d(TAG, "Touch released");
    }

    private void moveView(float x, float y) {
        MotionEvent motionEvent = MotionEvent.obtain(
                0,
                0,
                MotionEvent.ACTION_MOVE,
                mCenterX + x - mOldX,
                mCenterY + y - mOldY,
                0
        );

        mFlingCardListener.onTouch(mViewToBeAnimated, motionEvent);

        mOldX = x;
        mOldY = y;
    }

    private void startXMoveAnimation(float startX, float endX) {
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
                checkLeftRightAnimation();
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

    private void startYMoveAnimation(float startY, float endY) {
        ValueAnimator va = ValueAnimator.ofFloat(startY, endY);
        va.setDuration(ANIMATION_DURATION);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.d(TAG, "Touch move --> " + animation.getAnimatedValue());
                moveView(mCenterX, (float) animation.getAnimatedValue());
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                checkNeitherAnimation();
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

    private void destroyAll() {
        mViewToBeAnimated = null;
        mFlingCardListener = null;
    }

    public interface OnDGAutoMoverListener {

        void onAnimationStarted();

        void onAnimationEnd();
    }
}