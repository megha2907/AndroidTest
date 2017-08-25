package in.sportscafe.nostragamus.module.challenges.viewPager;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeeva.android.BaseFragment;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.challenges.adapter.ChallengeAdapterListener;
import in.sportscafe.nostragamus.module.challenges.adapter.ChallengesRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengeViewPagerFragment extends BaseFragment {


    public ChallengeViewPagerFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_challenge_view_pager2, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.challenge_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(new ChallengesRecyclerAdapter(recyclerView.getContext(), null, getChallengeAdapterListener()));
    }

    @NonNull
    private ChallengeAdapterListener getChallengeAdapterListener() {
        return new ChallengeAdapterListener() {
            @Override
            public void onChallengeClicked(Bundle args) {

            }

            @Override
            public void onChallengeJoinClicked(Bundle args) {

            }
        };
    }

}
