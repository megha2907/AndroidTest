package in.sportscafe.scgame.module.user.group.joingroup;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

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

    private EditText mEtGroupCode1;
    private EditText mEtGroupCode2;
    private EditText mEtGroupCode3;
    private EditText mEtGroupCode4;
    private EditText mEtGroupCode5;

    private String mGroupCode;

    private JoinGroupPresenter mJoinGroupPresenter;

    private Toolbar mtoolbar;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        this.mEtGroupCode1 = (EditText) findViewById(R.id.join_group_et_group_code_char_one);
        this.mEtGroupCode2 = (EditText) findViewById(R.id.join_group_et_group_code_char_two);
        this.mEtGroupCode3 = (EditText) findViewById(R.id.join_group_et_group_code_char_three);
        this.mEtGroupCode4 = (EditText) findViewById(R.id.join_group_et_group_code_char_four);
        this.mEtGroupCode5 = (EditText) findViewById(R.id.join_group_et_group_code_char_five);

        this.mJoinGroupPresenter = JoinGroupPresenterImpl.newInstance(this);
        this.mJoinGroupPresenter.onCreateJoinGroup();
        initToolBar();

        mEtGroupCode1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(mEtGroupCode1.getText().toString().length()==1)     //size as per your requirement
                {
                    mEtGroupCode2.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        mEtGroupCode2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(mEtGroupCode2.getText().toString().length()==1)     //size as per your requirement
                {
                    mEtGroupCode3.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });


        mEtGroupCode3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(mEtGroupCode3.getText().toString().length()==1)     //size as per your requirement
                {
                    mEtGroupCode4.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        mEtGroupCode4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(mEtGroupCode4.getText().toString().length()==1)     //size as per your requirement
                {
                    mEtGroupCode5.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });


        mEtGroupCode5.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(mEtGroupCode5.getText().toString().length()==1)     //size as per your requirement
                {
                    mEtGroupCode5.setImeOptions(EditorInfo.IME_ACTION_DONE);
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

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
                mGroupCode=getTrimmedText(mEtGroupCode1)+getTrimmedText(mEtGroupCode2)
                        +getTrimmedText(mEtGroupCode3)
                        +getTrimmedText(mEtGroupCode4)
                        +getTrimmedText(mEtGroupCode5);

                mJoinGroupPresenter.onClickJoin(mGroupCode);
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
                        onBackPressed();
                    }
                }

        );
    }
}