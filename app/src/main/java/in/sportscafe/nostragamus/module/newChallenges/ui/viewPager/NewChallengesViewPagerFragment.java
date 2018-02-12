package in.sportscafe.nostragamus.module.newChallenges.ui.viewPager;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.jeeva.android.BaseFragment;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.customViews.CustomSnackBar;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengeAdapterListener;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengesRecyclerAdapter;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.SportsDataProvider;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesResponse;
import in.sportscafe.nostragamus.module.newChallenges.dto.SportsTab;
import in.sportscafe.nostragamus.module.newChallenges.ui.matches.NewChallengesMatchActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewChallengesViewPagerFragment extends BaseFragment {

    private static final String TAG = NewChallengesViewPagerFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private SportsTab mSportsTab;
    private List<NewChallengesResponse> mFilteredChallenges;

    public NewChallengesViewPagerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_challenge_view_pager2, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.challenge_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    private void loadData() {
        if (mFilteredChallenges != null && !mFilteredChallenges.isEmpty()) {
            mRecyclerView.setAdapter(new NewChallengesRecyclerAdapter(mRecyclerView.getContext(), mFilteredChallenges, getChallengeAdapterListener()));
        } else {
            showEmptyListMsg();
        }

        if (mSportsTab != null) {
            switch (mSportsTab.getSportsId()) {
                case SportsDataProvider.FILTER_ALL_SPORTS_ID:
                    scrollToChallenge();
                    break;
            }
        }
    }

    private void showEmptyListMsg() {
        if (getView() != null) {
            mRecyclerView.setVisibility(View.GONE);
            getView().findViewById(R.id.empty_list_textView).setVisibility(View.VISIBLE);
        }
    }

    private void scrollToChallenge() {

    }

    @NonNull
    private NewChallengeAdapterListener getChallengeAdapterListener() {
        return new NewChallengeAdapterListener() {
            @Override
            public void onChallengeClicked(Bundle args) {
                launchNewChallengesMatchesActivity(args);
            }

            @Override
            public void onChallengeJoinClicked(Bundle args) {

            }
        };
    }

    private void launchNewChallengesMatchesActivity(Bundle args) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            if (getActivity() != null && !getActivity().isFinishing()) {
                Intent intent = new Intent(getActivity(), NewChallengesMatchActivity.class);
                intent.putExtras(args);
                getActivity().startActivity(intent);
            }
        } else {
            handleError(Constants.DataStatus.NO_INTERNET);
        }
    }

    private void handleError(int status) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            switch (status) {
                case Constants.DataStatus.NO_INTERNET:
                    CustomSnackBar.make(getView(), Constants.Alerts.NO_INTERNET_CONNECTION, CustomSnackBar.DURATION_LONG).show();
                    break;

                default:
                    CustomSnackBar.make(getView(), Constants.Alerts.SOMETHING_WRONG, CustomSnackBar.DURATION_LONG).show();
                    break;
            }
        }
    }

    public void setTabDetails(SportsTab sportsTab) {
        mSportsTab = sportsTab;
    }

    public SportsTab getTabDetails() {
        return mSportsTab;
    }

    public void onChallengeData(List<NewChallengesResponse> challengesFiltered) {
        mFilteredChallenges = challengesFiltered;

        if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

}
