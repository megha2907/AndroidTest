package in.sportscafe.nostragamus.module.user.group.joingroup;

import android.os.Bundle;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.module.user.group.JoinGroupApiModelImpl;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;

/**
 * Created by Jeeva on 1/7/16.
 */
public class JoinGroupModelImpl implements JoinGroupModel, JoinGroupApiModelImpl.OnJoinGroupApiModelListener {

    private String mGroupCode;

    private OnJoinGroupModelListener mJoinGroupModelListener;

    private JoinGroupApiModelImpl mJoinGroupApiModel;

    private JoinGroupModelImpl(OnJoinGroupModelListener listener) {
        this.mJoinGroupModelListener = listener;
        mJoinGroupApiModel = JoinGroupApiModelImpl.newInstance(this);
    }

    public static JoinGroupModel newInstance(OnJoinGroupModelListener listener) {
        return new JoinGroupModelImpl(listener);
    }

    @Override
    public void init(Bundle bundle) {
        if(null != bundle && bundle.containsKey(BundleKeys.GROUP_CODE)) {
            mGroupCode = bundle.getString(BundleKeys.GROUP_CODE);
            if(mJoinGroupApiModel.isValidGroupCode(mGroupCode)) {
                mJoinGroupModelListener.onGetGroupCode(mGroupCode);
            }
        }
    }

    @Override
    public void joinGroup(String groupCode) {
        mJoinGroupApiModel.joinGroup(groupCode, false);

        /*GroupInfo groupInfo = new GroupInfo();
        groupInfo.setId(2323);
        groupInfo.setName("Dummy Group");
        onSuccessJoinGroupApi(groupInfo);*/
    }

    @Override
    public boolean hadGroupCode() {
        return null != mGroupCode;
    }

    @Override
    public void onSuccessJoinGroupApi(GroupInfo groupInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKeys.GROUP_INFO, Parcels.wrap(groupInfo));
        mJoinGroupModelListener.onSuccess(bundle);
    }

    @Override
    public void onFailedJoinGroupApi(String message) {
        mJoinGroupModelListener.onFailed(message);
    }

    @Override
    public void onNoInternet() {
        mJoinGroupModelListener.onNoInternet();
    }

    @Override
    public void onInvalidGroupCode() {
        mJoinGroupModelListener.onInvalidGroupCode();
    }

    public interface OnJoinGroupModelListener {

        void onSuccess(Bundle bundle);

        void onInvalidGroupCode();

        void onNoInternet();

        void onFailed(String message);

        void onGetGroupCode(String groupCode);
    }
}