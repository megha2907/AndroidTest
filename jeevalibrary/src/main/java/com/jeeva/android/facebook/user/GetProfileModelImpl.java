package com.jeeva.android.facebook.user;

import android.text.TextUtils;

import com.facebook.AccessToken;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeva.android.converter.JacksonConverterFactory;
import com.jeeva.android.facebook.FacebookConstants;
import com.jeeva.android.facebook.url.FacebookUrl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetProfileModelImpl implements FacebookConstants.ApiKeys {

    private static final List<String> mDefaultFields = Arrays.asList(FacebookFields.FACEBOOK_ID,
            FacebookFields.FIRST_NAME, FacebookFields.LAST_NAME, FacebookFields.GENDER);

    private AccessToken mAccessToken;

    private GetProfileModelListener mGetProfileModelListener;

    public GetProfileModelImpl(GetProfileModelListener modelListener) {
        this.mGetProfileModelListener = modelListener;
    }

    public void getProfileByPermissions(AccessToken accessToken, List<String> readPermissions) {
        getProfileByFields(accessToken, getRequiredFieldsByPermission(readPermissions));
    }

    public void getProfileByFields(AccessToken accessToken, List<String> requiredFields) {
        this.mAccessToken = accessToken;
        getFacebookWebService(FacebookUrl.getFactory().getBaseUrl())
                .getProfile(convertListToString(requiredFields), accessToken.getToken()).enqueue(
                new Callback<FacebookProfile>() {
                    @Override
                    public void onResponse(Call<FacebookProfile> call, Response<FacebookProfile> response) {
                        if(response.isSuccessful()) {
                            FacebookProfile facebookProfile = response.body();
                            facebookProfile.setAccessToken(mAccessToken.getToken());
                            facebookProfile.setTokenExpiry(mAccessToken.getExpires());

                            mGetProfileModelListener.onGetProfileSuccess(facebookProfile);
                        } else {
                            mGetProfileModelListener.onGetProfileFailed();
                        }
                    }

                    @Override
                    public void onFailure(Call<FacebookProfile> call, Throwable t) {
                        mGetProfileModelListener.onGetProfileFailed();
                    }
                }
        );
    }

    private List<String> getRequiredFieldsByPermission(List<String> readPermissions) {

        // Add default fields
        List<String> requiredFields = new ArrayList<>(mDefaultFields);

        for (String permission : readPermissions) {
            switch (permission) {
                case FacebookPermission.USER_BIRTHDAY:
                    requiredFields.add(FacebookFields.BIRTHDAY);
                    break;
                case FacebookPermission.EMAIL:
                    requiredFields.add(FacebookFields.EMAIL);
                    break;
                case FacebookPermission.USER_LOCATION:
                    requiredFields.add(FacebookFields.LOCATION);
                    break;
                case FacebookPermission.USER_RELATIONSHIP_DETAILS:
                    requiredFields.add(FacebookFields.INTERESTED_IN);
                    break;
                case FacebookPermission.USER_WORK_HISTORY:
                    requiredFields.add(FacebookFields.WORK);
                    break;
            }
        }
        return requiredFields;
    }

    private String convertListToString(List<String> dataList) {
        return TextUtils.join(",", dataList);
    }

    private FacebookWebService getFacebookWebService(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create(getObjectMapper()))
                .client(getCustomHttpClient())
                .build();
        return retrofit.create(FacebookWebService.class);
    }

    private OkHttpClient getCustomHttpClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(30, TimeUnit.SECONDS);
        httpClient.readTimeout(30, TimeUnit.SECONDS);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        return httpClient.build();
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper mObjectMapper = new ObjectMapper();
        mObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mObjectMapper;
    }

    public interface GetProfileModelListener {

        void onGetProfileSuccess(FacebookProfile facebookProfile);

        void onGetProfileFailed();
    }
}