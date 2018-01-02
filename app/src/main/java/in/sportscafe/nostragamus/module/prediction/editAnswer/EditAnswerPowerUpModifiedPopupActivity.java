package in.sportscafe.nostragamus.module.prediction.editAnswer;

import android.os.Bundle;
import android.view.View;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.popups.PopUpDialogActivity;

public class EditAnswerPowerUpModifiedPopupActivity extends PopUpDialogActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_answer_power_up_modified_popup);

        initViews();
    }

    private void initViews() {
        (findViewById(R.id.ok_btn)).setOnClickListener(this);
        (findViewById(R.id.cancel_btn)).setOnClickListener(this);
        (findViewById(R.id.modified_powerup_cross_btn)).setOnClickListener(this);
        (findViewById(R.id.popup_bg)).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok_btn:
                onKeepEditingClicked();
                break;

            case R.id.cancel_btn:
                onCancelChangesClicked();
                break;

            case R.id.modified_powerup_cross_btn:
            case R.id.popup_bg:
                onBackPressed();
                break;
        }
    }

    private void onKeepEditingClicked() {
        onBackPressed();
    }

    private void onCancelChangesClicked() {
        setResult(RESULT_OK);
        finish();
    }
}
