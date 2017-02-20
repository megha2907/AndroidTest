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

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;

/**
 * Created by Jeeva on 17/02/17.
 */
public class ChallengeFragment extends NostragamusFragment implements ChallengeView ,View.OnClickListener{

    private ChallengePresenter mChallengePresenter;

    private   RecyclerView mRecyclerView;

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

        ImageButton swipeView = (ImageButton) findViewById(R.id.challenges_swipe_view);
        ImageButton listView = (ImageButton) findViewById(R.id.challenges_list_view);
        swipeView.setOnClickListener(this);
        listView.setOnClickListener(this);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView  = (RecyclerView) findViewById(R.id.challenges_rcv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.challenges_swipe_view:
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    mChallengePresenter.changeAdapterLayout();;
                break;

            case R.id.challenges_list_view:
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                break;
        }

    }
}