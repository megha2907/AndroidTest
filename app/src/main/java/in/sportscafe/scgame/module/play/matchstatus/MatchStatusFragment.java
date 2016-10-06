package in.sportscafe.scgame.module.play.matchstatus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.volley.Volley;
import com.jeeva.android.widgets.HmImageView;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.ScGameFragment;
import in.sportscafe.scgame.module.play.PlayActivity;
import in.sportscafe.scgame.module.play.prediction.PredictionActivity;
import in.sportscafe.scgame.utils.timeutils.TimeAgo;
import in.sportscafe.scgame.utils.timeutils.TimeUtils;

public class MatchStatusFragment extends ScGameFragment implements MatchStatusView, View.OnClickListener {

    private MatchStatusPresenter mMatchStatusPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_match_status, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setClickListeners();

        this.mMatchStatusPresenter = MatchStatusPresenterImpl.newInstance(this);
        this.mMatchStatusPresenter.onCreateMatchStatus(getArguments());
    }

    private void setClickListeners() {
        findViewById(R.id.match_status_ib_play).setOnClickListener(this);
    }

    @Override
    public void setTournament(String tournament) {
        TextView textView = (TextView) findViewById(R.id.match_status_tv_tournament);
        textView.setText(tournament);
    }

    @Override
    public void setStartTime(String startTime) {
        TextView textView = (TextView) findViewById(R.id.match_status_tv_start_time);
        textView.setText(TimeUtils.getFormattedDateString(
                startTime,
                Constants.DateFormats.HH_MM_AA_D_MMM,
                Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                Constants.DateFormats.GMT
        ));
    }

    @Override
    public void setTimeLeft(String startTime) {
        TextView textView = (TextView) findViewById(R.id.match_status_tv_time_left);

        TimeAgo timeLeft = TimeUtils.getTimeLeft(
                startTime,
                Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                Constants.DateFormats.GMT
        );

        if(timeLeft.timeDiff <= 0) {
            textView.setText("Match started");
        } else {
            String[] timeLeftFormat = TimeUtils.DEFAULT_TIME_AGO_FORMAT;
            timeLeftFormat[timeLeftFormat.length - 1] = "from now";
            textView.setText(TimeUtils.convertTimeAgoToString(timeLeft, timeLeftFormat));
        }
    }

    @Override
    public void setContestName(String contestName) {
        TextView textView = (TextView) findViewById(R.id.match_status_tv_contest_name);
        textView.setText(contestName);
    }

    @Override
    public void setTournamentImageTeam1(String tournamentImageTeam1) {
        HmImageView mIvPartyAPhoto=(HmImageView) findViewById(R.id.swipe_card_iv_left);
        mIvPartyAPhoto.setImageUrl(
                tournamentImageTeam1,
                Volley.getInstance().getImageLoader(),
                false
        );
    }

    @Override
    public void setTournamentImageTeam2(String tournamentImageTeam2) {
        HmImageView mIvPartyBPhoto=(HmImageView) findViewById(R.id.swipe_card_iv_right);
        mIvPartyBPhoto.setImageUrl(
                tournamentImageTeam2,
                Volley.getInstance().getImageLoader(),
                false
        );
    }

    @Override
    public void navigateToPrediction(Bundle bundle) {
        Intent intent = new Intent(getContext(), PredictionActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, PlayActivity.PLAY_REQUEST_CODE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.match_status_ib_play:
                mMatchStatusPresenter.onClickPlay();
                break;
        }
    }
}