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

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeConfig;
import in.sportscafe.nostragamus.module.common.NostragamusDialogFragment;
import in.sportscafe.nostragamus.module.common.OnDismissListener;

/**
 * Created by deepanshi on 4/10/17.
 */

public class ChallengeRewardsFragment extends NostragamusDialogFragment implements ChallengeConfigsApiModelImpl.OnConfigsApiModelListener,
        ChallengeRewardAdapter.OnConfigAccessListener {

    private static final String TAG = ChallengeConfigsDialogFragment.class.getSimpleName();

    private OnDismissListener mDismissListener;

    private int mDialogRequestCode;

    private int mChallengeId;

    private Challenge mChallengeInfo;

    private String mChallengeName;

    private int mConfigIndex;

    private String mChallengeEndTime;

    private Challenge mChallenge;

    private ChallengeRewardAdapter mConfigAdapter;

    private RecyclerView mRcvConfigs;

    private int mTitleHeight;

    private int mMaxHeight;

    public static ChallengeRewardsFragment newInstance(int requestCode, Challenge challenge, String challengeName, int configIndex, String endTime) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BundleKeys.DIALOG_REQUEST_CODE, requestCode);
        bundle.putInt(Constants.BundleKeys.CHALLENGE_ID, challenge.getChallengeId());
        bundle.putString(Constants.BundleKeys.CHALLENGE_NAME, challengeName);
        bundle.putInt(Constants.BundleKeys.CONFIG_INDEX, configIndex);
        bundle.putString(Constants.BundleKeys.CHALLENGE_END_TIME, endTime);
        bundle.putParcelable(Constants.BundleKeys.CHALLENGE_INFO, Parcels.wrap(challenge));

        ChallengeRewardsFragment fragment = new ChallengeRewardsFragment();
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
        return inflater.inflate(R.layout.fragment_challenge_rewards, container, false);
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
        mDialogRequestCode = bundle.getInt(Constants.BundleKeys.DIALOG_REQUEST_CODE);
        mChallengeId = bundle.getInt(Constants.BundleKeys.CHALLENGE_ID);
        mChallengeName = bundle.getString(Constants.BundleKeys.CHALLENGE_NAME);
        mConfigIndex = bundle.getInt(Constants.BundleKeys.CONFIG_INDEX);
        mChallengeEndTime = bundle.getString(Constants.BundleKeys.CHALLENGE_END_TIME);
        mChallengeInfo=Parcels.unwrap(bundle.getParcelable(Constants.BundleKeys.CHALLENGE_INFO));
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
        new ChallengeConfigsApiModelImpl(this).getConfigs(mChallengeId,mConfigIndex);
    }

    private ChallengeRewardAdapter createAdapter(List<ChallengeConfig> configs) {
        return new ChallengeRewardAdapter(getContext(), configs,mChallengeEndTime,mChallengeInfo,this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (null != mDismissListener) {
            Bundle bundle = new Bundle();
            if (null != mChallenge) {
                bundle.putParcelable(Constants.BundleKeys.CHALLENGE_DATA, Parcels.wrap(mChallenge));
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
        showMessage(Constants.Alerts.POLL_LIST_EMPTY);
        dismiss();
    }

    @Override
    public void onFailedConfigsApi() {
        showMessage(Constants.Alerts.API_FAIL);
        dismiss();
    }

    @Override
    public void onNoInternet() {
        showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
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
    public void onConfigHeightChanged() {
        mRcvConfigs.postDelayed(new Runnable() {
            @Override
            public void run() {
                int configsHeight = mRcvConfigs.computeVerticalScrollRange() + mTitleHeight;
                Log.d("ChallengeConfigsDialogFragment", "MaxHeight --> " + mMaxHeight + ", " + "ScrollHeight --> " + configsHeight);
                if (configsHeight > mMaxHeight) {
                    configsHeight = mMaxHeight;
                }

                WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
                getDialog().getWindow().setLayout(attributes.width, configsHeight);
            }
        }, 250);
    }


}