package in.sportscafe.nostragamus.module.contest.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import in.sportscafe.nostragamus.module.contest.dto.ContestType;
import in.sportscafe.nostragamus.module.contest.helper.ContestFilterHelper;
import in.sportscafe.nostragamus.module.contest.ui.viewPager.ContestViewPagerAdapter;
import in.sportscafe.nostragamus.module.contest.ui.viewPager.ContestViewPagerFragment;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayListChallengeItem;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContestFragment extends NostraBaseFragment {

    private static final String TAG = ContestFragment.class.getSimpleName();

    public ContestFragment() {
    }

    TextView mTvTBarHeading;
    TextView mTvTBarSubHeading;
    TextView mTvTBarWalletMoney;
    String challengeName;


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
    }

    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    private void loadData() {
        ContestDataProvider dataProvider = new ContestDataProvider();
        final List<ContestType> contestTypeList = dataProvider.getContestTypeList();

        //// TODO: 9/5/17 send challenge id and challenge name from bundle
        int challengeId = getChallengeId();
        challengeName = "Australia vs India T20 Challenge";
        dataProvider.getContestDetails(challengeId, new ContestDataProvider.ContestDataProviderListener() {
            @Override
            public void onSuccessResponse(int status, ContestResponse response) {
                switch (status) {
                    case Constants.DataStatus.FROM_SERVER_API_SUCCESS:
                        if (response != null && response.getData() != null && response.getData().getContests() != null) {
                            showOnUi(contestTypeList, response.getData().getContests());
                        } else {
                            // TODO: improper response
                        }
                        break;

                    default:
                        handleError(status);
                        break;
                }
            }

            @Override
            public void onError(int status) {
                handleError(status);
            }
        });


    }

    private int getChallengeId() {
        int challengeId = 300;

        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(Constants.BundleKeys.INPLAY_CHALLENGE_LIST_ITEM)) {
                InPlayListChallengeItem challengeItem = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.INPLAY_CHALLENGE_LIST_ITEM));
                if (challengeItem != null) {
                    challengeId = challengeItem.getChallengeId();
                }
            }
        }

        return challengeId;
    }

    private void handleError(int status) {

    }

    private void showOnUi(List<ContestType> contestTypeList, List<Contest> contestList) {
        if (getView() != null && getActivity() != null) {
            if (contestTypeList != null && contestTypeList.size() > 0
                    && contestList != null && contestList.size() > 0) {

                TabLayout contestTabLayout = (TabLayout) getView().findViewById(R.id.contest_tabs);
                ViewPager challengesViewPager = (ViewPager) getView().findViewById(R.id.contest_viewPager);

                ArrayList<ContestViewPagerFragment> fragmentList = new ArrayList<>();
                ContestFilterHelper filterHelper = new ContestFilterHelper();
                ContestViewPagerFragment tabFragment = null;

                for (ContestType contestType : contestTypeList) {
                    tabFragment = new ContestViewPagerFragment();
                    List<Contest> contestFiltered = filterHelper.getFilteredContestByType(contestType.getId(), contestList);

                    if (contestFiltered != null) {
                        contestType.setContestCount(contestFiltered.size());

                        tabFragment.onContestData(contestFiltered);
                        tabFragment.setContestType(contestType);
                        fragmentList.add(tabFragment);
                    }
                }

                mTvTBarHeading.setText(String.valueOf(contestTypeList.size()) + " Contests Available");
                mTvTBarSubHeading.setText(challengeName);

                int amount = (int) WalletHelper.getTotalBalance();
                mTvTBarWalletMoney.setText(String.valueOf(amount));

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

            } else {
                // TODO: error page / no items found
            }
        }
    }

}
