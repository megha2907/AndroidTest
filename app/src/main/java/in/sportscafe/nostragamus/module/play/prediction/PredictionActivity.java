package in.sportscafe.nostragamus.module.play.prediction;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomTextView;

import in.sportscafe.nostragamus.Config.Sports;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.feed.FeedActivity;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.SwipeFlingAdapterView;

public class PredictionActivity extends NostragamusActivity implements PredictionView, View.OnClickListener, View.OnTouchListener, View.OnDragListener {

    private RelativeLayout mRlPlayBg;

    private boolean m2xpowerUpApplied = false;

    private boolean mAudiencePollpowerUpApplied = false;

    private boolean mNonegspowerUpApplied = false;

    private ImageView iv2xPowerup;

    private ImageView ivNonegsPowerup;

    private ImageView ivPollPowerup;

    private TextView tv2xPowerupCount;

    private TextView tvNonegsPowerupCount;

    private TextView tvPollPowerupCount;

    private SwipeFlingAdapterView mSwipeFlingAdapterView;

    private PredictionPresenter mPredictionPresenter;

    private PredictionModel mPredictionModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        mSwipeFlingAdapterView = (SwipeFlingAdapterView) findViewById(R.id.activity_prediction_swipe);
        mSwipeFlingAdapterView.setScrollListener(new SwipeFlingAdapterView.OnScrollListener() {
            @Override
            public void onScroll(float scrollProgressPercent) {
                /*View view = mSwipeFlingAdapterView.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);*/
            }
        });

        //TOOLBAR
        Toolbar mtoolbar = (Toolbar) findViewById(R.id.play_toolbar);
        mtoolbar.setContentInsetsAbsolute(0, 0);
        mtoolbar.setPadding(0, 0, 0, 0);
        setSupportActionBar(mtoolbar);

        mRlPlayBg = (RelativeLayout) findViewById(R.id.content);

        iv2xPowerup = (ImageView) findViewById(R.id.powerups_iv_2x);
        ivNonegsPowerup = (ImageView) findViewById(R.id.powerups_iv_nonegs);
        ivPollPowerup = (ImageView) findViewById(R.id.powerups_iv_poll);

        iv2xPowerup.setBackground(getPowerupDrawable(R.color.greencolor));
        ivNonegsPowerup.setBackground(getPowerupDrawable(R.color.radical_red));
        ivPollPowerup.setBackground(getPowerupDrawable(R.color.dodger_blue));

        tv2xPowerupCount = (TextView) findViewById(R.id.powerup_tv_2x_count);
        tvNonegsPowerupCount = (TextView) findViewById(R.id.powerup_tv_nonegs_count);
        tvPollPowerupCount = (TextView) findViewById(R.id.powerup_tv_poll_count);

        this.mPredictionPresenter = PredictionPresenterImpl.newInstance(this);
        this.mPredictionPresenter.onCreatePrediction(getIntent().getExtras());
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
    public void setRightArrowAnimation() {
        HmImageView rightArrow = (HmImageView) findViewById(R.id.swipe_card_iv_right);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        rightArrow.startAnimation(myFadeInAnimation);
    }


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
                        tv2xPowerupCount.setText(UpdatedPowerUps);
                    }
                }
                break;
        }
        return true;
    }


    @Override
    public void setAdapter(PredictionAdapter predictionAdapter,
                           SwipeFlingAdapterView.OnSwipeListener<Question> swipeListener) {
        mSwipeFlingAdapterView.setAdapter(predictionAdapter);
        mSwipeFlingAdapterView.setSwipeListener(swipeListener);

        invokeCardListener();
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
    public void showNoNegativeAlert() {
        showAlertMessage("No negatives, So make a guess!");
    }

    @Override
    public void showLastQuestionAlert() {
        showAlertMessage("No use to pass the last question!");
    }

    private void showAlertMessage(String message) {
        /*TextView textView = (TextView) findViewById(R.id.prediction_tv_message);
        textView.setText(message);
        textView.setVisibility(View.VISIBLE);*/
    }

    @Override
    public void navigateToResult(Bundle bundle) {
        navigateToAllDone(bundle);
    }

    @Override
    public void swipeCardToTop() {
        mSwipeFlingAdapterView.getTopCardListener().selectTop();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prediction_ibtn_back:
                onBackPressed();
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
                if (!m2xpowerUpApplied || !mAudiencePollpowerUpApplied || !mNonegspowerUpApplied) {
                    if (NostragamusDataHandler.getInstance().getNumberof2xPowerups() > 0) {

                        m2xpowerUpApplied = true;
                        mAudiencePollpowerUpApplied = true;
                        mNonegspowerUpApplied = true;
                        mPredictionPresenter.onPowerUp("2x");

                        String UpdatedPowerUps = String.valueOf(NostragamusDataHandler.getInstance().getNumberof2xPowerups() - 1);
                        tv2xPowerupCount.setText(UpdatedPowerUps);
                    }
                }
                break;
            case R.id.powerups_iv_nonegs:
                if (!mNonegspowerUpApplied || !m2xpowerUpApplied || !mAudiencePollpowerUpApplied) {
                    if (NostragamusDataHandler.getInstance().getNumberofNonegsPowerups() > 0) {

                        m2xpowerUpApplied = true;
                        mAudiencePollpowerUpApplied = true;
                        mNonegspowerUpApplied = true;
                        mPredictionPresenter.onPowerUp("no_negs");

                        String UpdatedPowerUps = String.valueOf(NostragamusDataHandler.getInstance().getNumberofNonegsPowerups() - 1);
                        tvNonegsPowerupCount.setText(UpdatedPowerUps);
                    }
                }
                break;
            case R.id.powerups_iv_poll:
                if (!mAudiencePollpowerUpApplied || !m2xpowerUpApplied || !mNonegspowerUpApplied) {
                    if (NostragamusDataHandler.getInstance().getNumberofAudiencePollPowerups() > 0) {
                        showProgressbar();

                        m2xpowerUpApplied = true;
                        mAudiencePollpowerUpApplied = true;
                        mNonegspowerUpApplied = true;
                        mPredictionPresenter.onPowerUp("player_poll");

                        String UpdatedPowerUps = String.valueOf(NostragamusDataHandler.getInstance().getNumberofAudiencePollPowerups() - 1);
                        tvPollPowerupCount.setText(UpdatedPowerUps);
                    }
                }
                break;
        }
    }


    @Override
    public void dismissPowerUp() {
        m2xpowerUpApplied = false;
        mAudiencePollpowerUpApplied = false;
        mNonegspowerUpApplied = false;
    }

    @Override
    public void updateAudiencePollPowerup() {

        NostragamusDataHandler.getInstance().setNumberofAudiencePollPowerups(NostragamusDataHandler.getInstance().getNumberofAudiencePollPowerups() + 1);
        String UpdatedPowerUps = String.valueOf(NostragamusDataHandler.getInstance().getNumberofAudiencePollPowerups());
        tvPollPowerupCount.setText(UpdatedPowerUps);
    }


    @Override
    public void navigateToAllDone(Bundle bundle) {
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

        ((HmImageView) findViewById(R.id.prediction_iv_tournament_photo)).setImageUrl(tournamentPhoto
        );

    }

    @Override
    public void setNumberofCards(int itemsInAdapter, int initialCount) {

        if (itemsInAdapter == 1) {
            ((CustomTextView) findViewById(R.id.prediction_tv_number_of_cards)).setText(String.valueOf(itemsInAdapter) + "/" + String.valueOf(initialCount));
        } else {
            ((CustomTextView) findViewById(R.id.prediction_tv_number_of_cards)).setText(String.valueOf(itemsInAdapter) + "/" + String.valueOf(initialCount));
        }


    }

    @Override
    public void setNumberofPowerups(int numberof2xPowerups, int numberofAudiencePollPowerups, int numberofNonegsPowerups) {
        m2xpowerUpApplied = numberof2xPowerups < 1;
        mAudiencePollpowerUpApplied = numberofAudiencePollPowerups < 1;
        mNonegspowerUpApplied = numberofNonegsPowerups < 1;

        tv2xPowerupCount.setText(String.valueOf(numberof2xPowerups));
        tvPollPowerupCount.setText(String.valueOf(numberofAudiencePollPowerups));
        tvNonegsPowerupCount.setText(String.valueOf(numberofNonegsPowerups));
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

    @Override
    public View getRootView() {
        return findViewById(R.id.content);
    }

    private Drawable getPowerupDrawable(int colorRes) {
        GradientDrawable powerupDrawable = new GradientDrawable();
        powerupDrawable.setShape(GradientDrawable.RECTANGLE);
        powerupDrawable.setCornerRadius(getResources().getDimensionPixelSize(R.dimen.dp_5));
        powerupDrawable.setColor(getResources().getColor(colorRes));
        return powerupDrawable;
    }
}