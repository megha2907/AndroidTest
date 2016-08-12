package in.sportscafe.scgame.module.user.sportselection;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.ScGameActivity;
import in.sportscafe.scgame.module.common.SpacesItemDecoration;
import in.sportscafe.scgame.module.home.HomeActivity;
import in.sportscafe.scgame.module.user.login.LogInActivity;

/**
 * Created by Jeeva on 27/5/16.
 */
public class SportSelectionActivity extends ScGameActivity implements SportSelectionView,
        View.OnClickListener {

    private RecyclerView mRvSportSelection;

    private SportSelectionPresenter mSportSelectionPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_selection);

        this.mRvSportSelection = (RecyclerView) findViewById(R.id.sport_selection_rcv);
        this.mRvSportSelection.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_10)));
        this.mRvSportSelection.setLayoutManager(new GridLayoutManager(this, 3));
        this.mRvSportSelection.setHasFixedSize(true);
        this.mSportSelectionPresenter = SportSelectionPresenterImpl.newInstance(this);
        this.mSportSelectionPresenter.onCreateSportSelection(getIntent().getExtras());
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        this.mRvSportSelection.setAdapter(adapter);
    }

    @Override
    public void navigateToLogin() {
        startActivity(new Intent(this, LogInActivity.class));
        finish();
    }

    @Override
    public void navigateToHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void goBackWithSuccessResult() {
        setResult(RESULT_OK);
        onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sport_selection_btn_next:
                mSportSelectionPresenter.onClickNext();
                break;
            case R.id.sport_selection_btn_back:
                finish();
                break;
        }
    }
}