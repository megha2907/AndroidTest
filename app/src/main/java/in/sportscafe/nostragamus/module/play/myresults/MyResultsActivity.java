package in.sportscafe.nostragamus.module.play.myresults;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeeva.android.facebook.FacebookHandler;

import org.parceler.Parcels;

import java.io.File;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.IntentActions;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsChecker;
import in.sportscafe.nostragamus.module.play.myresults.flipPowerup.FlipActivity;
import in.sportscafe.nostragamus.module.play.prediction.PredictionActivity;
import in.sportscafe.nostragamus.utils.ViewUtils;

/**
 * Created by Jeeva on 15/6/16.
 */
public class MyResultsActivity extends NostragamusActivity implements MyResultsView, View.OnClickListener {

    private RecyclerView mRvMyResults;

    private MyResultsPresenter mResultsPresenter;

    private boolean mReplaypowerUpApplied = false;
    private boolean mFlippowerUpApplied = false;

    private Button btnReplayPowerUpCount;
    private Button btnFlipPowerUpCount;

    private Toolbar mtoolbar;
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

    private float offset1;
    private float offset2;

    private float offset3;

    private Bundle matchBundle;

    PermissionsChecker checker;
    LinearLayoutManager mlayoutManager;

    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_results);

        initToolBar();
        checker = new PermissionsChecker(this);

        mlayoutManager = new LinearLayoutManager(this);
        this.mRvMyResults = (RecyclerView) findViewById(R.id.my_results_rv);
        this.mRvMyResults.setLayoutManager(mlayoutManager);


        shareResultsBtn = (Button) findViewById(R.id.my_results_ll_share_score);
        shareResultsBtn.setOnClickListener(this);

        /*shareFab = (FloatingActionButton)findViewById(R.id.fab_share);
        btnfbShare =findViewById(R.id.fab_fb);
        shareContainer = findViewById(R.id.fab_container_share);


        shareFab.setOnClickListener(this);
        btnfbShare.setOnClickListener(this);*/

        //POWERUPFAB ICONS
        btnReplayPowerUpCount = (Button) findViewById(R.id.powerup_replay_count);
        btnFlipPowerUpCount = (Button) findViewById(R.id.powerup_flip_count);
        powerupMainFab = (ImageButton) findViewById(R.id.results_fab_main);

        powerupReplayFab = findViewById(R.id.fab_fl_replay);
        powerupFlipFab = findViewById(R.id.fab_fl_flip);
        fabContainer = findViewById(R.id.fab_container);

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

        /*shareContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                shareContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                float mainY = shareFab.getY();

                offset3 = mainY - btnfbShare.getY();
                btnfbShare.setTranslationY(offset3);
                return true;
            }
        });*/


        this.mRvMyResults.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        this.mRvMyResults.setHasFixedSize(true);

        this.mResultsPresenter = MyResultPresenterImpl.newInstance(this);
        this.mResultsPresenter.onCreateMyResults(getIntent().getExtras());


        //COMMENT REPLAY AND FLIP POWERUPS FOR NOW

        powerupMainFab.setVisibility(View.GONE);

       /* Bundle mbundle = new Bundle();
        mbundle = getIntent().getExtras();
        Match match = Parcels.unwrap(mbundle.getParcelable(Constants.BundleKeys.MATCH_LIST));

        long startTimeMs = TimeUtils.getMillisecondsFromDateString(
                match.getStartTime(),
                Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                Constants.DateFormats.GMT
        );

        TimeAgo timeAgo = TimeUtils.calcTimeAgo(Calendar.getInstance().getTimeInMillis(), startTimeMs);
        boolean isMatchStarted = timeAgo.timeDiff <= 0
                || timeAgo.timeUnit == TimeUnit.MILLISECOND
                || timeAgo.timeUnit == TimeUnit.SECOND;


        if (match.isResultPublished() || isMatchStarted){
            powerupMainFab.setVisibility(View.GONE);
        }else {
            powerupMainFab.setVisibility(View.VISIBLE);
        } */




        /*this.mRvMyResults.addOnScrollListener(new RecyclerView.OnScrollListener()
        {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if (dy < 0) {
                    shareFab.show();

                    if (isShareFabOpen) {
                        btnfbShare.setVisibility(View.VISIBLE);
                    }

                    // Recycle view scrolling up...

                } else if (dy > 0) {
                    shareFab.hide();

                    if (isShareFabOpen) {
                        btnfbShare.setVisibility(View.INVISIBLE);
                    }
                    // Recycle view scrolling down...
                }


            }

        });*/


    }

    @Override
    public void setAdapter(MyResultsAdapter myResultsAdapter) {
        mRvMyResults.setAdapter(myResultsAdapter);
    }

    public void onBack(View view) {
        onBackPressed();
    }

    public void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.my_results_toolbar);
        mTitle = (TextView) mtoolbar.findViewById(R.id.toolbar_title);
        mMatchStage = (TextView) mtoolbar.findViewById(R.id.toolbar_match_stage);
        mTitle.setText("Match Result");
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setContentInsetStartWithNavigation(2);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (goback == true) {
                            onBackPressed();
                        } else {
                            gotoHomeActivity();
                        }
                    }
                }

        );
    }

    @Override
    public void goBack() {
        goback = true;
    }

    /*private void gotoMyResultsTimeline() {
        Intent intent = new Intent(this, MyResultsTimelineActivity.class);
        startActivity(intent);
        finish();
    }*/

    private void gotoHomeActivity() {
        Intent homeIntent = new Intent(this, HomeActivity.class);
        startActivity(homeIntent);
        finish();
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
                broadcastShareScore(v.getContext());
                break;

            /*case R.id.fab_fb:

                break;*/

        }
    }


    private void broadcastShareScore(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(IntentActions.ACTION_SHARE_SCORE));
    }

    private void navigatetoFlipScreen() {
        Intent mintent = new Intent(this, FlipActivity.class);
        mintent.putExtras(matchBundle);
        startActivity(mintent);
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
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKeys.MATCH_LIST, Parcels.wrap(match));
        Intent intent = new Intent(this, PredictionActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
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
        Resources resources = getResources();
//        LinearLayout ShareRow = (LinearLayout) findViewById(R.id.my_results_ll_share_score);
//        int delta = ShareRow.getHeight();
        Bitmap screenshot = Bitmap.createBitmap(mRvMyResults.getWidth(), mRvMyResults.computeVerticalScrollRange(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(screenshot);
        mRvMyResults.layout(
                0,
                mRvMyResults.getHeight() - mRvMyResults.computeVerticalScrollRange(),
                mRvMyResults.getWidth(),
                mRvMyResults.computeVerticalScrollRange());

        mRvMyResults.draw(c);

        if (null != screenshot) {

            final ViewGroup parent = (ViewGroup) findViewById(R.id.for_screenshot);
            final View sharePhoto = getLayoutInflater().inflate(R.layout.inflater_my_result_share_holder, parent, false);
            parent.addView(sharePhoto);

            /*((TextView) sharePhoto.findViewById(R.id.my_results_share_tv_content)).setText(String.format(
                    resources.getString(R.string.fb_share_result_text),
                    matchResult,
                    matchPoints
            ));*/

            sharePhoto.findViewById(R.id.my_results_share_iv_screenshot)
                    .setBackground(new BitmapDrawable(resources, screenshot));

            sharePhoto.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    sharePhoto.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    Bitmap screenshot = ViewUtils.viewToBitmap(sharePhoto, sharePhoto.getWidth(), sharePhoto.getHeight());
                    File screenshotFile = AppSnippet.saveBitmap(screenshot, "fb");
                    parent.removeAllViews();
                    mResultsPresenter.onGetScreenShot(screenshotFile);
                }
            });

        }

//        doSomething(matchResult, matchPoints);
    }


    @Override
    public void showFbShare(String url) {
        FacebookHandler.getInstance(MyResultsActivity.this).share(MyResultsActivity.this, url);
    }

    @Override
    public void setMatchName(String matchName) {
        mMatchStage.setText(matchName);
    }

    @Override
    public void setToolbarHeading(String result) {
        mTitle.setText(result);
    }

    /*public void onClickbtnfbShare() {
        mResultsPresenter.onClickFbShare();
    }*/

    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(this, 0, permission);
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


    /*private void collapseShareFab() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createCollapseAnimator(btnfbShare, offset3));
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                btnfbShare.setVisibility(View.INVISIBLE);
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

    private void expandShareFab() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createExpandAnimator(btnfbShare, offset3));
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                btnfbShare.setVisibility(View.VISIBLE);
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
    }*/

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
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mShareScoreReceiver,
                new IntentFilter(IntentActions.ACTION_SHARE_SCORE));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mShareScoreReceiver);
    }

    BroadcastReceiver mShareScoreReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (checker.lacksPermissions(PERMISSIONS_READ_STORAGE)) {
                startPermissionsActivity(PERMISSIONS_READ_STORAGE);
            } else {
                mResultsPresenter.onClickFbShare();
            }
        }
    };

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.RESULTS;
    }
}