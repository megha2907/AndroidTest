package in.sportscafe.scgame.module.user.group.admin.adminmembers;

import android.os.Bundle;
import android.view.View;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.ScGameActivity;

import static in.sportscafe.scgame.Constants.BundleKeys;

/**
 * Created by Jeeva on 2/7/16.
 */
public class AdminMembersActivity extends ScGameActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_members);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.admin_members_fl_members_holder,
                AdminMembersFragment.newInstance(getIntent().getLongExtra(BundleKeys.GROUP_ID, -1)))
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