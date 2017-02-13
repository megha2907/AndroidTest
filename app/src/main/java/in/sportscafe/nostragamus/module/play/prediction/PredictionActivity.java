package in.sportscafe.nostragamus.module.play.prediction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.CustomProgressbar;
import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomTextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.coachmarker.TargetView;
import in.sportscafe.nostragamus.module.coachmarker.TourGuide;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.feed.FeedActivity;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.play.DummyGameFragment;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.SwipeFlingAdapterView;
import in.sportscafe.nostragamus.utils.ViewUtils;


public class PredictionActivity extends NostragamusActivity implements PredictionView, View.OnClickListener {

    private RelativeLayout mRlPlayBg;

    private ViewGroup mVgPlayPage;

    private SwipeFlingAdapterView mSwipeFlingAdapterView;

    private TextView mTvNumberOfCards;

    private ImageView mIv2xGlobalPowerup;

    private ImageView mIv2xPowerup;

    private ImageView mIvNonegsPowerup;

    private ImageView mIvPollPowerup;

    private TextView mTv2xGlobalPowerupCount;

    private TextView mTv2xPowerupCount;

    private TextView mTvNonegsPowerupCount;

    private TextView mTvPollPowerupCount;

    private TextView mTvNeitherOption;

    private PredictionPresenter mPredictionPresenter;

    private PredictionModel mPredictionModel;

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.PLAY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        initToolbar();

        mRlPlayBg = (RelativeLayout) findViewById(R.id.content);
        mVgPlayPage = (ViewGroup) findViewById(R.id.prediction_rl_play_page);
        mSwipeFlingAdapterView = (SwipeFlingAdapterView) findViewById(R.id.activity_prediction_swipe);

        mTvNumberOfCards = (TextView) findViewById(R.id.prediction_tv_number_of_cards);
        mIv2xGlobalPowerup = (ImageView) findViewById(R.id.powerups_iv_2x_global);
        mIv2xPowerup = (ImageView) findViewById(R.id.powerups_iv_2x);
        mIvNonegsPowerup = (ImageView) findViewById(R.id.powerups_iv_nonegs);
        mIvPollPowerup = (ImageView) findViewById(R.id.powerups_iv_poll);
        mTv2xGlobalPowerupCount = (TextView) findViewById(R.id.powerup_tv_2x_global_count);
        mTv2xPowerupCount = (TextView) findViewById(R.id.powerup_tv_2x_count);
        mTvNonegsPowerupCount = (TextView) findViewById(R.id.powerup_tv_nonegs_count);
        mTvPollPowerupCount = (TextView) findViewById(R.id.powerup_tv_poll_count);
        mTvNeitherOption = (TextView) findViewById(R.id.prediction_tv_neither_text);

        mIv2xGlobalPowerup.setBackground(getPowerupDrawable(R.color.goldenyellowcolor));
        mIv2xPowerup.setBackground(getPowerupDrawable(R.color.greencolor));
        mIvNonegsPowerup.setBackground(getPowerupDrawable(R.color.radical_red));
        mIvPollPowerup.setBackground(getPowerupDrawable(R.color.dodger_blue));

        this.mPredictionPresenter = PredictionPresenterImpl.newInstance(this);
        this.mPredictionPresenter.onCreatePrediction(getIntent().getExtras());
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.setPadding(0, 0, 0, 0);
        setSupportActionBar(toolbar);
    }

    @Override
    public void setTournamentName(String tournamentName) {
        ((TextView) findViewById(R.id.prediction_tv_tournament)).setText(tournamentName);
    }

    @Override
    public void setContestName(String contestName) {
        ((TextView) findViewById(R.id.prediction_tv_tournament_match_stage)).setText(contestName);
    }

    @Override
    public void setAdapter(PredictionAdapter predictionAdapter, SwipeFlingAdapterView.OnSwipeListener<Question> swipeListener) {
        predictionAdapter.setRootView(findViewById(R.id.content));

        mSwipeFlingAdapterView.setAdapter(predictionAdapter);
        mSwipeFlingAdapterView.setSwipeListener(swipeListener);

        predictionAdapter.notifyDataSetChanged();
    }

    private void invokeCardListener() {
        mSwipeFlingAdapterView.post(new Runnable() {
            @Override
            public void run() {
                mPredictionPresenter.setFlingListener(mSwipeFlingAdapterView.getTopCardListener());
            }
        });
    }

    @Override
    public void hideShuffle() {
        findViewById(R.id.prediction_iv_shuffle).setVisibility(View.INVISIBLE);
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
            case R.id.prediction_ibtn_back:
                mPredictionPresenter.onClickBack();
                break;
            case R.id.prediction_tv_skip:
                mPredictionPresenter.onClickSkip();
                break;
            case R.id.prediction_iv_shuffle:
                mSwipeFlingAdapterView.getTopCardListener().selectBottom();
                break;
            case R.id.prediction_ll_neither:
                mSwipeFlingAdapterView.getTopCardListener().selectTop();
                break;
            case R.id.prediction_iv_left_arrow:
                mSwipeFlingAdapterView.getTopCardListener().selectLeft();
                break;
            case R.id.prediction_iv_right_arrow:
                mSwipeFlingAdapterView.getTopCardListener().selectRight();
                break;
            case R.id.powerups_iv_2x_global:
                mPredictionPresenter.onClick2xGlobalPowerup();
                break;
            case R.id.powerups_iv_2x:
                mPredictionPresenter.onClick2xPowerup();
                break;
            case R.id.powerups_iv_nonegs:
                mPredictionPresenter.onClickNonegsPowerup();
                break;
            case R.id.powerups_iv_poll:
                mPredictionPresenter.onClickPollPowerup();
                break;
        }
    }

    @Override
    public void navigateToFeed() {
        Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

    @Override
    public void setNeitherOption(String neitherOption) {
        mTvNeitherOption.setText(neitherOption);

        invokeCardListener();
    }

    @Override
    public void setMatchStage(String matchStage) {
        ((CustomTextView) findViewById(R.id.prediction_tv_tournament_match_stage)).setText(matchStage);
    }

    @Override
    public void setTournamentPhoto(String tournamentPhoto) {
        ((HmImageView) findViewById(R.id.prediction_iv_tournament_photo)).setImageUrl(tournamentPhoto);
    }

    @Override
    public void setNumberofCards(String numberofCards) {
        mTvNumberOfCards.setText(numberofCards);
    }

    @Override
    public void set2xGlobalPowerupCount(int count) {
        mTv2xGlobalPowerupCount.setText(String.valueOf(count));
    }

    @Override
    public void set2xPowerupCount(int count) {
        mTv2xPowerupCount.setText(String.valueOf(count));
    }

    @Override
    public void setNonegsPowerupCount(int count) {
        mTvNonegsPowerupCount.setText(String.valueOf(count));
    }

    @Override
    public void setPollPowerupCount(int count) {
        mTvPollPowerupCount.setText(String.valueOf(count));
    }

    @Override
    public void notifyTopView() {
        mSwipeFlingAdapterView.refreshTopLayout();
    }

    @Override
    public void changeBackgroundImage(Integer sportId) {
        int bgRes;
        switch (sportId) {
            case 1:
                bgRes = R.drawable.play_cricket_bg;
                break;
            case 3:
                bgRes = R.drawable.play_tennis_bg;
                break;
            case 6:
                bgRes = R.drawable.play_badminton_bg;
                break;
            case 4:
                bgRes = R.drawable.play_football_bg;
                break;
            default:
                return;
        }

        mRlPlayBg.setBackgroundResource(bgRes);
    }

    @Override
    public void goBack() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        mPredictionPresenter.onClickBack();
    }

    private DummyGameFragment mDummyGameFragment;

    @Override
    public void changeToDummyGameMode() {
        findViewById(R.id.prediction_tv_skip).setVisibility(View.VISIBLE);
        findViewById(R.id.prediction_iv_tournament_photo).setVisibility(View.GONE);
        findViewById(R.id.prediction_ibtn_back).setVisibility(View.GONE);
        findViewById(R.id.prediction_iv_left_arrow).setVisibility(View.INVISIBLE);
        findViewById(R.id.prediction_iv_right_arrow).setVisibility(View.INVISIBLE);
        findViewById(R.id.prediction_iv_shuffle).setVisibility(View.INVISIBLE);

        mVgPlayPage.setVisibility(View.INVISIBLE);
        mIv2xGlobalPowerup.setVisibility(View.GONE);
        mTv2xGlobalPowerupCount.setVisibility(View.GONE);

        mDummyGameFragment = DummyGameFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.prediction_fl_dummy_holder, mDummyGameFragment).commit();
    }

    @Override
    public void showDummyGameInfo() {
        mVgPlayPage.setVisibility(View.INVISIBLE);
        findViewById(R.id.prediction_fl_dummy_holder).setVisibility(View.VISIBLE);
        findViewById(R.id.prediction_tv_skip).setVisibility(View.GONE);
    }

    @Override
    public void hideDummyGameInfo() {
        mVgPlayPage.setVisibility(View.VISIBLE);
        findViewById(R.id.prediction_fl_dummy_holder).setVisibility(View.INVISIBLE);
    }

    @Override
    public void enableLeftRightOptions() {
        setLeftRightOption(true, 1f);
    }

    @Override
    public void disableLeftRightOptions() {
        setLeftRightOption(false, 0.5f);
    }

    private void setLeftRightOption(boolean enabled, float alpha) {
        View arrow = findViewById(R.id.prediction_iv_left_arrow);
        arrow.setEnabled(enabled);
        arrow.setAlpha(alpha);

        arrow = findViewById(R.id.prediction_iv_right_arrow);
        arrow.setEnabled(enabled);
        arrow.setAlpha(alpha);
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
    public void showLeftRightIndicator() {
        findViewById(R.id.prediction_iv_dummy_left_right_indicator).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLeftRightIndicator() {
        findViewById(R.id.prediction_iv_dummy_left_right_indicator).setVisibility(View.GONE);
    }

    @Override
    public void showNeitherIndicator() {
        findViewById(R.id.prediction_iv_dummy_neither_indicator).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNeitherIndicator() {
        findViewById(R.id.prediction_iv_dummy_neither_indicator).setVisibility(View.GONE);
    }

    @Override
    public void showPowerupsHint() {
        findViewById(R.id.prediction_rl_powerup_hints).setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePowerupsHint() {
        findViewById(R.id.prediction_rl_powerup_hints).setVisibility(View.GONE);
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private TourGuide mCoachMarker;

    @Override
    public void showLeftRightCoach() {
        /*View leftArrow = findViewById(R.id.prediction_iv_left_arrow);
        View rightArrow = findViewById(R.id.prediction_iv_right_arrow);*/
        mCoachMarker = ViewUtils.showCoachMarker(
                this,
                mVgPlayPage,
                R.string.left_right_coach_title,
                R.string.left_right_coach_desc,
                false,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideLeftRightIndicator();
                    }
                }/*,
                new TargetView(leftArrow, leftArrow.getWidth(), leftArrow.getHeight()),
                new TargetView(rightArrow, rightArrow.getWidth(), rightArrow.getHeight())*/
        );
    }

    @Override
    public void showNeitherCoach() {
        View neitherOption = findViewById(R.id.prediction_ll_neither);
        mCoachMarker = ViewUtils.showCoachMarker(
                this,
                mVgPlayPage,
                R.string.neither_coach_title,
                R.string.neither_coach_desc,
                false,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideNeitherIndicator();
                    }
                },
                new TargetView(neitherOption, neitherOption.getWidth(), neitherOption.getHeight())
        );
    }

    @Override
    public void showPowerupsCoach() {
        mCoachMarker = ViewUtils.showCoachMarker(
                this,
                mVgPlayPage,
                R.string.powerups_coach_title,
                R.string.powerups_coach_desc,
                true,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideLeftRightIndicator();
                    }
                },
                new TargetView(mIv2xPowerup, mIv2xPowerup.getWidth(), mIv2xPowerup.getHeight()),
                new TargetView(mIvNonegsPowerup, mIvNonegsPowerup.getWidth(), mIvNonegsPowerup.getHeight()),
                new TargetView(mIvPollPowerup, mIvPollPowerup.getWidth(), mIvPollPowerup.getHeight())
        );
    }

    @Override
    public boolean dismissCoach() {
        if (null != mCoachMarker) {
            return mCoachMarker.dismiss();
        }
        return false;
    }

    private Drawable getPowerupDrawable(int colorRes) {
        GradientDrawable powerupDrawable = new GradientDrawable();
        powerupDrawable.setShape(GradientDrawable.RECTANGLE);
        powerupDrawable.setCornerRadius(getResources().getDimensionPixelSize(R.dimen.dp_5));
        powerupDrawable.setColor(getResources().getColor(colorRes));
        return powerupDrawable;
    }

    @Override
    public void showProgressbar() {
        CustomProgressbar.getProgressbar(this).show();
    }

    @Override
    public boolean dismissProgressbar() {
        return CustomProgressbar.getProgressbar(this).dismissProgress();
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mDummyGameStartReceiver,
                new IntentFilter(Constants.IntentActions.ACTION_DUMMY_GAME_PLAY));
        LocalBroadcastManager.getInstance(this).registerReceiver(mDummyGameEndReceiver,
                new IntentFilter(Constants.IntentActions.ACTION_DUMMY_GAME_PROCEED));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mDummyGameStartReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mDummyGameEndReceiver);
    }

    BroadcastReceiver mDummyGameStartReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            hideDummyGameInfo();
            mPredictionPresenter.onDummyGameStart();
        }
    };

    BroadcastReceiver mDummyGameEndReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPredictionPresenter.onDummyGameEnd();
        }
    };



}



    /*

    , View.OnTouchListener, View.OnDragListener

    @Override
    public boolean onTouch(View v, MotionEvent arg1) {
        if (!m2xpowerUpApplied) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadow = new View.DragShadowBuilder(v);
            v.startDrag(data, shadow, null, 0);
        }
        return false;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        final int action = dragEvent.getAction();
        switch (action) {

            case DragEvent.ACTION_DRAG_STARTED:
                break;

            case DragEvent.ACTION_DRAG_EXITED:
                break;

            case DragEvent.ACTION_DRAG_ENTERED:
                break;

            case DragEvent.ACTION_DROP:
                break;

            case DragEvent.ACTION_DRAG_ENDED:
                if (!m2xpowerUpApplied) {
                    if (NostragamusDataHandler.getInstance().getNumberof2xPowerups() > 0) {
                        m2xpowerUpApplied = true;
                        mPredictionPresenter.onPowerUp("2x");
                        NostragamusDataHandler.getInstance().setNumberof2xPowerups(NostragamusDataHandler.getInstance().getNumberof2xPowerups() - 1);
                        String UpdatedPowerUps = String.valueOf(NostragamusDataHandler.getInstance().getNumberof2xPowerups());
                        mTv2xPowerupCount.setText(UpdatedPowerUps);
                    }
                }
                break;
        }
        return true;
    }*/
