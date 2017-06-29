package in.sportscafe.nostragamus.module.navigation.wallet;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.text.DecimalFormat;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.navigation.wallet.dto.UserWalletResponse;
import in.sportscafe.nostragamus.module.user.login.dto.UserPaymentInfo;
import in.sportscafe.nostragamus.module.user.login.dto.UserPaymentInfoBankDto;
import in.sportscafe.nostragamus.module.user.login.dto.UserPaymentInfoPaytmDto;

/**
 * Created by sandip on 12/06/17.
 */

public class WalletHelper {

    /**
     * Returns string with formatted amount (2 decimal) and rupee symbols
     * @param amount
     * @return
     */
    public synchronized static String getFormattedStringOfAmount(double amount) {
        String str = "";

        if (amount >= 0) {
            DecimalFormat df = new DecimalFormat(Constants.AMOUNT_DECIMAL_PATTERN);
            str = Constants.RUPEE_SYMBOL + df.format(amount);
        }

        return str;
    }

    /**
     *
     * @return balance amount from ServerDataManager
     */
    public synchronized static double getDepositAmount() {
        double amount = 0;

        UserWalletResponse userWalletResponse = Nostragamus.getInstance().getServerDataManager().getUserWalletResponse();
        if (userWalletResponse != null) {
            amount = userWalletResponse.getDepositAmount();
        }

        return amount;
    }

    /**
     *
     * @return promo amount from serverDataManager
     */
    public synchronized static double getPromoAmount() {
        double promoAmount = 0;

        UserWalletResponse userWalletResponse = Nostragamus.getInstance().getServerDataManager().getUserWalletResponse();
        if (userWalletResponse != null) {
            promoAmount = userWalletResponse.getPromoAmount();
        }

        return promoAmount;
    }

    public synchronized static double getWinningAmount() {
        double winningAmount = 0;

        UserWalletResponse userWalletResponse = Nostragamus.getInstance().getServerDataManager().getUserWalletResponse();
        if (userWalletResponse != null) {
            winningAmount = userWalletResponse.getWinningsAmount();
        }

        return winningAmount;
    }


    public synchronized static boolean isPaytmPayoutDetailsProvided() {
        boolean isProvided = false;

        UserWalletResponse userWalletResponse = Nostragamus.getInstance().getServerDataManager().getUserWalletResponse();
        if (userWalletResponse != null && userWalletResponse.getUserPaymentInfo() != null) {
            UserPaymentInfoPaytmDto paytm = userWalletResponse.getUserPaymentInfo().getPaytm();
            if (paytm != null && !TextUtils.isEmpty(paytm.getMobile())) {
                isProvided = true;
            }
        }

        return isProvided;
    }

    public synchronized static boolean isBankPayoutDetailsProvided() {
        boolean isProvided = false;

        UserWalletResponse userWalletResponse = Nostragamus.getInstance().getServerDataManager().getUserWalletResponse();
        if (userWalletResponse != null && userWalletResponse.getUserPaymentInfo() != null) {
            UserPaymentInfoBankDto bank = userWalletResponse.getUserPaymentInfo().getBank();
            if (bank != null && !TextUtils.isEmpty(bank.getAccountNo()) && !TextUtils.isEmpty(bank.getIfscCode())) {
                isProvided = true;
            }
        }

        return isProvided;
    }

    public synchronized static @Nullable UserPaymentInfoPaytmDto getPaytm() {
        UserPaymentInfoPaytmDto paytmDto = null;
        UserWalletResponse userWalletResponse = Nostragamus.getInstance().getServerDataManager().getUserWalletResponse();
        if (userWalletResponse != null && userWalletResponse.getUserPaymentInfo() != null) {
            paytmDto = userWalletResponse.getUserPaymentInfo().getPaytm();
        }
        return paytmDto;
    }

    public synchronized static @Nullable UserPaymentInfoBankDto getBank() {
        UserPaymentInfoBankDto bankDto = null;
        UserWalletResponse userWalletResponse = Nostragamus.getInstance().getServerDataManager().getUserWalletResponse();
        if (userWalletResponse != null && userWalletResponse.getUserPaymentInfo() != null) {
            bankDto = userWalletResponse.getUserPaymentInfo().getBank();
        }
        return bankDto;
    }

    public synchronized static @Nullable UserPaymentInfo getUserPaymentInfo() {
        UserPaymentInfo userPaymentInfo = null;
        UserWalletResponse userWalletResponse = Nostragamus.getInstance().getServerDataManager().getUserWalletResponse();
        if (userWalletResponse != null && userWalletResponse.getUserPaymentInfo() != null) {
            userPaymentInfo = userWalletResponse.getUserPaymentInfo();
        }
        return userPaymentInfo;
    }

    public synchronized static int getWithdrawalsInProgress() {
        int inProgress = 0;

        UserWalletResponse response = Nostragamus.getInstance().getServerDataManager().getUserWalletResponse();
        if (response != null && response.getWithdrawalProgress() > 0) {
            inProgress = response.getWithdrawalProgress();
        }

        return inProgress;
    }

    public synchronized static int getPayoutAccountsAdded() {
        int accountAdded = 0;

        UserPaymentInfoPaytmDto paytm = getPaytm();
        UserPaymentInfoBankDto bank = getBank();

        if (paytm != null && !TextUtils.isEmpty(paytm.getMobile())) {
            accountAdded += 1;  // Paytm details are added
        }
        if (bank != null && !TextUtils.isEmpty(bank.getAccountNo()) && !TextUtils.isEmpty(bank.getIfscCode())) {
            accountAdded += 1;  // Bank details added
        }

        return accountAdded;
    }

    /**
     * NOTE: This considers only BALANCE-AMOUNT and PROMO-MONEY
     * @param balRequired bal required to join challenge
     * @return true if sufficient bal available in wallet
     */
    public synchronized static boolean isSufficientBalAvailableInWallet(double balRequired) {
        boolean balAvailable = false;

        double total = getTotalBalance();
        if (total >= 0 && balRequired >= 0 && balRequired <= total) {
            balAvailable = true;
        }

        return balAvailable;
    }

    public synchronized static double getTotalBalance() {
        double total = 0;

        UserWalletResponse userWalletResponse = Nostragamus.getInstance().getServerDataManager().getUserWalletResponse();
        if (userWalletResponse != null) {
            double depositAmount = WalletHelper.getDepositAmount();
            double promoMoney = WalletHelper.getPromoAmount();
            double winningAmount = WalletHelper.getWinningAmount();

            if (depositAmount > 0) {
                total += depositAmount;
            }
            if (promoMoney > 0) {
                total += promoMoney;
            }
            if (winningAmount > 0) {
                total += winningAmount;
            }
        }

        return total;
    }

    /**
     * Adds given extra amount into given base-amount string, else return extra-amount if exception found.
     * @param baseAmountStr
     * @param extraAmtToAdd
     * @return
     */
    public static synchronized double addMoreAmount(String baseAmountStr, double extraAmtToAdd) {
        double addedAmt = extraAmtToAdd;

        if (!TextUtils.isEmpty(baseAmountStr)) {
            try {
                double baseAmt = Double.parseDouble(baseAmountStr);
                if (extraAmtToAdd > 0) {
                    baseAmt = baseAmt + extraAmtToAdd;
                }
                if (baseAmt > 0) {
                    addedAmt = baseAmt;
                }
            } catch (NumberFormatException nex) {
                nex.printStackTrace();
            }
        }

        return addedAmt;
    }
}
