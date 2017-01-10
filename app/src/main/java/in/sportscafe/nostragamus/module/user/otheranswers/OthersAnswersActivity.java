package in.sportscafe.nostragamus.module.user.otheranswers;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;

/**
 * Created by deepanshu on 4/10/16.
 */

public class OthersAnswersActivity extends NostragamusActivity implements OthersAnswersView {

    private RecyclerView mRecyclerView;

    private EditText mEtSearch;

    private OthersAnswersPresenter mOthersAnswersPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_answers);

        mRecyclerView = (RecyclerView) findViewById(R.id.others_answers_rcv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        mEtSearch = ((EditText) findViewById(R.id.others_answers_et_search));
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mOthersAnswersPresenter.filterSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        this.mOthersAnswersPresenter = OthersAnswersPresenterImpl.newInstance(this);
        mOthersAnswersPresenter.onCreateOthersAnswers(getIntent().getExtras());
    }

    @Override
    public void setAdapter(FuzzyPlayerAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }
}