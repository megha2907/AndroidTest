package in.sportscafe.nostragamus.module.play.prediction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
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

import com.jeeva.android.Log;
import com.jeeva.android.widgets.CustomProgressbar;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AppPermissions;
import in.sportscafe.nostragamus.Constants.IntentActions;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.coachmarker.TargetView;
import in.sportscafe.nostragamus.module.coachmarker.TourGuide;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.ShakeListener;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsChecker;
import in.sportscafe.nostragamus.module.play.dummygame.DummyGameActivity;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.module.play.tindercard.SwipeFlingAdapterView;
import in.sportscafe.nostragamus.module.popups.BankInfoDialogFragment;
import in.sportscafe.nostragamus.module.popups.BankTransferDialogFragment;
import in.sportscafe.nostragamus.module.popups.PowerupDialogFragment;
import in.sportscafe.nostragamus.module.popups.inapppopups.InAppPopupActivity;
import in.sportscafe.nostragamus.module.popups.inapppopups.InAppPopupFragment;
import in.sportscafe.nostragamus.utils.ViewUtils;

public class PredictionActivity extends NostragamusActivity implements PredictionView,
        View.OnClickListener, DialogInterface.OnDismissListener {

    private final static int DUMMY_GAME_REQUEST_CODE = 45;

    private final static int SHARE_QUESTION_REQUEST_CODE = 46;

    private RelativeLayout mRlPlayBg;

    private ViewGroup mVgPlayPage;

    private SwipeFlingAdapterView mSwipeFlingAdapterView;

    private TextView mTvNumberOfCards;

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

        mShakeListener = new ShakeListener() {
            @Override
            public void onShake() {
                Log.d("PredictionActivity", "Shaked");
                mPredictionPresenter.onShake();
            }
        };

        initToolbar();

        mRlPlayBg = (RelativeLayout) findViewById(R.id.content);
        mVgPlayPage = (ViewGroup) findViewById(R.id.prediction_rl_play_page);
        mSwipeFlingAdapterView = (SwipeFlingAdapterView) findViewById(R.id.activity_prediction_swipe);

        mTvNumberOfCards = (TextView) findViewById(R.id.prediction_tv_number_of_cards);
        mIv2xPowerup = (ImageView) findViewById(R.id.powerups_iv_2x);
        mIvNonegsPowerup = (ImageView) findViewById(R.id.powerups_iv_nonegs);
        mIvPollPowerup = (ImageView) findViewById(R.id.powerups_iv_poll);
        mTv2xPowerupCount = (TextView) findViewById(R.id.powerup_tv_2x_count);
        mTvNonegsPowerupCount = (TextView) findViewById(R.id.powerup_tv_nonegs_count);
        mTvPollPowerupCount = (TextView) findViewById(R.id.powerup_tv_poll_count);
        mTvNeitherOption = (TextView) findViewById(R.id.prediction_tv_neither_text);

//        mIv2xGlobalPowerup.setBackground(getPowerupDrawable(R.color.goldenyellowcolor));
        mIv2xPowerup.setBackground(getPowerupDrawable(R.color.dodger_blue));
        mIvNonegsPowerup.setBackground(getPowerupDrawable(R.color.amaranth));
        mIvPollPowerup.setBackground(getPowerupDrawable(R.color.greencolor));

        this.mPredictionPresenter = PredictionPresenterImpl.newInstance(this);
        this.mPredictionPresenter.onCreatePrediction(getIntent().getExtras());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mShakeListener.resume(this);
        dismissMessage();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mShakeListener.pause();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.setPadding(0, 0, 0, 0);
        setSupportActionBar(toolbar);
    }

    @Override
    public void setContestName(String contestName, String matchStage) {
        ((TextView) findViewById(R.id.prediction_tv_contest_name)).setText(contestName);
        TextView tvMatchStage = (TextView) findViewById(R.id.prediction_tv_match_stage);
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
            case R.id.powerups_iv_2x:
                mPredictionPresenter.onClick2xPowerup();
                break;
            case R.id.powerups_iv_nonegs:
                mPredictionPresenter.onClickNonegsPowerup();
                break;
            case R.id.powerups_iv_poll:
                mPredictionPresenter.onClickPollPowerup();
                break;
            case R.id.powerups_iv_info:
                new PowerupDialogFragment().show(getSupportFragmentManager(), "Powerup");
                break;
            case R.id.powerups_iv_bank:
                mPredictionPresenter.onClickBankTransfer();
//                new BankInfoDialogFragment().show(getSupportFragmentManager(), "BankInfo");
                break;
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
        mTvNumberOfCards.setText(numberofCards);
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
        invokeCardListener();
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
        findViewById(R.id.powerups_iv_info).setVisibility(View.VISIBLE);
//        findViewById(R.id.prediction_rl_powerup_hints).setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePowerupsHint() {
        findViewById(R.id.powerups_iv_info).setVisibility(View.GONE);
//        findViewById(R.id.prediction_rl_powerup_hints).setVisibility(View.GONE);
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

    @Override
    public void navigateToBankTransfer(Bundle bundle) {
        BankTransferDialogFragment.newInstance(bundle).show(getSupportFragmentManager(), "BankTransfer");
    }

    @Override
    public void showBankInfo() {
        BankInfoDialogFragment.newInstance().show(getSupportFragmentManager(), "BankInfo");
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

        InAppPopupFragment fragment = new InAppPopupFragment();
        bundle.putString(Constants.InAppPopups.IN_APP_POPUP_TYPE, popUpType);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "InAppPopup");

    }

    @Override
    public boolean checkStoragePermission() {
        if(new PermissionsChecker(this).lacksPermissions(AppPermissions.STORAGE)) {
            PermissionsActivity.startActivityForResult(this, 0, AppPermissions.STORAGE);
            return false;
        }
        return true;
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
            AppSnippet.doGeneralImageShare(this, playCardBitmap, "");
        }
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
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mPowerUpUpdatedReceiver);
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
    public void onDismiss(DialogInterface dialog) {
        mPredictionPresenter.onBankInfoDismiss();
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
                    if (NostragamusDataHandler.getInstance().get2xPowerupsCount() > 0) {
                        m2xpowerUpApplied = true;
                        mPredictionPresenter.onPowerUp("2x");
                        NostragamusDataHandler.getInstance().setNumberof2xPowerups(NostragamusDataHandler.getInstance().get2xPowerupsCount() - 1);
                        String UpdatedPowerUps = String.valueOf(NostragamusDataHandler.getInstance().get2xPowerupsCount());
                        mTv2xPowerupCount.setText(UpdatedPowerUps);
                    }
                }
                break;
        }
        return true;
    }*/
