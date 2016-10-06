package in.sportscafe.scgame.module.user.leaderboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.volley.Volley;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.Adapter;
import in.sportscafe.scgame.module.common.RoundImage;
import in.sportscafe.scgame.module.user.leaderboard.dto.LeaderBoard;

/**
 * Created by Jeeva on 10/6/16.
 */
public class LeaderBoardAdapter extends Adapter<LeaderBoard, LeaderBoardAdapter.ViewHolder> {

    public LeaderBoardAdapter(Context context) {
        super(context);
    }

    @Override
    public LeaderBoard getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_leaderboard_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LeaderBoard leaderBoard = getItem(position);

        if(leaderBoard.getRankChange() < 0) {
            holder.mIvStatus.setImageResource(R.drawable.status_arrow_down);
        } else {
            holder.mIvStatus.setImageResource(R.drawable.status_arrow_up);
        }

        holder.mTvRank.setText(String.valueOf(leaderBoard.getRank()));

        holder.mTvName.setText(leaderBoard.getUserName());

        holder.mTvPoints.setText(String.valueOf(leaderBoard.getPoints()));

        String imageUrl=leaderBoard.getUserPhoto();
        holder.mIvUser.setImageUrl(
                imageUrl,
                Volley.getInstance().getImageLoader(),
                false
        );
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mIvStatus;

        TextView mTvRank;

        RoundImage mIvUser;

        TextView mTvName;

        TextView mTvPoints;

        public ViewHolder(View V) {
            super(V);

            mIvStatus = (ImageView) V.findViewById(R.id.leaderboard_row_iv_status);
            mTvRank = (TextView) V.findViewById(R.id.leaderboard_row_tv_rank);
            mIvUser = (RoundImage) V.findViewById(R.id.leaderboard_row_iv_user_img);
            mTvName = (TextView) V.findViewById(R.id.leaderboard_row_tv_user_name);
            mTvPoints = (TextView) V.findViewById(R.id.leaderboard_row_tv_points);

            V.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

    }
}