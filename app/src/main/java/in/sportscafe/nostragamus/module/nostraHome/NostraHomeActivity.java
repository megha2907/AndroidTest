package in.sportscafe.nostragamus.module.nostraHome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.newChallenges.ui.NewChallengesActivity;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.module.inPlay.ui.InPlayActivity;
import in.sportscafe.nostragamus.module.navigation.ProfileActivity;

public abstract class NostraHomeActivity extends NostraBaseActivity implements View.OnClickListener {

    private LinearLayout mNewChallengesBottomButton;
    private LinearLayout mJoinedBottomButton;
    private LinearLayout mGroupBottomButton;
    private LinearLayout mProfileBottomButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nostra_home);
        initViews();
    }

    /**
     * Use this method for all activities where bottom navigation bar is visible to set it;s contentView
     * NOTE : Replace setContentView(layout) method with this method in your activity
     * @param activityLayout
     */
    protected void setContentLayout(int activityLayout) {
        getLayoutInflater().inflate(activityLayout, (FrameLayout)findViewById(R.id.activity_container));
    }

    private void initViews() {
        mNewChallengesBottomButton = (LinearLayout) findViewById(R.id.home_challenges_tab_layout);
        mJoinedBottomButton = (LinearLayout) findViewById(R.id.home_join_tab_layout);
        mGroupBottomButton = (LinearLayout) findViewById(R.id.home_group_tab_layout);
        mProfileBottomButton = (LinearLayout) findViewById(R.id.home_profile_tab_layout);

        mNewChallengesBottomButton.setOnClickListener(this);
        mJoinedBottomButton.setOnClickListener(this);
        mGroupBottomButton.setOnClickListener(this);
        mProfileBottomButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_challenges_tab_layout:
                onChallengesClicked();
                break;

            case R.id.home_join_tab_layout:
                onJoinClicked();
                break;

            case R.id.home_group_tab_layout:
                onGroupClicked();
                break;

            case R.id.home_profile_tab_layout:
                onProfileClicked();
                break;
        }
    }

    protected void setNewChallengesSelected() {
        mNewChallengesBottomButton.setSelected(true);
        mJoinedBottomButton.setSelected(false);
        mGroupBottomButton.setSelected(false);
        mProfileBottomButton.setSelected(false);
    }

    protected void setJoinSelected() {
        mJoinedBottomButton.setSelected(true);
        mNewChallengesBottomButton.setSelected(false);
        mGroupBottomButton.setSelected(false);
        mProfileBottomButton.setSelected(false);
    }

    protected void setGroupSelected() {
        mGroupBottomButton.setSelected(true);
        mNewChallengesBottomButton.setSelected(false);
        mJoinedBottomButton.setSelected(false);
        mProfileBottomButton.setSelected(false);
    }

    protected void setProfileSelected() {
        mProfileBottomButton.setSelected(true);
        mNewChallengesBottomButton.setSelected(false);
        mJoinedBottomButton.setSelected(false);
        mGroupBottomButton.setSelected(false);
    }



    private void onProfileClicked() {
        Intent intent = new Intent(this.getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }

    private void onGroupClicked() {
       /* Intent intent = new Intent(this.getApplicationContext(), GroupActivity.class);
        startActivity(intent);*/
    }

    private void onJoinClicked() {
        Intent intent = new Intent(this.getApplicationContext(), InPlayActivity.class);
        startActivity(intent);
    }

    private void onChallengesClicked() {
        Intent intent = new Intent(this.getApplicationContext(), NewChallengesActivity.class);
        startActivity(intent);
    }
}
