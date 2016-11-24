package in.sportscafe.scgame.module.user.myprofile.myposition.groups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.GroupsTourSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.TourSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.sports.SportsLayout;

/**
 * Created by deepanshi on 10/13/16.
 */

public class GroupsLayout  extends FrameLayout {

    private GroupsTourSummary mtourSummary;

    private GroupsLayout.OnRankClickListener mOnRankClickListener;

    public GroupsLayout(Context context, GroupsTourSummary groupsTourSummary, GroupsLayout.OnRankClickListener myPositionClickListener) {
        super(context);
        this.mtourSummary = groupsTourSummary;
        mOnRankClickListener = myPositionClickListener;
        init();
    }

    private void init() {
        addView(LayoutInflater.from(getContext()).inflate(R.layout.inflater_my_position_row, this, false));
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
        ((TextView) findViewById(R.id.my_position_row_tv_sport))
                .setText(mtourSummary.getTournamentName());
//
//        ((ImageView) findViewById(R.id.my_position_row_img_sport))
//                .setImageResource(mRankSummary.get());

        ImageView ivRankStatus = (ImageView) findViewById(R.id.my_position_row_iv_rank_status);
        if(null == mtourSummary.getRankChange()) {
            ivRankStatus.setVisibility(View.INVISIBLE);
        } else if(mtourSummary.getRankChange() < 0) {
            ivRankStatus.setImageResource(R.drawable.status_arrow_down);
        } else {
            ivRankStatus.setImageResource(R.drawable.status_arrow_up);
        }

        TextView tvRank =(TextView) findViewById(R.id.my_position_row_tv_rank);
        if(null == mtourSummary.getRank()) {
            tvRank.setVisibility(INVISIBLE);
            findViewById(R.id.my_position_row_tv_rank_label).setVisibility(INVISIBLE);
        } else {
            tvRank.setText(String.valueOf(mtourSummary.getRank()));
        }
    }

    public interface OnRankClickListener {

        void onClickRank(GroupsTourSummary groupsTourSummary);
    }
}