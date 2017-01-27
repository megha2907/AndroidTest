package in.sportscafe.nostragamus.module.user.group.members;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;

/**
 * Created by Jeeva on 2/7/16.
 */
public class MembersActivity extends NostragamusActivity {

    private Toolbar mtoolbar;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_members);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.admin_members_fl_members_holder,
                        MembersFragment.newInstance(getIntent().getIntExtra(Constants.BundleKeys.GROUP_ID,0)), "Group Members")
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
}