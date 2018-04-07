package in.sportscafe.nostragamus.module.privateContest.helper;

import android.text.TextUtils;

import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto.PrivateContestPrizeResponse;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto.PrivateContestPrizeTemplateResponse;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto.PrizeListItemDto;

/**
 * Created by sc on 23/3/18.
 */

public class PrivateContestPrizeEstimationHelper {

    private static final String TAG = PrivateContestPrizeEstimationHelper.class.getSimpleName();

    public interface PrivateContestPrizeEstimationListener {
        int ERR_CODE_ENTRIES_LESS_THAN_WINNERS = 121;

        void onError(String msg, int errorCode);
    }

    /**
     *
     * @param prizeTemplate
     * @param distributableTotalPrizeAmt
     * @param numberOfParticipants
     * @return
     */
    public List<PrizeListItemDto> getPrizeList(PrivateContestPrizeTemplateResponse prizeTemplate,
                                               double distributableTotalPrizeAmt, int numberOfParticipants,
                                               PrivateContestPrizeEstimationListener listener) {
        List<PrizeListItemDto> prizeListItemDtoList = null;

        if (prizeTemplate != null && !TextUtils.isEmpty(prizeTemplate.getShareType())
                && prizeTemplate.getPrizes() != null && distributableTotalPrizeAmt > 0) {
            prizeListItemDtoList = new ArrayList<>();


            /* If RANK based template chosen */
            if (prizeTemplate.getShareType().equalsIgnoreCase(Constants.PrivateContests.PrizeEstimationTemplateType.RANK)) {

                if (numberOfParticipants > prizeTemplate.getPrizes().size()) {
                    PrizeListItemDto prizeListItemDto = null;

                    for (PrivateContestPrizeResponse prizeResponse : prizeTemplate.getPrizes()) {
                        if (prizeResponse.getSharePercentage() > 0) {
                            prizeListItemDto = new PrizeListItemDto();

                            double prizeAmount = distributableTotalPrizeAmt * prizeResponse.getSharePercentage();

                            prizeListItemDto.setWinnerRank(prizeResponse.getWinnerRank());
                            prizeListItemDto.setAmount(prizeAmount);
                            prizeListItemDto.setSharePercent(prizeResponse.getSharePercentage() * 100);

                            prizeListItemDtoList.add(prizeListItemDto);
                        }
                    }
                } else {
                    if (listener != null) {
                        listener.onError("Entries are less than winners", PrivateContestPrizeEstimationListener.ERR_CODE_ENTRIES_LESS_THAN_WINNERS);
                    }
                }


            } else if (prizeTemplate.getShareType().equalsIgnoreCase(Constants.PrivateContests.PrizeEstimationTemplateType.PERCENT)) {

                PrizeListItemDto prizeListItemDto = null;
                for (PrivateContestPrizeResponse prizeResponse : prizeTemplate.getPrizes()) {
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

                    /* Percentage-of-prizeTemplate out of total prizeTemplate which is to be shared equally for
                     * countable-users in payout-level(this iteration)  */
                        winningMoney = distributableTotalPrizeAmt * prizeResponse.getSharePercentage();
                        if (winningMoney > 0 && usersCount > 0) {
                            prizeAmount = (int) (winningMoney / usersCount);

                        /* For every countable-user */
                        float sharePercentage = (prizeResponse.getSharePercentage() * 100) / usersCount;
                            for (int count = 1; count <= usersCount; count++) {
                                prizeListItemDto = new PrizeListItemDto();
                                prizeListItemDto.setWinnerRank(count);
                                prizeListItemDto.setAmount(prizeAmount);
                                prizeListItemDto.setSharePercent(sharePercentage);

                                prizeListItemDtoList.add(prizeListItemDto);
                            }
                        }

                        Log.d(TAG, "\n ----------- \n" +
                                "\n Total-participants : " + numberOfParticipants +
                                "\n Total-prizeTemplate (calculated) : " + distributableTotalPrizeAmt +
                                "\n Margin :" + prizeTemplate.getMargin() +
                                "\n ------------- " +
                                "\n %-of-user(percent) : " + participantsRation + "("+prizeResponse.getWinningPercentage()+")" +
                                "\n countable-user (based on %) :" + usersCount +
                                "\n step/rounding : " + "" +
                                "\n %-of-prizeTemplate (share) : " + winningMoney + "("+prizeResponse.getSharePercentage()+")" +
                                "\n prizeAmountPerUser : " + prizeAmount);
                    }
                }

            }
        }

        return prizeListItemDtoList;
    }

    /**
     * Reduces margin/profit amt from total prize-amt
     * @param prizeFee
     * @param entries
     * @param margin
     * @return
     */
    public static synchronized double getDistributableTotalPrize(double prizeFee, int entries, float margin) {
        double prizeAmt = 0;

        double totalAmount = prizeFee * entries;
        if (margin > 0 && totalAmount > 0) {
            double marginAmt = totalAmount * margin;
            prizeAmt = totalAmount - marginAmt;
        }

        return prizeAmt;
    }

}
