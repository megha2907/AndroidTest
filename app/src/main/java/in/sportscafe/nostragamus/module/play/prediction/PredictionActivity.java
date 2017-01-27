package in.sportscafe.nostragamus.module.play.prediction;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;
import com.jeeva.android.widgets.customfont.CustomTextView;

import in.sportscafe.nostragamus.Config.Sports;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.feed.FeedActivity;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.SwipeFlingAdapterView;

public class PredictionActivity extends NostragamusActivity implements PredictionView, View.OnClickListener, View.OnTouchListener, View.OnDragListener {

    private SwipeFlingAdapterView mSwipeFlingAdapterView;
    private PredictionPresenter mPredictionPresenter;

    private boolean m2xpowerUpApplied = false;
    private boolean mAudiencePollpowerUpApplied = false;
    private boolean mNonegspowerUpApplied = false;

    CustomButton btn2xpowerUpCount;
    CustomButton btnAudiencePollCount;
    CustomButton btnNonegsCount;

    private PredictionModel mPredictionModel;
    RelativeLayout rlPowerUp;
    private Boolean isFabOpen = false;
    private ImageButton powerupMainFab;
    private View powerup2xFab,powerupAudiencePollFab,powerupNonegsFab, fabContainer;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private RelativeLayout mplayBg;

    private float offset1;
    private float offset2;
    private float offset3;

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

                Log.d("scrollProgressPercent --> ", scrollProgressPercent + "");
            }
        });


        //TOOLBAR
        Toolbar mtoolbar=(Toolbar)findViewById(R.id.play_toolbar);
        mtoolbar.setContentInsetsAbsolute(0,0);
        mtoolbar.setPadding(0,0,0,0);
        setSupportActionBar(mtoolbar);


        //POWERUPFAB ICONS
        btn2xpowerUpCount=(CustomButton)findViewById(R.id.powerup_2x_count);
        btnAudiencePollCount=(CustomButton)findViewById(R.id.powerup_audience_poll_count);
        btnNonegsCount=(CustomButton)findViewById(R.id.powerup_nonegs_count);
        powerupMainFab = (ImageButton) findViewById(R.id.fab_main);
        fabContainer = findViewById(R.id.fab_container);
        powerup2xFab = findViewById(R.id.fab_fl_2x);
        powerupAudiencePollFab = findViewById(R.id.fab_fl_audience_poll);
        powerupNonegsFab = findViewById(R.id.fab_fl_nonegs);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.powerup_fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.powerup_fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.powerup_fab_rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.powerup_fab_rotate_backward);
        powerupMainFab.setOnClickListener(this);
        powerup2xFab.setOnClickListener(this);
        powerupAudiencePollFab.setOnClickListener(this);
        powerupNonegsFab.setOnClickListener(this);

        mplayBg=(RelativeLayout) findViewById(R.id.content);

        fabContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                fabContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                float mainY = powerupMainFab.getY();

                offset1 = mainY - powerup2xFab.getY();
                powerup2xFab.setTranslationY(offset1);
                offset2 = mainY - powerupAudiencePollFab.getY();
                powerupAudiencePollFab.setTranslationY(offset2);
                offset3 = mainY - powerupNonegsFab.getY();
                powerupNonegsFab.setTranslationY(offset3);
                return true;
            }
        });

        this.mPredictionPresenter = PredictionPresenterImpl.newInstance(this);
        this.mPredictionPresenter.onCreatePrediction(getIntent().getExtras());


//        btnquestionValue.setOnTouchListener(this);
//        powerup2xFab.setOnTouchListener(this);
//        powerupNonegsFab.setOnTouchListener(this);
//        powerupAudiencePollFab.setOnTouchListener(this);
//        mSwipeFlingAdapterView.setOnDragListener(this);

//        final ImageView rightArrow =  (ImageView) findViewById(R.id.swipe_card_iv_right_arrow);
//        final ImageView rightArrow2 =  (ImageView) findViewById(R.id.swipe_card_iv_right_arrow2);
//        final ImageView rightArrow3 =  (ImageView) findViewById(R.id.swipe_card_iv_right_arrow3);
//
//
//        final Animation arrow1Animation = createArrowAnimation(1000,1,0);
//        arrow1Animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                Log.i("PredictionActivity", "First anim starts");
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//                Log.i("PredictionActivity", "First anim repeats");
//                rightArrow.setVisibility(View.GONE);
//                /*arrow1Animation.setDuration(2000);
//                arrow1Animation.setStartOffset(1000);*/
//
//            }
//        });
//        final Animation arrow2Animation = createArrowAnimation(1000,1,0);
//        arrow2Animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                rightArrow2.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//                rightArrow2.setVisibility(View.GONE);
//
//            }
//        });
//        final Animation arrow3Animation = createArrowAnimation(1000,1,0);
//        arrow3Animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                rightArrow3.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//                rightArrow3.setVisibility(View.GONE);
//
//            }
//        });
//
//        rightArrow.startAnimation(arrow1Animation);
//
//        rightArrow2.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                rightArrow2.startAnimation(arrow2Animation);
//            }
//        }, 500);
//
//        rightArrow3.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                rightArrow3.startAnimation(arrow3Animation);
//            }
//        }, 1000);


    }

//    private Animation createArrowAnimation(int duration, float fromAlpha, float toAlpha){
//
////        AnimationSet animationSet = new AnimationSet(true);
//
//        /*TranslateAnimation translateAnimation=new TranslateAnimation(fromXDelta,toXDelta,fromYDelta,toYDelta);
//        translateAnimation.setDuration(duration);
//        translateAnimation.setStartOffset(startoffset);
//        translateAnimation.setRepeatCount(Animation.INFINITE);
//        animationSet.addAnimation(translateAnimation);*/
//
//        AlphaAnimation alphaAnimation=new AlphaAnimation(fromAlpha,toAlpha);
//        alphaAnimation.setDuration(duration);
////        alphaAnimation.setRepeatCount(Animation.INFINITE);
////        animationSet.addAnimation(alphaAnimation);
//
//        return alphaAnimation;
//    }


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
        if(!m2xpowerUpApplied) {
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
                if(!m2xpowerUpApplied) {
                    if (NostragamusDataHandler.getInstance().getNumberof2xPowerups() > 0) {
                        m2xpowerUpApplied = true;
                        mPredictionPresenter.onPowerUp("2x");
                        NostragamusDataHandler.getInstance().setNumberof2xPowerups(NostragamusDataHandler.getInstance().getNumberof2xPowerups() - 1);
                        String UpdatedPowerUps = String.valueOf(NostragamusDataHandler.getInstance().getNumberof2xPowerups());
                        btn2xpowerUpCount.setText(UpdatedPowerUps);
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
        findViewById(R.id.downnarrow).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNeither() {
        findViewById(R.id.prediction_btn_neither).setVisibility(View.INVISIBLE);
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
            case R.id.fab_main:
                animateFAB();
                break;
            case R.id.fab_fl_2x:
                if(!m2xpowerUpApplied || !mAudiencePollpowerUpApplied || !mNonegspowerUpApplied) {
                    if (NostragamusDataHandler.getInstance().getNumberof2xPowerups() > 0) {
                        m2xpowerUpApplied = true;
                        mAudiencePollpowerUpApplied = true;
                        mNonegspowerUpApplied = true;
                        mPredictionPresenter.onPowerUp("2x");
                        String UpdatedPowerUps = String.valueOf(NostragamusDataHandler.getInstance().getNumberof2xPowerups()-1);
                        btn2xpowerUpCount.setText(UpdatedPowerUps);
                        animateFAB();
                    }
                }
                break;
            case R.id.fab_fl_audience_poll:
                if(!mAudiencePollpowerUpApplied || !m2xpowerUpApplied || !mNonegspowerUpApplied) {
                    if (NostragamusDataHandler.getInstance().getNumberofAudiencePollPowerups() > 0) {
                        showProgressbar();
                        m2xpowerUpApplied = true;
                        mAudiencePollpowerUpApplied = true;
                        mNonegspowerUpApplied = true;
                        mPredictionPresenter.onPowerUp("player_poll");
                       // NostragamusDataHandler.getInstance().setNumberofAudiencePollPowerups(NostragamusDataHandler.getInstance().getNumberofAudiencePollPowerups() - 1);
                        String UpdatedPowerUps = String.valueOf(NostragamusDataHandler.getInstance().getNumberofAudiencePollPowerups()-1);
                        btnAudiencePollCount.setText(UpdatedPowerUps);
                        animateFAB();
                    }
                }
                break;
            case R.id.fab_fl_nonegs:
                if(!mNonegspowerUpApplied || !m2xpowerUpApplied || !mAudiencePollpowerUpApplied) {
                    if (NostragamusDataHandler.getInstance().getNumberofNonegsPowerups() > 0) {
                        m2xpowerUpApplied = true;
                        mAudiencePollpowerUpApplied = true;
                        mNonegspowerUpApplied = true;
                        mPredictionPresenter.onPowerUp("no_negs");
                        //NostragamusDataHandler.getInstance().setNumberofNonegsPowerups(NostragamusDataHandler.getInstance().getNumberofNonegsPowerups() - 1);
                        String UpdatedPowerUps = String.valueOf(NostragamusDataHandler.getInstance().getNumberofNonegsPowerups()-1);
                        btnNonegsCount.setText(UpdatedPowerUps);
                        animateFAB();
                    }
                }
                break;
        }
    }



    @Override
    public void dismissPowerUp() {
        m2xpowerUpApplied =false;
        mAudiencePollpowerUpApplied =false;
        mNonegspowerUpApplied =false;
    }

    @Override
    public void updateAudiencePollPowerup(){

        NostragamusDataHandler.getInstance().setNumberofAudiencePollPowerups(NostragamusDataHandler.getInstance().getNumberofAudiencePollPowerups() + 1);
        String UpdatedPowerUps = String.valueOf(NostragamusDataHandler.getInstance().getNumberofAudiencePollPowerups());
        btnAudiencePollCount.setText(UpdatedPowerUps);
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

        if (itemsInAdapter==1){
            ((CustomTextView) findViewById(R.id.prediction_tv_number_of_cards)).setText(String.valueOf(itemsInAdapter) + "/" + String.valueOf(initialCount));
        }
        else {
            ((CustomTextView) findViewById(R.id.prediction_tv_number_of_cards)).setText(String.valueOf(itemsInAdapter) + "/" + String.valueOf(initialCount));
        }


    }

    @Override
    public void setNumberofPowerups(int numberof2xPowerups, int numberofAudiencePollPowerups, int numberofNonegsPowerups) {

        if (numberof2xPowerups < 1) {
            m2xpowerUpApplied = true;
        }
        else if (numberofAudiencePollPowerups < 1){
            mAudiencePollpowerUpApplied=true;
        }
        else if(numberofNonegsPowerups < 1) {
            mNonegspowerUpApplied=true;
        }

        btn2xpowerUpCount.setText(String.valueOf(numberof2xPowerups));
        btnAudiencePollCount.setText(String.valueOf(numberofAudiencePollPowerups));
        btnNonegsCount.setText(String.valueOf(numberofNonegsPowerups));

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

        mplayBg.setBackgroundResource(bgRes);
    }

    @Override
    public void goBack() {
        onBackPressed();
    }

    @Override
    public View getRootView() {
        return findViewById(R.id.content);
    }


    public void animateFAB(){

        if(isFabOpen){
            /*powerup2xFab.startAnimation(fab_close);
            powerupAudiencePollFab.startAnimation(fab_close);
            powerupNonegsFab.startAnimation(fab_close);
            powerup2xFab.setClickable(false);
            powerupAudiencePollFab.setClickable(false);
            powerupNonegsFab.setClickable(false);
            btn2xpowerUpCount.setVisibility(View.GONE);
            btnAudiencePollCount.setVisibility(View.GONE);
            btnNonegsCount.setVisibility(View.GONE);*/
            collapseFab();
            powerupMainFab.startAnimation(rotate_backward);
            powerupMainFab.setImageResource(R.drawable.powerup_main_icon_white);
            isFabOpen = false;

        } else {
            /*powerup2xFab.startAnimation(fab_open);
            powerupAudiencePollFab.startAnimation(fab_open);
            powerupNonegsFab.startAnimation(fab_open);
            powerup2xFab.setClickable(true);
            powerupAudiencePollFab.setClickable(true);
            powerupNonegsFab.setClickable(true);
            btn2xpowerUpCount.setVisibility(View.VISIBLE);
            btnAudiencePollCount.setVisibility(View.VISIBLE);
            btnNonegsCount.setVisibility(View.VISIBLE);*/
            expandFab();
            powerupMainFab.startAnimation(rotate_forward);
            powerupMainFab.setImageResource(R.drawable.powerup_main_icon);
            isFabOpen = true;

        }
    }

    private void collapseFab() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createCollapseAnimator(powerup2xFab, offset1),
                createCollapseAnimator(powerupAudiencePollFab, offset2),
                createCollapseAnimator(powerupNonegsFab, offset3));
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                powerup2xFab.setVisibility(View.INVISIBLE);
                powerupAudiencePollFab.setVisibility(View.INVISIBLE);
                powerupNonegsFab.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    private void expandFab() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createExpandAnimator(powerup2xFab, offset1),
                createExpandAnimator(powerupAudiencePollFab, offset2),
                createExpandAnimator(powerupNonegsFab, offset3));
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                powerup2xFab.setVisibility(View.VISIBLE);
                powerupAudiencePollFab.setVisibility(View.VISIBLE);
                powerupNonegsFab.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    private static final String TRANSLATION_Y = "translationY";

    private Animator createCollapseAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, 0, offset)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

    private Animator createExpandAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, offset, 0)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
    }
}