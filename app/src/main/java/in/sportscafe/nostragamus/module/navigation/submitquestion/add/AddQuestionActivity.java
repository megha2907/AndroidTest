package in.sportscafe.nostragamus.module.navigation.submitquestion.add;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import in.sportscafe.nostragamus.Constants.ScreenNames;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;

/**
 * Created by Jeeva on 24/03/17.
 */
public class AddQuestionActivity extends NostragamusActivity implements AddQuestionView {

    private EditText mEtQuestion;

    private EditText mEtContext;

    private EditText mEtLeftOption;

    private EditText mEtRightOption;

    private EditText mEtNeitherOption;

    private ScrollView mScrollMain;

    private AddQuestionPresenter mAddQuestionPresenter;

    @Override
    public String getScreenName() {
        return ScreenNames.QUESTION_ADD;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.black);

        setContentView(R.layout.activity_add_question);

        initToolBar();

        mScrollMain = (ScrollView) findViewById(R.id.add_question_scrollview);
        mEtQuestion = (EditText) findViewById(R.id.add_question_et_question);
        mEtContext = (EditText) findViewById(R.id.add_question_et_context);
        mEtLeftOption = (EditText) findViewById(R.id.add_question_et_left_option);
        mEtRightOption = (EditText) findViewById(R.id.add_question_et_right_option);
        mEtNeitherOption = (EditText) findViewById(R.id.add_question_et_neither_option);

        mEtQuestion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mScrollMain.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        mEtContext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mScrollMain.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        this.mAddQuestionPresenter = AddQuestionPresenterImpl.newInstance(this);
        this.mAddQuestionPresenter.onCreateAddQuestion(getIntent().getExtras());
    }

    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_question_toolbar);
        toolbar.setTitle("Submit Question");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_icon_grey);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }

    @Override
    public void goBack() {
        onBackPressed();
    }

    @Override
    public void setMatchDate(String matchDate) {
        ((TextView) findViewById(R.id.schedule_row_tv_date)).setText(matchDate);
    }

    @Override
    public void setPartyAName(String name) {
        ((TextView) findViewById(R.id.schedule_row_tv_party_a_name)).setText(name);
    }

    @Override
    public void setPartyBName(String name) {
        ((TextView) findViewById(R.id.schedule_row_tv_party_b_name)).setText(name);
    }

    @Override
    public void setPartyAPhoto(String imageUrl) {
        ((HmImageView) findViewById(R.id.swipe_card_iv_left)).setImageUrl(imageUrl);
    }

    @Override
    public void setPartyBPhoto(String imageUrl) {
        ((HmImageView) findViewById(R.id.swipe_card_iv_right)).setImageUrl(imageUrl);
    }

    @Override
    public void setMatchStage(String stage) {
        ((TextView) findViewById(R.id.schedule_row_tv_match_result)).setText(stage);
    }

    public void onClickSave(View view) {
        mAddQuestionPresenter.onClickAdd(
                getTrimmedText(mEtQuestion),
                getTrimmedText(mEtContext),
                getTrimmedText(mEtLeftOption),
                getTrimmedText(mEtRightOption),
                getTrimmedText(mEtNeitherOption)
        );
    }
}