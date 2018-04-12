package in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.contestDetails;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.widgets.CustomProgressbar;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.contest.dataProvider.ContestEntriesDataProvider;
import in.sportscafe.nostragamus.module.contest.dto.ContestEntriesResponse;
import in.sportscafe.nostragamus.module.contest.dto.ContestRoomDto;
import in.sportscafe.nostragamus.module.contest.dto.ContestRoomEntryDto;
import in.sportscafe.nostragamus.module.customViews.CustomSnackBar;
import in.sportscafe.nostragamus.module.privateContest.adapter.PrivateContestEntriesRecyclerAdapter;
import in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.dto.FindPrivateContestResponseData;
import in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.dto.PrivateContestDetailsScreenData;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrivateContestEntriesFragment extends BaseFragment {

    private static final String TAG = PrivateContestEntriesFragment.class.getSimpleName();

    private PrivateContestDetailsScreenData mScreenData;

    private RecyclerView mEntriesRecyclerView;

    public PrivateContestEntriesFragment() {
    }

    public void setScreenData(PrivateContestDetailsScreenData screenData) {
        this.mScreenData = screenData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_private_contest_entries, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mEntriesRecyclerView = (RecyclerView) view.findViewById(R.id.private_contest_entries_recyclerView);
        mEntriesRecyclerView.setHasFixedSize(true);
        mEntriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    private void loadData() {
        if (mScreenData != null && mScreenData.getPrivateContestDetailsResponse() != null &&
                mScreenData.getPrivateContestDetailsResponse().getData() != null) {

            FindPrivateContestResponseData responseData = mScreenData.getPrivateContestDetailsResponse().getData();

            int challengeId = responseData.getChallengeId();
            int contestId = 0;
            if (responseData.getPrivateContestData() != null && responseData.getPrivateContestData().size() > 0) {
                contestId = responseData.getPrivateContestData().get(0).getConfigId();
            }

            CustomProgressbar.getProgressbar(getContext()).show();
            new ContestEntriesDataProvider().getContestEntries(challengeId, contestId, getDataProviderListener());
        }
    }

    @NonNull
    private ContestEntriesDataProvider.ContestEntriesDataProviderListener getDataProviderListener() {
        return new ContestEntriesDataProvider.ContestEntriesDataProviderListener() {
            @Override
            public void onSuccessResponse(int status, ContestEntriesResponse response) {
                CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                onContestDataSuccess(response);
            }

            @Override
            public void onError(int status) {
                CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                handleError("", status);
            }
        };
    }

    private void onContestDataSuccess(ContestEntriesResponse response) {
        if (response != null && response.getFillingRooms() != null && response.getFillingRooms().size() > 0 &&
                response.getFillingRooms().get(0).getEntries() != null && response.getFillingRooms().get(0).getEntries().size() > 0) {

            List<ContestRoomEntryDto> entryDtoList = response.getFillingRooms().get(0).getEntries();

            showTotalEntries(entryDtoList.size());

            PrivateContestEntriesRecyclerAdapter entriesRecyclerAdapter =
                    new PrivateContestEntriesRecyclerAdapter(entryDtoList);

            mEntriesRecyclerView.setAdapter(entriesRecyclerAdapter);
        }
    }

    private void showTotalEntries(int size) {
        if (size > 0 && getView() != null) {
            TextView entriesTextView = (TextView) getView().findViewById(R.id.private_contest_entries_textview);
            entriesTextView.setText(size + " Entries");
        }
    }

    private void handleError(String msg, int status) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            if (!TextUtils.isEmpty(msg)) {
                CustomSnackBar.make(getView(), msg, CustomSnackBar.DURATION_LONG).show();

            } else {
                switch (status) {
                    case Constants.DataStatus.NO_INTERNET:
                        CustomSnackBar.make(getView(), Constants.Alerts.NO_INTERNET_CONNECTION, CustomSnackBar.DURATION_LONG).show();
                        break;

                    default:
                        CustomSnackBar.make(getView(), Constants.Alerts.SOMETHING_WRONG, CustomSnackBar.DURATION_LONG).show();
                        break;
                }
            }
        }
    }
}
