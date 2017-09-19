package in.sportscafe.nostragamus.module.prediction;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.prediction.dto.PowerupEnum;

/**
 * Created by sandip on 18/09/17.
 */

public class PredictionUiHelper {

    public View getPowerUpView(Context context, PowerupEnum powerupEnum) {
        View view = View.inflate(context, R.layout.inflater_powerup_applied, null);
        ImageView icon = (ImageView) view.findViewById(R.id.powerup_applied_iv_icon);
        view.setTag(powerupEnum);

        switch (powerupEnum) {
            case DOUBLER:
                icon.setImageResource(R.drawable.powerup_2x_white);
                icon.setBackground(getPowerUpDrawable(context,R.color.dodger_blue));
                break;

            case NO_NEGATIVE:
                icon.setImageResource(R.drawable.powerup_nonegs_white);
                icon.setBackground(getPowerUpDrawable(context, R.color.amaranth));
                break;

            case PLAYER_POLL:
                // NO UI
                break;

            default:
                return null;
        }

        return view;
    }

    private Drawable getPowerUpDrawable(Context context, int colorRes) {
        GradientDrawable powerupDrawable = new GradientDrawable();
        powerupDrawable.setShape(GradientDrawable.OVAL);
        powerupDrawable.setColor(ContextCompat.getColor(context, colorRes));
        return powerupDrawable;
    }

    public void showPowerUpAnimation(final View view, final RelativeLayout parent) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
                animatePowerupViewsIfAddedMoreThanOne(parent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(scaleAnimation);
    }

    private void animatePowerupViewsIfAddedMoreThanOne(RelativeLayout parent) {
        if (parent != null && parent.getChildCount() > 1) {
            View childView1 = parent.getChildAt(0);
            View childView2 = parent.getChildAt(1);

            int moveUpt0Dp = (int) parent.getContext().getResources().getDimension(R.dimen.dp_30);
            childView1.animate().setDuration(750).translationX(-moveUpt0Dp).start();
            childView2.animate().setDuration(750).translationX(moveUpt0Dp).start();
        }
    }

    public void dismissPowerUpAnimation(final View view, Animation.AnimationListener animationListener) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setAnimationListener(animationListener);
        view.startAnimation(scaleAnimation);
    }

    public void animateOtherPowerupWhenAnyOneRemoved(RelativeLayout parent) {
        if (parent.getChildCount() == 1) {
            View childView1 = parent.getChildAt(0);
            int center = parent.getLayoutParams().width / 2;
            childView1.animate().setDuration(750).translationX(center).start();
        }
    }

}
