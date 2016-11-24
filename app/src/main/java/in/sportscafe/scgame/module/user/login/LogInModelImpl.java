package in.sportscafe.scgame.module.user.login;

import android.app.Activity;
import android.content.Intent;

import com.jeeva.android.Log;
import com.jeeva.android.facebook.FacebookHandler;
import com.jeeva.android.facebook.user.FacebookProfile;
import com.jeeva.android.facebook.user.GetProfileModelImpl;
import com.jeeva.android.facebook.user.UserModelImpl;

import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.user.login.dto.LogInRequest;
import in.sportscafe.scgame.module.user.login.dto.LogInResponse;
import in.sportscafe.scgame.module.user.login.dto.UserInfo;
import in.sportscafe.scgame.webservice.MyWebService;
import in.sportscafe.scgame.webservice.ScGameCallBack;
import retrofit2.Call;
import retrofit2.Response;

public class LogInModelImpl implements LogInModel {

    private static final String PROVIDER_FB = "fb";

    private static final String PROVIDER_GOOGLE = "gplus";

    private final String TAG = "LogInModelImpl";

    private LogInModelListener mLogInModelListener;

    private FacebookHandler mFacebookHandler;

    protected LogInModelImpl(LogInModelListener modelListener) {
        this.mLogInModelListener = modelListener;

        this.mFacebookHandler = FacebookHandler.getInstance(ScGame.getInstance());
    }

    public static LogInModel newInstance(LogInModelListener modelListener) {
        return new LogInModelImpl(modelListener);
    }

    @Override
    public void triggerFacebook() {

        if (ScGame.getInstance().hasNetworkConnection()) {
            // done facebook api request here
            if (mFacebookHandler.isLoggedIn()) {
                mFacebookHandler.logOut();
            }
            loginWithFacebook();
        } else {
            mLogInModelListener.onNoInternet();
        }
    }

    @Override
    public void onLoggedInGoogle(String token) {
        mLogInModelListener.onGettingProfile();
        callScGameLoginApi(PROVIDER_GOOGLE, token);
    }

    private void loginWithFacebook() {
        mFacebookHandler.login(mLogInModelListener.getActivity(), new UserModelImpl.UserModelListener() {
                    @Override
                    public void onFbLoginSuccess() {
                        getProfile();
                    }

                    @Override
                    public void onLoginCancelled() {
                        mLogInModelListener.onLoginCanceled();
                    }

                    @Override
                    public void onFbLoginFailed() {
                        mLogInModelListener.onLoginFailed();
                    }
                },
                ScGame.getInstance().getRequiredPermissionListFromFacebook());
    }

    @Override
    public void onGetResult(int requestCode, int resultCode, Intent data) {     // callback for facebook
        mFacebookHandler.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean isPreferenceDone() {
        Log.d(TAG, ScGameDataHandler.getInstance().getFavoriteSportsIdList() + "");
        return ScGameDataHandler.getInstance().getFavoriteSportsIdList().size() > 0;
    }

    private void getProfile() {
        mLogInModelListener.onGettingProfile();
        mFacebookHandler.getProfile(new GetProfileModelImpl.GetProfileModelListener() {
            @Override
            public void onGetProfileSuccess(FacebookProfile facebookProfile) {
                callScGameLoginApi(PROVIDER_FB, facebookProfile.getAccessToken());
                Log.d(TAG, MyWebService.getInstance().getJsonStringFromObject(facebookProfile));
            }

            @Override
            public void onGetProfileFailed() {
                mLogInModelListener.onLoginFailed();
            }
        });
    }

    private void callScGameLoginApi(String provider, String accessToken) {
        LogInRequest logInRequest = new LogInRequest();
        logInRequest.setAccessPovider(provider);
        logInRequest.setAccessToken(accessToken);

        MyWebService.getInstance().getLogInRequest(logInRequest).enqueue(
                new ScGameCallBack<LogInResponse>() {
                    @Override
                    public void onResponse(Call<LogInResponse> call, Response<LogInResponse> response) {
                        if (response.isSuccessful()) {
                            handleLoginResponse(response.body().getUserInfo());
                        } else {
                            mLogInModelListener.onLoginFailed();
                        }
                    }
                });
    }

    private void handleLoginResponse(UserInfo userInfo) {
        ScGameDataHandler scGameDataHandler = ScGameDataHandler.getInstance();

        scGameDataHandler.setLoggedInUser(true);
        scGameDataHandler.setUserId(userInfo.getId().toString());
        scGameDataHandler.setCookie(userInfo.getCookie());
        scGameDataHandler.setNumberofPowerups(userInfo.getPowerUps().get("2x"));
        scGameDataHandler.setNumberofBadges(userInfo.getBadges().size());
        userInfo.setPoints(100L);
        Log.i("powerups", String.valueOf(userInfo.getPowerUps().get("2x")));
        scGameDataHandler.setUserInfo(userInfo);

        // Getting the saved sports from server and saving it locally
        scGameDataHandler.setFavoriteSportsIdList(userInfo.getUserSports());

        mLogInModelListener.onLoginCompleted();
    }

    public interface LogInModelListener {

        Activity getActivity();

        void onGettingProfile();

        void onLoginCompleted();

        void onLoginCanceled();

        void onLoginFailed();

        void onNoInternet();
    }
}