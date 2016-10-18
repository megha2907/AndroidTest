package in.sportscafe.scgame.module.user.myprofile.myposition.challenges;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.Log;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.ChallengesSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.ChallengesTourSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.RankSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.TourSummary;

/**
 * Created by Deepanshi on 13/10/16.
 */
public class ChallengesLayout extends FrameLayout {

    private ChallengesTourSummary mtourSummary;

    private OnRankClickListener mOnRankClickListener;

    public ChallengesLayout(Context context, ChallengesTourSummary tourSummary, OnRankClickListener myPositionClickListener) {
        super(context);
        this.mtourSummary = tourSummary;
        mOnRankClickListener = myPositionClickListener;
        init();
    }

    private void init() {
        addView(LayoutInflater.from(getContext()).inflate(R.layout.inflater_challenges_row, this, false));
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != mOnRankClickListener) {
                    mOnRankClickListener.onClickRank(mtourSummary);
                }
            }
        });

        populateRankDetails();
    }

    public void populateRankDetails() {
//        ((TextView) findViewById(R.id.challenges_tv_sport))
//                .setText(mRankSummary.getSportName());

        ImageView ivRankStatus = (ImageView) findViewById(R.id.challenges_row_iv_rank_status);
        if(null == mtourSummary.getRankChange()) {
            ivRankStatus.setVisibility(View.INVISIBLE);
        } else if(mtourSummary.getRankChange() < 0) {
            ivRankStatus.setImageResource(R.drawable.status_arrow_down);
        } else {
            ivRankStatus.setImageResource(R.drawable.status_arrow_up);
        }

        TextView tvRank =(TextView) findViewById(R.id.challenges_row_tv_rank);
        if(null == mtourSummary.getRank()) {
            tvRank.setVisibility(INVISIBLE);
            findViewById(R.id.challenges_tv_rank_label).setVisibility(INVISIBLE);
        } else {
            tvRank.setText(String.valueOf(mtourSummary.getRank()));
        }
    }

    public interface OnRankClickListener {

        void onClickRank(ChallengesTourSummary tourSummary);
    }
}