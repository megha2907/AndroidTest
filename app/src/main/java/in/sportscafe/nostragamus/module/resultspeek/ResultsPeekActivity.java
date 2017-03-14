package in.sportscafe.nostragamus.module.resultspeek;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.RoundImage;

/**
 * Created by deepanshi on 2/16/17.
 */

public class ResultsPeekActivity extends NostragamusActivity implements ResultsPeekView, View.OnClickListener {

    private ResultsPeekPresenter mPlayerComparisonPresenter;
    private RecyclerView mRvResultsComp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_peek);

        this.mRvResultsComp = (RecyclerView) findViewById(R.id.results_compare_rcv);
        this.mRvResultsComp.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        this.mRvResultsComp.setHasFixedSize(true);
        this.mPlayerComparisonPresenter = ResultsPeekPresenterImpl.newInstance(this);
        this.mPlayerComparisonPresenter.onCreateResultsPeek(getIntent().getExtras());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.results_peek_btn_back:
                finish();
                break;
        }
    }

    @Override
    public void setName(String userName, String playerName) {
        TextView tvUserName = (TextView) findViewById(R.id.player_comparison_user_tv_title);
        TextView tvPlayerName = (TextView) findViewById(R.id.player_comparison_other_player_name);
        tvUserName.setText(userName);
        tvPlayerName.setText(playerName);
    }

    @Override
    public void setProfileImage(String userImageUrl, String playerImageUrl) {
        ((RoundImage) findViewById(R.id.player_comparison_user_iv_image)).setImageUrl(
                userImageUrl
        );
        ((RoundImage) findViewById(R.id.player_comparison_other_player_image)).setImageUrl(
                playerImageUrl
        );
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        this.mRvResultsComp.setAdapter(adapter);
    }

    @Override
    public void setPointsAndMatch(Integer myPoints, Integer playerPoints, String matchStage, String challengeName) {
        Button btnUserPoints = (Button) findViewById(R.id.player_comparison_user_btn_points);
        Button btnPlayerPoints = (Button) findViewById(R.id.player_comparison_player_btn_points);
        TextView tvMatchStage = (TextView) findViewById(R.id.results_peek_tv_match_name);
        TextView tvChallengeName = (TextView) findViewById(R.id.results_peek_tv_challenge_name);
        tvMatchStage.setText(matchStage);
        tvChallengeName.setText(challengeName);
        if (null==myPoints) {
            btnUserPoints.setText("0");
        }else {
            btnUserPoints.setText(myPoints.toString());
        }
        btnPlayerPoints.setText(playerPoints.toString());
    }

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.RESULTS_PEEK;
    }
}