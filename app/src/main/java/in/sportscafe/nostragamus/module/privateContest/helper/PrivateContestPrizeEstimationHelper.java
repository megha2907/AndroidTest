package in.sportscafe.nostragamus.module.privateContest.helper;

import android.support.annotation.NonNull;
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
        void updateTotalAmount(double updatedTotalAmount);
    }

    /**
     *
     * @param prizeTemplate - Template based on the calculation for prize is considered
     *                      1. Rank based templates (Top 2 wins, Top 5 wins, Top 10 wins)
     *                      2. Percentage based templates (Top 30% wins, Top 50% wins, Top 1/3rd Wins)
     *
     *                     Tamplates are received from server along with other values like company margin etc
     * @param distributableTotalPrizeAmt - Total contest amount after deduction of margin from user inputted values
     *                                   distributable = totalAmount - marginAmount
     *                                   totalAmount = fee * entries
     *                                   marginAmount = totalAmount * margin (margin always between 0 and 1)
     * @param numberOfParticipants - entries
     * @return - A list of calcuated prize
     */
    @NonNull
    public List<PrizeListItemDto> getPrizeList(PrivateContestPrizeTemplateResponse prizeTemplate,
                                               double distributableTotalPrizeAmt, int numberOfParticipants,
                                               PrivateContestPrizeEstimationListener listener) {
        List<PrizeListItemDto> prizeListItemDtoList = new ArrayList<>();

        if (prizeTemplate != null && !TextUtils.isEmpty(prizeTemplate.getShareType())
                && prizeTemplate.getPrizes() != null && distributableTotalPrizeAmt > 0) {

            /* If RANK based template chosen */
            if (prizeTemplate.getShareType().equalsIgnoreCase(Constants.PrivateContests.PrizeEstimationTemplateType.RANK)) {

                if (numberOfParticipants > prizeTemplate.getPrizes().size()) {
                    PrizeListItemDto prizeListItemDto = null;
                    double fractionTotalSum = 0;

                    for (PrivateContestPrizeResponse prizeResponse : prizeTemplate.getPrizes()) {
                        if (prizeResponse.getSharePercentage() > 0) {
                            prizeListItemDto = new PrizeListItemDto();

                            double prizeAmountDouble = distributableTotalPrizeAmt * prizeResponse.getSharePercentage();
                            long prizeAmount = (long) (distributableTotalPrizeAmt * prizeResponse.getSharePercentage());
                            fractionTotalSum = fractionTotalSum + (prizeAmountDouble - prizeAmount);

                            prizeListItemDto.setWinnerRank(prizeResponse.getWinnerRank());
                            prizeListItemDto.setAmount(prizeAmount);
                            prizeListItemDto.setSharePercent(prizeResponse.getSharePercentage() * 100);

                            prizeListItemDtoList.add(prizeListItemDto);
                        }
                    }

                    if (fractionTotalSum > 0 && distributableTotalPrizeAmt > fractionTotalSum && listener != null) {
                        listener.updateTotalAmount(distributableTotalPrizeAmt - fractionTotalSum);
                    }
                } else {
                    if (listener != null) {
                        listener.onError("Entries should be more than winners",
                                PrivateContestPrizeEstimationListener.ERR_CODE_ENTRIES_LESS_THAN_WINNERS);
                    }
                }

            } else if (prizeTemplate.getShareType().equalsIgnoreCase(Constants.PrivateContests.PrizeEstimationTemplateType.PERCENT)) {

                PrizeListItemDto prizeListItemDto = null;
                for (PrivateContestPrizeResponse prizeResponse : prizeTemplate.getPrizes()) {
                    if (prizeResponse.getWinningPercentage() >= 0 && prizeResponse.getSharePercentage() >= 0) {

                        double fractionTotalSum = 0;
                        int usersCount = 0;
                        double winningMoney = 0;
                        long prizeAmount = 0;

                    /* Percentage-of-users out of total participants who should be considered
                    combined for a share in payout-level(this iteration) */
                        float participantsRation = numberOfParticipants * prizeResponse.getWinningPercentage();

                    /* get countable-users based on percentage-of-users and total participants */
                        usersCount = (int) participantsRation;

                    /* Percentage-of-prizeTemplate out of total prizeTemplate which is to be shared equally for
                     * countable-users in payout-level(this iteration)  */
                        winningMoney = distributableTotalPrizeAmt * prizeResponse.getSharePercentage();
                        if (winningMoney > 0 && usersCount > 0) {
                            double prizeAmountDouble = winningMoney / usersCount;
                            prizeAmount = (long) (winningMoney / usersCount);

                        /* For every countable-user */
                        float sharePercentage = (prizeResponse.getSharePercentage() * 100) / usersCount;
                            for (int count = 1; count <= usersCount; count++) {
                                prizeListItemDto = new PrizeListItemDto();
                                prizeListItemDto.setWinnerRank(count);
                                prizeListItemDto.setAmount(prizeAmount);
                                prizeListItemDto.setSharePercent(sharePercentage);

                                fractionTotalSum = fractionTotalSum + (prizeAmountDouble - prizeAmount);

                                prizeListItemDtoList.add(prizeListItemDto);
                            }

                            if (fractionTotalSum > 0 && distributableTotalPrizeAmt > fractionTotalSum && listener != null) {
                                listener.updateTotalAmount(distributableTotalPrizeAmt - fractionTotalSum);
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
     * @param prizeFee - User given entry fee per entry
     * @param entries - max participants
     * @param margin - server sent margin percentage (ALWAYS between 0 and 1)
     * @return - returns amount to distribute among users after deducting margins
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
