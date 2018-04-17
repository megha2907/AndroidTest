package in.sportscafe.nostragamus.module.privateContest.ui.createContest;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;
import com.jeeva.android.widgets.CustomProgressbar;
import com.jeeva.android.widgets.customfont.CustomEditText;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.contest.contestDetailsBeforeJoining.CompletePaymentDialogFragment;
import in.sportscafe.nostragamus.module.contest.dto.ContestScreenData;
import in.sportscafe.nostragamus.module.contest.dto.JoinContestData;
import in.sportscafe.nostragamus.module.customViews.CustomSnackBar;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.lowBalance.AddMoneyOnLowBalanceActivity;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;
import in.sportscafe.nostragamus.module.nostraHome.helper.TimerHelper;
import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivity;
import in.sportscafe.nostragamus.module.popups.timerPopup.TimerFinishDialogHelper;
import in.sportscafe.nostragamus.module.privateContest.adapter.PrivateContestPrizeListRecyclerAdapter;
import in.sportscafe.nostragamus.module.privateContest.adapter.PrivateContestPrizeTemplateSpinnerAdapter;
import in.sportscafe.nostragamus.module.privateContest.dataProvider.CreatePrivateContestApiMoelImpl;
import in.sportscafe.nostragamus.module.privateContest.dataProvider.PrivateContestPrizeTemplatesApiModelImpl;
import in.sportscafe.nostragamus.module.privateContest.helper.CreateAndJoinPrivateContestHelper;
import in.sportscafe.nostragamus.module.privateContest.helper.PrivateContestPrizeEstimationHelper;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.advancePrize.PrivateContestAdvancePrizeStructurePopupActivity;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto.CreatePrivateContestResponse;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto.CreatePrivateContestScreenData;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto.PrivateContestAdvancePrizeScreenData;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto.PrivateContestPrizeSpinnerItemType;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto.PrivateContestPrizeTemplateResponse;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto.PrizeListItemDto;
import in.sportscafe.nostragamus.utils.CodeSnippet;

import static in.sportscafe.nostragamus.module.privateContest.helper.PrivateContestPrizeEstimationHelper.getDistributableTotalPrize;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatePrivateContestFragment extends BaseFragment implements
        View.OnClickListener, View.OnFocusChangeListener {

    private static final String TAG = CreatePrivateContestFragment.class.getSimpleName();
    private static final int ADD_MONEY_ON_LOW_BALANCE_REQUEST_CODE = 411;
    private static final int ADVANCE_PRIZE_STRUCTURE_REQUEST_CODE = 412;

    private CreatePrivateContestFragmentListener mFragmentListener;
    private CreatePrivateContestScreenData mScreenData;
    private PrivateContestPrizeListRecyclerAdapter mPrizeListAdapter;
    private PrivateContestPrizeTemplateSpinnerAdapter mSpinnerAdapter;
    private PrivateContestPrizeTemplateResponse mSelectedPrizeTemplate;

    private Spinner mPrizeStructureSpinner;
    private Button mCreatePrivateContestButton;
    private EditText mContestNameEditText;
    private EditText mEntriesEditText;
    private CustomEditText mEntryFeeEditText;
    private TextView mEntriesErrorTextView;
    private TextView mFeeErrorTextView;
    private TextView mPrizesErrorTextView;
    private TextView mSubHeaderTimerTextView;
    private TextView mMessageTextView;
    private RecyclerView mEstimatedPrizeRecyclerView;
    private NestedScrollView mNestedScrollView;
    private RadioButton mTop1WinRadio;

    private TextWatcher mEntriesTextWatcher = getEntriesTextWatcher();
    private TextWatcher mEntryFeeTextWatcher = getEntryFeeTextWatcher();

    @NonNull
    private TextWatcher getEntriesTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validatePrizeStructureTemplate();

                if (!TextUtils.isEmpty(mEntriesEditText.getText().toString())) {
                    if (isEntriesValid()) {

                        calculatePrizeEstimation();

                        /* Remove error if was shown */
                        if (mEntriesErrorTextView.getVisibility() == View.VISIBLE) {
                            mEntriesErrorTextView.setVisibility(View.GONE);
                        }
                        if (getView() != null) {
                            TextView titleTextView = (TextView) getView().findViewById(R.id.create_contest_entries_heading_TextView);
                            View lineView = getView().findViewById(R.id.create_private_entries_line_view);
                            setActiveState(titleTextView, lineView, true);
                        }
                    } else {
                        handleEntriesValid(false, "");
                    }
                } else {
                    setEmptyEstimationList();  // Set empty list
                }
            }
        };
    }

    private void setEmptyEstimationList() {
        setEstimatePrizeAdapter(new ArrayList<PrizeListItemDto>());
        setTotalPrizeAmount(0);
    }

    private void validatePrizeStructureTemplate() {
        if (mSelectedPrizeTemplate != null &&
                mSelectedPrizeTemplate.getTemplateType() == PrivateContestPrizeSpinnerItemType.ADVANCE_TEMPLATE) {

            showSpinnerError(true, "Select different prize structure");
            // re-set ui
            setEmptyEstimationList();
        }
    }

    private void calculatePrizeEstimation() {
        if (!TextUtils.isEmpty(mEntriesEditText.getText().toString()) &&
                !TextUtils.isEmpty(mEntryFeeEditText.getText().toString())) {

            if (isEntryFeeValid()) {
                handleEntryFeeValid(true);

                if (isEntriesValid()) {
                    handleEntriesValid(true, "");

                    performEstimation(mSelectedPrizeTemplate, getEntryFee(), getEntries());

                } else {
                    handleEntriesValid(false, "");
                }
            } else {
                handleEntryFeeValid(false);
            }
        }
    }

    @NonNull
    private TextWatcher getEntryFeeTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validatePrizeStructureTemplate();

                if (!TextUtils.isEmpty(mEntryFeeEditText.getText().toString())) {
                    if (isEntryFeeValid()) {

                        calculatePrizeEstimation();

                        /* Remove error if was shown */
                        if (mFeeErrorTextView.getVisibility() == View.VISIBLE) {
                            mFeeErrorTextView.setVisibility(View.GONE);
                        }
                        if (getView() != null) {
                            TextView titleTextView = (TextView) getView().findViewById(R.id.create_contest_fee_heading_TextView);
                            View lineView = getView().findViewById(R.id.create_private_fee_line_view);
                            setActiveState(titleTextView, lineView, true);
                        }
                    } else {
                        handleEntryFeeValid(false);
                    }
                } else {
                    setEmptyEstimationList();  // Set empty list
                }
            }
        };
    }

    private void setSpinnerEnabled(boolean makeEnable) {
        try {
            LinearLayout itemParent = (LinearLayout) mPrizeStructureSpinner.getChildAt(0);
            TextView itemTextView = (TextView) itemParent.findViewById(R.id.spinner_item_textView);

            if (makeEnable) {
                itemTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            } else {
                itemTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.white_606060));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Make enable/disable
        mPrizeStructureSpinner.setEnabled(makeEnable);
    }

    public CreatePrivateContestFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreatePrivateContestFragmentListener) {
            mFragmentListener = (CreatePrivateContestFragmentListener) context;
        } else {
            throw new RuntimeException(TAG + "Should implement " +
                    CreatePrivateContestFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_private_contest, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mPrizeStructureSpinner = (Spinner) view.findViewById(R.id.create_contest_prize_spinner);
        mCreatePrivateContestButton = (Button) view.findViewById(R.id.create_join_private_contest_btn);
        mContestNameEditText = (EditText) view.findViewById(R.id.create_contest_name_editText);
        mEntriesEditText = (EditText) view.findViewById(R.id.create_contest_entries_editText);
        mEntryFeeEditText = (CustomEditText) view.findViewById(R.id.create_contest_fee_editText);
        mEntriesErrorTextView = (TextView) view.findViewById(R.id.create_contest_entries_heading_err_TextView);
        mPrizesErrorTextView = (TextView) view.findViewById(R.id.create_contest_prize_structure_heading_err_TextView);
        mFeeErrorTextView = (TextView) view.findViewById(R.id.create_contest_fee_heading_err_TextView);
        mSubHeaderTimerTextView = (TextView) view.findViewById(R.id.toolbar_heading_two);
        mMessageTextView = (TextView) view.findViewById(R.id.create_private_contest_msg_textView);
        mNestedScrollView = (NestedScrollView) view.findViewById(R.id.create_contest_nestedScrollView);
        mTop1WinRadio = (RadioButton) view.findViewById(R.id.pvt_contest_top_1_win_radio);

        mEstimatedPrizeRecyclerView = (RecyclerView) view.findViewById(R.id.prize_estimation_recyclerView);
        mEstimatedPrizeRecyclerView.setHasFixedSize(true);
        mEstimatedPrizeRecyclerView.setLayoutManager(
                new LinearLayoutManager(mEstimatedPrizeRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false));

        mCreatePrivateContestButton.setOnClickListener(this);
        view.findViewById(R.id.back_btn).setOnClickListener(this);
        view.findViewById(R.id.toolbar_wallet_linear_layout).setOnClickListener(this);
        view.findViewById(R.id.pvt_contest_name_layout).setOnClickListener(this);
        view.findViewById(R.id.pvt_contest_entries_layout).setOnClickListener(this);
        view.findViewById(R.id.pvt_contest_entry_fee_layout).setOnClickListener(this);
        view.findViewById(R.id.pvt_contest_spinner_layout).setOnClickListener(this);

        mContestNameEditText.setOnFocusChangeListener(this);
        mEntriesEditText.setOnFocusChangeListener(this);
        mEntryFeeEditText.setOnFocusChangeListener(this);

        mEntriesEditText.addTextChangedListener(mEntriesTextWatcher);
        mEntryFeeEditText.addTextChangedListener(mEntryFeeTextWatcher);

        setKeyboardDismissListener();
    }

    private void setKeyboardDismissListener() {
        try {
            mEntryFeeEditText.setOnEditTextImeBackListener(new CustomEditText.EditTextImeBackListener() {
                @Override
                public void onImeBack(CustomEditText ctrl, String text) {
                }

                @Override
                public void onImeActionDoneClicked() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPrizeStructureSpinner.performClick();
                        }
                    }, 500);
                }
            });
        } catch (Exception e) {
        }
    }

    @NonNull
    private AdapterView.OnItemSelectedListener getSpinnerItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedPrizeTemplate = (PrivateContestPrizeTemplateResponse) mPrizeStructureSpinner.getSelectedItem();
                mSpinnerAdapter.setSelectedTemplate(mSelectedPrizeTemplate);

                showSpinnerError(false, "");
                showMinEntryRadioIfRequired(mSelectedPrizeTemplate);

                if (mSelectedPrizeTemplate != null && !TextUtils.isEmpty(mSelectedPrizeTemplate.getTemplateId()) &&
                        !mSelectedPrizeTemplate.getTemplateId().equalsIgnoreCase(PrivateContestPrizeSpinnerItemType.DEFAULT_PRIZE_TEMPLATE_ID)) {

                    onPrizeTemplateSelected(mSelectedPrizeTemplate);

//                    scrollNestedView();
                } else {
                    setEmptyEstimationList(); // Make list empty
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
    }

    private void scrollNestedView() {
        if (mNestedScrollView != null && mEstimatedPrizeRecyclerView != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mNestedScrollView.smoothScrollTo(0, mEstimatedPrizeRecyclerView.getTop());
                }
            }, 500);
        }
    }

    private void onPrizeTemplateSelected(PrivateContestPrizeTemplateResponse prizeResponse) {
        if (prizeResponse.getTemplateType() == PrivateContestPrizeSpinnerItemType.ADVANCE_TEMPLATE) {

            handleAdvancePrizeEstimation();

        } else {
            if (isEntriesValid()) {
                handleEntriesValid(true, "");

                if (isEntryFeeValid()) {
                    handleEntryFeeValid(true);

                    performEstimation(prizeResponse, getEntryFee(), getEntries());

                } else {
                    handleEntryFeeValid(false);
                }
            } else {
                handleEntriesValid(false, "");
            }
        }
    }

    private void showMinEntryRadioIfRequired(PrivateContestPrizeTemplateResponse prizeResponse) {
        if (prizeResponse != null && getView() != null) {
            LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.pvt_contest_prize_spinner_radio_layout);

            if (prizeResponse.getMinEntryRequired() > 0) {
                TextView msgTextView = (TextView) getView().findViewById(R.id.pvt_contest_spinner_msg_textView);
                msgTextView.setText("What if there are less than " + prizeResponse.getMinEntryRequired() + " entries in the contest?");
                linearLayout.setVisibility(View.VISIBLE);
            } else {
                linearLayout.setVisibility(View.GONE);
            }
        }
    }

    private void handleAdvancePrizeEstimation() {
        if (isEntryFeeValid()) {

            PrivateContestAdvancePrizeScreenData advancePrizeScreenData = new PrivateContestAdvancePrizeScreenData();
            advancePrizeScreenData.setEntryFee(getEntryFee());
            advancePrizeScreenData.setEntries(getEntries());

            Bundle args = new Bundle();
            args.putParcelable(Constants.BundleKeys.PRIVATE_CONTEST_ADVANCE_PRIZE_STRUCTURE_SCREEN_DATA,
                    Parcels.wrap(advancePrizeScreenData));

            final Intent intent = new Intent(getContext(), PrivateContestAdvancePrizeStructurePopupActivity.class);
            intent.putExtras(args);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivityForResult(intent, ADVANCE_PRIZE_STRUCTURE_REQUEST_CODE);
                }
            }, 200);

        } else {
            handleError("Please provide entry fee", -1);
        }
    }

    private void performEstimation(PrivateContestPrizeTemplateResponse prizeResponseItem, double prizeFee, int entries) {
        if (prizeResponseItem != null) {

            double totalPrizeAmount = getDistributableTotalPrize(prizeFee, entries, prizeResponseItem.getMargin());
            setTotalPrizeAmount(totalPrizeAmount);

            List<PrizeListItemDto> prizeListItemDtoList = new PrivateContestPrizeEstimationHelper()
                    .getPrizeList(prizeResponseItem, totalPrizeAmount, entries, getPrizeEstimationListener());

            setEstimatePrizeAdapter(prizeListItemDtoList);
        }
    }

    private void setEstimatePrizeAdapter(List<PrizeListItemDto> prizeListItemDtoList) {
        if (prizeListItemDtoList != null) {
            mPrizeListAdapter = new PrivateContestPrizeListRecyclerAdapter(prizeListItemDtoList);
            mEstimatedPrizeRecyclerView.setAdapter(mPrizeListAdapter);

            if (prizeListItemDtoList.size() < 1) {
                setTotalPrizeAmount(0);
            }
        }
    }

    @NonNull
    private PrivateContestPrizeEstimationHelper.PrivateContestPrizeEstimationListener getPrizeEstimationListener() {
        return new PrivateContestPrizeEstimationHelper.PrivateContestPrizeEstimationListener() {
            @Override
            public void onError(String msg, int errorCode) {
                if (errorCode > 0) {
                    switch (errorCode) {
                        case PrivateContestPrizeEstimationHelper.PrivateContestPrizeEstimationListener.ERR_CODE_ENTRIES_LESS_THAN_WINNERS:
                            handleEntriesValid(false, msg);
                            break;
                    }
                } else {
                    handleError(msg, -1);
                }
            }

            @Override
            public void updateTotalAmount(double updatedTotalAmount) {
                setTotalPrizeAmount(updatedTotalAmount);
            }
        };
    }

    private void setTotalPrizeAmount(double totalPrizeAmount) {
        if (getView() != null) {
            TextView totalPrizeTextView = (TextView) getView().findViewById(R.id.private_contest_total_prize_amt_textView);
            totalPrizeTextView.setText(Constants.RUPEE_SYMBOL + CodeSnippet.getFormattedAmount(totalPrizeAmount));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                if (mFragmentListener != null) {
                    mFragmentListener.onBackClicked();
                }
                break;

            case R.id.toolbar_wallet_linear_layout:
                if (mFragmentListener != null) {
                    mFragmentListener.onWalletClicked();
                }
                break;

            case R.id.create_join_private_contest_btn:
                hideKeyBoard();
                onCreateAndJoinContestClicked();
                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.CREATE_PRIVATE_CONTEST, Constants.AnalyticsClickLabels.CREATE_CONTEST);
                break;

            case R.id.pvt_contest_name_layout:
                mContestNameEditText.requestFocus();
                showKeyBoard(mContestNameEditText);
                break;

            case R.id.pvt_contest_entries_layout:
                mEntriesEditText.requestFocus();
                showKeyBoard(mEntriesEditText);
                break;

            case R.id.pvt_contest_entry_fee_layout:
                mEntryFeeEditText.requestFocus();
                showKeyBoard(mEntriesEditText);
                break;

            case R.id.pvt_contest_spinner_layout:
                mPrizeStructureSpinner.performClick();
                break;
        }
    }

    private void hideKeyBoard() {
        if (getActivity() != null && getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                        .getApplicationWindowToken(), 0);
            }
        }
    }


    private void onCreateAndJoinContestClicked() {
        if (mScreenData != null && mScreenData.getContestScreenData() != null &&
                getActivity() != null && !getActivity().isFinishing()) {

            /* No error on scree visible */
            if (mEntriesErrorTextView.getVisibility() != View.VISIBLE &&
                    mFeeErrorTextView.getVisibility() != View.VISIBLE) {


                /* Validate input again before joining */
                if (isEntriesValid()) {
                    if (isEntryFeeValid()) {
                        if (!TextUtils.isEmpty(getSelectedPrizeTemplateId())) {
                            if (mPrizeListAdapter != null && mPrizeListAdapter.getItemCount() > 0) {

                                ContestScreenData contestScreenData = mScreenData.getContestScreenData();
                                int entries = getEntries();
                                double entryFee = getEntryFee();
                                String contestName = mContestNameEditText.getText().toString();
                                boolean shouldTop1Win = getPrizeStructureRadioChoice();

                                final JoinContestData joinPrivateContestData = new JoinContestData();
                                joinPrivateContestData.setPrivateContestTemplateId(getSelectedPrizeTemplateId());
                                joinPrivateContestData.setChallengeId(contestScreenData.getChallengeId());
                                joinPrivateContestData.setChallengeName(contestScreenData.getChallengeName());
                                joinPrivateContestData.setChallengeStartTime(contestScreenData.getChallengeStartTime());
                                joinPrivateContestData.setEntryFee(entryFee);
                                joinPrivateContestData.setContestName(contestName);
                                joinPrivateContestData.setPrivateContestEntries(entries);
                                joinPrivateContestData.setPrivateContestTop1Wins(shouldTop1Win);
                                joinPrivateContestData.setJoiContestDialogLaunchMode(CompletePaymentDialogFragment.DialogLaunchMode.JOINING_CHALLENGE_LAUNCH);

                                Bundle args = new Bundle();
                                args.putParcelable(Constants.BundleKeys.JOIN_CONTEST_DATA, Parcels.wrap(joinPrivateContestData));

                                performCreateAndJoin(args);

                            } else {
                                showSpinnerError(true, "Select proper prize structure");
                            }
                        } else {
                            showSpinnerError(true, "Select prize structure");
                        }
                    } else {
                        handleEntryFeeValid(false);
                    }
                } else {
                    handleEntriesValid(false, "");
                }
            }
        } else {
            handleError("", -1);
        }
    }

    private boolean getPrizeStructureRadioChoice() {
        boolean top1Win = true; // default chosen item

        if (mSelectedPrizeTemplate != null && mSelectedPrizeTemplate.getMinEntryRequired() > 0) {
            top1Win = mTop1WinRadio.isChecked();
        } else {
            top1Win = false;
        }

        return top1Win;
    }

    private void showSpinnerError(boolean isError, String text) {
        if (isError) {
            mPrizesErrorTextView.setText(text);
            mPrizesErrorTextView.setVisibility(View.VISIBLE);
        } else {
            mPrizesErrorTextView.setText("Select prize structure");
            mPrizesErrorTextView.setVisibility(View.GONE);
        }
    }

    private String getSelectedPrizeTemplateId() {
        String templateId = "";

        if (mSelectedPrizeTemplate != null &&
                !TextUtils.isEmpty(mSelectedPrizeTemplate.getTemplateId()) &&
                !mSelectedPrizeTemplate.getTemplateId().equalsIgnoreCase(PrivateContestPrizeSpinnerItemType.DEFAULT_PRIZE_TEMPLATE_ID)) {

            templateId = mSelectedPrizeTemplate.getTemplateId();
        }

        return templateId;
    }

    private void performCreateAndJoin(final Bundle args) {
        if (args != null && args.containsKey(Constants.BundleKeys.JOIN_CONTEST_DATA)) {

            final JoinContestData joinPrivateContestData =
                    Parcels.unwrap(args.getParcelable(Constants.BundleKeys.JOIN_CONTEST_DATA));

            if (joinPrivateContestData != null &&
                    TimerFinishDialogHelper.canJoinContest(joinPrivateContestData.getChallengeStartTime())) {

                CustomProgressbar.getProgressbar(getContext()).show();
                new CreateAndJoinPrivateContestHelper().createAndJoinPrivateContest(joinPrivateContestData,
                        (AppCompatActivity) getActivity(),
                        new CreateAndJoinPrivateContestHelper.JoinPrivateContestProcessListener() {
                            @Override
                            public void noInternet() {
                                CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                                handleError("", Constants.DataStatus.NO_INTERNET);
                            }

                            @Override
                            public void lowWalletBalance(JoinContestData joinContestData) {
                                CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                                launchLowBalActivity(args);
                            }

                            @Override
                            public void joinPrivateContestSuccess(int status, CreatePrivateContestResponse createPrivateContestResponse) {
                                CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                                onContestJoinedSuccessfulResponse(createPrivateContestResponse);

                                if (createPrivateContestResponse != null) {
                                    sendContestJoinedDataToAmplitude(createPrivateContestResponse, joinPrivateContestData);
                                }

                            }

                            @Override
                            public void onApiFailure() {
                                CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                                handleError("", Constants.DataStatus.FROM_SERVER_API_FAILED);
                            }

                            @Override
                            public void onServerReturnedError(String msg) {
                                CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                                if (TextUtils.isEmpty(msg)) {
                                    msg = Constants.Alerts.SOMETHING_WRONG;
                                }
                                handleError(msg, -1);
                            }

                            @Override
                            public void hideProgressBar() {
                                CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                            }

                            @Override
                            public void showProgressBar() {
                                CustomProgressbar.getProgressbar(getContext()).show();
                            }
                        });

            } else {
                TimerFinishDialogHelper.showCanNotJoinTimerOutDialog((AppCompatActivity) getActivity());
            }
        }
    }

    private void launchLowBalActivity(Bundle args) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            Intent intent = new Intent(getActivity(), AddMoneyOnLowBalanceActivity.class);
            intent.putExtras(args);
            startActivityForResult(intent, ADD_MONEY_ON_LOW_BALANCE_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

                case ADD_MONEY_ON_LOW_BALANCE_REQUEST_CODE:
                    if (data != null && data.getExtras() != null) {
                        performCreateAndJoin(data.getExtras());
                    }
                    break;

                case ADVANCE_PRIZE_STRUCTURE_REQUEST_CODE:
                    if (data != null && data.getExtras() != null) {
                        Bundle resultArgs = data.getExtras();

                        PrivateContestAdvancePrizeScreenData advancePrizeScreenData =
                                Parcels.unwrap(resultArgs.getParcelable(Constants.BundleKeys.PRIVATE_CONTEST_ADVANCE_PRIZE_STRUCTURE_SCREEN_DATA));
                        if (advancePrizeScreenData != null && advancePrizeScreenData.getPrizeListItemDtos() != null) {
                            setEstimatePrizeAdapter(advancePrizeScreenData.getPrizeListItemDtos());
                            setEntries(advancePrizeScreenData.getEntries());
                        }
                    }
                    break;
            }
        }
    }

    private void setEntries(int entries) {
        if (entries > 0) {
            try {
                mEntriesEditText.removeTextChangedListener(mEntriesTextWatcher);
                mEntriesEditText.setText(String.valueOf(entries));
                mEntriesEditText.addTextChangedListener(mEntriesTextWatcher);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void onContestJoinedSuccessfulResponse(CreatePrivateContestResponse createPrivateContestResponse) {

        if (createPrivateContestResponse != null && createPrivateContestResponse.getInfo() != null &&
                !TextUtils.isEmpty(createPrivateContestResponse.getInfo().getPrivateCode())) {

            Log.d(TAG, "Private Contest join successful");
            if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {

                /* Add joinContestData to make it scroll to joined contest in inplay */
                JoinContestData joinContestData = new JoinContestData();
                joinContestData.setContestId(createPrivateContestResponse.getConfigId());
                joinContestData.setShouldShowPrivateContestCreatedMsg(true);

                Bundle args = new Bundle();
                args.putParcelable(Constants.BundleKeys.JOIN_CONTEST_DATA, Parcels.wrap(joinContestData));

                Intent clearTaskIntent = new Intent(getContext().getApplicationContext(), NostraHomeActivity.class);
                clearTaskIntent.putExtra(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, NostraHomeActivity.LaunchedFrom.SHOW_IN_PLAY);
                clearTaskIntent.putExtras(args);
                clearTaskIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(clearTaskIntent);
            }
        }

    }

    private void sendContestJoinedDataToAmplitude(CreatePrivateContestResponse createPrivateContestResponse, JoinContestData joinPrivateContestData) {

        if (createPrivateContestResponse != null && joinPrivateContestData != null && createPrivateContestResponse.getInfo() != null) {
        /* Joining a contest = Revenue */
            NostragamusAnalytics.getInstance().trackRevenue(createPrivateContestResponse.getInfo().getFee(), createPrivateContestResponse.getConfigId(),
                    createPrivateContestResponse.getInfo().getConfigName(), "Private Contest - Create", String.valueOf(createPrivateContestResponse.getId()));

        /* Send Contest Joined Details to Analytics */
            NostragamusAnalytics.getInstance().trackContestJoined(createPrivateContestResponse.getInfo().getConfigName(),
                    "Private Contest - Create", createPrivateContestResponse.getInfo().getFee(), "Private Contest - Create", joinPrivateContestData.getChallengeName(), 0);

        }

    }

    private void handleError(String msg, int status) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing()) {
            if (!TextUtils.isEmpty(msg)) {
                CustomSnackBar.make(getView(), msg, CustomSnackBar.DURATION_LONG).show();

            } else {
                switch (status) {
                    case Constants.DataStatus.NO_INTERNET:
                        CustomSnackBar.make(getView(), Constants.Alerts.NO_INTERNET_CONNECTION, CustomSnackBar.DURATION_LONG).show();
                        break;

                    default:
                        CustomSnackBar.make(getView(), Constants.Alerts.SOMETHING_WRONG, CustomSnackBar.DURATION_LONG).show();
                        break;
                }
            }
        }
    }

    @NonNull
    private CreatePrivateContestApiMoelImpl.CreatePrivateContestApiListener
    getCreatePrivateContestApiListener() {
        return new CreatePrivateContestApiMoelImpl.CreatePrivateContestApiListener() {
            @Override
            public void onSuccessResponse(int status, CreatePrivateContestResponse response) {
                CustomProgressbar.getProgressbar(getContext()).dismissProgress();
            }

            @Override
            public void onError(int status) {
                CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                handleError("", status);
            }

            @Override
            public void onServerSentError(String errorMsg) {
                CustomProgressbar.getProgressbar(getContext()).dismissProgress();
                if (TextUtils.isEmpty(errorMsg)) {
                    errorMsg = Constants.Alerts.SOMETHING_WRONG;
                }
                handleError(errorMsg, -1);
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMembers();
        setHeaderValues();
        loadPrizeTemplatesFromServer();
    }

    private void setHeaderValues() {
        if (getView() != null) {
            /* Wallet amount */
            TextView walletAmtTextView = (TextView) getView().findViewById(R.id.toolbar_wallet_money);
            walletAmtTextView.setText(CodeSnippet.getFormattedAmount(WalletHelper.getTotalBalance()));

            /* challenge name */
            if (mScreenData != null && mScreenData.getContestScreenData() != null
                    && !TextUtils.isEmpty(mScreenData.getContestScreenData().getChallengeName())) {
                TextView challengeNameHeaderTextView = (TextView) getView().findViewById(R.id.toolbar_heading_one);
                challengeNameHeaderTextView.setText(mScreenData.getContestScreenData().getChallengeName());
            }

            /* challenge start timer */
            setTimer();
        }
    }

    private void setTimer() {
        if (mScreenData != null && mScreenData.getContestScreenData() != null
                && !TextUtils.isEmpty(mScreenData.getContestScreenData().getChallengeStartTime())) {

            long futureTime = TimerHelper.getCountDownFutureTime(mScreenData.getContestScreenData().getChallengeStartTime());

            CountDownTimer countDownTimer = new CountDownTimer(futureTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mSubHeaderTimerTextView.setText(TimerHelper.getTimerFormatFromMillis(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    onMatchStarted();
                }
            };
            countDownTimer.start();
        }
    }

    private void onMatchStarted() {
        if (mScreenData != null && mScreenData.getContestScreenData() != null
                && !TextUtils.isEmpty(mScreenData.getContestScreenData().getChallengeStartTime())) {

            boolean isMatchStarted = DateTimeHelper.isMatchStarted(mScreenData.getContestScreenData().getChallengeStartTime());
            if (isMatchStarted) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (CreatePrivateContestFragment.this.isVisible()) {
                            String msg = String.format(Constants.Alerts.CHALLENGE_STARTED_ALERT_FOR_TIMER,
                                    mScreenData.getContestScreenData().getChallengeName());

                            TimerFinishDialogHelper.showChallengeStartedTimerOutDialog(getChildFragmentManager(),
                                    msg, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mFragmentListener != null) {
                                                mFragmentListener.onBackClicked();
                                            }
                                        }
                                    });
                        }
                    }
                }, 500);
            }
        }
    }

    private void loadPrizeTemplatesFromServer() {
        showLoadingProgressBar();

        new PrivateContestPrizeTemplatesApiModelImpl().fetchPrizeTemplates(
                new PrivateContestPrizeTemplatesApiModelImpl.PrivateContestDetailApiListener() {
                    @Override
                    public void onSuccessResponse(int status, List<PrivateContestPrizeTemplateResponse> responseList) {
                        hideLoadingProgressBar();
                        onPrizeStructureReceived(responseList);
                    }

                    @Override
                    public void onError(int status) {
                        hideLoadingProgressBar();
                        handleError("", status);
                    }
                });
    }

    private void onPrizeStructureReceived(List<PrivateContestPrizeTemplateResponse> responseList) {
        if (getView() != null && getActivity() != null && !getActivity().isFinishing() &&
                responseList != null && responseList.size() > 0) {

            /* Add Always first Selected item */
            PrivateContestPrizeTemplateResponse defaultTemplate = new PrivateContestPrizeTemplateResponse();
            defaultTemplate.setTemplateType(PrivateContestPrizeSpinnerItemType.DEFAULT_PRIZE_TEMPLATE);
            defaultTemplate.setName(PrivateContestPrizeSpinnerItemType.DEFAULT_PRIZE_TEMPLATE_ID);  // name same as id
            defaultTemplate.setTemplateId(PrivateContestPrizeSpinnerItemType.DEFAULT_PRIZE_TEMPLATE_ID);
            responseList.add(0, defaultTemplate);

            /* Select top template as default */
            mSelectedPrizeTemplate = responseList.get(0);

            /* Add Advance Template */
            /*PrivateContestPrizeTemplateResponse advanceTemplate = new PrivateContestPrizeTemplateResponse();
            advanceTemplate.setTemplateType(PrivateContestPrizeSpinnerItemType.ADVANCE_TEMPLATE);
            advanceTemplate.setName("Advance");
            responseList.add(advanceTemplate);*/

            mSpinnerAdapter = new PrivateContestPrizeTemplateSpinnerAdapter(
                    getContext(), responseList, mSelectedPrizeTemplate);
            mSpinnerAdapter.setDropDownViewResource(R.layout.pvt_contest_spinner_textview);

            mPrizeStructureSpinner.setAdapter(mSpinnerAdapter);
            mPrizeStructureSpinner.setSelected(false);
            mPrizeStructureSpinner.setSelection(0, true);  // default item selected
            mPrizeStructureSpinner.setOnItemSelectedListener(getSpinnerItemSelectedListener());
        }
    }

    private void initMembers() {
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(Constants.BundleKeys.PRIVATE_CONTEST_SCREEN_DATA)) {
                mScreenData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.PRIVATE_CONTEST_SCREEN_DATA));
            }
        }
    }

    private void setActiveState(TextView titleTextView, View editTextUnderlineView, boolean shouldMakeActive) {
        int colorState = (shouldMakeActive) ? R.color.blue_008ae1 : R.color.white_999999;

        if (titleTextView != null) {
            titleTextView.setTextColor(ContextCompat.getColor(titleTextView.getContext(), colorState));
        }
        if (editTextUnderlineView != null) {
            editTextUnderlineView.setBackgroundColor(ContextCompat.getColor(editTextUnderlineView.getContext(), colorState));
        }
    }

    private void setErrorState(TextView titleTextView, View editTextUnderlineView, boolean isError) {
        int colorState = (isError) ? R.color.radical_red : R.color.white_999999;

        if (titleTextView != null) {
            titleTextView.setTextColor(ContextCompat.getColor(titleTextView.getContext(), colorState));
        }
        if (editTextUnderlineView != null) {
            editTextUnderlineView.setBackgroundColor(ContextCompat.getColor(editTextUnderlineView.getContext(), colorState));
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (getView() != null) {
            switch (view.getId()) {
                case R.id.create_contest_name_editText:
                    TextView titleTextView = (TextView) getView().findViewById(R.id.create_contest_name_heading_TextView);
                    View lineView = getView().findViewById(R.id.create_private_name_line_view);
                    setActiveState(titleTextView, lineView, hasFocus);
                    break;

                case R.id.create_contest_entries_editText:
                    entriesFocusChanged(hasFocus);
                    break;

                case R.id.create_contest_fee_editText:
                    EntryFeeFocusChanged(hasFocus);
                    break;
            }
        }
    }

    private void EntryFeeFocusChanged(boolean hasFocus) {
        if (getView() != null) {
            TextView titleTextView = (TextView) getView().findViewById(R.id.create_contest_fee_heading_TextView);
            View lineView = getView().findViewById(R.id.create_private_fee_line_view);
            setActiveState(titleTextView, lineView, hasFocus);

            if (!hasFocus && !TextUtils.isEmpty(mEntryFeeEditText.getText().toString())) {
                if (isEntryFeeValid()) {
                    handleEntryFeeValid(true);

                    calculatePrizeEstimation();
                } else {
                    handleEntryFeeValid(false);
                }
            }
        }
    }

    private void entriesFocusChanged(boolean hasFocus) {
        if (getView() != null) {
            TextView titleTextView = (TextView) getView().findViewById(R.id.create_contest_entries_heading_TextView);
            View lineView = getView().findViewById(R.id.create_private_entries_line_view);
            setActiveState(titleTextView, lineView, hasFocus);

            if (!hasFocus && !TextUtils.isEmpty(mEntriesEditText.getText().toString())) {
                if (isEntriesValid()) {
                    handleEntriesValid(true, "");

                    calculatePrizeEstimation();
                } else {
                    handleEntriesValid(false, "");
                }
            }
        }
    }

    private boolean isEntriesValid() {
        boolean isValid = false;
        int entries = getEntries();

        if (entries >= Constants.PrivateContests.MIN_ENTRIES &&
                entries <= Constants.PrivateContests.MAX_ENTRIES) {
            isValid = true;
        }

        return isValid;
    }

    private void handleEntriesValid(boolean isValid, String msg) {
        if (getView() != null) {
            TextView titleTextView = (TextView) getView().findViewById(R.id.create_contest_entries_heading_TextView);
            View lineView = getView().findViewById(R.id.create_private_entries_line_view);

            if (isValid) {
                mEntriesErrorTextView.setVisibility(View.GONE);
                setActiveState(titleTextView, lineView, false);
            } else {
                String str = "Enter a number between 2 and 40";
                if (!TextUtils.isEmpty(msg)) {
                    str = msg;
                }

                setErrorState(titleTextView, lineView, true);
                mEntriesErrorTextView.setText(str);
                mEntriesErrorTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    private int getEntries() {
        int entries = 0;
        try {
            if (!TextUtils.isEmpty(mEntriesEditText.getText().toString())) {
                entries = Integer.valueOf(mEntriesEditText.getText().toString());
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return entries;
    }

    private boolean isEntryFeeValid() {
        boolean isValid = false;

        if (!TextUtils.isEmpty(mEntryFeeEditText.getText().toString())) {

            double fee = getEntryFee();
            if (fee >= Constants.PrivateContests.MIN_ENTRY_FEE && fee <= Constants.PrivateContests.MAX_ENTRY_FEE) {
                isValid = true;
            }
        }

        return isValid;
    }

    private void handleEntryFeeValid(boolean isValid) {
        if (getView() != null) {
            TextView titleTextView = (TextView) getView().findViewById(R.id.create_contest_fee_heading_TextView);
            View lineView = getView().findViewById(R.id.create_private_fee_line_view);

            if (isValid) {
                mFeeErrorTextView.setVisibility(View.GONE);
                setActiveState(titleTextView, lineView, false);

            } else {
                mFeeErrorTextView.setVisibility(View.VISIBLE);
                setErrorState(titleTextView, lineView, true);
            }
        }
    }

    private double getEntryFee() {
        double fee = 0;
        try {
            if (!TextUtils.isEmpty(mEntryFeeEditText.getText().toString())) {
                fee = Double.valueOf(mEntryFeeEditText.getText().toString());
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return fee;
    }

    private void showLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.pvt_contest_layouts).setVisibility(View.GONE);
            getView().findViewById(R.id.loading_layout).setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.loading_layout).setVisibility(View.GONE);
            getView().findViewById(R.id.pvt_contest_layouts).setVisibility(View.VISIBLE);
        }
    }
}
