package in.sportscafe.nostragamus.module.contest.helper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.allchallenges.join.CompletePaymentDialogFragment;
import in.sportscafe.nostragamus.module.allchallenges.join.dto.JoinChallengeResponse;
import in.sportscafe.nostragamus.module.contest.dataProvider.JoinContestApiImpl;
import in.sportscafe.nostragamus.module.contest.dto.JoinContestData;
import in.sportscafe.nostragamus.module.contest.dto.VerifyJoinContestResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletApiModelImpl;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.dto.UserWalletResponse;

/**
 * Created by sandip on 25/09/17.
 */

public class JoinContestHelper {

    private static final String TAG = JoinContestHelper.class.getSimpleName();
    private static final int JOIN_CHALLENGE_CONFIRMATION_REQUEST_CODE = 1001;

    private JoinContestProcessListener mListener;
    private JoinContestData mJoinContestData;

    public interface JoinContestProcessListener {
        void noInternet();
        void lowWalletBalance(JoinContestData joinContestData);
        void joinContestSuccess(JoinContestData joinContestData);
        void onApiFailure();
        void onServerReturnedError(String msg);
        void hideProgressBar();
        void showProgressBar();
    }

    public synchronized int JoinContest(@NonNull JoinContestData joinContestData,
                                        @NonNull AppCompatActivity appCompatActivity,
                                        @NonNull JoinContestProcessListener listener) {

        if (joinContestData != null && listener != null && appCompatActivity != null) {
            mListener = listener;
            mJoinContestData = joinContestData;
            fetchUserWalletFromServer(mJoinContestData, appCompatActivity);
            return 0;
        }
        return -1;
    }

    private void fetchUserWalletFromServer(final JoinContestData joinContestData, final AppCompatActivity appCompatActivity) {
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
                payAndJoinChallenge(joinContestData, appCompatActivity);
            }
        }).performApiCall();
    }

    private void payAndJoinChallenge(JoinContestData joinContestData, AppCompatActivity appCompatActivity) {
        if (joinContestData != null) {
            Log.d(TAG, "Entry fee : " + String.valueOf(joinContestData.getEntryFee()));
            if (WalletHelper.isSufficientBalAvailableInWallet(joinContestData.getEntryFee())) {
                showJoinDialog(joinContestData, appCompatActivity);

            } else {
                Log.d(TAG, "Insufficient balance to join contest");
                if (mListener != null) {
                    mListener.lowWalletBalance(joinContestData);
                }
            }
        }
    }

    private void showJoinDialog(JoinContestData joinContestData, AppCompatActivity appCompatActivity) {
        if (appCompatActivity != null) {
            Bundle args = new Bundle();
            args.putParcelable(Constants.BundleKeys.JOIN_CONTEST_DATA, Parcels.wrap(joinContestData));

            CompletePaymentDialogFragment dialogFragment =
                    CompletePaymentDialogFragment.newInstance(JOIN_CHALLENGE_CONFIRMATION_REQUEST_CODE,
                            joinContestData.getJoiContestDialogLaunchMode(),
                            args,
                            getCompletePaymentDialogActionListener(joinContestData));

            /* Hide progress dialog from UI */
            if (mListener != null) {
                mListener.hideProgressBar();
            }

            dialogFragment.show(appCompatActivity.getSupportFragmentManager(), CompletePaymentDialogFragment.class.getSimpleName());
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

    private void performJoinChallengeAction(final JoinContestData joinContestData) {
        JoinContestApiImpl joinContestApi = new JoinContestApiImpl(new JoinContestApiImpl.JoinContestApiListener() {

            @Override
            public void onFailure(int dataStatus) {
                if (mListener != null) {
                    mListener.onApiFailure();
                }
            }

            @Override
            public void onServerReturnedError(String msg) {
                if (mListener != null) {
                    mListener.onServerReturnedError(msg);
                }
            }

            @Override
            public void onSuccessResponse(VerifyJoinContestResponse verifyJoinContestResponse) {
                if (mListener != null) {
                    mListener.joinContestSuccess(joinContestData);
                }
            }
        });

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            joinContestApi.joinContestQueue(joinContestData.getContestId(),
                    joinContestData.getChallengeId(),
                    joinContestData.getChallengeName());
        } else {
            if (mListener != null) {
                mListener.noInternet();
            }
        }
    }
}