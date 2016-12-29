package in.sportscafe.nostragamus.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class AnimatorBuilder {

    private List<Animator> mAnimatorList = new ArrayList<>();

    public AnimatorBuilder translatorX(View view, float start, float end, long duration) {
        addAnimator(view, "translationX", start, end, duration);
        return this;
    }

    public AnimatorBuilder translatorY(View view, float start, float end, long duration) {
        addAnimator(view, "translationY", start, end, duration);
        return this;
    }

    public AnimatorBuilder translatorXY(View view, float startX, float endX,
                                       float startY, float endY, long duration) {
        translatorX(view, startX, endX, duration);
        translatorY(view, startY, endY, duration);
        return this;
    }

    public AnimatorBuilder alpha(View view, float start, float end, long duration) {
        addAnimator(view, "alpha", start, end, duration);
        return this;
    }

    private void addAnimator(View view, String propertyName, float start, float end, long duration) {
        mAnimatorList.add(ObjectAnimator
                .ofFloat(view, propertyName, start, end)
                .setDuration(duration));
    }

    public Animator build() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(mAnimatorList);
        return animatorSet;
    }
}