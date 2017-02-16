package in.sportscafe.nostragamus.module.user.comparisons.compareLeaderBoards;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.webservice.CompareLeaderBoard;

/**
 * Created by deepanshi on 2/14/17.
 */

public class CompareLeaderBoardAdapter extends Adapter<CompareLeaderBoard, CompareLeaderBoardAdapter.ViewHolder> {

    private Context mcon;
    private String userRank;
    private String playerRank;

    public CompareLeaderBoardAdapter(Context context, List<CompareLeaderBoard> compareLeaderBoardList) {
        super(context);
        mcon = context;
        addAll(compareLeaderBoardList);
    }

    @Override
    public CompareLeaderBoard getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public CompareLeaderBoardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CompareLeaderBoardAdapter.ViewHolder(getLayoutInflater().inflate(R.layout.inflater_compare_leaderboard, parent, false));
    }

    @Override
    public void onBindViewHolder(CompareLeaderBoardAdapter.ViewHolder holder, int position) {

        CompareLeaderBoard compareLeaderBoard = getItem(position);
        holder.mTvChallengeName.setText(compareLeaderBoard.getName());

        if (compareLeaderBoard.getUserRank() != null) {
            userRank = AppSnippet.ordinal(compareLeaderBoard.getUserRank());
        } else {
            userRank = "No Rank";
            holder.mBtnUserRank.setTextSize(10);
        }

        if (compareLeaderBoard.getPlayerRank() != null) {
            playerRank = AppSnippet.ordinal(compareLeaderBoard.getPlayerRank());
        } else {
            playerRank = "No Rank";
            holder.mBtnPlayerRank.setTextSize(10);
        }

        holder.mBtnUserRank.setText("#" + userRank);
        holder.mBtnPlayerRank.setText("#" + playerRank);

        if (compareLeaderBoard.getPlayerRank() != null && compareLeaderBoard.getUserRank() != null) {

            if (compareLeaderBoard.getPlayerRank() < compareLeaderBoard.getUserRank()) {
                holder.mBtnPlayerRank.setTextColor(ContextCompat.getColor(holder.mBtnPlayerRank.getContext(), R.color.yellowcolor));
            } else {
                holder.mBtnUserRank.setTextColor(ContextCompat.getColor(holder.mBtnUserRank.getContext(), R.color.yellowcolor));
            }

        } else if (compareLeaderBoard.getPlayerRank() == null && compareLeaderBoard.getUserRank() != null) {
            holder.mBtnUserRank.setTextColor(ContextCompat.getColor(holder.mBtnUserRank.getContext(), R.color.yellowcolor));
        } else if (compareLeaderBoard.getPlayerRank() != null && compareLeaderBoard.getUserRank() == null) {
            holder.mBtnPlayerRank.setTextColor(ContextCompat.getColor(holder.mBtnPlayerRank.getContext(), R.color.yellowcolor));
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View mMainView;
        TextView mTvChallengeName;
        Button mBtnPlayerRank;
        Button mBtnUserRank;


        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mTvChallengeName = (TextView) V.findViewById(R.id.compare_leaderboard_item_name);
            mBtnPlayerRank = (Button) V.findViewById(R.id.compare_leaderboard_item_player_pos);
            mBtnUserRank = (Button) V.findViewById(R.id.compare_leaderboard_item_user_pos);
        }
    }
}