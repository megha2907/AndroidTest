package in.sportscafe.nostragamus.module.popups.inapppopups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;

/**
 * Created by deepanshi on 3/11/17.
 */

public class InAppPopupActivity extends AppCompatActivity implements View.OnClickListener {

    private String inAppPopUpType;

    private String inAppPopUpTitle;

    private String inAppPopUpBody;

    private String inAppPopUpBtnTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_popup);

        inAppPopUpType = getIntent().getExtras().getString(Constants.InAppPopups.IN_APP_POPUP_TYPE);
        checkPopupType(inAppPopUpType);
    }

    private void checkPopupType(String inAppPopUpType) {

        if (inAppPopUpType.equals(Constants.InAppPopups.FIRST_MATCH_PLAYED)) {
            inAppPopUpTitle = "Remember!";
            inAppPopUpBody = "Great! You will be notified once the result of the match is out";
            inAppPopUpBtnTitle = "Check Current Standings!";
            NostragamusDataHandler.getInstance().setPlayedFirstMatch(true);
        } else if (inAppPopUpType.equals(Constants.InAppPopups.SECOND_MATCH_PLAYED_WITH_NO_POWERUP)) {
            inAppPopUpTitle = "Power Tip!";
            inAppPopUpBody = "A good thumb rule is to use 1 power-up of each kind per each game!";
            inAppPopUpBtnTitle = "Check Powerups!";
        } else if (inAppPopUpType.equals(Constants.InAppPopups.FIFTH_MATCH_PLAYED_WITH_NO_POWERUP)) {
            inAppPopUpTitle = "Power Tip!";
            inAppPopUpBody = "Power-ups are feeling left alone - use them to race ahead of your peers in the leaderboards";
            inAppPopUpBtnTitle = "Ok!";
        } else if (inAppPopUpType.equals(Constants.InAppPopups.LESS_POWERUPS)) {
            inAppPopUpTitle = "Power Tip!";
            inAppPopUpBody = "You can get more power-ups by inviting people  to join the game & then use the \"+\" button below to add them to the particular challenge";
            inAppPopUpBtnTitle = "Refer a friend";
        } else if (inAppPopUpType.equals(Constants.InAppPopups.NOT_VISITED_OTHER_PROFILE)) {
            inAppPopUpTitle = "Cool Tip!";
            inAppPopUpBody = "You can compare your predictions with your competitors by either clicking on their profile from the Leaderboard (or) by using the \"compare results\" button";
            inAppPopUpBtnTitle = "Ok!";
            NostragamusDataHandler.getInstance().setVisitedLeaderBoards(true);
        } else if (inAppPopUpType.equals(Constants.InAppPopups.SEVENTH_MATCH_PLAYED_WITH_NO_GROUPS)) {
            inAppPopUpTitle = "Cool Tip!";
            inAppPopUpBody = "Spread and share the love for NostraGamus by playing with your friends. As Mr. Trump would say, \"Trust me, it's more fun..\"";
            inAppPopUpBtnTitle = "Create group";
        }

        showPopUp(inAppPopUpTitle, inAppPopUpBody, inAppPopUpBtnTitle);

    }

    private void showPopUp(String inAppPopUpTitle, String inAppPopUpBody, String inAppPopUpBtnTitle) {


        ((TextView) findViewById(R.id.in_app_popup_title)).setText(inAppPopUpTitle);
        ((TextView) findViewById(R.id.in_app_popup_body)).setText(inAppPopUpBody);
        ((Button) findViewById(R.id.in_app_popup_btn)).setText(inAppPopUpBtnTitle);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.in_app_popup_btn:
                onBackPressed();
                break;
        }
    }
}