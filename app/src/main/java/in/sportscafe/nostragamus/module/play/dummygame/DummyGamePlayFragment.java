package in.sportscafe.nostragamus.module.play.dummygame;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.play.prediction.PredictionAdapter;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.SwipeFlingAdapterView;


public class DummyGamePlayFragment extends NostragamusFragment implements DummyGamePlayView, View.OnClickListener {

    private RelativeLayout mRlPlayBg;

    private ViewGroup mVgPlayPage;

    private SwipeFlingAdapterView mSwipeFlingAdapterView;

    private ImageView mIv2xPowerup;

    private ImageView mIvNonegsPowerup;

    private ImageView mIvPollPowerup;

    private TextView mTv2xPowerupCount;

    private TextView mTvNonegsPowerupCount;

    private TextView mTvPollPowerupCount;

    private TextView mTvNeitherOption;

    private DummyGamePlayPresenter mDummyGamePlayPresenter;

    private OnDGPlayActionListener mPlayActionListener;

    public static DummyGamePlayFragment newInstance(Question dummyQuestion) {
        Bundle bundle = new Bundle();
        if(null != dummyQuestion) {
            bundle.putParcelable(BundleKeys.DUMMY_QUESTION, Parcels.wrap(dummyQuestion));
        }

        DummyGamePlayFragment fragment = new DummyGamePlayFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnDGPlayActionListener) {
            this.mPlayActionListener = (OnDGPlayActionListener) context;
        } else throw new IllegalArgumentException(context.getClass().getSimpleName() + " should implement the OnDGPlayActionListener");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dummy_game_play, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();

        this.mDummyGamePlayPresenter = DummyGamePlayPresenterImpl.newInstance(this);
        this.mDummyGamePlayPresenter.onCreatePrediction(getArguments());
    }

    private void initViews() {
        mRlPlayBg = (RelativeLayout) findViewById(R.id.content);
        mVgPlayPage = (ViewGroup) findViewById(R.id.prediction_rl_play_page);
        mSwipeFlingAdapterView = (SwipeFlingAdapterView) findViewById(R.id.activity_prediction_swipe);

        mIv2xPowerup = (ImageView) findViewById(R.id.powerups_iv_2x);
        mIvNonegsPowerup = (ImageView) findViewById(R.id.powerups_iv_nonegs);
        mIvPollPowerup = (ImageView) findViewById(R.id.powerups_iv_poll);
        mTv2xPowerupCount = (TextView) findViewById(R.id.powerup_tv_2x_count);
        mTvNonegsPowerupCount = (TextView) findViewById(R.id.powerup_tv_nonegs_count);
        mTvPollPowerupCount = (TextView) findViewById(R.id.powerup_tv_poll_count);
        mTvNeitherOption = (TextView) findViewById(R.id.prediction_tv_neither_text);

        mIv2xPowerup.setBackground(getPowerupDrawable(R.color.dodger_blue));
        mIvNonegsPowerup.setBackground(getPowerupDrawable(R.color.amaranth));
        mIvPollPowerup.setBackground(getPowerupDrawable(R.color.greencolor));

        mIv2xPowerup.setOnClickListener(this);
        mIvNonegsPowerup.setOnClickListener(this);
        mIvPollPowerup.setOnClickListener(this);
        findViewById(R.id.prediction_ll_neither).setOnClickListener(this);
    }

    @Override
    public void setAdapter(PredictionAdapter predictionAdapter, SwipeFlingAdapterView.OnSwipeListener<Question> swipeListener) {
        predictionAdapter.setRootView(findViewById(R.id.content));

        mSwipeFlingAdapterView.setAdapter(predictionAdapter, R.id.swipe_card_cv_main);
        mSwipeFlingAdapterView.setSwipeListener(swipeListener);

        predictionAdapter.notifyDataSetChanged();
    }

    private void invokeCardListener() {
        mSwipeFlingAdapterView.post(new Runnable() {
            @Override
            public void run() {
                mDummyGamePlayPresenter.setFlingListener(mSwipeFlingAdapterView.getTopCardListener());
            }
        });
    }

    @Override
    public void showNeither() {
        findViewById(R.id.prediction_ll_neither).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNeither() {
        findViewById(R.id.prediction_ll_neither).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prediction_ll_neither:
                mSwipeFlingAdapterView.getTopCardListener().selectTop();
                break;
            case R.id.prediction_iv_left_arrow:
                mSwipeFlingAdapterView.getTopCardListener().selectLeft();
                break;
            case R.id.prediction_iv_right_arrow:
                mSwipeFlingAdapterView.getTopCardListener().selectRight();
                break;
            case R.id.powerups_iv_2x:
                mDummyGamePlayPresenter.onClick2xPowerup();
                break;
            case R.id.powerups_iv_nonegs:
                mDummyGamePlayPresenter.onClickNonegsPowerup();
                break;
            case R.id.powerups_iv_poll:
                mDummyGamePlayPresenter.onClickPollPowerup();
                break;
        }
    }

    @Override
    public void setNeitherOption(String neitherOption) {
        mTvNeitherOption.setText(neitherOption);

        invokeCardListener();
    }

    @Override
    public void set2xPowerupCount(int count, boolean reverse) {
        if(!reverse) {
            mPlayActionListener.on2xApplied();
        }
        applyAlphaForPowerUp(mIv2xPowerup, mTv2xPowerupCount, reverse, count);
    }

    @Override
    public void setNonegsPowerupCount(int count, boolean reverse) {
        if(!reverse) {
            mPlayActionListener.onNonegsApplied();
        }
        applyAlphaForPowerUp(mIvNonegsPowerup, mTvNonegsPowerupCount, reverse, count);
    }

    @Override
    public void setPollPowerupCount(int count, boolean reverse) {
        if(!reverse) {
            mPlayActionListener.onPollApplied();
        }
        applyAlphaForPowerUp(mIvPollPowerup, mTvPollPowerupCount, reverse, count);
    }

    @Override
    public void notifyTopView() {
        mSwipeFlingAdapterView.refreshTopLayout();
        invokeCardListener();
    }

    @Override
    public void showPowerups() {
        findViewById(R.id.prediction_ll_powerup_layout).setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePowerups() {
        findViewById(R.id.prediction_ll_powerup_layout).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPlayDone(Integer scoredPoints) {
        mPlayActionListener.onPlayed(scoredPoints);
    }

    private Drawable getPowerupDrawable(int colorRes) {
        GradientDrawable powerupDrawable = new GradientDrawable();
        powerupDrawable.setShape(GradientDrawable.OVAL);
        powerupDrawable.setColor(getResources().getColor(colorRes));
        return powerupDrawable;
    }

    private void applyAlphaForPowerUp(View powerUpIcon, TextView powerUpText, boolean reverse, int count) {
        float alpha = 0.6f;
        if (reverse) {
            alpha = 1f;
        }

        powerUpIcon.setAlpha(alpha);
        powerUpText.setAlpha(alpha);

        if (count == 0) {
            powerUpText.setText("+");
            powerUpText.setBackgroundResource(R.drawable.powerup_count_add_bg);
        } else {
            powerUpText.setText(String.valueOf(count));
            powerUpText.setBackgroundResource(R.drawable.powerup_count_bg);
        }
    }

    public void addQuestion(Question question) {
        mDummyGamePlayPresenter.onGetQuestions(question);
    }

    public interface OnDGPlayActionListener {

        void onPlayed(Integer scoredPoints);

        void on2xApplied();

        void onNonegsApplied();

        void onPollApplied();
    }
}