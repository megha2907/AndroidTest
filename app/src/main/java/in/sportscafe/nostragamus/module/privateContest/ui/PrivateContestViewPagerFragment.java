package in.sportscafe.nostragamus.module.privateContest.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeeva.android.BaseFragment;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.contest.dto.Contest;
import in.sportscafe.nostragamus.module.contest.dto.ContestScreenData;
import in.sportscafe.nostragamus.module.contest.dto.ContestType;
import in.sportscafe.nostragamus.module.privateContest.dto.CreatePrivateContestScreenData;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.CreatePrivateContestActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrivateContestViewPagerFragment extends BaseFragment implements View.OnClickListener {

    private ContestType mContestType;
    private List<Contest> mContestList;
    private ContestScreenData mContestScreenData;
    private int mMaxPowerupTransferLimit = 0;

    public PrivateContestViewPagerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_private_contest_view_pager, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        view.findViewById(R.id.create_private_contest_btn).setOnClickListener(this);
        view.findViewById(R.id.private_contest_join_with_invite_code_btn).setOnClickListener(this);
    }

    public ContestType getContestType() {
        return mContestType;
    }

    public void setContestType(ContestType contestType) {
        this.mContestType = contestType;
    }

    public List<Contest> getContestList() {
        return mContestList;
    }

    public void setContestList(List<Contest> mContestList) {
        this.mContestList = mContestList;
    }

    public ContestScreenData getContestScreenData() {
        return mContestScreenData;
    }

    public void setContestScreenData(ContestScreenData mContestScreenData) {
        this.mContestScreenData = mContestScreenData;
    }

    public int getMaxPowerupTransferLimit() {
        return mMaxPowerupTransferLimit;
    }

    public void setMaxPowerupTransferLimit(int mMaxPowerupTransferLimit) {
        this.mMaxPowerupTransferLimit = mMaxPowerupTransferLimit;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_private_contest_btn:
                onCreateContestClicked();
                break;

            case R.id.private_contest_join_with_invite_code_btn:
                break;
        }
    }

    private void onCreateContestClicked() {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {

            CreatePrivateContestScreenData screenData = new CreatePrivateContestScreenData();
            screenData.setContestType(mContestType);
            screenData.setContestScreenData(mContestScreenData);

            Bundle args = new Bundle();
            args.putParcelable(Constants.BundleKeys.PRIVATE_CONTEST_SCREEN_DATA, Parcels.wrap(screenData));

            Intent intent = new Intent(getActivity(), CreatePrivateContestActivity.class);
            intent.putExtras(args);
            getActivity().startActivityFromFragment(this, intent, 91);
        }
    }
}
