package in.sportscafe.scgame.module.play.prediction;

import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.customfont.CustomButton;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.play.prediction.dto.Question;
import in.sportscafe.scgame.module.play.tindercard.SwipeFlingAdapterView;

public class PredictionActivity extends ScGameActivity implements PredictionView, View.OnClickListener, View.OnTouchListener, View.OnDragListener,PredictionAdapter.OnPredictionTimerListener {

    private SwipeFlingAdapterView mSwipeFlingAdapterView;

    private PredictionPresenter mPredictionPresenter;
    private boolean mpowerUpApplied = false;

    private PredictionModel mPredictionModel;

    CustomButton btnpowerUpCount;
    RelativeLayout rlPowerUp;



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


        //rlPowerUp = (RelativeLayout) findViewById(R.id.rl_powerup);
        CustomButton btnquestionValue = (CustomButton)findViewById(R.id.swipe_card_question_value);
        btnpowerUpCount = (CustomButton) findViewById(R.id.swipe_card_tv_powerup_count);

        btnquestionValue.setOnTouchListener(this);
        mSwipeFlingAdapterView.setOnDragListener(this);

        int powerUps = ScGameDataHandler.getInstance().getNumberofPowerups();
        btnpowerUpCount.setText(String.valueOf(powerUps));

        if (powerUps < 1) {
            mpowerUpApplied = true;
        }


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
    public boolean onTouch(View v, MotionEvent arg1) {
        if(!mpowerUpApplied) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadow = new View.DragShadowBuilder(v);
            v.startDrag(data, shadow, null, 0);
        }
        return false;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        final int action = dragEvent.getAction();
        switch(action) {

            case DragEvent.ACTION_DRAG_STARTED:
                break;

            case DragEvent.ACTION_DRAG_EXITED:
                break;

            case DragEvent.ACTION_DRAG_ENTERED:
                break;

            case DragEvent.ACTION_DROP:
                break;

            case DragEvent.ACTION_DRAG_ENDED:
                if(!mpowerUpApplied) {
                    if (ScGameDataHandler.getInstance().getNumberofPowerups() > 0) {
                        mpowerUpApplied = true;
                        mPredictionPresenter.onPowerUp();
                        ScGameDataHandler.getInstance().setNumberofPowerups(ScGameDataHandler.getInstance().getNumberofPowerups() - 1);
                        String UpdatedPowerUps = String.valueOf(ScGameDataHandler.getInstance().getNumberofPowerups());
                        btnpowerUpCount.setText(UpdatedPowerUps);
                    }
                }
                break;
        }
        return true;
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


    @Override
    public void onTimeUp() {

    }

    @Override
    public void dismissPowerUp() {
        mpowerUpApplied=false;
    }


}