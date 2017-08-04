package in.sportscafe.nostragamus.module.coachmarker;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.R;

/**
 * Created by tanjunrong on 2/10/15.
 */
public class TourGuide {



    /**
     * This describes the allowable motion, for example if you want the users to learn about clicking, but want to stop them from swiping, then use CLICK_ONLY
     */
    public enum MotionType {
        ALLOW_ALL, CLICK_ONLY, SWIPE_ONLY
    }
    private List<FrameLayoutWithHole> mFrameList = new ArrayList<>();

    protected MotionType mMotionType;

    private int mPointerGravity = Gravity.CENTER;

    private View.OnClickListener mOverLayClickListener;

    /*************
     *
     * Public API
     *
     *************/

    /* Static builder */
    public static TourGuide init(int pointerGravity, View.OnClickListener listener){
        return new TourGuide(pointerGravity, listener);
    }

    /* Constructor */
    public TourGuide(int pointerGravity, View.OnClickListener listener){
        this.mPointerGravity = pointerGravity;
        this.mOverLayClickListener = listener;
    }

    /**
     * Sets the targeted view for TourGuide to play on
     * @param targetViews the view in which the tutorial button will be placed on top of
     * @param basicOverlay
     * @return return TourGuide instance for chaining purpose
     */
    public TourGuide playOn(ViewGroup mainView, FrameLayout basicOverlay, TargetView... targetViews) {
        if(null != targetViews && targetViews.length > 0) {
            for (TargetView targetView : targetViews) {
                setupView(mainView, basicOverlay, targetView);
            }
        } else {
            startView(mainView, basicOverlay);
        }
        return this;
    }

    /**
     * Clean up the tutorial that is added to the activity
     */
     public void cleanUp() {
         for (FrameLayoutWithHole layoutWithHole : mFrameList) {
             layoutWithHole.cleanUp();
         }
         mFrameList.clear();
         mFrameList = null;
    }
    /******
     *
     * Private methods
     *
     *******/
    //TODO: move into Pointer
    private int getXBasedOnGravity(int width, int targetViewWidth, View targetView) {
        int [] pos = new int[2];
        targetView.getLocationOnScreen(pos);
        int x = pos[0];
        if((mPointerGravity & Gravity.RIGHT) == Gravity.RIGHT){
            return x+targetViewWidth-width;
        } else if ((mPointerGravity & Gravity.LEFT) == Gravity.LEFT) {
            return x;
        } else { // this is center
            return x+targetViewWidth/2-width/2;
        }
    }
    //TODO: move into Pointer
    private int getYBasedOnGravity(int height, int targetViewHeight, View targetView) {
        int [] pos = new int[2];
        targetView.getLocationInWindow(pos);
        int y = pos[1];
        if((mPointerGravity & Gravity.BOTTOM) == Gravity.BOTTOM){
            return y+targetViewHeight-height;
        } else if ((mPointerGravity & Gravity.TOP) == Gravity.TOP) {
            return y;
        }else { // this is center
            return y+targetViewHeight/2-height/2;
        }
    }
    protected void setupView(final ViewGroup mainView, final FrameLayout basicOverlay, final TargetView targetView){
        // TourGuide can only be setup after all the views is ready and obtain it's position/measurement
        // so when this is the 1st time TourGuide is being added,
        // else block will be executed, and ViewTreeObserver will make TourGuide setup process to be delayed until everything is ready
        // when this is run the 2nd or more times, if block will be executed
        if (ViewCompat.isAttachedToWindow(targetView.view)) {
            startView(mainView, basicOverlay, targetView);
        } else {
            final ViewTreeObserver viewTreeObserver = targetView.view.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    targetView.view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    startView(mainView, basicOverlay, targetView);
                }
            });
        }
    }

    private void startView(ViewGroup mainView, FrameLayout basicOverlay) {
        /* Initialize a frame layout with a hole */
        FrameLayoutWithHole layoutWithHole = new FrameLayoutWithHole(mainView.getContext(), mMotionType);
        mFrameList.add(layoutWithHole);

        /* handle click disable */
        handleDisableClicking(layoutWithHole, null);

        setupFrameLayout(mainView, basicOverlay, layoutWithHole);
    }

    private void startView(ViewGroup mainView, FrameLayout basicOverlay, TargetView targetView){
        /* Initialize a frame layout with a hole */
        FrameLayoutWithHole layoutWithHole = new FrameLayoutWithHole(mainView.getContext(), targetView.view, mMotionType);
        mFrameList.add(layoutWithHole);

        /* handle click disable */
        handleDisableClicking(layoutWithHole, targetView.view);

        /* setup floating action button */
        performAnimationOn(getOuterCircle(layoutWithHole, targetView),
                getInnerCircle(layoutWithHole, targetView), layoutWithHole);

        setupFrameLayout(mainView, basicOverlay, layoutWithHole);
    }

    private void handleDisableClicking(FrameLayoutWithHole frameLayoutWithHole, View targetView) {
        if(null != targetView) {
            frameLayoutWithHole.setViewHole(targetView);
        }

        frameLayoutWithHole.setSoundEffectsEnabled(false);
        frameLayoutWithHole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanUp();
                if(null != mOverLayClickListener) {
                    mOverLayClickListener.onClick(v);
                }
            }
        });
    }

    private View getInnerCircle(FrameLayoutWithHole frameLayoutWithHole, TargetView targetView) {
        View innerCircle = new View(frameLayoutWithHole.getContext());
        innerCircle.setBackgroundResource(R.drawable.overlay_inner_circle);

        Resources resources = frameLayoutWithHole.getResources();
        int x = resources.getDimensionPixelSize(R.dimen.dp_20);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(x, x);
        frameLayoutWithHole.addView(innerCircle, params);

        // measure size of image to be placed
        params.setMargins(getXBasedOnGravity(x, targetView.width, targetView.view),
                getYBasedOnGravity(x, targetView.height, targetView.view), 0, 0);

        return innerCircle;
    }

    private View getOuterCircle(FrameLayoutWithHole frameLayoutWithHole, TargetView targetView){
        View outerCircle = new View(frameLayoutWithHole.getContext());
        outerCircle.setBackgroundResource(R.drawable.overlay_outer_circle);

        Resources resources = frameLayoutWithHole.getResources();
        int x = resources.getDimensionPixelSize(R.dimen.dp_60);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(x, x);
        frameLayoutWithHole.addView(outerCircle, params);

        // measure size of image to be placed
        params.setMargins(getXBasedOnGravity(x, targetView.width, targetView.view),
                getYBasedOnGravity(x, targetView.height, targetView.view), 0, 0);

        return outerCircle;
    }

    private void setupFrameLayout(ViewGroup mainView, FrameLayout basicOverlay, FrameLayoutWithHole layoutWithHole){
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        int [] pos = new int[2];
        mainView.getLocationOnScreen(pos);
        // frameLayoutWithHole's coordinates are calculated taking full screen height into account
        // but we're adding it to the content area only, so we need to offset it to the same Y value of contentArea

        layoutParams.setMargins(0,-pos[1],0,0);
        if(null == basicOverlay.getParent()) {
            mainView.addView(basicOverlay, layoutParams);
        }
        mainView.addView(layoutWithHole, layoutParams);
    }

    private void performAnimationOn(final View outerCircle, final View innerCircle, FrameLayoutWithHole layoutWithHole) {
        long fadeInDuration = 400;
        long scaleUpDuration = 1500;
        long fadeOutDuration = 1500;

        final float minScale = 0.3f;
        final float maxScale = 1;

        final AnimatorSet firstAnimSet = new AnimatorSet();
        final AnimatorSet secondAnimSet = new AnimatorSet();

        firstAnimSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                outerCircle.setScaleX(minScale);
                outerCircle.setScaleY(minScale);
                outerCircle.setTranslationX(maxScale);
                secondAnimSet.start();
            }
        });

        secondAnimSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                outerCircle.setScaleX(minScale);
                outerCircle.setScaleY(minScale);
                outerCircle.setTranslationX(maxScale);
                firstAnimSet.start();
            }
        });


        ValueAnimator fadeDefaultImage = ObjectAnimator.ofFloat(innerCircle, "alpha", 1f, 0f);
        fadeDefaultImage.setDuration(fadeInDuration);

        ValueAnimator fadeDefaultImage2 = ObjectAnimator.ofFloat(innerCircle, "alpha", 1f, 0f);
        fadeDefaultImage2.setDuration(fadeInDuration);


        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(outerCircle, "scaleX", minScale, maxScale);
        scaleDownX.setDuration(scaleUpDuration);

        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(outerCircle, "scaleY", minScale, maxScale);
        scaleDownY.setDuration(scaleUpDuration);

        ValueAnimator fadeOutAnim = ObjectAnimator.ofFloat(outerCircle, "alpha", 1f, 0f);
        fadeOutAnim.setDuration(fadeOutDuration);

        ObjectAnimator scaleDownX2 = ObjectAnimator.ofFloat(outerCircle, "scaleX", minScale, maxScale);
        scaleDownX2.setDuration(scaleUpDuration);

        ObjectAnimator scaleDownY2 = ObjectAnimator.ofFloat(outerCircle, "scaleY", minScale, maxScale);
        scaleDownY2.setDuration(scaleUpDuration);

        ValueAnimator fadeOutAnim2 = ObjectAnimator.ofFloat(outerCircle, "alpha", 1f, 0f);
        fadeOutAnim2.setDuration(fadeOutDuration);

        firstAnimSet.play(fadeDefaultImage);
        firstAnimSet.play(scaleDownX).with(scaleDownY);
        firstAnimSet.play(fadeOutAnim).with(scaleDownY);

        secondAnimSet.play(fadeDefaultImage2);
        secondAnimSet.play(scaleDownX2).with(scaleDownY2);
        secondAnimSet.play(fadeOutAnim2).with(scaleDownY2);

        firstAnimSet.start();

            /* these animatorSets are kept track in FrameLayout, so that they can be cleaned up when FrameLayout is detached from window */
        layoutWithHole.addAnimatorSet(firstAnimSet);
        layoutWithHole.addAnimatorSet(secondAnimSet);
    }

    public boolean dismiss() {
        if(null != mFrameList && mFrameList.size() > 0) {
            mFrameList.get(0).performClick();
            return true;
        }
        return false;
    }
}