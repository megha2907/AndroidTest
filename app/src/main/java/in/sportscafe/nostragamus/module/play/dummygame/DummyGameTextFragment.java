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

    private DGInstruction mLastInstruction;

    private TextView mTvTopText;

    private TextView mTvCenterText;

    private TextView mTvBottomText;

    private Button mBtnAction;

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

        mBtnAction = (Button) findViewById(R.id.dummy_game_btn_action);
        mBtnAction.setOnClickListener(this);

        applyInstruction((DGInstruction) Parcels.unwrap(getArguments().getParcelable(BundleKeys.DUMMY_INSTRUCTION)));
    }

    public void applyInstruction(DGInstruction instruction) {
        mLastInstruction = instruction;

        switch (mLastInstruction.getAlignment()) {
            case Alignment.TOP:
                setTopText(mLastInstruction.getName());
                break;
            case Alignment.BOTTOM:
                setBottomText(mLastInstruction.getName());
                break;
            case Alignment.CENTER:
                findViewById(R.id.dummy_game_ll_center_holder).setVisibility(View.VISIBLE);
                setCenterText(mLastInstruction.getName());
                setActionText(mLastInstruction.getActionText());
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (R.id.dummy_game_btn_action == view.getId()) {
            mTextActionListener.onActionClicked(((Button) view).getText().toString());
        }
    }

    private void setTopText(String topText) {
        mTvTopText.setText(topText);
    }

    private void setCenterText(String centerText) {
        mTvCenterText.setText(centerText);
    }

    private void setBottomText(String bottomText) {
        mTvBottomText.setText(bottomText);
    }

    private void setActionText(String actionText) {
        mBtnAction.setText(actionText);
    }

    public interface OnDGTextActionListener {
        void onActionClicked(String action);
    }
}