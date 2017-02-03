package in.sportscafe.nostragamus.module.user.group.newgroup;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.jeeva.android.widgets.HmImageView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.addphoto.AddPhotoActivity;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsChecker;
import in.sportscafe.nostragamus.module.popups.GetScreenNameListener;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GroupInfoActivity;

/**
 * Created by Jeeva on 1/7/16.
 */
public class NewGroupActivity extends NostragamusActivity implements NewGroupView,
        View.OnClickListener,GetScreenNameListener {

    private EditText mEtGroupName;

    private RecyclerView mRvTournamentSelection;

    private NewGroupPresenter mNewGroupPresenter;

    private LinearLayoutManager mlinearLayoutManagerVertical;

    private Toolbar mtoolbar;


    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    PermissionsChecker checker;

    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        checker = new PermissionsChecker(this);

        this.mEtGroupName = (EditText) findViewById(R.id.new_group_et_group_name);

        this.mRvTournamentSelection = (RecyclerView) findViewById(R.id.new_group_rcv);
        this.mRvTournamentSelection.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        this.mRvTournamentSelection.setHasFixedSize(true);

        this.mNewGroupPresenter = NewGroupPresenterImpl.newInstance(this);
        this.mNewGroupPresenter.onCreateNewGroup();

        initToolBar();
    }

    @Override
    public void setAdapter(GrpTournamentSelectionAdapter adapter) {
        this.mRvTournamentSelection.setAdapter(adapter);
    }

    @Override
    public void setSuccessBack(Bundle bundle) {
        Intent intent = new Intent(this, GroupInfoActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_group_btn_done:
                mNewGroupPresenter.onClickDone(getTrimmedText(mEtGroupName));
                break;
        }
    }

    public void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.new_group_toolbar);
        mtoolbar.setTitle("Create Group");
        setSupportActionBar(mtoolbar);
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

    @Override
    public void setGroupImage(String imageUrl) {
        ((HmImageView) findViewById(R.id.group_iv_user_image)).setImageUrl(imageUrl);
    }

    @Override
    public void navigateToAddPhoto(int addPhotoRequestCode) {
        startActivityForResult(new Intent(this, AddPhotoActivity.class), addPhotoRequestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mNewGroupPresenter.onGetResult(requestCode, resultCode, data);
    }


    public void showImagePopup(View view) {
        if (checker.lacksPermissions(PERMISSIONS_READ_STORAGE)) {
            startPermissionsActivity(PERMISSIONS_READ_STORAGE);
        } else {

            mNewGroupPresenter.onClickImage();
        }
    }

    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(this, 0, permission);
    }

    @Override
    public String onGetScreenName() {
        return Constants.ScreenNames.GROUPS_CREATE_NEW;
    }
}