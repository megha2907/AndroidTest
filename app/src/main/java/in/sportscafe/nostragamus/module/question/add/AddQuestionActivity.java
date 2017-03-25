package in.sportscafe.nostragamus.module.question.add;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;

import in.sportscafe.nostragamus.Constants;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        initToolBar();

        mScrollMain = (ScrollView) findViewById(R.id.add_question_scrollview);
        mEtQuestion = (EditText) findViewById(R.id.add_question_et_question);
        mEtContext = (EditText) findViewById(R.id.add_question_et_context);
        mEtLeftOption = (EditText) findViewById(R.id.add_question_et_left_option);
        mEtRightOption = (EditText) findViewById(R.id.add_question_et_right_option);
        mEtNeitherOption = (EditText) findViewById(R.id.add_question_et_neither_option);

        this.mAddQuestionPresenter = AddQuestionPresenterImpl.newInstance(this);
        this.mAddQuestionPresenter.onCreateAddQuestion(getIntent().getExtras());
        mEtContext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mScrollMain.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
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