package in.sportscafe.nostragamus.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.Calendar;
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
 *
 * Used to send Offline [InApp] notifications about match start reminders.
 * NOTE : In-App notifications logic depends on DB data availability & accuracy
 *
 * Steps:
 * 1. Based on alarm set, whenever this service is started...
 * 2. Get In-Play data from Database
 * 3. Get serverTime saved from Database
 * 4. Filter In-Play data list for reminder notification as
 * 4.1. Calculate current approximate server time based on DB-server time
 * 4.2. Calculate match-start time for each inplay match that they are satisfying reminder conditions to send notifications
 * 4.2. [   1. Match not started
 *          2. Reminder time period meeting condition according to approx-serverTime and match-start-time
 *          3. Match is not played by user
 *      ]
 */

public class InAppNotificationService extends IntentService {

    private static final String TAG = InAppNotificationService.class.getSimpleName();

    /**
     * This number defines that before match start, how much time prior to that; notification should be sent
     * e.g. send notification before 1hr (or less than 1 hr) - then 60 minutes
     *      send notification before 3hrs (within 3 hours time period) - then 180 minutes
     */
    public static final int DAY_TIME_PRIOR_MATCH_STARTS_MINUTES = 60;     /* Minutes */

    /**
     * Time prior to match start time to send notification while NIGHT..
     * 12AM to 7AM - is considered NIGHT
     * As alarm can fire service anytime between a period of hours; 7-hours
     */
    public static final int NIGHT_TIME_PRIOR_MATCH_STARTS_MINUTES = 420;     /* Minutes */

    public InAppNotificationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "InApp notification service started - onHandle");

        FetchInPlayDataFromDatabase();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "InApp notification service exiting - onDestroy");
        super.onDestroy();
    }

    private void FetchInPlayDataFromDatabase() {
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
            Log.d(TAG, "Database response received ");

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
                        onCachedServerTimeReceived(serverTimeDbDto, inPlayResponseList);
                    } else {
                        Log.i(TAG, "Server time from database is null - can not continue for InAppNotifications");
                    }
            }
        }.execute();
    }

    private void onCachedServerTimeReceived(ServerTimeDbDto serverTimeDbDto,
                                            List<InPlayResponse> inPlayResponseList) {
        long approxServerTime = Nostragamus.getInstance().getApproxTimeBasedOnSavedServerTime(serverTimeDbDto);
        if (approxServerTime > 0) {

            int minutesPriorMatchStart = 0;
            if (DateTimeHelper.isDayTime(approxServerTime)) {
                minutesPriorMatchStart = DAY_TIME_PRIOR_MATCH_STARTS_MINUTES;
            } else {
                minutesPriorMatchStart = getNightMinutes(approxServerTime);
            }

            filterMatchesAndSendNotifications(inPlayResponseList, approxServerTime, minutesPriorMatchStart);
        }
    }

    private void filterMatchesAndSendNotifications(List<InPlayResponse> inPlayResponseList,
                                                   long approxServerTime, int minutesPriorMatchStart) {
        InAppNotificationHelper notificationHelper = new InAppNotificationHelper();

        if (inPlayResponseList != null) {
            for (InPlayResponse inPlayResponse : inPlayResponseList) {      /* For every Challenge */

                if (inPlayResponse.getContestList() != null) {
                    for (InPlayContestDto inPlayContestDto : inPlayResponse.getContestList()) {     /* For every Contest in Challenge */

                        if (inPlayContestDto.getMatches() != null) {
                            for (InPlayContestMatchDto matchDto : inPlayContestDto.getMatches()) {  /* For every Match in Contest */

                                String startTime = matchDto.getStartTime();
                                if (!TextUtils.isEmpty(startTime)) {
                                    long matchStartRemainingMinutes = DateTimeHelper.getMatchStartRemainingTimeInMinutesForInAppNoti(startTime, approxServerTime);

                                    if (Constants.GameAttemptedStatus.NOT == matchDto.isPlayed() /* Match NOT played */
                                            && (matchStartRemainingMinutes > 0 && matchStartRemainingMinutes <= minutesPriorMatchStart) /* Match will start within 1 hour, also Match NOT started */) {

                                        /* Required to launch screens & then fetch server data */
                                        inPlayContestDto.setChallengeId(inPlayResponse.getChallengeId());
                                        inPlayContestDto.setChallengeName(inPlayResponse.getChallengeName());
                                        inPlayContestDto.setChallengeStartTime(inPlayResponse.getChallengeStartTime());

                                        sendNotification(notificationHelper, inPlayResponse, inPlayContestDto, matchDto);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void sendNotification(InAppNotificationHelper notificationHelper,
                                  InPlayResponse inPlayResponse, InPlayContestDto inPlayContestDto,
                                  InPlayContestMatchDto matchDto) {

        String msg = "Predict now and stay in the race to win " + inPlayResponse.getChallengeName();
        String subTitle = "The game starts at " + DateTimeHelper.getInAppMatchStartTime(matchDto.getStartTime());

        notificationHelper.sendNotification(getApplicationContext(),
                matchDto.getParties(), msg, subTitle, inPlayContestDto);
    }

    /**
     * As alarm can get fired any time (Not exact 10PM, 11PM); need to add extra minutes to get exact night-timings
     * @param millis
     * @return
     */
    private int getNightMinutes(long millis) {
        int nightMinutes = NIGHT_TIME_PRIOR_MATCH_STARTS_MINUTES;

        if (millis > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);
            int hoursOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);

            if (hoursOfDay == 23 /* 11PM */) {
                nightMinutes += (60 - minutes);
            }
        }

        return nightMinutes;
    }
}
