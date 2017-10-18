package in.sportscafe.nostragamus.module.user.group;

import android.content.Intent;
import android.os.Bundle;

import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivity;

public class GroupActivity extends NostraHomeActivity {

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setGroupSelected();
        loadFragment();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

    }

    private void loadFragment() {
        /*AllGroupsFragment fragment = AllGroupsFragment.newInstance();
        FragmentHelper.replaceFragment(this, R.id.fragment_container, fragment);*/
    }
}
