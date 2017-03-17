package in.sportscafe.nostragamus.module.play.dummygame;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.play.dummygame.DGAnimation.AnimationType;

/**
 * Created by Jeeva on 30/01/17.
 */

public class DGTextFragment extends NostragamusFragment implements View.OnClickListener {

    interface TextType {
        String TOP_TEXT = "topText";
        String BOTTOM_TEXT = "bottomText";
        String CENTER_TOP_TEXT = "centerTopText";
        String CENTER_BOTTOM_TEXT = "centerBottomText";
        String ACTION1 = "action1";
        String ACTION2 = "action2";
        String ACTION3 = "action3";
    }

    private TextView mTvTopText;

    private TextView mTvCenterTopText;

    private TextView mTvCenterBottomText;

    private TextView mTvBottomText;

    private Button mBtnAction1;

    private Button mBtnAction2;

    private Button mBtnAction3;

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

        mTvTopText = (TextView) findViewById(R.id.dummy_game_tv_top_text);
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
        String name = instruction.getName();
        if (null != instruction.getScoredPoints()) {
            name = String.format(name, instruction.getScoredPoints());
        }

        TextView textView = null;
        switch (instruction.getTextType()) {
            case TextType.TOP_TEXT:
                textView = mTvTopText;
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
                textView.setText(name);
            }
            textView.setVisibility(View.VISIBLE);

            animateView(textView, instruction.getAnimation());
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

    public void hideBottomText() {
        mTvBottomText.animate().alpha(0).setDuration(1000);
    }

    private void animateView(TextView textView, DGAnimation animation) {
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
}