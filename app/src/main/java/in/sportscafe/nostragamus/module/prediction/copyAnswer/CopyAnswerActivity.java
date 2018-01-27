package in.sportscafe.nostragamus.module.prediction.copyAnswer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jeeva.android.widgets.CustomProgressbar;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.module.contest.contestDetailsAfterJoining.InplayContestDetailsActivity;
import in.sportscafe.nostragamus.module.customViews.CustomSnackBar;
import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivity;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.adapter.CopyAnswerParentAdapterListener;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.adapter.CopyAnswerRecyclerParentAdapter;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dataProvider.AnsweredContestsApiModelImpl;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dataProvider.CopyPredictionsApiImpl;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerContest;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerContestsResponse;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerResponse;
import in.sportscafe.nostragamus.module.prediction.copyAnswer.dto.CopyAnswerScreenData;

public class CopyAnswerActivity extends NostraBaseActivity implements View.OnClickListener {

    private CopyAnswerScreenData mCopyAnswerScreenData;
    private RecyclerView mRecyclerView;
    private CheckBox mCopyPowerUpsCheckBox;

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_answer);

        initViews();
        initMembers();
        populateHeaderDetails();
        loadContestDataFromServer();
    }

    private void initViews() {
        findViewById(R.id.back_btn).setOnClickListener(this);

        mCopyPowerUpsCheckBox = (CheckBox) findViewById(R.id.copy_answer_use_powerup_checkbox);
        mRecyclerView = (RecyclerView) findViewById(R.id.copy_answer_parent_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
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
                TextView playedContestCounterTextView = (TextView) findViewById(R.id.copy_answer_title_counter_textView);

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
                    playedContestCounterTextView.setText("(" + playedContests + ")");
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
            CopyAnswerRecyclerParentAdapter adapter = new CopyAnswerRecyclerParentAdapter(
                    response.getData(), getParentAdapterListener());
            mRecyclerView.setAdapter(adapter);
        }
    }

    @NonNull
    private CopyAnswerParentAdapterListener getParentAdapterListener() {
        return new CopyAnswerParentAdapterListener() {
            @Override
            public void onUseClicked(CopyAnswerContest copyAnswerContest) {
                copyPredictions(copyAnswerContest);
            }

            @Override
            public void scrollToItem(final int position) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(mRecyclerView.getContext()) {
                            @Override
                            protected int getVerticalSnapPreference() {
                                return LinearSmoothScroller.SNAP_TO_START;
                            }
                        };
                        smoothScroller.setTargetPosition(position);
                        mRecyclerView.getLayoutManager().startSmoothScroll(smoothScroller);
                    }
                }, 600);
            }
        };
    }

    private void copyPredictions(CopyAnswerContest copyAnswerContest) {
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
                    mCopyPowerUpsCheckBox.isChecked(),
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
        CustomSnackBar.make(findViewById(R.id.copy_answer_launcher_root_view),
                "Predictions copied successfully", CustomSnackBar.DURATION_INFINITE).
                setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finishCopyActivityStack();
                    }
                }).show();
    }

    private void finishCopyActivityStack() {
        Intent clearTaskIntent = new Intent(this, InplayContestDetailsActivity.class);
        clearTaskIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(clearTaskIntent);
    }

    private void handleError(String msg, int status) {
        if (!isFinishing()) {
            View view = findViewById(R.id.copy_answer_launcher_root_view);

            if (!TextUtils.isEmpty(msg)) {
                CustomSnackBar.make(view, msg, CustomSnackBar.DURATION_LONG).show();
            } else {
                switch (status) {
                    case Constants.DataStatus.NO_INTERNET:
                        CustomSnackBar.make(view, Constants.Alerts.NO_NETWORK_CONNECTION, CustomSnackBar.DURATION_LONG).show();
                        break;

                    default:
                        CustomSnackBar.make(view, Constants.Alerts.SOMETHING_WRONG, CustomSnackBar.DURATION_LONG).show();
                        break;
                }
            }
        }
    }
}
