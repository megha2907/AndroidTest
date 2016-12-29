package in.sportscafe.nostragamus.module.feed;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.Space;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.TournamentFeed.dto.Tournament;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.feed.dto.Feed;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.play.myresults.MyResultsActivity;
import in.sportscafe.nostragamus.module.play.prediction.PredictionActivity;
import in.sportscafe.nostragamus.utils.ViewUtils;
import in.sportscafe.nostragamus.utils.timeutils.TimeAgo;
import in.sportscafe.nostragamus.utils.timeutils.TimeUnit;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by Jeeva on 15/6/16.
 */
public class FeedAdapter extends Adapter<Feed, FeedAdapter.ViewHolder> {


    private AlertDialog mAlertDialog;
    private Context mcon;
    private List<Match> mMyResultList = new ArrayList<>();
    private String sportname;

    public FeedAdapter(Context context, String sportName) {
        super(context);
        mcon = context;
        sportname = sportName;
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


        int date= Integer.parseInt(TimeUtils.getDateStringFromMs(feed.getDate(), "d"));
        String newOrdinalDate= AppSnippet.ordinal(date);
        String month = TimeUtils.getDateStringFromMs(feed.getDate(), "MMM");
        String finalDate = newOrdinalDate + " " +month ;

        holder.mTvDate.setText(finalDate);
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

        Calendar c = Calendar.getInstance();
        Long formattedCurrentDate = c.getTimeInMillis();
        Date date = TimeUtils.getDateFromDateString(
                match.getStartTime(),
                Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                Constants.DateFormats.GMT
        );
        TimeAgo timeAgo = TimeUtils.calcTimeAgo(formattedCurrentDate, date.getTime());


        //Show Match Commentary
        if( null == match.getParties() || match.getParties().isEmpty())
        {
            holder.mTvPartyAName.setVisibility(View.GONE);
            holder.mTvPartyBName.setVisibility(View.GONE);
            holder.mIvPartyAPhoto.setVisibility(View.GONE);
            holder.mIvPartyBPhoto.setVisibility(View.GONE);
            holder.mTvMatchStage.setVisibility(View.GONE);
            holder.mRlMatchStageParent.setVisibility(View.GONE);
            holder.mTvStartTime.setVisibility(View.GONE);
            holder.mViewResult.setVisibility(View.GONE);
            holder.mRlMatchPoints.setVisibility(View.GONE);
            holder.mLlMatch.setBackgroundColor(Color.TRANSPARENT);
            holder.mIbfeedDotIcon.setBackgroundResource(R.drawable.round_grey_button_with_shadow);
            holder.mTvMatchResult.setGravity(Gravity.LEFT);
            holder.mLlMatch.setVisibility(View.GONE);
        }
        else
        {   //Show Match Parties
            holder.mTvPartyAName.setText(match.getParties().get(0).getPartyName());
            holder.mTvPartyBName.setText(match.getParties().get(1).getPartyName());

            holder.mIvPartyAPhoto.setImageUrl(
                    match.getParties().get(0).getPartyImageUrl()
            );

            holder.mIvPartyBPhoto.setImageUrl(
                    match.getParties().get(1).getPartyImageUrl()
            );
        }

        //Show Match Stage
        if(null == match.getStage()|| match.getStage().isEmpty()) {
            holder.mTvMatchStage.setVisibility(View.GONE);
        } else {
            holder.mTvMatchStage.setText(match.getStage());
        }


        //Show Match Result
        if (null == match.getResult() || match.getResult().isEmpty()) {

            holder.mTvMatchResult.setVisibility(View.GONE);
            holder.mTvStartTime.setText(TimeUtils.getFormattedDateString(
                    match.getStartTime(), Constants.DateFormats.HH_MM_AA,
                    Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE, Constants.DateFormats.GMT));

        } else {

            holder.mTvMatchResult.setVisibility(View.VISIBLE);
            holder.mTvMatchResult.setText(Html.fromHtml(match.getResult()));

            holder.mBtnPlayMatch.setVisibility(View.GONE);

            if (null!= match.getWinnerPartyId()){

                if (match.getWinnerPartyId()==(match.getParties().get(0).getPartyId())){
                    holder.mTvPartyAName.setTextColor(ContextCompat.getColor(scheduleView.getContext(), R.color.white));
                    holder.mTvPartyBName.setTextColor(ContextCompat.getColor(scheduleView.getContext(), R.color.textcolorlight));
                }
                else if(match.getWinnerPartyId()==(match.getParties().get(1).getPartyId())){
                    holder.mTvPartyBName.setTextColor(ContextCompat.getColor(scheduleView.getContext(), R.color.white));
                    holder.mTvPartyAName.setTextColor(ContextCompat.getColor(scheduleView.getContext(), R.color.textcolorlight));
                }

            }

            if (null != match.getResultdesc() && !match.getResultdesc().trim().isEmpty()){
                holder.mMatchResultCommentary.setVisibility(View.VISIBLE);
                holder.mMatchResultCommentary.addView(getResultCommentaryView(holder.mMatchResultCommentary,match));
            }

        }


        // if(questions exist) then check if answers submitted or not else show coming up matches
        if(match.getMatchQuestionCount() > 0){

            //if(answers submitted) then check if results published or not else show play button
            if(match.getisAttempted()==true){


                //if(results published) then show Match Points else show pending results
                if (match.getMatchPoints()!=0)
                {
                    holder.mBtnMatchPoints.setVisibility(View.VISIBLE);
                    holder.mTvResultCorrectCount.setVisibility(View.VISIBLE);
                    holder.mRlMatchPoints.setVisibility(View.VISIBLE);
                    holder.mViewResult.setVisibility(View.VISIBLE);
                    holder.mTvResultWait.setVisibility(View.GONE);

                    holder.mBtnMatchPoints.setText(match.getMatchPoints()+" Points");
                    holder.mBtnMatchPoints.setTag(match);
                    holder.mTvResultCorrectCount.setText("You got "+ match.getCorrectCount()+"/"+match.getMatchQuestionCount() +" questions correct");


                    float percent = (match.getCorrectCount() * 100.0f) / match.getMatchQuestionCount();

                    if (percent < 40.0){
                        holder.mIbfeedDotIcon.setBackgroundResource(R.drawable.round_red_button_with_shadow);
                    }
                    else if (percent >= 40.0 && percent <= 60.0){
                        holder.mIbfeedDotIcon.setBackgroundResource(R.drawable.round_blue_button_with_shadow);
                    }
                    else if (percent > 60.0){
                        holder.mIbfeedDotIcon.setBackgroundResource(R.drawable.round_green_button_with_shadow);
                    }
                    else {
                        holder.mIbfeedDotIcon.setBackgroundResource(R.drawable.round_grey_button_with_shadow);
                    }

                }
                else //show pending results
                {
                    holder.mBtnMatchPoints.setVisibility(View.GONE);
                    holder.mTvResultCorrectCount.setVisibility(View.GONE);
                    holder.mTvMatchResult.setVisibility(View.GONE);
                    holder.mBtnPlayMatch.setVisibility(View.GONE);
                    holder.mTvResultWait.setVisibility(View.VISIBLE);
                    holder.mViewResult.setVisibility(View.VISIBLE);
                    holder.mTvResultWait.setText(match.getMatchQuestionCount()+" predictions made, waiting for results");
                    holder.mTvResultWait.setTag(match);

                }

            }
            else { // check if match in future

                // CHECK IF MATCH START TIME < CURRENT TIME THEN NO PLAY BTN
                if(match.getMatchQuestionCount()!=null && timeAgo.timeDiff < 0 )
                {
                    holder.mBtnPlayMatch.setVisibility(View.GONE);
                } else //Show Play Btn
                {
                    holder.mBtnPlayMatch.setVisibility(View.VISIBLE);
                    holder.mBtnPlayMatch.setTag(match);
                    holder.mIbfeedDotIcon.setBackgroundResource(R.drawable.round_grey_button_with_shadow);
                }


            }

        }
        else{
            // FOR COMING UP MATCHES , CHECK IF MATCH TIME IS GREATER THAN THE CURRENT TIME
            if(match.getMatchQuestionCount()==0 && match.getParties() != null && timeAgo.timeDiff > 0 && timeAgo.timeUnit != TimeUnit.MILLISECOND && timeAgo.timeUnit != TimeUnit.SECOND)
            {
                Log.i("inside","coming up");
                holder.mTvResultWait.setVisibility(View.VISIBLE);
                holder.mTvResultWait.setText("Coming up");
                holder.mTvResultWait.setTextColor(ContextCompat.getColor(scheduleView.getContext(), R.color.textcolorlight));
                holder.mTvResultWait.setClickable(false);
                holder.mViewResult.setVisibility(View.GONE);
                holder.mTvResultWait.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

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

    private View getResultCommentaryView(ViewGroup parent,Match match) {


        View resultCommentaryView = getLayoutInflater().inflate(R.layout.inflater_match_result_commentary_row, parent, false);

        TextView tvcommentary = (TextView) resultCommentaryView.findViewById(R.id.schedule_row_tv_match_result_commentary);

        TextView tvcommentarytitle = (TextView) resultCommentaryView.findViewById(R.id.schedule_row_tv_match_result_commentary_title);

        if (null != match.getResultdesc() && !match.getResultdesc().trim().isEmpty()) {

            tvcommentary.setVisibility(View.VISIBLE);

            tvcommentary.setText(noTrailingwhiteLines(Html.fromHtml(match.getResultdesc().toString().replaceAll("&nbsp;",""))));
            tvcommentary.setMovementMethod(LinkMovementMethod.getInstance());

            CharSequence text = tvcommentary.getText();
            if(text instanceof Spannable){
                int end = text.length();
                Spannable sp = (Spannable)tvcommentary.getText();
                URLSpan[] urls=sp.getSpans(0, end, URLSpan.class);
                SpannableStringBuilder style=new SpannableStringBuilder(text);
                style.clearSpans();//should clear old spans
                for(URLSpan url : urls){
                    LinkSpan click = new LinkSpan(url.getURL());
                    style.setSpan(click,sp.getSpanStart(url),sp.getSpanEnd(url),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                tvcommentary.setText(style);
            }

            if (match.getMatchPoints()==0) {
                tvcommentarytitle.setVisibility(View.VISIBLE);
                tvcommentarytitle.setText(Html.fromHtml(match.getResult()));
            }
            else {
                tvcommentarytitle.setVisibility(View.GONE);
            }

            if (!match.getStage().equals("Commentary")){
                tvcommentarytitle.setVisibility(View.GONE);
            }
        }

        return resultCommentaryView;
    }

    class ScheduleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTvMatchStage;

        TextView mTvPartyAName;

        TextView mTvPartyAScore;

        TextView mTvPartyBName;

        TextView mTvPartyBScore;

        TextView mTvMatchResultCommentary;


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

        LinearLayout mLlMatch;

        RelativeLayout mRlMatchPoints;

        Button mIbfeedDotIcon;

        LinearLayout mMatchResultCommentary;

        Space viewSpace;

        public ScheduleViewHolder(View V) {
            super(V);

            mTvMatchStage = (TextView) V.findViewById(R.id.schedule_row_tv_match_stage);
            mTvPartyAName = (TextView) V.findViewById(R.id.schedule_row_tv_party_a_name);
            mTvPartyBName = (TextView) V.findViewById(R.id.schedule_row_tv_party_b_name);
            mTvMatchResult = (TextView) V.findViewById(R.id.schedule_row_tv_match_result);
            mTvMatchResultCommentary = (TextView) V.findViewById(R.id.schedule_row_tv_match_result_commentary);
            mTvStartTime = (TextView) V.findViewById(R.id.schedule_row_tv_match_time);
            mTvResultCorrectCount = (TextView) V.findViewById(R.id.schedule_row_tv_match_correct_questions);
            mTvResultWait = (TextView) V.findViewById(R.id.schedule_row_tv_match_result_wait);
            mIvPartyAPhoto=(HmImageView) V.findViewById(R.id.swipe_card_iv_left);
            mIvPartyBPhoto=(HmImageView) V.findViewById(R.id.swipe_card_iv_right);
            mBtnPlayMatch=(CustomButton) V.findViewById(R.id.schedule_row_btn_playmatch);
            mIbfeedDotIcon=(Button) V.findViewById(R.id.feed_dot_icon);
            mBtnMatchPoints=(CustomButton) V.findViewById(R.id.schedule_row_btn_points);
            mLlMatch = (LinearLayout) V.findViewById(R.id.schedule_row_ll);
            mViewResult=(View) V.findViewById(R.id.schedule_row_v_party_a);
            mLlMatchCommentaryParent = (LinearLayout) V.findViewById(R.id.schedule_row_ll_match_commentary_parent);
            mRlMatchStageParent = (RelativeLayout) V.findViewById(R.id.schedule_row_rl_match_stage);
            mRlMatchPoints = (RelativeLayout) V.findViewById(R.id.rl_points);
            mMatchResultCommentary = (LinearLayout) V.findViewById(R.id.schedule_row_ll_match_commentary_parent);
            viewSpace=(Space)V.findViewById(R.id.space);

            mBtnPlayMatch.setOnClickListener(this);
            mBtnMatchPoints.setOnClickListener(this);
            mTvResultWait.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.schedule_row_btn_playmatch:

                    Match match = (Match)view.getTag();
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(Constants.BundleKeys.MATCH_LIST, match);
                    mBundle.putString(Constants.BundleKeys.SPORT_NAME,sportname);

                    Intent intent =  new Intent(view.getContext(), PredictionActivity.class);
                    intent.putExtras(mBundle);
                    view.getContext().startActivity(intent);
                    break;

                case R.id.schedule_row_btn_points:

                    Match match2 = (Match) view.getTag();
                    Bundle mBundle2 = new Bundle();
                    mBundle2.putSerializable(Constants.BundleKeys.MATCH_LIST, match2);
                    mBundle2.putString("screen","feed");
                    Intent mintent =  new Intent(view.getContext(), MyResultsActivity.class);
                    mintent.putExtras(mBundle2);
                    view.getContext().startActivity(mintent);
                    break;

                case R.id.schedule_row_tv_match_result_wait:

                    Match match3 = (Match) view.getTag();
                    Bundle mBundle3 = new Bundle();
                    mBundle3.putSerializable(Constants.BundleKeys.MATCH_LIST, match3);
                    mBundle3.putString("screen","feed");
                    Intent mintent2 =  new Intent(view.getContext(), MyResultsActivity.class);
                    mintent2.putExtras(mBundle3);
                    view.getContext().startActivity(mintent2);
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


    private class LinkSpan extends URLSpan {
        private LinkSpan(String url) {
            super(url);
        }

        @Override
        public void onClick(View view) {
            String url = getURL();
            if (url != null) {

                view.getContext().startActivity(new Intent(view.getContext(),FeedWebView.class).putExtra("url",url));

            }
        }
    }

    private CharSequence noTrailingwhiteLines(CharSequence text) {

        while (text.charAt(text.length() - 1) == '\n') {
            text = text.subSequence(0, text.length() - 1);
        }
        return text;
    }

}

