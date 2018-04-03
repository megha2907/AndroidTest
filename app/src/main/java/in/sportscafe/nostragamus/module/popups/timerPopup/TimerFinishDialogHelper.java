package in.sportscafe.nostragamus.module.popups.timerPopup;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivity;
import in.sportscafe.nostragamus.module.nostraHome.helper.TimerHelper;

/**
 * Created by sandip on 09/10/17.
 */

public class TimerFinishDialogHelper {

    private static TimerFinishDialogScreenData getCanNotJoinScreenData() {
        TimerFinishDialogScreenData screenData = new TimerFinishDialogScreenData();
        screenData.setRequestCode(Constants.TimerOutDialogRequestCode.CAN_NOT_JOIN);
        screenData.setDialogTitle("Can't join challenge");
        screenData.setMessage("Oops! Run out of time!");
        screenData.setSubMessage("You can't join this challenge as it has started! Join other challenges from the challenge tab.");
        screenData.setButtonText("Go to Challenges");

        return screenData;
    }

    private static TimerFinishDialogScreenData getCanNotPlayGameScreenData() {
        TimerFinishDialogScreenData screenData = new TimerFinishDialogScreenData();
        screenData.setRequestCode(Constants.TimerOutDialogRequestCode.CAN_NOT_PLAY_GAME);
        screenData.setDialogTitle("Can't play game");
        screenData.setMessage("Oops! Time is up!");
        screenData.setSubMessage("cannot play this game as timer has elapsed! You can still play other games that are available");
        screenData.setButtonText("Go to Games List");

        return screenData;
    }

    private static TimerFinishDialogScreenData getChallengeStartedScreenData(String subMessage) {
        TimerFinishDialogScreenData screenData = new TimerFinishDialogScreenData();
        screenData.setRequestCode(Constants.TimerOutDialogRequestCode.CHALLENGE_STARTED);
        screenData.setDialogTitle("Challenge Started");
        screenData.setMessage("Oops! Time is up!");
        screenData.setSubMessage(subMessage);
        screenData.setButtonText("Ok");

        return screenData;
    }

    private static TimerFinishDialogScreenData getPrivateContestFullScreenData(String subMessage) {
        TimerFinishDialogScreenData screenData = new TimerFinishDialogScreenData();
        screenData.setRequestCode(Constants.TimerOutDialogRequestCode.PRIVATE_CONTEST_FULL);
        screenData.setDialogTitle("Contest Full");
        screenData.setMessage("Private Contest Full");
        screenData.setSubMessage(subMessage);
        screenData.setButtonText("Ok");
        screenData.setIconResource(R.drawable.private_contest_error_dialog_icon);

        return screenData;
    }

    private static TimerFinishDialogScreenData getPrivateContestUnknownErrorScreenData(String subMessage) {
        TimerFinishDialogScreenData screenData = new TimerFinishDialogScreenData();
        screenData.setRequestCode(Constants.TimerOutDialogRequestCode.CAN_NOT_JOIN_PRIVATE_CONTEST);
        screenData.setDialogTitle("Private Contest");
        screenData.setMessage("Unknown Error");
        screenData.setSubMessage(subMessage);
        screenData.setButtonText("Ok");
        screenData.setIconResource(R.drawable.private_contest_error_dialog_icon);

        return screenData;
    }

    public static boolean canJoinContest(String startTimeStr) {
        boolean canJoin = false;

        if (!TextUtils.isEmpty(startTimeStr)) {
            long startTime =  TimerHelper.getCountDownFutureTime(startTimeStr);
            if (startTime >= 1000) {
                canJoin = true;
            }
        }

        return canJoin;
    }

    public static boolean canPlayGame(String gameStartTimeStr) {
        boolean canJoin = false;

        if (!TextUtils.isEmpty(gameStartTimeStr)) {
            long startTime = TimerHelper.getCountDownFutureTime(gameStartTimeStr);
            if (startTime >= 1000) {
                canJoin = true;
            }
        }

        return canJoin;
    }

    public static void showCanNotJoinTimerOutDialog(final AppCompatActivity activityContext) {
        TimerFinishedDialogFragment dialogFragment = TimerFinishedDialogFragment.newInstance(getCanNotJoinScreenData(),
                new TimerFinishedDialogFragment.TimerFinishedFragmentListener() {
                    @Override
                    public void onActionButtonClicked() {
                        Intent homeChallengeIntent = new Intent(activityContext, NostraHomeActivity.class);
                        homeChallengeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        activityContext.startActivity(homeChallengeIntent);
                    }
                }
        );

        dialogFragment.showDialogAllowingStateLoss(activityContext.getSupportFragmentManager(),dialogFragment, dialogFragment.getClass().getSimpleName());
    }

    public static void showCanNotPlayGameTimerOutDialog(FragmentManager fragmentManager) {
        TimerFinishedDialogFragment dialogFragment = TimerFinishedDialogFragment.newInstance(getCanNotPlayGameScreenData(),
                new TimerFinishedDialogFragment.TimerFinishedFragmentListener() {
                    @Override
                    public void onActionButtonClicked() {
                    }
                }
        );

        dialogFragment.showDialogAllowingStateLoss(fragmentManager,dialogFragment, dialogFragment.getClass().getSimpleName());
    }

    public static void showChallengeStartedTimerOutDialog(FragmentManager fragmentManager, String subMessage,
                                                          final View.OnClickListener onClickListener) {

        TimerFinishedDialogFragment dialogFragment = TimerFinishedDialogFragment.newInstance(getChallengeStartedScreenData(subMessage),
                new TimerFinishedDialogFragment.TimerFinishedFragmentListener() {
                    @Override
                    public void onActionButtonClicked() {
                        if (onClickListener != null) {
                            onClickListener.onClick(null);
                        }
                    }
                }
        );

        dialogFragment.showDialogAllowingStateLoss(fragmentManager, dialogFragment, dialogFragment.getClass().getSimpleName());
    }

    public static void showPrivateContestFullDialog(FragmentManager fragmentManager, String subMessage,
                                                          final View.OnClickListener onClickListener) {

        TimerFinishedDialogFragment dialogFragment =
                TimerFinishedDialogFragment.newInstance(getPrivateContestFullScreenData(subMessage),
                new TimerFinishedDialogFragment.TimerFinishedFragmentListener() {
                    @Override
                    public void onActionButtonClicked() {
                        if (onClickListener != null) {
                            onClickListener.onClick(null);
                        }
                    }
                }
        );

        dialogFragment.showDialogAllowingStateLoss(fragmentManager,
                dialogFragment, dialogFragment.getClass().getSimpleName());
    }

    public static void showPrivateContestUnknownErrorDialog(FragmentManager fragmentManager, String subMessage,
                                                    final View.OnClickListener onClickListener) {

        TimerFinishedDialogFragment dialogFragment =
                TimerFinishedDialogFragment.newInstance(getPrivateContestUnknownErrorScreenData(subMessage),
                        new TimerFinishedDialogFragment.TimerFinishedFragmentListener() {
                            @Override
                            public void onActionButtonClicked() {
                                if (onClickListener != null) {
                                    onClickListener.onClick(null);
                                }
                            }
                        }
                );

        dialogFragment.showDialogAllowingStateLoss(fragmentManager,
                dialogFragment, dialogFragment.getClass().getSimpleName());
    }


}
