package in.sportscafe.nostragamus.module.popups;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;

import com.jeeva.android.BaseActivity;

import in.sportscafe.nostragamus.R;

/**
 * Created by deepanshi on 11/21/17.
 */

public class PopUpDialogActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
        window.getAttributes().dimAmount = 0.7f;
    }

}

