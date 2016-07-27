package in.sportscafe.scgame.module.user.myprofile.edit;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jeeva.android.volley.Volley;
import com.jeeva.android.widgets.HmImageView;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.ScGameActivity;

/**
 * Created by Jeeva on 12/6/16.
 */
public class EditProfileActivity extends ScGameActivity implements EditProfileView,
        View.OnClickListener {

    private EditText mEtName;

//    private EditText mEtUserName;

    private EditText mEtNickName;

    private EditProfilePresenter mEditProfilePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mEtName = (EditText) findViewById(R.id.edit_et_name);
//        mEtUserName = (EditText) findViewById(R.id.edit_et_username);
        mEtNickName = (EditText) findViewById(R.id.edit_et_nickname);

        this.mEditProfilePresenter = EditProfilePresenterImpl.newInstance(this);
        this.mEditProfilePresenter.onCreateEditProfile(getIntent().getExtras());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_btn_cancel:
                close();
                break;
            case R.id.edit_btn_done:
                mEditProfilePresenter.onClickDone(
                        getTrimmedText(mEtName),
                        getTrimmedText(mEtNickName)
                );
                break;
        }
    }

    @Override
    public void setProfileImage(String imageUrl) {
        ((HmImageView) findViewById(R.id.edit_iv_user_image)).setImageUrl(
                imageUrl,
                Volley.getInstance().getImageLoader(),
                false
        );
    }

    @Override
    public void setName(String name) {
        mEtName.setText(name);
    }

    /*@Override
    public void setUserName(String userName) {
        mEtUserName.setText(userName);
    }*/

    @Override
    public void setAbout(String about) {
        mEtNickName.setText(about);
    }

    @Override
    public void close() {
        onBackPressed();
    }

    @Override
    public void setSuccessResult() {
        setResult(RESULT_OK);
    }
}