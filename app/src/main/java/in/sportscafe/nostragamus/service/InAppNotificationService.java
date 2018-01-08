package in.sportscafe.nostragamus.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.db.DbConstants;
import in.sportscafe.nostragamus.db.NostragamusDatabase;
import in.sportscafe.nostragamus.db.tableDto.ServerTimeDbDto;
import in.sportscafe.nostragamus.module.inPlay.dataProvider.InAppNotificationsInplayDataProvider;
import in.sportscafe.nostragamus.module.inPlay.dataProvider.InPlayDataProvider;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestMatchDto;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayResponse;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;
import in.sportscafe.nostragamus.module.notifications.inApp.InAppNotificationHelper;

/**
 * Created by sc on 5/1/18.
 */

public class InAppNotificationService extends IntentService {

    private static final String TAG = InAppNotificationService.class.getSimpleName();
    public static final int MINUTES_PRIOR_MATCH_STARTS_TO_SEND_NOTIFICATIONS = 500;     /* Minutes */

    public InAppNotificationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "InApp notification service started");

        final InAppNotificationsInplayDataProvider inplayDataProvider = new InAppNotificationsInplayDataProvider();
        inplayDataProvider.loadInPlayFromDatabase(getApplicationContext(), new InPlayDataProvider.InPlayDataProviderListener() {
            @Override
            public void onData(int status, @Nullable List<InPlayResponse> inPlayResponseList) {
                switch (status) {
                    case Constants.DataStatus.FROM_DATABASE_CACHED_DATA:
                        onDataReceived(inPlayResponseList);
                        break;

                    default:
                        Log.e(TAG, "Improper data status from database , no data, can not continue in-app notifications");
                        break;
                }
            }

            @Override
            public void onError(int status) {
                Log.e(TAG, "Error in getting data from database, can not continue for in-app notifications");
            }
        });
    }

    private void onDataReceived(List<InPlayResponse> inPlayResponseList) {
        if (inPlayResponseList != null) {

            getSavedServerTimeFromDatabaseAndContinue(inPlayResponseList);
        } else {
            Log.e(TAG, "Database response null - Cant continue for In-app notification");
        }
    }

    private void getSavedServerTimeFromDatabaseAndContinue(final List<InPlayResponse> inPlayResponseList) {

        new AsyncTask<Void, Void, ServerTimeDbDto>() {

            @Override
            protected ServerTimeDbDto doInBackground(Void... params) {
                try {
                    Object object = NostragamusDatabase.getInstance(getApplicationContext()).select(DbConstants.TableIds.SERVER_TIME_TABLE);
                    return (ServerTimeDbDto) object;
                } catch (Exception ex) {}
                return null;
            }

            @Override
            protected void onPostExecute(ServerTimeDbDto serverTimeDbDto) {
                super.onPostExecute(serverTimeDbDto);

                    if (serverTimeDbDto != null) {
                        Log.i(TAG, "Cached serverTime received");
                        checkAndSendInAppNotification(serverTimeDbDto, inPlayResponseList);
                    } else {
                        Log.i(TAG, "Server time from database is null - can not continue for InAppNotifications");
                    }
            }
        }.execute();
    }

    private void checkAndSendInAppNotification(ServerTimeDbDto serverTimeDbDto, List<InPlayResponse> inPlayResponseList) {
        int notificationCount = 0;
        if (inPlayResponseList != null) {
            InAppNotificationHelper notificationHelper = new InAppNotificationHelper();
            ArrayList<String> notificationMsgList = new ArrayList<>();

            for (InPlayResponse inPlayResponse : inPlayResponseList) {      /* For every Challenge */

                if (inPlayResponse.getContestList() != null) {
                    for (InPlayContestDto inPlayContestDto : inPlayResponse.getContestList()) {     /* For every Contest in Challenge */

                        if (inPlayContestDto.getMatches() != null) {
                            for (InPlayContestMatchDto matchDto : inPlayContestDto.getMatches()) {  /* For every Match in Contest */

                                String startTime = matchDto.getStartTime();
                                if (!TextUtils.isEmpty(startTime)) {

                                    long approxServerTime = Nostragamus.getInstance().getApproxTimeBasedOnSavedServerTime(serverTimeDbDto);
                                    long matchStartRemainingMinutes = DateTimeHelper.getMatchStartRemainingTimeInMinutesForInAppNoti(startTime, approxServerTime);

                                    if (Constants.GameAttemptedStatus.NOT == matchDto.isPlayed() /* Match NOT played */
                                            && (matchStartRemainingMinutes > 0 && matchStartRemainingMinutes <= MINUTES_PRIOR_MATCH_STARTS_TO_SEND_NOTIFICATIONS) /* Match will start within 1 hour, also Match NOT started */) {

                                        notificationCount++;
                                        String msg = "A Game starts in " + matchStartRemainingMinutes + " mins";
                                        notificationMsgList.add(msg);

                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (notificationMsgList.size() > 0) {
                notificationHelper.sendNotification(getApplicationContext(), "Nostragamus",
                        "", notificationMsgList);
            }
        }
    }
}
