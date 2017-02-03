package in.sportscafe.nostragamus.module.user.lblanding;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;

/**
 * Created by deepanshi on 1/24/17.
 */

public class LBLandingRow extends LinearLayout implements View.OnClickListener {

    private LinearLayoutManager mLayoutManager;

    private ImageButton mLeftArrow;

    private ImageButton mRightArrow;

    private RecyclerView mRecyclerView;

    private LBLandingAdapter mLbLandingAdapter;

    public LBLandingRow(Context context) {
        this(context, null);
    }

    public LBLandingRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LBLandingRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(List<LbLanding> lbList, String lbLandingType, boolean needPadding) {
        ((TextView) findViewById(R.id.lblanding_tv_category_name)).setText(getCategoryName(lbLandingType));

        mRightArrow = (ImageButton) findViewById(R.id.lblanding_ib_right_arrow);
        mLeftArrow = (ImageButton) findViewById(R.id.lblanding_ib_left_arrow);
        mRightArrow.setOnClickListener(this);
        mLeftArrow.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.lblanding_rv);

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mLbLandingAdapter = new LBLandingAdapter(getContext(), lbLandingType, needPadding);
        mLbLandingAdapter.addAll(lbList);
        mRecyclerView.setAdapter(mLbLandingAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                updateArrowState();
            }

        });

        if(lbList.size() < 4) {
            mLeftArrow.setVisibility(View.INVISIBLE);
            mRightArrow.setVisibility(View.INVISIBLE);
        }
    }

    private void updateArrowState() {
        int position = mLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (position == 0) {
            mLeftArrow.setImageResource(R.drawable.leaderboard_grey_arrow_icon);
        } else if(position > 0) {
            mLeftArrow.setImageResource(R.drawable.leaderboard_white_arrow_icon);
        }

        position = mLayoutManager.findLastCompletelyVisibleItemPosition() + 1;
        if (position == mLbLandingAdapter.getItemCount()) {
            mRightArrow.setImageResource(R.drawable.leaderboard_grey_arrow_icon);
        } else if(position < mLbLandingAdapter.getItemCount()) {
            mRightArrow.setImageResource(R.drawable.leaderboard_white_arrow_icon);
        }
    }

    private String getCategoryName(String lbLandingType) {
        switch (lbLandingType) {
            case Constants.LBLandingType.SPORT:
                return getResources().getString(R.string.sports);
            case Constants.LBLandingType.GROUP:
                return getResources().getString(R.string.groups);
            case Constants.LBLandingType.CHALLENGE:
                return getResources().getString(R.string.challenges);
        }
        return "";
    }

    @Override
    public void onClick(View view) {
        int position;
        switch (view.getId()) {
            case R.id.lblanding_ib_right_arrow:
                position = mLayoutManager.findLastVisibleItemPosition() + 2;
                int lastPosition = mLbLandingAdapter.getItemCount() - 1;

                mRecyclerView.smoothScrollToPosition(position <= lastPosition ? position : lastPosition);
                break;
            case R.id.lblanding_ib_left_arrow:
                position = mLayoutManager.findFirstVisibleItemPosition() - 2;

                mRecyclerView.smoothScrollToPosition(position < 0 ? 0 : position);
                break;
        }
    }
}