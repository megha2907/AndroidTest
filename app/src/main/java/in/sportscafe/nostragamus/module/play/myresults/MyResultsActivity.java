package in.sportscafe.nostragamus.module.play.myresults;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.play.myresultstimeline.MyResultsTimelineActivity;
import in.sportscafe.nostragamus.module.play.prediction.PredictionActivity;

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


    }

    @Override
    public void setAdapter(MyResultsAdapter myResultsAdapter) {
        mRvMyResults.setAdapter(myResultsAdapter);
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
//                    if (NostragamusDataHandler.getInstance().getNumberof2xPowerups() > 0) {
                        mFlippowerUpApplied = true;
                        mReplaypowerUpApplied = true;
                        mResultsPresenter.onPowerUp("match_replay");
                    //}
                }
                break;

        }
    }

    @Override
    public void openDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You want to apply Replay Powerup?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                               // NostragamusDataHandler.getInstance().setNumberofReplayPowerups(NostragamusDataHandler.getInstance().getNumberofReplayPowerups() - 1);
                                //String updatedPowerUps = String.valueOf(NostragamusDataHandler.getInstance().getNumberofReplayPowerups() - 1);
                                //btnReplayPowerUpCount.setText(updatedPowerUps);
                                mResultsPresenter.onReplayPowerupApplied();
                                animateFAB();
                                Toast.makeText(MyResultsActivity.this,"You clicked yes button", Toast.LENGTH_LONG).show();
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


}