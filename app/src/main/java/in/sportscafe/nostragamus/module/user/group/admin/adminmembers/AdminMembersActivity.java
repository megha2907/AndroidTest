package in.sportscafe.nostragamus.module.user.group.admin.adminmembers;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.popups.GetScreenNameListener;

import static in.sportscafe.nostragamus.Constants.BundleKeys;

/**
 * Created by Jeeva on 2/7/16.
 */
public class AdminMembersActivity extends NostragamusActivity {

    private Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_members);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.admin_members_fl_members_holder,
                AdminMembersFragment.newInstance(getIntent().getIntExtra(BundleKeys.GROUP_ID,0)))
                .commit();

        initToolBar();
    }

    public void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.admin_members_toolbar);
        mtoolbar.setTitle("Members");
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                }

        );
    }


    @Override
    public String getScreenName() {
        return Constants.ScreenNames.ADMIN_MEMBERS;
    }
}