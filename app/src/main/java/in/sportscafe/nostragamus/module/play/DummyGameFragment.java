package in.sportscafe.nostragamus.module.play;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.IntentActions;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;

import static in.sportscafe.nostragamus.R.id.view;

/**
 * Created by Jeeva on 30/01/17.
 */

public class DummyGameFragment extends NostragamusFragment implements View.OnClickListener {

    private TextView mTvTitle;

    private TextView mTvDesc;

    private Button mBtnAction;

    private boolean mPlayDone = false;

    public static DummyGameFragment newInstance() {
        return new DummyGameFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dummy_game, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTvTitle = (TextView) findViewById(R.id.dummy_game_tv_title);
        mTvDesc = (TextView) findViewById(R.id.dummy_game_tv_desc);
        mBtnAction = (Button) findViewById(R.id.dummy_game_btn_action);
        mBtnAction.setOnClickListener(this);

        changeContentsToPlay();
    }

    @Override
    public void onClick(View view) {
        if(R.id.dummy_game_btn_action == view.getId()) {
            if (mPlayDone) {
                broadcastProceed();
            } else {
                broadcastPlay();

                changeContentsToProceed();
                mPlayDone = true;
            }
        }
    }

    private void changeContentsToPlay() {
        mTvTitle.setText(R.string.dummy_start_title);
        mTvDesc.setText(R.string.dummy_start_desc);
        mBtnAction.setText(R.string.dummy_start_action);
    }

    private void changeContentsToProceed() {
        mTvTitle.setText(R.string.dummy_done_title);
        mTvDesc.setText(R.string.dummy_done_desc);
        mBtnAction.setText(R.string.dummy_done_action);
    }

    private void broadcastPlay() {
        LocalBroadcastManager.getInstance(getContext())
                .sendBroadcast(new Intent(IntentActions.ACTION_DUMMY_GAME_PLAY));
    }

    private void broadcastProceed() {
        LocalBroadcastManager.getInstance(getContext())
                .sendBroadcast(new Intent(IntentActions.ACTION_DUMMY_GAME_PROCEED));
    }
}