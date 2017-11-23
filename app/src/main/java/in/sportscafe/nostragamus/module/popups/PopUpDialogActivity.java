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
        performActivityEntryAnimation();
    }

    private void performActivityExitAnimation() {
            overridePendingTransition(0, R.anim.popup_slide_out);
    }

    private void performActivityEntryAnimation() {
            overridePendingTransition(R.anim.popup_slide_in, 0);
    }

    @Override
    public void finish() {
        super.finish();
        performActivityExitAnimation();
    }

}

