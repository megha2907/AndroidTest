package in.sportscafe.nostragamus.module.feed;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.newChallenges.ui.NewChallengesActivity;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelineAdapter;
import in.sportscafe.nostragamus.utils.ViewUtils;

/**
 * Created by Jeeva on 15/6/16.
 */
public class FeedActivity extends NostragamusActivity implements FeedView {

    private static final float MAX_ROTATION = 15f;

    private float mTopScheduleMargin;

    private float mVisibleHeight;

    private float mHalfVisibleHeight;

    private float mDifference;

    private RecyclerView mRcvFeed;

    private FeedPresenter mFeedPresenter;

    private ImageView mIv2xPowerup;

    private ImageView mIvNonegsPowerup;

    private ImageView mIvPollPowerup;

    private TextView mTv2xPowerupCount;

    private TextView mTvNonegsPowerupCount;

    private TextView mTvPollPowerupCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        initToolBar();

        this.mRcvFeed = (RecyclerView) findViewById(R.id.feed_rv);
        this.mRcvFeed.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        this.mRcvFeed.setHasFixedSize(true);

        this.mFeedPresenter = FeedPresenterImpl.newInstance(this);
        this.mFeedPresenter.onCreateFeed(getIntent().getExtras());

        initRotation();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mFeedPresenter.onRefresh();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.feed_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setNavigationIcon(R.drawable.back_icon_grey);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                }
        );
        toolbar.setContentInsetStartWithNavigation(0);
    }

    private void initRotation() {
        mRcvFeed.post(new Runnable() {
            @Override
            public void run() {
                mTopScheduleMargin = getResources().getDimensionPixelSize(R.dimen.dp_45);
                mVisibleHeight = mRcvFeed.getMeasuredHeight();
                mHalfVisibleHeight = getResources().getDimensionPixelSize(R.dimen.dp_220);
                mDifference = mVisibleHeight - mHalfVisibleHeight;

                Log.d("FeedActivity", "mVisibleHeight --> " + mVisibleHeight);
                Log.d("FeedActivity", "mHalfVisibleHeight -->" + mHalfVisibleHeight);
                Log.d("FeedActivity", "mDifference -->" + mDifference);
            }
        });

        mRcvFeed.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                View child;
                View dateView;
                RelativeLayout.LayoutParams layoutParams;
                int[] location = new int[2];
                float maxHeight;
                float rotation;
                int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    child = parent.getChildAt(i);
                    dateView = child.findViewById(R.id.schedule_row_tv_date);
                    child = child.findViewById(R.id.schedule_row_ll);

                    if (child.getVisibility() == View.VISIBLE) {
                        maxHeight = child.getMeasuredHeight();
                        child.setPivotY(maxHeight);
                        child.getLocationOnScreen(location);

                        rotation = getRotationByPercent((location[1] - mHalfVisibleHeight) / mDifference);
                        child.setRotationX(rotation);

                        /*layoutParams = (RelativeLayout.LayoutParams) child.getLayoutParams();
                        layoutParams.topMargin = (int) (mTopScheduleMargin - (maxHeight - getHeightByRotation(maxHeight, rotation)));
                        child.setLayoutParams(layoutParams);*/
                    }
                }
                super.onDraw(c, parent, state);
            }
        });
    }

    private float getRotationByPercent(float percent) {
        float rotation = MAX_ROTATION * percent;
        Log.d("FeedActivity", "maxRotation --> " + MAX_ROTATION + ", calcRotation --> " + rotation);
        if (rotation < 0) {
            return 0;
        }
        return rotation;
    }

    private float getHeightByRotation(float maxHeight, float rotation) {
        float calcHeight = maxHeight * (1f - rotation / 75f);
        Log.d("FeedActivity", "maxHeight --> " + maxHeight + ", calcHeight --> " + calcHeight);
        if (calcHeight < 0 || calcHeight > maxHeight) {
            return maxHeight;
        }
        return calcHeight;
    }

    @Override
    public void showPowerups(Integer powerUp2x, Integer powerUpNonEgs, Integer powerUpAudiencePoll, String powerupText) {
        findViewById(R.id.feed_rl_powerup_layout).setVisibility(View.VISIBLE);

        mTv2xPowerupCount = (TextView) findViewById(R.id.powerup_tv_2x_count);
        mTvNonegsPowerupCount = (TextView) findViewById(R.id.powerup_tv_nonegs_count);
        mTvPollPowerupCount = (TextView) findViewById(R.id.powerup_tv_poll_count);

        mTv2xPowerupCount.setText(powerUp2x.toString());
        mTvNonegsPowerupCount.setText(powerUpNonEgs.toString());
        mTvPollPowerupCount.setText(powerUpAudiencePoll.toString());

        mIv2xPowerup = (ImageView) findViewById(R.id.powerups_iv_2x);
        mIvNonegsPowerup = (ImageView) findViewById(R.id.powerups_iv_nonegs);
        mIvPollPowerup = (ImageView) findViewById(R.id.powerups_iv_poll);

        mIv2xPowerup.setBackground(getPowerupDrawable(R.color.greencolor));
        mIvNonegsPowerup.setBackground(getPowerupDrawable(R.color.radical_red));
        mIvPollPowerup.setBackground(getPowerupDrawable(R.color.dodger_blue));

        TextView poweruptext = (TextView) findViewById(R.id.feed_tv_tournament_matches_left);
        poweruptext.setText(powerupText);
    }

    @Override
    public void hidePowerups() {
        findViewById(R.id.feed_rl_powerup_layout).setVisibility(View.GONE);
    }

    @Override
    public void setAdapter(TimelineAdapter feedAdapter) {
        mRcvFeed.setAdapter(ViewUtils.getAnimationAdapter(feedAdapter));
    }

    @Override
    public void moveAdapterPosition(int movePosition) {
        mRcvFeed.scrollToPosition(movePosition);
    }

    @Override
    public void setTournamentName(String tournamentName) {
        ((TextView) findViewById(R.id.feed_tv_tournament_name)).setText(tournamentName);
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(this, NewChallengesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void goBack() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        mFeedPresenter.onBack();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFeedPresenter.onDestroy();
    }

    private Drawable getPowerupDrawable(int colorRes) {
        GradientDrawable powerupDrawable = new GradientDrawable();
        powerupDrawable.setShape(GradientDrawable.RECTANGLE);
        powerupDrawable.setCornerRadius(getResources().getDimensionPixelSize(R.dimen.dp_5));
        powerupDrawable.setColor(ViewUtils.getColor(getContext(), colorRes));
        return powerupDrawable;
    }

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.TOURNAMENTS_TIMELINE;
    }
}