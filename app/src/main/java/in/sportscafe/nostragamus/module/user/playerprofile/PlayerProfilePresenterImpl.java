package in.sportscafe.nostragamus.module.user.playerprofile;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;

/**
 * Created by deepanshi on 12/22/16.
 */

public class PlayerProfilePresenterImpl implements PlayerProfilePresenter, PlayerProfileModelImpl.OnProfileModelListener {

    private PlayerProfileView mProfileView;

    private PlayerProfileModel mProfileModel;

    private PlayerProfilePresenterImpl(PlayerProfileView playerProfileView) {
        this.mProfileView = playerProfileView;
        this.mProfileModel = PlayerProfileModelImpl.newInstance(this);
    }

    public static PlayerProfilePresenter newInstance(PlayerProfileView playerProfileView) {
        return new PlayerProfilePresenterImpl(playerProfileView);
    }

    @Override
    public void onCreateProfile(Bundle bundle) {
        getProfile(bundle);
        //populateUserInfo();
    }

    @Override
    public void onClickCompareProfile() {
        PlayerInfo playerInfo = mProfileModel.getPlayerInfo();

        Bundle playerInfoBundle = new Bundle();

        Long totalPoints =  playerInfo.getTotalPoints();

        playerInfoBundle.putInt(Constants.BundleKeys.PLAYER_ID,playerInfo.getId());
        playerInfoBundle.putInt(Constants.BundleKeys.POINTS,totalPoints.intValue());
        playerInfoBundle.putInt(Constants.BundleKeys.NO_OF_MATCHES,playerInfo.getPredictionCount());
        playerInfoBundle.putInt(Constants.BundleKeys.ACCURACY,playerInfo.getAccuracy());
        playerInfoBundle.putInt(Constants.BundleKeys.NO_OF_BADGES,playerInfo.getInfoDetails().getBadges().size());
        playerInfoBundle.putInt(Constants.BundleKeys.NO_OF_SPORTS_FOLLOWED,playerInfo.getUserSports().size());
        playerInfoBundle.putString(Constants.BundleKeys.LEVEL,playerInfo.getInfoDetails().getLevel());
        playerInfoBundle.putString(Constants.BundleKeys.PLAYER_NAME,playerInfo.getUserNickName());
        playerInfoBundle.putString(Constants.BundleKeys.PLAYER_PHOTO,playerInfo.getPhoto());

        mProfileView.navigateToPlayerComparison(playerInfoBundle);

    }


    private void getProfile(Bundle bundle) {
        mProfileView.showProgressbar();
        mProfileModel.getProfileDetails(bundle);
    }



    private void populateUserInfo() {
        PlayerInfo playerInfo = mProfileModel.getPlayerInfo();
        mProfileView.setName(playerInfo.getUserNickName());
        mProfileView.setProfileImage(playerInfo.getPhoto());
       // mProfileView.setTotalPoints(playerInfo.getTotalPoints());
        mProfileView.setBadgesCount(playerInfo.getBadges().size(),playerInfo.getBadges());

        if (!TextUtils.isEmpty(playerInfo.getInfoDetails().getLevel())) {
            mProfileView.setLevel(playerInfo.getInfoDetails().getLevel());
        }else {
            mProfileView.setLevel("1");
        }

        if (null == playerInfo.getMutualGroups()) {
            mProfileView.setGroupsCount(0);
        }else {
            mProfileView.setGroupsCount(playerInfo.getMutualGroups().size());
        }
        mProfileView.setPoints(playerInfo.getTotalPoints());
        mProfileView.setAccuracy(playerInfo.getAccuracy());
        mProfileView.setPredictionCount(playerInfo.getPredictionCount());
        mProfileView.initMyPosition(playerInfo);
    }


    @Override
    public void onNoInternet() {
        mProfileView.dismissProgressbar();
        showAlert(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onSuccessPlayerInfo(PlayerInfo playerInfo) {
        mProfileView.dismissProgressbar();
        populateUserInfo();
    }

    @Override
    public void onFailedPlayerInfo() {
        mProfileView.dismissProgressbar();
    }

    private void showAlert(String message) {
        mProfileView.showMessage(message, "RETRY",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
    }
}