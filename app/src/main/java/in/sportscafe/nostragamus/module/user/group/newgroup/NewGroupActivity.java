package in.sportscafe.nostragamus.module.user.group.newgroup;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Constants.AnalyticsLabels;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsChecker;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GroupInfoActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Jeeva on 1/7/16.
 */
public class NewGroupActivity extends NostragamusActivity implements NewGroupView,
        View.OnClickListener {

    private EditText mEtGroupName;

    private RecyclerView mRvTournamentSelection;

    private NewGroupPresenter mNewGroupPresenter;

    private LinearLayoutManager mlinearLayoutManagerVertical;

    private Toolbar mtoolbar;


    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    PermissionsChecker checker;

    private String imagePath;

    private ImageView mIvGroupImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        mIvGroupImage = (ImageView) findViewById(R.id.group_iv_user_image);
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

        Picasso.with(this)
                .load(imageUrl)
                .into(mIvGroupImage);
    }


    public void showImagePopup(View view) {
        if (checker.lacksPermissions(PERMISSIONS_READ_STORAGE)) {
            startPermissionsActivity(PERMISSIONS_READ_STORAGE);
        } else {
            // File System.
            final Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_PICK);

            // Chooser of file system options.
            final Intent chooserIntent = Intent.createChooser(galleryIntent, Constants.Alerts.IMAGE_UPLOAD_TEXT);
            startActivityForResult(chooserIntent, 1010);
        }
    }

    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(this, 0, permission);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK ) {

                if (requestCode == 1010){

                    if (data == null) {
                        Toast.makeText(getActivity(), Constants.Alerts.IMAGE_UNABLE_TO_PICK,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Uri selectedImageUri = data.getData();

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);

                    if (cursor != null) {
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imagePath = cursor.getString(columnIndex);

                        File file = new File(imagePath);
                        long length = file.length() / 1024;

                        if(length < 1024){
                            if (!TextUtils.isEmpty(imagePath)) {
                                uploadImage();
                            }

                            Picasso.with(getApplicationContext()).load(new File(imagePath))
                                    .into(mIvGroupImage);
                            cursor.close();

                            mIvGroupImage.setVisibility(View.VISIBLE);

                        }
                        else
                        {
                            Toast toast =Toast.makeText(getContext(), "Image size is too large, please select an image with size <1MB", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }

                        NostragamusAnalytics.getInstance().trackNewGroup(AnalyticsActions.PHOTO, AnalyticsLabels.GALLERY);
                    } else {

                        mIvGroupImage.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), Constants.Alerts.IMAGE_UNABLE_TO_LOAD,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void uploadImage() {

        //File creating from selected URL
        File file = new File(imagePath);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // add another part within the multipart request
        String Serverfilepath = "game/groupimages/";
        RequestBody filepath =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), Serverfilepath);

        String ServerfileName = file.getName();
        RequestBody filename =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), ServerfileName);


        mNewGroupPresenter.onGroupPhotoDone(file, Serverfilepath, ServerfileName);

    }
}