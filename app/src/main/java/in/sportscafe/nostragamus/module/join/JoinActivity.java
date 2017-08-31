package in.sportscafe.nostragamus.module.join;

import android.os.Bundle;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.nostraHome.NostraHomeActivity;

public class JoinActivity extends NostraHomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setJoinSelected();
        setContentLayout(R.layout.activity_join);
    }
}
