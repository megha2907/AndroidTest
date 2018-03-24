package in.sportscafe.nostragamus.module.privateContest.ui.advancePrize;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.popups.PopUpDialogActivity;

public class PrivateContestAdvancePrizeStructurePopupActivity extends PopUpDialogActivity implements View.OnClickListener {

    private static final String TAG = PrivateContestAdvancePrizeStructurePopupActivity.class.getSimpleName();

    private RecyclerView mPrizeReycler;
    private TextView mErrorTitleTextView;
    private TextView mErrorMsgTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_contest_advance_prize_structure);
        initViews();
    }

    private void initViews() {
        mPrizeReycler = (RecyclerView) findViewById(R.id.prize_recyclerView);
        mErrorTitleTextView = (TextView) findViewById(R.id.private_contest_prize_err_heading_textView);
        mErrorMsgTextView =(TextView) findViewById(R.id.private_contest_prize_err_msg_textView);

        findViewById(R.id.private_contest_prizes_popup_ok_btn).setOnClickListener(this);
        findViewById(R.id.popup_close_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.private_contest_prizes_popup_ok_btn:
            case R.id.popup_close_btn:
                onOkClicked();
                break;
        }
    }

    private void onOkClicked() {
        finish();
    }
}
