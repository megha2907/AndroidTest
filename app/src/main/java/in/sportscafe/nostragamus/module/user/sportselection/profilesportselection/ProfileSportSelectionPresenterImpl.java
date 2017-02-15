package in.sportscafe.nostragamus.module.user.sportselection.profilesportselection;

import android.view.Gravity;
import android.widget.Toast;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.Alerts;

/**
 * Created by deepanshi on 12/8/16.
 */

public class ProfileSportSelectionPresenterImpl implements ProfileSportSelectionPresenter, ProfileSportSelectionModelImpl.ProfileSportSelectionModelListener {

    private ProfileSportSelectionView mSportSelectionView;

    private ProfileSportSelectionModel mSportSelectionModel;

    private ProfileSportSelectionFragment.OnSportSelectionChangedListener mChangedListener;

    public ProfileSportSelectionPresenterImpl(ProfileSportSelectionView sportSelectionView,ProfileSportSelectionFragment.OnSportSelectionChangedListener listener) {
        this.mSportSelectionView = sportSelectionView;
        this.mSportSelectionModel = ProfileSportSelectionModelImpl.newInstance(this);
        this.mChangedListener =listener;
    }

    public static ProfileSportSelectionPresenter newInstance(ProfileSportSelectionView sportSelectionView,ProfileSportSelectionFragment.OnSportSelectionChangedListener listener) {
        return new ProfileSportSelectionPresenterImpl(sportSelectionView,listener);
    }

    @Override
    public void onCreateSportSelection() {
        mSportSelectionView.setAdapter(mSportSelectionModel.getSportsSelectionAdapter(mSportSelectionView.getContext()));
    }

    @Override
    public void onSelectedSportsSaved() {
        mChangedListener.onSportsSelectionChanged();
    }

    @Override
    public void onEmptySelection() {
        mSportSelectionView.dismissProgressbar();
        mSportSelectionView.showMessage(Alerts.EMPTY_SPORT_SELECTION, Toast.LENGTH_LONG);
    }

    @Override
    public void onNoInternet() {
        mSportSelectionView.dismissProgressbar();
        mSportSelectionView.showMessage(Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onFailed(String message) {
        mSportSelectionView.dismissProgressbar();
        mSportSelectionView.showMessage(message);
    }
}