package in.sportscafe.nostragamus.module.user.myprofile.myposition.groups;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.jeeva.android.volley.Volley;
import com.jeeva.android.widgets.HmImageView;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.BaseSummary;
import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.GroupsTourSummary;
import in.sportscafe.nostragamus.module.user.points.PointsActivity;

/**
 * Created by deepanshi on 10/13/16.
 */

public class GroupsViewChildHolder extends ChildViewHolder {

    private TextView mTournamentNameTextView;
    private TextView mTournamentRankTextView;
    private ImageView mTournamentRankStatus;
    private HmImageView mTournamentPhotoImageView;
    private  View mChildView;
    private static final int CODE_GROUP_INFO = 23;

    public GroupsViewChildHolder(View itemView) {
        super(itemView);
        mChildView = itemView;
        mTournamentNameTextView = (TextView) itemView.findViewById(R.id.groups_child_view_tournaments_tv_name);
        mTournamentRankTextView = (TextView) itemView.findViewById(R.id.groups_child_view_tournaments_tv_rank);
        mTournamentRankStatus = (ImageView) itemView.findViewById(R.id.groups_child_view_tournaments_rank_status);
        mTournamentPhotoImageView =(HmImageView) itemView.findViewById(R.id.groups_child_view_tournaments_iv_photo);
    }

    public void bind(BaseSummary groupsTourSummary) {

        mTournamentNameTextView.setText(groupsTourSummary.getTournamentName());

        mTournamentPhotoImageView.setImageUrl(
                groupsTourSummary.getTournamentImageUrl(),
                Volley.getInstance().getImageLoader(),
                false
        );

        if(null == groupsTourSummary.getRankChange()) {
            mTournamentRankStatus.setVisibility(View.INVISIBLE);
        } else if(groupsTourSummary.getRankChange() < 0) {
            mTournamentRankStatus.setImageResource(R.drawable.status_arrow_down);
        } else {
            mTournamentRankStatus.setImageResource(R.drawable.status_arrow_up);
        }


        if(null == groupsTourSummary.getRank()) {
            mTournamentRankTextView.setText("-");
        } else {
            String rank = AppSnippet.ordinal(groupsTourSummary.getRank());
            mTournamentRankTextView.setText(rank);
        }

        mChildView.setTag(groupsTourSummary);
        mChildView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GroupsTourSummary groupsTourSummary= (GroupsTourSummary) v.getTag();

                Bundle bundle = new Bundle();
                bundle.putString(Constants.BundleKeys.LEADERBOARD_KEY, "group");
                bundle.putInt(Constants.BundleKeys.SPORT_ID,0);
                bundle.putLong(Constants.BundleKeys.GROUP_ID,groupsTourSummary.getGroupId());
                bundle.putInt(Constants.BundleKeys.CHALLENGE_ID,0);
                bundle.putSerializable(Constants.BundleKeys.TOURNAMENT_SUMMARY, (GroupsTourSummary) v.getTag());
                Intent intent =  new Intent(v.getContext(), PointsActivity.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }
}

