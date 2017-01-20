package in.sportscafe.nostragamus.module.play.tindercard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

public class FlingCardListener implements View.OnTouchListener {

    enum Direction {
        LEFT, RIGHT, TOP, BOTTOM
    }

    private static final String TAG = FlingCardListener.class.getSimpleName();

    private static final float PERCENT = 1f;
    private static final float DELTA = 10f;
    private static final int INVALID_POINTER_ID = -1;
    private final float objectX;
    private final float objectY;
    private final int objectH;
    private final int objectW;
    private final int parentWidth;
    private final int parentHeight;
    private final FlingListener mFlingListener;
    private final Object dataObject;
    private final float halfWidth;
    private final float halfHeight;
    private final int TOUCH_ABOVE = 0;
    private final int TOUCH_BELOW = 1;
    private final Object obj = new Object();
    private float BASE_ROTATION_DEGREES;
    private float aPosX;
    private float aPosY;
    private float aDownTouchX;
    private float aDownTouchY;
    // The active pointer is the one currently moving our object.
    private int mActivePointerId = INVALID_POINTER_ID;
    private View frame = null;
    private int touchPosition;
    private boolean isAnimationRunning = false;
    private float MAX_COS = (float) Math.cos(Math.toRadians(45));

    private boolean mIsRotation = true;

    private boolean mIsXAxis = true;

    private boolean mIsYAxis = true;


    public FlingCardListener(View frame, Object itemAtPosition, FlingListener flingListener) {
        this(frame, itemAtPosition, 15f, flingListener);
    }

    public FlingCardListener(View frame, Object itemAtPosition, float rotation_degrees, FlingListener flingListener) {
        super();

        this.frame = frame;
        this.objectX = frame.getX();
        this.objectY = frame.getY();
        this.objectH = frame.getHeight();
        this.objectW = frame.getWidth();
        this.halfWidth = objectW / 2f;
        this.halfHeight = objectH / 2f;
        this.dataObject = itemAtPosition;
        this.parentWidth = ((ViewGroup) frame.getParent()).getWidth();
        this.parentHeight = ((ViewGroup) frame.getParent()).getHeight();
        this.BASE_ROTATION_DEGREES = rotation_degrees;
        this.mFlingListener = flingListener;

    }

    public View getFrame() {
        return frame;
    }

    public boolean onTouch(View view, MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

//                MainActivity.removeBackground();


                mActivePointerId = event.getPointerId(0);
                float x = 0;
                float y = 0;
                boolean success = false;
                try {
                    x = event.getX(mActivePointerId);
                    y = event.getY(mActivePointerId);
                    success = true;
                } catch (IllegalArgumentException e) {
                    Log.w(TAG, "Exception in onTouch(view, event) : " + mActivePointerId, e);
                }
                if (success) {

                    aDownTouchX = x;
                    aDownTouchY = y;

                    if (aPosX == 0) {
                        aPosX = frame.getX();
                    }
                    if (aPosY == 0) {
                        aPosY = frame.getY();
                    }

                    if (y < objectH / 2) {
                        touchPosition = TOUCH_ABOVE;
                    } else {
                        touchPosition = TOUCH_BELOW;
                    }
                }

                view.getParent().requestDisallowInterceptTouchEvent(true);
                break;

            case MotionEvent.ACTION_UP:
                mActivePointerId = INVALID_POINTER_ID;
                resetCardViewOnStack();
                view.getParent().requestDisallowInterceptTouchEvent(false);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                break;

            case MotionEvent.ACTION_POINTER_UP:
                // Extract the index of the pointer that left the touch sensor
                final int pointerIndex = (event.getAction() &
                        MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mActivePointerId = event.getPointerId(newPointerIndex);
                }
                break;
            case MotionEvent.ACTION_MOVE:

                // Find the index of the active pointer and fetch its position
                final int pointerIndexMove = event.findPointerIndex(mActivePointerId);
                final float xMove = event.getX(pointerIndexMove);
                final float yMove = event.getY(pointerIndexMove);


                final float dx = xMove - aDownTouchX;
                final float dy = yMove - aDownTouchY;


                aPosX += dx;
                /*if(mIsXAxis) {
                } else {
                    float tempX = aPosX + dx;
                    if(tempX > mLeftXStopPos && tempX < mRightXStopPos) {
                        aPosX = tempX;
                    }
                }*/

                aPosY += dy;
                /*if(mIsYAxis) {
                } else {
                    float tempY = aPosY + dy;
                    if(tempY > mTopYStopPos && tempY < mBottomYStopPos) {
                        aPosY = tempY;
                    }
                }*/

                float distobjectX = aPosX - objectX;
                float rotation = BASE_ROTATION_DEGREES * 2.f * distobjectX / parentWidth;
                if (touchPosition == TOUCH_BELOW) {
                    rotation = -rotation;
                }

//                checkSwipeDirection();

                frame.setX(aPosX);

                frame.setY(aPosY);

                if(mIsRotation) {
                    frame.setRotation(rotation);
                }

                mFlingListener.onScroll(getScrollProgressPercent());
                break;

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                view.getParent().requestDisallowInterceptTouchEvent(false);
                break;
            }
        }

        try {
            return true;
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private float getScrollProgressPercent() {
        float xPercent = ((aPosX + halfWidth - leftBorder()) / (rightBorder() - leftBorder())) * 2f - 1f;
        float yPercent = ((aPosY + halfHeight - topBorder()) / (bottomBorder() - topBorder())) * 2f - 1f;
        mFlingListener.onCardMoving((int) (xPercent/1f * 100), (int) (yPercent/1f * 100));

        return xPercent;


        /*if (movedBeyondLeftBorder()) {
            mFlingListener.onCardMovedtoLeft();
            return -1f;
        } else if (movedBeyondRightBorder()) {
            mFlingListener.onCardMovedtoRight();
            return 1f;
        } else if (movedBeyondBottomBorder()) {
            mFlingListener.onCardMovedtoBottom();
            return 1f;
        } else {
            if(movedBeyondTopBorder()) {
                mFlingListener.onCardMovedtoTop();
            } else {
                mFlingListener.onCardNotInMoveRegion();
            }*/




//        }
    }

    private float mLeftXStopPos;
    private float mRightXStopPos;

    private float mTopYStopPos;
    private float mBottomYStopPos;

    private void checkSwipeDirection() {
        if(movingToLeftBorder() || movingToRightBorder()) {
            mIsXAxis = true;
            mIsYAxis = false;

            mTopYStopPos = aPosY - DELTA;
            mBottomYStopPos = aPosY + DELTA;
        } else if(movingToTopBorder() || movingToBottomBorder()) {
            mIsXAxis = false;
            mIsYAxis = true;

            mLeftXStopPos = aPosX - DELTA;
            mRightXStopPos = aPosX + DELTA;
        } else {
            mIsXAxis = true;
            mIsYAxis = true;
        }

        com.jeeva.android.Log.d("x, y --> ", mIsXAxis + ", " + mIsYAxis);
    }

    private boolean resetCardViewOnStack() {
        if (movedBeyondLeftBorder()) {
            // Left Swipe
            onSelected(Direction.LEFT, objectX, getExitPoint(-objectW), 100);
            mFlingListener.onScroll(-1.0f);
        } else if (movedBeyondRightBorder()) {
            // Right Swipe
            onSelected(Direction.RIGHT, objectX, getExitPoint(parentWidth), 100);
            mFlingListener.onScroll(1.0f);
        } else if (mFlingListener.needBottomSwipe() && movedBeyondBottomBorder()) {
            // Bottom Swipe
            onSelected(Direction.BOTTOM, objectX, getExitPoint(parentHeight), 100);
            mFlingListener.onScroll(1.0f);
        } else if (mFlingListener.needTopSwipe() && movedBeyondTopBorder()) {
            // Bottom Swipe
            onSelected(Direction.TOP, objectX, getExitPoint(-objectH), 100);
            mFlingListener.onScroll(1.0f);
        } else {
            float abslMoveDistance = Math.abs(aPosX - objectX);
            aPosX = 0;
            aPosY = 0;
            aDownTouchX = 0;
            aDownTouchY = 0;
            frame.animate()
                    .setDuration(200)
                    .setInterpolator(new OvershootInterpolator(1.5f))
                    .x(objectX)
                    .y(objectY)
                    .rotation(0)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {}

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            getScrollProgressPercent();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {}

                        @Override
                        public void onAnimationRepeat(Animator animation) {}
                    });
            mFlingListener.onScroll(0.0f);
            if (abslMoveDistance < 4.0) {
                mFlingListener.onClick(dataObject);
            }
        }
        return false;
    }

    private boolean movedBeyondLeftBorder() {
        return aPosX + halfWidth < leftBorder();
    }

    private boolean movedBeyondRightBorder() {
        return aPosX + halfWidth > rightBorder();
    }

    private boolean movedBeyondTopBorder() {
        return aPosY + halfHeight < topBorder();
    }

    private boolean movedBeyondBottomBorder() {
        return aPosY + halfHeight > bottomBorder();
    }

    private boolean movingToLeftBorder() {
        return aPosX + halfWidth < leftBorder() * PERCENT;
    }

    private boolean movingToRightBorder() {
        return aPosX + halfWidth > rightBorder() * PERCENT;
    }

    private boolean movingToTopBorder() {
        return aPosY + halfHeight < topBorder() * PERCENT;
    }

    private boolean movingToBottomBorder() {
        return aPosY + halfHeight > bottomBorder() * PERCENT;
    }

    public float leftBorder() {
        return parentWidth / 4.f;
    }

    public float rightBorder() {
        return 3 * parentWidth / 4.f;
    }

    public float topBorder() {
        return parentHeight / 4.f;
    }

    public float bottomBorder() {
        return 3 * parentHeight / 4.f;
    }

    public void onSelected(final Direction direction, float exitX, float exitY, long duration) {
        isAnimationRunning = true;
        if (Direction.LEFT == direction) {
            exitX = -objectW - getRotationWidthOffset();
        } else if (Direction.RIGHT == direction) {
            exitX = parentWidth + getRotationWidthOffset();
        } else if (Direction.TOP == direction) {
            exitY = -objectH - getRotationHeightOffset();
        } else {
            exitY = parentHeight + getRotationHeightOffset();
        }

        this.frame.animate()
                .setDuration(duration)
                .setInterpolator(new AccelerateInterpolator())
                .x(exitX)
                .y(exitY)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mFlingListener.onCardExited(dataObject);

                        if (Direction.LEFT == direction) {
                            mFlingListener.leftExit(dataObject);
                        } else if (Direction.RIGHT == direction) {
                            mFlingListener.rightExit(dataObject);
                        } else if (Direction.TOP == direction) {
                            mFlingListener.topExit(dataObject);
                        } else {
                            mFlingListener.bottomExit(dataObject);
                        }

                        isAnimationRunning = false;
                    }
                })
                .rotation(getExitRotation(direction));
    }

    public void selectLeft() {
        Log.i("swipe","left");
        if (!isAnimationRunning)
            onSelected(Direction.LEFT, objectX, objectY, 200);
    }

    public void selectRight() {
        Log.i("swipe","right");
        if (!isAnimationRunning)
            onSelected(Direction.RIGHT, objectX, objectY, 200);
    }

    public void selectTop() {
        Log.i("swipe","top");
        if (!isAnimationRunning)
            onSelected(Direction.TOP, objectX, objectY, 200);
    }

    public void selectBottom() {
        Log.i("swipe","bottom");
        if (!isAnimationRunning)
            onSelected(Direction.BOTTOM, objectX, objectY, 200);
    }

    private float getExitPoint(int exitXPoint) {
        float[] x = new float[2];
        x[0] = objectX;
        x[1] = aPosX;

        float[] y = new float[2];
        y[0] = objectY;
        y[1] = aPosY;

        LinearRegression regression = new LinearRegression(x, y);

        // y = ax+b linear regression
        return (float) regression.slope() * exitXPoint + (float) regression.intercept();
    }

    private float getExitRotation(Direction direction) {
        float rotation;
        if(Direction.LEFT == direction || Direction.RIGHT == direction) {
            rotation = BASE_ROTATION_DEGREES * 2.f * (parentWidth - objectX) / parentWidth;
        } else {
            rotation = BASE_ROTATION_DEGREES * 2.f * (parentHeight - objectY) / parentHeight;
        }

        if (touchPosition == TOUCH_BELOW) {
            rotation = -rotation;
        }

        switch (direction) {
            case LEFT:
            case TOP:
                rotation = -rotation;
                break;
        }
        return rotation;
    }



    private float getRotationWidthOffset() {
        return objectW / MAX_COS - objectW;
    }

    private float getRotationHeightOffset() {
        return objectH / MAX_COS - objectH;
    }

    public void setRotationDegrees(float degrees) {
        this.BASE_ROTATION_DEGREES = degrees;
    }

    public boolean isTouching() {
        return this.mActivePointerId != INVALID_POINTER_ID;
    }

    public PointF getLastPoint() {
        return new PointF(this.aPosX, this.aPosY);
    }

    protected interface FlingListener {
        void onCardExited(Object dataObject);

        void leftExit(Object dataObject);

        void rightExit(Object dataObject);

        void topExit(Object dataObject);

        void bottomExit(Object dataObject);

        boolean needTopSwipe();

        boolean needBottomSwipe();

        void onClick(Object dataObject);

        void onScroll(float scrollProgressPercent);

        void onCardMoving(int xPercent, int yPercent);
    }

}