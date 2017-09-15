package in.sportscafe.nostragamus.module.user.leaderboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.user.leaderboard.dto.LeaderBoard;

/**
 * Created by Jeeva on 10/6/16.
 */
public class LeaderBoardFragment extends BaseFragment implements LeaderBoardView {

    private RecyclerView mRvLeaderBoard;

    private LeaderBoardPresenter mLeaderBoardPresenter;

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

    public void refreshLeaderBoard(Bundle bundle) {
        mLeaderBoardPresenter.update(bundle);
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