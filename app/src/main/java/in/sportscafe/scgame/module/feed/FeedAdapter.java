package in.sportscafe.scgame.module.feed;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.Adapter;
import in.sportscafe.scgame.module.feed.dto.Feed;
import in.sportscafe.scgame.module.feed.dto.Tournament;
import in.sportscafe.scgame.module.feed.dto.Match;
import in.sportscafe.scgame.module.home.HomeActivity;
import in.sportscafe.scgame.module.home.OnHomeActionListener;
import in.sportscafe.scgame.utils.ViewUtils;
import in.sportscafe.scgame.utils.timeutils.TimeUtils;

/**
 * Created by Jeeva on 15/6/16.
 */
public class FeedAdapter extends Adapter<Feed, FeedAdapter.ViewHolder> {

    private OnHomeActionListener mOnHomeActionListener;

    private AlertDialog mAlertDialog;
    private Context mcon;

    public FeedAdapter(Context context, OnHomeActionListener listener) {
        super(context);
        mcon = context;
        this.mOnHomeActionListener = listener;
    }

    @Override
    public Feed getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_feed_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Feed feed = getItem(position);
        holder.mPosition = position;

        holder.mTvDate.setText(TimeUtils.getDateStringFromMs(feed.getDate(), "d'th' MMM"));
        holder.mLlTourParent.removeAllViews();
        for (Tournament tournament : feed.getTournaments()) {
            holder.mLlTourParent.addView(getTourView(tournament, holder.mLlTourParent));
        }
    }

    private View getTourView(Tournament tournament, ViewGroup parent) {
        View tourView = getLayoutInflater().inflate(R.layout.inflater_tour_row, parent, false);
        TourViewHolder holder = new TourViewHolder(tourView);

        holder.mTvTournamentName.setText(tournament.getTournamentName());

        holder.mLlScheduleParent.removeAllViews();
        for (Match match : tournament.getMatches()) {
            holder.mLlScheduleParent.addView(getScheduleView(match, holder.mLlScheduleParent));
        }

        return tourView;
    }

    private View getScheduleView(Match match, ViewGroup parent) {
        View scheduleView = getLayoutInflater().inflate(R.layout.inflater_schedule_row, parent, false);
        ScheduleViewHolder holder = new ScheduleViewHolder(scheduleView);

        holder.mTvMatchStage.setText(match.getStage());

        String[] parties = match.getParties().split(" vs ");
        holder.mTvPartyAName.setText(parties[0]);
        holder.mTvPartyBName.setText(parties[1]);

        if (null == match.getResult()) {
            holder.mTvMatchResult.setVisibility(View.GONE);
            holder.mTvPartyAScore.setVisibility(View.GONE);
            holder.mTvPartyBScore.setVisibility(View.GONE);
            holder.mTvStartTime.setVisibility(View.VISIBLE);

            holder.mTvStartTime.setText(TimeUtils.getFormattedDateString(
                    match.getStartTime(), Constants.DateFormats.HH_MM_AA,
                    Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE, Constants.DateFormats.GMT));
        } else {
            holder.mTvMatchResult.setVisibility(View.VISIBLE);
            holder.mTvPartyAScore.setVisibility(View.VISIBLE);
            holder.mTvPartyBScore.setVisibility(View.VISIBLE);
            holder.mTvStartTime.setVisibility(View.GONE);

            holder.mTvMatchResult.setText(match.getResult());
        }

        return scheduleView;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        int mPosition;

        TextView mTvDate;

        private LinearLayout mLlTourParent;

        public ViewHolder(View V) {
            super(V);

            mTvDate = (TextView) V.findViewById(R.id.feed_row_tv_date);
            mLlTourParent = (LinearLayout) V.findViewById(R.id.feed_row_ll_tour_parent);
        }
    }

    class TourViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTvTournamentName;

        LinearLayout mLlScheduleParent;

        public TourViewHolder(View V) {
            super(V);

            mTvTournamentName = (TextView) V.findViewById(R.id.tour_row_tv_tour_name);
            mLlScheduleParent = (LinearLayout) V.findViewById(R.id.tour_row_ll_schedule_parent);

            V.findViewById(R.id.tour_row_ibtn_options).setOnClickListener(this);
            V.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tour_row_ibtn_options:
                    showOptions(view.getContext());
                    break;
            }
        }
    }

    class ScheduleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTvMatchStage;

        TextView mTvPartyAName;

        TextView mTvPartyAScore;

        TextView mTvPartyBName;

        TextView mTvPartyBScore;

        TextView mTvMatchResult;

        TextView mTvStartTime;

        public ScheduleViewHolder(View V) {
            super(V);

            mTvMatchStage = (TextView) V.findViewById(R.id.schedule_row_tv_match_stage);
            mTvPartyAName = (TextView) V.findViewById(R.id.schedule_row_tv_party_a_name);
            mTvPartyAScore = (TextView) V.findViewById(R.id.schedule_row_tv_party_a_score);
            mTvPartyBName = (TextView) V.findViewById(R.id.schedule_row_tv_party_b_name);
            mTvPartyBScore = (TextView) V.findViewById(R.id.schedule_row_tv_party_b_score);
            mTvMatchResult = (TextView) V.findViewById(R.id.schedule_row_tv_match_result);
            mTvStartTime = (TextView) V.findViewById(R.id.schedule_row_tv_start_time);
            V.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnHomeActionListener.onClickPlay();
        }
    }

    private void showOptions(Context context) {
        if (null == mAlertDialog) {
            mAlertDialog = ViewUtils.getDialogList(context, getOptions(),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAlertDialog.dismiss();
                        }
                    });
        }
        mAlertDialog.show();
    }

    private List<String> getOptions() {
        List<String> options = new ArrayList<>();
        options.add("Option 1");
        options.add("Option 2");
        options.add("Option 3");
        return options;
    }
}