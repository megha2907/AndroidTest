package in.sportscafe.nostragamus.module.prediction.editAnswer;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class EditAnswerActivity extends NostraBaseActivity implements EditAnswerFragmentListener {

    private static final String TAG = EditAnswerActivity.class.getSimpleName();

    @Override
    public String getScreenName() {
        return Constants.Notifications.SCREEN_EDIT_ANSWER;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_asnwer);
        setImmersiveFullScreenMode();

        loadFragment();
    }

    private void loadFragment() {
        EditAnswerFragment editAnswerFragment = new EditAnswerFragment();
        if (getIntent() != null && getIntent().getExtras() != null) {
            editAnswerFragment.setArguments(getIntent().getExtras());
        }

        FragmentHelper.replaceFragment(this, R.id.fragment_container, editAnswerFragment);
    }

    @Override
    public void onBackClicked() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onAnswerEditSuccessful() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        /* check that powerup is not edited, if editted; then show dialog */

        Fragment editAnswerFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (editAnswerFragment != null && editAnswerFragment instanceof  EditAnswerFragment) {
            ((EditAnswerFragment) editAnswerFragment).onBackPressed();
        }

    }
}
