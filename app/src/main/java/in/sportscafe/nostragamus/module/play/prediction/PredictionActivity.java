package in.sportscafe.nostragamus.module.play.prediction;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomTextView;

import in.sportscafe.nostragamus.Config.Sports;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.feed.FeedActivity;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.SwipeFlingAdapterView;

public class PredictionActivity extends NostragamusActivity implements PredictionView, View.OnClickListener {

    private RelativeLayout mRlPlayBg;

    private SwipeFlingAdapterView mSwipeFlingAdapterView;

    private TextView mTvNumberOfCards;

    private ImageView mIv2xPowerup;

    private ImageView mIvNonegsPowerup;

    private ImageView mIvPollPowerup;

    private TextView mTv2xPowerupCount;

    private TextView mTvNonegsPowerupCount;

    private TextView mTvPollPowerupCount;

    private PredictionPresenter mPredictionPresenter;

    private PredictionModel mPredictionModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        initToolbar();

        mRlPlayBg = (RelativeLayout) findViewById(R.id.content);
        mSwipeFlingAdapterView = (SwipeFlingAdapterView) findViewById(R.id.activity_prediction_swipe);

        mTvNumberOfCards = (TextView) findViewById(R.id.prediction_tv_number_of_cards);
        mIv2xPowerup = (ImageView) findViewById(R.id.powerups_iv_2x);
        mIvNonegsPowerup = (ImageView) findViewById(R.id.powerups_iv_nonegs);
        mIvPollPowerup = (ImageView) findViewById(R.id.powerups_iv_poll);
        mTv2xPowerupCount = (TextView) findViewById(R.id.powerup_tv_2x_count);
        mTvNonegsPowerupCount = (TextView) findViewById(R.id.powerup_tv_nonegs_count);
        mTvPollPowerupCount = (TextView) findViewById(R.id.powerup_tv_poll_count);

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
        findViewById(R.id.prediction_btn_shuffle).setVisibility(View.GONE);
    }

    @Override
    public void showNeither() {
        findViewById(R.id.prediction_btn_neither).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNeither() {
        findViewById(R.id.prediction_btn_neither).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prediction_ibtn_back:
                goBack();
                break;
            case R.id.prediction_btn_shuffle:
                mSwipeFlingAdapterView.getTopCardListener().selectBottom();
                break;
            case R.id.prediction_btn_neither:
                mSwipeFlingAdapterView.getTopCardListener().selectTop();
                break;
            case R.id.swipe_card_tv_left:
                mSwipeFlingAdapterView.getTopCardListener().selectLeft();
                break;
            case R.id.swipe_card_tv_right:
                mSwipeFlingAdapterView.getTopCardListener().selectRight();
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
    public void navigateToFeed(Bundle bundle) {
        Intent i = new Intent(getApplicationContext(), FeedActivity.class);
        i.putExtras(bundle);
        startActivity(i);
    }

    @Override
    public void setNeitherOption(String neitherOption) {
        TextView textView = (TextView) findViewById(R.id.prediction_btn_neither);
        textView.setText(neitherOption);

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
    public void changeBackgroundImage(String sportName) {
        int bgRes;
        switch (sportName.toLowerCase()) {
            case Sports.CRICKET:
                bgRes = R.drawable.play_cricket_bg;
                break;
            case Sports.BADMINTON:
                bgRes = R.drawable.play_badminton_bg;
                break;
            case Sports.FOOTBALL:
                bgRes = R.drawable.play_football_bg;
                break;
            case Sports.TENNIS:
                bgRes = R.drawable.play_tennis_bg;
                break;
            default:
                return;
        }

        mRlPlayBg.setBackgroundResource(bgRes);
    }

    @Override
    public void goBack() {
        onBackPressed();
    }

    private Drawable getPowerupDrawable(int colorRes) {
        GradientDrawable powerupDrawable = new GradientDrawable();
        powerupDrawable.setShape(GradientDrawable.RECTANGLE);
        powerupDrawable.setCornerRadius(getResources().getDimensionPixelSize(R.dimen.dp_5));
        powerupDrawable.setColor(getResources().getColor(colorRes));
        return powerupDrawable;
    }
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
