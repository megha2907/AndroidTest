package in.sportscafe.nostragamus.module.prediction.copyAnswer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatch;
import in.sportscafe.nostragamus.module.popups.timerPopup.TimerFinishDialogHelper;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerScreenData;
import in.sportscafe.nostragamus.module.prediction.playScreen.PredictionActivity;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayScreenDataDto;

public class CopyAnswerLauncherActivity extends NostraBaseActivity implements View.OnClickListener {

    private final String TAG = CopyAnswerLauncherActivity.class.getSimpleName();

    private CopyAnswerScreenData mCopyAnswerScreenData;

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_answer_launcher);

        initViews();
        initMembers();
        populateHeaderDetails();
    }

    private void initViews() {
        findViewById(R.id.back_btn).setOnClickListener(this);
        findViewById(R.id.copy_answer_launch_import_contest_button).setOnClickListener(this);
        findViewById(R.id.copy_answer_launch_play_again_button).setOnClickListener(this);
    }

    private void initMembers() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle args = getIntent().getExtras();
            mCopyAnswerScreenData = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.COPY_ANSWER_SCREEN_DATA));
        }
    }

    private void populateHeaderDetails() {
        if (mCopyAnswerScreenData != null) {
            TextView headingParty1TextView = (TextView) findViewById(R.id.copy_answer_heading_party1_textView);
            TextView headingParty2TextView = (TextView) findViewById(R.id.copy_answer_heading_party2_textView);
            TextView subHeadingTextView = (TextView) findViewById(R.id.copy_answer_sub_heading_textView);
            TextView titleTextView = (TextView) findViewById(R.id.copy_answer_launcher_title_textView);
            TextView buttonCountTextView = (TextView) findViewById(R.id.copy_answer_contest_count_textView);

            if (mCopyAnswerScreenData.getPlayScreenDataDto() != null) {
                String party1 = mCopyAnswerScreenData.getPlayScreenDataDto().getMatchPartyTitle1();
                String party2 = mCopyAnswerScreenData.getPlayScreenDataDto().getMatchPartyTitle2();
                if (!TextUtils.isEmpty(party1) && !TextUtils.isEmpty(party2)) {
                    headingParty1TextView.setText(party1);
                    headingParty2TextView.setText(party2);
                }

                if (!TextUtils.isEmpty(mCopyAnswerScreenData.getPlayScreenDataDto().getSubTitle())) {
                    subHeadingTextView.setText(mCopyAnswerScreenData.getPlayScreenDataDto().getSubTitle());
                }
            }

            if (mCopyAnswerScreenData.getInPlayMatch() != null) {
                int playedContests = mCopyAnswerScreenData.getInPlayMatch().getCopyAnswerPlayedContests();
                titleTextView.setText("You have played this game in " + playedContests + " other contests");
                buttonCountTextView.setText("(" + playedContests + ")");
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                onBackPressed();
                break;

            case R.id.copy_answer_launch_import_contest_button:
                onImportContestClicked();
                break;

            case R.id.copy_answer_launch_play_again_button:
                onPlayAgainClicked();
                break;
        }
    }

    private void onPlayAgainClicked() {
        if (mCopyAnswerScreenData != null && mCopyAnswerScreenData.getInPlayMatch() != null) {
            InPlayMatch match = mCopyAnswerScreenData.getInPlayMatch();
            if (TimerFinishDialogHelper.canPlayGame(match.getMatchStartTime())) {

                PlayScreenDataDto playData = mCopyAnswerScreenData.getPlayScreenDataDto();
                if (playData != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constants.BundleKeys.PLAY_SCREEN_DATA, Parcels.wrap(playData));

                    Intent predictionIntent = new Intent(this, PredictionActivity.class);
                    predictionIntent.putExtras(bundle);
                    predictionIntent.putExtra(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT,
                            PredictionActivity.LaunchedFrom.IN_PLAY_SCREEN_PLAY_MATCH); // Same as inplay match
                    startActivity(predictionIntent);
                    finish();
                }

            } else {
                TimerFinishDialogHelper.showCanNotPlayGameTimerOutDialog(getSupportFragmentManager());
            }
        }
    }

    private void onImportContestClicked() {
        Intent intent = new Intent(this, CopyAnswerActivity.class);
        intent.putExtras(getIntent().getExtras());
        startActivity(intent);
    }
}

