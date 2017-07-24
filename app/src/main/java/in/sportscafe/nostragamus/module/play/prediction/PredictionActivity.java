package in.sportscafe.nostragamus.module.play.prediction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.CustomProgressbar;
import com.jeeva.android.widgets.HmImageView;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AppPermissions;
import in.sportscafe.nostragamus.Constants.IntentActions;
import in.sportscafe.nostragamus.Constants.RequestCodes;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.coachmarker.TourGuide;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.NostragamusWebView;
import in.sportscafe.nostragamus.module.common.OnDismissListener;
import in.sportscafe.nostragamus.module.common.ShakeListener;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsChecker;
import in.sportscafe.nostragamus.module.navigation.help.dummygame.DummyGameActivity;
import in.sportscafe.nostragamus.module.play.gamePlayHelp.GamePlayHelpActivity;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsActivity;
import in.sportscafe.nostragamus.module.play.powerup.PowerupBankTransferToPlayActivity;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.SwipeFlingAdapterView;
import in.sportscafe.nostragamus.module.popups.BankInfoDialogFragment;
import in.sportscafe.nostragamus.module.popups.BankTransferDialogFragment;
import in.sportscafe.nostragamus.module.popups.PowerupDialogFragment;
import in.sportscafe.nostragamus.module.popups.inapppopups.InAppPopupFragment;
import in.sportscafe.nostragamus.utils.ViewUtils;

public class PredictionActivity extends NostragamusActivity implements PredictionView,
        View.OnClickListener, OnDismissListener {

    private final static int POWERUP_BANK_ACTIVITY_REQUEST_CODE = 99;
    private final static int GAME_PLAY_HELP_ACTIVITY = 98;

    private final static int DUMMY_GAME_REQUEST_CODE = 45;

    private final static int SHARE_QUESTION_REQUEST_CODE = 46;

    private final static int BANK_DIALOG_REQUEST_CODE = 47;

    private final static int POPUP_DIALOG_REQUEST_CODE = 48;

    private SwipeFlingAdapterView mSwipeFlingAdapterView;

    private TextView mCardNumberTextView;

    private ImageView mIv2xPowerup;

    private ImageView mIvNonegsPowerup;

    private ImageView mIvPollPowerup;

    private TextView mTv2xPowerupCount;

    private TextView mTvNonegsPowerupCount;

    private TextView mTvPollPowerupCount;

    private TextView mTvNeitherOption;

    private ShakeListener mShakeListener;

    private PredictionPresenter mPredictionPresenter;

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.PLAY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);
        setImmersiveFullScreenMode();

        mShakeListener = new ShakeListener() {
            @Override
            public void onShake() {
                if (new PermissionsChecker(PredictionActivity.this).lacksPermissions(AppPermissions.STORAGE)) {
                    PermissionsActivity.startActivityForResult(PredictionActivity.this,
                            RequestCodes.STORAGE_PERMISSION, AppPermissions.STORAGE);
                } else {
                    mPredictionPresenter.onShake();
                }
            }
        };

//        initToolbar();

        mSwipeFlingAdapterView = (SwipeFlingAdapterView) findViewById(R.id.activity_prediction_swipe);

        mCardNumberTextView = (TextView) findViewById(R.id.card_number_textview);
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

        this.mPredictionPresenter = PredictionPresenterImpl.newInstance(this);
        this.mPredictionPresenter.onCreatePrediction(getIntent().getExtras());
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mShakeListener.resume(this);
        dismissMessage();
        setImmersiveFullScreenMode();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mShakeListener.pause();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.setPadding(0, 0, 0, 0);
        setSupportActionBar(toolbar);
    }

    @Override
    public void setContestName(String leftContestName, String rightContestName,
                               String leftImageUrl, String rightImageUrl,
                               String matchStage) {

        TextView mLeftContestName = (TextView) findViewById(R.id.prediction_contest_name_left_textview);
        mLeftContestName.setText(leftContestName);
        TextView mRightContestName = (TextView) findViewById(R.id.prediction_contest_name_right_textview);
        mRightContestName.setText(rightContestName);
        HmImageView mLeftContestImage = (HmImageView) findViewById(R.id.prediction_contest_left_imageView);
        mLeftContestImage.setImageUrl(leftImageUrl);
        HmImageView mRightContestImage = (HmImageView) findViewById(R.id.prediction_contest_right_imageView);
        mRightContestImage.setImageUrl(rightImageUrl);


        if (TextUtils.isEmpty(rightContestName)){
            ((TextView) findViewById(R.id.prediction_contest_vs_textView)).setVisibility(View.GONE);
            mRightContestImage.setVisibility(View.GONE);
            mRightContestName.setVisibility(View.GONE);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            mLeftContestName.setLayoutParams(params);
        }


        /*((TextView) findViewById(R.id.prediction_tv_contest_name)).setText(leftContestName);
        TextView tvMatchStage = (TextView) findViewById(R.id.prediction_tv_match_stage);*/
//        tvMatchStage.setVisibility(View.VISIBLE);
//        tvMatchStage.setText(matchStage);
    }

    @Override
    public void setAdapter(PredictionAdapter predictionAdapter, SwipeFlingAdapterView.OnSwipeListener<Question> swipeListener) {
        predictionAdapter.setRootView(findViewById(R.id.content));

        mSwipeFlingAdapterView.setAdapter(predictionAdapter, R.id.swipe_card_cv_main);
        mSwipeFlingAdapterView.setSwipeListener(swipeListener);

        predictionAdapter.notifyDataSetChanged();
    }

    @Override
    public void showShuffle() {
        findViewById(R.id.prediction_iv_shuffle).setVisibility(View.VISIBLE);
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

            case R.id.prediction_iv_shuffle:
                try {
                    mSwipeFlingAdapterView.getTopCardListener().selectBottom();
                } catch (Exception ex) {}
                break;

            case R.id.prediction_ll_neither:
                try {
                    mSwipeFlingAdapterView.getTopCardListener().selectTop();
                } catch (Exception ex) {}
                break;

            case R.id.prediction_iv_left_arrow:
                try {
                    mSwipeFlingAdapterView.getTopCardListener().selectLeft();
                } catch (Exception ex) {}
                break;

            case R.id.prediction_iv_right_arrow:
                try {
                    mSwipeFlingAdapterView.getTopCardListener().selectRight();
                } catch (Exception ex) {}
                break;

            case R.id.powerups_iv_2x:
                makeClickAnimation(findViewById(R.id.powerup_2x_view));
                mPredictionPresenter.onClick2xPowerup();
                break;

            case R.id.powerups_iv_nonegs:
                makeClickAnimation(findViewById(R.id.powerup_no_negative_view));
                mPredictionPresenter.onClickNonegsPowerup();
                break;

            case R.id.powerups_iv_poll:
                makeClickAnimation(findViewById(R.id.powerup_audience_poll_view));
                mPredictionPresenter.onClickPollPowerup();
                break;

            case R.id.powerups_iv_info:
//                new PowerupDialogFragment().show(getSupportFragmentManager(), "Powerup");
                onGamePlayInfoButtonClicked();
                break;

            case R.id.powerups_iv_bank:
                mPredictionPresenter.onClickBankTransfer();
//                new BankInfoDialogFragment().show(getSupportFragmentManager(), "BankInfo");
                break;

            case R.id.prediction_share_layout:
                onAskFriendClicked();
                break;
        }
    }

    private void onGamePlayInfoButtonClicked() {
        Intent intent = new Intent(PredictionActivity.this, GamePlayHelpActivity.class);
        startActivityForResult(intent, GAME_PLAY_HELP_ACTIVITY);
        hidePowerupBankAndHelpButtons(findViewById(R.id.powerups_iv_bank), findViewById(R.id.powerups_iv_info));
    }

    private void makeClickAnimation(final View view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.7f, 1f, 0.7f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1f, 0.7f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(200);
                view.startAnimation(scaleAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(scaleAnimation);
    }

    private void onAskFriendClicked() {
        if (new PermissionsChecker(this).lacksPermissions(AppPermissions.STORAGE)) {
            PermissionsActivity.startActivityForResult(this, RequestCodes.STORAGE_PERMISSION, AppPermissions.STORAGE);
        } else {
            takeScreenshotAndShare();
        }
    }

    @Override
    public void navigateToFeed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void setNeitherOption(String neitherOption) {
        mTvNeitherOption.setText(neitherOption);

        invokeCardListener();
    }

    @Override
    public void setNumberofCards(String numberofCards) {
        mCardNumberTextView.setText(numberofCards);
    }

    @Override
    public void set2xPowerupCount(int count, boolean reverse) {
        applyAlphaForPowerUp(mIv2xPowerup, mTv2xPowerupCount, reverse, count);
    }

    @Override
    public void setNonegsPowerupCount(int count, boolean reverse) {
        applyAlphaForPowerUp(mIvNonegsPowerup, mTvNonegsPowerupCount, reverse, count);
    }

    @Override
    public void setPollPowerupCount(int count, boolean reverse) {
        applyAlphaForPowerUp(mIvPollPowerup, mTvPollPowerupCount, reverse, count);
    }

    @Override
    public void notifyTopView() {
        mSwipeFlingAdapterView.refreshTopLayout();
//        invokeCardListener();
    }

    @Override
    public void goBack() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        mPredictionPresenter.onClickBack();
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
    public void hideLeftRightIndicator() {
//        findViewById(R.id.prediction_iv_dummy_left_right_indicator).setVisibility(View.GONE);
    }

    @Override
    public void hideNeitherIndicator() {
//        findViewById(R.id.prediction_iv_dummy_neither_indicator).setVisibility(View.GONE);
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void navigateToDummyGame() {
        startActivityForResult(new Intent(this, DummyGameActivity.class), DUMMY_GAME_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (DUMMY_GAME_REQUEST_CODE == requestCode) {
            mPredictionPresenter.onGetDummyGameResult();
        }

        if (RequestCodes.STORAGE_PERMISSION == requestCode && PermissionsActivity.PERMISSIONS_GRANTED == resultCode) {
            mPredictionPresenter.onShake();
        }

        if (requestCode == POWERUP_BANK_ACTIVITY_REQUEST_CODE || requestCode == GAME_PLAY_HELP_ACTIVITY) {
            showPowerupBankAndHelpButtons(findViewById(R.id.powerups_iv_bank), findViewById(R.id.powerups_iv_info));
        }
    }

    private void showPowerupBankAndHelpButtons(final View powerupView, final View helpView) {
        AlphaAnimation animation = new AlphaAnimation(0f, 1f);
        animation.setDuration(500);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                powerupView.setVisibility(View.VISIBLE);
                helpView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        powerupView.startAnimation(animation);
        helpView.startAnimation(animation);
    }

    private void hidePowerupBankAndHelpButtons(final View powerupView, final View helpView) {
        AlphaAnimation animation = new AlphaAnimation(1f, 0f);
        animation.setDuration(500);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                powerupView.setVisibility(View.GONE);
                helpView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        powerupView.startAnimation(animation);
        helpView.startAnimation(animation);
    }

    private TourGuide mCoachMarker;

    @Override
    public void navigateToBankTransfer(Bundle bundle) {
        BankTransferDialogFragment.newInstance(bundle).show(getSupportFragmentManager(), "BankTransfer");
    }

    @Override
    public void showBankInfo() {
        BankInfoDialogFragment.newInstance(BANK_DIALOG_REQUEST_CODE).show(getSupportFragmentManager(), "BankInfo");
    }

    @Override
    public void showPopUp(String popUpType) {
        openPopup(popUpType);
    }

    @Override
    public void showFirstMatchPlayedPopUp(String popUpType, Bundle bundle) {
//        Intent intent = new Intent(this, InAppPopupActivity.class);
//        intent.putExtra(Constants.InAppPopups.IN_APP_POPUP_TYPE, popUpType);
//        intent.putExtras(bundle);
//        startActivity(intent);

//        InAppPopupFragment fragment = new InAppPopupFragment();
//        bundle.putString(Constants.InAppPopups.IN_APP_POPUP_TYPE, popUpType);
//        fragment.setArguments(bundle);
//        fragment.show(getSupportFragmentManager(), "InAppPopup");

        bundle.putString(Constants.InAppPopups.IN_APP_POPUP_TYPE, popUpType);
        InAppPopupFragment.newInstance(POPUP_DIALOG_REQUEST_CODE, bundle).show(getSupportFragmentManager(), "InAppPopup");

    }

    @Override
    public void takeScreenshotAndShare() {
        View rootView = findViewById(R.id.content);

        Bitmap playCardBitmap = ViewUtils.viewToBitmap(
                rootView,
                rootView.getMeasuredWidth(),
                rootView.getMeasuredHeight()
        );

        if (null != playCardBitmap) {
            AppSnippet.doGeneralImageShare(this, playCardBitmap, String.valueOf(NostragamusDataHandler.getInstance().getAskFriendText()));
        }
    }

    @Override
    public void navigateToResults(Bundle bundle) {
        Intent intent = new Intent(this, MyResultsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void showPowerUpBankActivity(Bundle args) {
        Intent intent = new Intent(PredictionActivity.this, PowerupBankTransferToPlayActivity.class);
        if (args != null) {
            intent.putExtras(args);
        }
        startActivityForResult(intent, POWERUP_BANK_ACTIVITY_REQUEST_CODE);
        hidePowerupBankAndHelpButtons(findViewById(R.id.powerups_iv_bank), findViewById(R.id.powerups_iv_info));
    }

    private void openPopup(String popUpType) {

        InAppPopupFragment fragment = new InAppPopupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.InAppPopups.IN_APP_POPUP_TYPE, popUpType);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "InAppPopup");

//        Intent intent = new Intent(this, InAppPopupActivity.class);
//        intent.putExtra(Constants.InAppPopups.IN_APP_POPUP_TYPE, popUpType);
//        startActivity(intent);
    }

    private Drawable getPowerupDrawable(int colorRes) {
        GradientDrawable powerupDrawable = new GradientDrawable();
        powerupDrawable.setShape(GradientDrawable.OVAL);
//        powerupDrawable.setCornerRadius(getResources().getDimensionPixelSize(R.dimen.dp_5));
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
        LocalBroadcastManager.getInstance(this).registerReceiver(mPowerUpUpdatedReceiver,
                new IntentFilter(IntentActions.ACTION_POWERUPS_UPDATED));

        LocalBroadcastManager.getInstance(this).registerReceiver(mOpenWebView,
                new IntentFilter(IntentActions.ACTION_OPEN_WEBVIEW));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mPowerUpUpdatedReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mOpenWebView);
    }

    BroadcastReceiver mPowerUpUpdatedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPredictionPresenter.onChallengeInfoUpdated(intent.getExtras());
        }
    };


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

    @Override
    public void onDismiss(int requestCode, Bundle bundle) {

        if (requestCode == BANK_DIALOG_REQUEST_CODE) {
            mPredictionPresenter.onBankInfoDismiss();
        } else if (requestCode == POPUP_DIALOG_REQUEST_CODE) {
            navigateToFeed();
        }
    }


    BroadcastReceiver mOpenWebView = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showNostragamusWebView();
        }
    };

    private void showNostragamusWebView() {
        Intent intent = new Intent(this, NostragamusWebView.class);
        startActivity(intent);
    }



}