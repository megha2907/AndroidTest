package in.sportscafe.nostragamus.module.play.myresults;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AppPermissions;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.Notifications;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.Constants.RequestCodes;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.contest.dto.ContestScreenData;
import in.sportscafe.nostragamus.module.contest.ui.ContestsActivity;
import in.sportscafe.nostragamus.module.inPlay.ui.ResultsScreenDataDto;
import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsChecker;
import in.sportscafe.nostragamus.module.popups.timerPopup.TimerFinishDialogHelper;
import in.sportscafe.nostragamus.module.prediction.playScreen.PredictionActivity;
import in.sportscafe.nostragamus.module.resultspeek.dto.Match;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by Jeeva on 15/6/16.
 */
public class MyResultsActivity extends NostragamusActivity implements MyResultsView, View.OnClickListener {

    private static final String TAG = MyResultsActivity.class.getSimpleName();

    private RecyclerView mRvMyResults;
    private MyResultsPresenter mResultsPresenter;

    private boolean mReplaypowerUpApplied = false;
    private boolean mFlippowerUpApplied = false;

    private Button btnReplayPowerUpCount;
    private Button btnFlipPowerUpCount;
    private Toolbar mtoolbar;
    private Bundle mbundle = new Bundle();
    private TextView mTitle;
    private TextView mMatchStage;
    private boolean goback = false;
    private Boolean isFabOpen = false;
    private Boolean isShareFabOpen = false;
    private View powerupReplayFab, powerupFlipFab, fabContainer/*,shareContainer,btnfbShare*/;
    private ImageButton powerupMainFab;
    private FloatingActionButton shareFab;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward, share_rotate_forward, share_rotate_backward;

    private Button shareResultsBtn;
    private RelativeLayout mRlshareResults;
    TextView mTvLeaderBoardRank;
    TextView mTvLeaderBoardTotalPlayers;

    private float offset1;
    private float offset2;

    private float offset3;

    private Bundle matchBundle;

    LinearLayoutManager mlayoutManager;
    private boolean mIsResultAwaitingScreen = false;
    private ResultsScreenDataDto mResultScreenData;

    public interface LaunchedFrom {
        int IN_PLAY_SCREEN_MATCH_DID_NOT_PLAY = 115;
        int IN_PLAY_SCREEN_MATCH_CHECK_POINTS = 116;
        int IN_PLAY_SCREEN_MATCH_AWAITING_RESULTS = 117;
        int CHECK_RESULTS_NOTIFICATION = 118;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_results);

        initToolBar();
        initMembers();

        mlayoutManager = new LinearLayoutManager(this);
        mRvMyResults = (RecyclerView) findViewById(R.id.my_results_rv);
        mRvMyResults.setLayoutManager(mlayoutManager);
        mRvMyResults.setNestedScrollingEnabled(false);
        shareResultsBtn = (Button) findViewById(R.id.my_results_ll_share_score);
        mRlshareResults = (RelativeLayout) findViewById(R.id.my_results_rl_share_score);
        shareResultsBtn.setOnClickListener(this);
        btnReplayPowerUpCount = (Button) findViewById(R.id.powerup_replay_count);
        btnFlipPowerUpCount = (Button) findViewById(R.id.powerup_flip_count);
        powerupMainFab = (ImageButton) findViewById(R.id.results_fab_main);
        powerupReplayFab = findViewById(R.id.fab_fl_replay);
        powerupFlipFab = findViewById(R.id.fab_fl_flip);
        fabContainer = findViewById(R.id.fab_container);
        mTvLeaderBoardRank = (TextView)findViewById(R.id.schedule_row_tv_leaderboard_rank);
        mTvLeaderBoardTotalPlayers = (TextView) findViewById(R.id.schedule_row_tv_leaderboard_total_players);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.powerup_fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.powerup_fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.powerup_fab_rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.powerup_fab_rotate_backward);
        share_rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.share_fab_rotate_forward);
        share_rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.share_fab_rotate_backward);

        powerupMainFab.setOnClickListener(this);
        powerupReplayFab.setOnClickListener(this);
        powerupFlipFab.setOnClickListener(this);

        fabContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                fabContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                float mainY = powerupMainFab.getY();

                offset1 = mainY - powerupFlipFab.getY();
                powerupFlipFab.setTranslationY(offset1);
                offset2 = mainY - powerupReplayFab.getY();
                powerupReplayFab.setTranslationY(offset2);
                return true;
            }
        });


        this.mRvMyResults.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        this.mRvMyResults.setHasFixedSize(true);

        this.mResultsPresenter = MyResultPresenterImpl.newInstance(this);
        this.mResultsPresenter.onCreateMyResults(getIntent().getExtras());
        powerupMainFab.setVisibility(View.GONE);

        /* If headless flow, show join contest layout */
        if (isHeadlessFlow() || isPlayingPseudoGame()) {
            LinearLayout cardLayout = (LinearLayout) findViewById(R.id.my_result_join_contest_layout);
            cardLayout.setVisibility(View.VISIBLE);

            Button btnJoinContest = (Button) findViewById(R.id.results_join_contest_button);
            btnJoinContest.setOnClickListener(this);
        }

    }

    private void initMembers() {
        if (getIntent() != null) {
            mbundle = getIntent().getExtras();
            if (mbundle != null && mbundle.containsKey(BundleKeys.RESULTS_SCREEN_DATA)) {
                mResultScreenData = Parcels.unwrap(mbundle.getParcelable(BundleKeys.RESULTS_SCREEN_DATA));
            }
        }
    }

    private boolean isHeadlessFlow() {
        boolean isHeadLess = false;
        if (mbundle != null && mbundle.getBoolean(BundleKeys.IS_HEADLESS_FLOW, false)) {
            isHeadLess = true;
        }
        return isHeadLess;
    }

    private boolean isPlayingPseudoGame() {
        boolean isPseudoGame = false;
        if (mResultScreenData != null && mResultScreenData.isPlayingPseudoGame()) {
            isPseudoGame = true;
        }
        return isPseudoGame;
    }

    @Override
    protected void onResume() {
        super.onResume();
        dismissMessage();
    }

    @Override
    public void setAdapter(MyResultsAdapter myResultsAdapter) {
        mRvMyResults.setAdapter(myResultsAdapter);
    }

    public void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.my_results_toolbar);
        mTitle = (TextView) mtoolbar.findViewById(R.id.toolbar_title);
        mMatchStage = (TextView) mtoolbar.findViewById(R.id.toolbar_match_stage);
        mTitle.setText(getString(R.string.match_results));
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setContentInsetStartWithNavigation(2);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                }

        );
    }

    @Override
    public void onBackPressed() {
        finishStackAndLaunchHomeInPlay();
    }

    private void finishStackAndLaunchHomeInPlay() {
        Bundle args = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            args = getIntent().getExtras();
        }

        Intent clearTaskIntent = new Intent(getApplicationContext(), NostraHomeActivity.class);
        clearTaskIntent.putExtra(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, NostraHomeActivity.LaunchedFrom.SHOW_IN_PLAY);
        clearTaskIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        if (args != null) {
            clearTaskIntent.putExtras(args);
        }
        startActivity(clearTaskIntent);
    }

    private final boolean shouldUpRecreateTask(Activity from) {
        Bundle bundle = from.getIntent().getExtras();
        return null != bundle && bundle.getBoolean(Notifications.IS_LAUNCHED_FROM_NOTIFICATION);
    }

    @Override
    public void navigateToHome() {
        /*Intent homeIntent = new Intent(this, HomeActivity.class);
        startActivity(homeIntent);
        finish();*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.results_fab_main:
                animateFAB();
                break;

            /*case R.id.fab_share:
                animateShareFAB();
                break;*/

            case R.id.fab_fl_replay:
                if (!mFlippowerUpApplied || !mReplaypowerUpApplied) {
                    if (NostragamusDataHandler.getInstance().getReplayPowerupsCount() > 0) {
                        mFlippowerUpApplied = true;
                        mReplaypowerUpApplied = true;
                        mResultsPresenter.onPowerUp(Powerups.MATCH_REPLAY);
                    } else {
                        Toast toast = Toast.makeText(this, Constants.Alerts.REPLAY_POWERUP_OVER, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    animateFAB();
                }
            case R.id.fab_fl_flip:
                if (!mFlippowerUpApplied || !mReplaypowerUpApplied) {
                    if (NostragamusDataHandler.getInstance().getFlipPowerupsCount() > 0) {
                        mFlippowerUpApplied = true;
                        mReplaypowerUpApplied = true;
                        navigatetoFlipScreen();
                    } else {
                        Toast toast = Toast.makeText(this, Constants.Alerts.FLIP_POWERUP_OVER, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    animateFAB();
                }
                break;

            case R.id.my_results_ll_share_score:
                if (new PermissionsChecker(this).lacksPermissions(AppPermissions.STORAGE)) {
                    PermissionsActivity.startActivityForResult(this, RequestCodes.STORAGE_PERMISSION, AppPermissions.STORAGE);
                } else {
                    mResultsPresenter.onClickShare();
                }
                break;

            case R.id.results_join_contest_button:
                navigateToContestScreen();
                break;

            case R.id.my_results_challenge_back_btn_layout:
                onBackPressed();
                break;

        }
    }

    private void navigateToContestScreen() {
        if (mResultScreenData != null) {
            ContestScreenData screenData = new ContestScreenData();
            screenData.setChallengeId(mResultScreenData.getChallengeId());
            screenData.setChallengeName(mResultScreenData.getChallengeName());
            screenData.setChallengeStartTime(mResultScreenData.getChallengeStartTime());
            if (isHeadlessFlow()) {
                screenData.setHeadLessFlow(true);
                screenData.setPseudoRoomId(mResultScreenData.getRoomId());
            }

            Bundle args = new Bundle();
            args.putParcelable(Constants.BundleKeys.CONTEST_SCREEN_DATA, Parcels.wrap(screenData));
            args.putInt(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT, ContestsActivity.LaunchedFrom.IN_PLAY_HEAD_LESS_MATCHES);

            Intent contestScreen = new Intent(this, ContestsActivity.class);
            contestScreen.putExtras(args);
            startActivity(contestScreen);

        } else {
            Log.d(TAG, "No screen data !");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RequestCodes.STORAGE_PERMISSION == requestCode && PermissionsActivity.PERMISSIONS_GRANTED == resultCode) {
            mResultsPresenter.onClickShare();
        }
    }

    private void navigatetoFlipScreen() {
        /*Intent mintent = new Intent(this, FlipActivity.class);
        mintent.putExtras(matchBundle);
        startActivity(mintent);*/
    }

    @Override
    public void setNumberofPowerups(int numberofReplayPowerups, int numberofFlipPowerups) {

        if (numberofReplayPowerups < 1) {
            mReplaypowerUpApplied = true;
        } else if (numberofFlipPowerups < 1) {
            mFlippowerUpApplied = true;
        }

        btnReplayPowerUpCount.setText(String.valueOf(numberofReplayPowerups));
        btnFlipPowerUpCount.setText(String.valueOf(numberofFlipPowerups));

    }

    @Override
    public void setMatchDetails(Match match) {
        matchBundle = new Bundle();
        matchBundle.putParcelable(BundleKeys.MATCH_LIST, Parcels.wrap(match));
    }

    @Override
    public void openReplayDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You want to apply Replay Powerup?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        NostragamusDataHandler.getInstance().setReplayPowerupsCount(NostragamusDataHandler.getInstance().getReplayPowerupsCount() - 1);
                        String updatedPowerUps = String.valueOf(NostragamusDataHandler.getInstance().getReplayPowerupsCount() - 1);
                        btnReplayPowerUpCount.setText(updatedPowerUps);
                        mResultsPresenter.onReplayPowerupApplied();
                        animateFAB();
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void navigatetoPlay(Match match) {
        if (TimerFinishDialogHelper.canPlayGame(match.getStartTime())) {

            Bundle bundle = new Bundle();
            bundle.putParcelable(BundleKeys.MATCH_LIST, Parcels.wrap(match));
            Intent intent = new Intent(this, PredictionActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

        } else {
            TimerFinishDialogHelper.showCanNotPlayGameTimerOutDialog(getSupportFragmentManager());
        }
    }

    @Override
    public void openFlipDialog() {
        String flipPowerUps = String.valueOf(NostragamusDataHandler.getInstance().getFlipPowerupsCount());
        btnFlipPowerUpCount.setText(flipPowerUps);
        mResultsPresenter.onFlipPowerupApplied();
    }

    public void animateFAB() {

        if (isFabOpen) {
            collapseFab();
            powerupMainFab.startAnimation(rotate_backward);
            powerupMainFab.setImageResource(R.drawable.powerup_main_icon_white);
            isFabOpen = false;

        } else {
            expandFab();
            powerupMainFab.startAnimation(rotate_forward);
            powerupMainFab.setImageResource(R.drawable.powerup_main_icon);
            isFabOpen = true;

        }
    }

    /*public void animateShareFAB(){

        if(isShareFabOpen){
            collapseShareFab();
            shareFab.startAnimation(share_rotate_backward);
            shareFab.setImageResource(R.drawable.share_icon_white);
            isShareFabOpen = false;

        } else {
            expandShareFab();
            shareFab.startAnimation(share_rotate_forward);
            shareFab.setImageResource(R.drawable.share_cross_icon);
            isShareFabOpen = true;

        }
    }*/

    @Override
    public void takeScreenShot() {
        int totalHeight = mRvMyResults.computeVerticalScrollRange() + getResources().getDimensionPixelSize(R.dimen.dp_60);

        Bitmap screenshot = Bitmap.createBitmap(mRvMyResults.getWidth(), totalHeight, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(screenshot);
        mRvMyResults.layout(
                0,
                mRvMyResults.getHeight() - totalHeight,
                mRvMyResults.getWidth(),
                totalHeight
        );

        View logo = findViewById(R.id.my_results_nostra_logo);
        logo.setVisibility(View.VISIBLE);

        mRvMyResults.draw(c);

        logo.setVisibility(View.INVISIBLE);

        if (null != screenshot) {
            AppSnippet.doGeneralImageShare(MyResultsActivity.this, screenshot, "");
            mResultsPresenter.onDoneScreenShot();
        }
    }

    @Override
    public void setMatchName(String matchName) {
        mMatchStage.setText(matchName);
    }

    @Override
    public void setToolbarHeading(String result) {
        mTitle.setText(result);
        mIsResultAwaitingScreen = true;
        mRlshareResults.setVisibility(View.GONE);
    }

    @Override
    public void showResultsToBeDeclaredView(Boolean playedFirstMatch, String matchEndTime) {
        LinearLayout llResultsDeclaration = (LinearLayout) findViewById(R.id.my_results_declaration_rl);
        TextView tvResultsDeclarationHeading = (TextView) findViewById(R.id.my_results_declaration_tv_heading);
        TextView tvResultsDeclarationDesc = (TextView) findViewById(R.id.my_results_declaration_tv_desc);
        LinearLayout backButtonLayout = (LinearLayout) findViewById(R.id.my_results_challenge_back_btn_layout);
        backButtonLayout.setOnClickListener(this);

        if (!TextUtils.isEmpty(matchEndTime)) {
            String endDateStr = matchEndTime;
            if (!TextUtils.isEmpty(endDateStr)) {
                llResultsDeclaration.setVisibility(View.VISIBLE);

                // format date
                String endDateFormatted = "";
                long endDateMs = 0;

                try {
                    endDateMs = TimeUtils.getMillisecondsFromDateString(endDateStr,
                            Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE, Constants.DateFormats.GMT);
                    int dayOfMonth = Integer.parseInt(TimeUtils.getDateStringFromMs(endDateMs, "d"));
                    String month = TimeUtils.getDateStringFromMs(endDateMs, "MMM");

                    endDateFormatted = dayOfMonth + AppSnippet.ordinalOnly(dayOfMonth) + " " + month + ", " +
                            TimeUtils.getDateStringFromMs(endDateMs, Constants.DateFormats.HH_MM_AA);
                } catch (Exception e) {

                    endDateMs = TimeUtils.getMillisecondsFromDateString(endDateStr,
                            Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE_ZZ_ZZ, Constants.DateFormats.GMT);
                    int dayOfMonth = Integer.parseInt(TimeUtils.getDateStringFromMs(endDateMs, "d"));
                    String month = TimeUtils.getDateStringFromMs(endDateMs, "MMM");

                    endDateFormatted = dayOfMonth + AppSnippet.ordinalOnly(dayOfMonth) + " " + month + ", " +
                            TimeUtils.getDateStringFromMs(endDateMs, Constants.DateFormats.HH_MM_AA);
                }

                SpannableStringBuilder builder = new SpannableStringBuilder();

                if (playedFirstMatch) {
                    String t1 = "Results will be declared on ";
                    SpannableString st1 = new SpannableString(t1);
                    builder.append(st1);

                    String t2 = endDateFormatted;
                    SpannableString st2 = new SpannableString(t2);
                    st2.setSpan(new ForegroundColorSpan(Color.WHITE), 0, t2.length(), 0);
                    builder.append(st2);

                    String t3 = ". We will notify you when the results are out. Meanwhile, go back and play other live games!";
                    SpannableString st3 = new SpannableString(t3);
                    builder.append(st3);

                    tvResultsDeclarationHeading.setText("You just predicted your first game!");
                    tvResultsDeclarationDesc.setText(builder, TextView.BufferType.SPANNABLE);
                    backButtonLayout.setVisibility(View.VISIBLE);

                } else {
                    tvResultsDeclarationHeading.setText("Done!");
                    findViewById(R.id.correct_icon).setVisibility(View.VISIBLE);

                    // set text
                    String t1 = "Results will be declared on ";
                    SpannableString st1 = new SpannableString(t1);
                    builder.append(st1);

                    String t2 = endDateFormatted;
                    SpannableString st2 = new SpannableString(t2);
                    st2.setSpan(new ForegroundColorSpan(Color.WHITE), 0, t2.length(), 0);
                    builder.append(st2);

                    tvResultsDeclarationDesc.setText(builder, TextView.BufferType.SPANNABLE);
                    backButtonLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void updateAnswers(Match match) {
      refresh(match);
    }

    @Override
    public void setUserRank(Match match) {
        if (null != match) {
            if (null != match.getUserRank() && null != match.getCountPlayers()) {
                mTvLeaderBoardRank.setText(AppSnippet.ordinal(match.getUserRank()));
                mTvLeaderBoardTotalPlayers.setText(" / " + String.valueOf(match.getCountPlayers()));
            } else {
                mTvLeaderBoardRank.setText("NA");
            }
        }else {
            mTvLeaderBoardRank.setText("NA");
        }
    }


    private void collapseFab() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createCollapseAnimator(powerupFlipFab, offset1),
                createCollapseAnimator(powerupReplayFab, offset2));
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                powerupFlipFab.setVisibility(View.INVISIBLE);
                powerupReplayFab.setVisibility(View.INVISIBLE);
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
        animatorSet.playTogether(createExpandAnimator(powerupFlipFab, offset1),
                createExpandAnimator(powerupReplayFab, offset2));
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                powerupFlipFab.setVisibility(View.VISIBLE);
                powerupReplayFab.setVisibility(View.VISIBLE);
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

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.RESULTS;
    }

    private void sendReloadChallengeBroadcast() {
        /*  Send broadcast after some time so that homeActivity broadcast receivers are registered (onStart) */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Constants.IntentActions.ACTION_RELOAD_CHALLENGES);
                LocalBroadcastManager.getInstance(MyResultsActivity.this.getApplicationContext()).sendBroadcast(intent);
            }
        }, 500);

    }

    public void refresh(Match match){
        navigateToResultsActivity(match);
    }

    public void navigateToResultsActivity(Match match) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKeys.MATCH_LIST, Parcels.wrap(match));
        Intent intent = new Intent(this, MyResultsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}