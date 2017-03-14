package in.sportscafe.nostragamus.module.play.dummygame;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;

/**
 * Created by Jeeva on 30/01/17.
 */

public class DummyGameTextFragment extends NostragamusFragment implements View.OnClickListener {

    interface Alignment {
        String TOP = "top";
        String BOTTOM = "bottom";
        String CENTER = "center";
    }

    private TextView mTvTopText;

    private TextView mTvCenterText;

    private TextView mTvBottomText;

    private Button mBtnAction1;

    private Button mBtnAction2;

    private OnDGTextActionListener mTextActionListener;

    public static DummyGameTextFragment newInstance(DGInstruction instruction) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKeys.DUMMY_INSTRUCTION, Parcels.wrap(instruction));

        DummyGameTextFragment fragment = new DummyGameTextFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnDGTextActionListener) {
            this.mTextActionListener = (OnDGTextActionListener) context;
        } else throw new IllegalArgumentException(context.getClass().getSimpleName() + " should implement the OnDGTextActionListener");
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
        mTvCenterText = (TextView) findViewById(R.id.dummy_game_tv_center_text);
        mTvBottomText = (TextView) findViewById(R.id.dummy_game_tv_bottom_text);

        mBtnAction1 = (Button) findViewById(R.id.dummy_game_btn_action1);
        mBtnAction1.setOnClickListener(this);

        mBtnAction2 = (Button) findViewById(R.id.dummy_game_btn_action2);
        mBtnAction2.setOnClickListener(this);

        applyInstruction((DGInstruction) Parcels.unwrap(getArguments().getParcelable(BundleKeys.DUMMY_INSTRUCTION)));
    }

    public void applyInstruction(DGInstruction instruction) {
        String name = instruction.getName();
        if(null != instruction.getScoredPoints()) {
            name = String.format(name, instruction.getScoredPoints());
        }

        switch (instruction.getAlignment()) {
            case Alignment.TOP:
                setTopText(name);
                break;
            case Alignment.BOTTOM:
                setBottomText(name);
                break;
            case Alignment.CENTER:
                setCenterText(name);

                if(null != instruction.getAction1Text()) {
                    setAction1Text(instruction.getAction1Text());
                }

                if(null != instruction.getAction2Text()) {
                    setAction2Text(instruction.getAction2Text());
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dummy_game_btn_action1:
            case R.id.dummy_game_btn_action2:
                mTextActionListener.onActionClicked(((Button) view).getText().toString());
                break;
        }
    }

    private void setTopText(String topText) {
        applyFadeInAnimation(mTvTopText);
        mTvTopText.setText(topText);
    }

    private void setCenterText(String centerText) {
        applyFadeInAnimation(mTvCenterText);
        mTvCenterText.setText(centerText);
    }

    private void setBottomText(String bottomText) {
        applyFadeInAnimation(mTvBottomText);
        mTvBottomText.setText(bottomText);
    }

    private void setAction1Text(String actionText) {
        mBtnAction1.setVisibility(View.VISIBLE);
        applyFadeInAnimation(mBtnAction1);
        mBtnAction1.setText(actionText);
    }

    private void setAction2Text(String actionText) {
        mBtnAction2.setVisibility(View.VISIBLE);
        applyFadeInAnimation(mBtnAction2);
        mBtnAction2.setText(actionText);
    }

    private void applyFadeInAnimation(View view) {
        view.setAlpha(0f);
        view.animate().alpha(1f).setDuration(3000);
    }

    public interface OnDGTextActionListener {
        void onActionClicked(String actionText);
    }
}