package in.sportscafe.nostragamus.module.user.group.groupselection;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.common.SpacesItemDecoration;
import in.sportscafe.nostragamus.module.user.group.newgroup.GrpTournamentSelectedAdapter;
import in.sportscafe.nostragamus.module.user.group.newgroup.GrpTournamentSelectionAdapter;

/**
 * Created by deepanshi on 1/6/17.
 */

public class GroupSelectionFragment extends NostragamusFragment implements GroupSelectionView {

    private static final String KEY_GROUP_ID = "groupId";

    private RecyclerView mRvSportSelection;

    private RecyclerView mRvUnselectedTournaments;

    private GroupSelectionPresenter mGroupSelectionPresenter;

    public static GroupSelectionFragment newInstance(String groupid) {

        Long groupId = Long.parseLong(groupid);

        Bundle bundle = new Bundle();
        bundle.putLong(KEY_GROUP_ID, groupId);

        GroupSelectionFragment fragment = new GroupSelectionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_selection, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mRvSportSelection = (RecyclerView) findViewById(R.id.group_info_selected_tournaments_rcv);
        this.mRvSportSelection.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_10)));
        this.mRvSportSelection.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        this.mRvSportSelection.setHasFixedSize(true);

        this.mRvUnselectedTournaments = (RecyclerView) findViewById(R.id.group_info_unselected_tournaments_rcv);
        this.mRvUnselectedTournaments.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_10)));
        this.mRvUnselectedTournaments.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        this.mRvUnselectedTournaments.setHasFixedSize(true);

        this.mGroupSelectionPresenter = GroupSelectionPresenterImpl.newInstance(this);
        this.mGroupSelectionPresenter.onGetGroupSelectionInfo(getArguments().getLong(KEY_GROUP_ID));
    }

    @Override
    public void setAdapter(GrpTournamentSelectedAdapter adapter) {
        this.mRvSportSelection.setAdapter(adapter);
        this.mRvUnselectedTournaments.setAdapter(adapter);
    }

    @Override
    public void setSuccessResult() {
        getActivity().setResult(Activity.RESULT_OK);
    }


}