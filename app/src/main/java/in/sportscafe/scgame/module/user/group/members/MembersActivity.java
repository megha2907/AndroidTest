package in.sportscafe.scgame.module.user.group.members;

import android.os.Bundle;
import android.view.View;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.ScGameActivity;

/**
 * Created by Jeeva on 2/7/16.
 */
public class MembersActivity extends ScGameActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_members);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.admin_members_fl_members_holder,
                        MembersFragment.newInstance(getIntent()
                                .getLongExtra(Constants.BundleKeys.GROUP_ID, -1)))
                .commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.admin_members_btn_back:
                onBackPressed();
                break;
        }
    }
}