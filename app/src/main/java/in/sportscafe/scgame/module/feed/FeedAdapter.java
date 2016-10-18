package in.sportscafe.scgame.module.feed;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.volley.Volley;
import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.Adapter;
import in.sportscafe.scgame.module.feed.dto.Feed;
import in.sportscafe.scgame.module.TournamentFeed.dto.Tournament;
import in.sportscafe.scgame.module.feed.dto.Match;
import in.sportscafe.scgame.module.play.PlayActivity;
import in.sportscafe.scgame.module.play.myresults.MyResultsActivity;
import in.sportscafe.scgame.module.play.prediction.PredictionActivity;
import in.sportscafe.scgame.utils.ViewUtils;
import in.sportscafe.scgame.utils.timeutils.TimeAgo;
import in.sportscafe.scgame.utils.timeutils.TimeUnit;
import in.sportscafe.scgame.utils.timeutils.TimeUtils;

/**
 * Created by Jeeva on 15/6/16.
 */
public class FeedAdapter extends Adapter<Feed, FeedAdapter.ViewHolder> {

    private AlertDialog mAlertDialog;
    private Context mcon;
    private List<Match> mMyResultList = new ArrayList<>();

    public FeedAdapter(Context context) {
        super(context);
        mcon = context;
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
            holder.mLlTourParent.addView(getTourView(tournament, holder.mLlTourParent,position));
        }
    }

    private View getTourView(Tournament tournament, ViewGroup parent,int position) {
        View tourView = getLayoutInflater().inflate(R.layout.inflater_tour_row, parent, false);
        TourViewHolder holder = new TourViewHolder(tourView);

        holder.mTvTournamentName.setText(tournament.getTournamentName());

        holder.mLlScheduleParent.removeAllViews();
        for (Match match : tournament.getMatches()) {
            holder.mLlScheduleParent.addView(getScheduleView(match, holder.mLlScheduleParent,position));
        }

        return tourView;
    }

    private View getScheduleView(Match match, ViewGroup parent,int position) {
        View scheduleView = getLayoutInflater().inflate(R.layout.inflater_schedule_row, parent, false);
        ScheduleViewHolder holder = new ScheduleViewHolder(scheduleView);

        if( null == match.getParties())
        {
            holder.mTvPartyAName.setVisibility(View.GONE);
            holder.mTvPartyBName.setVisibility(View.GONE);
            holder.mIvPartyAPhoto.setVisibility(View.GONE);
            holder.mIvPartyBPhoto.setVisibility(View.GONE);
            holder.mBtnVs.setVisibility(View.GONE);
            holder.mTvMatchStage.setVisibility(View.GONE);
            holder.mRlMatchStageParent.setVisibility(View.GONE);
            holder.mTvStartTime.setVisibility(View.GONE);
            holder.mViewResult.setVisibility(View.GONE);

        }
        else
        {
            holder.mTvPartyAName.setText(match.getParties().get(0).getPartyName());
            holder.mTvPartyBName.setText(match.getParties().get(1).getPartyName());

            holder.mIvPartyAPhoto.setImageUrl(
                    match.getParties().get(0).getPartyImageUrl(),
                    Volley.getInstance().getImageLoader(),
                    false
            );

            holder.mIvPartyBPhoto.setImageUrl(
                    match.getParties().get(1).getPartyImageUrl(),
                    Volley.getInstance().getImageLoader(),
                    false
            );
        }


        if(null == match.getStage()) {
            holder.mTvMatchStage.setVisibility(View.GONE);
        } else {
            holder.mTvMatchStage.setText(match.getStage());
        }


        if (null == match.getResult() || match.getResult().isEmpty()) {
            holder.mTvMatchResult.setVisibility(View.GONE);
            holder.mTvStartTime.setText(TimeUtils.getFormattedDateString(
                    match.getStartTime(), Constants.DateFormats.HH_MM_AA,
                    Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE, Constants.DateFormats.GMT));
        } else {
            holder.mTvMatchResult.setVisibility(View.VISIBLE);
            holder.mTvMatchResult.setText(match.getResult());
        }



        if (match.getMatchPoints()==0)
        {
            holder.mBtnMatchPoints.setVisibility(View.GONE);
            holder.mTvResultCorrectCount.setVisibility(View.GONE);
        }
        else
        {
            holder.mBtnMatchPoints.setVisibility(View.VISIBLE);
            holder.mTvResultCorrectCount.setVisibility(View.VISIBLE);
            holder.mViewResult.setVisibility(View.VISIBLE);
            holder.mTvResultWait.setVisibility(View.GONE);

            holder.mBtnMatchPoints.setText(match.getMatchPoints()+" Points");
            holder.mBtnMatchPoints.setTag(match.getId());
            holder.mTvResultCorrectCount.setText("You got "+ match.getCorrectCount()+"/"+match.getMatchQuestionCount() +" questions correct");

        }


        Calendar c = Calendar.getInstance();
        Long formattedCurrentDate = c.getTimeInMillis();
        String date = TimeUtils.getFormattedDateString(
                match.getStartTime(),
                Constants.DateFormats.DD_MM_YYYY,
                Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                Constants.DateFormats.GMT
        );

        Long formattedstartDate = TimeUtils.getMillisecondsFromDateString(
                date,
                Constants.DateFormats.DD_MM_YYYY,
                Constants.DateFormats.GMT
        );


         TimeAgo timeAgo = TimeUtils.calcTimeAgo(formattedCurrentDate,formattedstartDate);

         if(match.getMatchQuestionCount()==0 && match.getParties() != null && timeAgo.timeDiff > 0 && timeAgo.timeUnit != TimeUnit.MILLISECOND && timeAgo.timeUnit != TimeUnit.SECOND)
         {
             holder.mTvResultWait.setVisibility(View.VISIBLE);
             holder.mTvResultWait.setText("Coming up");
             holder.mViewResult.setVisibility(View.GONE);
         }

        if(match.getMatchQuestionCount()==0)
        {
            holder.mBtnPlayMatch.setVisibility(View.GONE);
        }
        else if(match.getisAttempted()==true)
        {
            holder.mTvMatchResult.setVisibility(View.GONE);
            holder.mBtnPlayMatch.setVisibility(View.GONE);
            holder.mTvResultWait.setVisibility(View.VISIBLE);
            holder.mViewResult.setVisibility(View.VISIBLE);
            holder.mTvResultWait.setText(match.getMatchQuestionCount()+" predictions made, waiting for results");
        }
        else
        {
            holder.mBtnPlayMatch.setVisibility(View.VISIBLE);
            holder.mBtnPlayMatch.setTag(match);
        }

        return scheduleView;
    }



    private View getMatchCommentary(Match match2, ViewGroup parent) {
        View MatchCommentary = getLayoutInflater().inflate(R.layout.inflater_match_commentary_row, parent, false);
        MatchCommentaryHolder holder = new MatchCommentaryHolder(MatchCommentary);

        holder.mTvMatchCommentary.setText(match2.getVenue()); //// TODO: 9/22/16 change to match commentry
        
        return MatchCommentary;
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

        TextView mTvResultCorrectCount;

        TextView mTvResultWait;

        TextView mTvStartTime;

        HmImageView mIvPartyAPhoto;

        HmImageView mIvPartyBPhoto;

        Button mBtnVs;

        CustomButton mBtnMatchPoints;

        CustomButton mBtnPlayMatch;

        View mViewResult;

        LinearLayout mLlMatchCommentaryParent;

        RelativeLayout mRlMatchStageParent;


        public ScheduleViewHolder(View V) {
            super(V);

            mTvMatchStage = (TextView) V.findViewById(R.id.schedule_row_tv_match_stage);
            mTvPartyAName = (TextView) V.findViewById(R.id.schedule_row_tv_party_a_name);
            mTvPartyBName = (TextView) V.findViewById(R.id.schedule_row_tv_party_b_name);
            mTvMatchResult = (TextView) V.findViewById(R.id.schedule_row_tv_match_result);
            mTvStartTime = (TextView) V.findViewById(R.id.schedule_row_tv_match_time);
            mTvResultCorrectCount = (TextView) V.findViewById(R.id.schedule_row_tv_match_correct_questions);
            mTvResultWait = (TextView) V.findViewById(R.id.schedule_row_tv_match_result_wait);
            mIvPartyAPhoto=(HmImageView) V.findViewById(R.id.swipe_card_iv_left);
            mIvPartyBPhoto=(HmImageView) V.findViewById(R.id.swipe_card_iv_right);
            mBtnVs=(Button) V.findViewById(R.id.schedule_row_btn_vs);
            mBtnPlayMatch=(CustomButton) V.findViewById(R.id.schedule_row_btn_playmatch);
            mBtnMatchPoints=(CustomButton) V.findViewById(R.id.schedule_row_btn_points);
            mViewResult=(View) V.findViewById(R.id.schedule_row_v_party_a);
            mLlMatchCommentaryParent = (LinearLayout) V.findViewById(R.id.schedule_row_ll_match_commentary_parent);
            mRlMatchStageParent = (RelativeLayout) V.findViewById(R.id.schedule_row_rl_match_stage);
            mBtnPlayMatch.setOnClickListener(this);
            mBtnMatchPoints.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.schedule_row_btn_playmatch:

                    Match match = (Match)view.getTag();
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(Constants.BundleKeys.MATCH_LIST, match);

                    Intent intent =  new Intent(mcon, PredictionActivity.class);
                    intent.putExtras(mBundle);
                    mcon.startActivity(intent);
                    break;

                case R.id.schedule_row_btn_points:

                    Integer matchId = (Integer) view.getTag();
                    Bundle mBundle2 = new Bundle();
                    mBundle2.putInt(Constants.BundleKeys.MATCH_ID, matchId);

                    Intent mintent =  new Intent(mcon, MyResultsActivity.class);
                    mintent.putExtras(mBundle2);
                    mcon.startActivity(mintent);
                    break;
            }


        }
    }

    class MatchCommentaryHolder extends RecyclerView.ViewHolder {

        TextView mTvMatchCommentary;

        public MatchCommentaryHolder(View V) {
            super(V);

            mTvMatchCommentary = (TextView) V.findViewById(R.id.match_commentary_tv);

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