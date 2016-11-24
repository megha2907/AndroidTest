package in.sportscafe.scgame.module.play;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.common.ScGameFragment;
import in.sportscafe.scgame.module.home.OnHomeActionListener;
import in.sportscafe.scgame.module.play.matchstatus.MatchStatusFragment;

/**
 * Created by Jeeva on 14/7/16.
 */
public class PlayActivity extends ScGameActivity implements PlayView, View.OnClickListener {

    public static final int PLAY_REQUEST_CODE = 23;

    private Button mBtnSkip;

    private OnHomeActionListener mHomeActionListener;

    private PlayPresenter mPlayPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_play);

        this.mBtnSkip = (Button) findViewById(R.id.play_btn_skip);
        mBtnSkip.setOnClickListener(this);

        findViewById(R.id.play_btn_back).setOnClickListener(this);

        this.mPlayPresenter = PlayPresenterImpl.newInstance(this);
        this.mPlayPresenter.onCreatePlay();
    }

    @Override
    public void showSkip() {
        mBtnSkip.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSkip() {
        mBtnSkip.setVisibility(View.GONE);
    }

//    @Override
//    public void navigateToAllDone() {
//        loadFragment(new AllDoneFragment());
//    }

//    @Override
//    public void navigateMatchStatus(Bundle bundle) {
//        MatchStatusFragment matchStatusFragment = new MatchStatusFragment();
//        matchStatusFragment.setArguments(bundle);
//        loadFragment(matchStatusFragment);
//    }
//
//    private void loadFragment(Fragment fragment) {
//        getChildFragmentManager().beginTransaction().replace(R.id.play_fl_container,
//                fragment).commitAllowingStateLoss();
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (PLAY_REQUEST_CODE == requestCode) {
            mPlayPresenter.onGetResultBack(resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_btn_skip:
                mPlayPresenter.onClickSkip();
                break;
            case R.id.play_btn_back:
                mHomeActionListener.onClickBack();
                break;
        }
    }
}