package in.sportscafe.nostragamus.module.addphoto;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.ImageFolders;
import in.sportscafe.nostragamus.Nostragamus;

import static in.sportscafe.nostragamus.Constants.IMAGE_TYPE;

/**
 * Created by hmspl on 4/2/16.
 */
public class AddPhotoModelImpl implements AddPhotoModel, ImageHandler.ProcessImageListener {

    private static final String KEY_CAMERA_DATA = "data";

    private static final int PROFILE_PIC_MAX_SIZE = 500;

    private OnAddPhotoModelListener mAddPhotoModelListener;

    private Uri mCameraOutputUri;

    public AddPhotoModelImpl(OnAddPhotoModelListener addPhotoModelListener) {
        this.mAddPhotoModelListener = addPhotoModelListener;
    }

    public static AddPhotoModel newInstance(OnAddPhotoModelListener addPhotoModelListener) {
        return new AddPhotoModelImpl(addPhotoModelListener);
    }

    @Override
    public Uri getCameraOutputUri() {
        File externalFile = getExternalRandomFile();
        if(null != externalFile) {
            return mCameraOutputUri = Uri.fromFile(externalFile);
        }
        return null;
    }

    @Override
    public void processGalleryResult(Intent data) {
        if (null != data) {
                Uri tempUri = data.getData();
                if(null != tempUri) {
                    ImageHandler.newInstance().processAndSaveImageFromUri(
                            Nostragamus.getInstance().getApplicationContext(),
                            tempUri, getRandomFileName(), PROFILE_PIC_MAX_SIZE, this);
                    return;
                }
        }
        mAddPhotoModelListener.onPhotoAddedFailed();
    }

    @Override
    public void processCameraResult(Intent data) {
        /*if(bundle.containsKey(KEY_CAMERA_DATA)) {
            Bitmap cameraBitmap = (Bitmap) bundle.get(KEY_CAMERA_DATA);
            if(null != cameraBitmap) {
                ImageHandler.newInstance().processAndSaveImageFromBitmap(
                        BuzzTm.getBuzzTm().getApplicationContext(),
                        cameraBitmap, getRandomFileName(), PROFILE_PIC_MAX_SIZE, this);
                return;
            }
        }*/
        if(null != mCameraOutputUri) {
            ImageHandler.newInstance().processAndSaveImageFromUri(Nostragamus.getInstance().getApplicationContext(),
                    mCameraOutputUri, getRandomFileName(), PROFILE_PIC_MAX_SIZE, this);
            return;
        }
        mAddPhotoModelListener.onPhotoAddedFailed();
    }

    private String getRandomFileName() {
        File file = getImageFolder(ImageFolders.PROFILE);
        return file.getAbsolutePath() + "/file_" + Calendar.getInstance().getTimeInMillis() + "." + IMAGE_TYPE;
    }

    private File getImageFolder(String subFolderName) {
        return AppSnippet.getInternalFolder(Nostragamus.getInstance().getApplicationContext(),
                ImageFolders.MAIN, subFolderName);
    }

    private File getExternalRandomFile() {
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .getAbsolutePath() + "/Nostragamus");
        dir.mkdirs();

        if (dir.exists()) {
            try {
                return File.createTempFile("IMG_" + Calendar.getInstance().getTimeInMillis(), ".png", dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void onProcessSuccess(String imagePath, Bitmap processedImage) {
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.ADDED_NEW_IMAGE_PATH, imagePath);
        mAddPhotoModelListener.onPhotoAdded(bundle);
    }

    @Override
    public void onProcessFailed(Exception exception) {
        mAddPhotoModelListener.onPhotoAddedFailed();
    }

    public interface OnAddPhotoModelListener {

        void onPhotoAdded(Bundle bundle);

        void onPhotoAddedFailed();
    }
}