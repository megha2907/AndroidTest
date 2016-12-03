package in.sportscafe.nostragamus.module.settings;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.moe.pushlibrary.MoEHelper;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.WebViewActivity;
import in.sportscafe.nostragamus.module.getstart.GetStartActivity;
import in.sportscafe.nostragamus.module.user.myprofile.edit.EditProfileActivity;
import in.sportscafe.nostragamus.module.user.referral.ReferralActivity;

/**
 * Created by deepanshi on 31/8/16.
 */
public class SettingsActivity extends AppCompatActivity {

    private Toolbar mtoolbar;

    private static final int EDIT_PROFILE_CODE = 35;

    private String[] account = {"Edit Profile", "Logout" , "Refer a Friend"};
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

        accountListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch(position){
                    case 0:
                        navigateToEditProfile();
                        break;
                    case 1:
                        onClickLogout();
                        break;
                    case 2:
                        navigateToReferFriend();
                        break;
                }

            }
        });


        supportListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch(position){
                    case 0:
                        navigateToWebView("https://sportscafe.in/contactus","Report a Problem");
                        break;
                }

            }
        });

        aboutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch(position){
                    case 0:
                        navigateToWebView("https://sportscafe.in/aboutus","About Sportscafe");
                        break;
                    case 1:
                        navigateToWebView("https://sportscafe.in/termsandconditions","Terms of Service");
                        break;
                    case 2:
                        navigateToWebView("https://sportscafe.in/privacy","Privacy Policy");
                        break;
                }

            }
        });



    }

    private void navigateToReferFriend() {

        Intent intent=new Intent(this,ReferralActivity.class);
        startActivity(intent);
    }

    private void navigateToWebView(String url, String heading) {

        Intent intent=new Intent(SettingsActivity.this,WebViewActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("url", url);
        bundle.putString("heading", heading);
        intent.putExtras(bundle);
        startActivity(intent);

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

    private void navigateToEditProfile() {
        Intent intent=new Intent(this,EditProfileActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("screen", Constants.BundleKeys.HOME_SCREEN);
        intent.putExtras(bundle);
        startActivityForResult(intent, EDIT_PROFILE_CODE);
    }


    private void onClickLogout() {
        NostragamusDataHandler.getInstance().clearAll();
        navigateToLogIn();

        NostragamusAnalytics.getInstance().trackLogOut();
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
