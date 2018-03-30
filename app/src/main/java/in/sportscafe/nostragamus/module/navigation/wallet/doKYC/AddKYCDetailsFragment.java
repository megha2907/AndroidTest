package in.sportscafe.nostragamus.module.navigation.wallet.doKYC;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;

import org.parceler.Parcels;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.addphoto.AddPhotoActivity;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.customViews.CustomSnackBar;
import in.sportscafe.nostragamus.module.navigation.wallet.doKYC.dto.KYCDetails;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.AddPaytmOrBankDetailModelModelImpl;
import in.sportscafe.nostragamus.module.permission.PermissionsActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsChecker;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.login.dto.UserPaymentInfo;
import in.sportscafe.nostragamus.module.user.login.dto.UserPaymentInfoBankDto;
import okhttp3.ResponseBody;

import static android.app.Activity.RESULT_OK;

/**
 * Created by deepanshi on 3/27/18.
 */

public class AddKYCDetailsFragment extends BaseFragment implements View.OnFocusChangeListener, View.OnClickListener {

    private static final String TAG = AddKYCDetailsFragment.class.getSimpleName();
    private AddKYCDetailsFragmentListener mFragmentListener;

    private EditText mUserNameEditText;
    private EditText mPANNumberEditText;
    private EditText mDOBEditText;
    private ImageView mPanImageView;
    private File panCardFile;


    private static final int ADD_PHOTO_REQUEST_CODE = 24;

    public AddKYCDetailsFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddKYCDetailsFragmentListener) {
            mFragmentListener = (AddKYCDetailsFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " +
                    AddKYCDetailsFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_kyc_details, container, false);
        initViews(rootView);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        checkKYCStatus();
    }


    private void checkKYCStatus() {

        String kycStatusFromServer = "";

        UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();
        if (userInfo != null && userInfo.getInfoDetails() != null && !TextUtils.isEmpty(userInfo.getInfoDetails().getKycStatus())) {
            kycStatusFromServer = userInfo.getInfoDetails().getKycStatus();
        }

        switch (kycStatusFromServer) {
            case Constants.KYCStatus.NOT_REQUIRED:
                allowAddingKYCDetails();
                break;

            case Constants.KYCStatus.REQUIRED:
                allowAddingKYCDetails();
                break;

            case Constants.KYCStatus.UPLOADED:
                showVerificationStatus(Constants.KYCStatus.UPLOADED);
                getUserKYCInfo();
                blockEditingKYCDetails();
                break;

            case Constants.KYCStatus.VERIFIED:
                showVerificationStatus(Constants.KYCStatus.VERIFIED);
                getUserKYCInfo();
                blockEditingKYCDetails();
                break;

            case Constants.KYCStatus.FAILED:
                showVerificationFailed();
                getUserKYCInfo();
                allowAddingKYCDetails();
                break;

            case Constants.KYCStatus.BLOCKED:
                showVerificationFailed();
                getUserKYCInfo();
                blockEditingKYCDetails();
                break;

        }

    }

    private void showVerificationFailed() {
        if (getView() != null) {
            getView().findViewById(R.id.add_kyc_details_failed_layout).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.kyc_benefits_layout).setVisibility(View.GONE);
            TextView failedStatus = (TextView) getView().findViewById(R.id.add_kyc_details_failed_status_msg);
            failedStatus.setText("KYC rejected because the data entered does not match the PAN image. Please upload valid info to complete your KYC");
        }
    }

    private void showVerificationStatus(String status) {
        if (getView() != null) {
            getView().findViewById(R.id.add_kyc_details_verification_status_layout).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.kyc_benefits_layout).setVisibility(View.GONE);
            TextView verificationStatus = (TextView) getView().findViewById(R.id.add_kyc_details_verification_status_tv);

            if (status.equalsIgnoreCase(Constants.KYCStatus.VERIFIED)) {
                verificationStatus.setText("Verification completed");
                verificationStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.paid_entry_tv_color));
            } else if (status.equalsIgnoreCase(Constants.KYCStatus.UPLOADED)) {
                verificationStatus.setText("Verification in Progress");
                verificationStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.blue_008ae1));
            }

        }
    }


    private void initViews(View rootView) {
        mUserNameEditText = (EditText) rootView.findViewById(R.id.add_kyc_details_name_edittext);
        mPANNumberEditText = (EditText) rootView.findViewById(R.id.add_kyc_details_pan_number_edittext);
        mDOBEditText = (EditText) rootView.findViewById(R.id.add_kyc_details_dob_edittext);
        mPanImageView = (ImageView) rootView.findViewById(R.id.add_kyc_details_show_pan_image);
        final RelativeLayout uploadPANLayout = (RelativeLayout) rootView.findViewById(R.id.add_kyc_details_upload_pan_layout);
        RelativeLayout uploadPANButton = (RelativeLayout) rootView.findViewById(R.id.add_kyc_details_upload_pan_btn);
        rootView.findViewById(R.id.add_kyc_details_save_btn).setOnClickListener(this);
        uploadPANButton.setOnClickListener(this);

        mPANNumberEditText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mPANNumberEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideSoftKeyboard();
                    mPANNumberEditText.clearFocus();
                    uploadPANLayout.requestFocus();
                }
                return false;
            }
        });

        final Calendar dobCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                dobCalendar.set(Calendar.YEAR, year);
                dobCalendar.set(Calendar.MONTH, monthOfYear);
                dobCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(dobCalendar, mDOBEditText);
            }

        };

        mDOBEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, dobCalendar
                        .get(Calendar.YEAR), dobCalendar.get(Calendar.MONTH),
                        dobCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mUserNameEditText.setOnFocusChangeListener(this);
        mPANNumberEditText.setOnFocusChangeListener(this);
        mDOBEditText.setOnFocusChangeListener(this);
    }

    private void updateLabel(Calendar dobCalendar, EditText DOBEditText) {
        String myFormat = "dd / MM / yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        DOBEditText.setText(sdf.format(dobCalendar.getTime()));
    }

    private void scrollView() {
        if (getView() != null) {
            final ScrollView scrollView = (ScrollView) getView().findViewById(R.id.add_kyc_sv);
            scrollView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    View lastChild = scrollView.getChildAt(scrollView.getChildCount() - 1);
                    int bottom = lastChild.getBottom() + scrollView.getPaddingBottom();
                    int sy = scrollView.getScrollY();
                    int sh = scrollView.getHeight();
                    int delta = bottom - (sy + sh);
                    scrollView.smoothScrollBy(0, delta);
                }
            }, 200);
        }
    }


    private boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable) drawable).getBitmap() != null;
        }

        return hasImage;
    }

    public void navigateToAddPhoto(int requestCode) {
        if (mFragmentListener != null) {
            mFragmentListener.navigateToAddPhotoActivity(requestCode);
        }
    }


    public void showImagePopup(View view) {
        if (new PermissionsChecker(getContext()).lacksPermissions(Constants.AppPermissions.STORAGE)) {
            if (mFragmentListener != null) {
                mFragmentListener.startPermissionActivity();
            }
        } else {
            navigateToAddPhoto(ADD_PHOTO_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && ADD_PHOTO_REQUEST_CODE == requestCode) {
            onGetImage(data);
            hideUploadImageError();
        }

        if (Constants.RequestCodes.STORAGE_PERMISSION == requestCode && PermissionsActivity.PERMISSIONS_GRANTED == resultCode) {
            navigateToAddPhoto(ADD_PHOTO_REQUEST_CODE);
        }
    }


    public void onGetImage(Intent imageData) {
        String imagePath = imageData.getStringExtra(Constants.BundleKeys.ADDED_NEW_IMAGE_PATH);
        panCardFile = new File(imagePath);
        mPanImageView.setImageURI(Uri.fromFile(panCardFile));
        final RelativeLayout uploadPANLayout = (RelativeLayout) getView().findViewById(R.id.add_kyc_details_show_pan_image_layout);
        uploadPANLayout.setVisibility(View.VISIBLE);
        scrollView();
    }

    private void getUserKYCInfo() {
        if (getActivity() != null) {
            GetKYCDetailsDataProvider dataProvider = new GetKYCDetailsDataProvider();
            dataProvider.getKYCDetails(getContext(), getKYCDetailsCallBackListener());
        }
    }

    private GetKYCDetailsDataProvider.GetKYCDetailsDataProviderListener getKYCDetailsCallBackListener() {

        return new GetKYCDetailsDataProvider.GetKYCDetailsDataProviderListener() {

            @Override
            public void onData(int status, @Nullable KYCDetails kycDetails) {
                setKYCDetails(kycDetails);
            }

            @Override
            public void onError(int status) {
                if (mFragmentListener != null) {
                    mFragmentListener.handleError("", Constants.DataStatus.FROM_SERVER_API_FAILED);
                }
            }

            @Override
            public void onImageData(int status, @Nullable ResponseBody body) {
                if (body != null && mPanImageView != null) {
                    // display the image data in a ImageView
                    Bitmap bm = BitmapFactory.decodeStream(body.byteStream());
                    mPanImageView.setImageBitmap(bm);
                    final RelativeLayout uploadPANLayout = (RelativeLayout) getView().findViewById(R.id.add_kyc_details_show_pan_image_layout);
                    uploadPANLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNoInternet() {
                if (mFragmentListener != null) {
                    mFragmentListener.handleError("", Constants.DataStatus.NO_INTERNET);
                }
            }
        };
    }

    private void setKYCDetails(KYCDetails kycDetails) {

        if (kycDetails != null) {
            if (!TextUtils.isEmpty(kycDetails.getUserName())) {
                mUserNameEditText.setText(kycDetails.getUserName());
            }
            if (!TextUtils.isEmpty(kycDetails.getPanNumber())) {
                mPANNumberEditText.setText(kycDetails.getPanNumber());
            }
            if (!TextUtils.isEmpty(kycDetails.getDateOfBirth())) {
                mDOBEditText.setText(kycDetails.getDateOfBirth());
            }
        }

    }

    public void onAddKYCDetailsSaveClicked() {

        if (getActivity() != null && getView() != null) {

            hideSoftKeyboard();

            String name = getTrimmedText(mUserNameEditText);
            String panNumber = getTrimmedText(mPANNumberEditText);
            String dob = getTrimmedText(mDOBEditText);

            TextView nameErrorTextView = (TextView) getView().findViewById(R.id.add_kyc_details_name_error_textview);
            TextView panCardErrorTextView = (TextView) getView().findViewById(R.id.add_kyc_details_pan_number_error_textview);
            TextView dobErrorTextView = (TextView) getView().findViewById(R.id.add_kyc_details_dob_error_textview);
            TextView uploadPanErrorTextView = (TextView) getView().findViewById(R.id.add_kyc_details_upload_pan_error_textview);
            ImageView panImageView = (ImageView) getView().findViewById(R.id.add_kyc_details_show_pan_image);

            //Verify if PAN Number is correct
            Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
            Matcher matcher = pattern.matcher(panNumber);

            // check if name exist
            if (name.length() == 0) {
                nameErrorTextView.setVisibility(View.VISIBLE);
            }  // check if PAN number is correct
            else if (panNumber.length() != 10 && !matcher.matches()) {
                panCardErrorTextView.setVisibility(View.VISIBLE);
                nameErrorTextView.setVisibility(View.GONE);
            } // check if DOB exist
            else if (dob.length() == 0) {
                dobErrorTextView.setVisibility(View.VISIBLE);
                panCardErrorTextView.setVisibility(View.GONE);
                nameErrorTextView.setVisibility(View.GONE);
            } // check if Pan card image uploaded
            else if (!hasImage(panImageView) && panCardFile != null) {
                uploadPanErrorTextView.setVisibility(View.VISIBLE);
                panCardErrorTextView.setVisibility(View.GONE);
                nameErrorTextView.setVisibility(View.GONE);
                dobErrorTextView.setVisibility(View.GONE);
            } // send all details to api
            else {
                uploadPanErrorTextView.setVisibility(View.GONE);
                panCardErrorTextView.setVisibility(View.GONE);
                nameErrorTextView.setVisibility(View.GONE);
                dobErrorTextView.setVisibility(View.GONE);

                showLoadingProgressBar();
                AddKYCDetailsDataProvider detailsModel = new AddKYCDetailsDataProvider();
                detailsModel.saveUserPANDetails(name, panNumber, dob, panCardFile, getCallBackListener());
            }
        }
    }


    private AddKYCDetailsDataProvider.AddKYCDetailsDataProviderModelListener getCallBackListener() {

        return new AddKYCDetailsDataProvider.AddKYCDetailsDataProviderModelListener() {
            @Override
            public void onAddDetailSuccess() {
                hideLoadingProgressBar();
                onApiSuccess();
            }

            @Override
            public void onNoInternet() {
                hideLoadingProgressBar();
                if (mFragmentListener != null) {
                    mFragmentListener.handleError("", Constants.DataStatus.NO_INTERNET);
                }
            }

            @Override
            public void onAddDetailFailed() {
                hideLoadingProgressBar();
                if (mFragmentListener != null) {
                    mFragmentListener.handleError("", Constants.DataStatus.FROM_SERVER_API_FAILED);
                }
            }

            @Override
            public void onServerSentError(String errorMsg) {
                hideLoadingProgressBar();
                if (TextUtils.isEmpty(errorMsg)) {
                    errorMsg = Constants.Alerts.SOMETHING_WRONG;
                }
                if (mFragmentListener != null) {
                    mFragmentListener.handleError(errorMsg, -1);
                }
            }
        };
    }

    private void onApiSuccess() {
        if (mFragmentListener != null) {
            mFragmentListener.finishThisAndGotoWalletScreen();
        }
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (getActivity() != null && getView() != null && getContext() != null) {
            switch (v.getId()) {
                case R.id.add_kyc_details_name_edittext:
                    TextView textView = (TextView) getView().findViewById(R.id.add_kyc_details_name_textview);
                    if (hasFocus) {
                        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    } else {
                        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white_999999));
                    }
                    break;

                case R.id.add_kyc_details_pan_number_edittext:
                    textView = (TextView) getView().findViewById(R.id.add_kyc_details_pan_number_textview);
                    if (hasFocus) {
                        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    } else {
                        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white_999999));
                    }
                    break;

                case R.id.add_kyc_details_dob_edittext:
                    textView = (TextView) getView().findViewById(R.id.add_kyc_details_dob_textview);
                    if (hasFocus) {
                        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    } else {
                        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white_999999));
                    }
                    break;
            }
        }
    }

    private void showLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.add_kyc_loading_anim).setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.add_kyc_loading_anim).setVisibility(View.GONE);
        }
    }

    private void hideUploadImageError() {
        if (getView() != null) {
            getView().findViewById(R.id.add_kyc_details_upload_pan_error_textview).setVisibility(View.GONE);
        }
    }

    private void allowAddingKYCDetails() {
        if (getView() != null && getActivity() != null) {
            mUserNameEditText.setEnabled(true);
            mPANNumberEditText.setEnabled(true);
            mDOBEditText.setEnabled(true);
            getView().findViewById(R.id.add_kyc_details_save_btn).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.add_kyc_details_upload_pan_btn).setVisibility(View.VISIBLE);
        }
    }

    private void blockEditingKYCDetails() {
        if (getView() != null && getActivity() != null) {
            mUserNameEditText.setEnabled(false);
            mPANNumberEditText.setEnabled(false);
            mDOBEditText.setEnabled(false);
            getView().findViewById(R.id.add_kyc_details_save_btn).setVisibility(View.GONE);
            getView().findViewById(R.id.add_kyc_details_upload_pan_btn).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_kyc_details_save_btn:
                onAddKYCDetailsSaveClicked();
                break;

            case R.id.add_kyc_details_upload_pan_btn:
                showImagePopup(v);
                break;
        }
    }
}