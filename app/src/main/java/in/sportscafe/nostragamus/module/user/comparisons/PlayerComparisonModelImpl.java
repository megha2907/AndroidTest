package in.sportscafe.nostragamus.module.user.comparisons;

import android.os.Bundle;

import in.sportscafe.nostragamus.Constants;

/**
 * Created by deepanshi on 2/11/17.
 */

public class PlayerComparisonModelImpl implements PlayerComparisonModel {

    private PlayerComparisonModelImpl.OnPlayerDataModelListener mProfileModelListener;

    private Integer mPlayerId;

    private Integer mPoints;

    private Integer mMatches;

    private Integer mAccuracy;

    private Integer mBadges;

    private Integer mSportsFollowed;

    private String mLevel;

    private String mPlayerName;

    private String mPlayerPhoto;

    private Bundle mbundle;

    private PlayerComparisonModelImpl(PlayerComparisonModelImpl.OnPlayerDataModelListener listener) {
        this.mProfileModelListener = listener;
    }

    public static PlayerComparisonModel newInstance(PlayerComparisonModelImpl.OnPlayerDataModelListener listener) {
        return new PlayerComparisonModelImpl(listener);
    }

    @Override
    public void getPlayerData(Bundle bundle) {

        mbundle = new Bundle();
        mbundle = bundle;

        mPlayerId = bundle.getInt(Constants.BundleKeys.PLAYER_ID);
        mPoints = bundle.getInt(Constants.BundleKeys.POINTS);
        mMatches = bundle.getInt(Constants.BundleKeys.NO_OF_MATCHES);
        mAccuracy = bundle.getInt(Constants.BundleKeys.ACCURACY);
        mBadges = bundle.getInt(Constants.BundleKeys.NO_OF_BADGES);
        mSportsFollowed = bundle.getInt(Constants.BundleKeys.NO_OF_SPORTS_FOLLOWED);
        mLevel = bundle.getString(Constants.BundleKeys.LEVEL);
        mPlayerName = bundle.getString(Constants.BundleKeys.PLAYER_NAME);
        mPlayerPhoto = bundle.getString(Constants.BundleKeys.PLAYER_PHOTO);

        setPlayerProfileData();

    }

    public void setPlayerProfileData(){
        mProfileModelListener.getPlayerComparisonData(mbundle,mPlayerName,mPlayerPhoto);
    }

    public interface OnPlayerDataModelListener {

        void onNoInternet();

        void onFailedPlayerInfo();

        void getPlayerComparisonData(Bundle bundle,String playerName,String playerPhoto);
    }
}
