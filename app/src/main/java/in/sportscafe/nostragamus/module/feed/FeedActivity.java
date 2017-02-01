package in.sportscafe.nostragamus.module.feed;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    private static final float MAX_ROTATION = 30;

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
        this.mRcvFeed.setHasFixedSize(true);
        this.mFeedPresenter = FeedPresenterImpl.newInstance(this);
        this.mFeedPresenter.onCreateFeed(getIntent().getExtras());

        bundle = getIntent().getExtras();

        initRotation();
    }

    private void initRotation() {
        mRcvFeed.post(new Runnable() {
            @Override
            public void run() {
                mVisibleHeight = findViewById(R.id.content).getMeasuredHeight();
                mHalfVisibleHeight = getResources().getDimensionPixelSize(R.dimen.dp_150);
                mDifference = mVisibleHeight - mHalfVisibleHeight;
            }
        });

        mRcvFeed.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                View child = null;
                int[] location = new int[2];
                int yAxis;
                int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    child = parent.getChildAt(i).findViewById(R.id.schedule_row_ll);

                    if (child.getVisibility() == View.VISIBLE) {
                        child.setPivotY(child.getMeasuredHeight());
                        child.getLocationOnScreen(location);
                        child.setRotationX(getRotationByY(location[1]));

//                        getRotationWidth(child.getMeasuredWidth(), rotation);

                    }
                }
                super.onDraw(c, parent, state);
            }
        });
    }

    private float getRotationByY(int yAxis) {
        float rotation = MAX_ROTATION * (yAxis - mHalfVisibleHeight) / mDifference;
        if (rotation < 0) {
            return 0;
        }
        return rotation;
    }

    @Override
    public void setAdapter(TimelineAdapter feedAdapter) {
        mRcvFeed.setAdapter(ViewUtils.getAnimationAdapter(feedAdapter));
    }

    @Override
    public void moveAdapterPosition(int movePosition) {
        mRcvFeed.getLayoutManager().scrollToPosition(movePosition);
    }

    @Override
    public void initToolBar(Integer powerUp2x, Integer powerUpNonEgs, Integer powerUpAudiencePoll) {
        mtoolbar = (Toolbar) findViewById(R.id.feed_toolbar);
        TextView tournamentName = (TextView) mtoolbar.findViewById(R.id.feed_tv_tournament_name);
        mIv2xPowerup = (ImageView) mtoolbar.findViewById(R.id.powerups_iv_2x);
        mIvNonegsPowerup = (ImageView) mtoolbar.findViewById(R.id.powerups_iv_nonegs);
        mIvPollPowerup = (ImageView) mtoolbar.findViewById(R.id.powerups_iv_poll);
        mTv2xPowerupCount = (TextView) mtoolbar.findViewById(R.id.powerup_tv_2x_count);
        mTvNonegsPowerupCount = (TextView) mtoolbar.findViewById(R.id.powerup_tv_nonegs_count);
        mTvPollPowerupCount = (TextView) mtoolbar.findViewById(R.id.powerup_tv_poll_count);

        mTv2xPowerupCount.setText(powerUp2x.toString());
        mTvNonegsPowerupCount.setText(powerUpNonEgs.toString());
        mTvPollPowerupCount.setText(powerUpAudiencePoll.toString());

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
}