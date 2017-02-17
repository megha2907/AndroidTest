package in.sportscafe.nostragamus.module.allchallenges;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.challenge.ChallengeFragment;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.common.ViewPagerAdapter;

/**
 * Created by Jeeva on 17/02/17.
 */

public class AllChallengesFragment extends NostragamusFragment implements AllChallengesApiModelImpl.OnAllChallengesApiModelListener {

    private ViewPagerAdapter mViewPagerAdapter;

    private AllChallengesApiModelImpl mAllChallengesApiModel;

    public static AllChallengesFragment newInstance() {
        return new AllChallengesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_challenges, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAllChallengesApiModel = AllChallengesApiModelImpl.newInstance(this);
        getAllChallenges();
    }

    private void getAllChallenges() {
        showProgressbar();
        mAllChallengesApiModel.getAllChallenges();
    }

    @Override
    public void onSuccessAllChallengesApi() {
        dismissProgressbar();
        createAdapter();
    }

    @Override
    public void onEmpty() {
        dismissProgressbar();
        showMessage(Alerts.EMPTY_CHALLENGES);
    }

    @Override
    public void onFailedAllChallengesApi(String message) {
        showAlertMessage(Alerts.API_FAIL);
    }

    @Override
    public void onNoInternet() {
        showAlertMessage(Alerts.NO_NETWORK_CONNECTION);
    }

    private void showAlertMessage(String message) {
        dismissProgressbar();
        showMessage(message, "RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllChallenges();
            }
        });
    }

    private void createAdapter() {
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        List<Challenge> challenges = mAllChallengesApiModel.getCompletedChallenges();
        if (challenges.size() > 0) {
            mViewPagerAdapter.addFragment(ChallengeFragment.newInstance(challenges), "Completed");
        }

        challenges = mAllChallengesApiModel.getInPlayChallenges();
        if (challenges.size() > 0) {
            mViewPagerAdapter.addFragment(ChallengeFragment.newInstance(challenges), "In Play");
        }

        challenges = mAllChallengesApiModel.getNewChallenges();
        if (challenges.size() > 0) {
            mViewPagerAdapter.addFragment(ChallengeFragment.newInstance(challenges), "New");
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.tab_vp);
        viewPager.setAdapter(mViewPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_tl);
        tabLayout.setupWithViewPager(viewPager);
    }
}