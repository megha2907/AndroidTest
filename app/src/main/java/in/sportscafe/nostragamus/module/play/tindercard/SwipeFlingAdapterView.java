package in.sportscafe.nostragamus.module.play.tindercard;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.play.prediction.PredictionAdapter;


public class SwipeFlingAdapterView extends FlingAdapterView {


    private int MAX_VISIBLE = 2;
    private int MIN_ADAPTER_STACK = 2;
    private float ROTATION_DEGREES = 15.f;

    private Adapter mAdapter;
    private int LAST_OBJECT_IN_STACK = -1;
    private OnSwipeListener mSwipeListener;
    private OnScrollListener mScrollListener;
    private AdapterDataSetObserver mDataSetObserver;
    private boolean mInLayout = false;
    private View mActiveCard = null;
    private OnItemClickListener mOnItemClickListener;
    private FlingCardListener flingCardListener;
    private PointF mLastTouchPoint;
    float initialX, initialY;

    private int mMainViewId;

    public SwipeFlingAdapterView(Context context) {
        this(context, null);
    }

    public SwipeFlingAdapterView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.SwipeFlingStyle);
    }

    public SwipeFlingAdapterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeFlingAdapterView, defStyle, 0);
        MAX_VISIBLE = a.getInt(R.styleable.SwipeFlingAdapterView_max_visible, MAX_VISIBLE);
        MIN_ADAPTER_STACK = a.getInt(R.styleable.SwipeFlingAdapterView_min_adapter_stack, MIN_ADAPTER_STACK);
        ROTATION_DEGREES = a.getFloat(R.styleable.SwipeFlingAdapterView_rotation_degrees, ROTATION_DEGREES);
        a.recycle();
    }


    /**
     * A shortcut method to set both the listeners and the adapter.
     *
     * @param context  The activity context which extends OnSwipeListener, OnItemClickListener or both
     * @param mAdapter The adapter you have to set.
     */
    public void init(final Context context, Adapter mAdapter) {
        if (context instanceof OnSwipeListener) {
            mSwipeListener = (OnSwipeListener) context;
        } else {
            throw new RuntimeException("Activity does not implement SwipeFlingAdapterView.OnSwipeListener");
        }
        if (context instanceof OnItemClickListener) {
            mOnItemClickListener = (OnItemClickListener) context;
        }
        setAdapter(mAdapter);
    }

    @Override
    public View getSelectedView() {
        return mActiveCard;
    }


    @Override
    public void requestLayout() {
        if (!mInLayout) {
            super.requestLayout();
        }
    }

    public void refreshTopLayout() {

        boolean oneLastItem= mAdapter.getCount() == 1;

        if(oneLastItem){
            removeViewInLayout(mActiveCard);
        }else {
            removeAllViewsInLayout();
        }

        addViewToFrame(0);

        if (!oneLastItem){
            addViewToFrame(1);
        }

        setTopView();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // if we don't have an adapter, we don't need to do anything
        if (mAdapter == null) {
            return;
        }

        mInLayout = true;
        final int adapterCount = mAdapter.getCount();

        if (adapterCount == 0) {
            removeAllViewsInLayout();
        } else {
            View topCard = getChildAt(LAST_OBJECT_IN_STACK);
            if (mActiveCard != null && topCard != null && topCard == mActiveCard) {
                if (this.flingCardListener.isTouching()) {
                    PointF lastPoint = this.flingCardListener.getLastPoint();
                    if (this.mLastTouchPoint == null || !this.mLastTouchPoint.equals(lastPoint)) {
                        this.mLastTouchPoint = lastPoint;
                        removeViewsInLayout(0, LAST_OBJECT_IN_STACK);
//                        layoutChildren(1, adapterCount);
                    }
                }
            } else {
                // Reset the UI and set top view listener
//                removeAllViewsInLayout();
                if(LAST_OBJECT_IN_STACK == -1) {
                    addViewToFrame(0);
                }

                if(adapterCount > 1) {
                    addViewToFrame(1);
                    LAST_OBJECT_IN_STACK = 1;
                } else {
                    LAST_OBJECT_IN_STACK = 0;
                }
//                layoutChildren(1, adapterCount);
                setTopView();
            }
        }

        mInLayout = false;

//        if (adapterCount <= MIN_ADAPTER_STACK)
        mSwipeListener.onAdapterAboutToEmpty(adapterCount);
    }

    /*private void layoutChildren(int startingIndex, int adapterCount) {
        while (startingIndex < Math.min(adapterCount, MAX_VISIBLE)) {
            addViewToFrame(startingIndex);
            startingIndex++;
        }
    }*/

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInEditMode()) {
            super.onDraw(canvas);
        }
    }

    private void addViewToFrame(int position) {
        View newUnderChild = mAdapter.getView(position, null, this);
        if (newUnderChild.getVisibility() != GONE) {
            makeAndAddView(newUnderChild);
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void makeAndAddView(View child) {

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
        addViewInLayout(child, 0, lp, true);

        final boolean needToMeasure = child.isLayoutRequested();
        if (needToMeasure) {
            int childWidthSpec = getChildMeasureSpec(getWidthMeasureSpec(),
                    getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin,
                    lp.width);
            int childHeightSpec = getChildMeasureSpec(getHeightMeasureSpec(),
                    getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin,
                    lp.height);
            child.measure(childWidthSpec, childHeightSpec);
        } else {
            cleanupLayoutState(child);
        }


        int w = child.getMeasuredWidth();
        int h = child.getMeasuredHeight();

        int gravity = lp.gravity;
        if (gravity == -1) {
            gravity = Gravity.TOP | Gravity.START;
        }


        int layoutDirection = getLayoutDirection();
        final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
        final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

        int childLeft;
        int childTop;
        switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.CENTER_HORIZONTAL:
                childLeft = (getWidth() + getPaddingLeft() - getPaddingRight() - w) / 2 +
                        lp.leftMargin - lp.rightMargin;
                break;
            case Gravity.END:
                childLeft = getWidth() + getPaddingRight() - w - lp.rightMargin;
                break;
            case Gravity.START:
            default:
                childLeft = getPaddingLeft() + lp.leftMargin;
                break;
        }
        switch (verticalGravity) {
            case Gravity.CENTER_VERTICAL:
                childTop = (getHeight() + getPaddingTop() - getPaddingBottom() - h) / 2 +
                        lp.topMargin - lp.bottomMargin;
                break;
            case Gravity.BOTTOM:
                childTop = getHeight() - getPaddingBottom() - h - lp.bottomMargin;
                break;
            case Gravity.TOP:
            default:
                childTop = getPaddingTop() + lp.topMargin;
                break;
        }

        child.layout(childLeft, childTop, childLeft + w, childTop + h);
    }


    /**
     * Set the top view and add the fling listener
     */
    private void setTopView() {
        if (getChildCount() > 0) {

            mActiveCard = getChildAt(LAST_OBJECT_IN_STACK);
            if (mActiveCard != null) {

                flingCardListener = new FlingCardListener(mActiveCard, mAdapter.getItem(0),
                        ROTATION_DEGREES, new FlingCardListener.FlingListener() {

                    @Override
                    public void onCardExited(Object dataObject) {
                        mActiveCard = null;
                        mSwipeListener.removeFirstObjectInAdapter(dataObject);
                    }

                    @Override
                    public void leftExit(Object dataObject) {
                        mSwipeListener.onLeftSwipe(dataObject);
                    }

                    @Override
                    public void rightExit(Object dataObject) {
                        mSwipeListener.onRightSwipe(dataObject);
                    }

                    @Override
                    public void topExit(Object dataObject) {
                        mSwipeListener.onTopSwipe(dataObject);
                    }

                    @Override
                    public void bottomExit(Object dataObject) {
                        mSwipeListener.onBottomSwipe(dataObject);
                    }

                    @Override
                    public boolean needLeftSwipe() {
                        return mSwipeListener.needLeftSwipe();
                    }

                    @Override
                    public boolean needRightSwipe() {
                        return mSwipeListener.needRightSwipe();
                    }

                    @Override
                    public boolean needTopSwipe() {
                        return mSwipeListener.needTopSwipe();
                    }

                    @Override
                    public boolean needBottomSwipe() {
                        return mSwipeListener.needBottomSwipe();
                    }

                    @Override
                    public void onClick(Object dataObject) {
                        if (mOnItemClickListener != null)
                            mOnItemClickListener.onItemClicked(0, dataObject);

                    }

                    @Override
                    public void onScroll(float scrollProgressPercent) {
                        if(null != mScrollListener) {
                            mScrollListener.onScroll(scrollProgressPercent);
                        }
                    }

                    @Override
                    public void onCardMoving(int xPercent, int yPercent) {
                        mSwipeListener.onCardMoving(xPercent, yPercent);
                    }
                });

                mActiveCard.findViewById(mMainViewId).setOnTouchListener(flingCardListener);
            }
        }
    }



    public FlingCardListener getTopCardListener() throws NullPointerException {
        if (flingCardListener == null) {
            throw new NullPointerException();
        }
        return flingCardListener;
    }

    public void setMaxVisible(int MAX_VISIBLE) {
        this.MAX_VISIBLE = MAX_VISIBLE;
    }

    public void setMinStackInAdapter(int MIN_ADAPTER_STACK) {
        this.MIN_ADAPTER_STACK = MIN_ADAPTER_STACK;
    }

    @Override
    public Adapter getAdapter() {
        return mAdapter;
    }


    @Override
    public void setAdapter(Adapter adapter) {
        if(null != mAdapter) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        mAdapter = adapter;

        mAdapter.registerDataSetObserver(mDataSetObserver = new AdapterDataSetObserver());
    }

    public void setSwipeListener(OnSwipeListener OnSwipeListener) {
        this.mSwipeListener = OnSwipeListener;
    }

    public void setScrollListener(OnScrollListener onScrollListener) {
        this.mScrollListener = onScrollListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FrameLayout.LayoutParams(getContext(), attrs);
    }

    public void setAdapter(ArrayAdapter adapter, int mainViewId) {
        this.mMainViewId = mainViewId;
        setAdapter(adapter);
    }


    public interface OnItemClickListener {
        void onItemClicked(int itemPosition, Object dataObject);
    }


    public interface OnSwipeListener<T> {

        void removeFirstObjectInAdapter(T dataObject);

        void onLeftSwipe(T dataObject);

        void onRightSwipe(T dataObject);

        void onTopSwipe(T dataObject);

        void onBottomSwipe(T dataObject);

        void onAdapterAboutToEmpty(int itemsInAdapter);

        boolean needLeftSwipe();

        boolean needRightSwipe();

        boolean needTopSwipe();

        boolean needBottomSwipe();

        void onCardMoving(float xPercent, float yPercent);
    }

    public interface OnScrollListener {

        void onScroll(float scrollProgressPercent);
    }

    private class AdapterDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            requestLayout();
        }

        @Override
        public void onInvalidated() {
            requestLayout();
        }

    }


}