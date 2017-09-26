package in.sportscafe.nostragamus.module.inPlay.ui.viewPager;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import in.sportscafe.nostragamus.module.contest.contestDetailsAfterJoining.ContestDetailsAfterJoinActivity;
import in.sportscafe.nostragamus.module.contest.ui.ContestsActivity;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayAdapterItemType;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayAdapterListener;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayRecyclerAdapter;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayListChallengeItem;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayListItem;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayResponse;
import in.sportscafe.nostragamus.module.newChallenges.dto.SportsTab;
import in.sportscafe.nostragamus.utils.AlertsHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class InPlayViewPagerFragment extends BaseFragment {

    private static final String TAG = InPlayViewPagerFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private SportsTab mSportsTab;
    private List<InPlayResponse> mFilteredContests;

    public InPlayViewPagerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inplay_viewpager, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.inplay_contest_recycler);
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

            List<InPlayListItem> inPlayListItemList = getInPlayItemList(mFilteredContests);
            if (inPlayListItemList != null) {
                mRecyclerView.setAdapter(new InPlayRecyclerAdapter(inPlayListItemList, getAdapterListener()));
            } else {
                // No list UI
            }
        }
    }

    private List<InPlayListItem> getInPlayItemList(List<InPlayResponse> inPlayResponses) {
        List<InPlayListItem> itemList = null;
        if (inPlayResponses != null && inPlayResponses.size() > 0) {
            itemList = new ArrayList<>();
            InPlayListItem listItem = null;

            for (InPlayResponse inplay : inPlayResponses) {
                if (inplay != null) {
                    listItem = new InPlayListItem();

                    /* ------------- Add Challenge as an Item of list ------------ */
                    InPlayListChallengeItem item = getChallengeItem(inplay);
                    if (item != null) {
                        listItem.setInPlayAdapterItemType(InPlayAdapterItemType.CHALLENGE_ITEM);
                        listItem.setItemData(item);
                    }

                    itemList.add(listItem);

                    /* ------------- Add all contests of the challenge, as items in list ---------- */
                    String status = inplay.getStatus();
                    if (inplay.getContestList() != null && inplay.getContestList().size() > 0 && !TextUtils.isEmpty(status)) {
                        for (InPlayContestDto contestDto : inplay.getContestList()) {

                            contestDto.setChallengeId(inplay.getChallengeId());     // To identify every contest that which challenge it belongs

                            listItem = new InPlayListItem();
                            listItem.setItemData(contestDto);

                            if (status.equalsIgnoreCase("Completed")) {
                                listItem.setInPlayAdapterItemType(InPlayAdapterItemType.COMPLETED_CONTEST);

                            } else if (status.equalsIgnoreCase("ongoing")) {
                                listItem.setInPlayAdapterItemType(InPlayAdapterItemType.JOINED_CONTEST);

                            } else if (status.equalsIgnoreCase("Headless")) {
                                listItem.setInPlayAdapterItemType(InPlayAdapterItemType.HEADLESS_CONTEST);
                            }

                            itemList.add(listItem);
                        }
                    }
                }
            }
        }

        return itemList;
    }

    private InPlayListChallengeItem getChallengeItem(InPlayResponse response) {
        InPlayListChallengeItem challengeItem = null;
        if (response != null) {
            challengeItem = new InPlayListChallengeItem();

            challengeItem.setChallengeId(response.getChallengeId());
            challengeItem.setChallengeName(response.getChallengeName());
            challengeItem.setChallengeDesc(response.getChallengeDesc());
            challengeItem.setStatus(response.getStatus());
            challengeItem.setSportsId(response.getSportsId());
            challengeItem.setContestCount((response.getContestList() != null) ? response.getContestList().size() : 0);
        }
        return challengeItem;
    }

    @NonNull
    private InPlayAdapterListener getAdapterListener() {
        return new InPlayAdapterListener() {
            @Override
            public void onJoinAnotherContestClicked(Bundle args) {
                gotoContestScreen(args);
            }

            @Override
            public void onJoinedContestCardClicked(Bundle args) {
                goToNewMatchesTimeline(args);
            }

            @Override
            public void onJoinedContestMoreContestButtonClicked(Bundle args) {

            }

            @Override
            public void onJoinedContestWinningsClicked(Bundle args) {

            }

            @Override
            public void onCompletedCardClicked(Bundle args) {

            }

            @Override
            public void onCompletedWinningClicked(Bundle args) {

            }

            @Override
            public void onHeadLessContestCardClicked(Bundle args) {

            }

            @Override
            public void onHeadLessJoinButtonClicked(Bundle args) {

            }
        };
    }

    private void gotoContestScreen(Bundle args) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            if (getActivity() != null && !getActivity().isFinishing()) {
                Intent intent = new Intent(getActivity(), ContestsActivity.class);
                if (args != null) {
                    intent.putExtras(args);
                }

                getActivity().startActivity(intent);
            }
        } else {
            AlertsHelper.showAlert(getContext(), "No Internet", "Please turn ON internet to continue", null);
        }
    }

    private void goToNewMatchesTimeline(Bundle args) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            if (getActivity() != null && !getActivity().isFinishing()) {
                Intent intent = new Intent(getActivity(), ContestDetailsAfterJoinActivity.class);
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

    public void onChallengeData(List<InPlayResponse> inPlayFilterred) {
        mFilteredContests = inPlayFilterred;

        if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }
}
