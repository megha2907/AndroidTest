package in.sportscafe.nostragamus.module.popups;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.HashMap;

import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.IntentActions;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusDialogFragment;
import in.sportscafe.nostragamus.module.popups.banktransfer.BankTranferApiModelImpl;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

/**
 * Created by Jeeva on 28/02/17.
 */

public class BankTransferDialogFragment extends NostragamusDialogFragment implements View.OnClickListener {

    private TextView mTv2xInBank;

    private TextView mTvNonegsInBank;

    private TextView mTvPollInBank;

    private TextView mTv2xInAdd;

    private TextView mTvNonegsInAdd;

    private TextView mTvPollInAdd;

    private String mChallengeName;

    private int mChallengeId;

    private int mMaxTransferCount;

    private HashMap<String, Integer> mPowerUpBank = new HashMap<>();

    private HashMap<String, Integer> mTransferedPowerUp = new HashMap<>();

    public static BankTransferDialogFragment newInstance(String challengeName, int challengeId, int maxTransferCount, HashMap<String, Integer> powerUps) {
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.CHALLENGE_NAME, challengeName);
        bundle.putInt(BundleKeys.CHALLENGE_ID, challengeId);
        bundle.putInt(BundleKeys.MAX_TRANSFER_COUNT, maxTransferCount);
        bundle.putParcelable(BundleKeys.POWERUPS, Parcels.wrap(powerUps));

        BankTransferDialogFragment fragment = new BankTransferDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bank_transfer, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
        setCancelable(false);

        initViews();

        initAddPowerUpMap();

        openBundle(getArguments());

        populateBankPowerUp();
    }

    private void initViews() {
        mTv2xInBank = (TextView) findViewById(R.id.bank_transfer_tv_2x_bank);
        mTvNonegsInBank = (TextView) findViewById(R.id.bank_transfer_tv_nonegs_bank);
        mTvPollInBank = (TextView) findViewById(R.id.bank_transfer_tv_poll_bank);
        mTv2xInAdd = (TextView) findViewById(R.id.bank_transfer_tv_2x_add_count);
        mTvNonegsInAdd = (TextView) findViewById(R.id.bank_transfer_tv_nonegs_add_count);
        mTvPollInAdd = (TextView) findViewById(R.id.bank_transfer_tv_poll_add_count);

        getView().findViewById(R.id.bank_transfer_btn_add).setOnClickListener(this);
        getView().findViewById(R.id.bank_transfer_iv_close).setOnClickListener(this);

        getView().findViewById(R.id.bank_transfer_iv_2x_minus).setOnClickListener(this);
        getView().findViewById(R.id.bank_transfer_iv_2x_plus).setOnClickListener(this);

        getView().findViewById(R.id.bank_transfer_iv_nonegs_minus).setOnClickListener(this);
        getView().findViewById(R.id.bank_transfer_iv_nonegs_plus).setOnClickListener(this);

        getView().findViewById(R.id.bank_transfer_iv_poll_minus).setOnClickListener(this);
        getView().findViewById(R.id.bank_transfer_iv_poll_plus).setOnClickListener(this);
    }

    private void set2xInBank(int count) {
        mPowerUpBank.put(Powerups.XX, count);
        mTv2xInBank.setText(count + "");

        updateBankPowerUpBg(mTv2xInBank, count);
    }

    private void setNonegsInBank(int count) {
        mPowerUpBank.put(Powerups.NO_NEGATIVE, count);
        mTvNonegsInBank.setText(count + "");

        updateBankPowerUpBg(mTvNonegsInBank, count);
    }

    private void setPollInBank(int count) {
        mPowerUpBank.put(Powerups.AUDIENCE_POLL, count);
        mTvPollInBank.setText(count + "");

        updateBankPowerUpBg(mTvPollInBank, count);
    }

    private void updateBankPowerUpBg(TextView textView, int count) {
        if (count == 0) {
            textView.setBackground(getResources().getDrawable(R.drawable.bank_powerup_count_bg));
        } else {
            textView.setBackground(getResources().getDrawable(R.drawable.bank_powerup_count_bg_enabled));
        }
    }

    private void set2xInAdd(int count) {
        mTransferedPowerUp.put(Powerups.XX, count);
        mTv2xInAdd.setText(count + "");
    }

    private void setNonegsInAdd(int count) {
        mTransferedPowerUp.put(Powerups.NO_NEGATIVE, count);
        mTvNonegsInAdd.setText(count + "");
    }

    private void setPollInAdd(int count) {
        mTransferedPowerUp.put(Powerups.AUDIENCE_POLL, count);
        mTvPollInAdd.setText(count + "");
    }

    private int get2xInBank() {
        return mPowerUpBank.get(Powerups.XX);
    }

    private int getNonegsInBank() {
        return mPowerUpBank.get(Powerups.NO_NEGATIVE);
    }

    private int getPollInBank() {
        return mPowerUpBank.get(Powerups.AUDIENCE_POLL);
    }

    private int get2xInAdd() {
        return mTransferedPowerUp.get(Powerups.XX);
    }

    private int getNonegsInAdd() {
        return mTransferedPowerUp.get(Powerups.NO_NEGATIVE);
    }

    private int getPollInAdd() {
        return mTransferedPowerUp.get(Powerups.AUDIENCE_POLL);
    }

    private void initAddPowerUpMap() {
        mTransferedPowerUp.put(Powerups.XX, 0);
        mTransferedPowerUp.put(Powerups.NO_NEGATIVE, 0);
        mTransferedPowerUp.put(Powerups.AUDIENCE_POLL, 0);
    }

    private void openBundle(Bundle bundle) {
        mChallengeName = bundle.getString(BundleKeys.CHALLENGE_NAME);
        mChallengeId = bundle.getInt(BundleKeys.CHALLENGE_ID);
        mMaxTransferCount = bundle.getInt(BundleKeys.MAX_TRANSFER_COUNT);
        mPowerUpBank = Parcels.unwrap(bundle.getParcelable(BundleKeys.POWERUPS));
    }

    private void populateBankPowerUp() {
        set2xInBank(get2xInBank());
        setNonegsInBank(getNonegsInBank());
        setPollInBank(getPollInBank());

        ((TextView) findViewById(R.id.bank_transfer_tv_add_limit)).setText(String.format("You can add %1d of each powerup to", mMaxTransferCount));
        ((TextView) findViewById(R.id.bank_transfer_tv_challenge_name)).setText(mChallengeName);
    }

    private void transferToChallenge() {
        showProgressbar();
        BankTranferApiModelImpl.newInstance(new BankTranferApiModelImpl.BankTransferApiModelListener() {
            @Override
            public void onSuccess(HashMap<String, Integer> powerUps) {
                dismissProgressbar();

                UserInfo userInfo = NostragamusDataHandler.getInstance().getUserInfo();
                userInfo.getInfoDetails().setPowerUps(mPowerUpBank);
                NostragamusDataHandler.getInstance().setUserInfo(userInfo);

                broadcastUpdatedPowerUp(powerUps);

                showMessage(Alerts.BANK_TRANSFER_SUCCESS);
                dismiss();
            }

            @Override
            public void onNoInternet() {
                dismissProgressbar();
                showMessage(Alerts.NO_NETWORK_CONNECTION, Toast.LENGTH_LONG);
            }

            @Override
            public void onFailed(String message) {
                dismissProgressbar();
                showMessage(message, Toast.LENGTH_LONG);
            }
        }).transferToChallenge(mTransferedPowerUp, mChallengeId);
    }

    private void broadcastUpdatedPowerUp(HashMap<String, Integer> powerUps) {
        Intent intent = new Intent(IntentActions.ACTION_POWERUPS_UPDATED);
        intent.putExtra(BundleKeys.UPDATED_POWERUPS, Parcels.wrap(powerUps));
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    @Override
    public void onClick(View view) {
        int bankCount;
        int transferCount;
        switch (view.getId()) {
            case R.id.bank_transfer_btn_add:
                transferToChallenge();
                break;
            case R.id.bank_transfer_iv_close:
                dismiss();
                break;
            case R.id.bank_transfer_iv_2x_minus:
                bankCount = get2xInBank() + 1;
                transferCount = get2xInAdd() - 1;
                if (checkTransferLimit(transferCount)) {
                    set2xInAdd(transferCount);
                    set2xInBank(bankCount);
                }
                break;
            case R.id.bank_transfer_iv_2x_plus:
                bankCount = get2xInBank() - 1;
                transferCount = get2xInAdd() + 1;
                if (checkBankLimit(bankCount) && checkTransferLimit(transferCount)) {
                    set2xInAdd(transferCount);
                    set2xInBank(bankCount);
                }
                break;
            case R.id.bank_transfer_iv_nonegs_minus:
                bankCount = getNonegsInBank() + 1;
                transferCount = getNonegsInAdd() - 1;
                if (checkTransferLimit(transferCount)) {
                    setNonegsInAdd(transferCount);
                    setNonegsInBank(bankCount);
                }
                break;
            case R.id.bank_transfer_iv_nonegs_plus:
                bankCount = getNonegsInBank() - 1;
                transferCount = getNonegsInAdd() + 1;
                if (checkBankLimit(bankCount) && checkTransferLimit(transferCount)) {
                    setNonegsInAdd(transferCount);
                    setNonegsInBank(bankCount);
                }
                break;
            case R.id.bank_transfer_iv_poll_minus:
                bankCount = getPollInBank() + 1;
                transferCount = getPollInAdd() - 1;
                if (checkTransferLimit(transferCount)) {
                    setPollInAdd(transferCount);
                    setPollInBank(bankCount);
                }
                break;
            case R.id.bank_transfer_iv_poll_plus:
                bankCount = getPollInBank() - 1;
                transferCount = getPollInAdd() + 1;
                if (checkBankLimit(bankCount) && checkTransferLimit(transferCount)) {
                    setPollInAdd(transferCount);
                    setPollInBank(bankCount);
                }
                break;
        }
    }

    private boolean checkTransferLimit(int count) {
        if (count > mMaxTransferCount) {
            showMessage("You can't add more than " + mMaxTransferCount + " powerups in one category");
            return false;
        }
        return count >= 0;
    }

    private boolean checkBankLimit(int count) {
        if (count < 0) {
            showMessage("Powerup not available");
            return false;
        }
        return true;
    }
}