package in.sportscafe.nostragamus.module.contest.ui.viewPager;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.contest.adapter.ContestAdapterListener;
import in.sportscafe.nostragamus.module.contest.adapter.ContestRecyclerAdapter;
import in.sportscafe.nostragamus.module.contest.dto.Contest;
import in.sportscafe.nostragamus.module.contest.dto.ContestType;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContestViewPagerFragment extends NostraBaseFragment {

    private static final String TAG = ContestViewPagerFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private ContestType contestType;
    private List<Contest> mContestList;
    private TextView mTvContestName;
    private TextView mTvContestDesc;

    public ContestViewPagerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contest_view_pager, container, false);
        initRoot(rootView);
        return rootView;
    }

    private void initRoot(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.contest_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mTvContestName = (TextView) rootView.findViewById(R.id.contest_name);
        mTvContestDesc = (TextView) rootView.findViewById(R.id.contest_desc);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populateDataOnUi();
    }

    private void populateDataOnUi() {
        if (mRecyclerView != null && mContestList != null) {
            mRecyclerView.setAdapter(new ContestRecyclerAdapter(mRecyclerView.getContext(), mContestList, getContestAdapterListener()));
            mTvContestName.setText(getContestType().getName());
            mTvContestDesc.setText(getContestType().getTagLine());
        }
    }

    private ContestAdapterListener getContestAdapterListener() {
        return new ContestAdapterListener() {
            @Override
            public void onContestClicked() {

            }

            @Override
            public void onJoinContestClicked() {

            }
        };
    }

    public ContestType getContestType() {
        return contestType;
    }

    public void setContestType(ContestType contestType) {
        this.contestType = contestType;
    }

    public void onContestData(List<Contest> contests) {
        mContestList = contests;
    }
}
