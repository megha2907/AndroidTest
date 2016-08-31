package in.sportscafe.scgame.module.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;

/**
 * Created by Jeeva on 10/6/16.
 */
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Bundle bundle = new Bundle();
        long groupId = getIntent().getLongExtra(Constants.BundleKeys.GROUP_ID,0);
        Log.i("groupidtest", String.valueOf(groupId));


    }
}