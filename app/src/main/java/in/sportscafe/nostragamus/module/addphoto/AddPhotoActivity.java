package in.sportscafe.nostragamus.module.addphoto;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jeeva.android.BaseActivity;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.popups.GetScreenNameListener;

/**
 * Created by hmspl on 4/2/16.
 */
public class AddPhotoActivity extends Activity implements AddPhotoView, View.OnClickListener,GetScreenNameListener {

    private AddPhotoPresenter mAddPhotoPresenter;

    private TextView mTvGallery;

    private TextView mTvCamera;

    private TextView mTvProcessText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_add_photo);

        positionView();

        mTvGallery = (TextView) findViewById(R.id.add_photo_tv_gallery);
        mTvCamera = (TextView) findViewById(R.id.add_photo_tv_camera);
        mTvProcessText = (TextView) findViewById(R.id.add_photo_tv_load);

        mAddPhotoPresenter = AddPhotoPresenterImpl.newInstance(this);
        mAddPhotoPresenter.onCreateAddPhoto();
    }

    private void positionView() {
        Window window = getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
//        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        DisplayMetrics displayMetrics = AppSnippet.getDisplayDimension(this);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = displayMetrics.widthPixels;
        params.gravity = Gravity.BOTTOM;
        window.setLayout(displayMetrics.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void navigateToGallery(int requestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, requestCode);
    }

    @Override
    public void navigateToCamera(Uri cameraOutputUri, int requestCode) {
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(cameraIntent, requestCode);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraOutputUri);
        startActivityForResult(cameraIntent, requestCode);
    }

    @Override
    public void setResult(Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
    }

    @Override
    public void goBack() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    AddPhotoActivity.super.onBackPressed();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void showProcessing() {
        /*mTvGallery.setVisibility(View.GONE);
        mTvCamera.setVisibility(View.GONE);
        mTvProcessText.setVisibility(View.VISIBLE);*/
    }

    @Override
    public void dismissProcessing() {
        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvGallery.setVisibility(View.VISIBLE);
                mTvCamera.setVisibility(View.VISIBLE);
                mTvProcessText.setVisibility(View.GONE);
            }
        });*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            mAddPhotoPresenter.onGetSuccessResult(requestCode, data);
        } else {
            mAddPhotoPresenter.onGetFailureResult(requestCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_photo_tv_gallery:
                mAddPhotoPresenter.onClickGallery();
                break;
            case R.id.add_photo_tv_camera:
                mAddPhotoPresenter.onClickCamera();
                break;
        }
    }

    @Override
    public String onGetScreenName() {
        return null;
    }
}