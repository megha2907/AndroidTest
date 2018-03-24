package in.sportscafe.nostragamus.module.privateContest.helper;

import android.text.TextUtils;

import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.privateContest.dto.PrivateContestPrizeResponse;
import in.sportscafe.nostragamus.module.privateContest.dto.PrivateContestPrizeTemplateResponse;
import in.sportscafe.nostragamus.module.privateContest.dto.PrizeListItemDto;

/**
 * Created by sc on 23/3/18.
 */

public class PrivateContestPrizeEstimationHelper {

    private static final String TAG = PrivateContestPrizeEstimationHelper.class.getSimpleName();

    public interface PrivateContestPrizeEstimationListener {
        void onError(String msg);
    }

    /**
     *
     * @param prize
     * @param totalPrizeAmount
     * @param numberOfParticipants
     * @return
     */
    public List<PrizeListItemDto> getPrizeList(PrivateContestPrizeTemplateResponse prize,
                                               double totalPrizeAmount, int numberOfParticipants,
                                               PrivateContestPrizeEstimationListener listener) {
        List<PrizeListItemDto> prizeListItemDtoList = null;

        if (prize != null && !TextUtils.isEmpty(prize.getShareType()) && prize.getPrizes() != null && totalPrizeAmount > 0) {
            prizeListItemDtoList = new ArrayList<>();

            if (prize.getShareType().equalsIgnoreCase(Constants.PrivateContests.PrizeEstimationTemplateType.RANK)) {

                if (numberOfParticipants > prize.getPrizes().size()) {
                    PrizeListItemDto prizeListItemDto = null;

                    for (PrivateContestPrizeResponse prizeResponse : prize.getPrizes()) {
                        if (prizeResponse.getSharePercentage() > 0) {
                            prizeListItemDto = new PrizeListItemDto();

                            double prizeAmount = totalPrizeAmount * prizeResponse.getSharePercentage();

                            prizeListItemDto.setWinnerRank(prizeResponse.getWinnerRank());
                            prizeListItemDto.setAmount(prizeAmount);
                            prizeListItemDto.setSharePercent(prizeResponse.getSharePercentage() * 100);

                            prizeListItemDtoList.add(prizeListItemDto);
                        }
                    }
                } else {
                    if (listener != null) {
                        listener.onError("Entries are less than winners");
                    }
                }


            } else if (prize.getShareType().equalsIgnoreCase(Constants.PrivateContests.PrizeEstimationTemplateType.PERCENT)) {

                PrizeListItemDto prizeListItemDto = null;
                for (PrivateContestPrizeResponse prizeResponse : prize.getPrizes()) {
                    if (prizeResponse.getWinningPercentage() >= 0 && prizeResponse.getSharePercentage() >= 0) {

                        int prizeAmount = 0, usersCount = 0;
                        double winningMoney = 0;

                    /* Percentage-of-users out of total participants who should be considered
                    combined for a share in payout-level(this iteration) */
                        float participantsRation = numberOfParticipants * prizeResponse.getWinningPercentage();

                    /* get countable-users based on percentage-of-users and total participants */
                        usersCount = (int) participantsRation;

                    /* If percentage-of-users number is fraction decimal THEN
                     * based on values of rounding/step from api,
                     * ONLY for UP , increase a countable-user */
                       /* if (participantsRation % 1 != 0 && !TextUtils.isEmpty(rounding) && rounding.equalsIgnoreCase("up")) {
                            usersCount++;
                        }*/

                    /* Percentage-of-prize out of total prize which is to be shared equally for
                     * countable-users in payout-level(this iteration)  */
                        winningMoney = totalPrizeAmount * prizeResponse.getSharePercentage();
                        if (winningMoney > 0 && usersCount > 0) {
                            prizeAmount = (int) (winningMoney / usersCount);

                        /* For every countable-user */
                            for (int count = 1; count <= usersCount; count++) {
                                prizeListItemDto = new PrizeListItemDto();
                                prizeListItemDto.setWinnerRank(count);
                                prizeListItemDto.setAmount(prizeAmount);
                                prizeListItemDto.setSharePercent(prizeResponse.getSharePercentage() * 100);

                                prizeListItemDtoList.add(prizeListItemDto);
                            }
                        }

                        Log.d(TAG, "\n ----------- \n" +
                                "\n Total-participants : " + numberOfParticipants +
                                "\n Total-prize (calculated) : " + totalPrizeAmount +
                                "\n ------------- " +
                                "\n %-of-user(percent) : " + participantsRation + "("+prizeResponse.getWinningPercentage()+")" +
                                "\n countable-user (based on %) :" + usersCount +
                                "\n step/rounding : " + "" +
                                "\n %-of-prize (share) : " + winningMoney + "("+prizeResponse.getSharePercentage()+")" +
                                "\n prizeAmountPerUser : " + prizeAmount);
                    }
                }

            }
        }

        return prizeListItemDtoList;
    }
}
