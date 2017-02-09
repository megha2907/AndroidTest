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
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelineAdapter;
import in.sportscafe.nostragamus.utils.ViewUtils;

/**
 * Created by Jeeva on 15/6/16.
 */
public class FeedActivity extends NostragamusActivity implements FeedView {

    private static final float MAX_ROTATION = 10f;

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

    private Toolbar mtoolbar;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        this.mRcvFeed = (RecyclerView) findViewById(R.id.feed_rv);
        this.mRcvFeed.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
//        this.mRcvFeed.setHasFixedSize(true);
        this.mFeedPresenter = FeedPresenterImpl.newInstance(this);
        this.mFeedPresenter.onCreateFeed(getIntent().getExtras());

        bundle = getIntent().getExtras();

        initRotation();
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
                Log.d("FeedActivity",  "mHalfVisibleHeight -->" + mHalfVisibleHeight);
                Log.d("FeedActivity",  "mDifference -->" + mDifference);
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
                float percent;
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
                        Log.d("FeedActivity", "y --> " + location[1]);

                        percent = (location[1] - mHalfVisibleHeight) / mDifference;
                        Log.d("FeedActivity", "percent --> " + percent);
                        rotation = getRotationByPercent(percent);
                        child.setRotationX(rotation);

                        layoutParams = (RelativeLayout.LayoutParams) child.getLayoutParams();
                        layoutParams.topMargin = (int) (mTopScheduleMargin - (maxHeight - getHeightByRotation(maxHeight, rotation)));
                        child.setLayoutParams(layoutParams);
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
    public void setAdapter(TimelineAdapter feedAdapter) {
        mRcvFeed.setAdapter(ViewUtils.getAnimationAdapter(feedAdapter));
    }

    @Override
    public void moveAdapterPosition(int movePosition) {
        mRcvFeed.scrollToPosition(movePosition);
    }

    @Override
    public void initToolBar(Integer powerUp2x, Integer powerUpNonEgs, Integer powerUpAudiencePoll, String powerUpText) {


        mtoolbar = (Toolbar) findViewById(R.id.feed_toolbar);
        RelativeLayout powerUpRl = (RelativeLayout) findViewById(R.id.feed_rl_powerup_layout);
        TextView tournamentName = (TextView) mtoolbar.findViewById(R.id.feed_tv_tournament_name);
        TextView poweruptext = (TextView) mtoolbar.findViewById(R.id.feed_tv_tournament_matches_left);
        mIv2xPowerup = (ImageView) mtoolbar.findViewById(R.id.powerups_iv_2x);
        mIvNonegsPowerup = (ImageView) mtoolbar.findViewById(R.id.powerups_iv_nonegs);
        mIvPollPowerup = (ImageView) mtoolbar.findViewById(R.id.powerups_iv_poll);
        mTv2xPowerupCount = (TextView) mtoolbar.findViewById(R.id.powerup_tv_2x_count);
        mTvNonegsPowerupCount = (TextView) mtoolbar.findViewById(R.id.powerup_tv_nonegs_count);
        mTvPollPowerupCount = (TextView) mtoolbar.findViewById(R.id.powerup_tv_poll_count);

        powerUpRl.setVisibility(View.VISIBLE);
        mTv2xPowerupCount.setText(powerUp2x.toString());
        mTvNonegsPowerupCount.setText(powerUpNonEgs.toString());
        mTvPollPowerupCount.setText(powerUpAudiencePoll.toString());
        poweruptext.setText(powerUpText);

        tournamentName.setText(bundle.getString(Constants.BundleKeys.TOURNAMENT_NAME));
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                }

        );
        mtoolbar.setContentInsetStartWithNavigation(0);

        mIv2xPowerup.setBackground(getPowerupDrawable(R.color.greencolor));
        mIvNonegsPowerup.setBackground(getPowerupDrawable(R.color.radical_red));
        mIvPollPowerup.setBackground(getPowerupDrawable(R.color.dodger_blue));
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
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
        powerupDrawable.setColor(getResources().getColor(colorRes));
        return powerupDrawable;
    }

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.TOURNAMENTS_TIMELINE;
    }
}