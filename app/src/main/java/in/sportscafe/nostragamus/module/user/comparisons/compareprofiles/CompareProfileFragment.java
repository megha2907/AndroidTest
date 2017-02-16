package in.sportscafe.nostragamus.module.user.comparisons.compareprofiles;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;

/**
 * Created by deepanshi on 2/13/17.
 */

public class CompareProfileFragment extends NostragamusFragment {

    private Integer muserSportsFollowed;

    private Integer mplayerSportsFollowed;

    private String mUserLevel;

    private String mPlayerLevel;

    private Integer mUserPredictions;

    private Integer mPlayerPredictions;

    private Integer mUserAccuracy;

    private Integer mPlayerAccuracy;

    private Integer groupsCount;

    private Integer badgeCount;

    private Integer mUserPoints;

    private Integer mPlayerPoints;

    private Integer mUserBadgeCount;

    private Integer mPlayerBadgeCount;

    public static CompareProfileFragment newInstance(Bundle bundle) {
        CompareProfileFragment fragment = new CompareProfileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_compare_profile, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();

        mPlayerPoints = bundle.getInt(Constants.BundleKeys.POINTS);
        mPlayerPredictions = bundle.getInt(Constants.BundleKeys.NO_OF_MATCHES);
        mPlayerAccuracy = bundle.getInt(Constants.BundleKeys.ACCURACY);
        mPlayerBadgeCount = bundle.getInt(Constants.BundleKeys.NO_OF_BADGES);
        mplayerSportsFollowed = bundle.getInt(Constants.BundleKeys.NO_OF_SPORTS_FOLLOWED);
        mPlayerLevel = bundle.getString(Constants.BundleKeys.LEVEL);

        setPlayerData(mPlayerPoints, mPlayerPredictions, mPlayerAccuracy, mPlayerBadgeCount, mplayerSportsFollowed, mPlayerLevel);


    }

    private void setPlayerData(Integer playerPoints, Integer playerPredictions, Integer playerAccuracy, Integer playerBadgeCount,
                               Integer playerSportsFollowed, String playerLevel) {

        String userLevel = NostragamusDataHandler.getInstance().getUserInfo().getInfoDetails().getLevel();

        if (!TextUtils.isEmpty(userLevel) && !TextUtils.isEmpty(playerLevel)) {
            setLevel(userLevel, playerLevel);
        } else if (TextUtils.isEmpty(userLevel) && !TextUtils.isEmpty(playerLevel)) {
            setLevel("1", playerLevel);
        } else if (TextUtils.isEmpty(playerLevel) && !TextUtils.isEmpty(userLevel)) {
            setLevel(userLevel, "1");
        } else {
            setLevel("1", "1");
        }

        setPoints(NostragamusDataHandler.getInstance().getUserInfo().getTotalPoints(), playerPoints);
        setAccuracy(NostragamusDataHandler.getInstance().getUserInfo().getAccuracy(), playerAccuracy);
        setPredictionCount(NostragamusDataHandler.getInstance().getUserInfo().getPredictionCount(), playerPredictions);
        setBadgesCount(NostragamusDataHandler.getInstance().getUserInfo().getBadges().size(), playerBadgeCount);
        setSportsFollowedCount(NostragamusDataHandler.getInstance().getFavoriteSportsIdList().size(), playerSportsFollowed);

    }

    public void setSportsFollowedCount(int userSportsFollowedCount, int playerSportsFollowedCount) {

        Button userSportsFollowed = (Button) findViewById(R.id.profile_compare_tv_user_sports);
        userSportsFollowed.setText(String.valueOf(userSportsFollowedCount));

        Button playerSportsFollowed = (Button) findViewById(R.id.profile_compare_tv_player_sports);
        playerSportsFollowed.setText(String.valueOf(playerSportsFollowedCount));

    }


    public void setLevel(String userLevel, String playerLevel) {

        Button userLevelBtn = (Button) findViewById(R.id.profile_compare_tv_user_level);
        userLevelBtn.setText(userLevel);

        Button playerLevelBtn = (Button) findViewById(R.id.profile_compare_tv_player_level);
        playerLevelBtn.setText(playerLevel);
    }

    public void setPoints(long userPoints, long playerPoints) {

        Button userPointsBtn = (Button) findViewById(R.id.profile_compare_tv_user_points);
        userPointsBtn.setText(String.valueOf(userPoints));

        Button playerPointsBtn = (Button) findViewById(R.id.profile_compare_tv_player_points);
        playerPointsBtn.setText(String.valueOf(playerPoints));

    }


    public void setAccuracy(int userAccuracy, int playerAccuracy) {

        Button userAccuracyBtn = (Button) findViewById(R.id.profile_compare_tv_user_accuracy);
        userAccuracyBtn.setText(String.valueOf(userAccuracy + "%"));

        Button playerAccuracyBtn = (Button) findViewById(R.id.profile_compare_tv_player_accuracy);
        playerAccuracyBtn.setText(String.valueOf(playerAccuracy + "%"));

    }

    public void setPredictionCount(Integer userPredictionCount, Integer playerPredictionCount) {

        Button userPredictionsBtn = (Button) findViewById(R.id.profile_compare_tv_user_matches);
        userPredictionsBtn.setText(String.valueOf(userPredictionCount));

        Button playerPredictionsBtn = (Button) findViewById(R.id.profile_compare_tv_player_matches);
        playerPredictionsBtn.setText(String.valueOf(playerPredictionCount));

    }

    public void setBadgesCount(Integer userBadgeCount, Integer playerBadgeCount) {

        Button userBadgeCountBtn = (Button) findViewById(R.id.profile_compare_tv_user_badges);
        userBadgeCountBtn.setText(String.valueOf(userBadgeCount));

        Button playerBadgeCountBtn = (Button) findViewById(R.id.profile_compare_tv_player_badges);
        playerBadgeCountBtn.setText(String.valueOf(playerBadgeCount));

    }


}
