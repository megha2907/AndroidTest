package in.sportscafe.scgame.module.user.group.allgroups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.home.HomeActivity;
import in.sportscafe.scgame.module.user.group.joingroup.JoinGroupActivity;
import in.sportscafe.scgame.module.user.login.dto.UserInfo;
import in.sportscafe.scgame.module.user.myprofile.dto.UserInfoResponse;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Deepanshi on 23/8/16.
 */
public class AllGroupsActivity extends ScGameActivity implements AllGroupsView , View.OnClickListener{

    private RecyclerView mRvAllGroups;

    private AllGroupsPresenter mAllGroupsPresenter;

    private Toolbar mtoolbar;

    private Button mbtnEmptyGroups;

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
        this.mAllGroupsPresenter.onCreateAllGroups();
        initToolBar();

    }

    @Override
    public void navigateToJoinGroup() {
        startActivity(new Intent(this, JoinGroupActivity.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.all_groups_join_group:
                navigateToJoinGroup();
                break;
            case R.id.all_groups_empty_btn:
                navigateToJoinGroup();
                break;

        }
    }

    @Override
    public void goBackWithSuccessResult() {
        setResult(RESULT_OK);
        onBackPressed();
    }

    @Override
    public void navigateToHomeActivity() {
        Intent homeintent = new Intent(this, HomeActivity.class);
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
        mbtnEmptyGroups = (Button)findViewById(R.id.all_groups_empty_btn);
        mbtnEmptyGroups.setVisibility(View.VISIBLE);

        mTvEmptyGroups=(TextView)findViewById(R.id.all_groups_empty_tv);
        mTvEmptyGroups.setVisibility(View.VISIBLE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Activity.RESULT_OK == resultCode) {
            if(CODE_GROUP_INFO == requestCode) {
                mAllGroupsPresenter.onCreateAllGroups();
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
}