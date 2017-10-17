package in.sportscafe.nostragamus.module.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.jeeva.android.Log;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.inPlay.ui.ResultsScreenDataDto;
import in.sportscafe.nostragamus.module.navigation.appupdate.AppUpdateActivity;
import in.sportscafe.nostragamus.module.navigation.referfriends.ReferFriendActivity;
import in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits.ReferralCreditActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.walletHistory.WalletHistoryActivity;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengeMatchesScreenData;
import in.sportscafe.nostragamus.module.newChallenges.ui.matches.NewChallengesMatchActivity;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsActivity;
import in.sportscafe.nostragamus.module.store.StoreActivity;

/**
 * Created by sandip on 03/10/17.
 */

public class NotificationHelper {

    private static final String TAG = NotificationHelper.class.getSimpleName();

    public NostraNotification getNotificationIfToBeSent(Intent intent) {
        NostraNotification nostraNotification = null;

        if (intent != null && intent.getExtras() != null) {
            Bundle args =intent.getExtras();
            if (args.containsKey(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION)) {
                boolean isFromNotification = args.getBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, false);

                if (isFromNotification) {
                    Log.d(TAG, "From Notification");
                    Object object = args.get("notificationDetails");

                    if (object != null) {
                        Log.d(TAG, "obj : " + object.toString());
                        try {
                            nostraNotification = new Gson().fromJson(object.toString(), NostraNotification.class); //MyWebService.getInstance().getObjectFromJson(object.toString(), NostraNotification.class);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }

        return nostraNotification;
    }

    @NonNull
    public Intent getNewChallengeMatchesScreenIntent(Context context, NostraNotification notification) {
        Bundle args = new Bundle();
        NewChallengeMatchesScreenData screenData = new NewChallengeMatchesScreenData();

        if (notification != null && notification.getData() != null) {
            screenData.setChallengeId(notification.getData().getChallengeId());
            screenData.setChallengeName(notification.getData().getChallengeName());

            args.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
            args.putParcelable(Constants.Notifications.NOSTRA_NOTIFICATION, Parcels.wrap(notification));
        }
        args.putParcelable(Constants.BundleKeys.NEW_CHALLENGE_MATCHES_SCREEN_DATA, Parcels.wrap(screenData));

        Intent intent = new Intent(context, NewChallengesMatchActivity.class);
        intent.putExtras(args);
        return intent;
    }

    @NonNull
    public Intent getResultsScreenIntent(Context context, NostraNotification notification) {
        Bundle args = new Bundle();
        ResultsScreenDataDto screenData = new ResultsScreenDataDto();

        if (notification != null && notification.getData() != null) {
            screenData.setChallengeId(notification.getData().getChallengeId());
            screenData.setChallengeName(notification.getData().getChallengeName());
            screenData.setMatchStatus(notification.getData().getMatchStatus());
            screenData.setMatchId(notification.getData().getMatchId());
            screenData.setRoomId(notification.getData().getRoomId());
            screenData.setChallengeStartTime(notification.getData().getChallengeStartTime());

            args.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
            args.putParcelable(Constants.Notifications.NOSTRA_NOTIFICATION, Parcels.wrap(notification));
        }
        args.putParcelable(Constants.BundleKeys.RESULTS_SCREEN_DATA, Parcels.wrap(screenData));

        Intent intent = new Intent(context, MyResultsActivity.class);
        intent.putExtras(args);
        return intent;
    }

    @NonNull
    public Intent getReferFriendScreenIntent(Context context, NostraNotification notification) {
        Bundle args = new Bundle();
        if (notification != null) {
            args.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
            args.putParcelable(Constants.Notifications.NOSTRA_NOTIFICATION, Parcels.wrap(notification));
        }

        Intent intent = new Intent(context, ReferFriendActivity.class);
        intent.putExtras(args);
        return intent;
    }

    @NonNull
    public Intent getReferralCreditsScreenIntent(Context context, NostraNotification notification) {
        Bundle args = new Bundle();
        if (notification != null) {
            args.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
            args.putParcelable(Constants.Notifications.NOSTRA_NOTIFICATION, Parcels.wrap(notification));
            if (notification.getData() != null) {
                args.putParcelable(Constants.BundleKeys.USER_REFERRAL_INFO, Parcels.wrap(notification.getData().getReferFriend()));
            }
        }

        Intent intent = new Intent(context, ReferralCreditActivity.class);
        intent.putExtras(args);
        return intent;
    }

    @NonNull
    public Intent getStoreScreenIntent(Context context, NostraNotification notification) {
        Bundle args = new Bundle();
        if (notification != null) {
            args.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
            args.putParcelable(Constants.Notifications.NOSTRA_NOTIFICATION, Parcels.wrap(notification));
        }

        Intent intent = new Intent(context, StoreActivity.class);
        intent.putExtras(args);
        return intent;
    }

    @NonNull
    public Intent getAppUpdateScreenIntent(Context context, NostraNotification notification) {
        Bundle args = new Bundle();
        if (notification != null) {
            args.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
            args.putParcelable(Constants.Notifications.NOSTRA_NOTIFICATION, Parcels.wrap(notification));
        }

        Intent intent = new Intent(context, AppUpdateActivity.class);
        intent.putExtras(args);
        return intent;
    }

    @NonNull
    public Intent getWalletHistoryScreenIntent(Context context, NostraNotification notification) {
        Bundle args = new Bundle();
        if (notification != null) {
            args.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
            args.putParcelable(Constants.Notifications.NOSTRA_NOTIFICATION, Parcels.wrap(notification));
        }

        Intent intent = new Intent(context, WalletHistoryActivity.class);
        intent.putExtras(args);
        return intent;
    }

    @NonNull
    public Bundle getBundleAddedNotificationDetailsIntoArgs(Intent intent, NostraNotification notification) {
        Bundle args = null;
        if (intent != null && intent.getExtras() != null) {
            args = intent.getExtras();
        } else {
            args = new Bundle();
        }
        if (notification != null) {
            args.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
            args.putParcelable(Constants.Notifications.NOSTRA_NOTIFICATION, Parcels.wrap(notification));
        }

        return args;
    }

}
