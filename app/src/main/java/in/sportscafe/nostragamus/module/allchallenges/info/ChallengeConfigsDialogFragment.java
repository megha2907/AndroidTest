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
import android.widget.Toast;

import com.jeeva.android.Log;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeConfig;
import in.sportscafe.nostragamus.module.common.NostragamusDialogFragment;
import in.sportscafe.nostragamus.module.common.OnDismissListener;
import in.sportscafe.nostragamus.module.paytm.PaytmApiModelImpl;
import in.sportscafe.nostragamus.module.paytm.PaytmTransactionSuccessResponse;
import in.sportscafe.nostragamus.utils.CodeSnippet;

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

        if(challengeConfig.isFreeEntry()) {
            Log.d(TAG, "Free challenge selected... ");
            joinChallenge(challengeConfig);
        } else {

            Log.d(TAG, "Paid challenge selected... ");

            if (BuildConfig.IS_PAID_VERSION) {
                Log.d(TAG, "Paid / Full App Version... ");
                performTransaction(challengeConfig);

            } else {
                Log.d(TAG, "Free App Version, Can't continue... ");
                showMessage(Alerts.NOT_FREE_CHALLENGE, Toast.LENGTH_LONG);
            }
        }
    }

    /**
     * Performs paytm transaction
     * @param challengeConfig - selected Challenge
     */
    private void performTransaction(ChallengeConfig challengeConfig) {
        PaytmApiModelImpl paytmApiModel = PaytmApiModelImpl.newInstance(getPaytmApiListener(challengeConfig), getContext());

        if (Nostragamus.getInstance().hasNetworkConnection()) {
            showProgressbar();  // Dismissed at all type of callback i.e. at getPaytmApiListener()

            paytmApiModel.onStartTransaction(CodeSnippet.getOrderId(),
                    NostragamusDataHandler.getInstance().getUserId(),
                    String.valueOf(challengeConfig.getEntryFee()));

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
     * Used for both the purpose as free or
     * @param config
     */
    private void joinChallenge(ChallengeConfig config) {
        new JoinChallengeApiModelImpl(new JoinChallengeApiModelImpl.OnJoinChallengeApiModelListener() {
            @Override
            public void onSuccessJoinApi(Challenge challenge) {
                mChallenge = challenge;
                dismiss();
            }

            @Override
            public void onFailedJoinApi() {
                showMessage(Alerts.API_FAIL);
            }

            @Override
            public void onNoInternet() {
                showMessage(Alerts.NO_NETWORK_CONNECTION);
            }

            @Override
            public void onApiCallStarted() {
                showProgressbar();
            }

            @Override
            public boolean onApiCallStopped() {
                return dismissProgressbar();
            }
        }).joinChallenge(mChallengeId, config.getConfigIndex());
    }

    /**
     * Handles call back
     * @param challengeConfigToJoin
     * @return
     */
    private PaytmApiModelImpl.OnPaytmApiModelListener getPaytmApiListener(final ChallengeConfig challengeConfigToJoin) {
        return new PaytmApiModelImpl.OnPaytmApiModelListener() {
            @Override
            public void onApiSuccess() {
                // This is for non transaction api call
                dismissProgressbar();
            }

            @Override
            public void onApiFailure() {
                dismissProgressbar();
                showMessage(Alerts.API_FAIL);
            }

            @Override
            public void onTransactionUiError() {
                dismissProgressbar();
                showMessage(Alerts.PAYTM_FAILURE);
            }

            @Override
            public void onTransactionNoNetwork() {
                dismissProgressbar();
                showMessage(Alerts.NO_NETWORK_CONNECTION);
            }

            @Override
            public void onTransactionClientAuthenticationFailed() {
                dismissProgressbar();
                showMessage(Alerts.PAYTM_AUTHENTICATION_FAILED);
            }

            @Override
            public void onTransactionPageLoadingError() {
                dismissProgressbar();
                showMessage(Alerts.PAYTM_FAILURE);
            }

            @Override
            public void onTransactionCancelledByBackPressed() {
                dismissProgressbar();
                showMessage(Alerts.PAYTM_TRANSACTION_CANCELLED);
            }

            @Override
            public void onTransactionCancelled() {
                dismissProgressbar();
                showMessage(Alerts.PAYTM_TRANSACTION_CANCELLED);
            }

            @Override
            public void onTransactionResponse(PaytmTransactionSuccessResponse successResponse) {
                dismissProgressbar();
                joinChallenge(challengeConfigToJoin);
            }
        };
    }
}