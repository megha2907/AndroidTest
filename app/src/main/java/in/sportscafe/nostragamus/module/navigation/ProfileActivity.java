package in.sportscafe.nostragamus.module.navigation;

import android.content.Intent;
import android.os.Bundle;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.nostraHome.NostraHomeActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class ProfileActivity extends NostraHomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setProfileSelected();
        setContentLayout(R.layout.activity_profile);
        loadFragment();

    }

    private void loadFragment() {
        NavigationFragment navigationFragment = new NavigationFragment();
        if (getIntent() != null) {
            navigationFragment.setArguments(getIntent().getExtras());
        }

        FragmentHelper.replaceFragment(this, R.id.fragment_container, navigationFragment);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        // Need to pass intent extra args to fragments again as launched activity again
    }
}
