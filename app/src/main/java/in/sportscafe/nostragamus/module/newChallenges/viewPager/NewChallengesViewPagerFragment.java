package in.sportscafe.nostragamus.module.newChallenges.viewPager;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits.ReferralCreditActivity;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengeAdapterListener;
import in.sportscafe.nostragamus.module.newChallenges.adapter.NewChallengesRecyclerAdapter;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesResponse;
import in.sportscafe.nostragamus.module.newChallenges.dto.SportsTab;
import in.sportscafe.nostragamus.module.newmatches.MatchesTimelineActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewChallengesViewPagerFragment extends BaseFragment implements View.OnClickListener {

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
        setupAdsWebView(rootView);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.challenge_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
    }

    private void setupAdsWebView(View rootView) {
        WebView webView = (WebView) rootView.findViewById(R.id.challenges_ads_webView);
        webView.setScrollContainer(false);
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    Log.d(TAG, "Webview Ad Touched");
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    private void loadData() {
        /*if (getView() != null) {
            WebView webView = (WebView) getView().findViewById(R.id.challenges_ads_webView);
            webView.loadUrl("http://sportscafe.in/");
        }*/

        if (mRecyclerView != null && mFilteredChallenges != null) {
            mRecyclerView.setAdapter(new NewChallengesRecyclerAdapter(mRecyclerView.getContext(), mFilteredChallenges, getChallengeAdapterListener()));
        }
    }

    @NonNull
    private NewChallengeAdapterListener getChallengeAdapterListener() {
        return new NewChallengeAdapterListener() {
            @Override
            public void onChallengeClicked(Bundle args) {
                goToNewMatchesTimeline(args);
            }

            @Override
            public void onChallengeJoinClicked(Bundle args) {

            }
        };
    }

    private void goToNewMatchesTimeline(Bundle args) {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), MatchesTimelineActivity.class);
            intent.putExtras(args);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.challenges_ads_webView:
                Log.d(TAG, "Webview Ad clicked");
                break;


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
    }
}
