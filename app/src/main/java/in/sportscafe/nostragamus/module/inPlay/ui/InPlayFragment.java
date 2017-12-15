package in.sportscafe.nostragamus.module.inPlay.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.contest.contestDetailsAfterJoining.InplayContestDetailsActivity;
import in.sportscafe.nostragamus.module.contest.dto.JoinContestData;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayAdapterItemType;
import in.sportscafe.nostragamus.module.inPlay.dataProvider.InPlayDataProvider;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayListItem;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayResponse;
import in.sportscafe.nostragamus.module.inPlay.helper.InPlayFilterHelper;
import in.sportscafe.nostragamus.module.inPlay.ui.headless.dto.HeadLessMatchScreenData;
import in.sportscafe.nostragamus.module.inPlay.ui.headless.matches.InPlayHeadLessMatchActivity;
import in.sportscafe.nostragamus.module.inPlay.ui.viewPager.InPlayViewPagerAdapter;
import in.sportscafe.nostragamus.module.inPlay.ui.viewPager.InPlayViewPagerFragment;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.SportsDataProvider;
import in.sportscafe.nostragamus.module.newChallenges.dto.SportsTab;
import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivityListener;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayScreenDataDto;

/**
 * A simple {@link Fragment} subclass.
 */
public class InPlayFragment extends NostraBaseFragment implements View.OnClickListener {

    private static final String TAG = InPlayFragment.class.getSimpleName();
    private Snackbar mSnackBar;
    private NostraHomeActivityListener mFragmentListener;

    public InPlayFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NostraHomeActivityListener) {
            mFragmentListener = (NostraHomeActivityListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_in_play, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        Button emptyScreenBrowseChallengeButton = (Button) rootView.findViewById(R.id.empty_inplay_browse_challenge_button);
        emptyScreenBrowseChallengeButton.setOnClickListener(this);
    }

    /**
     * Supplies intent received from on new-intent of activity
     * @param intent
     */
    public void onNewIntent(Intent intent) {

    }

    public void onInternetConnected() {
        loadData();
        if (mSnackBar != null && mSnackBar.isShown()) {
            mSnackBar.dismiss();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();

    }

    private void loadData() {
        showLoadingProgressBar();
        InPlayDataProvider dataProvider = new InPlayDataProvider();
        dataProvider.getInPlayChallenges(getContext().getApplicationContext(), new InPlayDataProvider.InPlayDataProviderListener() {
            @Override
            public void onData(int status, @Nullable List<InPlayResponse> inPlayResponseData) {
                hideLoadingProgressBar();
                onDataReceived(status, inPlayResponseData);
            }

            @Override
            public void onError(int status) {
                hideLoadingProgressBar();
                handleError(status);
            }
        });
    }

    private void handleError(int status) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            switch (status) {
                case Constants.DataStatus.FROM_DATABASE_AS_NO_INTERNET:
                    mSnackBar = Snackbar.make(getView(), Constants.Alerts.NO_INTERNET_CONNECTION, Snackbar.LENGTH_INDEFINITE);
                    break;

                case Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED:
                    mSnackBar = Snackbar.make(getView(), Constants.Alerts.COULD_NOT_FETCH_DATA_FROM_SERVER, Snackbar.LENGTH_LONG);
                    break;

                default:
                    mSnackBar = Snackbar.make(getView(), Constants.Alerts.SOMETHING_WRONG, Snackbar.LENGTH_LONG);
                    break;
            }

            mSnackBar.setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onInternetConnected();
                }
            });
            mSnackBar.show();
        }
    }

    private void onDataReceived(int status, List<InPlayResponse> inPlayResponseData) {
        switch (status) {
            case Constants.DataStatus.FROM_SERVER_API_SUCCESS:
                showDataOnUi(inPlayResponseData, true);
                break;

            case Constants.DataStatus.FROM_DATABASE_AS_NO_INTERNET:
            case Constants.DataStatus.FROM_DATABASE_ERROR:
            case Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED:
                showDataOnUi(inPlayResponseData, false);
                handleError(status);
                break;

            default:
                handleError(status);
                break;

        }
    }

    private void showDataOnUi(List<InPlayResponse> inPlayResponseList, boolean isServerResponse) {
        if (getView() != null && getActivity() != null) {
            TabLayout inPlayTabLayout = (TabLayout) getView().findViewById(R.id.inplay_tabs);
            ViewPager inPlayViewPager = (ViewPager) getView().findViewById(R.id.inplay_viewPager);

            if (BuildConfig.IS_ACL_VERSION) {
                inPlayTabLayout.setTabMode(TabLayout.MODE_FIXED);
            }else {
                inPlayTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            }

            SportsDataProvider sportsDataProvider = new SportsDataProvider();
            List<SportsTab> sportsTabList = sportsDataProvider.getSportsList();

            if (inPlayResponseList != null && inPlayResponseList.size() > 0) {

                ArrayList<InPlayViewPagerFragment> fragmentList = new ArrayList<>();
                InPlayFilterHelper filterHelper = new InPlayFilterHelper();
                InPlayViewPagerFragment tabFragment = null;

                for (SportsTab sportsTab : sportsTabList) {
                    tabFragment = new InPlayViewPagerFragment();
                    tabFragment.setArguments(getArguments());

                    int sportId = sportsTab.getSportsId();
                    List<InPlayResponse> inPlayFilteredList = null;
                    switch (sportId) {

                        case SportsDataProvider.FILTER_ALL_SPORTS_ID:
                            inPlayFilteredList = inPlayResponseList;
                            break;

                        case SportsDataProvider.FILTER_DAILY_SPORTS_ID:
                            inPlayFilteredList = filterHelper.getDailyInPlayChallenges(inPlayResponseList);
                            break;

                        case SportsDataProvider.FILTER_MIXED_SPORTS_ID:
                            inPlayFilteredList = filterHelper.getInPlayMixedSportsChallengesFilteredOn(inPlayResponseList);
                            break;

                        default:
                            inPlayFilteredList = filterHelper.getInPlayChallengesFilteredOn(sportsTab.getSportsId(), inPlayResponseList);
                            break;
                    }

                    if (inPlayFilteredList != null) {
                        sportsTab.setChallengeCount(inPlayFilteredList.size());
                        tabFragment.onChallengeData(inPlayFilteredList);
                        tabFragment.setTabDetails(sportsTab);
                        fragmentList.add(tabFragment);
                    }
                }

            /* Sort tabs */
            if (!BuildConfig.IS_ACL_VERSION) {
                Collections.sort(fragmentList, new Comparator<InPlayViewPagerFragment>() {
                    @Override
                    public int compare(InPlayViewPagerFragment fragment1, InPlayViewPagerFragment fragment2) {
                        int sportId = fragment2.getTabDetails().getSportsId();
                        if (sportId == SportsDataProvider.FILTER_ALL_SPORTS_ID ||
                                sportId == SportsDataProvider.FILTER_DAILY_SPORTS_ID) {
                            return 0;
                        } else {

                            if (fragment1.getTabDetails().getChallengeCount() < fragment2.getTabDetails().getChallengeCount()) {
                                return 1;
                            } else if (fragment1.getTabDetails().getChallengeCount() == fragment2.getTabDetails().getChallengeCount()) {
                                return 0;
                            }
                        }
                        return -1;
                    }
                });
            }

            /* create adapter */
                InPlayViewPagerAdapter viewPagerAdapter = new InPlayViewPagerAdapter
                        (getActivity().getSupportFragmentManager(), fragmentList);
                inPlayViewPager.setAdapter(viewPagerAdapter);

                inPlayTabLayout.setupWithViewPager(inPlayViewPager);

                for (int temp = 0; temp < inPlayTabLayout.getTabCount(); temp++) {
                    TabLayout.Tab tab = inPlayTabLayout.getTabAt(temp);
                    if (tab != null) {
                        tab.setCustomView(viewPagerAdapter.getTabView(inPlayTabLayout.getContext(), temp));
                    }
                }

                showAppropriateTabForAclVersionAfterJoiningChallenge(isServerResponse, inPlayResponseList, inPlayViewPager);
                handleFurtherFlow(isServerResponse, inPlayResponseList);

            } else {
                showEmptyScreen(inPlayViewPager, inPlayTabLayout, sportsTabList);
            }
        }
    }

    private void showAppropriateTabForAclVersionAfterJoiningChallenge(boolean isServerResponse,
                                                                      List<InPlayResponse> inPlayResponseList,
                                                                      ViewPager viewPager) {
        if (BuildConfig.IS_ACL_VERSION &&
                isServerResponse && inPlayResponseList != null &&
                getArguments() != null &&
                getArguments().containsKey(Constants.BundleKeys.JOIN_CONTEST_DATA)) {

            JoinContestData joinContestData = Parcels.unwrap(getArguments().getParcelable(Constants.BundleKeys.JOIN_CONTEST_DATA));
            if (joinContestData != null && joinContestData.isShouldScrollContestsInPlay()) {

                joinContestData.setShouldScrollContestsInPlay(false); //

                for (InPlayResponse challengeResponse : inPlayResponseList) {
                    if (challengeResponse.getChallengeId() == joinContestData.getChallengeId()) {
                        int counter = 0;
                        if (challengeResponse.getSportsIdArray() != null) {
                            for (int sportId : challengeResponse.getSportsIdArray()) {
                                if (sportId == SportsDataProvider.FILTER_ACL_SPORTS_ID) {
                                    break;
                                }
                                counter++;
                            }
                        }

                        if (counter == challengeResponse.getSportsIdArray().length) {
                            // This challenge has no ACL sport, so select Other tab
                            // Tab-1 : ACL ; Tab-2 : Others
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        }

                        break;
                    }
                }



            }

        }
    }

    private void showEmptyScreen(ViewPager viewPager, TabLayout tabLayout, List<SportsTab> sportsTabList) {
        if (getView() != null) {
            viewPager.setVisibility(View.GONE);
            getView().findViewById(R.id.inplay_empty_screen).setVisibility(View.VISIBLE);

            for (SportsTab sportsTab : sportsTabList) {
                TabLayout.Tab tab = tabLayout.newTab();
                tab.setCustomView(getEmptyTab(tabLayout.getContext(), sportsTab));
                tabLayout.addTab(tab);
            }
        }
    }

    private View getEmptyTab(Context context, SportsTab sportsTab) {
        LinearLayout parentLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.challenge_tab, null);

        TextView tabTextView = (TextView) parentLayout.findViewById(R.id.tab_name);
        HmImageView tabImageView = (HmImageView) parentLayout.findViewById(R.id.tab_iv);
        tabTextView.setText(sportsTab.getSportsName());
        tabImageView.setBackground(ContextCompat.getDrawable(context, sportsTab.getSportIconUnSelectedDrawable()));

        return parentLayout;
    }

    /**
     * After fetching and populating inplay contests, do this
     * 1. If playing pseudoGame (And back pressed in Play screen) then launch Headless matches screen
     *      (Headless contest should be available in fetched list)
     * 2. If Back Pressed in Results OR Awaiting results screen, then launch matches screen as per contest-status
     *      (Headless OR joined-contest matches screen as returned from server-list)
     *
     * @param isServerResponse
     * @param inPlayResponseList
     */
    private void handleFurtherFlow(boolean isServerResponse, List<InPlayResponse> inPlayResponseList) {
        Bundle args = getArguments();
        if (isServerResponse && args != null) {

            PlayScreenDataDto playScreenDataDto = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.PLAY_SCREEN_DATA));
            if (playScreenDataDto != null && inPlayResponseList != null) {

                boolean shouldBreak = false;
                for (InPlayResponse inPlayResponse : inPlayResponseList) {
                    if (inPlayResponse.getChallengeId() == playScreenDataDto.getChallengeId()) {

                        if (playScreenDataDto.isPlayingPseudoGame()) {
                            launchHeadLessMatchesScreen(playScreenDataDto); /* Playing pseudoGame and challenge found */
                        } else {
                            /* If PlayScreenData obj AND inPlayContestDto found then,
                              * Check that screen is not launched any-time previously, then launch matches screen
                               * as per contest type, e.g Headless OR joined-contest matches screen
                               * NOTE: Contest should be found in list (in server sent list) for all cases */

                            if (playScreenDataDto.getInPlayContestDto() != null &&
                                    playScreenDataDto.getInPlayContestDto().isShouldLaunchMatchesScreen()) {
                                for (InPlayContestDto contestDto : inPlayResponse.getContestList()) {
                                    if (contestDto.getContestId() == playScreenDataDto.getInPlayContestDto().getContestId()) {

                                        playScreenDataDto.getInPlayContestDto().setShouldLaunchMatchesScreen(false);    /* For this contest, already launched once; so should not be launch */
                                        if (contestDto.isHeadlessState()) {
                                            launchHeadLessMatchesScreen(playScreenDataDto);
                                        } else {
                                            contestDto.setChallengeId(inPlayResponse.getChallengeId());
                                            contestDto.setChallengeName(inPlayResponse.getChallengeName());
                                            launchInplayContestDetailsScreen(contestDto);
                                        }

                                        shouldBreak = true;
                                        break;
                                    }
                                }
                                if (shouldBreak) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void launchInplayContestDetailsScreen(InPlayContestDto inPlayContestDto) {
        if (getActivity() != null && !getActivity().isFinishing() && inPlayContestDto != null) {
            Bundle args = new Bundle();
            args.putParcelable(Constants.BundleKeys.INPLAY_CONTEST, Parcels.wrap(inPlayContestDto));

            Intent intent = new Intent(getActivity(), InplayContestDetailsActivity.class);
            intent.putExtras(args);
            getActivity().startActivity(intent);
        }
    }

    private void launchHeadLessMatchesScreen(PlayScreenDataDto playScreenDataDto) {
        if (playScreenDataDto.isPlayingPseudoGame()) {
            HeadLessMatchScreenData data = new HeadLessMatchScreenData();
            data.setChallengeName(playScreenDataDto.getChallengeName());
            data.setChallengeId(playScreenDataDto.getChallengeId());
            data.setPowerUp(playScreenDataDto.getPowerUp());
            data.setContestName(playScreenDataDto.getSubTitle());
            data.setRoomId(playScreenDataDto.getRoomId());
            data.setPlayingPseudoGame(playScreenDataDto.isPlayingPseudoGame());
            data.setInPlayContestDto(playScreenDataDto.getInPlayContestDto());
            data.setStartTime(playScreenDataDto.getChallengeStartTime());

            playScreenDataDto.setPlayingPseudoGame(false);  // Action is taken once so remove this flag

                    /* New bundle as older one has unnecessary values which can lead improper flow */
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.BundleKeys.HEADLESS_MATCH_SCREEN_DATA, Parcels.wrap(data));

            Intent intent = new Intent(getActivity(), InPlayHeadLessMatchActivity.class);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        }
    }

    private void showLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.inplayContentLayout).setVisibility(View.GONE);
            getView().findViewById(R.id.inPlayProgressBarLayout).setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.inPlayProgressBarLayout).setVisibility(View.GONE);
            getView().findViewById(R.id.inplayContentLayout).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.empty_inplay_browse_challenge_button:
                onEmptyScreenBrowseChallengesClicked();
                break;
        }
    }

    private void onEmptyScreenBrowseChallengesClicked() {
        if (mFragmentListener != null) {
            mFragmentListener.showNewChallenges(null);
        }
    }
}
