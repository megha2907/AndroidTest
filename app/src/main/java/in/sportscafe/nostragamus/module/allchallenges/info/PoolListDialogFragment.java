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
import android.widget.TextView;

import java.util.List;

import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Pool;
import in.sportscafe.nostragamus.module.common.NostragamusDialogFragment;
import in.sportscafe.nostragamus.module.common.OnDismissListener;

/**
 * Created by Jeeva on 28/02/17.
 */
public class PoolListDialogFragment extends NostragamusDialogFragment implements PoolListApiModelImpl.OnPoolListApiModelListener {

    private OnDismissListener mDismissListener;

    private int mDialogRequestCode;

    private int mChallengeId;

    private String mChallengeName;

    private PoolAdapter mPoolAdapter;

    private RecyclerView mRcvPools;

    public static PoolListDialogFragment newInstance(int requestCode, int challengeId, String challengeName) {
        Bundle bundle = new Bundle();
        bundle.putInt(BundleKeys.DIALOG_REQUEST_CODE, requestCode);
        bundle.putInt(BundleKeys.CHALLENGE_ID, challengeId);
        bundle.putString(BundleKeys.CHALLENGE_NAME, challengeName);

        PoolListDialogFragment fragment = new PoolListDialogFragment();
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
        return inflater.inflate(R.layout.fragment_pool_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setCancelable(true);

        openBundle(getArguments());

        initViews();

        getPoolList();
    }

    private void openBundle(Bundle bundle) {
        mDialogRequestCode = bundle.getInt(BundleKeys.DIALOG_REQUEST_CODE);
        mChallengeId = bundle.getInt(BundleKeys.CHALLENGE_ID);
        mChallengeName = bundle.getString(BundleKeys.CHALLENGE_NAME);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.pool_list_tv_challenge_name)).setText(mChallengeName);

        this.mRcvPools = (RecyclerView) findViewById(R.id.pool_list_rcv);
        this.mRcvPools.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        this.mRcvPools.setHasFixedSize(true);
    }

    private void getPoolList() {
        new PoolListApiModelImpl(this).getPoolList(mChallengeId);
    }

    private PoolAdapter createAdapter(List<Pool> poolList) {
        return new PoolAdapter(getContext(), poolList);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (null != mDismissListener) {
            mDismissListener.onDismiss(mDialogRequestCode);
        }
    }

    @Override
    public void onSuccessPoolListApi(List<Pool> poolList) {
        mPoolAdapter = createAdapter(poolList);
        mRcvPools.setAdapter(mPoolAdapter);
    }

    @Override
    public void onEmpty() {
        showMessage(Alerts.POLL_LIST_EMPTY);
    }

    @Override
    public void onFailedPoolListApi() {
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
}