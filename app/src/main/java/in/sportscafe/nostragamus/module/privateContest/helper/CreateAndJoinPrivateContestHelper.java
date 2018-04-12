package in.sportscafe.nostragamus.module.privateContest.helper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.contest.contestDetailsBeforeJoining.CompletePaymentDialogFragment;
import in.sportscafe.nostragamus.module.contest.dto.JoinContestData;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletApiModelImpl;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.dto.UserWalletResponse;
import in.sportscafe.nostragamus.module.privateContest.dataProvider.CreatePrivateContestApiMoelImpl;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto.CreatePrivateContestResponse;

/**
 * Created by sc on 22/3/18.
 */

public class CreateAndJoinPrivateContestHelper {

    private static final String TAG = CreateAndJoinPrivateContestHelper.class.getSimpleName();
    private static final int JOIN_CHALLENGE_CONFIRMATION_REQUEST_CODE = 151;

    private JoinPrivateContestProcessListener mListener;
    private JoinContestData mJoinPrivateContestData;

    public interface JoinPrivateContestProcessListener {
        void noInternet();
        void lowWalletBalance(JoinContestData joinContestData);
        void joinPrivateContestSuccess(int status, CreatePrivateContestResponse createPrivateContestResponse);
        void onApiFailure();
        void onServerReturnedError(String msg);
        void hideProgressBar();
        void showProgressBar();
    }

    public synchronized int createAndJoinPrivateContest(@NonNull JoinContestData joinPrivateContestData,
                                                        @NonNull AppCompatActivity appCompatActivity,
                                                        @NonNull JoinPrivateContestProcessListener listener) {

        if (joinPrivateContestData != null && listener != null && appCompatActivity != null) {
            mListener = listener;
            mJoinPrivateContestData = joinPrivateContestData;
            fetchUserWalletFromServer(mJoinPrivateContestData, appCompatActivity);
            return 0;
        }
        return -1;
    }

    private void fetchUserWalletFromServer(final JoinContestData joinPrivateContestData,
                                           final AppCompatActivity appCompatActivity) {
        WalletApiModelImpl.newInstance(new WalletApiModelImpl.WalletApiListener() {
            @Override
            public void noInternet() {
                if (mListener != null) {
                    mListener.noInternet();
                }
            }

            @Override
            public void onApiFailed() {
                if (mListener != null) {
                    mListener.onApiFailure();
                }
            }

            @Override
            public void onSuccessResponse(UserWalletResponse response) {
                payAndJoinChallenge(joinPrivateContestData, appCompatActivity);
            }
        }).performApiCall();
    }

    private void payAndJoinChallenge(JoinContestData joinPrivateContestData, AppCompatActivity appCompatActivity) {
        if (joinPrivateContestData != null) {
            Log.d(TAG, "Entry fee : " + String.valueOf(joinPrivateContestData.getEntryFee()));

            if (WalletHelper.isSufficientBalAvailableInWallet(joinPrivateContestData.getEntryFee())) {
                showJoinDialog(joinPrivateContestData, appCompatActivity);

                NostragamusAnalytics.getInstance().trackScreenShown(Constants.AnalyticsCategory.CONTEST,
                        Constants.AnalyticsClickLabels.JOIN_CONTEST + "-" + Constants.AnalyticsClickLabels.CONTEST_JOIN_POPUP);

            } else {
                Log.d(TAG, "Insufficient balance to join contest");
                if (mListener != null) {
                    mListener.lowWalletBalance(joinPrivateContestData);
                }
            }
        }
    }

    private void showJoinDialog(JoinContestData joinPrivateContestData, AppCompatActivity appCompatActivity) {
        if (appCompatActivity != null) {
            Bundle args = new Bundle();
            args.putParcelable(Constants.BundleKeys.JOIN_CONTEST_DATA, Parcels.wrap(joinPrivateContestData));

            CompletePaymentDialogFragment dialogFragment =
                    CompletePaymentDialogFragment.newInstance(JOIN_CHALLENGE_CONFIRMATION_REQUEST_CODE,
                            joinPrivateContestData.getJoiContestDialogLaunchMode(),
                            args,
                            getCompletePaymentDialogActionListener(joinPrivateContestData));

             /* Hide progress dialog from UI */
            if (mListener != null) {
                mListener.hideProgressBar();
            }

            dialogFragment.showDialogAllowingStateLoss(appCompatActivity.getSupportFragmentManager(),
                    dialogFragment, CompletePaymentDialogFragment.class.getSimpleName());
        }
    }

    private CompletePaymentDialogFragment.CompletePaymentActionListener
    getCompletePaymentDialogActionListener(final JoinContestData joinContestData) {

        return new CompletePaymentDialogFragment.CompletePaymentActionListener() {
            @Override
            public void onBackClicked() {
                // No action required
            }

            @Override
            public void onPayConfirmed() {
                if (mListener != null) {
                    mListener.showProgressBar();
                }
                performJoinChallengeAction(joinContestData);
            }
        };
    }

    private void performJoinChallengeAction(JoinContestData joinContestData) {
        new CreatePrivateContestApiMoelImpl().callCreateContestApi(
                joinContestData.getChallengeId(),
                joinContestData.getEntryFee(),
                joinContestData.getContestName(),
                joinContestData.getPrivateContestEntries(),
                joinContestData.getPrivateContestTemplateId(),
                joinContestData.isPrivateContestTop1Wins(),
                mListener);
    }


}
