package in.sportscafe.nostragamus.module.contest.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.contest.dataProvider.ContestDataProvider;
import in.sportscafe.nostragamus.module.contest.dto.Contest;
import in.sportscafe.nostragamus.module.contest.dto.ContestResponse;
import in.sportscafe.nostragamus.module.contest.dto.ContestScreenData;
import in.sportscafe.nostragamus.module.contest.dto.ContestType;
import in.sportscafe.nostragamus.module.contest.helper.ContestFilterHelper;
import in.sportscafe.nostragamus.module.contest.ui.viewPager.ContestViewPagerAdapter;
import in.sportscafe.nostragamus.module.contest.ui.viewPager.ContestViewPagerFragment;
import in.sportscafe.nostragamus.module.navigation.referfriends.ReferFriendActivity;
import in.sportscafe.nostragamus.module.navigation.referfriends.ReferFriendFragmentListener;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContestFragment extends NostraBaseFragment implements View.OnClickListener {

    private static final String TAG = ContestFragment.class.getSimpleName();

    private ContestsFragmentListener mContestsFragmentListener;

    public ContestFragment() {
    }

    private TextView mTvTBarHeading;
    private TextView mTvTBarSubHeading;
    private TextView mTvTBarWalletMoney;
    private ContestScreenData mContestScreenData;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ContestsActivity) {
            mContestsFragmentListener = (ContestsFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contest, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        mTvTBarHeading = (TextView) rootView.findViewById(R.id.toolbar_heading_one);
        mTvTBarSubHeading = (TextView) rootView.findViewById(R.id.toolbar_heading_two);
        mTvTBarWalletMoney = (TextView) rootView.findViewById(R.id.toolbar_wallet_money);
        rootView.findViewById(R.id.contest_back_btn).setOnClickListener(this);
    }

    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMembers();
        loadData();
    }

    private void loadData() {
        if (mContestScreenData != null) {
            showLoadingProgressBar();

            final ContestDataProvider dataProvider = new ContestDataProvider();

            dataProvider.getContestDetails(mContestScreenData.getChallengeId(),
                    new ContestDataProvider.ContestDataProviderListener() {
                @Override
                public void onSuccessResponse(int status, ContestResponse response) {
                    hideLoadingProgressBar();

                    switch (status) {
                        case Constants.DataStatus.FROM_SERVER_API_SUCCESS:
                            if (response != null && response.getData() != null && response.getData().getContests() != null) {
                                showOnUi(dataProvider.getContestTypeList(response.getData().getContests()), response.getData().getContests());

                            } else {
                                handleError(-1);
                            }
                            break;

                        default:
                            handleError(status);
                            break;
                    }
                }

                @Override
                public void onError(int status) {
                    hideLoadingProgressBar();
                    handleError(status);
                }
            });
        }
    }

    private void initMembers() {
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(Constants.BundleKeys.CONTEST_SCREEN_DATA)) {
                mContestScreenData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.CONTEST_SCREEN_DATA));
            }
        }
    }

    private void handleError(int status) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            switch (status) {
                case Constants.DataStatus.NO_INTERNET:
                    Snackbar.make(getView(), Constants.Alerts.NO_NETWORK_CONNECTION, Snackbar.LENGTH_LONG).show();
                    break;

                default:
                    Snackbar.make(getView(), Constants.Alerts.SOMETHING_WRONG, Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
    }

    private void showOnUi(List<ContestType> contestTypeList, List<Contest> contestList) {
        if (getView() != null && getActivity() != null && mContestScreenData != null) {
            if (contestTypeList != null && contestTypeList.size() > 0
                    && contestList != null && contestList.size() > 0) {

                addChallengeDetailsIntoContest(contestList);

                TabLayout contestTabLayout = (TabLayout) getView().findViewById(R.id.contest_tabs);
                ViewPager challengesViewPager = (ViewPager) getView().findViewById(R.id.contest_viewPager);

                ArrayList<ContestViewPagerFragment> fragmentList = new ArrayList<>();
                ContestFilterHelper filterHelper = new ContestFilterHelper();
                ContestViewPagerFragment tabFragment = null;


                /* For all the tabs */
                for (int temp = 0; temp < contestTypeList.size(); temp++) {
                    ContestType contestType = contestTypeList.get(temp);
                    tabFragment = new ContestViewPagerFragment();
                    List<Contest> contestFiltered = null;

                    if (contestType.getCategoryName().equalsIgnoreCase(ContestFilterHelper.JOINED_CONTEST)) {
                        contestFiltered = filterHelper.getJoinedContests(contestList);

                    } else {
                        contestFiltered = filterHelper.getFilteredContestByType(contestType.getCategoryName(), contestList);
                    }

                    if (contestFiltered != null && contestFiltered.size() > 0) {
                        contestType.setContestCount(contestFiltered.size());
                        tabFragment.onContestData(contestFiltered, mContestScreenData);
                        tabFragment.setContestType(contestType);
                        fragmentList.add(tabFragment);
                    }
                }

                ContestViewPagerAdapter viewPagerAdapter = new ContestViewPagerAdapter(
                        getActivity().getSupportFragmentManager(), fragmentList);
                challengesViewPager.setAdapter(viewPagerAdapter);

                contestTabLayout.setupWithViewPager(challengesViewPager);

                for (int temp = 0; temp < contestTabLayout.getTabCount(); temp++) {
                    TabLayout.Tab tab = contestTabLayout.getTabAt(temp);
                    if (tab != null) {
                        tab.setCustomView(viewPagerAdapter.getTabView(contestTabLayout.getContext(), temp));
                    }
                }

                setValues(contestList);

            } else {
                handleError(-1);
            }
        }
    }

    private void setValues(List<Contest> contestList) {
        int contestsAvailable = getAvailableContestCount(contestList);
        if (contestsAvailable > 0) {
            mTvTBarHeading.setText(contestsAvailable + " Contests Available");
        }
        mTvTBarSubHeading.setText(mContestScreenData.getChallengeName());
        mTvTBarWalletMoney.setText(String.valueOf((int) WalletHelper.getTotalBalance()));
    }

    private int getAvailableContestCount(List<Contest> contestList) {
        int count = 0;

        if (contestList != null) {
            for (Contest contest : contestList) {
                if (!contest.isContestJoined()) {
                    count++;
                }
            }
        }

        return count;
    }

    private void addChallengeDetailsIntoContest(List<Contest> contestList) {
        if (mContestScreenData != null && contestList != null) {
            for (Contest contest : contestList) {
                contest.setChallengeName(mContestScreenData.getChallengeName());
                contest.setChallengeId(mContestScreenData.getChallengeId());
            }
        }
    }

    private void showLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.contestsContentLayout).setVisibility(View.GONE);
            getView().findViewById(R.id.contestsProgressBarLayout).setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.contestsProgressBarLayout).setVisibility(View.GONE);
            getView().findViewById(R.id.contestsContentLayout).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contest_back_btn:
                mContestsFragmentListener.onBackClicked();
                break;
        }
    }
}
