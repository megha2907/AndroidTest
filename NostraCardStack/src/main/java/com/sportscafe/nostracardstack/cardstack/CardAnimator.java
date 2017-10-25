package com.sportscafe.nostracardstack.cardstack;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import java.util.ArrayList;
import java.util.HashMap;

import static com.sportscafe.nostracardstack.cardstack.CardUtils.cloneParams;
import static com.sportscafe.nostracardstack.cardstack.CardUtils.getMoveBottomParams;
import static com.sportscafe.nostracardstack.cardstack.CardUtils.getMoveLeftParams;
import static com.sportscafe.nostracardstack.cardstack.CardUtils.getMoveRightParams;
import static com.sportscafe.nostracardstack.cardstack.CardUtils.getMoveTopParams;
import static com.sportscafe.nostracardstack.cardstack.CardUtils.move;
import static com.sportscafe.nostracardstack.cardstack.CardUtils.scale;

public class CardAnimator {
    private static final String DEBUG_TAG = "CardAnimator";

    public static final int TOP = 48;
    public static final int BOTTOM = 80;

    private static final int REMOTE_DISTANCE = 1000;

    private int mBackgroundColor;
    public ArrayList<View> mCardCollection;
    private float mRotation;
    private HashMap<View, LayoutParams> mLayoutsMap;
    private LayoutParams[] mRemoteLayouts = new LayoutParams[4];
    private LayoutParams baseLayout;
    private int mStackMargin;
    private int mGravity = BOTTOM;
    private boolean mEnableRotation;
    private int mTopMargin;

    public CardAnimator(ArrayList<View> viewCollection, int backgroundColor, int margin, int topMargin) {
        mCardCollection = viewCollection;
        mBackgroundColor = backgroundColor;
        mStackMargin = margin;
        mTopMargin = topMargin;
        setup();
    }

    private void setup() {
        mLayoutsMap = new HashMap<View, LayoutParams>();

        for (View v : mCardCollection) {
            //setup basic layout
            LayoutParams params = (LayoutParams) v.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.width = LayoutParams.MATCH_PARENT;
            params.height = LayoutParams.WRAP_CONTENT;
            params.topMargin = mTopMargin;

            if (mBackgroundColor != -1) {
                v.setBackgroundColor(mBackgroundColor);
            }

            v.setLayoutParams(params);
        }

        baseLayout = (LayoutParams) mCardCollection.get(0).getLayoutParams();
        baseLayout.topMargin = mTopMargin;
        baseLayout = cloneParams(baseLayout);

    }

    public void initLayout() {
        int size = mCardCollection.size();
        for (View v : mCardCollection) {
            int index = mCardCollection.indexOf(v);
            if (index != 0) {
                index -= 1;
            }
            LayoutParams params = cloneParams(baseLayout);
            v.setLayoutParams(params);

            scale(v, -(size - index - 1) * 5, mGravity);

            int margin = index * mStackMargin;
            move(v, mGravity == TOP ? -margin : margin, 0);
            v.setRotation(0);

            LayoutParams paramsCopy =
                    cloneParams((LayoutParams) v.getLayoutParams());
            paramsCopy.topMargin = mTopMargin;
            mLayoutsMap.put(v, paramsCopy);
        }

        setupRemotes();
    }

    public void setGravity(int gravity) {
        mGravity = gravity;
    }

    private void setupRemotes() {
        View topView = getTopView();
        mRemoteLayouts[CardDirection.LEFT] = getMoveLeftParams(topView, REMOTE_DISTANCE);
        mRemoteLayouts[CardDirection.RIGHT] = getMoveRightParams(topView, REMOTE_DISTANCE);
        mRemoteLayouts[CardDirection.UP] = getMoveTopParams(topView, REMOTE_DISTANCE);
        mRemoteLayouts[CardDirection.DOWN] = getMoveBottomParams(topView, REMOTE_DISTANCE);

    }

    private View getTopView() {
        return mCardCollection.get(mCardCollection.size() - 1);
    }

    private void moveToBack(View child) {
        final ViewGroup parent = (ViewGroup) child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }

    public void reorder() {

        View temp = getTopView();
        //RelativeLayout.LayoutParams tempLp = mLayoutsMap.get(mCardCollection.get(0));
        //mLayoutsMap.put(temp,tempLp);
        moveToBack(temp);

        for (int i = (mCardCollection.size() - 1); i > 0; i--) {
            //View next = mCardCollection.get(i);
            //RelativeLayout.LayoutParams lp = mLayoutsMap.get(next);
            //mLayoutsMap.remove(next);
            View current = mCardCollection.get(i - 1);

            //current replace next
            mCardCollection.set(i, current);
            //mLayoutsMap.put(current,lp);

        }

        mCardCollection.set(0, temp);
    }

    public void discard(int direction, final AnimatorListener al) {
        AnimatorSet as = new AnimatorSet();
        ArrayList<Animator> aCollection = new ArrayList<Animator>();


        final View topView = getTopView();
        LayoutParams topParams = (LayoutParams) topView.getLayoutParams();
        LayoutParams layout = cloneParams(topParams);
        ValueAnimator discardAnim = ValueAnimator.ofObject(new RelativeLayoutParamsEvaluator(), layout, mRemoteLayouts[direction]);

        discardAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator value) {
                topView.setLayoutParams((LayoutParams) value.getAnimatedValue());
            }
        });

        discardAnim.setDuration(250);
        aCollection.add(discardAnim);

        for (int i = 0; i < mCardCollection.size(); i++) {
            final View v = mCardCollection.get(i);

            if (v == topView) continue;
            final View nv = mCardCollection.get(i + 1);
            LayoutParams layoutParams = (LayoutParams) v.getLayoutParams();
            LayoutParams endLayout = cloneParams(layoutParams);
            ValueAnimator layoutAnim = ValueAnimator.ofObject(new RelativeLayoutParamsEvaluator(), endLayout, mLayoutsMap.get(nv));
            layoutAnim.setDuration(250);
            layoutAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator value) {
                    v.setLayoutParams((LayoutParams) value.getAnimatedValue());
                }
            });
            aCollection.add(layoutAnim);
        }

        as.addListener(new AnimatorListenerAdapter() {


            @Override
            public void onAnimationEnd(Animator animation) {
                reorder();
                if (al != null) {
                    al.onAnimationEnd(animation);
                }
                mLayoutsMap = new HashMap<View, LayoutParams>();
                for (View v : mCardCollection) {
                    LayoutParams params = (LayoutParams) v.getLayoutParams();
                    LayoutParams paramsCopy = cloneParams(params);
                    mLayoutsMap.put(v, paramsCopy);
                }

            }

        });


        as.playTogether(aCollection);
        as.start();
    }

    public void discardByCall(int direction, final AnimatorListener al) {
        AnimatorSet as = new AnimatorSet();
        ArrayList<Animator> aCollection = new ArrayList<Animator>();

        final View topView = getTopView();
        LayoutParams layoutParams = null;
        switch (direction) {
            case CardDirection.LEFT:
                layoutParams = getMoveLeftParams(topView, REMOTE_DISTANCE);
                break;

            case CardDirection.RIGHT:
                layoutParams = getMoveRightParams(topView, REMOTE_DISTANCE);
                break;

            case CardDirection.UP:
                layoutParams = getMoveTopParams(topView, REMOTE_DISTANCE);
                break;

            case CardDirection.DOWN:
                layoutParams = getMoveBottomParams(topView, REMOTE_DISTANCE);
                break;
        }

        LayoutParams topParams = (LayoutParams) topView.getLayoutParams();
        LayoutParams layout = cloneParams(topParams);
        if (layoutParams != null) {
            ValueAnimator discardAnim = ValueAnimator.ofObject(new RelativeLayoutParamsEvaluator(), layout, layoutParams);

            discardAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator value) {
                    topView.setLayoutParams((LayoutParams) value.getAnimatedValue());
                }
            });

            discardAnim.setDuration(250);
            aCollection.add(discardAnim);
        }

        for (int i = 0; i < mCardCollection.size(); i++) {
            final View v = mCardCollection.get(i);

            if (v == topView) continue;
            final View nv = mCardCollection.get(i + 1);
            layoutParams = (LayoutParams) v.getLayoutParams();
            LayoutParams endLayout = cloneParams(layoutParams);
            ValueAnimator layoutAnim = ValueAnimator.ofObject(new RelativeLayoutParamsEvaluator(), endLayout, mLayoutsMap.get(nv));
            layoutAnim.setDuration(250);
            layoutAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator value) {
                    v.setLayoutParams((LayoutParams) value.getAnimatedValue());
                }
            });
            aCollection.add(layoutAnim);
        }

        as.addListener(new AnimatorListenerAdapter() {


            @Override
            public void onAnimationEnd(Animator animation) {
                reorder();
                if (al != null) {
                    al.onAnimationEnd(animation);
                }
                mLayoutsMap = new HashMap<View, LayoutParams>();
                for (View v : mCardCollection) {
                    LayoutParams params = (LayoutParams) v.getLayoutParams();
                    LayoutParams paramsCopy = cloneParams(params);
                    mLayoutsMap.put(v, paramsCopy);
                }

            }

        });

        as.playTogether(aCollection);
        as.start();
    }

    public void reverse(MotionEvent e1, MotionEvent e2) {
        final View topView = getTopView();
        ValueAnimator rotationAnim = ValueAnimator.ofFloat(mRotation, 0f);
        rotationAnim.setDuration(250);
        rotationAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator v) {
                topView.setRotation(((Float) (v.getAnimatedValue())).floatValue());
            }
        });

        rotationAnim.start();

        for (final View v : mCardCollection) {
            LayoutParams layoutParams = (LayoutParams) v.getLayoutParams();
            LayoutParams endLayout = cloneParams(layoutParams);
            ValueAnimator layoutAnim = ValueAnimator.ofObject(new RelativeLayoutParamsEvaluator(), endLayout, mLayoutsMap.get(v));
            layoutAnim.setDuration(100);
            layoutAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator value) {
                    v.setLayoutParams((LayoutParams) value.getAnimatedValue());
                }
            });
            layoutAnim.start();
        }

    }

    public void drag(MotionEvent e1, MotionEvent e2, float distanceX,
                     float distanceY) {

        View topView = getTopView();
        int x_diff = (int) ((e2.getRawX() - e1.getRawX()));
        int y_diff = (int) ((e2.getRawY() - e1.getRawY()));
        float rotation_coefficient = 20f;
        LayoutParams layoutParams = (LayoutParams) topView.getLayoutParams();
        LayoutParams topViewLayouts = mLayoutsMap.get(topView);
        layoutParams.leftMargin = topViewLayouts.leftMargin + x_diff;
        layoutParams.rightMargin = topViewLayouts.rightMargin - x_diff;
        layoutParams.topMargin = topViewLayouts.topMargin + y_diff;
        layoutParams.bottomMargin = topViewLayouts.bottomMargin - y_diff;

        if (mEnableRotation) {
            mRotation = (x_diff / rotation_coefficient);
            topView.setRotation(mRotation);
            topView.setLayoutParams(layoutParams);
        }

        //animate secondary views.
        /*for (View v : mCardCollection) {
            int index = mCardCollection.indexOf(v);
            if (v != getTopView() && index != 0) {
                LayoutParams l = CardUtils.scaleFrom(v, mLayoutsMap.get(v), (int) (Math.abs(x_diff) * 0.05), mGravity);
                CardUtils.moveFrom(v, l, 0, (int) (Math.abs(x_diff) * index * 0.05), mGravity);
            }
        }*/
    }

    public void setStackMargin(int margin) {
        mStackMargin = margin;
    }

    public void setTopMargin(int margin) {
        mTopMargin = margin;
    }

    public boolean isEnableRotation() {
        return mEnableRotation;
    }

    public void setEnableRotation(boolean enableRotation) {
        mEnableRotation = enableRotation;
    }
}
