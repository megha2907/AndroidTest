package in.sportscafe.scgame.module.user.myprofile.myposition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.RankSummary;

/**
 * Created by Jeeva on 13/6/16.
 */
public class MyPositionLayout extends FrameLayout {

    private RankSummary mRankSummary;

    private OnRankClickListener mOnRankClickListener;

    public MyPositionLayout(Context context, RankSummary rankSummary, OnRankClickListener myPositionClickListener) {
        super(context);
        this.mRankSummary = rankSummary;
        mOnRankClickListener = myPositionClickListener;
        init();
    }

    private void init() {
        addView(LayoutInflater.from(getContext()).inflate(R.layout.inflater_my_position_row, this, false));
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != mOnRankClickListener) {
                    mOnRankClickListener.onClickRank(mRankSummary);
                }
            }
        });

        populateRankDetails();
    }

    public void populateRankDetails() {
        ((TextView) findViewById(R.id.my_position_row_tv_sport))
                .setText(mRankSummary.getSportName());

        ImageView ivRankStatus = (ImageView) findViewById(R.id.my_position_row_iv_rank_status);
        if(null == mRankSummary.getRankChange()) {
            ivRankStatus.setVisibility(View.INVISIBLE);
        } else if(mRankSummary.getRankChange() < 0) {
            ivRankStatus.setImageResource(R.drawable.status_arrow_down);
        } else {
            ivRankStatus.setImageResource(R.drawable.status_arrow_up);
        }

        TextView tvRank =(TextView) findViewById(R.id.my_position_row_tv_rank);
        if(null == mRankSummary.getRank()) {
            tvRank.setVisibility(INVISIBLE);
            findViewById(R.id.my_position_row_tv_rank_label).setVisibility(INVISIBLE);
        } else {
            tvRank.setText(String.valueOf(mRankSummary.getRank()));
        }
    }

    public interface OnRankClickListener {

        void onClickRank(RankSummary rankSummary);
    }
}