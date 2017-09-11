package in.sportscafe.nostragamus.module.inPlay.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeeva.android.BaseFragment;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayRecyclerAdapter;
import in.sportscafe.nostragamus.module.inPlay.dataProvider.InPlayDataProvider;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class InPlayFragment extends BaseFragment {

    public InPlayFragment() {}

    private RecyclerView mRcvInPlay;

    private InPlayRecyclerAdapter inPlayRecyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_in_play, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {

        mRcvInPlay = (RecyclerView) rootView.findViewById(R.id.in_play_rv);
        mRcvInPlay.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        this.mRcvInPlay.setHasFixedSize(true);

    }

    /**
     * Supplies intent received from on new-intent of activity
     * @param intent
     */
    public void onNewIntent(Intent intent) {

    }

    public void onInternetConnected() {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            loadData();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();

    }

    private void loadData() {
        showLoadingProgressBar();
        InPlayDataProvider dataProvider = new InPlayDataProvider();
        dataProvider.getInPlayChallenges(getContext().getApplicationContext(), new InPlayDataProvider.InPlayDataProviderListener() {
            @Override
            public void onData(int status, @Nullable List<InPlayResponse> inPlayResponseData) {
                hideLoadingProgressBar();
                onDataReceived(status, inPlayResponseData);
            }

            @Override
            public void onError(int status) {
                hideLoadingProgressBar();
                handleError(status);
            }
        });
    }

    private void handleError(int status) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            Snackbar.make(getView(), Constants.Alerts.SOMETHING_WRONG, Snackbar.LENGTH_SHORT);
        }
    }

    private void onDataReceived(int status, List<InPlayResponse> inPlayResponseData) {
        switch (status) {
            case Constants.DataStatus.FROM_SERVER_API_SUCCESS:
            case Constants.DataStatus.FROM_DATABASE_AS_NO_INTERNET:
            case Constants.DataStatus.FROM_DATABASE_AS_SERVER_FAILED:
                showDataOnUi(inPlayResponseData);
                break;

            default:
                handleError(status);
                break;

        }
    }

    private void showDataOnUi(List<InPlayResponse> inPlayResponseData) {
        if (getView() != null && getActivity() != null) {
            if (inPlayResponseData != null && inPlayResponseData.size() > 0) {

                if (mRcvInPlay != null) {
                    //// TODO: 9/6/17 add list and set adapter
                }

            } else {
                // TODO: error page / no items found
            }
        }
    }

    private void showLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.in_play_rv).setVisibility(View.GONE);
            getView().findViewById(R.id.inPlayProgressBarLayout).setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.inPlayProgressBarLayout).setVisibility(View.GONE);
            getView().findViewById(R.id.in_play_rv).setVisibility(View.VISIBLE);
        }
    }

}
