package in.sportscafe.nostragamus.module.feedback;

import android.os.Bundle;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.popups.GetScreenNameListener;
import in.sportscafe.nostragamus.module.webview.GoogleFormFragment;

/**
 * Created by Jeeva on 25/11/16.
 */

public class GoogleFormActivity extends NostragamusActivity implements GoogleFormFragment.OnGoogleFormListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_form);

        getSupportFragmentManager().beginTransaction().replace(R.id.google_form_fl_holder,
                GoogleFormFragment.newInstance(getIntent().getStringExtra(BundleKeys.FEEDBACK_FORM_URL))).commit();
    }

    @Override
    public void onBackPressed() {
//        super.onBackClicked();
    }

    @Override
    public void onFormSubmitted() {
        NostragamusDataHandler.getInstance().setInitialFeedbackFormShown(true);
        finish();
    }

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.GOOGLE_FORM;
    }
}