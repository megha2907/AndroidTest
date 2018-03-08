package in.sportscafe.nostragamus.module.navigation.help.dummygame;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.widgets.customfont.Typefaces;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.utils.AnimationHelper;

import static android.R.attr.animation;

/**
 * A simple {@link Fragment} subclass.
 */
public class DGPowerUpInfoFragment extends BaseFragment implements View.OnClickListener {

    private DGPowerupInfoFragmentListener mFragmentListener;
    private DGInstruction mInstruction;

    public interface DGPowerupInfoFragmentListener {
        void onLearnPowerupClicked();
    }

    public DGPowerUpInfoFragment() {
    }

    public void setInstruction(DGInstruction instruction) {
        this.mInstruction = instruction;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DGPowerupInfoFragmentListener) {
            mFragmentListener = (DGPowerupInfoFragmentListener) context;
        } else
            throw new IllegalArgumentException(context.getClass().getSimpleName() + " should implement the DGPowerupInfoFragmentListener");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dmpower_up_info, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setSpannedHeading();
        animateLayouts();
    }

    private void animateLayouts() {
        if (getView() != null) {
            // heading
            TextView headingTextView = (TextView) getView().findViewById(R.id.dg_powerup_heading_textView);
            animateView(headingTextView, 500, 1000);

            // Sub heading
            TextView subHeadingTextView = (TextView) getView().findViewById(R.id.dg_powerup_sub_heading_textView);
            animateView(subHeadingTextView, 500, 3000);

            // powerup layouts
            LinearLayout powerupLayout = (LinearLayout) getView().findViewById(R.id.dg_powerup_info_layout);
            animateView(powerupLayout, 1000, 6000);

            // Button
            Button button = (Button) getView().findViewById(R.id.dm_learn_powerup_btn);
            animateView(button, 500, 8000);
        }
    }

    private void animateView(final View view, int duration, int startOfSet) {
        Animation animation = new AlphaAnimation(0f, 1f);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        animation.setStartOffset(startOfSet);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    private void initViews(View view) {
        view.findViewById(R.id.dm_learn_powerup_btn).setOnClickListener(this);
    }

    private void setSpannedHeading() {
        if (getView() != null && mInstruction != null && mInstruction.getScoredPoints() != null) {
            Typeface faceBold = Typefaces.get(getContext(), "fonts/lato/Lato-Bold.ttf");

            String headingStr = "You have Scored #+10 points$ for that prediction!";
            headingStr = headingStr.replace("+10", (mInstruction.getScoredPoints() > 0) ?
                    "+" + String.valueOf(mInstruction.getScoredPoints()) :
                    String.valueOf(mInstruction.getScoredPoints()));

            int startIndex = headingStr.indexOf("#");
            int endIndex = headingStr.indexOf("$");
            headingStr = headingStr.replace("#", "");
            headingStr = headingStr.replace("$", "");

            SpannableStringBuilder builder = new SpannableStringBuilder();
            SpannableString whiteSpannable = new SpannableString(headingStr);
            if (mInstruction.getScoredPoints() > 0) {
                whiteSpannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.greencolor)), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (mInstruction.getScoredPoints() < 0) {
                whiteSpannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.radical_red)), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            builder.append(whiteSpannable);

            TextView headingTextView = (TextView) getView().findViewById(R.id.dg_powerup_heading_textView);
            headingTextView.setText(builder, TextView.BufferType.SPANNABLE);
            headingTextView.setTypeface(faceBold);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dm_learn_powerup_btn:
                if (mFragmentListener != null) {
                    mFragmentListener.onLearnPowerupClicked();
                }
                break;
        }
    }

    public void applyInstruction(DGInstruction instruction) {
        mInstruction = instruction;
        setSpannedHeading();
    }
}
