package in.sportscafe.nostragamus.module.test;

import android.os.Bundle;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.question.tourlist.TourListActivity;

/**
 * Created by Jeeva on 10/6/16.
 */
public class TestActivity extends NostragamusActivity {

    private static final String TAG = "TestActivity";

    @Override
    public String getScreenName() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
}