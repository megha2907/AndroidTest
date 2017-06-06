package in.sportscafe.nostragamus.module.navigation.submitquestion.add;

import android.os.Bundle;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.Constants.DateFormats;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.feed.dto.Parties;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by Jeeva on 24/03/17.
 */
public class AddQuestionPresenterImpl implements AddQuestionPresenter, AddQuestionModelImpl.OnAddQuestionModelListener {

    private AddQuestionView mAddQuestionView;

    private AddQuestionModel mAddQuestionModel;

    private AddQuestionPresenterImpl(AddQuestionView view) {
        this.mAddQuestionView = view;
        this.mAddQuestionModel = AddQuestionModelImpl.newInstance(this);
    }

    public static AddQuestionPresenter newInstance(AddQuestionView view) {
        return new AddQuestionPresenterImpl(view);
    }

    @Override
    public void onCreateAddQuestion(Bundle bundle) {
        mAddQuestionModel.init(bundle);
        populateMatchDetails(mAddQuestionModel.getMatchDetails());
    }

    @Override
    public void onClickAdd(String question, String context, String leftOption, String rightOption, String neitherOption) {
        mAddQuestionModel.saveQuestion(question, context, leftOption, rightOption, neitherOption);
    }

    @Override
    public void onApiCallStarted() {
        mAddQuestionView.showProgressbar();
    }

    @Override
    public boolean onApiCallStopped() {
        return mAddQuestionView.dismissProgressbar();
    }

    @Override
    public void onNeedMoreCharForQuestion() {
        mAddQuestionView.showMessage(Alerts.QUESTION_LIMIT_ALERT);
    }

    @Override
    public void onNeedMoreCharForContext() {
        mAddQuestionView.showMessage(Alerts.CONTEXT_LIMIT_ALERT);
    }

    @Override
    public void onEmptyLeftOption() {
        mAddQuestionView.showMessage(Alerts.LEFT_OPTION_EMPTY);
    }

    @Override
    public void onEmptyRightOption() {
        mAddQuestionView.showMessage(Alerts.RIGHT_OPTION_EMPTY);
    }

    @Override
    public void onNoInternet() {
        mAddQuestionView.showMessage(Alerts.NO_NETWORK_CONNECTION);
    }

    @Override
    public void onQuestionSaveSuccess() {
        mAddQuestionView.showMessage(Alerts.SUBMIT_QUESTION_SUCCESS);
        mAddQuestionView.goBack();
    }

    @Override
    public void onQuestionSaveFailed() {
        mAddQuestionView.showMessage(Alerts.API_FAIL);
    }

    private void populateMatchDetails(Match match) {
        String startTime = match.getStartTime().replace("+00:00", ".000Z");
        long startTimeMs = TimeUtils.getMillisecondsFromDateString(
                startTime,
                DateFormats.FORMAT_DATE_T_TIME_ZONE,
                DateFormats.GMT
        );

        int dayOfMonth = Integer.parseInt(TimeUtils.getDateStringFromMs(startTimeMs, "d"));
        // Setting date of the match
        mAddQuestionView.setMatchDate(dayOfMonth + AppSnippet.ordinalOnly(dayOfMonth) + " " +
                TimeUtils.getDateStringFromMs(startTimeMs, "MMM") + ", "
                + TimeUtils.getDateStringFromMs(startTimeMs, DateFormats.HH_MM_AA)
        );

        List<Parties> parties = match.getParties();
        mAddQuestionView.setPartyAName(parties.get(0).getPartyName());
        mAddQuestionView.setPartyBName(parties.get(1).getPartyName());
        mAddQuestionView.setPartyAPhoto(parties.get(0).getPartyImageUrl());
        mAddQuestionView.setPartyBPhoto(parties.get(1).getPartyImageUrl());
        mAddQuestionView.setMatchStage(match.getStage());
    }
}