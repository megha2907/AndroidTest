package in.sportscafe.nostragamus.module.popups;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.HashMap;

import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.IntentActions;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeUserInfo;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.NostragamusDialogFragment;
import in.sportscafe.nostragamus.module.popups.banktransfer.BankTranferApiModelImpl;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

import static android.R.id.replaceText;
import static com.google.android.gms.analytics.internal.zzy.d;

/**
 * Created by Jeeva on 28/02/17.
 */

public class BankTransferDialogFragment extends NostragamusDialogFragment implements View.OnClickListener {

    private TextView mTv2xInBank;

    private TextView mTvNonegsInBank;

    private TextView mTvPollInBank;

    private TextView mTv2xToWithdraw;

    private TextView mTvNonegsToWithdraw;

    private TextView mTvPollToWithdraw;


    private Challenge mChallengeInfo;

    private int mMaxTransferCount;

    private HashMap<String, Integer> mWithdrawnPowerUps = new HashMap<>();

    private HashMap<String, Integer> mPowerUpsInBank = new HashMap<>();

    private HashMap<String, Integer> mPowerUpsToWithdraw = new HashMap<>();

    public static BankTransferDialogFragment newInstance(Bundle bundle) {
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

        NostragamusAnalytics.getInstance().trackPowerBank(AnalyticsActions.STARTED);

        initViews();

        initWithdrawPowerUps();

        openBundle(getArguments());

        populateBankPowerUp();
    }

    private void initViews() {
        mTv2xInBank = (TextView) findViewById(R.id.bank_transfer_tv_2x_bank);
        mTvNonegsInBank = (TextView) findViewById(R.id.bank_transfer_tv_nonegs_bank);
        mTvPollInBank = (TextView) findViewById(R.id.bank_transfer_tv_poll_bank);
        mTv2xToWithdraw = (TextView) findViewById(R.id.bank_transfer_tv_2x_add_count);
        mTvNonegsToWithdraw = (TextView) findViewById(R.id.bank_transfer_tv_nonegs_add_count);
        mTvPollToWithdraw = (TextView) findViewById(R.id.bank_transfer_tv_poll_add_count);

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
        mPowerUpsInBank.put(Powerups.XX, count);
        mTv2xInBank.setText(count + "");

        updateBankPowerUpBg(mTv2xInBank, count);
    }

    private void setNonegsInBank(int count) {
        mPowerUpsInBank.put(Powerups.NO_NEGATIVE, count);
        mTvNonegsInBank.setText(count + "");

        updateBankPowerUpBg(mTvNonegsInBank, count);
    }

    private void setPollInBank(int count) {
        mPowerUpsInBank.put(Powerups.AUDIENCE_POLL, count);
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

    private void set2xToWithdraw(int count) {
        mPowerUpsToWithdraw.put(Powerups.XX, count);
        mTv2xToWithdraw.setText(count + "");
    }

    private void setNonegsToWithdraw(int count) {
        mPowerUpsToWithdraw.put(Powerups.NO_NEGATIVE, count);
        mTvNonegsToWithdraw.setText(count + "");
    }

    private void setPollToWithdraw(int count) {
        mPowerUpsToWithdraw.put(Powerups.AUDIENCE_POLL, count);
        mTvPollToWithdraw.setText(count + "");
    }

    private int get2xInBank() {
        return mPowerUpsInBank.get(Powerups.XX);
    }

    private int getNonegsInBank() {
        return mPowerUpsInBank.get(Powerups.NO_NEGATIVE);
    }

    private int getPollInBank() {
        return mPowerUpsInBank.get(Powerups.AUDIENCE_POLL);
    }

    private int get2xToWithdraw() {
        return mPowerUpsToWithdraw.get(Powerups.XX);
    }

    private int getNonegsToWithdraw() {
        return mPowerUpsToWithdraw.get(Powerups.NO_NEGATIVE);
    }

    private int getPollToWithdraw() {
        return mPowerUpsToWithdraw.get(Powerups.AUDIENCE_POLL);
    }

    private void initWithdrawPowerUps() {
        mPowerUpsToWithdraw.put(Powerups.XX, 0);
        mPowerUpsToWithdraw.put(Powerups.NO_NEGATIVE, 0);
        mPowerUpsToWithdraw.put(Powerups.AUDIENCE_POLL, 0);
    }

    private void openBundle(Bundle bundle) {
        mChallengeInfo = Parcels.unwrap(bundle.getParcelable(BundleKeys.CHALLENGE_INFO));

        mMaxTransferCount = mChallengeInfo.getChallengeInfo().getMaxWithdrawLimit();
        mWithdrawnPowerUps = mChallengeInfo.getChallengeUserInfo().getWithdrawnPowerUps();

        mPowerUpsInBank = NostragamusDataHandler.getInstance().getUserInfo().getPowerUps();
    }

    private void populateBankPowerUp() {
        set2xInBank(get2xInBank());
        setNonegsInBank(getNonegsInBank());
        setPollInBank(getPollInBank());

        String transferCount = mMaxTransferCount + "";
        String challengeName = mChallengeInfo.getName();
        String text = String.format("Transfer maximum %1s powerups of each type to the %2s challenge", transferCount, challengeName);

        SpannableString spannable = new SpannableString(text);
        spannable.setSpan(new UnderlineSpan(), text.indexOf(transferCount), text.indexOf(transferCount) + transferCount.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new UnderlineSpan(), text.indexOf(challengeName), text.indexOf(challengeName) + challengeName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ((TextView) findViewById(R.id.bank_transfer_tv_add_limit)).setText(spannable);
        ((TextView) findViewById(R.id.bank_transfer_tv_challenge_name)).setText(mChallengeInfo.getName());
    }

    private void transferToChallenge() {
        showProgressbar();
        BankTranferApiModelImpl.newInstance(new BankTranferApiModelImpl.BankTransferApiModelListener() {
            @Override
            public void onSuccess(ChallengeUserInfo challengeUserInfo) {
                dismissProgressbar();

                UserInfo userInfo = NostragamusDataHandler.getInstance().getUserInfo();
                userInfo.getInfoDetails().setPowerUps(mPowerUpsInBank);
                NostragamusDataHandler.getInstance().setUserInfo(userInfo);

                broadcastUpdatedChallengeInfo(challengeUserInfo);

//                showMessage(Alerts.BANK_TRANSFER_SUCCESS);
                dismiss();

                NostragamusAnalytics.getInstance().trackPowerBank(AnalyticsActions.COMPLETED);
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
        }).transferToChallenge(mPowerUpsToWithdraw, mChallengeInfo.getChallengeId());

        NostragamusAnalytics.getInstance().trackPowerBank(AnalyticsActions.ADDED);
    }

    private void broadcastUpdatedChallengeInfo(ChallengeUserInfo challengeUserInfo) {
        Intent intent = new Intent(IntentActions.ACTION_POWERUPS_UPDATED);
        intent.putExtra(BundleKeys.UPDATED_CHALLENGE_USER_INFO, Parcels.wrap(challengeUserInfo));
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
                removeFromChallenge(Powerups.XX);
                break;
            case R.id.bank_transfer_iv_2x_plus:
                addToChallenge(Powerups.XX);
                break;
            case R.id.bank_transfer_iv_nonegs_minus:
                removeFromChallenge(Powerups.NO_NEGATIVE);
                break;
            case R.id.bank_transfer_iv_nonegs_plus:
                addToChallenge(Powerups.NO_NEGATIVE);
                break;
            case R.id.bank_transfer_iv_poll_minus:
                removeFromChallenge(Powerups.AUDIENCE_POLL);
                break;
            case R.id.bank_transfer_iv_poll_plus:
                addToChallenge(Powerups.AUDIENCE_POLL);
                break;
        }
    }

    private void addToChallenge(String powerUpId) {
        updatePowerUps(powerUpId, mPowerUpsInBank.get(powerUpId) - 1, mPowerUpsToWithdraw.get(powerUpId) + 1);
    }

    private void removeFromChallenge(String powerUpId) {
        updatePowerUps(powerUpId, mPowerUpsInBank.get(powerUpId) + 1, mPowerUpsToWithdraw.get(powerUpId) - 1);
    }

    private void updatePowerUps(String powerUpId, int amountInBank, int withdrawAmout) {
        if (checkBankLimit(amountInBank) && checkWithdrawLimit(withdrawAmout, mWithdrawnPowerUps.get(powerUpId))) {
            switch (powerUpId) {
                case Powerups.XX:
                    set2xToWithdraw(withdrawAmout);
                    set2xInBank(amountInBank);
                    break;
                case Powerups.NO_NEGATIVE:
                    setNonegsToWithdraw(withdrawAmout);
                    setNonegsInBank(amountInBank);
                    break;
                case Powerups.AUDIENCE_POLL:
                    setPollToWithdraw(withdrawAmout);
                    setPollInBank(amountInBank);
                    break;
            }
        }
    }

    private boolean checkWithdrawLimit(int withdrawAmount, int alreadyWithdrawnAmout) {
        if (withdrawAmount > 0 && (withdrawAmount + alreadyWithdrawnAmout) > mMaxTransferCount) {
            showMessage("You can't add more than " + mMaxTransferCount + " powerups in one category");
            return false;
        }
        return withdrawAmount >= 0;
    }

    private boolean checkBankLimit(int count) {
        if (count < 0) {
            showMessage("Powerup is not available in your bank");
            return false;
        }
        return true;
    }
}