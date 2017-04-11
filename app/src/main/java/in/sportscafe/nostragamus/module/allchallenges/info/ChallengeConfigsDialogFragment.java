package in.sportscafe.nostragamus.module.allchallenges.info;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.jeeva.android.Log;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeConfig;
import in.sportscafe.nostragamus.module.common.NostragamusDialogFragment;
import in.sportscafe.nostragamus.module.common.OnDismissListener;
import in.sportscafe.nostragamus.module.paytm.GenerateOrderResponse;
import in.sportscafe.nostragamus.module.paytm.PaytmApiModelImpl;
import in.sportscafe.nostragamus.module.paytm.PaytmTransactionResponse;

/**
 * Created by Jeeva on 28/02/17.
 */
public class ChallengeConfigsDialogFragment extends NostragamusDialogFragment implements ChallengeConfigsApiModelImpl.OnConfigsApiModelListener,
        ChallengeConfigAdapter.OnConfigAccessListener {

    private static final String TAG = ChallengeConfigsDialogFragment.class.getSimpleName();

    private OnDismissListener mDismissListener;

    private int mDialogRequestCode;

    private int mChallengeId;

    private String mChallengeName;

    private Challenge mChallenge;

    private ChallengeConfigAdapter mConfigAdapter;

    private RecyclerView mRcvConfigs;

    private int mTitleHeight;

    private int mMaxHeight;

    public static ChallengeConfigsDialogFragment newInstance(int requestCode, int challengeId, String challengeName) {
        Bundle bundle = new Bundle();
        bundle.putInt(BundleKeys.DIALOG_REQUEST_CODE, requestCode);
        bundle.putInt(BundleKeys.CHALLENGE_ID, challengeId);
        bundle.putString(BundleKeys.CHALLENGE_NAME, challengeName);

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
        mDialogRequestCode = bundle.getInt(BundleKeys.DIALOG_REQUEST_CODE);
        mChallengeId = bundle.getInt(BundleKeys.CHALLENGE_ID);
        mChallengeName = bundle.getString(BundleKeys.CHALLENGE_NAME);
    }

    private void initViews() {
        this.mRcvConfigs = (RecyclerView) findViewById(R.id.configs_rcv);
        this.mRcvConfigs.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        this.mRcvConfigs.setHasFixedSize(true);

        mTitleHeight = getResources().getDimensionPixelSize(R.dimen.dp_42);
        mMaxHeight = getResources().getDimensionPixelSize(R.dimen.dp_360);
    }

    private void getConfigs() {
        new ChallengeConfigsApiModelImpl(this).getConfigs(mChallengeId);
    }

    private ChallengeConfigAdapter createAdapter(List<ChallengeConfig> configs) {
        return new ChallengeConfigAdapter(getContext(), configs, this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (null != mDismissListener) {
            Bundle bundle = new Bundle();
            if(null != mChallenge) {
                bundle.putParcelable(BundleKeys.CHALLENGE_DATA, Parcels.wrap(mChallenge));
            }
            mDismissListener.onDismiss(mDialogRequestCode, bundle);
        }
    }

    @Override
    public void onSuccessConfigsApi(List<ChallengeConfig> configs) {
        mConfigAdapter = createAdapter(configs);
        mRcvConfigs.setAdapter(mConfigAdapter);


        findViewById(R.id.configs_ll_title).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.configs_tv_challenge_name)).setText(mChallengeName);
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
        ChallengeConfig challengeConfig = mConfigAdapter.getItem(position);

        generateOrderAndProceedToJoin(challengeConfig);
    }

    /**
     * GenerateOrder requires call for all free & paid challenges.
     * Server will appropriately manage joining itself which will be reflected while challenges are refreshed.
     * If Free, server will update challenge as joined
     * If Paid, based on paytm transaction success, server'll update challenge for user
     *
     * Client app just does refresh at the end.
     *
     * @param challengeConfig
     */
    private void generateOrderAndProceedToJoin(ChallengeConfig challengeConfig) {
        GenerateOderApiModelImpl generateOderApiModel =
                GenerateOderApiModelImpl.newInstance(getGenerateOrderApiListener(challengeConfig));

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            showProgressbar();  // Dismissed at all type of callback

            generateOderApiModel.callGenerateOrder(
                    Long.valueOf(NostragamusDataHandler.getInstance().getUserId()),
                    mChallengeId,
                    challengeConfig.getConfigIndex()
            );

        } else {
            showMessage(Alerts.NO_NETWORK_CONNECTION);
        }

    }

    /**
     * Performs paytm transaction
     * @param challengeConfig - selected Challenge
     */
    private void performPaytmTransaction(ChallengeConfig challengeConfig, GenerateOrderResponse generateOrderResponse) {
        PaytmApiModelImpl paytmApiModel = PaytmApiModelImpl.newInstance(getPaytmApiListener(challengeConfig), getContext());

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            paytmApiModel.initPaytmTransaction(generateOrderResponse);

        } else {
            showMessage(Alerts.NO_NETWORK_CONNECTION);
        }
    }

    @Override
    public void onConfigHeightChanged() {
        mRcvConfigs.postDelayed(new Runnable() {
            @Override
            public void run() {
                int configsHeight = mRcvConfigs.computeVerticalScrollRange() + mTitleHeight;
                Log.d("ChallengeConfigsDialogFragment", "MaxHeight --> " + mMaxHeight + ", " + "ScrollHeight --> " + configsHeight);
                if(configsHeight > mMaxHeight) {
                    configsHeight = mMaxHeight;
                }

                WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
                getDialog().getWindow().setLayout(attributes.width, configsHeight);
            }
        }, 250);
    }

    /**
     * Generate Order and perform task based on challenge type as paid (paytm trans) , free (direct join)
     * @param selectedChallenge - user chosen challenge
     * @return
     */
    private GenerateOderApiModelImpl.OnGenerateOrderApiModelListener getGenerateOrderApiListener(final ChallengeConfig selectedChallenge) {
        return new GenerateOderApiModelImpl.OnGenerateOrderApiModelListener() {
            @Override
            public void makePaytmTransaction(GenerateOrderResponse generateOrderResponse) {
                dismissProgressbar();
                performPaytmTransaction(selectedChallenge, generateOrderResponse);
            }

            @Override
            public void joinFreeChallenge() {
                dismissProgressbar();
                 /*Server will manage joining based on generateOrder api if it's free
                 * No Need to call any api here for joining */

                 onJoinActionSuccess();
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
     * @param challengeConfigToJoin
     * @return
     */
    private PaytmApiModelImpl.OnPaytmApiModelListener getPaytmApiListener(final ChallengeConfig challengeConfigToJoin) {
        return new PaytmApiModelImpl.OnPaytmApiModelListener() {
            @Override
            public void onTransactionUiError() {
                Log.d(TAG, Alerts.PAYTM_FAILURE);
                showMessage(Alerts.PAYTM_FAILURE);
            }

            @Override
            public void onTransactionNoNetwork() {
                Log.d(TAG, Alerts.NO_NETWORK_CONNECTION);
                showMessage(Alerts.NO_NETWORK_CONNECTION);
            }

            @Override
            public void onTransactionClientAuthenticationFailed() {
                Log.d(TAG, Alerts.PAYTM_AUTHENTICATION_FAILED);
                showMessage(Alerts.PAYTM_AUTHENTICATION_FAILED);
            }

            @Override
            public void onTransactionPageLoadingError() {
                Log.d(TAG, Alerts.PAYTM_FAILURE);
                showMessage(Alerts.PAYTM_FAILURE);
            }

            @Override
            public void onTransactionCancelledByBackPressed() {
                Log.d(TAG, Alerts.PAYTM_TRANSACTION_CANCELLED);
                showMessage(Alerts.PAYTM_TRANSACTION_CANCELLED);
            }

            @Override
            public void onTransactionCancelled() {
                Log.d(TAG, Alerts.PAYTM_TRANSACTION_CANCELLED);
                showMessage(Alerts.PAYTM_TRANSACTION_CANCELLED);
            }

            @Override
            public void onTransactionSuccessResponse(PaytmTransactionResponse successResponse) {
                Log.d(TAG, "Transaction Response - Success (Joining Challenge)");
                /* Server will arrange all joining, No need of any api here.  */
                onJoinActionSuccess();
            }

            @Override
            public void onTransactionFailureResponse(PaytmTransactionResponse response) {
                Log.d(TAG, Alerts.PAYTM_TRANSACTION_FAILED);
                showMessage(Alerts.PAYTM_TRANSACTION_FAILED);
            }
        };
    }

    /**
     * Used to indicate join action completed either for free or paid and should be refreshed (onDismiss).
     */
    private void onJoinActionSuccess() {
        dismiss();
    }
}