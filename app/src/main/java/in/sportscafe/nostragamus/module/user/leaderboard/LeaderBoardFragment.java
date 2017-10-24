package in.sportscafe.nostragamus.module.user.leaderboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.LeaderBoard;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.UserLeaderBoard;

/**
 * Created by Jeeva on 10/6/16.
 */
public class LeaderBoardFragment extends BaseFragment implements LeaderBoardView, View.OnClickListener {

    private RecyclerView mRvLeaderBoard;

    private LeaderBoardPresenter mLeaderBoardPresenter;

    private View mSelectedImage;

    public static LeaderBoardFragment newInstance(LeaderBoard leaderBoard) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKeys.LEADERBOARD_LIST, Parcels.wrap(leaderBoard));
        LeaderBoardFragment fragment = new LeaderBoardFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static LeaderBoardFragment newInstance(LeaderBoard leaderBoard, int challengeId) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKeys.LEADERBOARD_LIST, Parcels.wrap(leaderBoard));
        bundle.putInt(BundleKeys.CHALLENGE_ID, challengeId);

        LeaderBoardFragment fragment = new LeaderBoardFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static LeaderBoardFragment newInstance(int roomId) {
        Bundle bundle = new Bundle();
        bundle.putInt(BundleKeys.ROOM_ID, roomId);

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView()!=null) {
            getView().findViewById(R.id.sort_by_accuracy_btn).setOnClickListener(this);
            getView().findViewById(R.id.sort_by_total_points_btn).setOnClickListener(this);
            getView().findViewById(R.id.sort_by_powerups_btn).setOnClickListener(this);
        }

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
    public void setSortSelectedType(int sortType) {
        if (getView()!=null) {
            setSelected(getView().findViewById(R.id.sort_by_total_points_btn));
        }
    }

    @Override
    public void setUserLeaderBoardView(UserLeaderBoard userLeaderBoard) {
        if (getView() != null && getActivity() != null) {
            RelativeLayout userPoints = (RelativeLayout) getView().findViewById(R.id.points_user_rl);
            View gradientView = findViewById(R.id.gradient_view);

            if (null == userLeaderBoard) {
                userPoints.setVisibility(View.GONE);
                gradientView.setVisibility(View.GONE);
                return;
            }

            userPoints.setVisibility(View.VISIBLE);
            gradientView.setVisibility(View.VISIBLE);

            ImageView mIvStatus = (ImageView) findViewById(R.id.leaderboard_row_iv_status);
            TextView mTvRank = (TextView) findViewById(R.id.leaderboard_row_tv_rank);
            HmImageView mIvUser = (HmImageView) findViewById(R.id.leaderboard_row_iv_user_img);
            TextView mTvName = (TextView) findViewById(R.id.leaderboard_row_tv_user_name);
            TextView mTvPoints = (TextView) findViewById(R.id.leaderboard_row_tv_points);
            TextView mTvPlayed = (TextView) findViewById(R.id.leaderboard_row_tv_played);
            TextView mTvAccuracy = (TextView) findViewById(R.id.leaderboard_row_tv_accuracy);
            TextView mTvMatchPoints = (TextView) findViewById(R.id.leaderboard_row_tv_match_points);

            if (null == userLeaderBoard.getRank()) {
                mTvRank.setText("-");
            } else {
                mTvRank.setText(userLeaderBoard.getRank().toString());
            }

            //set PowerUps if Match Points is null
            if (null == userLeaderBoard.getMatchPoints()) {
                mTvMatchPoints.setText(userLeaderBoard.getUserPowerUps().toString());
            } else {
                mTvMatchPoints.setText(String.valueOf(userLeaderBoard.getMatchPoints()));
                mTvMatchPoints.setCompoundDrawablesWithIntrinsicBounds(R.drawable.match_points_white_icon, 0, 0, 0);
            }


            mTvName.setText(userLeaderBoard.getUserName());
            mTvPoints.setText(String.valueOf(userLeaderBoard.getPoints()));

            if (null != userLeaderBoard.getRankChange()) {
                if (userLeaderBoard.getRankChange() < 0) {
                    mIvStatus.setImageResource(R.drawable.status_arrow_down);
                } else {
                    mIvStatus.setImageResource(R.drawable.status_arrow_up);
                }
            }

            mIvUser.setImageUrl(
                    userLeaderBoard.getUserPhoto()
            );

            if (userLeaderBoard.getCountPlayed() == 1 || userLeaderBoard.getCountPlayed() == 0) {
                mTvPlayed.setText(String.valueOf(userLeaderBoard.getCountPlayed()) + " Match");
            } else {
                mTvPlayed.setText(String.valueOf(userLeaderBoard.getCountPlayed()) + " Matches");
            }

            if (userLeaderBoard.getAccuracy() != null) {
                mTvAccuracy.setText(userLeaderBoard.getAccuracy() + "%");
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.sort_by_total_points_btn:
                LeaderBoardModelImpl.SORT_TYPE = 0;
                mLeaderBoardPresenter.checkSortType();
                setSelected(findViewById(R.id.sort_by_total_points_btn));
                break;

            case R.id.sort_by_accuracy_btn:
                LeaderBoardModelImpl.SORT_TYPE = 1;
                mLeaderBoardPresenter.checkSortType();
                setSelected(findViewById(R.id.sort_by_accuracy_btn));
                break;

            case R.id.sort_by_powerups_btn:

                LeaderBoardModelImpl.SORT_TYPE = 2;
                mLeaderBoardPresenter.checkSortType();
                setSelected(findViewById(R.id.sort_by_powerups_btn));

//                    if (ismMatchPoints) {
//                        LeaderBoardModelImpl.SORT_TYPE = 3;
//                    } else {
//                        LeaderBoardModelImpl.SORT_TYPE = 2;
//                    }
                break;
        }
    }

    private void setSelected(View selImg) {
        if (null != mSelectedImage) {
            mSelectedImage.setSelected(false);
        }

        mSelectedImage = selImg;
        mSelectedImage.setSelected(true);
    }

    @Override
    public void showInApp() {

    }

    @Override
    public void hideInApp() {

    }

    @Override
    public void showInAppMessage(String message) {

    }

    @Override
    public void showMessage(String message, String positiveButton, View.OnClickListener positiveClickListener) {

    }

    @Override
    public void showMessage(String message, String positiveButton, View.OnClickListener positiveClickListener, String negativeButton, View.OnClickListener negativeClickListener) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showMessage(String message, int duration) {

    }

    @Override
    public boolean isMessageShowing() {
        return false;
    }

    @Override
    public void dismissMessage() {

    }

    @Override
    public void showProgressbar() {

    }

    @Override
    public void updateProgressbar() {

    }

    @Override
    public boolean dismissProgressbar() {
        return false;
    }

    @Override
    public void showSoftKeyboard(View view) {

    }

    @Override
    public void hideSoftKeyboard() {

    }

    @Override
    public LayoutInflater getLayoutInflater() {
        return null;
    }

    @Override
    public String getTrimmedText(TextView textView) {
        return null;
    }
}