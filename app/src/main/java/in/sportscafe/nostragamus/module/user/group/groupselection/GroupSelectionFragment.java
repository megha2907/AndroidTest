package in.sportscafe.nostragamus.module.user.group.groupselection;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.common.SpacesItemDecoration;
import in.sportscafe.nostragamus.module.user.group.newgroup.GrpTournamentSelectedAdapter;
import in.sportscafe.nostragamus.module.user.group.newgroup.GrpTournamentSelectionAdapter;
import in.sportscafe.nostragamus.module.user.sportselection.profilesportselection.ProfileSportSelectionFragment;

/**
 * Created by deepanshi on 1/6/17.
 */

public class GroupSelectionFragment extends NostragamusFragment implements GroupSelectionView {

    private static final String KEY_GROUP_ID = "groupId";

    private RecyclerView mRvSportSelection;

    private GroupSelectionPresenter mGroupSelectionPresenter;

    private GroupSelectionFragment.OnTournamentUpdatedListener mUpdatedListener;

    public static GroupSelectionFragment newInstance(Integer groupId) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_GROUP_ID, groupId);

        GroupSelectionFragment fragment = new GroupSelectionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof OnTournamentUpdatedListener) {
            mUpdatedListener = (OnTournamentUpdatedListener) context;
        } else throw new IllegalArgumentException("Called activity should implement the OnTournamentUpdatedListener");
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

        this.mGroupSelectionPresenter = GroupSelectionPresenterImpl.newInstance(this);
        this.mGroupSelectionPresenter.onGetGroupSelectionInfo(getArguments().getInt(KEY_GROUP_ID));
    }

    @Override
    public void setSelectedAdapter(GrpTournamentSelectionAdapter adapter) {
        this.mRvSportSelection.setAdapter(adapter);
    }

    @Override
    public void setSuccessResult() {
        getActivity().setResult(Activity.RESULT_OK);
    }

    @Override
    public void setTournamentsCount(int size, boolean isGroupTournamentChanged) {
        mUpdatedListener.setTournamentsCount(size, isGroupTournamentChanged);
    }

    public interface OnTournamentUpdatedListener {
        void setTournamentsCount(int tournamentsCount, boolean isGroupTournamentChanged);
    }
}