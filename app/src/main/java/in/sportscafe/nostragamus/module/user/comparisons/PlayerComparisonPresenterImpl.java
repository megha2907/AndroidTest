package in.sportscafe.nostragamus.module.user.comparisons;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;

/**
 * Created by deepanshi on 2/11/17.
 */

public class PlayerComparisonPresenterImpl implements PlayerComparisonPresenter, PlayerComparisonModelImpl.OnPlayerDataModelListener {

    private PlayerComparisonView mProfileCompView;

    private PlayerComparisonModel mProfileCompModel;

    private PlayerComparisonPresenterImpl(PlayerComparisonView playerComparisonView) {
        this.mProfileCompView = playerComparisonView;
        this.mProfileCompModel = PlayerComparisonModelImpl.newInstance(this);
    }

    public static PlayerComparisonPresenter newInstance(PlayerComparisonView playerComparisonView) {
        return new PlayerComparisonPresenterImpl(playerComparisonView);
    }

    @Override
    public void onCreatePlayerComparison(Bundle bundle) {
        getPlayerData(bundle);
    }


    private void getPlayerData(Bundle bundle) {
        mProfileCompModel.getPlayerData(bundle);
    }


    @Override
    public void onNoInternet() {
        mProfileCompView.dismissProgressbar();
        showAlert(Constants.Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onFailedPlayerInfo() {
        mProfileCompView.dismissProgressbar();
    }

    @Override
    public void getPlayerComparisonData(Bundle bundle,String playerName,String playerPhoto) {

        mProfileCompView.setName(NostragamusDataHandler.getInstance().getUserInfo().getUserName(),playerName);
        mProfileCompView.setProfileImage(NostragamusDataHandler.getInstance().getUserInfo().getPhoto(),playerPhoto);
        mProfileCompView.initMyPosition(bundle);
    }

    private void showAlert(String message) {
        mProfileCompView.showMessage(message, "RETRY",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
    }
}