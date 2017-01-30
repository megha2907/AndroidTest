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