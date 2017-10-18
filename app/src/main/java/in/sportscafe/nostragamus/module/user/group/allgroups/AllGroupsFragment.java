package in.sportscafe.nostragamus.module.user.group.allgroups;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.BaseActivity;
import com.jeeva.android.BaseFragment;
import com.jeeva.android.widgets.CustomProgressbar;
import com.jeeva.android.widgets.CustomToast;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.popups.inapppopups.InAppPopupActivity;
import in.sportscafe.nostragamus.module.user.group.groupinfo.GroupInfoActivity;
import in.sportscafe.nostragamus.module.user.group.joingroup.JoinGroupActivity;
import in.sportscafe.nostragamus.module.user.playerprofile.dto.PlayerInfo;

import static android.app.Activity.RESULT_OK;

/**
 * Created by deepanshi on 12/7/16.
 */

public class AllGroupsFragment extends NostraBaseFragment implements AllGroupsView, View.OnClickListener {

    private static final String TAG = AllGroupsFragment.class.getSimpleName();
    private static final int GROUP_INFO = 11;
    private static final int JOIN_GROUP = 12;

    private RecyclerView mRvAllGroups;
    private AllGroupsPresenter mAllGroupsPresenter;
    private TextView mTvEmptyGroups;

    public static AllGroupsFragment newInstance() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(BundleKeys.IS_ALL_GROUPS, true);

        AllGroupsFragment fragment = new AllGroupsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static AllGroupsFragment newMutualGroupInstance(PlayerInfo playerInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleKeys.PLAYERINFO, Parcels.wrap(playerInfo));
        bundle.putBoolean(BundleKeys.IS_ALL_GROUPS, false);

        AllGroupsFragment fragment = new AllGroupsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_groups, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        this.mRvAllGroups = (RecyclerView) rootView.findViewById(R.id.all_groups_rcv);
        this.mRvAllGroups.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        this.mRvAllGroups.setHasFixedSize(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iniMembers();
    }

    private void iniMembers() {
        this.mAllGroupsPresenter = AllGroupsPresenterImpl.newInstance(this);
        this.mAllGroupsPresenter.onCreateAllGroups(getArguments());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (GROUP_INFO == requestCode) {
                mAllGroupsPresenter.onGetGroupInfoResult(data.getExtras());
            } else if (JOIN_GROUP == requestCode) {
                mAllGroupsPresenter.onGetJoinGroupResult(data.getExtras());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.join_grp_btn:
                navigateToJoinGroup();
                break;
        }
    }


    @Override
    public void showTitleBar() {
        if (getView() != null) {
            Toolbar toolbar = (Toolbar) getView().findViewById(R.id.all_groups_toolbar);
            toolbar.setVisibility(View.VISIBLE);
            Button createGroupbtn = (Button) getView().findViewById(R.id.join_grp_btn);
            createGroupbtn.setVisibility(View.VISIBLE);
            createGroupbtn.setOnClickListener(this);
        }
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        this.mRvAllGroups.setAdapter(adapter);
    }

    @Override
    public void showGroupsEmpty() {
        if (getView() != null) {
            mTvEmptyGroups = (TextView) getView().findViewById(R.id.all_groups_empty_tv);
            mTvEmptyGroups.setVisibility(View.VISIBLE);
            ImageView mIvEmptyGroups = (ImageView) getView().findViewById(R.id.all_groups_empty_iv);
            mIvEmptyGroups.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void navigateToJoinGroup() {
        startActivityForResult(new Intent(getContext(), JoinGroupActivity.class), JOIN_GROUP);
    }

    @Override
    public void navigateToGroupInfo(Bundle bundle) {
        Intent intent = new Intent(getContext(), GroupInfoActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, GROUP_INFO);
    }

    @Override
    public void showPopUp(String popUpType) {
        openPopup(popUpType);
    }

    private void openPopup(String popUpType) {
        Intent intent = new Intent(getContext(), InAppPopupActivity.class);
        intent.putExtra(Constants.InAppPopups.IN_APP_POPUP_TYPE, popUpType);
        startActivity(intent);
    }

    @Override
    public void goBackWithSuccessResult() {
        getActivity().setResult(RESULT_OK);
        getActivity().onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mGroupItemClickReceiver,
                new IntentFilter(Constants.IntentActions.ACTION_GROUP_CLICK));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mGroupItemClickReceiver);
    }

    BroadcastReceiver mGroupItemClickReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mAllGroupsPresenter.onClickGroupItem(intent.getExtras());
        }
    };

    /*  ----------------------
     Temp method as HomeActivity is changed to NostraHomeActivity
     * and unneccesory methods removed from new-home activity,
       * As Group fragment is kept older one, need to manage these methods...
       * ----------- */


    public void showMessage(String message) {
        CustomToast.getToast(getContext()).show(message);
    }

    public void showMessage(String message, int duration) {
        CustomToast.getToast(getContext()).show(message, duration);
    }

    @Override
    public boolean isMessageShowing() {
        return CustomToast.getToast(getContext()).isToastShowing();
    }

    @Override
    public void dismissMessage() {
        CustomToast.getToast(getContext()).dismissToast();
    }

    public void showProgressbar() {
        CustomProgressbar.getProgressbar(getContext()).show();
    }

    public void updateProgressbar() {
    }

    public boolean dismissProgressbar() {
        return CustomProgressbar.getProgressbar(getContext()).dismissProgress();
    }

    public void showSoftKeyboard(View view) {
        getInputMethodManager().showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public void hideSoftKeyboard() {
        if (getActivity() != null && getActivity().getCurrentFocus() != null) {
            getInputMethodManager().hideSoftInputFromWindow(getActivity().getCurrentFocus()
                    .getApplicationWindowToken(), 0);
        }
    }

    @Override
    public LayoutInflater getLayoutInflater() {
        return null;
    }

    private InputMethodManager getInputMethodManager() {
        return (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public String getTrimmedText(TextView textView) {
        return textView.getText().toString().trim();
    }

    @Override
    public void showInApp() {
        Log.d(TAG, "No action in ShowInApp");
    }

    @Override
    public void hideInApp() {
        Log.d(TAG, "No action in HideInApp");
    }

    @Override
    public void showInAppMessage(String message) {
        showMessage(message);
    }

    @Override
    public void showMessage(String message, String positiveButton, View.OnClickListener positiveClickListener) {
        showMessage(message, positiveButton, positiveClickListener);
    }

    @Override
    public void showMessage(String message, String positiveButton, View.OnClickListener positiveClickListener, String negativeButton, View.OnClickListener negativeClickListener) {
        showMessage(message, positiveButton, positiveClickListener, negativeButton, negativeClickListener);
    }
}