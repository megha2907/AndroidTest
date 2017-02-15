package in.sportscafe.nostragamus.module.user.group.editgroupinfo;

import android.content.Intent;
import android.os.Bundle;

import org.parceler.Parcels;

import java.io.File;
import java.util.UUID;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GrpNameUpdateModelImpl;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.nostragamus.module.user.myprofile.dto.Result;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Jeeva on 12/6/16.
 */
public class EditGroupInfoModelImpl implements EditGroupInfoModel {

    private boolean mAnythingChanged = false;

    private GroupInfo mGroupInfo;

    private OnEditGroupInfoModelListener mGroupInfoModelListener;

    private EditGroupInfoModelImpl(OnEditGroupInfoModelListener listener) {
        this.mGroupInfoModelListener = listener;
    }

    public static EditGroupInfoModel newInstance(OnEditGroupInfoModelListener listener) {
        return new EditGroupInfoModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {
        mGroupInfo = Parcels.unwrap(bundle.getParcelable(BundleKeys.GROUP_INFO));
    }

    @Override
    public GroupInfo getGroupInfo() {
        return mGroupInfo;
    }

    @Override
    public void onGetImage(Intent data) {
        File file = new File(data.getStringExtra(BundleKeys.ADDED_NEW_IMAGE_PATH));
        updateGroupPhoto(file, "game/groupimages/", file.getName());
    }

    private void updateGroupPhoto(File file, String filepath, String filename) {
        if (filepath.equals(null)) {
            mGroupInfoModelListener.onGroupImagePathNull();
            return;
        }
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            mGroupInfoModelListener.onUpdating();
            callUpdateGroupPhotoApi(file, filepath, UUID.randomUUID().toString() + "_" + filename);
        } else {
            mGroupInfoModelListener.onNoInternet();
        }
    }

    private void callUpdateGroupPhotoApi(File file, String filepath, String filename) {
        MyWebService.getInstance().getUploadPhotoRequest(file, filepath, filename).enqueue(new NostragamusCallBack<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                super.onResponse(call, response);

                if (response.isSuccessful()) {
                    mAnythingChanged = true;

                    mGroupInfo.setPhoto(response.body().getResult());
                    mGroupInfoModelListener.onPhotoUpdate(mGroupInfo.getPhoto());
                } else {
                    mGroupInfoModelListener.onEditFailed(response.message());
                }
            }
        });
    }

    @Override
    public void updateGroupName(final String groupName) {
        if (groupName.isEmpty()) {
            mGroupInfoModelListener.onGroupNameEmpty();
            return;
        }

        new GrpNameUpdateModelImpl(mGroupInfo.getId(),
                new GrpNameUpdateModelImpl.OnGrpNameUpdateModelListener() {
                    @Override
                    public void onSuccessGrpNameUpdate() {
                        mAnythingChanged = true;

                        mGroupInfo.setName(groupName);
                        mGroupInfoModelListener.onGroupNameUpdateSuccess();
                    }

                    @Override
                    public void onFailedGrpNameUpdate(String message) {
                        mGroupInfoModelListener.onEditFailed(message);
                    }

                    @Override
                    public void onNoInternet() {
                        mGroupInfoModelListener.onNoInternet();
                    }
                }).updateGrpName(groupName, mGroupInfo.getPhoto());
    }

    @Override
    public Bundle getGroupInfoBundle() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKeys.GROUP_INFO, Parcels.wrap(mGroupInfo));
        return bundle;
    }

    @Override
    public boolean isAnythingChanged() {
        return mAnythingChanged;
    }

    public interface OnEditGroupInfoModelListener {

        void onGroupNameUpdateSuccess();

        void onGroupNameEmpty();

        void onNoInternet();

        void onGroupImagePathNull();

        void onUpdating();

        void onPhotoUpdate(String photo);

        void onEditFailed(String message);
    }
}