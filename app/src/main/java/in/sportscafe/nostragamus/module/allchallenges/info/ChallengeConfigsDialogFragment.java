package in.sportscafe.nostragamus.module.allchallenges.info;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.Log;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeConfig;
import in.sportscafe.nostragamus.module.allchallenges.join.CompletePaymentDialogFragment;
import in.sportscafe.nostragamus.module.allchallenges.join.JoinChallengeUseWalletApiModelImpl;
import in.sportscafe.nostragamus.module.allchallenges.join.dto.JoinChallengeResponse;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.CustomLayoutManagerWithSmoothScroll;
import in.sportscafe.nostragamus.module.common.NostragamusDialogFragment;
import in.sportscafe.nostragamus.module.common.OnDismissListener;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletApiModelImpl;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.lowBalance.AddMoneyOnLowBalanceActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.dto.UserWalletResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.JoinChallengeFailureDialogFragment;

/**
 * Created by Jeeva on 28/02/17.
 */
public class ChallengeConfigsDialogFragment extends NostragamusDialogFragment implements ChallengeConfigsApiModelImpl.OnConfigsApiModelListener,
        ChallengeConfigAdapter.OnConfigAccessListener, JoinChallengeFailureDialogFragment.IPaytmFailureActionListener, View.OnClickListener {

    private static final String TAG = ChallengeConfigsDialogFragment.class.getSimpleName();

    private static final int ADD_MONEY_ON_LOW_BALANCE_REQUEST_CODE = 2180;
    private static final int JOIN_CHALLENGE_CONFIRMATION_REQUEST_CODE = 2190;

    private OnDismissListener mDismissListener;

    private int OPEN_JOINED_CHALLENGE_DIALOG = 53;

    private int OPEN_DOWNLOAD_APP_DIALOG = 54;

    private boolean mJoinedChallenge = false;

    private boolean mOpenDownloadDialog = false;

    private Challenge mChallenge;

    private Bundle mChallengeDetailsBundle;

    private int mConfigIndex;

    private ChallengeConfigAdapter mConfigAdapter;

    private RecyclerView mRcvConfigs;

    private int mTitleHeight;

    private int mGreaterHeight;

    private int mExtraHeight;

    private int mMaxHeight;

    private ImageView mBtnPopupClose;

    public static ChallengeConfigsDialogFragment newInstance(int requestCode, Challenge challenge) {
        Bundle bundle = new Bundle();
        bundle.putInt(BundleKeys.DIALOG_REQUEST_CODE, requestCode);
        bundle.putParcelable(BundleKeys.CHALLENGE, Parcels.wrap(challenge));

        ChallengeConfigsDialogFragment fragment = new ChallengeConfigsDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDismissListener) {
            mDismissListener = (OnDismissListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_configs, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setCancelable(true);

        openBundle(getArguments());

        initViews();

        getConfigs();
    }

    private void openBundle(Bundle bundle) {
        mChallenge = Parcels.unwrap(bundle.getParcelable(BundleKeys.CHALLENGE));
    }

    private void initViews() {
        this.mRcvConfigs = (RecyclerView) findViewById(R.id.configs_rcv);
        this.mRcvConfigs.setLayoutManager(new CustomLayoutManagerWithSmoothScroll(getContext()));
        this.mRcvConfigs.setHasFixedSize(true);

        mBtnPopupClose = (ImageView)findViewById(R.id.popup_cross_btn);

        mTitleHeight = getResources().getDimensionPixelSize(R.dimen.dp_42);
        mMaxHeight = getResources().getDimensionPixelSize(R.dimen.dp_360);
        mExtraHeight = getResources().getDimensionPixelSize(R.dimen.dp_80);
        mGreaterHeight = getResources().getDimensionPixelSize(R.dimen.dp_20);
    }

    private void getConfigs() {
        new ChallengeConfigsApiModelImpl(this).getConfigs(mChallenge.getChallengeId(), null);
    }

    private ChallengeConfigAdapter createAdapter(List<ChallengeConfig> configs) {
        return new ChallengeConfigAdapter(getContext(), configs, this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (null != mDismissListener) {
            if (mJoinedChallenge) {
                if (null != mChallengeDetailsBundle) {
                    mDismissListener.onDismiss(OPEN_JOINED_CHALLENGE_DIALOG, mChallengeDetailsBundle);
                }
            } else if (mOpenDownloadDialog) {
                mDismissListener.onDismiss(OPEN_DOWNLOAD_APP_DIALOG, null);
            }
        }
        super.onDismiss(dialog);
    }

    @Override
    public void onSuccessConfigsApi(List<ChallengeConfig> configs) {
        mConfigAdapter = createAdapter(configs);
        mRcvConfigs.setAdapter(mConfigAdapter);

        if (mRcvConfigs.getAdapter().getItemCount() >= 3) {
            changeHeight();
        }

        findViewById(R.id.configs_ll_title).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.configs_tv_challenge_name)).setText(mChallenge.getName() + ": Pick a contest");
        mBtnPopupClose.setVisibility(View.VISIBLE);
        mBtnPopupClose.setOnClickListener(this);

    }

    @Override
    public void onEmpty() {
        showMessage(Alerts.POLL_LIST_EMPTY);
        dismissThisDialog();
    }

    @Override
    public void onFailedConfigsApi() {
        showMessage(Alerts.API_FAIL);
        dismissThisDialog();
    }

    @Override
    public void onNoInternet() {
        showMessage(Alerts.NO_NETWORK_CONNECTION);
        dismissThisDialog();
    }

    @Override
    public void onApiCallStarted() {
        showProgressbar();
    }

    @Override
    public boolean onApiCallStopped() {
        return dismissProgressbar();
    }

    @Override
    public void onJoinClick(int position) {
        if (mConfigAdapter != null && mChallenge != null) {
            ChallengeConfig challengeConfig = mConfigAdapter.getItem(position);
            int challengeId = mChallenge.getChallengeId();

            if (BuildConfig.IS_PAID_VERSION) {
                joinChallengeOnPaidVersion(challengeConfig, challengeId);
            } else {
                joinChallengeOnFreeVersion(challengeConfig, challengeId);
            }
        } else {
            showMessage(Alerts.SOMETHING_WRONG);
        }
    }

    private void fetchUserWalletFromServer(final ChallengeConfig challengeConfig, final int challengeId, final int joinDialogLaunchMode) {
        showProgressbar();
        WalletApiModelImpl.newInstance(new WalletApiModelImpl.WalletApiListener() {
            @Override
            public void noInternet() {
                dismissProgressbar();
                showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
            }

            @Override
            public void onApiFailed() {
                dismissProgressbar();
                showMessage(Constants.Alerts.API_FAIL);
            }

            @Override
            public void onSuccessResponse(UserWalletResponse response) {
                dismissProgressbar();
                payAndJoinChallenge(challengeConfig, challengeId, joinDialogLaunchMode);
            }
        }).performApiCall();
    }

    private void joinChallengeOnPaidVersion(ChallengeConfig challengeConfig, int challengeId) {
        if (challengeConfig != null) {

            if (challengeConfig.isFreeEntry()) {
                /* Entry is free */
                performJoinChallengeAction(challengeId, challengeConfig);
            } else {
                // Paid entry, check wallet balance, fetch wallet and then continue
                fetchUserWalletFromServer(challengeConfig, challengeId,
                        CompletePaymentDialogFragment.DialogLaunchMode.JOINING_CHALLENGE_LAUNCH);
            }
        } else {
            showMessage(Alerts.SOMETHING_WRONG);
        }
    }

    private void payAndJoinChallenge(ChallengeConfig challengeConfig, int challengeId, int joinDialogLaunchMode) {
        double entryFee = challengeConfig.getEntryFee();

        if (WalletHelper.isSufficientBalAvailableInWallet(entryFee)) {
            showJoinDialog(challengeId, challengeConfig, joinDialogLaunchMode);

        } else {
            // Add money into wallet to join
            addMoneyInWalletAsLowBalance(challengeId, challengeConfig);
        }
    }

    private void addMoneyInWalletAsLowBalance(int challengeId, ChallengeConfig challengeConfig) {
        Bundle args = new Bundle();
        args.putParcelable(BundleKeys.CHALLENGE_CONFIG, Parcels.wrap(challengeConfig));
        args.putInt(BundleKeys.CHALLENGE_ID, challengeId);

        Intent intent = new Intent(getActivity(), AddMoneyOnLowBalanceActivity.class);
        intent.putExtras(args);
        if (getActivity() != null) {
            getActivity().startActivityForResult(intent, ADD_MONEY_ON_LOW_BALANCE_REQUEST_CODE);
        }
    }

    private void joinChallengeOnFreeVersion(ChallengeConfig challengeConfig, int challengeId) {
        if (challengeConfig != null) {

            if (challengeConfig.isFreeEntry()) {
                /* For Free-App version, do not show any dialog AND Entry is free */
                performJoinChallengeAction(challengeId, challengeConfig);
            } else {
                mOpenDownloadDialog = true;
                dismissThisDialog();
            }
        } else {
            showMessage(Alerts.SOMETHING_WRONG);
        }
    }

    /**
     * Make api call for joining
     * @param challengeId
     * @param challengeConfig
     */
    private void performJoinChallengeAction(int challengeId, ChallengeConfig challengeConfig) {
        if (challengeConfig != null) {
            JoinChallengeUseWalletApiModelImpl joinChallenge = JoinChallengeUseWalletApiModelImpl.newInstance(getJoinChallengeListener());

            if (Nostragamus.getInstance().hasNetworkConnection()) {
                showProgressbar();
                joinChallenge.makeApiCall(challengeId, challengeConfig.getConfigIndex());
            } else {
                showMessage(Alerts.NO_NETWORK_CONNECTION);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_MONEY_ON_LOW_BALANCE_REQUEST_CODE &&
                resultCode == Activity.RESULT_OK &&
                data != null && data.getExtras() != null) {

            Log.d(TAG, "Money add successfully (on low balance) ; performing join challenge");
            final ChallengeConfig challengeConfig = Parcels.unwrap(data.getExtras().getParcelable(BundleKeys.CHALLENGE_CONFIG));
            final int challengeId = data.getExtras().getInt(BundleKeys.CHALLENGE_ID, -1);

            if (challengeConfig != null && challengeId != -1) {

                /* Fetch wallet details and continue in loop */
                fetchUserWalletFromServer(challengeConfig, challengeId,
                        CompletePaymentDialogFragment.DialogLaunchMode.JOINING_CHALLENGE_AFTER_LOW_BAL_LAUNCH);
            } else {
                showMessage(Alerts.SOMETHING_WRONG);
            }
        }
    }

    private JoinChallengeUseWalletApiModelImpl.JoinChallengeUseWalletApiListener getJoinChallengeListener() {
        return new JoinChallengeUseWalletApiModelImpl.JoinChallengeUseWalletApiListener() {
            @Override
            public void noInternet() {
                dismissProgressbar();
                showMessage(Alerts.NO_NETWORK_CONNECTION);
            }

            @Override
            public void onNoApiResponse() {
                dismissProgressbar();
                showMessage(Alerts.API_FAIL);
            }

            @Override
            public void onSuccessResponse(JoinChallengeResponse joinChallengeResponse) {
                dismissProgressbar();
                onChallengeJoinedSuccessfully(joinChallengeResponse);
            }
        };
    }

    @Override
    public void onConfigHeightChanged(final int pos) {

        ((SimpleItemAnimator) mRcvConfigs.getItemAnimator()).setSupportsChangeAnimations(false);
        mRcvConfigs.postDelayed(new Runnable() {
            @Override
            public void run() {

                int configsHeight;
                if (mRcvConfigs.getAdapter().getItemCount() >= 3) {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        configsHeight = mMaxHeight + mTitleHeight+ mGreaterHeight;
                    }else {
                        configsHeight = mMaxHeight + mTitleHeight+ 3*mGreaterHeight;
                    }
                } else {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        configsHeight = mRcvConfigs.computeVerticalScrollRange() + mTitleHeight+ mExtraHeight;
                    }else {
                        configsHeight = mRcvConfigs.computeVerticalScrollRange() + mTitleHeight + mExtraHeight;
                    }

                    Log.d("ChallengeConfigsDialogFragment5", "MaxHeight --> " + mMaxHeight + ", " + "ScrollHeight --> " + configsHeight);
                    if (configsHeight > mMaxHeight) {
                        configsHeight = mMaxHeight;
                    }

                }

                WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
                getDialog().getWindow().setLayout(attributes.width, configsHeight);
                mRcvConfigs.smoothScrollToPosition(pos);

            }
        }, 250);
    }

    private void changeHeight(){
        int configsHeight = 0;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            configsHeight = mMaxHeight + mTitleHeight + mGreaterHeight;
        }else {
            configsHeight = mMaxHeight + mTitleHeight+ 3*mGreaterHeight;
        }
        WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
        getDialog().getWindow().setLayout(attributes.width, configsHeight);
    }

    private void onChallengeJoinedSuccessfully(@NonNull JoinChallengeResponse joinChallengeResponse) {
        if (joinChallengeResponse != null) {

            double amount = joinChallengeResponse.getJoinedChallengeInfo().getEntryFee();
            if ( BuildConfig.IS_PAID_VERSION &&
                    !joinChallengeResponse.getJoinedChallengeInfo().isFreeEntry() &&
                    amount > 0 && mChallenge != null) {
                NostragamusAnalytics.getInstance().trackRevenue(amount, mChallenge.getChallengeId(), mChallenge.getName());
            }

            Bundle bundle = new Bundle();
            bundle.putParcelable(BundleKeys.JOINED_CHALLENGE_INFO, Parcels.wrap(joinChallengeResponse.getJoinedChallengeInfo()));
            if (mChallenge != null) {
                bundle.putInt(BundleKeys.CHALLENGE_ID, mChallenge.getChallengeId());
            }

            onJoinActionSuccess(bundle);
        }
    }

    /**
     * Used to indicate join action completed either for free or paid and should be refreshed (onDismiss).
     */
    private void onJoinActionSuccess(Bundle bundle) {
        mJoinedChallenge = true;
        mChallengeDetailsBundle = bundle;
        dismissThisDialog();
    }

    @Override
    public void onRejoinClicked() {
        /* User can click 'Join' button again to re-try transaction
        * No action required here */
    }

    @Override
    public void onBackToHomeClicked() {
        mJoinedChallenge = false;
        dismissThisDialog();
    }

    /**
     * This method allows slow and visible dialog dismissal, should be used same for same purpose instead directly calling dismiss.
     */
    private void dismissThisDialog() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Dismissing this...");
                ChallengeConfigsDialogFragment.this.dismiss();
            }
        }, 300);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_cross_btn:
                dismissThisDialog();
                break;
        }
    }

    private void showJoinDialog(int challengeId, ChallengeConfig challengeConfig, int dialogLaunchMode) {
        Bundle bundle = new Bundle();
        bundle.putDouble(Constants.BundleKeys.ENTRY_FEE, challengeConfig.getEntryFee());
        bundle.putString(Constants.BundleKeys.CONFIG_NAME, challengeConfig.getConfigName());

        CompletePaymentDialogFragment dialogFragment =
                CompletePaymentDialogFragment.newInstance(JOIN_CHALLENGE_CONFIRMATION_REQUEST_CODE,
                        dialogLaunchMode,
                        bundle,
                        getCompletePaymentDialoActionListener(challengeId, challengeConfig));

        dialogFragment.show(getChildFragmentManager(), "COMPLETE_JOIN");
    }

    private CompletePaymentDialogFragment.CompletePaymentActionListener
    getCompletePaymentDialoActionListener(final int challengeId, final ChallengeConfig challengeConfig) {

        return new CompletePaymentDialogFragment.CompletePaymentActionListener() {
            @Override
            public void onBackClicked() {
                // No action required
            }

            @Override
            public void onPayConfirmed() {
                performJoinChallengeAction(challengeId, challengeConfig);
            }
        };
    }
}