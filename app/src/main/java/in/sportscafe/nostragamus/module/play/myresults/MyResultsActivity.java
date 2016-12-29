package in.sportscafe.nostragamus.module.play.myresults;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.animator.AnimationAdapter;
import in.sportscafe.nostragamus.animator.SlideInUpAnimationAdapter;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.play.myresults.flipPowerup.FlipActivity;
import in.sportscafe.nostragamus.module.play.myresultstimeline.MyResultsTimelineActivity;
import in.sportscafe.nostragamus.module.play.prediction.PredictionActivity;
import in.sportscafe.nostragamus.utils.ViewUtils;

/**
 * Created by Jeeva on 15/6/16.
 */
public class MyResultsActivity extends NostragamusActivity implements MyResultsView,View.OnClickListener {

    private RecyclerView mRvMyResults;

    private MyResultsPresenter mResultsPresenter;

    private boolean mReplaypowerUpApplied = false;
    private boolean mFlippowerUpApplied = false;

    private Button btnReplayPowerUpCount;
    private Button btnFlipPowerUpCount;

    private Toolbar mtoolbar;
    private TextView mTitle;
    private boolean goback = false;
    private Boolean isFabOpen = false;
    private FloatingActionButton powerupMainFab,powerupReplayFab,powerupFlipFab;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    private Bundle matchBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_results);

        initToolBar();

        this.mRvMyResults = (RecyclerView) findViewById(R.id.my_results_rv);

        //POWERUPFAB ICONS
        btnReplayPowerUpCount=(Button) findViewById(R.id.powerup_replay_count);
        btnFlipPowerUpCount=(Button)findViewById(R.id.powerup_flip_count);
        powerupMainFab = (FloatingActionButton)findViewById(R.id.fab_main);
        powerupReplayFab = (FloatingActionButton)findViewById(R.id.fab_replay);
        powerupFlipFab = (FloatingActionButton)findViewById(R.id.fab_flip);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.powerup_fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.powerup_fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.powerup_fab_rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.powerup_fab_rotate_backward);
        powerupMainFab.setOnClickListener(this);
        powerupReplayFab.setOnClickListener(this);
        powerupFlipFab.setOnClickListener(this);


        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);

        // It is to find the scrolling stage to do the pagination
        this.mRvMyResults.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mResultsPresenter.onArticleScroll(linearLayoutManager.findFirstVisibleItemPosition(),
                        linearLayoutManager.getChildCount(), linearLayoutManager.getItemCount());
            }
        });
        this.mRvMyResults.setLayoutManager(linearLayoutManager);
        this.mRvMyResults.setHasFixedSize(true);

        this.mResultsPresenter = MyResultPresenterImpl.newInstance(this);
        this.mResultsPresenter.onCreateMyResults(getIntent().getExtras());

        Bundle mbundle = new Bundle();
        mbundle = getIntent().getExtras();
        Match match = (Match) mbundle.getSerializable(Constants.BundleKeys.MATCH_LIST);
        if (match.getMatchPoints()!=0){
            powerupMainFab.setVisibility(View.GONE);
        }else {
            powerupMainFab.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void setAdapter(MyResultsAdapter myResultsAdapter) {
        mRvMyResults.setAdapter(getAnimationAdapter(myResultsAdapter));
    }

    private AnimationAdapter getAnimationAdapter(RecyclerView.Adapter adapter) {
        SlideInUpAnimationAdapter animationAdapter = new SlideInUpAnimationAdapter(adapter);
        animationAdapter.setFirstOnly(true);
        animationAdapter.setDuration(750);
        animationAdapter.setInterpolator(new DecelerateInterpolator(1f));
        return animationAdapter;
    }

    public void onBack(View view) {
        onBackPressed();
    }

    public void initToolBar() {
        Typeface tftitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato/Lato-Regular.ttf");
        mtoolbar = (Toolbar) findViewById(R.id.my_results_toolbar);
        mTitle = (TextView) mtoolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Match Result");
        mTitle.setTypeface(tftitle);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (goback == true) {
                            onBackPressed();
                        } else {
                            gotoMyResultsTimeline();
                        }
                    }
                }

        );
    }

    @Override
        public void goBack(){
        goback=true;
    }

    private void gotoMyResultsTimeline() {
        Intent intent = new Intent(this, MyResultsTimelineActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fab_main:
                animateFAB();
                break;

            case R.id.fab_replay:
                if (!mFlippowerUpApplied || !mReplaypowerUpApplied ) {
                    if (NostragamusDataHandler.getInstance().getNumberofReplayPowerups() > 0) {
                        mFlippowerUpApplied = true;
                        mReplaypowerUpApplied = true;
                        mResultsPresenter.onPowerUp("match_replay");
                    }else {
                        Toast toast = Toast.makeText(this,Constants.Alerts.REPLAY_POWERUP_OVER, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            case R.id.fab_flip:
                if (!mFlippowerUpApplied || !mReplaypowerUpApplied ) {
                   if (NostragamusDataHandler.getInstance().getNumberofFlipPowerups() > 0) {
                    mFlippowerUpApplied = true;
                    mReplaypowerUpApplied = true;
                    navigatetoFlipScreen();
                   }else {
                       Toast toast = Toast.makeText(this,Constants.Alerts.FLIP_POWERUP_OVER, Toast.LENGTH_LONG);
                       toast.setGravity(Gravity.CENTER, 0, 0);
                       toast.show();
                   }
                }
                break;

        }
    }

    private void navigatetoFlipScreen() {
        Intent mintent =  new Intent(this, FlipActivity.class);
        mintent.putExtras(matchBundle);
        startActivity(mintent);
    }

    @Override
    public void setNumberofPowerups(int numberofReplayPowerups, int numberofFlipPowerups) {

        if (numberofReplayPowerups < 1) {
            mReplaypowerUpApplied = true;
        }
        else if (numberofFlipPowerups < 1){
            mFlippowerUpApplied=true;
        }

        btnReplayPowerUpCount.setText(String.valueOf(numberofReplayPowerups));
        btnFlipPowerUpCount.setText(String.valueOf(numberofFlipPowerups));

    }

    @Override
    public void setMatchDetails(Match match) {
        matchBundle = new Bundle();
        matchBundle.putSerializable(Constants.BundleKeys.MATCH_LIST, match);
    }

    @Override
    public void openReplayDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You want to apply Replay Powerup?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                NostragamusDataHandler.getInstance().setNumberofReplayPowerups(NostragamusDataHandler.getInstance().getNumberofReplayPowerups() - 1);
                                String updatedPowerUps = String.valueOf(NostragamusDataHandler.getInstance().getNumberofReplayPowerups() - 1);
                                btnReplayPowerUpCount.setText(updatedPowerUps);
                                mResultsPresenter.onReplayPowerupApplied();
                                animateFAB();
                            }
                        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
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
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(Constants.BundleKeys.MATCH_LIST, match);
        Intent intent =  new Intent(this, PredictionActivity.class);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    @Override
    public void openFlipDialog() {
        String flipPowerUps = String.valueOf(NostragamusDataHandler.getInstance().getNumberofFlipPowerups());
        btnFlipPowerUpCount.setText(flipPowerUps);
        mResultsPresenter.onFlipPowerupApplied();
        animateFAB();
    }

    public void animateFAB(){

        if(isFabOpen){
            powerupMainFab.startAnimation(rotate_backward);
            powerupMainFab.setImageResource(R.drawable.powerup_main_icon_white);
            powerupReplayFab.startAnimation(fab_close);
            powerupFlipFab.startAnimation(fab_close);
            powerupReplayFab.setClickable(false);
            powerupFlipFab.setClickable(false);
            btnReplayPowerUpCount.setVisibility(View.GONE);
            btnFlipPowerUpCount.setVisibility(View.GONE);
            isFabOpen = false;

        } else {

            powerupMainFab.startAnimation(rotate_forward);
            powerupMainFab.setImageResource(R.drawable.powerup_main_icon);
            powerupReplayFab.startAnimation(fab_open);
            powerupFlipFab.startAnimation(fab_open);
            powerupReplayFab.setClickable(true);
            powerupFlipFab.setClickable(true);
            btnReplayPowerUpCount.setVisibility(View.VISIBLE);
            btnFlipPowerUpCount.setVisibility(View.VISIBLE);
            isFabOpen = true;

        }
    }

    @Override
    public void showResultShare(String matchResult, int matchPoints) {
        Bitmap screenshot = ViewUtils.viewToBitmap(
                mRvMyResults,
                mRvMyResults.getWidth(),
                mRvMyResults.computeVerticalScrollRange()
        );

        if(null != screenshot) {
            Resources resources = getResources();
            int delta = resources.getDimensionPixelSize(R.dimen.dp_10);

            // cutting top part
            screenshot = ViewUtils.cutPartOfBitmap(screenshot, new Rect(
                    0,
                    findViewById(R.id.schedule_row_ll).getTop() + delta,
                    screenshot.getWidth(),
                    findViewById(R.id.my_results_row_ll_predictions).getBottom() + delta)
            );

            final ViewGroup parent = (ViewGroup) findViewById(R.id.for_screenshot);
            final View sharePhoto = getLayoutInflater().inflate(R.layout.inflater_my_result_share_holder, parent, false);
            parent.addView(sharePhoto);

            ((TextView) sharePhoto.findViewById(R.id.my_results_share_tv_content)).setText(String.format(
                    resources.getString(R.string.fb_share_result_text),
                    matchResult,
                    matchPoints
            ));

            sharePhoto.findViewById(R.id.my_results_share_iv_screenshot)
                    .setBackground(new BitmapDrawable(resources, screenshot));

            sharePhoto.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    sharePhoto.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    Bitmap screenshot = ViewUtils.viewToBitmap(sharePhoto, sharePhoto.getWidth(), sharePhoto.getHeight());

                    File screenshotFile = ViewUtils.saveBitmap(screenshot);
                    AppSnippet.doGeneralImageShare(MyResultsActivity.this, screenshotFile);

                    parent.removeAllViews();
                }
            });

        }
    }


    public void takeScreenshot(View view) {
        mResultsPresenter.onClickFbShare();
    }
}