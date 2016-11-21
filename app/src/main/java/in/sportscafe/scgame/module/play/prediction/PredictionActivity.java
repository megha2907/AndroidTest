package in.sportscafe.scgame.module.play.prediction;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.volley.Volley;
import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;
import com.jeeva.android.widgets.customfont.CustomTextView;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.feed.FeedActivity;
import in.sportscafe.scgame.module.play.prediction.dto.Question;
import in.sportscafe.scgame.module.play.tindercard.SwipeFlingAdapterView;

public class PredictionActivity extends ScGameActivity implements PredictionView, View.OnClickListener, View.OnTouchListener, View.OnDragListener {

    private SwipeFlingAdapterView mSwipeFlingAdapterView;

    private PredictionPresenter mPredictionPresenter;
    private boolean mpowerUpApplied = false;

    private PredictionModel mPredictionModel;

    CustomButton btnpowerUpCount;
    RelativeLayout rlPowerUp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        mSwipeFlingAdapterView = (SwipeFlingAdapterView) findViewById(R.id.activity_prediction_swipe);
        mSwipeFlingAdapterView.setScrollListener(new SwipeFlingAdapterView.OnScrollListener() {
            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = mSwipeFlingAdapterView.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);
            }
        });




        //rlPowerUp = (RelativeLayout) findViewById(R.id.rl_powerup);
        CustomButton btnquestionValue = (CustomButton) findViewById(R.id.swipe_card_question_value);
        btnpowerUpCount = (CustomButton) findViewById(R.id.swipe_card_tv_powerup_count);
        Toolbar mtoolbar=(Toolbar)findViewById(R.id.play_toolbar);
        mtoolbar.setContentInsetsAbsolute(0,0);
        mtoolbar.setPadding(0,0,0,0);
        setSupportActionBar(mtoolbar);

        btnquestionValue.setOnTouchListener(this);
        mSwipeFlingAdapterView.setOnDragListener(this);

        int powerUps = ScGameDataHandler.getInstance().getNumberofPowerups();
        btnpowerUpCount.setText(String.valueOf(powerUps));

        if (powerUps < 1) {
            mpowerUpApplied = true;
        }

       final ImageView rightArrow =  (ImageView) findViewById(R.id.swipe_card_iv_right_arrow);
        final ImageView rightArrow2 =  (ImageView) findViewById(R.id.swipe_card_iv_right_arrow2);
        final ImageView rightArrow3 =  (ImageView) findViewById(R.id.swipe_card_iv_right_arrow3);


        final Animation arrow1Animation = createArrowAnimation(1000,1,0);
        arrow1Animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.i("PredictionActivity", "First anim starts");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.i("PredictionActivity", "First anim repeats");
                rightArrow.setVisibility(View.GONE);
                /*arrow1Animation.setDuration(2000);
                arrow1Animation.setStartOffset(1000);*/

            }
        });
        final Animation arrow2Animation = createArrowAnimation(1000,1,0);
        arrow2Animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                rightArrow2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                rightArrow2.setVisibility(View.GONE);

            }
        });
        final Animation arrow3Animation = createArrowAnimation(1000,1,0);
        arrow3Animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                rightArrow3.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                rightArrow3.setVisibility(View.GONE);

            }
        });

        rightArrow.startAnimation(arrow1Animation);

        rightArrow2.postDelayed(new Runnable() {
            @Override
            public void run() {
                rightArrow2.startAnimation(arrow2Animation);
            }
        }, 500);

        rightArrow3.postDelayed(new Runnable() {
            @Override
            public void run() {
                rightArrow3.startAnimation(arrow3Animation);
            }
        }, 1000);

        this.mPredictionPresenter = PredictionPresenterImpl.newInstance(this);
        this.mPredictionPresenter.onCreatePrediction(getIntent().getExtras());

    }

    private Animation createArrowAnimation(int duration, float fromAlpha, float toAlpha){

//        AnimationSet animationSet = new AnimationSet(true);

        /*TranslateAnimation translateAnimation=new TranslateAnimation(fromXDelta,toXDelta,fromYDelta,toYDelta);
        translateAnimation.setDuration(duration);
        translateAnimation.setStartOffset(startoffset);
        translateAnimation.setRepeatCount(Animation.INFINITE);
        animationSet.addAnimation(translateAnimation);*/

        AlphaAnimation alphaAnimation=new AlphaAnimation(fromAlpha,toAlpha);
        alphaAnimation.setDuration(duration);
//        alphaAnimation.setRepeatCount(Animation.INFINITE);
//        animationSet.addAnimation(alphaAnimation);

        return alphaAnimation;
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
        HmImageView rightArrow =  (HmImageView) findViewById(R.id.swipe_card_iv_right);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        rightArrow.startAnimation(myFadeInAnimation);
    }


    @Override
    public boolean onTouch(View v, MotionEvent arg1) {
        if(!mpowerUpApplied) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadow = new View.DragShadowBuilder(v);
            v.startDrag(data, shadow, null, 0);
        }
        return false;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        final int action = dragEvent.getAction();
        switch(action) {

            case DragEvent.ACTION_DRAG_STARTED:
                break;

            case DragEvent.ACTION_DRAG_EXITED:
                break;

            case DragEvent.ACTION_DRAG_ENTERED:
                break;

            case DragEvent.ACTION_DROP:
                break;

            case DragEvent.ACTION_DRAG_ENDED:
                if(!mpowerUpApplied) {
                    if (ScGameDataHandler.getInstance().getNumberofPowerups() > 0) {
                        mpowerUpApplied = true;
                        mPredictionPresenter.onPowerUp();
                        ScGameDataHandler.getInstance().setNumberofPowerups(ScGameDataHandler.getInstance().getNumberofPowerups() - 1);
                        String UpdatedPowerUps = String.valueOf(ScGameDataHandler.getInstance().getNumberofPowerups());
                        btnpowerUpCount.setText(UpdatedPowerUps);
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
    public void hidePass() {
        findViewById(R.id.prediction_btn_pass).setVisibility(View.INVISIBLE);
        findViewById(R.id.downnarrow).setVisibility(View.INVISIBLE);
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
        TextView textView = (TextView) findViewById(R.id.prediction_tv_message);
        textView.setText(message);
        textView.setVisibility(View.VISIBLE);
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
            case R.id.prediction_btn_pass:
                mSwipeFlingAdapterView.getTopCardListener().selectBottom();
                break;
            case R.id.swipe_card_tv_left:
                mSwipeFlingAdapterView.getTopCardListener().selectLeft();
                break;
            case R.id.swipe_card_tv_right:
                mSwipeFlingAdapterView.getTopCardListener().selectRight();
                break;
        }
    }



//    @Override
//    public void onTimeUp() {
//
//    }

    @Override
    public void dismissPowerUp() {
        mpowerUpApplied=false;
    }

    @Override
    public void navigateToAllDone(Bundle bundle) {
        Intent i = new Intent(getApplicationContext(), FeedActivity.class);
        i.putExtras(bundle);
        startActivity(i);
    }

    @Override
    public void setLeftOption(String questionOption1) {
        TextView swipeleftv = (TextView) findViewById(R.id.swipe_card_tv_left);
        swipeleftv.setText(questionOption1);
        invokeCardListener();

    }

    @Override
    public void setRightOption(String questionOption2) {
        TextView swiperighttv = (TextView) findViewById(R.id.swipe_card_tv_right);
        swiperighttv.setText(questionOption2);
        //setRightArrowAnimation();
    }

    @Override
    public void setMatchStage(String matchStage) {

        ((CustomTextView) findViewById(R.id.prediction_tv_tournament_match_stage)).setText(matchStage);
    }

    @Override
    public void setTournamentPhoto(String tournamentPhoto) {

        ((HmImageView) findViewById(R.id.prediction_iv_tournament_photo)).setImageUrl(tournamentPhoto,
                Volley.getInstance().getImageLoader(), false);

    }

    @Override
    public void setNumberofCards(int itemsInAdapter, int initialCount) {

        if (itemsInAdapter==1){
            ((CustomTextView) findViewById(R.id.prediction_tv_number_of_cards)).setText(String.valueOf(itemsInAdapter) + "/" + String.valueOf(initialCount));
        }
        else {
            ((CustomTextView) findViewById(R.id.prediction_tv_number_of_cards)).setText(String.valueOf(itemsInAdapter) + "/" + String.valueOf(initialCount));
        }


    }

}