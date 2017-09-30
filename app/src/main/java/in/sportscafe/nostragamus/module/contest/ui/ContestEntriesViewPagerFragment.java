package in.sportscafe.nostragamus.module.contest.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import org.parceler.Parcel;
import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.contest.adapter.ContestEntriesExpandableListAdapter;
import in.sportscafe.nostragamus.module.contest.dataProvider.ContestEntriesDataProvider;
import in.sportscafe.nostragamus.module.contest.dto.ContestEntriesResponse;
import in.sportscafe.nostragamus.module.contest.dto.ContestEntriesScreenData;
import in.sportscafe.nostragamus.utils.AlertsHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContestEntriesViewPagerFragment extends NostraBaseFragment {

    private static final String TAG = ContestEntriesViewPagerFragment.class.getSimpleName();

    private ExpandableListView mExpandableListView;
    private ContestEntriesScreenData mContestEntryScreenData;

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
        initMembers();
        loadData();
    }

    private void initMembers() {
        Bundle args = getArguments();
        if (args != null && args.containsKey(Constants.BundleKeys.CONTEST_ENTRIES_SCREEN_DATA)) {
            mContestEntryScreenData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.CONTEST_ENTRIES_SCREEN_DATA));
        }
    }

    private void loadData() {
        if (mContestEntryScreenData != null) {
            ContestEntriesDataProvider dataProvider = new ContestEntriesDataProvider();
            dataProvider.getContestEntries(mContestEntryScreenData.getChallengeId(),
                    mContestEntryScreenData.getContestId(), getDataProviderListener());
        } else {
            AlertsHelper.showAlert(getContext(),"Error!", Constants.Alerts.SOMETHING_WRONG, null);
        }
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
