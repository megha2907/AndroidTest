package in.sportscafe.nostragamus.module.user.sportselection.profilesportselection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.utils.ViewUtils;


/**
 * Created by deepanshi on 12/8/16.
 */

public class ProfileSportSelectionFragment extends NostragamusFragment implements ProfileSportSelectionView{

    private RecyclerView mRvSportSelection;

    private ProfileSportSelectionPresenter mSportSelectionPresenter;

    private ProfileSportSelectionFragment.OnSportSelectionChangedListener mChangedListener;


    public static ProfileSportSelectionFragment newInstance(ProfileSportSelectionFragment.OnSportSelectionChangedListener listener) {
        ProfileSportSelectionFragment fragment = new ProfileSportSelectionFragment();
        fragment.mChangedListener=listener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sport_selection, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mRvSportSelection = (RecyclerView) findViewById(R.id.sport_selection_rcv);
        this.mRvSportSelection.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        this.mRvSportSelection.setHasFixedSize(true);
        this.mSportSelectionPresenter = ProfileSportSelectionPresenterImpl.newInstance(this,mChangedListener);
        this.mSportSelectionPresenter.onCreateSportSelection();
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        this.mRvSportSelection.setAdapter(adapter);
    }

    @Override
    public void showToast() {
        Toast toast =Toast.makeText(getContext(), Constants.Alerts.EMPTY_TOURNAMENT_SELECTION, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public interface OnSportSelectionChangedListener {
        void setSportsCount(int sportsCount);
    }

}
