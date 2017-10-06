package in.sportscafe.nostragamus.module.challengeCompleted.ui.viewPager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeeva.android.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.challengeCompleted.adapter.CompletedChallengeAdapterItemType;
import in.sportscafe.nostragamus.module.challengeCompleted.adapter.CompletedChallengeAdapterListener;
import in.sportscafe.nostragamus.module.challengeCompleted.adapter.CompletedChallengeRecyclerAdapter;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedContestDto;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedListChallengeItem;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedListItem;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedResponse;
import in.sportscafe.nostragamus.module.contest.contestDetailsAfterJoining.ContestDetailsAfterJoinActivity;
import in.sportscafe.nostragamus.module.contest.contestDetailsCompletedChallenges.ContestDetailCompletedActivity;
import in.sportscafe.nostragamus.module.inPlay.helper.InPlayFilterHelper;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.SportsDataProvider;
import in.sportscafe.nostragamus.module.newChallenges.dto.SportsTab;
import in.sportscafe.nostragamus.utils.AlertsHelper;

/**
 * Created by deepanshi on 9/27/17.
 */

public class CompleteChallengeViewPagerFragment extends BaseFragment {

    private static final String TAG = CompleteChallengeViewPagerFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private SportsTab mSportsTab;
    private List<CompletedResponse> mFilteredContests;

    public CompleteChallengeViewPagerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_completed_viewpager, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.completed_contest_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    private void loadData() {
        if (mRecyclerView != null && mFilteredContests != null) {

            List<CompletedListItem> completedListItemList = getCompletedItemList(mFilteredContests);
            if (completedListItemList != null) {
                mRecyclerView.setAdapter(new CompletedChallengeRecyclerAdapter(completedListItemList, getAdapterListener()));

                if (mSportsTab != null) {
                    switch (mSportsTab.getSportsId()) {
                        case SportsDataProvider.FILTER_ALL_SPORTS_ID:
                            break;
                    }
                }
            } else {
                // TODO
            }
        }
    }


    private List<CompletedListItem> getCompletedItemList(List<CompletedResponse> completedResponses) {
        List<CompletedListItem> itemList = null;
        if (completedResponses != null && completedResponses.size() > 0) {
            itemList = new ArrayList<>();
            CompletedListItem listItem = null;

            for (CompletedResponse completedResponse : completedResponses) {
                if (completedResponse != null) {
                    listItem = new CompletedListItem();

                    /* ------------- Add Challenge as an Item of list ------------ */
                    CompletedListChallengeItem item = getChallengeItem(completedResponse);
                    if (item != null) {
                        listItem.setCompletedAdapterItemType(CompletedChallengeAdapterItemType.CHALLENGE_ITEM);
                        listItem.setItemData(item);
                    }

                    itemList.add(listItem);

                    /* ------------- Add all contests of the challenge, as items in list ---------- */
                    String status = completedResponse.getStatus();
                    if (completedResponse.getContestList() != null && completedResponse.getContestList().size() > 0 && !TextUtils.isEmpty(status)) {
                        for (CompletedContestDto contestDto : completedResponse.getContestList()) {

                            contestDto.setChallengeId(completedResponse.getChallengeId());     // To identify every contest that which challenge it belongs

                            listItem = new CompletedListItem();
                            listItem.setItemData(contestDto);
                            listItem.setCompletedAdapterItemType(CompletedChallengeAdapterItemType.COMPLETED_CONTEST);

                            itemList.add(listItem);
                        }
                    }
                }
            }
        }

        return itemList;
    }

    private CompletedListChallengeItem getChallengeItem(CompletedResponse response) {
        CompletedListChallengeItem challengeItem = null;
        if (response != null) {
            challengeItem = new CompletedListChallengeItem();

            challengeItem.setChallengeId(response.getChallengeId());
            challengeItem.setChallengeName(response.getChallengeName());
            challengeItem.setChallengeTournaments(response.getTournaments());
            challengeItem.setChallengeStartTime(response.getChallengeStartTime());
            challengeItem.setChallengeEndTime(response.getChallengeEndTime());
            challengeItem.setStatus(response.getStatus());
            challengeItem.setSportIdArray(response.getSportIdArray());
            challengeItem.setContestCount((response.getContestList() != null) ? response.getContestList().size() : 0);
        }
        return challengeItem;
    }

    @NonNull
    private CompletedChallengeAdapterListener getAdapterListener() {
        return new CompletedChallengeAdapterListener() {

            @Override
            public void onCompletedCardClicked(Bundle args) {
                goToHistoryDetails(args);
            }

            @Override
            public void onCompletedWinningClicked(Bundle args) {
                goToHistoryDetails(args);
            }

            @Override
            public void onCompletedContestCurrentRankClicked(Bundle args) {
                goToHistoryDetails(args);
            }

        };
    }

    private void goToHistoryDetails(Bundle args) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            if (getActivity() != null && !getActivity().isFinishing()) {
                Intent intent = new Intent(getActivity(), ContestDetailCompletedActivity.class);
                if (args != null) {
                    intent.putExtras(args);
                }

                getActivity().startActivity(intent);
            }
        } else {
            AlertsHelper.showAlert(getContext(), "No Internet", "Please turn ON internet to continue", null);
        }
    }


    public void setTabDetails(SportsTab sportsTab) {
        mSportsTab = sportsTab;
    }

    public SportsTab getTabDetails() {
        return mSportsTab;
    }

    public void onChallengeData(List<CompletedResponse> inPlayFilterred) {
        mFilteredContests = inPlayFilterred;

        if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }
}
