package in.sportscafe.nostragamus.module.user.myprofile.verify;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;

/**
 * Created by deepanshi on 7/18/17.
 */

public class VerifyPhoneNumberFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = VerifyPhoneNumberFragment.class.getSimpleName();

    private VerifyProfileFragmentListener mVerifyProfileFragmentListener;

    private EditText mUserPhoneNumber;

    private TextView errorTextView;

    private TextView phoneNumberLabel;

    public VerifyPhoneNumberFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof VerifyProfileActivity) {
            mVerifyProfileFragmentListener = (VerifyProfileFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + TAG);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_verify_phone_number, container, false);

        initRootView(rootView);

        initPhoneNumberEditText();

        return rootView;
    }

    private void initRootView(View rootView) {
        mUserPhoneNumber = (EditText) rootView.findViewById(R.id.verify_et_phone_number);
        errorTextView = (TextView) rootView.findViewById(R.id.verify_profile_phone_number_error_textView);
        phoneNumberLabel = (TextView) rootView.findViewById(R.id.verify_phone_no_label);
        rootView.findViewById(R.id.verify_request_otp).setOnClickListener(this);

    }

    private void initPhoneNumberEditText() {

        mUserPhoneNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                phoneNumberLabel.setTextColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.white, null));
                return false;
            }

        });

        mUserPhoneNumber.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable ed) {

                errorTextView.setVisibility(View.INVISIBLE);

                if (ed.length() == 10) {
                    hideSoftKeyboard();
                }
            }
        });
    }

    private void validatePhoneNumber() {

        String phoneNumber = getTrimmedText(mUserPhoneNumber);

        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 10) {
            setPhoneNumberNotValid();
        } else {
            errorTextView.setVisibility(View.INVISIBLE);
            if (mVerifyProfileFragmentListener != null) {
                mVerifyProfileFragmentListener.onVerifyPhoneNumber(phoneNumber);
            } else {
                showMessage(Constants.Alerts.SOMETHING_WRONG);
            }
        }
    }

    private void setPhoneNumberNotValid() {
        errorTextView.setText(Constants.Alerts.INVALID_PHONE_NUMBER);
        errorTextView.setVisibility(View.VISIBLE);
    }

    public void setPhoneNumberAlreadyExist() {
        errorTextView.setText(Constants.Alerts.PHONE_NUMBER_EXIST);
        errorTextView.setVisibility(View.VISIBLE);
    }

    public void setErrorMessage() {
        errorTextView.setText(Constants.Alerts.API_FAIL);
        errorTextView.setVisibility(View.VISIBLE);
    }

    public void showLoadingProgressBar() {
        if (getActivity() != null) {
            findViewById(R.id.verifyPhoneNumberProgressBarLayout).setVisibility(View.VISIBLE);
        }
    }

    public void hideLoadingProgressBar() {
        if (getActivity() != null) {
            findViewById(R.id.verifyPhoneNumberProgressBarLayout).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify_request_otp:
                validatePhoneNumber();
                break;
        }

    }

}
