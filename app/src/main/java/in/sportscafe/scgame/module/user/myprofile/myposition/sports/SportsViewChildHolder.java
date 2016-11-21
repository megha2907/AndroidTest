package in.sportscafe.scgame.module.user.myprofile.myposition.sports;

/**
 * Created by deepanshi on 9/21/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.jeeva.android.volley.Volley;
import com.jeeva.android.widgets.HmImageView;

import in.sportscafe.scgame.AppSnippet;
import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.BaseSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.TourSummary;
import in.sportscafe.scgame.module.user.points.PointsActivity;


public class SportsViewChildHolder extends ChildViewHolder{

    private TextView mTournamentNameTextView;
    private TextView mTournamentRankTextView1;
    private ImageView mTournamentRankStatus;
    private HmImageView mTournamentPhotoImageView;
    private  View mChildView;


    public SportsViewChildHolder(View itemView) {
        super(itemView);
        mChildView = itemView;
        mTournamentNameTextView = (TextView) itemView.findViewById(R.id.myglobal_tournaments_tv);
        mTournamentRankTextView1 = (TextView) itemView.findViewById(R.id.myglobal_tournaments_tv_ranks);
        mTournamentRankStatus = (ImageView) itemView.findViewById(R.id.myglobal_tournaments_rank_status);
        mTournamentPhotoImageView =(HmImageView) itemView.findViewById(R.id.myglobal_tournaments_iv_photo);
    }

    public void bind(BaseSummary tourSummary) {

        mTournamentNameTextView.setText(tourSummary.getTournamentName());

        mTournamentPhotoImageView.setImageUrl(
                tourSummary.getTournamentImageUrl(),
                Volley.getInstance().getImageLoader(),
                false
        );
//
        if(null == tourSummary.getRankChange()) {
            mTournamentRankStatus.setVisibility(View.INVISIBLE);
        } else if(tourSummary.getRankChange() < 0) {
            mTournamentRankStatus.setImageResource(R.drawable.status_arrow_down);
        } else {
            mTournamentRankStatus.setImageResource(R.drawable.status_arrow_up);
        }


        if(null == tourSummary.getRank()) {
            mTournamentRankTextView1.setText("-");
        } else {
            String rank = AppSnippet.ordinal(tourSummary.getRank());
            mTournamentRankTextView1.setText(rank);
        }

        mChildView.setTag(tourSummary);
        mChildView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TourSummary tourSummary = (TourSummary) v.getTag();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BundleKeys.LEADERBOARD_KEY, "sport");
                bundle.putSerializable(Constants.BundleKeys.TOURNAMENT_SUMMARY,(TourSummary) v.getTag());
                bundle.putLong(Constants.BundleKeys.GROUP_ID,0);
                bundle.putInt(Constants.BundleKeys.SPORT_ID,tourSummary.getSportId());
                bundle.putInt(Constants.BundleKeys.CHALLENGE_ID,0);
                Intent intent =  new Intent(v.getContext(), PointsActivity.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }


}
