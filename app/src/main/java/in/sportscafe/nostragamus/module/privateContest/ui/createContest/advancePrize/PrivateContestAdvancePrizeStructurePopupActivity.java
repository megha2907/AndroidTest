package in.sportscafe.nostragamus.module.privateContest.ui.createContest.advancePrize;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.popups.PopUpDialogActivity;
import in.sportscafe.nostragamus.module.privateContest.adapter.PrivateContestPrizeEstimationRecyclerAdapter;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto.PrivateContestAdvancePrizeScreenData;
import in.sportscafe.nostragamus.module.privateContest.ui.createContest.dto.PrizeListItemDto;

public class PrivateContestAdvancePrizeStructurePopupActivity extends PopUpDialogActivity implements View.OnClickListener {

    private static final String TAG = PrivateContestAdvancePrizeStructurePopupActivity.class.getSimpleName();

    private PrivateContestAdvancePrizeScreenData mScreenData;
    private PrivateContestPrizeEstimationRecyclerAdapter mAdapter;

    private RecyclerView mPrizeRecyclerView;
    private EditText mNumberOfWinnersEditText;
    private TextView mErrorTitleTextView;
    private TextView mErrorMsgTextView;
    private TextView mEntriesErrorTextView;

    private double mTotalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_contest_advance_prize_structure);

        initViews();
        initMember();
        populateScreenData();
    }

    private void populateScreenData() {
        if (mScreenData != null) {
            if (mScreenData.getEntryFee() > 0 && mScreenData.getEntries() > 0) {
                mNumberOfWinnersEditText.setText(String.valueOf(mScreenData.getEntries()));
                int length = mNumberOfWinnersEditText.getText().length();
                mNumberOfWinnersEditText.setSelection(length, length);
            }

            calculateTotalAmount(mScreenData.getEntryFee());
        }
    }

    private void calculateTotalAmount(double entryFee) {
        int entries = getEntries();
        if (entryFee > 0 && entries > 0) {
            mTotalAmount = entries * entryFee;

            TextView totalAmount = (TextView) findViewById(R.id.private_contest_prize_total_amount_textView);
            totalAmount.setText(WalletHelper.getFormattedStringOfAmount(mTotalAmount));

            createAdvancePrizeListAdapter(entries);
        }
    }

    private void initMember() {
        if (getIntent() != null) {
            Bundle args = getIntent().getExtras();
            if (args != null && args.containsKey(Constants.BundleKeys.PRIVATE_CONTEST_ADVANCE_PRIZE_STRUCTURE_SCREEN_DATA)) {
                mScreenData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.PRIVATE_CONTEST_ADVANCE_PRIZE_STRUCTURE_SCREEN_DATA));
            }
        }
    }

    private void initViews() {
        mPrizeRecyclerView = (RecyclerView) findViewById(R.id.prize_recyclerView);
        mErrorTitleTextView = (TextView) findViewById(R.id.private_contest_prize_err_heading_textView);
        mErrorMsgTextView =(TextView) findViewById(R.id.private_contest_prize_err_msg_textView);
        mNumberOfWinnersEditText = (EditText) findViewById(R.id.create_contest_entries_editText);
        mEntriesErrorTextView = (TextView) findViewById(R.id.create_contest_entries_heading_err_TextView);

        findViewById(R.id.private_contest_prizes_popup_ok_btn).setOnClickListener(this);
        findViewById(R.id.popup_close_btn).setOnClickListener(this);

        mPrizeRecyclerView.setHasFixedSize(true);
        mPrizeRecyclerView.setLayoutManager(new LinearLayoutManager(mPrizeRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false));

        mNumberOfWinnersEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEntriesValid() && mScreenData != null && mScreenData.getEntryFee() > 0) {
                    createAdvancePrizeListAdapter(getEntries());
                }
            }
        });
    }

    private void createAdvancePrizeListAdapter(int entries) {
        mAdapter = new PrivateContestPrizeEstimationRecyclerAdapter(entries, mTotalAmount);
        mPrizeRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.private_contest_prizes_popup_ok_btn:
                onOkClicked();
                break;

            case R.id.popup_close_btn:
                onClockClicked();
                break;
        }
    }

    private void onClockClicked() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void onOkClicked() {

            if (mAdapter != null) {
                List<PrizeListItemDto> advancePrizeList = mAdapter.getAdvancePrizeStructureList();
                if (advancePrizeList != null) {
                    if (validateInput(advancePrizeList)) {

                    if (mScreenData == null) {
                        mScreenData = new PrivateContestAdvancePrizeScreenData();
                    }
                    mScreenData.setPrizeListItemDtos(advancePrizeList);
                        mScreenData.setEntries(getEntries());

                    Bundle resultArgs = new Bundle();
                    resultArgs.putParcelable(Constants.BundleKeys.PRIVATE_CONTEST_ADVANCE_PRIZE_STRUCTURE_SCREEN_DATA,
                            Parcels.wrap(mScreenData));

                    Intent resultIntent = new Intent();
                    resultIntent.putExtras(resultArgs);

                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        }
    }

    private boolean validateInput(List<PrizeListItemDto> advancePrizeList) {
        boolean isValid = true;

        if (advancePrizeList != null) {
            double percent = 0;
            for (PrizeListItemDto prizeListItemDto : advancePrizeList) {
                percent = percent + prizeListItemDto.getSharePercent();
            }
            if (percent != 100) {
                isValid = false;
                setError("Total " + percent + "% of 100%", "Total share percentage should match 100!");
            }

            /* Recalculate input : required  */
            for (PrizeListItemDto prizeListItemDto : advancePrizeList) {
                double prize = mTotalAmount * (prizeListItemDto.getSharePercent() / 100);
                prizeListItemDto.setAmount(prize);
            }

            /* Validate thar higher rank get higher prize */
            for (int temp = 0; temp < advancePrizeList.size() - 1; temp++) {
                PrizeListItemDto prize1 = advancePrizeList.get(temp);
                PrizeListItemDto prize2 = advancePrizeList.get(temp + 1);
                if (prize1.getSharePercent() < prize2.getSharePercent()) {
                    setError("", "Higher rank should have higher prize share than others");
                    isValid = false;
                }
            }
        }

        return isValid;
    }

    private void setError(String errorTitle, String errMsg) {
        if (!TextUtils.isEmpty(errorTitle)) {
            mErrorTitleTextView.setText(errorTitle);
            mErrorTitleTextView.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(errMsg)) {
            mErrorMsgTextView.setText(errMsg);
            mErrorMsgTextView.setVisibility(View.VISIBLE);
        }
    }

    private boolean isEntriesValid() {
        boolean isValid = false;
        int entries = getEntries();

        if (entries <= 0 || entries > Constants.PrivateContests.MAX_ENTRIES) {
            mEntriesErrorTextView.setVisibility(View.VISIBLE);
        } else {
            mEntriesErrorTextView.setVisibility(View.GONE);
            isValid = true;
        }
        return isValid;
    }

    private int getEntries() {
        int entries = 0;
        try {
            if (!TextUtils.isEmpty(mNumberOfWinnersEditText.getText().toString())) {
                entries = Integer.valueOf(mNumberOfWinnersEditText.getText().toString());
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return entries;
    }

}
