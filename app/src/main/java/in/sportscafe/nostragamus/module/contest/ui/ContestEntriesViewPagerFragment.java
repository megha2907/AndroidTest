package in.sportscafe.nostragamus.module.contest.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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

    private ExpandableListView mFilledExpandableListView;
    private ExpandableListView mFillingExpandableListView;
    private ContestEntriesScreenData mContestEntryScreenData;
    private RelativeLayout mRlContestFilling;
    private RelativeLayout mRlContestFilled;
    private TextView mTvContestFillingCount;
    private TextView mTvContestFilledCount;

    public ContestEntriesViewPagerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contest_entries_view_pager, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        mFilledExpandableListView = (ExpandableListView) rootView.findViewById(R.id.challengesFilledEntriesExpandableListView);
        mFillingExpandableListView = (ExpandableListView) rootView.findViewById(R.id.challengesFillingEntriesExpandableListView);
        mRlContestFilled = (RelativeLayout)rootView.findViewById(R.id.contest_filled_rl);
        mRlContestFilling = (RelativeLayout)rootView.findViewById(R.id.contest_filling_rl);
        mTvContestFilledCount = (TextView) rootView.findViewById(R.id.contest_filled_tv);
        mTvContestFillingCount = (TextView) rootView.findViewById(R.id.contest_filling_tv);
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
        if (response != null){

            if (mFillingExpandableListView != null && response.getFillingRooms()!=null){
                if (!response.getFillingRooms().isEmpty()) {
                    mRlContestFilling.setVisibility(View.VISIBLE);
                    mFillingExpandableListView.setVisibility(View.VISIBLE);
                    mTvContestFillingCount.setText(String.valueOf(response.getFillingRooms().size()));
                    ContestEntriesExpandableListAdapter adapter = new ContestEntriesExpandableListAdapter(getContext(), response.getFillingRooms());
                    mFillingExpandableListView.setAdapter(adapter);
                    setListViewHeight(mFillingExpandableListView);
                    mFillingExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v,
                                                    int position, long id) {
                            setListViewHeight(parent, position);
                            return false;
                        }
                    });

                }else {
                    /* Hide Filling Rooms Header */
                    mRlContestFilling.setVisibility(View.GONE);
                    mFillingExpandableListView.setVisibility(View.GONE);
                }
            }

            if (mFilledExpandableListView!=null && response.getFilledRooms()!=null){
                if (!response.getFilledRooms().isEmpty()) {
                    mRlContestFilled.setVisibility(View.VISIBLE);
                    mFilledExpandableListView.setVisibility(View.VISIBLE);
                    mTvContestFilledCount.setText(String.valueOf(response.getFilledRooms().size()));
                    ContestEntriesExpandableListAdapter adapter = new ContestEntriesExpandableListAdapter(getContext(), response.getFilledRooms());
                    mFilledExpandableListView.setAdapter(adapter);
                    setListViewHeight(mFilledExpandableListView);
                    mFilledExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v,
                                                    int position, long id) {
                            setListViewHeight(parent, position);
                            return false;
                        }
                    });

                }else {
                    /* Hide Filled Rooms Header */
                    mRlContestFilled.setVisibility(View.GONE);
                    mFilledExpandableListView.setVisibility(View.GONE);
                }
            }

        }
    }

    private void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

}
