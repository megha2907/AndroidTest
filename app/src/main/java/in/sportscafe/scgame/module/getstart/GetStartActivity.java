package in.sportscafe.scgame.module.getstart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.home.HomeActivity;
import in.sportscafe.scgame.module.user.login.LogInActivity;
import in.sportscafe.scgame.module.user.myprofile.edit.EditProfileActivity;
import in.sportscafe.scgame.module.user.sportselection.SportSelectionActivity;
import in.sportscafe.scgame.module.user.sportselection.SportsModelImpl;

/**
 * Created by Jeeva on 27/5/16.
 */
public class GetStartActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getstarted);

        if (ScGameDataHandler.getInstance().getFavoriteSportsIdList().size() > 0) {
//            if (ScGameDataHandler.getInstance().getFavoriteSportsIdList().contains(10)){
//                autoSaveAllSports();
//            }
            navigateToHome();
            return;
        } else if (ScGameDataHandler.getInstance().isLoggedInUser()) {
            if (null == ScGameDataHandler.getInstance().getUserInfo().getUserNickName()) {
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
            }
        });
    }

//    private void autoSaveAllSports() {
//        new PreferenceManager().savePreference(Arrays.asList(new Integer[] {1,2,3,4,5,6,7,8,9,10}),
//                new SavePreferenceModelImpl.SavePreferenceModelListener() {
//                    @Override
//                    public void onSuccess()
//                    {
//                    }
//
//                    @Override
//                    public void onNoInternet() {
//                        onNoInternet();
//                    }
//
//                    @Override
//                    public void onFailed(String message) {
//                    }
//                });
//    }

    private void navigateToHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void navigateToEditProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("screen", Constants.BundleKeys.LOGIN_SCREEN);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    private void navigateToSportSelection() {
        Intent intent = new Intent(this, SportSelectionActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("screen", Constants.BundleKeys.LOGIN_SCREEN);
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