package in.sportscafe.nostragamus.module.user.leaderboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.common.RoundImage;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;
import in.sportscafe.nostragamus.module.user.myprofile.UserProfileActivity;
import in.sportscafe.nostragamus.module.user.playerprofile.PlayerProfileActivity;
import in.sportscafe.nostragamus.utils.ViewUtils;

/**
 * Created by Jeeva on 10/6/16.
 */
public class LeaderBoardAdapter extends Adapter<UserLeaderBoard, LeaderBoardAdapter.ViewHolder> {

    private Integer mChallengeId;

    private Integer mSelectedPos = 0;

    public LeaderBoardAdapter(Context context, Integer challengeId) {
        super(context);
        mChallengeId = challengeId;
    }

    @Override
    public UserLeaderBoard getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_leaderboard_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        UserLeaderBoard userLeaderBoard = getItem(position);

        holder.itemView.setTag(userLeaderBoard.getUserId());

        if (null != userLeaderBoard.getRankChange()) {
            if (userLeaderBoard.getRankChange() < 0) {
                holder.mIvStatus.setImageResource(R.drawable.status_arrow_down);
            } else {
                holder.mIvStatus.setImageResource(R.drawable.status_arrow_up);
            }
        }


        if (null == userLeaderBoard.getRank()) {
            holder.mTvRank.setText("-");
        } else {
//            String rank = AppSnippet.ordinal(userLeaderBoard.getRank());
            holder.mTvRank.setText(userLeaderBoard.getRank().toString());
        }

        holder.mTvName.setText(userLeaderBoard.getUserName());

        holder.mTvTotalPoints.setText(String.valueOf(userLeaderBoard.getPoints()));

        if (userLeaderBoard.getCountPlayed() == 1 || userLeaderBoard.getCountPlayed() == 0) {
            holder.mTvPlayed.setText(String.valueOf(userLeaderBoard.getCountPlayed()) + " Match");
        } else {
            holder.mTvPlayed.setText(String.valueOf(userLeaderBoard.getCountPlayed()) + " Matches");
        }

        String imageUrl = userLeaderBoard.getUserPhoto();
        holder.mIvUser.setImageUrl(
                imageUrl
        );

        if (NostragamusDataHandler.getInstance().getUserId().equals(String.valueOf(userLeaderBoard.getUserId()))) {
            holder.mLlLeaderBoards.setBackgroundColor(ViewUtils.getColor(holder.mLlLeaderBoards.getContext(), R.color.leaderboard_bg_color));
        } else {
            //alternate row color
            if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black5));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black4));
            }
        }

        if (userLeaderBoard.getRank() != null) {
            if (userLeaderBoard.getRank() == 1 || userLeaderBoard.getRank() == 2 || userLeaderBoard.getRank() == 3) {
                holder.mTvRank.setTextColor(Color.WHITE);
                holder.mIvTopRankIndicator.setVisibility(View.VISIBLE);
            } else {
                holder.mTvRank.setTextColor(ContextCompat.getColor(holder.mTvRank.getContext(), R.color.leaderboard_rank_color));
                holder.mIvTopRankIndicator.setVisibility(View.INVISIBLE);
            }
        }

        if (userLeaderBoard.getAccuracy() != null) {
            holder.mTvAccuracy.setText(userLeaderBoard.getAccuracy() + "%");
        }

        //set PowerUps if Match Points is null
        if (null == userLeaderBoard.getMatchPoints()) {
            holder.mTvMatchPoints.setText(userLeaderBoard.getUserPowerUps().toString());
        } else {
            holder.mTvMatchPoints.setText(String.valueOf(userLeaderBoard.getMatchPoints()));
            holder.mTvMatchPoints.setCompoundDrawablesWithIntrinsicBounds(R.drawable.match_points_white_icon, 0, 0, 0);
        }

        if (mSelectedPos == 0) {
            holder.mTvTotalPoints.setTextColor(ContextCompat.getColor(holder.mTvRank.getContext(), R.color.yellowcolor));
            holder.mTvTotalPoints.setCompoundDrawablesWithIntrinsicBounds(R.drawable.points_yellow_icon, 0, 0, 0);
        } else {
            holder.mTvTotalPoints.setTextColor(ContextCompat.getColor(holder.mTvRank.getContext(), R.color.white));
            holder.mTvTotalPoints.setCompoundDrawablesWithIntrinsicBounds(R.drawable.points_white_icon, 0, 0, 0);
        }

        if (mSelectedPos == 1) {
            holder.mTvAccuracy.setTextColor(ContextCompat.getColor(holder.mTvRank.getContext(), R.color.yellowcolor));
            holder.mTvAccuracy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.accuracy_yellow_icon_small, 0, 0, 0);
        } else {
            holder.mTvAccuracy.setTextColor(ContextCompat.getColor(holder.mTvRank.getContext(), R.color.white));
            holder.mTvAccuracy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.leaderboards_accuracy_icon, 0, 0, 0);
        }

        if (mSelectedPos == 2 || mSelectedPos == 3) {
            holder.mTvMatchPoints.setTextColor(ContextCompat.getColor(holder.mTvRank.getContext(), R.color.yellowcolor));
            if (null == userLeaderBoard.getMatchPoints()) {
                holder.mTvMatchPoints.setCompoundDrawablesWithIntrinsicBounds(R.drawable.powerups_yellow_icon, 0, 0, 0);
            } else {
                holder.mTvMatchPoints.setCompoundDrawablesWithIntrinsicBounds(R.drawable.match_points_yellow_icon, 0, 0, 0);
            }
        } else {
            holder.mTvMatchPoints.setTextColor(ContextCompat.getColor(holder.mTvRank.getContext(), R.color.white));
            if (null == userLeaderBoard.getMatchPoints()) {
                holder.mTvMatchPoints.setCompoundDrawablesWithIntrinsicBounds(R.drawable.powerups_white_icon, 0, 0, 0);
            } else {
                holder.mTvMatchPoints.setCompoundDrawablesWithIntrinsicBounds(R.drawable.match_points_white_icon, 0, 0, 0);
            }
        }

    }

    public void setPositionSelected(Integer pos) {
        mSelectedPos = pos;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mIvStatus;

        ImageView mIvTopRankIndicator;

        TextView mTvRank;

        HmImageView mIvUser;

        TextView mTvName;

        TextView mTvTotalPoints;

        TextView mTvPlayed;

        TextView mTvMatchPoints;

        TextView mTvAccuracy;

        View mViewUserLine;

        LinearLayout mLlLeaderBoards;

        public ViewHolder(View V) {
            super(V);

            mIvTopRankIndicator = (ImageView) V.findViewById(R.id.leaderboard_row_iv_rank_indicator);
            mIvStatus = (ImageView) V.findViewById(R.id.leaderboard_row_iv_status);
            mTvRank = (TextView) V.findViewById(R.id.leaderboard_row_tv_rank);
            mIvUser = (HmImageView) V.findViewById(R.id.leaderboard_row_iv_user_img);
            mTvName = (TextView) V.findViewById(R.id.leaderboard_row_tv_user_name);
            mTvTotalPoints = (TextView) V.findViewById(R.id.leaderboard_row_tv_points);
            mTvMatchPoints = (TextView) V.findViewById(R.id.leaderboard_row_tv_match_points);
            mTvPlayed = (TextView) V.findViewById(R.id.leaderboard_row_tv_played);
            mLlLeaderBoards = (LinearLayout) V.findViewById(R.id.leaderboard_ll);
            mViewUserLine = V.findViewById(R.id.leaderboard_row_view_user);
            mTvAccuracy = (TextView) V.findViewById(R.id.leaderboard_row_tv_accuracy);

            V.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = null;
            Context context = v.getContext();

            if (null != context) {
                Integer playerId = getItem(getAdapterPosition()).getUserId();
                if (NostragamusDataHandler.getInstance().getUserId().equals(playerId.toString())) {
                    intent = new Intent(context, UserProfileActivity.class);
                } else {
                    intent = new Intent(context, PlayerProfileActivity.class);
                    intent.putExtra(BundleKeys.PLAYER_ID, playerId);
                }

                if(null != mChallengeId) {
                    intent.putExtra(BundleKeys.CHALLENGE_ID, mChallengeId);
                }
                context.startActivity(intent);

                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.LEADERBOARD,
                        Constants.AnalyticsClickLabels.OTHER_PROFILE);
            }
        }
    }
}