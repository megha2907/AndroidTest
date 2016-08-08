package in.sportscafe.scgame.module.user.group.groupinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.common.SpacesItemDecoration;
import in.sportscafe.scgame.module.home.HomeActivity;
import in.sportscafe.scgame.module.user.group.admin.adminmembers.AdminMembersActivity;
import in.sportscafe.scgame.module.user.group.members.MembersActivity;
import in.sportscafe.scgame.module.user.group.newgroup.GrpSportSelectionAdapter;

/**
 * Created by Jeeva on 12/6/16.
 */
public class GroupInfoActivity extends ScGameActivity implements GroupInfoView,
        View.OnClickListener {

    private static final int CODE_ADMIN_MEMBERS = 3;

    private EditText mEtGroupName;

    private ImageButton mIBtnEditName;

    private TextView mTvMembersCount;

    private RecyclerView mRvSportSelection;

    private GroupInfoPresenter mGroupInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        findViewById(R.id.group_info_ll_share).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mGroupInfoPresenter.onLongClickShareCode();
                showMessage("Group code is copied to the clipboard");
                return true;
            }
        });

        mEtGroupName = (EditText) findViewById(R.id.group_info_et_name);
        mEtGroupName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    mGroupInfoPresenter.onDoneGroupName(getTrimmedText(mEtGroupName));
                }
                return true;
            }
        });

        mIBtnEditName = (ImageButton) findViewById(R.id.group_info_btn_edit_name);

        mTvMembersCount = (TextView) findViewById(R.id.group_info_tv_members_count);

        this.mRvSportSelection = (RecyclerView) findViewById(R.id.group_info_rcv);
        this.mRvSportSelection.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_10)));
        this.mRvSportSelection.setLayoutManager(new GridLayoutManager(this, 3));
        this.mRvSportSelection.setHasFixedSize(true);

        this.mGroupInfoPresenter = GroupInfoPresenterImpl.newInstance(this);
        this.mGroupInfoPresenter.onCreateGroupInfo(getIntent().getExtras());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.group_info_btn_back:
                onBackPressed();
                break;
            /*case R.id.group_info_btn_delete_group:
                mGroupInfoPresenter.onClickDeleteGroup();
                break;*/
            case R.id.group_info_btn_leave_group:
                mGroupInfoPresenter.onClickLeaveGroup();
                break;
            case R.id.group_info_ll_members:
                mGroupInfoPresenter.onClickMembers();
                break;
            case R.id.group_info_ll_share:
                mGroupInfoPresenter.onClickShareCode();
                break;
            case R.id.group_info_btn_edit_name:
                mEtGroupName.setEnabled(true);
                mIBtnEditName.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void setGroupName(String groupName) {
        mEtGroupName.setText(groupName);
    }

    @Override
    public void setMembersSize(int size) {
        mTvMembersCount.setText(String.valueOf(size));
    }

    @Override
    public void setAdapter(GrpSportSelectionAdapter adapter) {
        this.mRvSportSelection.setAdapter(adapter);
    }

    @Override
    public void setGroupCode(String groupCode) {
        TextView textView = (TextView) findViewById(R.id.group_info_tv_share_code);
        textView.setText(String.format("To add members, share the code %s with them", groupCode));
    }

    @Override
    public void showDeleteGroup() {
//        findViewById(R.id.group_info_btn_delete_group).setVisibility(View.VISIBLE);
    }

    @Override
    public void disableEdit() {
        mEtGroupName.setEnabled(false);
        mIBtnEditName.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigateToAdminMembers(Bundle bundle) {
        Intent intent = new Intent(this, AdminMembersActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, CODE_ADMIN_MEMBERS);
    }

    @Override
    public void navigateToGroupMembers(Bundle bundle) {
        Intent intent = new Intent(this, MembersActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(RESULT_OK == resultCode && CODE_ADMIN_MEMBERS == requestCode) {
            mGroupInfoPresenter.onGetMemberResult();
        }
    }

    @Override
    public void setSuccessResult() {
        setResult(RESULT_OK);
    }
}