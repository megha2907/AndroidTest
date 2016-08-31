package in.sportscafe.scgame.module.user.group.joingroup;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;

import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.ScGameDataHandler;
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

    private Toolbar mtoolbar;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        this.mEtGroupCode = (EditText) findViewById(R.id.join_group_et_group_code);

        this.mJoinGroupPresenter = JoinGroupPresenterImpl.newInstance(this);
        this.mJoinGroupPresenter.onCreateJoinGroup();
        initToolBar();
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
                PayloadBuilder builder = new PayloadBuilder();
                builder.putAttrString("GroupCode", getTrimmedText(mEtGroupCode))
                        .putAttrString("UserID", ScGameDataHandler.getInstance().getUserId());
                MoEHelper.getInstance(getContext()).trackEvent("JOIN GROUP-ONCLICK", builder.build());
                break;
            case R.id.join_group_btn_new_group:
                mJoinGroupPresenter.onClickCreateGroup();
                break;
//            case R.id.join_group_btn_back:
//                onBackPressed();
//                break;
        }
    }

    public void initToolBar() {
        Typeface tftitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato/Lato-Regular.ttf");
        mtoolbar = (Toolbar) findViewById(R.id.join_group_toolbar);
        mTitle = (TextView) mtoolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Add Group");
        mTitle.setTypeface(tftitle);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }

        );
    }
}