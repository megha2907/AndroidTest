package in.sportscafe.nostragamus.module.user.leaderboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.common.RoundImage;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;
import in.sportscafe.nostragamus.module.user.playerprofile.PlayerProfileActivity;

/**
 * Created by Jeeva on 10/6/16.
 */
public class LeaderBoardAdapter extends Adapter<UserLeaderBoard, LeaderBoardAdapter.ViewHolder> {

    private Context mcontext;

    public LeaderBoardAdapter(Context context) {
        super(context);
        mcontext=context;
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

        if(userLeaderBoard.getRankChange() < 0) {
            holder.mIvStatus.setImageResource(R.drawable.status_arrow_down);
        } else {
            holder.mIvStatus.setImageResource(R.drawable.status_arrow_up);
        }


        if(null == userLeaderBoard.getRank()) {
            holder.mTvRank.setText("-");
        } else {
            String rank = AppSnippet.ordinal(userLeaderBoard.getRank());
            holder.mTvRank.setText(rank);
        }

        holder.mTvName.setText(userLeaderBoard.getUserName());

        holder.mTvPoints.setText(String.valueOf(userLeaderBoard.getPoints()));

        if (userLeaderBoard.getCountPlayed()==1 || userLeaderBoard.getCountPlayed()==0) {
            holder.mTvPlayed.setText(String.valueOf(userLeaderBoard.getCountPlayed())+" Match");
        }else {
            holder.mTvPlayed.setText(String.valueOf(userLeaderBoard.getCountPlayed())+" Matches");
        }

        String imageUrl=userLeaderBoard.getUserPhoto();
        holder.mIvUser.setImageUrl(
                imageUrl
        );

        if (NostragamusDataHandler.getInstance().getUserId().equals(String.valueOf(userLeaderBoard.getUserId()))){
            holder.mLlLeaderBoards.setBackgroundColor(ContextCompat.getColor(mcontext, R.color.leaderboard_bg_color));
        }
        else {
            holder.mLlLeaderBoards.setBackgroundColor(ContextCompat.getColor(mcontext, R.color.black));
        }

        if (userLeaderBoard.getRank()!=null) {
            if (userLeaderBoard.getRank() == 1 || userLeaderBoard.getRank() == 2 || userLeaderBoard.getRank() == 3) {
                holder.mTvRank.setTextColor(Color.WHITE);
            } else {
                holder.mTvRank.setTextColor(ContextCompat.getColor(holder.mTvRank.getContext(), R.color.leaderboard_rank_color));
            }
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mIvStatus;

        TextView mTvRank;

        RoundImage mIvUser;

        TextView mTvName;

        TextView mTvPoints;

        TextView mTvPlayed;

        View mViewUserLine;

        LinearLayout mLlLeaderBoards;

        public ViewHolder(View V) {
            super(V);

            mIvStatus = (ImageView) V.findViewById(R.id.leaderboard_row_iv_status);
            mTvRank = (TextView) V.findViewById(R.id.leaderboard_row_tv_rank);
            mIvUser = (RoundImage) V.findViewById(R.id.leaderboard_row_iv_user_img);
            mTvName = (TextView) V.findViewById(R.id.leaderboard_row_tv_user_name);
            mTvPoints = (TextView) V.findViewById(R.id.leaderboard_row_tv_points);
            mTvPoints = (TextView) V.findViewById(R.id.leaderboard_row_tv_points);
            mTvPlayed= (TextView) V.findViewById(R.id.leaderboard_row_tv_played);
            mLlLeaderBoards=(LinearLayout)V.findViewById(R.id.leaderboard_ll);
            mViewUserLine = (View) V.findViewById(R.id.leaderboard_row_view_user);

            V.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Integer playerId = getItem(getAdapterPosition()).getUserId();

                    Bundle mBundle = new Bundle();
                    mBundle.putInt(Constants.BundleKeys.PLAYER_ID,playerId);
                    Intent mintent2 =  new Intent(view.getContext(), PlayerProfileActivity.class);
                    mintent2.putExtras(mBundle);
                    view.getContext().startActivity(mintent2);

                }
            });
        }

    }
}