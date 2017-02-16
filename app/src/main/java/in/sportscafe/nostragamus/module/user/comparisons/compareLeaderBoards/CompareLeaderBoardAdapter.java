package in.sportscafe.nostragamus.module.user.comparisons.compareLeaderBoards;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.webservice.CompareLeaderBoard;

/**
 * Created by deepanshi on 2/14/17.
 */

public class CompareLeaderBoardAdapter extends Adapter<CompareLeaderBoard, CompareLeaderBoardAdapter.ViewHolder> {

    private Context mcon;

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
        holder.mBtnUserRank.setText("#"+compareLeaderBoard.getUserRank().toString());
        holder.mBtnPlayerRank.setText("#"+compareLeaderBoard.getPlayerRank().toString());

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