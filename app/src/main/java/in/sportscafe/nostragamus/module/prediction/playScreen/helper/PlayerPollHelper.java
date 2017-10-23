package in.sportscafe.nostragamus.module.prediction.playScreen.helper;

import android.text.TextUtils;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayersPoll;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PredictionQuestion;

/**
 * Created by sandip on 21/10/17.
 */

public class PlayerPollHelper {

    public static String getLeftAnswerString(List<PlayersPoll> playersPollList) {
        String str = "";

        if (playersPollList != null && !playersPollList.isEmpty()) {
            for (PlayersPoll playersPoll : playersPollList) {
                if (playersPoll.getAnswerId() == Constants.AnswerIds.LEFT) {
                    str = playersPoll.getAnswerPercentage();
                    break;
                }
            }
        }

        return str;
    }

    public static String getRightAnswerString(List<PlayersPoll> playersPollList) {
        String str = "";

        if (playersPollList != null && !playersPollList.isEmpty()) {
            for (PlayersPoll playersPoll : playersPollList) {
                if (playersPoll.getAnswerId() == Constants.AnswerIds.RIGHT) {
                    str = playersPoll.getAnswerPercentage();
                    break;
                }
            }
        }

        return str;
    }

    public static String getNeitherAnswerString(List<PlayersPoll> playersPollList) {
        String str = "";

        if (playersPollList != null && !playersPollList.isEmpty()) {
            for (PlayersPoll playersPoll : playersPollList) {
                if (playersPoll.getAnswerId() == Constants.AnswerIds.NEITHER) {
                    str = playersPoll.getAnswerPercentage();
                    break;
                }
            }
        }

        return str;
    }

    private static float getPercentageFromAnswerPollStr(String string) {
        float result = 0f;

        if (!TextUtils.isEmpty(string)) {
            string = string.replaceAll("%","");

            try {
                result = Float.parseFloat(string);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return result;
    }

    public static void setMinorityAnswerId(PredictionQuestion question, String leftPlayerPollAnswerStr, String rightPlayerPollAnswerStr) {
        if (question != null) {

            float leftPollAnswer = 0, rightPollAnswer = 0;
            if (!TextUtils.isEmpty(leftPlayerPollAnswerStr) && !TextUtils.isEmpty(rightPlayerPollAnswerStr)) {
                leftPollAnswer = getPercentageFromAnswerPollStr(leftPlayerPollAnswerStr);
                rightPollAnswer = getPercentageFromAnswerPollStr(rightPlayerPollAnswerStr);
            }

            if (leftPollAnswer > rightPollAnswer) {
                question.setMinorityAnswerId(Constants.AnswerIds.RIGHT);
            } else if (leftPollAnswer < rightPollAnswer) {
                question.setMinorityAnswerId(Constants.AnswerIds.LEFT);
            } else {
                question.setMinorityAnswerId(-1);
            }
        }
    }
}
