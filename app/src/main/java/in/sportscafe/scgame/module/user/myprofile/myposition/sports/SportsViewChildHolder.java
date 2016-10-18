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
import com.jeeva.android.Log;
import com.jeeva.android.volley.Volley;

import java.io.Serializable;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.TournamentFeed.dto.TournamentInfo;
import in.sportscafe.scgame.module.common.RoundImage;
import in.sportscafe.scgame.module.feed.FeedActivity;
import in.sportscafe.scgame.module.feed.dto.Match;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.BaseSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.RankSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.SportSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.TourSummary;
import in.sportscafe.scgame.module.user.points.PointsActivity;

import static android.content.ContentValues.TAG;


public class SportsViewChildHolder extends ChildViewHolder{

    private TextView mTournamentNameTextView;
    private TextView mTournamentRankTextView1;
    private ImageView mTournamentRankStatus;
    private RoundImage mTournamentPhotoImageView;
    private  View mChildView;


    public SportsViewChildHolder(View itemView) {
        super(itemView);
        mChildView = itemView;
        mTournamentNameTextView = (TextView) itemView.findViewById(R.id.myglobal_tournaments_tv);
        mTournamentRankTextView1 = (TextView) itemView.findViewById(R.id.myglobal_tournaments_tv_ranks);
        mTournamentRankStatus = (ImageView) itemView.findViewById(R.id.myglobal_tournaments_rank_status);
        mTournamentPhotoImageView =(RoundImage) itemView.findViewById(R.id.myglobal_tournaments_iv_photo);
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
            mTournamentRankTextView1.setVisibility(View.INVISIBLE);
        } else {
            mTournamentRankTextView1.setText(String.valueOf(tourSummary.getRank()));
        }

        mChildView.setTag(tourSummary);
        mChildView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TourSummary tourSummary = (TourSummary) v.getTag();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BundleKeys.LEADERBOARD_KEY, "sport");
                bundle.putSerializable(Constants.BundleKeys.TOURNAMENT_SUMMARY,(TourSummary) v.getTag());
                bundle.putInt(Constants.BundleKeys.SPORT_ID,tourSummary.getSportId());
                Intent intent =  new Intent(v.getContext(), PointsActivity.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

}
