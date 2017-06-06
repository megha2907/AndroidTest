package in.sportscafe.nostragamus.module.navigation.submitquestion.matchlist;

/**
 * Created by deepanshi on 10/5/16.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.feed.dto.Match;

/**
 * Created by Jeeva on 15/6/16.
 */
public class MatchListFragment extends NostragamusFragment implements MatchListView {

    private RecyclerView mRcvMatchList;

    private MatchListPresenter mMatchListPresenter;

    public static MatchListFragment newInstance(List<Match> matchList) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKeys.MATCH_LIST, Parcels.wrap(matchList));

        MatchListFragment fragment = new MatchListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_match_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mRcvMatchList = (RecyclerView) findViewById(R.id.match_list_rv);
        this.mRcvMatchList.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        this.mRcvMatchList.setHasFixedSize(true);

        this.mMatchListPresenter = MatchListPresenterImpl.newInstance(this);
        this.mMatchListPresenter.onCreateMatchList(getArguments());
    }

    @Override
    public void setAdapter(MatchListAdapter matchesAdapter) {
        mRcvMatchList.setAdapter(matchesAdapter);
    }
}