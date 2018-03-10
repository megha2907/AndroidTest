package in.sportscafe.nostragamus.module.navigation.help.dummygame;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.customfont.Typefaces;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.navigation.help.dummygame.DGAnimation.AnimationType;

/**
 * Created by Jeeva on 30/01/17.
 */

public class DGTextFragment extends NostragamusFragment implements View.OnClickListener {

    interface TextType {
        String TOP_TEXT = "topText";
        String SUB_TOP_TEXT = "subTopText";
        String BOTTOM_TEXT = "bottomText";
        String CENTER_TOP_TEXT = "centerTopText";
        String CENTER_BOTTOM_TEXT = "centerBottomText";
        String ACTION1 = "action1";
        String ACTION2 = "action2";
        String ACTION3 = "action3";
        String TIMELINE_LAYOUT = "timelineLayout";
    }

    private TextView mTvTopText;
    private TextView mTvSubTopText;

    private TextView mTvCenterTopText;

    private TextView mTvCenterBottomText;

    private TextView mTvBottomText;

    private Button mBtnAction1;

    private Button mBtnAction2;

    private Button mBtnAction3;
    private RelativeLayout mTimelineLayout;

    private OnDGTextActionListener mTextActionListener;

    public static DGTextFragment newInstance(DGInstruction instruction) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKeys.DUMMY_INSTRUCTION, Parcels.wrap(instruction));

        DGTextFragment fragment = new DGTextFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDGTextActionListener) {
            this.mTextActionListener = (OnDGTextActionListener) context;
        } else
            throw new IllegalArgumentException(context.getClass().getSimpleName() + " should implement the OnDGTextActionListener");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dummy_game_text, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTimelineLayout = (RelativeLayout) findViewById(R.id.dg_timeline_layout);
        mTvTopText = (TextView) findViewById(R.id.dummy_game_tv_top_text);
        mTvSubTopText = (TextView) findViewById(R.id.dummy_game_tv_sub_top_text);
        mTvCenterTopText = (TextView) findViewById(R.id.dummy_game_tv_center_top_text);
        mTvCenterBottomText = (TextView) findViewById(R.id.dummy_game_tv_center_bottom_text);
        mTvBottomText = (TextView) findViewById(R.id.dummy_game_tv_bottom_text);

        mBtnAction1 = (Button) findViewById(R.id.dummy_game_btn_action1);
        mBtnAction1.setOnClickListener(this);

        mBtnAction2 = (Button) findViewById(R.id.dummy_game_btn_action2);
        mBtnAction2.setOnClickListener(this);

        mBtnAction3 = (Button) findViewById(R.id.dummy_game_btn_action3);
        mBtnAction3.setOnClickListener(this);

        applyInstruction((DGInstruction) Parcels.unwrap(getArguments().getParcelable(BundleKeys.DUMMY_INSTRUCTION)));
    }

    public void applyInstruction(DGInstruction instruction) {
        if (getView() != null) {
            String name = instruction.getName();
            if (null != instruction.getScoredPoints() && !TextUtils.isEmpty(name)) {
                name = String.format(name, instruction.getScoredPoints());
            }

            TextView textView = null;
            switch (instruction.getTextType()) {
                case TextType.TIMELINE_LAYOUT:
                    setTimelineLayout(instruction);
                    break;

                case TextType.TOP_TEXT:
                    textView = mTvTopText;
                    if (textView != null && !TextUtils.isEmpty(name)) {
                        textView.setVisibility(View.VISIBLE);
                        textView.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, getResources().getDisplayMetrics()), 1.0f);
                        textView.setText(name);
                        animateView(textView, instruction.getAnimation());
                    }
                    return;

                case TextType.SUB_TOP_TEXT:
                    textView = mTvSubTopText;
                    break;
                case TextType.BOTTOM_TEXT:
                    textView = mTvBottomText;
                    break;
                case TextType.CENTER_TOP_TEXT:
                    textView = mTvCenterTopText;
                    break;
                case TextType.CENTER_BOTTOM_TEXT:
                    textView = mTvCenterBottomText;
                    break;
                case TextType.ACTION1:
                    textView = mBtnAction1;
                    textView.setTag(instruction.getActionType());
                    break;
                case TextType.ACTION2:
                    textView = mBtnAction2;
                    textView.setTag(instruction.getActionType());
                    break;
                case TextType.ACTION3:
                    textView = mBtnAction3;
                    textView.setTag(instruction.getActionType());
                    break;
            }

            if (null != textView) {
                if (null != name) {
                    checkForColorAndSetText(textView, name);
                }
                textView.setVisibility(View.VISIBLE);
                textView.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, getResources().getDisplayMetrics()), 1.0f);

                animateView(textView, instruction.getAnimation());
            }
        }
    }

    private void setTimelineLayout(DGInstruction instruction) {
        if (mTimelineLayout.getVisibility() == View.INVISIBLE) {
            mTimelineLayout.setVisibility(View.VISIBLE);
            if (instruction.getTimelineState() > 0) {
                switch (instruction.getTimelineState()) {
                    case 1:
                        setTimelineStep1();
                        break;

                    case 2:
                        setTimelineStep1Passed();
                        setTimelineStep2();
                        break;

                    case 3:
                        setTimelineStep1Passed();
                        setTimelineStep2Passed();
                        setTimelineStep3();
                        break;
                }
            }
        }
    }

    private void checkForColorAndSetText(TextView textView, String name) {

        Typeface faceBold = Typefaces.get(getContext(), "fonts/lato/Lato-Bold.ttf");

        if (name.equalsIgnoreCase("After the game finishes, correct predictions get +10 points%, wrong ones -4 points&. Prediction now to see how you score!")) {

            int startIndex = name.indexOf("+");
            int endIndex = name.indexOf("%");
            int startIndexSecond = name.indexOf("-") - 2;
            int endIndexSecond = name.indexOf("&");
            name = name.replace("%", "");
            name = name.replace("&", "");
            SpannableStringBuilder builder = new SpannableStringBuilder();
            SpannableString whiteSpannable = new SpannableString(name);
            whiteSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#22b573")), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            whiteSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#fe3555")), startIndexSecond, endIndexSecond, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(whiteSpannable);
            textView.setText(builder, TextView.BufferType.SPANNABLE);

        } else if (name.equalsIgnoreCase("You scored #10 points$ for that prediction!")) {

            name = "You scored #+10 points$ for that prediction!";
            int startIndex = name.indexOf("#");
            int endIndex = name.indexOf("$");
            name = name.replace("#", "");
            name = name.replace("$", "");
            SpannableStringBuilder builder = new SpannableStringBuilder();
            SpannableString whiteSpannable = new SpannableString(name);
            whiteSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#22b573")), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(whiteSpannable);
            textView.setText(builder, TextView.BufferType.SPANNABLE);

            textView.setTypeface(faceBold);

        } else if (name.equalsIgnoreCase("You scored #-4 points$ for that prediction!")) {
            int startIndex = name.indexOf("#");
            int endIndex = name.indexOf("$");
            name = name.replace("#", "");
            name = name.replace("$", "");
            SpannableStringBuilder builder = new SpannableStringBuilder();
            SpannableString whiteSpannable = new SpannableString(name);
            whiteSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#fe3555")), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(whiteSpannable);
            textView.setText(builder, TextView.BufferType.SPANNABLE);

            textView.setTypeface(faceBold);

        } else if (name.equalsIgnoreCase("You scored #-8 points$ for that prediction!")) {
            int startIndex = name.indexOf("#");
            int endIndex = name.indexOf("$");
            name = name.replace("#", "");
            name = name.replace("$", "");
            SpannableStringBuilder builder = new SpannableStringBuilder();
            SpannableString whiteSpannable = new SpannableString(name);
            whiteSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#fe3555")), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(whiteSpannable);
            textView.setText(builder, TextView.BufferType.SPANNABLE);

            textView.setTypeface(faceBold);

        } else if (name.equalsIgnoreCase("You scored #20 points$ for that prediction!")) {

            name = "You scored #+20 points$ for that prediction!";
            int startIndex = name.indexOf("#");
            int endIndex = name.indexOf("$");
            name = name.replace("#", "");
            name = name.replace("$", "");
            SpannableStringBuilder builder = new SpannableStringBuilder();
            SpannableString whiteSpannable = new SpannableString(name);
            whiteSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#22b573")), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(whiteSpannable);
            textView.setText(builder, TextView.BufferType.SPANNABLE);

            textView.setTypeface(faceBold);

        } else if (name.equalsIgnoreCase("You scored #0 points$ for that prediction!")) {
            int startIndex = name.indexOf("#");
            int endIndex = name.indexOf("$");
            name = name.replace("#", "");
            name = name.replace("$", "");
            SpannableStringBuilder builder = new SpannableStringBuilder();
            SpannableString whiteSpannable = new SpannableString(name);
            whiteSpannable.setSpan(new ForegroundColorSpan(Color.WHITE), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(whiteSpannable);
            textView.setText(builder, TextView.BufferType.SPANNABLE);

            textView.setTypeface(faceBold);

        } else if (name.equalsIgnoreCase("Looks like you are playing for the first time.\nFollow our walk through to master the basics of making prediction in Nostragamus!")) {
            textView.setTypeface(faceBold);
            textView.setText(name);
        } else {
            textView.setText(name);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dummy_game_btn_action1:
            case R.id.dummy_game_btn_action2:
            case R.id.dummy_game_btn_action3:
                mTextActionListener.onActionClicked(view.getTag().toString());
                break;
        }
    }

    public void showBottomText() {
        mTvBottomText.animate().alpha(1).setDuration(1000);
    }

    public void hideBottomText() {
        mTvBottomText.animate().alpha(0).setDuration(1000);
    }

    private void animateView(final TextView textView, DGAnimation animation) {
        Animation viewAnimation = null;

        switch (animation.getType()) {
            case AnimationType.ALPHA:
                viewAnimation = new AlphaAnimation(animation.getStart(), animation.getEnd());
                viewAnimation.setDuration(animation.getDuration());
                viewAnimation.setFillAfter(true);
                break;
        }

        if (null != viewAnimation) {
            textView.startAnimation(viewAnimation);
        }

    }

    public interface OnDGTextActionListener {
        void onActionClicked(String actionType);
    }

    private void setTimelineStep1() {
        TextView step1TextView = (TextView) findViewById(R.id.dg_timeline_step_1_node);
        step1TextView.setBackgroundResource(R.drawable.blue_filled_circle_bg);
        step1TextView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
    }

    private void setTimelineStep1Passed() {
        TextView step1TextView = (TextView) findViewById(R.id.dg_timeline_step_1_node);
        step1TextView.setBackgroundResource(R.drawable.blue_round_tick);
        step1TextView.setText("");
    }

    private void setTimelineStep2() {
        TextView step2TextView = (TextView) findViewById(R.id.dg_timeline_step_2_node);
        step2TextView.setBackgroundResource(R.drawable.blue_filled_circle_bg);
        step2TextView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

        View lineView = (View) findViewById(R.id.dg_timeline_step_2_line);
        lineView.setBackgroundColor(ContextCompat.getColor(lineView.getContext(), R.color.blue_008ae1));
    }

    private void setTimelineStep2Passed() {
        TextView step2TextView = (TextView) findViewById(R.id.dg_timeline_step_2_node);
        step2TextView.setBackgroundResource(R.drawable.blue_round_tick);
        step2TextView.setText("");

        View lineView = (View) findViewById(R.id.dg_timeline_step_2_line);
        lineView.setBackgroundColor(ContextCompat.getColor(lineView.getContext(), R.color.blue_008ae1));
    }

    private void setTimelineStep3() {
        TextView step3TextView = (TextView) findViewById(R.id.dg_timeline_step_3_node);
        step3TextView.setBackgroundResource(R.drawable.blue_filled_circle_bg);
        step3TextView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

        View lineView = (View) findViewById(R.id.dg_timeline_step_3_line);
        lineView.setBackgroundColor(ContextCompat.getColor(lineView.getContext(), R.color.blue_008ae1));
    }

    private void setTimelineStep3Passed() {
        TextView step3TextView = (TextView) findViewById(R.id.dg_timeline_step_3_node);
        step3TextView.setBackgroundResource(R.drawable.blue_round_tick);
        step3TextView.setText("");

        View lineView = (View) findViewById(R.id.dg_timeline_step_3_line);
        lineView.setBackgroundColor(ContextCompat.getColor(lineView.getContext(), R.color.blue_008ae1));
    }
}