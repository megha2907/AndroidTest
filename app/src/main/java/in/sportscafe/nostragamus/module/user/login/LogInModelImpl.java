package in.sportscafe.nostragamus.module.user.login;

import android.app.Activity;
import android.content.Intent;

import com.jeeva.android.Log;
import com.jeeva.android.facebook.FacebookHandler;
import com.jeeva.android.facebook.user.FacebookProfile;
import com.jeeva.android.facebook.user.GetProfileModelImpl;
import com.jeeva.android.facebook.user.UserModelImpl;

import java.math.BigInteger;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.login.dto.LogInRequest;
import in.sportscafe.nostragamus.module.user.login.dto.LogInResponse;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.login.dto.UserLoginInResponse;
import in.sportscafe.nostragamus.module.user.login.dto.UserProfile;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

public class LogInModelImpl implements LogInModel {

    private static final String PROVIDER_FB = "facebook";

    private static final String PROVIDER_GOOGLE = "google";

    private final String TAG = "LogInModelImpl";

    private LogInModelListener mLogInModelListener;

    private FacebookHandler mFacebookHandler;

    protected LogInModelImpl(LogInModelListener modelListener) {
        this.mLogInModelListener = modelListener;

        this.mFacebookHandler = FacebookHandler.getInstance(Nostragamus.getInstance());
    }

    public static LogInModel newInstance(LogInModelListener modelListener) {
        return new LogInModelImpl(modelListener);
    }

    @Override
    public void triggerFacebook() {

        if (Nostragamus.getInstance().hasNetworkConnection()) {
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
    public void onLoggedInGoogle(String token, String personId, String personName, String persongender, String profileUrl,
                                 String personEmail, String personPhoto) {
        mLogInModelListener.onGettingProfile();
        callNostragamusLoginApi(PROVIDER_GOOGLE,token,personId,personName,persongender,profileUrl,personEmail,personPhoto);
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
                Nostragamus.getInstance().getRequiredPermissionListFromFacebook());
    }

    @Override
    public void onGetResult(int requestCode, int resultCode, Intent data) {     // callback for facebook
        mFacebookHandler.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean isPreferenceDone() {
        Log.d(TAG, NostragamusDataHandler.getInstance().getFavoriteSportsIdList() + "");
        return NostragamusDataHandler.getInstance().getFavoriteSportsIdList().size() > 0;
    }

    private void getProfile() {
        mLogInModelListener.onGettingProfile();
        mFacebookHandler.getProfile(new GetProfileModelImpl.GetProfileModelListener() {
            @Override
            public void onGetProfileSuccess(FacebookProfile facebookProfile) {
                callNostragamusLoginApi(PROVIDER_FB, facebookProfile.getAccessToken(),
                        facebookProfile.getId(),facebookProfile.getFirst_name(),
                        facebookProfile.getGender(),"https://facebook.com/"+facebookProfile.getId(),
                        facebookProfile.getEmail(),facebookProfile.getProfilePictureUrl(350,350));
                Log.d(TAG, MyWebService.getInstance().getJsonStringFromObject(facebookProfile));
            }

            @Override
            public void onGetProfileFailed() {
                mLogInModelListener.onLoginFailed();
            }
        });
    }

    private void callNostragamusLoginApi(String provider, String accessToken,String id , String username,
                                         String gender, String profileUrl, String email,String photo) {

        LogInRequest logInRequest = new LogInRequest();
        logInRequest.setAccessToken(accessToken);
        logInRequest.setRefreshToken("");

//        List<String> emails = new ArrayList<>();
//        emails.add(email);
//        Emails newemail = new Emails();
//        newemail.setEmail(emails);
//
//        List<String> photos = new ArrayList<>();
//        photos.add(photo);
//        Photos newphotos = new Photos();
//        newphotos.setPhoto(photos);

        UserProfile userProfile = new UserProfile();
        userProfile.setId(id);
        userProfile.setUserName(username);
        userProfile.setDisplayName(username);
        userProfile.setGender(gender);
        userProfile.setProfileUrl(profileUrl);
        userProfile.setProvider(provider);
        userProfile.setEmails(email);
        userProfile.setPhotos(photo);


        if(null != NostragamusDataHandler.getInstance().getReferralUserId()){
            Log.i("user_referral_id",NostragamusDataHandler.getInstance().getReferralUserId());
            userProfile.setUserReferralId(NostragamusDataHandler.getInstance().getReferralUserId());
        }else {
            Log.i("user_referral_id","null");
            userProfile.setUserReferralId("");
        }


        logInRequest.setUserProfile(userProfile);

        MyWebService.getInstance().getLogInRequest(logInRequest).enqueue(
                new NostragamusCallBack<LogInResponse>() {
                    @Override
                    public void onResponse(Call<LogInResponse> call, Response<LogInResponse> response) {
                        if (response.isSuccessful()) {
                            handleLoginResponse(response.body().getUserLoginInResponse());
                        } else {
                            mLogInModelListener.onLoginFailed();
                        }
                    }
                });
    }

    private void handleLoginResponse(UserLoginInResponse userLoginInResponse) {

        UserInfo userInfo = userLoginInResponse.getUserInfo();

        NostragamusDataHandler nostragamusDataHandler = NostragamusDataHandler.getInstance();

        nostragamusDataHandler.setLoggedInUser(true);
        nostragamusDataHandler.setUserId(userInfo.getId().toString());
        nostragamusDataHandler.setJwtToken(userLoginInResponse);
        nostragamusDataHandler.setNumberof2xPowerups(userInfo.getPowerUps().get("2x"));
        nostragamusDataHandler.setNumberofNonegsPowerups(userInfo.getPowerUps().get("no_negs"));
        nostragamusDataHandler.setNumberofAudiencePollPowerups(userInfo.getPowerUps().get("player_poll"));
        nostragamusDataHandler.setNumberofBadges(userInfo.getBadges().size());
        userInfo.setPoints(100L);
        Log.i("powerups", String.valueOf(userInfo.getPowerUps().get("2x")));
        nostragamusDataHandler.setUserInfo(userInfo);

        // Getting the saved sports from server and saving it locally
        nostragamusDataHandler.setFavoriteSportsIdList(userInfo.getUserSports());

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