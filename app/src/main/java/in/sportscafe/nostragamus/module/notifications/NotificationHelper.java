package in.sportscafe.nostragamus.module.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.jeeva.android.Log;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedContestDto;
import in.sportscafe.nostragamus.module.common.WebViewActivity;
import in.sportscafe.nostragamus.module.contest.contestDetailsCompletedChallenges.ChallengeHistoryContestDetailsActivity;
import in.sportscafe.nostragamus.module.contest.ui.DetailScreensLaunchRequest;
import in.sportscafe.nostragamus.module.inPlay.ui.ResultsScreenDataDto;
import in.sportscafe.nostragamus.module.navigation.appupdate.AppUpdateActivity;
import in.sportscafe.nostragamus.module.navigation.help.howtoplay.HowToPlayActivity;
import in.sportscafe.nostragamus.module.navigation.referfriends.ReferFriendActivity;
import in.sportscafe.nostragamus.module.navigation.referfriends.referralcredits.ReferralCreditActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHomeActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.AddWalletMoneyActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.doKYC.AddKYCDetailsActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.walletHistory.WalletHistoryActivity;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengeMatchesScreenData;
import in.sportscafe.nostragamus.module.newChallenges.ui.matches.NewChallengesMatchActivity;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsActivity;
import in.sportscafe.nostragamus.module.recentActivity.announcement.AnnouncementActivity;
import in.sportscafe.nostragamus.module.recentActivity.announcement.AnnouncementScreenData;
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

            /* Notifications sent from App Only */
            if (args.containsKey(Constants.Notifications.IS_IN_APP_NOTIFICATION)) {
                boolean isInAppNotification = args.getBoolean(Constants.Notifications.IS_IN_APP_NOTIFICATION);
                if (isInAppNotification) {
                    nostraNotification = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.IN_APP_NOSTRA_NOTIFICATION_DETAILS));
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
            screenData.setStartTime(notification.getData().getChallengeStartTime());

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
    public Intent getChallengeHistoryWinningsScreenIntent(Context context, NostraNotification notification) {
        Bundle args = new Bundle();
        CompletedContestDto completedContestDto = new CompletedContestDto();

        if (notification != null && notification.getData() != null) {
            completedContestDto.setChallengeId(notification.getData().getChallengeId());
            completedContestDto.setRoomId(notification.getData().getRoomId());
            completedContestDto.setContestId(notification.getData().getContestId());
            completedContestDto.setContestName(notification.getData().getContestName());
            completedContestDto.setChallengeName(notification.getData().getChallengeName());

            args.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
            args.putParcelable(Constants.Notifications.NOSTRA_NOTIFICATION, Parcels.wrap(notification));
            args.putInt(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, DetailScreensLaunchRequest.MATCHES_REWARDS_SCREEN);
        }
        args.putParcelable(Constants.BundleKeys.COMPLETED_CONTEST, Parcels.wrap(completedContestDto));

        Intent intent = new Intent(context, ChallengeHistoryContestDetailsActivity.class);
        intent.putExtras(args);
        return intent;
    }

    @NonNull
    public Intent getChallengeHistoryLeaderBoardsScreenIntent(Context context, NostraNotification notification) {
        Bundle args = new Bundle();
        CompletedContestDto completedContestDto = new CompletedContestDto();

        if (notification != null && notification.getData() != null) {
            completedContestDto.setChallengeId(notification.getData().getChallengeId());
            completedContestDto.setRoomId(notification.getData().getRoomId());
            completedContestDto.setContestId(notification.getData().getContestId());
            completedContestDto.setContestName(notification.getData().getContestName());
            completedContestDto.setChallengeName(notification.getData().getChallengeName());

            args.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
            args.putParcelable(Constants.Notifications.NOSTRA_NOTIFICATION, Parcels.wrap(notification));
            args.putInt(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, DetailScreensLaunchRequest.MATCHES_LEADER_BOARD_SCREEN);
        }
        args.putParcelable(Constants.BundleKeys.COMPLETED_CONTEST, Parcels.wrap(completedContestDto));

        Intent intent = new Intent(context, ChallengeHistoryContestDetailsActivity.class);
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
    public Intent getWhatsNewScreenIntent(Context context, NostraNotification notification) {
        Bundle args = new Bundle();
        if (notification != null) {
            args.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
            args.putParcelable(Constants.Notifications.NOSTRA_NOTIFICATION, Parcels.wrap(notification));
            args.putString(Constants.BundleKeys.SCREEN, Constants.ScreenNames.WHATS_NEW);
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
    public Intent getAddWalletMoneyScreenIntent(Context context, NostraNotification notification) {
        Bundle args = new Bundle();
        if (notification != null) {
            args.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
            args.putParcelable(Constants.Notifications.NOSTRA_NOTIFICATION, Parcels.wrap(notification));
        }

        Intent intent = new Intent(context, AddWalletMoneyActivity.class);
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

    @NonNull
    public Intent getChallengeHistoryMatchesScreenIntent(Context context, NostraNotification notification) {
        Bundle args = new Bundle();
        CompletedContestDto completedContestDto = new CompletedContestDto();

        if (notification != null && notification.getData() != null) {
            completedContestDto.setChallengeId(notification.getData().getChallengeId());
            completedContestDto.setRoomId(notification.getData().getRoomId());
            completedContestDto.setContestId(notification.getData().getContestId());
            completedContestDto.setContestName(notification.getData().getContestName());
            completedContestDto.setChallengeName(notification.getData().getChallengeName());

            args.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
            args.putParcelable(Constants.Notifications.NOSTRA_NOTIFICATION, Parcels.wrap(notification));
            args.putInt(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, DetailScreensLaunchRequest.MATCHES_DEFAULT_SCREEN);
        }
        args.putParcelable(Constants.BundleKeys.COMPLETED_CONTEST, Parcels.wrap(completedContestDto));

        Intent intent = new Intent(context, ChallengeHistoryContestDetailsActivity.class);
        intent.putExtras(args);
        return intent;
    }

    @NonNull
    public Intent getWebViewScreenIntent(Context context, NostraNotification notification) {
        Bundle args = new Bundle();
        if (notification != null && notification.getData() != null) {
            args.putString("url", notification.getData().getWebViewUrl());
            args.putString("heading", notification.getData().getWebViewHeading());

            args.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
            args.putParcelable(Constants.Notifications.NOSTRA_NOTIFICATION, Parcels.wrap(notification));
        }

        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtras(args);
        return intent;
    }

    @NonNull
    public Intent getSlidesScreenIntent(Context context, NostraNotification notification) {
        Bundle args = new Bundle();
        if (notification != null) {
            args.putString(Constants.BundleKeys.SLIDE_ID, notification.getData().getSlideId());

            args.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
            args.putParcelable(Constants.Notifications.NOSTRA_NOTIFICATION, Parcels.wrap(notification));
        }

        Intent intent = new Intent(context, HowToPlayActivity.class);
        intent.putExtras(args);
        return intent;
    }

    @NonNull
    public Intent getAnnouncementScreenIntent(Context context, NostraNotification notification) {
        Bundle args = new Bundle();
        AnnouncementScreenData announcementScreenData = new AnnouncementScreenData();

        if (notification != null && notification.getData()!=null) {
            announcementScreenData.setAnnouncementTitle(notification.getData().getAnnouncementTitle());
            announcementScreenData.setAnnouncementDesc(notification.getData().getAnnouncementDesc());
            announcementScreenData.setAnnouncementDate(notification.getData().getAnnouncementTime());

            args.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
            args.putParcelable(Constants.Notifications.NOSTRA_NOTIFICATION, Parcels.wrap(notification));
            args.putParcelable(Constants.BundleKeys.ANNOUNCEMENT_SCREEN_DATA, Parcels.wrap(announcementScreenData));
        }

        Intent intent = new Intent(context, AnnouncementActivity.class);
        intent.putExtras(args);
        return intent;
    }

    @NonNull
    public Intent getWalletHomeScreenIntent(Context context, NostraNotification notification) {
        Bundle args = new Bundle();
        if (notification != null) {
            args.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
            args.putParcelable(Constants.Notifications.NOSTRA_NOTIFICATION, Parcels.wrap(notification));
        }

        Intent intent = new Intent(context, WalletHomeActivity.class);
        intent.putExtras(args);
        return intent;
    }

    @NonNull
    public Intent getKYCScreenIntent(Context context, NostraNotification notification) {
        Bundle args = new Bundle();
        if (notification != null) {
            args.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
            args.putParcelable(Constants.Notifications.NOSTRA_NOTIFICATION, Parcels.wrap(notification));
        }

        Intent intent = new Intent(context, AddKYCDetailsActivity.class);
        intent.putExtras(args);
        return intent;
    }

}
