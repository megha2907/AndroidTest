package in.sportscafe.nostragamus.module.allchallenges.challenge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;

/**
 * Created by Jeeva on 17/02/17.
 */
public class ChallengeFragment extends NostragamusFragment implements ChallengeView, View.OnClickListener {

    private ChallengePresenter mChallengePresenter;

    private RecyclerView mRecyclerView;

    private LinearLayoutManager mLayoutManager;

    private RelativeLayout mRlChallengeCount;

    private ImageButton mSwipeView;

    private ImageButton mlistView;

    public static ChallengeFragment newInstance(List<Challenge> challenges) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKeys.CHALLENGE_LIST, Parcels.wrap(challenges));

        ChallengeFragment fragment = new ChallengeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_challenges, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mChallengePresenter = ChallengePresenterImpl.newInstance(this);
        this.mChallengePresenter.onCreateChallenge(getArguments());

        mSwipeView = (ImageButton) findViewById(R.id.challenges_swipe_view);
        mlistView = (ImageButton) findViewById(R.id.challenges_list_view);
        mSwipeView.setOnClickListener(this);
        mlistView.setOnClickListener(this);
        mSwipeView.setSelected(true);
        mlistView.setSelected(false);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView = (RecyclerView) findViewById(R.id.challenges_rcv);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mChallengePresenter.changeAdapterLayout(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                updateChallengeCount();
            }
        });

    }

    private void updateChallengeCount() {
        int currentPosition = mLayoutManager.findFirstCompletelyVisibleItemPosition();
        int totalCount = mRecyclerView.getAdapter().getItemCount();
        TextView challengeCount = (TextView) findViewById(R.id.challenges_count_tv);
        TextView challengeTotalCount = (TextView) findViewById(R.id.challenges_total_count_tv);
        mRlChallengeCount = (RelativeLayout) findViewById(R.id.challenges_count_rl);

        if (currentPosition < 0) {
            challengeCount.setText("Challenge " + String.valueOf(mLayoutManager.findLastVisibleItemPosition() + 1));
        } else {
            challengeCount.setText("Challenge " + String.valueOf(currentPosition + 1));
        }

        challengeTotalCount.setText("/" + String.valueOf(totalCount));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.challenges_swipe_view:
                mSwipeView.setSelected(true);
                mlistView.setSelected(false);
                mLayoutManager = new  LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mChallengePresenter.changeAdapterLayout(true);
                mRlChallengeCount.setVisibility(View.VISIBLE);
                break;

            case R.id.challenges_list_view:
                mlistView.setSelected(true);
                mSwipeView.setSelected(false);
                mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mChallengePresenter.changeAdapterLayout(false);
                mRlChallengeCount.setVisibility(View.GONE);
                break;
        }

    }
}