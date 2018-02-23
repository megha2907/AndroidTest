package in.sportscafe.nostragamus.module.navigation.help.howtoplay;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

/**
 * Created by deepanshi on 2/21/18.
 */

public class HowToPlayActivity extends NostragamusActivity implements HowToPlayFragmentListener {

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.HOW_TO_PLAY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadHowToPlayFragment();
    }

    private void loadHowToPlayFragment() {
        HowToPlayFragment fragment = new HowToPlayFragment();
        FragmentHelper.replaceFragment(this, R.id.fragment_container, fragment);
    }

    @Override
    public void onBackClicked() {
        super.onBackPressed();
    }

}
