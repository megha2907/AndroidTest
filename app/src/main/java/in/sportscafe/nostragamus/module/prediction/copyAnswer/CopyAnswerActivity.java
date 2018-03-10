package in.sportscafe.nostragamus.module.prediction.copyAnswer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.jeeva.android.widgets.CustomProgressbar;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.cache.CacheManagementHelper;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.module.customViews.CustomSnackBar;
import in.sportscafe.nostragamus.module.inPlay.ui.ResultsScreenDataDto;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsActivity;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.adapter.CopyAnswerExpandableAdapter;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.adapter.CopyAnswerAdapterListener;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dataProvider.AnsweredContestsApiModelImpl;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dataProvider.CopyPredictionsApiImpl;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerContest;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerContestsResponse;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerResponse;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerScreenData;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayScreenDataDto;

public class CopyAnswerActivity extends NostraBaseActivity implements View.OnClickListener {

    private CopyAnswerScreenData mCopyAnswerScreenData;

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_answer);
        setImmersiveFullScreenMode();

        initViews();
        initMembers();
        populateHeaderDetails();
        loadContestDataFromServer();
    }

    private void initViews() {
        findViewById(R.id.back_btn).setOnClickListener(this);
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
            }
        }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                onBackPressed();
                break;
        }
    }

    private void loadContestDataFromServer() {
        if (mCopyAnswerScreenData != null && mCopyAnswerScreenData.getInPlayMatch() != null) {

            CustomProgressbar.getProgressbar(CopyAnswerActivity.this).show();
            new AnsweredContestsApiModelImpl().fetchCopyAnswerContestsList(
                    mCopyAnswerScreenData.getInPlayMatch().getMatchId(),
                    getFetchAnsweredContestApiListener());
        }
    }

    @NonNull
    private AnsweredContestsApiModelImpl.CopyAnswerContestApiListener getFetchAnsweredContestApiListener() {
        return new AnsweredContestsApiModelImpl.CopyAnswerContestApiListener() {
            @Override
            public void noInternet() {
                CustomProgressbar.getProgressbar(CopyAnswerActivity.this).dismissProgress();
                handleError("", Constants.DataStatus.NO_INTERNET);
            }

            @Override
            public void onCopyAnswerContestListReceived(CopyAnswerContestsResponse response) {
                CustomProgressbar.getProgressbar(CopyAnswerActivity.this).dismissProgress();
                onDataReceived(response);
            }

            @Override
            public void onApiFailure() {
                CustomProgressbar.getProgressbar(CopyAnswerActivity.this).dismissProgress();
                handleError("", -1);
            }

            @Override
            public void onServerSentError(String errorMsg) {
                CustomProgressbar.getProgressbar(CopyAnswerActivity.this).dismissProgress();
                if (TextUtils.isEmpty(errorMsg)) {
                    errorMsg = Constants.Alerts.SOMETHING_WRONG;
                }
                handleError(errorMsg, -1);
            }
        };
    }

    private void onDataReceived(CopyAnswerContestsResponse response) {
        if (response != null) {
            ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.copy_answer_expandable_listView);
            expandableListView.setAdapter(new CopyAnswerExpandableAdapter(expandableListView.getContext(),
                    getAnswerList(response), getParentAdapterListener()));
        }
    }

    private List<CopyAnswerContest> getAnswerList(CopyAnswerContestsResponse response) {
        List<CopyAnswerContest> list = response.getData();
        if (response.getData() != null && response.getData().size() > 0) {
            CopyAnswerContest header = new CopyAnswerContest(); // Used as Header View/Layout inside list
            list.add(0, header);
        }
        return list;
    }

    @NonNull
    private CopyAnswerAdapterListener getParentAdapterListener() {
        return new CopyAnswerAdapterListener() {

            @Override
            public void onUseClicked(CopyAnswerContest copyAnswerContest, boolean shouldApplyPowerUp) {
                copyPredictions(copyAnswerContest, shouldApplyPowerUp);
            }
        };
    }

    private void copyPredictions(CopyAnswerContest copyAnswerContest, boolean shouldApplyPowerUp) {
        if (copyAnswerContest != null && mCopyAnswerScreenData != null && mCopyAnswerScreenData.getInPlayMatch() != null) {

            int targetRoomId = 0;
            if (mCopyAnswerScreenData.getPlayScreenDataDto() != null &&
                    mCopyAnswerScreenData.getPlayScreenDataDto().getInPlayContestDto() != null) {
                targetRoomId = mCopyAnswerScreenData.getPlayScreenDataDto().getInPlayContestDto().getRoomId();
            }

            CustomProgressbar.getProgressbar(this).show();
            new CopyPredictionsApiImpl().useContestAndCopyPredictions(
                    mCopyAnswerScreenData.getInPlayMatch().getMatchId(),
                    Integer.parseInt(copyAnswerContest.getRoomId()),
                    targetRoomId,
                    shouldApplyPowerUp,
                    getCopyAnswerApiListener());
        }
    }

    @NonNull
    private CopyPredictionsApiImpl.CopyAnswerUsePredictionsApiListener getCopyAnswerApiListener() {
        return new CopyPredictionsApiImpl.CopyAnswerUsePredictionsApiListener() {
            @Override
            public void noInternet() {
                CustomProgressbar.getProgressbar(CopyAnswerActivity.this).dismissProgress();
                handleError("", Constants.DataStatus.NO_INTERNET);
            }

            @Override
            public void onSuccess(CopyAnswerResponse response) {
                CustomProgressbar.getProgressbar(CopyAnswerActivity.this).dismissProgress();
                onMatchAnsweredCopied(response);
            }

            @Override
            public void onApiFailure() {
                CustomProgressbar.getProgressbar(CopyAnswerActivity.this).dismissProgress();
                handleError("", Constants.DataStatus.FROM_SERVER_API_FAILED);
            }

            @Override
            public void onServerSentError(String errorMsg) {
                CustomProgressbar.getProgressbar(CopyAnswerActivity.this).dismissProgress();
                if (TextUtils.isEmpty(errorMsg)) {
                    errorMsg = Constants.Alerts.SOMETHING_WRONG;
                }
                handleError(errorMsg, -1);
            }
        };
    }

    private void onMatchAnsweredCopied(CopyAnswerResponse response) {
        /* Fetch Inplay data and save into DB - This is for Bottom-nav bar badge notification counter */
        new CacheManagementHelper().fetchInplayDataAndSaveIntoDb(getApplicationContext());

        if (response != null && response.getData() != null && mCopyAnswerScreenData != null) {
            ResultsScreenDataDto data = new ResultsScreenDataDto();
            PlayScreenDataDto playScreenData = mCopyAnswerScreenData.getPlayScreenDataDto();

            if (playScreenData != null) {
                data.setRoomId(playScreenData.getRoomId());
                data.setChallengeId(playScreenData.getChallengeId());
                data.setMatchId(playScreenData.getMatchId());
                data.setMatchStatus(playScreenData.getMatchStatus());
                data.setChallengeName(playScreenData.getChallengeName());
                data.setPlayingPseudoGame(playScreenData.isPlayingPseudoGame());
                data.setChallengeStartTime(playScreenData.getChallengeStartTime());
                if (playScreenData.getInPlayContestDto() != null) {
                    data.setInPlayContestDto(playScreenData.getInPlayContestDto());
                    data.setSubTitle(playScreenData.getInPlayContestDto().getContestName());
                }
            }

            Bundle args = new Bundle();
            args.putParcelable(Constants.BundleKeys.RESULTS_SCREEN_DATA, Parcels.wrap(data));

            Intent intent = new Intent(this, MyResultsActivity.class);
            intent.putExtras(args);
            intent.putExtra(Constants.BundleKeys.SCREEN_LAUNCHED_FROM_PARENT, MyResultsActivity.LaunchedFrom.COPY_ANSWER_SUCCESS);
            intent.putExtra(Constants.BundleKeys.COPY_ANSWER_POWERUP_COPIED, response.getData().isPowerupsCopied());

            startActivity(intent);
            finish();
        }
    }

    private void handleError(String msg, int status) {
        if (!isFinishing()) {
            View view = findViewById(R.id.copy_answer_launcher_root_view);

            if (!TextUtils.isEmpty(msg)) {
                CustomSnackBar.make(view, msg, CustomSnackBar.DURATION_SECS_5).show();
            } else {
                switch (status) {
                    case Constants.DataStatus.NO_INTERNET:
                        CustomSnackBar.make(view, Constants.Alerts.NO_NETWORK_CONNECTION, CustomSnackBar.DURATION_SECS_5).show();
                        break;

                    default:
                        CustomSnackBar.make(view, Constants.Alerts.SOMETHING_WRONG, CustomSnackBar.DURATION_SECS_5).show();
                        break;
                }
            }
        }
    }
}
