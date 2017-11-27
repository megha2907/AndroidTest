package in.sportscafe.nostragamus.module.play.myresults;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AppPermissions;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.Notifications;
import in.sportscafe.nostragamus.Constants.RequestCodes;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.contest.contestDetailsAfterJoining.InplayContestDetailsActivity;
import in.sportscafe.nostragamus.module.contest.dto.ContestScreenData;
import in.sportscafe.nostragamus.module.contest.ui.ContestsActivity;
import in.sportscafe.nostragamus.module.contest.ui.DetailScreensLaunchRequest;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;
import in.sportscafe.nostragamus.module.inPlay.ui.ResultsScreenDataDto;
import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsChecker;
import in.sportscafe.nostragamus.module.popups.timerPopup.TimerFinishDialogHelper;
import in.sportscafe.nostragamus.module.prediction.playScreen.PredictionActivity;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayScreenDataDto;
import in.sportscafe.nostragamus.module.resultspeek.dto.Match;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by Jeeva on 15/6/16.
 */
public class MyResultsActivity extends NostragamusActivity implements MyResultsView, View.OnClickListener {

    private static final String TAG = MyResultsActivity.class.getSimpleName();

    private RecyclerView mRvMyResults;
    private MyResultsPresenter mResultsPresenter;
    private Toolbar mtoolbar;
    private Bundle mbundle = new Bundle();
    private TextView mTitle;
    private TextView mMatchStage;
    private Button shareResultsBtn;
    private RelativeLayout mRlshareResults;
    private TextView mTvLeaderBoardRank;
    private TextView mTvLeaderBoardTotalPlayers;
    private Bundle matchBundle;
    private LinearLayoutManager mlayoutManager;
    private boolean mIsResultAwaitingScreen = false;
    private ResultsScreenDataDto mResultScreenData;

    public interface LaunchedFrom {
        int IN_PLAY_SCREEN_MATCH_DID_NOT_PLAY = 115;
        int IN_PLAY_SCREEN_MATCH_CHECK_POINTS = 116;
        int IN_PLAY_SCREEN_MATCH_AWAITING_RESULTS = 117;
        int CHECK_RESULTS_NOTIFICATION = 118;
    }


    @Override
    public String getScreenName() {
        return Notifications.SCREEN_RESULTS;
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
        mTvLeaderBoardRank = (TextView) findViewById(R.id.schedule_row_tv_leaderboard_rank);
        mTvLeaderBoardTotalPlayers = (TextView) findViewById(R.id.schedule_row_tv_leaderboard_total_players);

        mRvMyResults.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRvMyResults.setHasFixedSize(true);

        mResultsPresenter = MyResultPresenterImpl.newInstance(this);
        mResultsPresenter.onCreateMyResults(getIntent().getExtras());

        /* If headless flow, show join contest layout */
        if (isHeadlessFlow() || isPlayingPseudoGame()) {
            LinearLayout declarationLayout = (LinearLayout) findViewById(R.id.my_results_declaration_rl);
            declarationLayout.setVisibility(View.GONE);

            LinearLayout cardLayout = (LinearLayout) findViewById(R.id.my_result_join_contest_layout);
            cardLayout.setVisibility(View.VISIBLE);

            Button btnJoinContest = (Button) findViewById(R.id.results_join_contest_button);
            btnJoinContest.setOnClickListener(this);

            NostragamusAnalytics.getInstance().trackScreenShown(Constants.AnalyticsCategory.RESULTS, Constants.AnalyticsClickLabels.PSEUDO_GAME_FLOW);

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
        Bundle args = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            args = getIntent().getExtras();
            if (args.containsKey(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST)) {
                int launchRequest = args.getInt(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST);

                if (launchRequest == (NostraHomeActivity.LaunchedFrom.SHOW_IN_PLAY)) {
                    finishStackAndLaunchMatchesInPlay();
                }
            }
        }

        super.onBackPressed();
    }

    private void finishStackAndLaunchMatchesInPlay() {
        Bundle args = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            args = getIntent().getExtras();
            if (args.containsKey(BundleKeys.PLAY_SCREEN_DATA)) {
                PlayScreenDataDto playScreenData = Parcels.unwrap(args.getParcelable(BundleKeys.PLAY_SCREEN_DATA));
                if (playScreenData != null) {
                    playScreenData.setShouldLaunchMatchesScreen(true);
                    com.jeeva.android.Log.d(TAG, "Should launch headless-matches OR inplayContestDetails screen activity");
                }

                Intent clearTaskIntent = new Intent(getApplicationContext(), NostraHomeActivity.class);
                clearTaskIntent.putExtra(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, NostraHomeActivity.LaunchedFrom.SHOW_IN_PLAY);
                clearTaskIntent.putExtras(args);
                clearTaskIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(clearTaskIntent);
            }
        }
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
            case R.id.my_results_ll_share_score:
                if (new PermissionsChecker(this).lacksPermissions(AppPermissions.STORAGE)) {
                    PermissionsActivity.startActivityForResult(this, RequestCodes.STORAGE_PERMISSION, AppPermissions.STORAGE);
                } else {
                    mResultsPresenter.onClickShare();
                }
                break;

            case R.id.results_join_contest_button:
                navigateToContestScreen();
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.RESULTS, Constants.AnalyticsClickLabels.JOIN_CONTEST);
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
            screenData.setPseudoRoomId(mResultScreenData.getRoomId());
            if (isHeadlessFlow()) {
                screenData.setHeadLessFlow(true);
            }
            if (isPlayingPseudoGame()) {
                screenData.setPseudoGameFlow(true);
            }

            Bundle args = new Bundle();
            args.putParcelable(Constants.BundleKeys.CONTEST_SCREEN_DATA, Parcels.wrap(screenData));
            args.putInt(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT, ContestsActivity.LaunchedFrom.RESULTS);

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

    @Override
    public void setNumberofPowerups(int numberofReplayPowerups, int numberofFlipPowerups) {

        /*if (numberofReplayPowerups < 1) {
            mReplaypowerUpApplied = true;
        } else if (numberofFlipPowerups < 1) {
            mFlippowerUpApplied = true;
        }

        btnReplayPowerUpCount.setText(String.valueOf(numberofReplayPowerups));
        btnFlipPowerUpCount.setText(String.valueOf(numberofFlipPowerups));*/
    }

    @Override
    public void setMatchDetails(Match match) {
        matchBundle = new Bundle();
        matchBundle.putParcelable(BundleKeys.MATCH_LIST, Parcels.wrap(match));
    }

    @Override
    public void openReplayDialog() {
        /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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
        alertDialog.show();*/
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
        /*String flipPowerUps = String.valueOf(NostragamusDataHandler.getInstance().getFlipPowerupsCount());
        btnFlipPowerUpCount.setText(flipPowerUps);
        mResultsPresenter.onFlipPowerupApplied();*/
    }

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
            if (match.getResult() != null && !match.getResult().isEmpty()) {
                if (null != match.getUserRank() && null != match.getCountPlayers() &&
                        !isPlayingPseudoGame() && !isHeadlessFlow()) {
                    mTvLeaderBoardRank.setText(AppSnippet.ordinal(match.getUserRank()));
                    mTvLeaderBoardTotalPlayers.setText(" / " + String.valueOf(match.getCountPlayers()));
                } else {
                    mTvLeaderBoardRank.setText("NA");
                }
            } else {
                mTvLeaderBoardRank.setText("NA");
            }
        }
    }

    public void refresh(Match match) {
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

    /*@Override
    public void showLoadingProgressBar() {
        if (getActivity() != null) {
            findViewById(R.id.myResultsProgressBarLayout).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoadingProgressBar() {
        if (getActivity() != null) {
            findViewById(R.id.myResultsProgressBarLayout).setVisibility(View.GONE);
        }
    }*/

}