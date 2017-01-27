package com.jeeva.android.facebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.Sharer;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.ShareDialog;
import com.jeeva.android.Log;
import com.jeeva.android.facebook.user.GetProfileModelImpl;
import com.jeeva.android.facebook.user.UserModelImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nandakumar on 17/4/15.
 */
public class FacebookHandler implements FacebookConstants.Alerts {

    private static final String TAG = "FacebookHandler";

    private static FacebookHandler sFacebookHandler;

    private CallbackManager mCallBackManager;

    private FacebookHandler() {
        mCallBackManager = CallbackManager.Factory.create();
    }

    public static FacebookHandler getInstance(Context context) {
        if (null == sFacebookHandler) {
            sFacebookHandler = new FacebookHandler();
            if (!FacebookSdk.isInitialized()) {
                Log.d(TAG, "FacebookSdk Initialized");
                FacebookSdk.sdkInitialize(context);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return sFacebookHandler;
    }

    public void login(Activity activity,
                      UserModelImpl.UserModelListener userModelListener,
                      List<String> readPermissions) {
        new UserModelImpl(userModelListener).loginUser(activity, mCallBackManager, readPermissions);
    }

    public boolean isLoggedIn() {
        return null != getAccessToken();
    }

    public AccessToken getAccessToken() {
        return AccessToken.getCurrentAccessToken();
    }

    public void isPermissionAvailable(String permission, OnPermissionListener permissionListener) {
        if (getAccessToken().getPermissions().contains(permission)) {
            permissionListener.permissionAvailable();
        } else {
            permissionListener.permissionNotAvailable();
        }
    }

    public void getProfile(GetProfileModelImpl.GetProfileModelListener profileModelListener) {
        AccessToken accessToken = getAccessToken();
        new GetProfileModelImpl(profileModelListener).getProfileByPermissions(accessToken,
                new ArrayList<>(accessToken.getPermissions()));
    }

    public void getProfile(GetProfileModelImpl.GetProfileModelListener profileModelListener,
                           List<String> requiredFields) {
        new GetProfileModelImpl(profileModelListener).getProfileByFields(getAccessToken(),
                requiredFields);
    }

    public void showInviteFriendsDialog(Activity activity, String appLinkUrl, String previewImageUrl) {
        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(appLinkUrl)
                    .setPreviewImageUrl(previewImageUrl)
                    .build();
            AppInviteDialog.show(activity, content);
        }
    }

    public void share(Activity activity, String contentUrl) {
        ShareDialog shareDialog = new ShareDialog(activity);
        share(shareDialog, contentUrl);
    }

    public void share(Fragment fragment, String contentUrl) {
        ShareDialog shareDialog = new ShareDialog(fragment);
        share(shareDialog, contentUrl);
    }

    private void share(ShareDialog shareDialog, String contentUrl) {
        shareDialog.registerCallback(mCallBackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.i("share",result.toString()+"share");
            }

            @Override
            public void onCancel() {
                Log.i("share","cancel"+"share");
            }

            @Override
            public void onError(FacebookException error) {
            }
        });

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent.Builder linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(contentUrl))
                    .setShareHashtag(new ShareHashtag.Builder()
                            .setHashtag("#NOSTRAGAMUS")
                            .build());

            shareDialog.show(linkContent.build());

        }
    }

    public void logOut() {
        LoginManager.getInstance().logOut();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallBackManager.onActivityResult(requestCode, resultCode, data);
    }

    public interface AccessTokenListener {

        void onGetAccessToken(AccessToken accessToken);

        void onNoAccessToken();
    }
}