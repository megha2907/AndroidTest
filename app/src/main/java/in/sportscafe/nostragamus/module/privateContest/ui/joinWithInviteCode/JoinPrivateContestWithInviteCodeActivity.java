package in.sportscafe.nostragamus.module.privateContest.ui.joinWithInviteCode;

import android.os.Bundle;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class JoinPrivateContestWithInviteCodeActivity extends NostraBaseActivity
        implements JoinPrivateContestWithInviteCodeFragmentListener {

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_private_contest_with_invite_code);
        loadFragment();
    }

    private void loadFragment() {
        JoinPrivateContestWithInviteCodeFragment fragment = new JoinPrivateContestWithInviteCodeFragment();
        if (getIntent() != null && getIntent().getExtras() != null) {
            fragment.setArguments(getIntent().getExtras());
        }
        FragmentHelper.replaceFragment(this, R.id.fragment_container, fragment);
    }

    @Override
    public void onBackClicked() {

    }
}
