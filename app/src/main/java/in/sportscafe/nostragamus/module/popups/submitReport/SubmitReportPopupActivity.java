package in.sportscafe.nostragamus.module.popups.submitReport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraFeedbackDataProvider;
import in.sportscafe.nostragamus.module.popups.PopUpDialogActivity;

/**
 * Created by deepanshi on 11/18/17.
 */

public class SubmitReportPopupActivity extends PopUpDialogActivity implements View.OnClickListener {

    TextView tvReportTitle;
    TextView tvReportDesc;
    TextView tvReportText;
    TextView tvReportHeading;
    private String reportType="";
    private String reportId="";

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

            reportId = args.getString(Constants.BundleKeys.REPORT_ID);
            reportType = args.getString(Constants.BundleKeys.REPORT_TYPE);
            String reportHeading = args.getString(Constants.BundleKeys.REPORT_HEADING);
            String reportTitle = args.getString(Constants.BundleKeys.REPORT_TITLE);
            String reportDesc = args.getString(Constants.BundleKeys.REPORT_DESC);
            String reportThankYouText = args.getString(Constants.BundleKeys.REPORT_THANKYOU_TEXT);

            tvReportHeading.setText(reportHeading);
            tvReportTitle.setText(reportTitle);
            tvReportDesc.setText(reportDesc);
            tvReportText.setText(reportThankYouText);
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
                callSubmitReportApi();
                break;
        }
    }

    private void callSubmitReportApi() {
            showLoadingProgressBar();
            NostraFeedbackDataProvider dataProvider = new NostraFeedbackDataProvider();
            dataProvider.sendReport(reportType,reportId, new NostraFeedbackDataProvider.NostraFeedbackDataProviderListener() {

                @Override
                public void onSuccessResponse(int status) {
                    hideLoadingProgressBar();
                    onBackPressed();
                }

                @Override
                public void onError(int status) {
                    hideLoadingProgressBar();
                    handleError(status);
                }
            });
    }

    private void handleError(int status) {

    }

    private void showLoadingProgressBar() {
        if (getActivity() != null) {
            findViewById(R.id.submitReportProgressBarLayout).setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingProgressBar() {
        if (getActivity() != null) {
            findViewById(R.id.submitReportProgressBarLayout).setVisibility(View.GONE);
        }
    }
}
