package in.sportscafe.nostragamus.module.user.group.allgroups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.user.group.joingroup.JoinGroupActivity;

/**
 * Created by Deepanshi on 23/8/16.
 */
public class AllGroupsActivity extends NostragamusActivity implements AllGroupsView {

    private RecyclerView mRvAllGroups;

    private AllGroupsPresenter mAllGroupsPresenter;

    private Toolbar mtoolbar;

    private TextView mTvEmptyGroups;

    private static final int CODE_GROUP_INFO = 11;

    private static final int GROUPS_CODE = 20;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_groups);

        this.mRvAllGroups = (RecyclerView) findViewById(R.id.all_groups_rcv);
        this.mRvAllGroups.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        this.mRvAllGroups.setHasFixedSize(true);
        this.mAllGroupsPresenter = AllGroupsPresenterImpl.newInstance(this);
        this.mAllGroupsPresenter.onCreateAllGroups(savedInstanceState);
        initToolBar();
    }

    @Override
    public void goBackWithSuccessResult() {
        setResult(RESULT_OK);
        onBackPressed();
    }

    @Override
    public void navigateToHomeActivity() {
        Intent homeintent = new Intent(this, HomeActivity.class);
        homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        homeintent.putExtra("group", "openprofile");
        startActivity(homeintent);
        finish();
    }


    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        this.mRvAllGroups.setAdapter(adapter);
    }

    @Override
    public void showGroupsEmpty() {
        mTvEmptyGroups=(TextView)findViewById(R.id.all_groups_empty_tv);
        mTvEmptyGroups.setVisibility(View.VISIBLE);

    }

    @Override
    public void navigateToJoinGroup() {
        startActivity(new Intent(getContext(), JoinGroupActivity.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Activity.RESULT_OK == resultCode) {
            if(CODE_GROUP_INFO == requestCode) {
                mAllGroupsPresenter.onCreateAllGroups(getArguments());
            }
        }
    }


    public void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.all_groups_toolbar);
        mtoolbar.setTitle("All Groups");
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigateToHomeActivity();
                    }
                }

        );
    }

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.GROUPS_ALL;
    }
}