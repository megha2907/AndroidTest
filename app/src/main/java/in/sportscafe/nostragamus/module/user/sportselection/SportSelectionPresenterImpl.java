package in.sportscafe.nostragamus.module.user.sportselection;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import in.sportscafe.nostragamus.Constants;

/**
 * Created by Jeeva on 27/5/16.
 */
public class SportSelectionPresenterImpl implements SportSelectionPresenter, SportSelectionModelImpl.SportSelectionModelListener {

    private SportSelectionView mSportSelectionView;

    private SportSelectionModel mSportSelectionModel;

    private boolean mFromProfile = false;

    public SportSelectionPresenterImpl(SportSelectionView sportSelectionView) {
        this.mSportSelectionView = sportSelectionView;
        this.mSportSelectionModel = SportSelectionModelImpl.newInstance(this);
    }

    public static SportSelectionPresenter newInstance(SportSelectionView sportSelectionView) {
        return new SportSelectionPresenterImpl(sportSelectionView);
    }

    @Override
    public void onCreateSportSelection(Bundle bundle) {
        if(null != bundle && bundle.containsKey(Constants.BundleKeys.FROM_PROFILE)) {
            this.mFromProfile = bundle.getBoolean(Constants.BundleKeys.FROM_PROFILE);

            if (bundle.getBoolean(Constants.BundleKeys.FROM_PROFILE))
            {
                mSportSelectionView.changeViewforProfile();
            }
            else {

                mSportSelectionView.changeViewforLogin();
            }

        }

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
        if(mFromProfile) {
            mSportSelectionView.goBackWithSuccessResult();
            return;
        }

        if (mSportSelectionModel.isUserLoggedIn()) {
            mSportSelectionView.navigateToHome();
        } else {
            mSportSelectionView.navigateToLogin();
        }
    }

    @Override
    public void onEmptySelection() {
        mSportSelectionView.dismissProgressbar();
        Toast toast =Toast.makeText(mSportSelectionView.getContext(), Constants.Alerts.EMPTY_SPORT_SELECTION, Toast.LENGTH_SHORT);
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
}