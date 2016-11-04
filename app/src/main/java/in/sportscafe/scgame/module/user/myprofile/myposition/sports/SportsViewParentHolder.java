package in.sportscafe.scgame.module.user.myprofile.myposition.sports;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.jeeva.android.Log;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.TournamentFeed.dto.TournamentInfo;
import in.sportscafe.scgame.module.common.Ordinal;
import in.sportscafe.scgame.module.feed.FeedActivity;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.BaseSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.RankSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.SportSummary;
import in.sportscafe.scgame.module.user.myprofile.myposition.dto.TourSummary;
import in.sportscafe.scgame.module.user.points.PointsActivity;

/**
 * Created by deepanshi on 9/21/16.
 */

public class SportsViewParentHolder extends ParentViewHolder {

        private static final float INITIAL_POSITION = 0.0f;
        private static final float ROTATED_POSITION = 180f;


        private final ImageView mArrowExpandImageView;
        private TextView mSportsNameTextView;
        private TextView mSportsOverallRankTextView;
        private ImageView mSportsOverallRankStatus;


        public SportsViewParentHolder(View itemView) {
            super(itemView);
            mSportsNameTextView = (TextView) itemView.findViewById(R.id.myglobal_sport_tv_name);
            mSportsOverallRankTextView = (TextView) itemView.findViewById(R.id.myglobal_sport_tv_overall_rank);
            mArrowExpandImageView = (ImageView) itemView.findViewById(R.id.arrow_expand_imageview);
            mSportsOverallRankStatus = (ImageView) itemView.findViewById(R.id.myglobal_sport_rank_status);

        }

        public void bind(BaseSummary tourSummary) {

            mSportsNameTextView.setText(tourSummary.getName());

            if(null == tourSummary.getOverallRankChange()) {
                mSportsOverallRankStatus.setVisibility(View.INVISIBLE);
            } else if(tourSummary.getRankChange() < 0) {
                mSportsOverallRankStatus.setImageResource(R.drawable.status_arrow_down);
            } else {
                mSportsOverallRankStatus.setImageResource(R.drawable.status_arrow_up);
            }


            if(null == tourSummary.getRank()) {
                mSportsOverallRankTextView.setText("-");
            } else {
                String rank = Ordinal.ordinal(tourSummary.getRank());
                mSportsOverallRankTextView.setText(rank);
            }
        }

        @SuppressLint("NewApi")
        @Override
        public void setExpanded(boolean expanded) {
            super.setExpanded(expanded);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                if (expanded) {
                    mArrowExpandImageView.setRotation(ROTATED_POSITION);
                } else {
                    mArrowExpandImageView.setRotation(INITIAL_POSITION);
                }
            }
        }

        @Override
        public void onExpansionToggled(boolean expanded) {
            super.onExpansionToggled(expanded);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                RotateAnimation rotateAnimation;
                if (expanded) { // rotate clockwise
                    rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                            INITIAL_POSITION,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                } else { // rotate counterclockwise
                    rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                            INITIAL_POSITION,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                }

                rotateAnimation.setDuration(200);
                rotateAnimation.setFillAfter(true);
                mArrowExpandImageView.startAnimation(rotateAnimation);
            }
        }


    }


