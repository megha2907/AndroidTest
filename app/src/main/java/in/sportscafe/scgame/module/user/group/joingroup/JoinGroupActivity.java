package in.sportscafe.scgame.module.user.group.joingroup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.user.group.groupinfo.GroupInfoActivity;
import in.sportscafe.scgame.module.user.group.newgroup.NewGroupActivity;

/**
 * Created by Jeeva on 1/7/16.
 */
public class JoinGroupActivity extends ScGameActivity implements JoinGroupView,
        View.OnClickListener {

    private static final int CODE_NEW_GROUP = 1;

    private static final int CODE_GROUP_INFO = 2;

    private EditText mEtGroupCode;

    private JoinGroupPresenter mJoinGroupPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        this.mEtGroupCode = (EditText) findViewById(R.id.join_group_et_group_code);

        this.mJoinGroupPresenter = JoinGroupPresenterImpl.newInstance(this);
        this.mJoinGroupPresenter.onCreateJoinGroup();
    }

    @Override
    public void navigateToCreateGroup() {
        startActivityForResult(new Intent(this, NewGroupActivity.class), CODE_NEW_GROUP);
    }

    @Override
    public void navigateToGroupInfo(Bundle bundle) {
        Intent intent = new Intent(this, GroupInfoActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, CODE_GROUP_INFO);
    }

    @Override
    public void showJoinGroupSuccess() {
        findViewById(R.id.join_group_ll_join_done).setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(CODE_NEW_GROUP == requestCode && RESULT_OK == resultCode) {
            mJoinGroupPresenter.onNewGroupSuccess(data.getExtras());
        } else if(CODE_GROUP_INFO == requestCode) {
            setResult(RESULT_OK);
            onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.join_group_btn_join:
                mJoinGroupPresenter.onClickJoin(getTrimmedText(mEtGroupCode));
                break;
            case R.id.join_group_btn_new_group:
                mJoinGroupPresenter.onClickCreateGroup();
                break;
            case R.id.join_group_btn_back:
                onBackPressed();
                break;
        }
    }
}