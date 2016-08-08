package in.sportscafe.scgame.module.getstart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.moe.pushlibrary.PayloadBuilder;

import java.util.Date;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.home.HomeActivity;
import in.sportscafe.scgame.module.user.login.LogInActivity;
import in.sportscafe.scgame.module.user.sportselection.SportSelectionActivity;
import in.sportscafe.scgame.module.user.sportselection.SportsModelImpl;

/**
 * Created by Jeeva on 27/5/16.
 */
public class GetStartActivity extends ScGameActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getstarted);

        if (ScGameDataHandler.getInstance().getFavoriteSportsIdList().size() > 0) {
            navigateToHome();
            return;
        } else if (ScGameDataHandler.getInstance().isLoggedInUser()) {
            navigateToSportSelection();
            return;
        }

        getUpdatedSports();

        findViewById(R.id.activity_getstarted_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToLogin();
//                trackClick("GetStarted", "Button");
            }
        });

        findViewById(R.id.activity_getstarted_btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });
    }

    private void trackClick(String text, String componentName) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString("text", text)
                .putAttrString("componentName", componentName)
                .putAttrDate("clickDate", new Date());
        getMoeHelper().trackEvent("Click", builder.build());
    }

    private void navigateToHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void navigateToSportSelection() {
        startActivity(new Intent(this, SportSelectionActivity.class));
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