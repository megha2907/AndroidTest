package in.sportscafe.nostragamus.module.user.group.joingroup;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jeeva.android.ExceptionTracker;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.popups.GetScreenNameListener;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroupsActivity;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GroupInfoActivity;
import in.sportscafe.nostragamus.module.user.group.newgroup.NewGroupActivity;

/**
 * Created by Jeeva on 1/7/16.
 */
public class JoinGroupActivity extends NostragamusActivity implements JoinGroupView,
        View.OnClickListener{

    private static final String TAG = "JoinGroupActivity";

    private static final int CODE_NEW_GROUP = 1;
    private static final int CODE_GROUP_INFO = 2;

    private static final int CODE_ALL_GROUP = 3;

    private static final int GROUPS_CODE = 20;

    private EditText mEtGroupCode1;
    private EditText mEtGroupCode2;
    private EditText mEtGroupCode3;
    private EditText mEtGroupCode4;
    private EditText mEtGroupCode5;

    private JoinGroupPresenter mJoinGroupPresenter;

    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        initToolBar();

        initViews();

        this.mJoinGroupPresenter = JoinGroupPresenterImpl.newInstance(this);
        this.mJoinGroupPresenter.onCreateJoinGroup(getIntent().getExtras());
    }

    private void initViews() {
        this.mEtGroupCode1 = (EditText) findViewById(R.id.join_group_et_group_code_char_one);
        this.mEtGroupCode2 = (EditText) findViewById(R.id.join_group_et_group_code_char_two);
        this.mEtGroupCode3 = (EditText) findViewById(R.id.join_group_et_group_code_char_three);
        this.mEtGroupCode4 = (EditText) findViewById(R.id.join_group_et_group_code_char_four);
        this.mEtGroupCode5 = (EditText) findViewById(R.id.join_group_et_group_code_char_five);

        initListener();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void initListener() {
        mEtGroupCode1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //size as per your requirement
                if (s.toString().length() == 1) {
                    mEtGroupCode2.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void afterTextChanged(Editable s) {}
        });

        mEtGroupCode2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //size as per your requirement
                if (s.toString().length() == 1) {
                    mEtGroupCode3.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void afterTextChanged(Editable s) {}
        });

        mEtGroupCode3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //size as per your requirement
                if (s.toString().length() == 1) {
                    mEtGroupCode4.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void afterTextChanged(Editable s) {}
        });

        mEtGroupCode4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //size as per your requirement
                if (s.toString().length() == 1) {
                    mEtGroupCode5.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void navigateToCreateGroup() {
        startActivity(new Intent(this, NewGroupActivity.class));
        finish();
    }

    @Override
    public void navigateToGroupInfo(Bundle bundle) {
        Intent intent = new Intent(this, GroupInfoActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, GROUPS_CODE);
    }

    @Override
    public void showJoinGroupSuccess(Integer groupId) {
        navigateToGroupInfo(groupId);
    }

    @Override
    public void navigateToAllGroups() {
        startActivityForResult(new Intent(this, AllGroupsActivity.class), CODE_ALL_GROUP);
    }

    @Override
    public void navigateToGroupInfo(Integer groupId) {
        Intent intent = new Intent(this, GroupInfoActivity.class);
        Bundle mbundle = new Bundle();
        mbundle.putInt(Constants.BundleKeys.GROUP_ID,groupId);
        intent.putExtras(mbundle);
        startActivity(intent);
        finish();
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
                String groupCode = getTrimmedText(mEtGroupCode1) + getTrimmedText(mEtGroupCode2)
                        + getTrimmedText(mEtGroupCode3) + getTrimmedText(mEtGroupCode4)
                        + getTrimmedText(mEtGroupCode5);

                mJoinGroupPresenter.onClickJoin(groupCode);
                break;
            case R.id.join_group_btn_new_group:
                mJoinGroupPresenter.onClickCreateGroup();
                break;
        }
    }

    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.join_group_toolbar);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Add Group");
        mTitle.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato/Lato-Regular.ttf"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.back_icon_grey);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToHome();
                    }
                }
        );
    }

    @Override
    public void populateGroupCode(String groupCode) {
        try {
            String[] codeSplitter = groupCode.split("");
            mEtGroupCode1.setText(codeSplitter[1]);
            mEtGroupCode2.setText(codeSplitter[2]);
            mEtGroupCode3.setText(codeSplitter[3]);
            mEtGroupCode4.setText(codeSplitter[4]);
            mEtGroupCode5.setText(codeSplitter[5]);
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
    }

    @Override
    public void goBack() {
        super.onBackPressed();
    }

    @Override
    public void goToHome() {
        Intent homeintent = new Intent(this, HomeActivity.class);
        homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        homeintent.putExtra("group", "openprofile");
        startActivity(homeintent);
        finish();
    }

    @Override
    public void onBackPressed() {
        mJoinGroupPresenter.onBack();
    }

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.GROUPS_JOIN;
    }
}