package in.sportscafe.nostragamus.module.allchallenges.info;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeConfig;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.CustomLayoutManagerWithSmoothScroll;
import in.sportscafe.nostragamus.module.common.NostragamusDialogFragment;
import in.sportscafe.nostragamus.module.common.OnDismissListener;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.GenerateOrderResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.PaytmApiModelImpl;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.PaytmTransactionFailureDialogFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto.PaytmTransactionResponse;

/**
 * Created by Jeeva on 28/02/17.
 */
public class ChallengeConfigsDialogFragment extends NostragamusDialogFragment implements ChallengeConfigsApiModelImpl.OnConfigsApiModelListener,
        ChallengeConfigAdapter.OnConfigAccessListener, PaytmTransactionFailureDialogFragment.IPaytmFailureActionListener, View.OnClickListener {

    private static final String TAG = ChallengeConfigsDialogFragment.class.getSimpleName();

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
        dismiss();
    }

    @Override
    public void onFailedConfigsApi() {
        showMessage(Alerts.API_FAIL);
        dismiss();
    }

    @Override
    public void onNoInternet() {
        showMessage(Alerts.NO_NETWORK_CONNECTION);
        dismiss();
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
        if (BuildConfig.IS_PAID_VERSION) {
            ChallengeConfig challengeConfig = mConfigAdapter.getItem(position);
            generateOrderAndProceedToJoin(challengeConfig);
        } else {
            if (mConfigAdapter.getItem(position).isFreeEntry()) {
                ChallengeConfig challengeConfig = mConfigAdapter.getItem(position);
                generateOrderAndProceedToJoin(challengeConfig);
            } else {
                mOpenDownloadDialog = true;
                dismissThisDialog();
            }
        }
    }

    /**
     * GenerateOrder requires call for all free & paid challenges.
     * Server will appropriately manage joining itself which will be reflected while challenges are refreshed.
     * If Free, server will update challenge as joined
     * If Paid, based on paytm transaction success, server'll update challenge for user
     * <p>
     * Client app just does refresh at the end.
     *
     * @param challengeConfig
     */
    private void generateOrderAndProceedToJoin(ChallengeConfig challengeConfig) {
        GenerateOderApiModelImpl generateOderApiModel =
                GenerateOderApiModelImpl.newInstance(getGenerateOrderApiListener());

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            showProgressbar();  // Dismissed at all type of callback

            generateOderApiModel.callGenerateOrder(
                    Long.valueOf(NostragamusDataHandler.getInstance().getUserId()),
                    mChallenge.getChallengeId(),
                    challengeConfig.getConfigIndex()
            );

        } else {
            showMessage(Alerts.NO_NETWORK_CONNECTION);
        }

    }

    /**
     * Performs paytm transaction
     */
    private void performPaytmTransaction(GenerateOrderResponse generateOrderResponse) {
        PaytmApiModelImpl paytmApiModel = PaytmApiModelImpl.newInstance(getPaytmApiListener(generateOrderResponse), getContext());

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            paytmApiModel.initPaytmTransaction(generateOrderResponse);

        } else {
            showMessage(Alerts.NO_NETWORK_CONNECTION);
        }
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


    /**
     * Generate Order and perform task based on challenge type as paid (paytm trans) , free (direct join)
     *
     * @return
     */
    private GenerateOderApiModelImpl.OnGenerateOrderApiModelListener getGenerateOrderApiListener() {
        return new GenerateOderApiModelImpl.OnGenerateOrderApiModelListener() {
            @Override
            public void makePaytmTransaction(GenerateOrderResponse generateOrderResponse) {
                dismissProgressbar();
                performPaytmTransaction(generateOrderResponse);
            }

            @Override
            public void joinFreeChallenge(Bundle bundle) {
                dismissProgressbar();
                 /*Server will manage joining based on generateOrder api if it's free
                 * No Need to call any api here for joining */

                onJoinActionSuccess(bundle);
            }

            @Override
            public void onApiFailure() {
                dismissProgressbar();
                showMessage(Alerts.API_FAIL);
            }

            @Override
            public void noInternet() {
                dismissProgressbar();
                showMessage(Alerts.NO_NETWORK_CONNECTION);
            }
        };
    }

    /**
     * Handles call back
     *
     * @return
     */
    private PaytmApiModelImpl.OnPaytmApiModelListener getPaytmApiListener(final GenerateOrderResponse generateOrderResponse) {
        return new PaytmApiModelImpl.OnPaytmApiModelListener() {
            @Override
            public void onTransactionUiError() {
                Log.d(TAG, Alerts.PAYTM_FAILURE);
                showPaytmTransactionFailureDialog();
            }

            @Override
            public void onTransactionNoNetwork() {
                Log.d(TAG, Alerts.NO_NETWORK_CONNECTION);
                showPaytmTransactionFailureDialog();
            }

            @Override
            public void onTransactionClientAuthenticationFailed() {
                Log.d(TAG, Alerts.PAYTM_AUTHENTICATION_FAILED);
                showPaytmTransactionFailureDialog();
            }

            @Override
            public void onTransactionPageLoadingError() {
                Log.d(TAG, Alerts.PAYTM_FAILURE);
                showPaytmTransactionFailureDialog();
            }

            @Override
            public void onTransactionCancelledByBackPressed() {
                Log.d(TAG, Alerts.PAYTM_TRANSACTION_CANCELLED);
                showPaytmTransactionFailureDialog();
            }

            @Override
            public void onTransactionCancelled() {
                Log.d(TAG, Alerts.PAYTM_TRANSACTION_CANCELLED);
                showPaytmTransactionFailureDialog();
            }

            @Override
            public void onTransactionSuccessResponse(@Nullable PaytmTransactionResponse successResponse) {
                Log.d(TAG, "Transaction Response - Success");
                /* Server will arrange all joining, No need of any api here.  */

                if (generateOrderResponse != null  && mChallenge != null) {
                    NostragamusAnalytics.getInstance().trackRevenue(
                            generateOrderResponse.getTxnAmount(),
                            mChallenge.getChallengeId(),
                            mChallenge.getName());
                }

                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.BundleKeys.JOINED_CHALLENGE_INFO, Parcels.wrap(successResponse.getJoinedChallengeInfo()));
                onJoinActionSuccess(bundle);
            }

            @Override
            public void onTransactionFailureResponse(@Nullable PaytmTransactionResponse response) {
                Log.d(TAG, Alerts.PAYTM_TRANSACTION_FAILED);
                showPaytmTransactionFailureDialog();
            }
        };
    }

    /**
     * Used to indicate join action completed either for free or paid and should be refreshed (onDismiss).
     */
    private void onJoinActionSuccess(Bundle bundle) {
        mJoinedChallenge = true;
        mChallengeDetailsBundle = bundle;
        dismissThisDialog();
    }

    private void showPaytmTransactionFailureDialog() {

        if (mChallenge != null) {
            PaytmTransactionFailureDialogFragment failureDialogFragment =
                    PaytmTransactionFailureDialogFragment.newInstance(1199, mChallenge, this);
            failureDialogFragment.show(getChildFragmentManager(), "FAILURE_DIALOG");
        } else {
            showMessage(Alerts.PAYTM_TRANSACTION_FAILED);
        }
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
        }, 200);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_cross_btn:
                dismiss();
                break;
        }
    }
}