package in.sportscafe.nostragamus.module.user.comparisons.compareLeaderBoards;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.utils.ViewUtils;
import in.sportscafe.nostragamus.webservice.CompareLeaderBoard;

/**
 * Created by deepanshi on 2/14/17.
 */
public class CompareLeaderBoardAdapter extends Adapter<CompareLeaderBoard, CompareLeaderBoardAdapter.ViewHolder> {

    public CompareLeaderBoardAdapter(Context context, List<CompareLeaderBoard> compareLeaderBoardList) {
        super(context);
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
        CompareLeaderBoard clBoard = getItem(position);
        holder.mTvChallengeName.setText(clBoard.getName());

        String rank = "No Rank";
        if (clBoard.getUserRank() != null) {
            rank = AppSnippet.ordinal(clBoard.getUserRank());
        } else {
            holder.mBtnUserRank.setTextSize(10);
        }
        holder.mBtnUserRank.setText("#" + rank);

        rank = "No Rank";
        if (clBoard.getPlayerRank() != null) {
            rank = AppSnippet.ordinal(clBoard.getPlayerRank());
        } else {
            holder.mBtnPlayerRank.setTextSize(10);
        }
        holder.mBtnPlayerRank.setText("#" + rank);

        Context context = holder.mBtnPlayerRank.getContext();
        if (clBoard.getPlayerRank() != clBoard.getUserRank()) {
            if (null == clBoard.getUserRank()) {
                holder.mBtnPlayerRank.setTextColor(ViewUtils.getColor(context, R.color.yellowcolor));
            } else if (null == clBoard.getPlayerRank()) {
                holder.mBtnUserRank.setTextColor(ViewUtils.getColor(context, R.color.yellowcolor));
            } else if (clBoard.getPlayerRank() < clBoard.getUserRank()) {
                holder.mBtnPlayerRank.setTextColor(ViewUtils.getColor(context, R.color.yellowcolor));
            } else {
                holder.mBtnUserRank.setTextColor(ViewUtils.getColor(context, R.color.yellowcolor));
            }
        } else {
            holder.mBtnPlayerRank.setTextColor(ViewUtils.getColor(context, R.color.white));
            holder.mBtnUserRank.setTextColor(ViewUtils.getColor(context, R.color.white));
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