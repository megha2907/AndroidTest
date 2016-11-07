package in.sportscafe.scgame.module.user.group.joingroup;

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
import com.jeeva.android.Log;

import org.json.JSONException;
import org.json.JSONObject;

import in.sportscafe.scgame.Constants.BundleKeys;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.user.group.allgroups.AllGroups;
import in.sportscafe.scgame.module.user.group.allgroups.AllGroupsActivity;
import in.sportscafe.scgame.module.user.group.groupinfo.GroupInfoActivity;
import in.sportscafe.scgame.module.user.group.newgroup.NewGroupActivity;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;

/**
 * Created by Jeeva on 1/7/16.
 */
public class JoinGroupActivity extends ScGameActivity implements JoinGroupView,
        View.OnClickListener {

    private static final String TAG = "JoinGroupActivity";

    private static final int CODE_NEW_GROUP = 1;
    private static final int CODE_GROUP_INFO = 2;

    private static final int CODE_ALL_GROUP = 3;

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
        this.mJoinGroupPresenter.onCreateJoinGroup();
    }

    private void initViews() {
        this.mEtGroupCode1 = (EditText) findViewById(R.id.join_group_et_group_code_char_one);
        this.mEtGroupCode2 = (EditText) findViewById(R.id.join_group_et_group_code_char_two);
        this.mEtGroupCode3 = (EditText) findViewById(R.id.join_group_et_group_code_char_three);
        this.mEtGroupCode4 = (EditText) findViewById(R.id.join_group_et_group_code_char_four);
        this.mEtGroupCode5 = (EditText) findViewById(R.id.join_group_et_group_code_char_five);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Branch.getInstance().initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    if(referringParams.has(BundleKeys.GROUP_CODE)) {
                        try {
                            populateGroupCode(referringParams.getString(BundleKeys.GROUP_CODE));
                        } catch (JSONException e) {
                            ExceptionTracker.track(e);
                        }
                    }
                } else {
                    Log.i(TAG, error.getMessage());
                }

                initListener();
            }
        }, getIntent().getData(), this);
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
        Toast.makeText(getActivity(), "Your request to join the group has been sent to the group admin for approval.",
                Toast.LENGTH_SHORT).show();
        navigateToAllGroups();
    }

    @Override
    public void navigateToAllGroups() {
        startActivityForResult(new Intent(this, AllGroupsActivity.class), CODE_ALL_GROUP);
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
                        onBackPressed();
                    }
                }
        );
    }

    private void populateGroupCode(String groupCode) {
        String[] codeSplitter = groupCode.split("");
        mEtGroupCode1.setText(codeSplitter[1]);
        mEtGroupCode2.setText(codeSplitter[2]);
        mEtGroupCode3.setText(codeSplitter[3]);
        mEtGroupCode4.setText(codeSplitter[4]);
        mEtGroupCode5.setText(codeSplitter[5]);
    }
}