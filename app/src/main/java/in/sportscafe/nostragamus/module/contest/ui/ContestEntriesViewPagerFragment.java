package in.sportscafe.nostragamus.module.contest.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.contest.adapter.ContestEntriesExpandableListAdapter;
import in.sportscafe.nostragamus.module.contest.dataProvider.ContestEntriesDataProvider;
import in.sportscafe.nostragamus.module.contest.dto.ContestEntriesResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContestEntriesViewPagerFragment extends NostraBaseFragment {

    private static final String TAG = ContestEntriesViewPagerFragment.class.getSimpleName();

    private ExpandableListView mExpandableListView;

    public ContestEntriesViewPagerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contest_entries_view_pager, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        mExpandableListView = (ExpandableListView) rootView.findViewById(R.id.challengesEntriesExpandableListView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    private void loadData() {
        ContestEntriesDataProvider dataProvider = new ContestEntriesDataProvider();
        dataProvider.getContestEntries(300, 1, getDataProviderListener());
    }

    @NonNull
    private ContestEntriesDataProvider.ContestEntriesDataProviderListener getDataProviderListener() {
        return new ContestEntriesDataProvider.ContestEntriesDataProviderListener() {
            @Override
            public void onSuccessResponse(int status, ContestEntriesResponse response) {
                onContestDataSuccess(response);
            }

            @Override
            public void onError(int status) {

            }
        };
    }

    private void onContestDataSuccess(ContestEntriesResponse response) {
        if (response != null && mExpandableListView != null) {
            ContestEntriesExpandableListAdapter adapter = new ContestEntriesExpandableListAdapter(getContext(), response.getRooms());
            mExpandableListView.setAdapter(adapter);

            mExpandableListView.expandGroup(0, true);
        }
    }
}
