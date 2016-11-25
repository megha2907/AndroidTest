package in.sportscafe.nostragamus.module.user.leaderboard;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.volley.Volley;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.common.RoundImage;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;

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

        holder.mTvPlayed.setText(String.valueOf(userLeaderBoard.getCountPlayed()));

        String imageUrl=userLeaderBoard.getUserPhoto();
        holder.mIvUser.setImageUrl(
                imageUrl,
                Volley.getInstance().getImageLoader(),
                false
        );

        if (NostragamusDataHandler.getInstance().getUserId().equals(String.valueOf(userLeaderBoard.getUserId()))){

            holder.mTvName.setTextColor(ContextCompat.getColor(mcontext, R.color.btn_powerup_screen_color));
            holder.mTvPoints.setTextColor(ContextCompat.getColor(mcontext, R.color.btn_powerup_screen_color));
            holder.mTvPlayed.setTextColor(ContextCompat.getColor(mcontext, R.color.btn_powerup_screen_color));
            holder.mViewUserLine.setVisibility(View.VISIBLE);

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

        public ViewHolder(View V) {
            super(V);

            mIvStatus = (ImageView) V.findViewById(R.id.leaderboard_row_iv_status);
            mTvRank = (TextView) V.findViewById(R.id.leaderboard_row_tv_rank);
            mIvUser = (RoundImage) V.findViewById(R.id.leaderboard_row_iv_user_img);
            mTvName = (TextView) V.findViewById(R.id.leaderboard_row_tv_user_name);
            mTvPoints = (TextView) V.findViewById(R.id.leaderboard_row_tv_points);
            mTvPoints = (TextView) V.findViewById(R.id.leaderboard_row_tv_points);
            mTvPlayed= (TextView) V.findViewById(R.id.leaderboard_row_tv_played);
            mViewUserLine = (View) V.findViewById(R.id.leaderboard_row_view_user);

            V.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

    }
}