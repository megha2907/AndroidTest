package in.sportscafe.nostragamus.module.getstart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.Arrays;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.feedback.GoogleFormActivity;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.user.login.LogInActivity;
import in.sportscafe.nostragamus.module.user.myprofile.edit.EditProfileActivity;
import in.sportscafe.nostragamus.module.user.preference.PreferenceManager;
import in.sportscafe.nostragamus.module.user.preference.SavePreferenceModelImpl;
import in.sportscafe.nostragamus.module.user.sportselection.SportSelectionActivity;
import in.sportscafe.nostragamus.module.user.sportselection.SportsModelImpl;

/**
 * Created by Jeeva on 27/5/16.
 */
public class GetStartActivity extends Activity {

    private static final int GOOGLE_FORM_CODE = 90;

    private static final String GOOGLE_FORM_LINK = "https://docs.google.com/a/sportscafe.in/forms/d/e/1FAIpQLScFyPxEFIeCe1Yg3xiV_BhxKTDKDCm0PuzltgLPXz7iwWexLg/viewform?c=0&w=1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getstarted);

        if (NostragamusDataHandler.getInstance().isInitialFeedbackFormShown()) {
            handleGetStart();
        } else {
            navigateToForm();
        }
    }

    private void handleGetStart() {
        if (NostragamusDataHandler.getInstance().getFavoriteSportsIdList().size() > 0) {
//            if (ScGameDataHandler.getInstance().getFavoriteSportsIdList().contains(10)){
//                autoSaveAllSports();
//            }
            navigateToHome();
            return;
        } else if (NostragamusDataHandler.getInstance().isLoggedInUser()) {
            if (null == NostragamusDataHandler.getInstance().getUserInfo().getUserNickName()) {
                navigateToEditProfile();
                return;
            }
            else {
                navigateToSportSelection();
                return;
            }
        }

        getUpdatedSports();

        findViewById(R.id.activity_getstarted_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToLogin();
//                NostragamusDataHandler.getInstance().setUserId("1");
//                                autoSaveAllSports();
//                                navigateToHome();
            }
        });
    }

    private void navigateToForm() {
        Intent intent = new Intent(this, GoogleFormActivity.class);
        intent.putExtra(BundleKeys.FEEDBACK_FORM_URL, GOOGLE_FORM_LINK);
        startActivityForResult(intent, GOOGLE_FORM_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(GOOGLE_FORM_CODE == requestCode) {
            handleGetStart();
        }
    }

    private void autoSaveAllSports() {
        new PreferenceManager().savePreference(Arrays.asList(new Integer[] {1,2,3,4,5,6,7,8,9,10}),
                new SavePreferenceModelImpl.SavePreferenceModelListener() {
                    @Override
                    public void onSuccess()
                    {
                    }

                    @Override
                    public void onNoInternet() {
                        onNoInternet();
                    }

                    @Override
                    public void onFailed(String message) {
                    }
                });
    }

    private void navigateToHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void navigateToEditProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("screen", BundleKeys.LOGIN_SCREEN);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    private void navigateToSportSelection() {
        Intent intent = new Intent(this, SportSelectionActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("screen", BundleKeys.LOGIN_SCREEN);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void navigateToLogin() {
        startActivity(new Intent(this, LogInActivity.class));
//        finish();
    }

    private void getUpdatedSports() {
        SportsModelImpl.newInstance(new SportsModelImpl.SportsModelListener() {
            @Override
            public void onGetSportsSuccess() {
            }

            @Override
            public void onNoNetwork() {
            }

            @Override
            public void onGetSportsFailed() {
            }
        }).getAllSportsFromServer();
    }
}