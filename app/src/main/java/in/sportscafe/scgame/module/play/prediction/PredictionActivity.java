package in.sportscafe.scgame.module.play.prediction;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.play.prediction.dto.Question;
import in.sportscafe.scgame.module.play.tindercard.SwipeFlingAdapterView;

public class PredictionActivity extends ScGameActivity implements PredictionView, View.OnClickListener {

    private SwipeFlingAdapterView mSwipeFlingAdapterView;

    private PredictionPresenter mPredictionPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        mSwipeFlingAdapterView = (SwipeFlingAdapterView) findViewById(R.id.activity_prediction_swipe);
        mSwipeFlingAdapterView.setScrollListener(new SwipeFlingAdapterView.OnScrollListener() {
            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = mSwipeFlingAdapterView.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);
            }
        });

        this.mPredictionPresenter = PredictionPresenterImpl.newInstance(this);
        this.mPredictionPresenter.onCreatePrediction(getIntent().getExtras());
    }

    @Override
    public void setTournamentName(String tournamentName) {
        ((TextView) findViewById(R.id.prediction_tv_tournament)).setText(tournamentName);
    }

    @Override
    public void setContestName(String contestName) {
        ((TextView) findViewById(R.id.prediction_tv_contest_name)).setText(contestName);
    }

    @Override
    public void setAdapter(PredictionAdapter predictionAdapter,
                           SwipeFlingAdapterView.OnSwipeListener<Question> swipeListener) {
        mSwipeFlingAdapterView.setAdapter(predictionAdapter);
        mSwipeFlingAdapterView.setSwipeListener(swipeListener);
    }

    @Override
    public void hidePass() {
        findViewById(R.id.prediction_btn_pass).setVisibility(View.INVISIBLE);
    }

    @Override
    public void showNoNegativeAlert() {
        showAlertMessage("No negatives, So make a guess!");
    }

    @Override
    public void showLastQuestionAlert() {
        showAlertMessage("No use to pass the last question!");
    }

    private void showAlertMessage(String message) {
        TextView textView = (TextView) findViewById(R.id.prediction_tv_message);
        textView.setText(message);
        textView.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigateToResult(Bundle bundle) {
        onBackPressed();
    }

    @Override
    public void swipeCardToTop() {
        mSwipeFlingAdapterView.getTopCardListener().selectTop();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prediction_btn_pass:
                mSwipeFlingAdapterView.getTopCardListener().selectBottom();
                break;
        }
    }
}