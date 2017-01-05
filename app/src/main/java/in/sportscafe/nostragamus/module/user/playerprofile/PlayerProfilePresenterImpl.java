package in.sportscafe.nostragamus.module.user.playerprofile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
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
       // mProfileView.setPoints(playerInfo.getPoints());
        mProfileView.setBadgesCount(playerInfo.getBadges().size(),playerInfo.getBadges());

        if (null == playerInfo.getMutualGroups()) {
            Log.i("inside","nullmutualgroups");
            mProfileView.setGroupsCount(0);
        }else {
            Log.i("inside","elsemutualgroups"+playerInfo.getMutualGroups());
            mProfileView.setGroupsCount(playerInfo.getMutualGroups().size());
        }
        mProfileView.setPoints(playerInfo.getPoints());
        mProfileView.initMyPosition(playerInfo);
    }


    @Override
    public void onNoInternet() {
        mProfileView.dismissProgressbar();
        showAlert(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public Context getContext() {
        return mProfileView.getContext();
    }

    @Override
    public void populatePlayerInfo() {
        mProfileView.dismissProgressbar();
        populateUserInfo();
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