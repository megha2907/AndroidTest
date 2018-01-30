package in.sportscafe.nostragamus.module.navigation.help;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.freshchat.consumer.sdk.ConversationOptions;
import com.freshchat.consumer.sdk.FaqOptions;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.navigation.help.dummygame.DummyGameActivity;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class HelpActivity extends NostragamusActivity implements HelpFragmentListener {

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.HELP;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        initialize();
        loadHelpFragment();
    }

    private void initialize() {
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.help_toolbar);
        TextView tvToolbar = (TextView) findViewById(R.id.help_toolbar_tv);
        tvToolbar.setText("Help");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.back_icon_grey);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }

    private void loadHelpFragment() {
        HelpFragment fragment = new HelpFragment();
        FragmentHelper.replaceFragment(this, R.id.fragment_container, fragment);
    }

    @Override
    public void onRulesClicked() {
        navigateToWebView(Constants.WebPageUrls.RULES, "Rules");
    }

    @Override
    public void onGamePlayClicked() {
        navigateToWebView(Constants.WebPageUrls.GAME_PLAY, "Gameplay");
    }

    @Override
    public void onFaqClicked() {
        navigateToWebView(Constants.WebPageUrls.FAQ, "FAQ");

//        FaqOptions faqOptions = new FaqOptions()
//                .showFaqCategoriesAsGrid(false)
//                .showContactUsOnAppBar(false)
//                .showContactUsOnFaqScreens(true)
//                .showContactUsOnFaqNotHelpful(false);
//        Freshchat.showFAQs(getApplicationContext(), faqOptions);

    }

    @Override
    public void onPlaySampleGameClicked() {
        Intent intent = new Intent(this, DummyGameActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSendFeedbackClicked() {

    }

    @Override
    public void onChatClicked() {

        UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();
        FreshchatUser user = Freshchat.getInstance(getApplicationContext()).getUser();
        if (userInfo != null && user != null) {

            if (!TextUtils.isEmpty(userInfo.getOtpMobileNumber())) {
                user.setFirstName(userInfo.getUserName())
                        .setEmail(userInfo.getEmail())
                        .setPhone("+91 ", userInfo.getOtpMobileNumber());
            } else {
                user.setFirstName(userInfo.getUserName())
                        .setEmail(userInfo.getEmail());
            }

            Freshchat.getInstance(getApplicationContext()).setUser(user);

            /* Set any custom metadata to give agents more context,
            and for segmentation for marketing or pro-active messaging */
            Map<String, String> userMeta = new HashMap<String, String>();
            userMeta.put("UserId", String.valueOf(userInfo.getId()));
            userMeta.put("Transaction Type", "");
            userMeta.put("Transaction Order Id", "");
            userMeta.put("Transaction Time", "");
            userMeta.put("Challenge Id", "");
            userMeta.put("MatchId", "");
            userMeta.put("RoomId", "");

            //Call setUserProperties to sync the user properties with Freshchat's servers
            Freshchat.getInstance(getApplicationContext()).setUserProperties(userMeta);

        }

         /* Open a Generic Chat Channel */
        List<String> tags = new ArrayList<>();
        tags.add("generic");
        ConversationOptions convOptions = new ConversationOptions()
                .filterByTags(tags, "generic");
        Freshchat.showConversations(getApplicationContext(), convOptions);

    }

}
