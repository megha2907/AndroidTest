package in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.common.NostragamusDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaytmTransactionFailureDialogFragment extends NostragamusDialogFragment implements View.OnClickListener {

    public interface IPaytmFailureActionListener {
        void onRejoinClicked();
        void onBackToHomeClicked();
    }

    private IPaytmFailureActionListener mPaytmFailureListener;
    private int mDialogRequestCode;
    private Challenge mChallenge;

    public void setFailureListener(IPaytmFailureActionListener listener) {
        mPaytmFailureListener = listener;
    }

    public static PaytmTransactionFailureDialogFragment newInstance(int requestCode, Challenge challenge,
                                                                    IPaytmFailureActionListener listener) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BundleKeys.DIALOG_REQUEST_CODE, requestCode);
        bundle.putParcelable(Constants.BundleKeys.CHALLENGE_DATA, Parcels.wrap(challenge));

        PaytmTransactionFailureDialogFragment fragment = new PaytmTransactionFailureDialogFragment();
        fragment.setFailureListener(listener);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_paytm_transaction_failure_dialog, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setCancelable(false);
        openBundle(getArguments());
        initViews();
        initValues();
    }

    private void openBundle(Bundle bundle) {
        mDialogRequestCode = bundle.getInt(Constants.BundleKeys.DIALOG_REQUEST_CODE);
        mChallenge = Parcels.unwrap(bundle.getParcelable(Constants.BundleKeys.CHALLENGE_DATA));
    }

    private void initViews() {
        findViewById(R.id.paytm_failure_backToHome_button).setOnClickListener(this);
        findViewById(R.id.paytm_failure_rejoin_button).setOnClickListener(this);
    }

    private void initValues() {

        TextView challengeDetailsMsgTextview = (TextView) findViewById(R.id.paytm_failed_challenge_str_Textview);
        if (mChallenge != null && !TextUtils.isEmpty(mChallenge.getName())) {
            challengeDetailsMsgTextview.setText(getSpannableStringBuilder(mChallenge.getName()), TextView.BufferType.SPANNABLE);
        }
    }

    @NonNull
    private SpannableStringBuilder getSpannableStringBuilder(@NonNull String challengeName) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        String priceTxt1 = "Transaction for joining the ";
        SpannableString priceTxt1Spannable= new SpannableString(priceTxt1);
        priceTxt1Spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#c2c2c2")), 0, priceTxt1.length(), 0);
        builder.append(priceTxt1Spannable);

        String priceTxt2 = challengeName;
        SpannableString priceTxt2Spannable= new SpannableString(priceTxt2);
        priceTxt2Spannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, priceTxt2.length(), 0);
        builder.append(priceTxt2Spannable);

        String priceTxt3 = " challenge has failed.";
        SpannableString priceTxt3Spannable= new SpannableString(priceTxt3);
        priceTxt3Spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#c2c2c2")), 0, priceTxt3.length(), 0);
        builder.append(priceTxt3Spannable);
        return builder;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.paytm_failure_backToHome_button:
                if (mPaytmFailureListener != null) {
                    mPaytmFailureListener.onBackToHomeClicked();
                }
                dismiss();
                break;

            case R.id.paytm_failure_rejoin_button:
                if (mPaytmFailureListener != null) {
                    mPaytmFailureListener.onRejoinClicked();
                }
                dismiss();
                break;
        }
    }
}
