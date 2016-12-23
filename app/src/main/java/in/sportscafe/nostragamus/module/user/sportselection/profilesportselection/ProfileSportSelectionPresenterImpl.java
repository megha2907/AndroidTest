package in.sportscafe.nostragamus.module.user.sportselection.profilesportselection;

import android.view.Gravity;
import android.widget.Toast;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;

/**
 * Created by deepanshi on 12/8/16.
 */

public class ProfileSportSelectionPresenterImpl implements ProfileSportSelectionPresenter, ProfileSportSelectionModelImpl.ProfileSportSelectionModelListener {

    private ProfileSportSelectionView mSportSelectionView;

    private ProfileSportSelectionModel mSportSelectionModel;

    private ProfileSportSelectionFragment.OnSportSelectionChangedListener mChangedListener;

    private boolean mFromProfile = false;

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
        mSportSelectionView.setAdapter(mSportSelectionModel
                .getSportsSelectionAdapter(mSportSelectionView.getContext()));
    }

    @Override
    public void onClickNext() {
        mSportSelectionView.showProgressbar();
        mSportSelectionModel.saveSelectedSports();
    }

    @Override
    public void onSelectedSportsSaved() {

    }

    @Override
    public void onEmptySelection() {
        mSportSelectionView.dismissProgressbar();
        Toast toast = Toast.makeText(mSportSelectionView.getContext(), Constants.Alerts.EMPTY_SPORT_SELECTION, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onNoInternet() {
        mSportSelectionView.dismissProgressbar();
        mSportSelectionView.showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onFailed(String message) {
        mSportSelectionView.dismissProgressbar();
        mSportSelectionView.showMessage(message);
    }

    @Override
    public void setSportsCount(int size) {
        Log.i("sportscount",size+"");
        mChangedListener.setSportsCount(size);
    }
}
