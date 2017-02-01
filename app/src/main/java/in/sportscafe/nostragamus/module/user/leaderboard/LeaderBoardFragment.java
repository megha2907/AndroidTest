package in.sportscafe.nostragamus.module.user.leaderboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.common.RoundImage;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.LeaderBoard;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;
import in.sportscafe.nostragamus.module.user.points.OnLeaderBoardUpdateListener;
import in.sportscafe.nostragamus.utils.ViewUtils;

/**
 * Created by Jeeva on 10/6/16.
 */
public class LeaderBoardFragment extends NostragamusFragment implements LeaderBoardView {

    private RecyclerView mRvLeaderBoard;

    private LeaderBoardPresenter mLeaderBoardPresenter;


    public static LeaderBoardFragment newInstance(LeaderBoard leaderBoard) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKeys.LEADERBOARD_LIST, Parcels.wrap(leaderBoard));

        LeaderBoardFragment fragment = new LeaderBoardFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getView() != null) {
            if (getUserVisibleHint() == true) {
                Log.i("inside","setUserVisibleHint");
                mLeaderBoardPresenter.checkSortType();
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mRvLeaderBoard = (RecyclerView) findViewById(R.id.leaderboard_rcv);
        this.mRvLeaderBoard.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        this.mRvLeaderBoard.setHasFixedSize(true);

        this.mLeaderBoardPresenter = LeaderBoardPresenterImpl.newInstance(this);
        this.mLeaderBoardPresenter.onCreateLeaderBoard(getArguments());
    }

    @Override
    public void setLeaderBoardAdapter(LeaderBoardAdapter leaderBoardAdapter) {
        mRvLeaderBoard.setAdapter(leaderBoardAdapter);
    }

    @Override
    public void moveAdapterPosition(int movePosition) {
        mRvLeaderBoard.getLayoutManager().scrollToPosition(movePosition);
    }

    @Override
    public void setUserPoints(UserLeaderBoard userLeaderBoard) {

        RelativeLayout userPoints = (RelativeLayout)findViewById(R.id.points_user_rl);
        View gradientView = (View)findViewById(R.id.gradient_view);
        ImageView mIvStatus = (ImageView) findViewById(R.id.leaderboard_row_iv_status);
        TextView  mTvRank = (TextView) findViewById(R.id.leaderboard_row_tv_rank);
        RoundImage mIvUser = (RoundImage) findViewById(R.id.leaderboard_row_iv_user_img);
        TextView mTvName = (TextView) findViewById(R.id.leaderboard_row_tv_user_name);
        TextView mTvPoints = (TextView) findViewById(R.id.leaderboard_row_tv_points);
        TextView mTvPlayed= (TextView) findViewById(R.id.leaderboard_row_tv_played);
        TextView mTvAccuracy = (TextView)findViewById(R.id.leaderboard_row_tv_accuracy);

        userPoints.setVisibility(View.VISIBLE);
        gradientView.setVisibility(View.VISIBLE);


        if(null == userLeaderBoard.getRank()) {
            mTvRank.setText("-");
        } else {
            String rank = AppSnippet.ordinal(userLeaderBoard.getRank());
            mTvRank.setText(rank);
        }

        mTvName.setText(userLeaderBoard.getUserName());
        mTvPoints.setText(String.valueOf(userLeaderBoard.getPoints()));

        if(null!=userLeaderBoard.getRankChange()) {
            if (userLeaderBoard.getRankChange() < 0) {
                mIvStatus.setImageResource(R.drawable.status_arrow_down);
            } else {
                mIvStatus.setImageResource(R.drawable.status_arrow_up);
            }
        }

        mIvUser.setImageUrl(
                userLeaderBoard.getUserPhoto()
        );

        if (userLeaderBoard.getCountPlayed()==1 || userLeaderBoard.getCountPlayed()==0) {
            mTvPlayed.setText(String.valueOf(userLeaderBoard.getCountPlayed())+" Match");
        }else {
            mTvPlayed.setText(String.valueOf(userLeaderBoard.getCountPlayed())+" Matches");
        }

        if (userLeaderBoard.getAccuracy()!=null) {
            mTvAccuracy.setText(userLeaderBoard.getAccuracy()+"%");
        }


    }

    public void refreshLeaderBoard(Bundle bundle) {
        mLeaderBoardPresenter.update(bundle);
    }

}