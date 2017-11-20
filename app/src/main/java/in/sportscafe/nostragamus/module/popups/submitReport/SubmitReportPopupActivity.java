package in.sportscafe.nostragamus.module.popups.submitReport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jeeva.android.BaseActivity;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;

/**
 * Created by deepanshi on 11/18/17.
 */

public class SubmitReportPopupActivity extends BaseActivity implements View.OnClickListener {

    TextView tvReportTitle;
    TextView tvReportDesc;
    TextView tvReportText;
    TextView tvReportHeading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_report_popup);
        initView();
        setInfo();
    }

    private void initView() {

        tvReportHeading = (TextView)findViewById(R.id.popup_report_tv_heading);
        tvReportTitle = (TextView)findViewById(R.id.popup_report_title);
        tvReportDesc = (TextView)findViewById(R.id.popup_report_desc);
        tvReportText = (TextView)findViewById(R.id.popup_report_text);

        (findViewById(R.id.popup_report_submit_btn)).setOnClickListener(this);
        (findViewById(R.id.popup_report_cross_btn)).setOnClickListener(this);
        (findViewById(R.id.popup_bg)).setOnClickListener(this);
    }

    private void setInfo() {
        Bundle args = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            args = getIntent().getExtras();

            String reportTitle = args.getString(Constants.BundleKeys.REPORT_TITLE);
            String reportDesc = args.getString(Constants.BundleKeys.REPORT_DESC);

            tvReportHeading.setText("Report Answers");
            tvReportTitle.setText(reportTitle);
            tvReportDesc.setText(reportDesc);
            tvReportText.setText("Thanks for helping us get better! In case of any issue, we will review the match result and update it.");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_bg:
                onBackPressed();
                break;
            case R.id.popup_report_cross_btn:
                onBackPressed();
                break;
            case R.id.popup_report_submit_btn:
                onBackPressed();
                break;
        }
    }
}
