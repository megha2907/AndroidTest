package in.sportscafe.scgame.module.common;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeeva.android.widgets.customfont.CustomButton;
import com.moe.pushlibrary.MoEHelper;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.analytics.ScGameAnalytics;
import in.sportscafe.scgame.module.getstart.GetStartActivity;

/**
 * Created by deepanshi on 31/8/16.
 */
public class Settings extends AppCompatActivity {

    private Toolbar mtoolbar;

    private String[] account = {"Edit Profile", "Logout", "Hourly Notifications", "Daily Notifications"};
    private String[] support = {"Report a Problem"};
    private String[] about = {"About Sportscafe" , "Terms of Service", "Privacy Policy"};

    private ListView accountListView;
    private ListView supportListView;
    private ListView aboutListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        accountListView = (ListView) findViewById(R.id.settings_account_lv);
        supportListView = (ListView) findViewById(R.id.settings_support_lv);
        aboutListView = (ListView) findViewById(R.id.settings_about_lv);

        accountListView.setAdapter(new ArrayAdapter<String>(this, R.layout.inflater_settings_list_row, account));
        supportListView.setAdapter(new ArrayAdapter<String>(this, R.layout.inflater_settings_list_row, support));
        aboutListView.setAdapter(new ArrayAdapter<String>(this, R.layout.inflater_settings_list_row, about));

        setDynamicHeight(accountListView);
        setDynamicHeight(supportListView);
        setDynamicHeight(aboutListView);

        initToolBar();
        getAppVersion();


    }

    private void getAppVersion() {

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String versionName = pInfo.versionName;
    }

    public void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        mtoolbar.setTitle("Settings");
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }

        );
    }


    private void onClickLogout() {
        ScGameDataHandler.getInstance().clearAll();
        navigateToLogIn();

        ScGameAnalytics.getInstance().trackLogOut();
        MoEHelper.getInstance(getApplicationContext()).logoutUser();

    }

    private void navigateToLogIn() {
        Intent intent = new Intent(getApplicationContext(), GetStartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public static void setDynamicHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        //check adapter if null
        if (adapter == null) {
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = height + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(layoutParams);
        listView.requestLayout();
    }


}
