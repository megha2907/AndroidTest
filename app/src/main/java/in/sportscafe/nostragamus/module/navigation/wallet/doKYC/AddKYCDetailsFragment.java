package in.sportscafe.nostragamus.module.navigation.wallet.doKYC;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.doKYC.dto.KYCDetails;
import in.sportscafe.nostragamus.module.permission.PermissionsActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsChecker;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
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
        checkForPermissions();
    }

    private void checkForPermissions() {

        if (getActivity() != null) {
            // Check if we have write permission
            int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                        getActivity(),
                        Constants.AppPermissions.STORAGE,
                        Constants.RequestCodes.STORAGE_PERMISSION
                );
            }
        }
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

        mUserNameEditText.setOnFocusChangeListener(this);
        mPANNumberEditText.setOnFocusChangeListener(this);
        mDOBEditText.setOnFocusChangeListener(this);

        TextView kycBenefitsOne = (TextView) rootView.findViewById(R.id.kyc_benefits_text_one);
        if (!TextUtils.isEmpty(NostragamusDataHandler.getInstance().getKYCBenefitsOne())) {
            kycBenefitsOne.setText(NostragamusDataHandler.getInstance().getKYCBenefitsOne());
        } else {
            kycBenefitsOne.setText("You have reached your transaction limit. Please update your KYC to make withdrawals");
        }

        TextView kycBenefitsTwo = (TextView) rootView.findViewById(R.id.kyc_benefits_text_two);
        if (!TextUtils.isEmpty(NostragamusDataHandler.getInstance().getKYCBenefitsTwo())) {
            kycBenefitsTwo.setText(NostragamusDataHandler.getInstance().getKYCBenefitsTwo());
        } else {
            kycBenefitsTwo.setText("On updating KYC, users will get 2 powerups each added to their powerup bank!");
        }


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

                TextView dobHeading = (TextView) getView().findViewById(R.id.add_kyc_details_dob_textview);
                dobHeading.setTextColor(ContextCompat.getColor(getContext(), R.color.white_999999));
                mDOBEditText.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.white_999999), PorterDuff.Mode.SRC_IN);

            }

        };

        mDOBEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mUserNameEditText.clearFocus();
                mPANNumberEditText.clearFocus();
                mDOBEditText.requestFocus();
                TextView dobHeading = (TextView) getView().findViewById(R.id.add_kyc_details_dob_textview);
                dobHeading.setTextColor(ContextCompat.getColor(getContext(), R.color.blue_008ae1));
                mDOBEditText.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.blue_008ae1), PorterDuff.Mode.SRC_IN);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), date,
                        dobCalendar.get(Calendar.YEAR),
                        dobCalendar.get(Calendar.MONTH),
                        dobCalendar.get(Calendar.DAY_OF_MONTH));

                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();

            }
        });

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

        if (getView() != null) {
            TextView panImageHeading = (TextView) getView().findViewById(R.id.add_kyc_details_upload_pan_textview);
            panImageHeading.setTextColor(ContextCompat.getColor(getContext(), R.color.white_999999));
            TextView uploadPanErrorTextView = (TextView) getView().findViewById(R.id.add_kyc_details_upload_pan_error_textview);
            uploadPanErrorTextView.setVisibility(View.GONE);
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
        if (!TextUtils.isEmpty(imagePath)) {
            panCardFile = new File(imagePath);
            mPanImageView.setImageURI(Uri.fromFile(panCardFile));
            showImageView();
            scrollView();
        }
    }

    private void showImageView() {
        if (getView() != null && getActivity() != null) {
            RelativeLayout uploadPANLayout = (RelativeLayout) getView().findViewById(R.id.add_kyc_details_show_pan_image_layout);
            ImageView uploadedImageIcon = (ImageView) getView().findViewById(R.id.add_kyc_details_pan_uploaded_icon);
            uploadPANLayout.setVisibility(View.VISIBLE);
            uploadedImageIcon.setVisibility(View.VISIBLE);
        }
    }

    private void getUserKYCInfo() {
        if (getActivity() != null) {
            GetKYCDetailsDataProvider dataProvider = new GetKYCDetailsDataProvider();
            dataProvider.getKYCDetails(getContext(), getKYCDetailsCallBackListener());
            showLoadingProgressBar();
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
                if (body != null) {
                    if (DownloadImage(body)) {
                        setPanCardImage();
                    } else {
                        onCouldNotLoadImage();
                    }
                } else {
                    onCouldNotLoadImage();
                }
            }

            @Override
            public void onImageFailed() {
                onCouldNotLoadImage();
            }

            @Override
            public void onNoInternet() {
                if (mFragmentListener != null) {
                    mFragmentListener.handleError("", Constants.DataStatus.FROM_SERVER_API_FAILED);
                }
            }
        };
    }

    private void onCouldNotLoadImage() {
        hideLoadingProgressBar();
        if (mFragmentListener != null) {
            mFragmentListener.handleError("", Constants.DataStatus.COULD_NOT_LOAD_IMAGE);
        }
    }


    private void setKYCDetails(KYCDetails kycDetails) {

        if (kycDetails != null && getView() != null) {
            if (!TextUtils.isEmpty(kycDetails.getUserName())) {
                mUserNameEditText.setText(kycDetails.getUserName());
            }
            if (!TextUtils.isEmpty(kycDetails.getPanNumber())) {
                mPANNumberEditText.setText(kycDetails.getPanNumber());
            }
            if (!TextUtils.isEmpty(kycDetails.getDateOfBirth())) {
                mDOBEditText.setText(kycDetails.getDateOfBirth());
            }
            if (!TextUtils.isEmpty(kycDetails.getKycRejectedError())) {
                TextView uploadPanErrorTextView = (TextView) getView().findViewById(R.id.add_kyc_details_failed_status_msg);
                uploadPanErrorTextView.setText(kycDetails.getKycRejectedError());
            }

        }

    }


    private boolean DownloadImage(ResponseBody body) {

        try {
            Log.d("DownloadImage", "Reading and writing file");
            InputStream in = null;
            FileOutputStream out = null;

            try {
                in = body.byteStream();
                out = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "/Nostragamus");
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

            } catch (IOException e) {
                Log.d("DownloadImage", e.toString());
                return false;
            } finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }

            return true;

        } catch (IOException e) {
            Log.d("DownloadImage", e.toString());
            onCouldNotLoadImage();
            return false;
        }
    }

    private void setPanCardImage() {

        if (mPanImageView != null) {
            int width, height;
            panCardFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "/Nostragamus");
            Bitmap bMap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "/Nostragamus");
            if (bMap != null) {
                width = bMap.getWidth();
                height = bMap.getHeight();
                Bitmap bMap2 = Bitmap.createScaledBitmap(bMap, width, height, false);
                mPanImageView.setImageBitmap(bMap2);
                showImageView();
                hideLoadingProgressBar();
            } else {
                onCouldNotLoadImage();
            }
        } else {
            onCouldNotLoadImage();
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

            EditText userNameEditText = (EditText) getView().findViewById(R.id.add_kyc_details_name_edittext);
            EditText panNumberEditText = (EditText) getView().findViewById(R.id.add_kyc_details_pan_number_edittext);
            EditText dobEditText = (EditText) getView().findViewById(R.id.add_kyc_details_dob_edittext);

            TextView userNameHeading = (TextView) getView().findViewById(R.id.add_kyc_details_name_textview);
            TextView panNumberHeading = (TextView) getView().findViewById(R.id.add_kyc_details_pan_number_textview);
            TextView dobHeading = (TextView) getView().findViewById(R.id.add_kyc_details_dob_textview);
            TextView panImageHeading = (TextView) getView().findViewById(R.id.add_kyc_details_upload_pan_textview);

            //Verify if PAN Number is correct
            Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
            Matcher matcher = pattern.matcher(panNumber);

            userNameEditText.clearFocus();
            panNumberEditText.clearFocus();
            dobEditText.clearFocus();

            // check if name exist
            if (name.length() == 0) {
                nameErrorTextView.setVisibility(View.VISIBLE);
                userNameHeading.setTextColor(ContextCompat.getColor(getContext(), R.color.radical_red));
                userNameEditText.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.radical_red), PorterDuff.Mode.SRC_IN);
            }  // check if PAN number is correct
            else if (panNumber.length() != 10 && !matcher.matches()) {
                panCardErrorTextView.setVisibility(View.VISIBLE);
                nameErrorTextView.setVisibility(View.GONE);
                panNumberHeading.setTextColor(ContextCompat.getColor(getContext(), R.color.radical_red));
                panNumberEditText.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.radical_red), PorterDuff.Mode.SRC_IN);
            } // check if DOB exist
            else if (dob.length() == 0) {
                dobErrorTextView.setVisibility(View.VISIBLE);
                panCardErrorTextView.setVisibility(View.GONE);
                nameErrorTextView.setVisibility(View.GONE);
                dobHeading.setTextColor(ContextCompat.getColor(getContext(), R.color.radical_red));
                dobEditText.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.radical_red), PorterDuff.Mode.SRC_IN);
            } // check if Pan card image uploaded
            else if (!hasImage(panImageView)) {
                uploadPanErrorTextView.setVisibility(View.VISIBLE);
                panCardErrorTextView.setVisibility(View.GONE);
                nameErrorTextView.setVisibility(View.GONE);
                dobErrorTextView.setVisibility(View.GONE);
                panImageHeading.setTextColor(ContextCompat.getColor(getContext(), R.color.radical_red));
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

            TextView nameErrorTextView = (TextView) getView().findViewById(R.id.add_kyc_details_name_error_textview);
            TextView panCardErrorTextView = (TextView) getView().findViewById(R.id.add_kyc_details_pan_number_error_textview);
            TextView dobErrorTextView = (TextView) getView().findViewById(R.id.add_kyc_details_dob_error_textview);
            TextView uploadPanErrorTextView = (TextView) getView().findViewById(R.id.add_kyc_details_upload_pan_error_textview);
            uploadPanErrorTextView.setVisibility(View.GONE);
            panCardErrorTextView.setVisibility(View.GONE);
            nameErrorTextView.setVisibility(View.GONE);
            dobErrorTextView.setVisibility(View.GONE);

            switch (v.getId()) {
                case R.id.add_kyc_details_name_edittext:
                    TextView textView = (TextView) getView().findViewById(R.id.add_kyc_details_name_textview);
                    EditText userNameEditText = (EditText) getView().findViewById(R.id.add_kyc_details_name_edittext);
                    if (hasFocus) {
                        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.blue_008ae1));
                        userNameEditText.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.blue_008ae1), PorterDuff.Mode.SRC_IN);
                    } else {
                        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white_999999));
                        userNameEditText.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.white_999999), PorterDuff.Mode.SRC_IN);
                    }
                    break;

                case R.id.add_kyc_details_pan_number_edittext:
                    textView = (TextView) getView().findViewById(R.id.add_kyc_details_pan_number_textview);
                    EditText panNumberEditText = (EditText) getView().findViewById(R.id.add_kyc_details_pan_number_edittext);
                    if (hasFocus) {
                        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.blue_008ae1));
                        panNumberEditText.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.blue_008ae1), PorterDuff.Mode.SRC_IN);
                    } else {
                        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white_999999));
                        panNumberEditText.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.white_999999), PorterDuff.Mode.SRC_IN);
                    }
                    break;

                case R.id.add_kyc_details_dob_edittext:
                    textView = (TextView) getView().findViewById(R.id.add_kyc_details_dob_textview);
                    EditText dobEditText = (EditText) getView().findViewById(R.id.add_kyc_details_dob_edittext);
                    if (hasFocus) {
                        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.blue_008ae1));
                        dobEditText.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.blue_008ae1), PorterDuff.Mode.SRC_IN);
                    } else {
                        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white_999999));
                        dobEditText.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.white_999999), PorterDuff.Mode.SRC_IN);
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