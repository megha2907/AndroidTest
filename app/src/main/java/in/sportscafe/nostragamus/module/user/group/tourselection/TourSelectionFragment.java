package in.sportscafe.nostragamus.module.user.group.tourselection;

import android.content.Context;
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
import in.sportscafe.nostragamus.module.common.SpacesItemDecoration;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedInfo;
import in.sportscafe.nostragamus.module.user.group.newgroup.TourSelectionAdapter;

/**
 * Created by deepanshi on 1/6/17.
 */

public class TourSelectionFragment extends NostragamusFragment implements TourSelectionView {

    private RecyclerView mRvTourSelection;

    private TourSelectionPresenter mTourSelectionPresenter;

    private OnTourSelectionListener mTourSelectionListener;

    public static TourSelectionFragment newInstance(boolean amAdmin, Integer groupId, List<TournamentFeedInfo> followedTours) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(BundleKeys.IS_ADMIN, amAdmin);
        bundle.putInt(BundleKeys.GROUP_ID, groupId);
        bundle.putParcelable(BundleKeys.FOLLOWED_TOURS, Parcels.wrap(followedTours));

        TourSelectionFragment fragment = new TourSelectionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnTourSelectionListener) {
            mTourSelectionListener = (OnTourSelectionListener) context;
        } else
            throw new IllegalArgumentException("Called activity should implement the OnTournamentUpdatedListener");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_selection, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mRvTourSelection = (RecyclerView) findViewById(R.id.group_info_selected_tournaments_rcv);
        this.mRvTourSelection.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_10)));
        this.mRvTourSelection.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        this.mRvTourSelection.setHasFixedSize(true);

        this.mTourSelectionPresenter = TourSelectionPresenterImpl.newInstance(this);
        this.mTourSelectionPresenter.onCreateTourSelection(getArguments(), mTourSelectionListener);
    }

    @Override
    public void setTourSelectionAdapter(TourSelectionAdapter adapter) {
        this.mRvTourSelection.setAdapter(adapter);
    }

    public interface OnTourSelectionListener {

        void onTourSelectionChanged(List<TournamentFeedInfo> followedTours);
    }
}