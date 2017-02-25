package in.sportscafe.nostragamus.module.allchallenges.challenge;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.IntentActions;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.play.myresultstimeline.TimelineAdapter;
import in.sportscafe.nostragamus.utils.timeutils.TimeAgo;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

import static in.sportscafe.nostragamus.R.*;

/**
 * Created by deepanshi on 17/2/17.
 */
public class ChallengeAdapter extends Adapter<Challenge, ChallengeAdapter.ViewHolder> {

    private Resources mResources;

    private boolean mSwipeView = true;

    private Integer mChallengeAmount;

    public ChallengeAdapter(Context context, List<Challenge> challenges, boolean swipeView) {
        super(context);
        mResources = context.getResources();
        mSwipeView = swipeView;
        addAll(challenges);
    }

    @Override
    public Challenge getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(layout.inflater_all_challenges_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mSwipeView/* || position == 0*/) {
            holder.mRlShowGameBg.setVisibility(View.GONE);
        } else {
            holder.mRlShowGameBg.setVisibility(View.VISIBLE);
        }

        Challenge challenge = getItem(position);

        holder.mTvChallengeName.setText(challenge.getName());

        try {
            int mChallengeAmount = challenge.getChallengeInfo().getPaymentInfo().getChallengeFee();
            holder.mTvChallengePrice.setText("Paid - Rs." + mChallengeAmount);
        } catch (Exception e) {
            holder.mTvChallengePrice.setVisibility(View.INVISIBLE);
        }

        holder.mIv2xPowerup.setBackground(getPowerupDrawable(color.dodger_blue));
        holder.mIvNonegsPowerup.setBackground(getPowerupDrawable(color.amaranth));
        holder.mIvPollPowerup.setBackground(getPowerupDrawable(color.greencolor));

        try {
            HashMap<String, Integer> powerUpMap = challenge.getChallengeUserInfo().getPowerUps();
            Integer powerUp2x = powerUpMap.get(Constants.Powerups.XX);
            Integer powerUpNoNegative = powerUpMap.get(Constants.Powerups.NO_NEGATIVE);
            Integer powerUpAudiencePoll = powerUpMap.get(Constants.Powerups.AUDIENCE_POLL);

            holder.mTv2xPowerupCount.setText(String.valueOf(powerUp2x));
            holder.mTvNonegsPowerupCount.setText(String.valueOf(powerUpNoNegative));
            holder.mTvPollPowerupCount.setText(String.valueOf(powerUpAudiencePoll));
        } catch (Exception e) {
            holder.mTv2xPowerupCount.setText("0");
            holder.mTvNonegsPowerupCount.setText("0");
            holder.mTvPollPowerupCount.setText("0");
        }

        if (null != challenge.getUserRank()) {
            String rank = AppSnippet.ordinal(challenge.getUserRank());
            holder.mTvChallengeUserRank.setText(rank + " Rank");
        } else {
            holder.mTvChallengeUserRank.setText("Did Not Play");
        }

        /*if (mSwipeView) {
            holder.mLlShowGames.setBackgroundColor(mResources.getColor(R.color.black));
        } else {
            holder.mLlShowGames.setBackground(mResources.getDrawable(R.drawable.shape_challenges_show_game_bg));
        }*/

        /*if (!TextUtils.isEmpty(challenge.getCountMatchesLeft())) {
            if (challenge.getCountMatchesLeft().equals("0")) {
                holder.mTvGamesLeftCount.setText("No Games");
            } else {
                holder.mTvGamesLeftCount.setText(challenge.getCountMatchesLeft() + " Games Left");
            }
        }*/

        LinearLayout.LayoutParams layoutParams = null;

        LinearLayout layout2 = new LinearLayout(holder.mLlTournament.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout2.setLayoutParams(params);
        holder.mLlTournament.setOrientation(LinearLayout.HORIZONTAL);
        holder.mLlTournament.removeAllViews();
        holder.mLlTournament.addView(layout2);

        Typeface tf = Typeface.createFromAsset(holder.mLlTournament.getContext().getAssets(), "fonts/roboto/RobotoCondensed-Regular.ttf");

        for (int i = 0; i < challenge.getTournaments().size(); i++) {

            TextView textview = new TextView(holder.mLlTournament.getContext());
            layout2.addView(textview);
            layoutParams =new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.rightMargin = 10;
            textview.setLayoutParams(layoutParams);
            textview.setText(challenge.getTournaments().get(i).getTournamentShortName());
            textview.setTextColor(Color.WHITE);
            textview.setPadding(10, 5, 10, 10);
            textview.setBackground(holder.mLlTournament.getContext().getResources().getDrawable(R.drawable.btn_not_played_bg));
            textview.setTextSize(12);
            textview.setTypeface(tf, Typeface.NORMAL);

        }


        long startTimeMs = TimeUtils.getMillisecondsFromDateString(
                challenge.getEndTime(),
                Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                Constants.DateFormats.GMT
        );

        TimeAgo timeAgo = TimeUtils.calcTimeAgo(Calendar.getInstance().getTimeInMillis(), startTimeMs);
        long updatedTime = Long.parseLong(String.valueOf(timeAgo.totalDiff));

        if (updatedTime < 0){
            holder.mRlTimer.setVisibility(View.INVISIBLE);
        }else {
            updateTimer(holder, updatedTime);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mMainView;
        TextView mTvChallengePrice;
        TextView mTvChallengeName;
        HmImageView mIvChallengeImage;

        TextView mTvChallengeUserRank;

        Button mTvChallengeDaysLeft;
        Button mTvChallengeHoursLeft;
        Button mTvChallengeMinsLeft;

        ImageView mIv2xPowerup;
        ImageView mIvNonegsPowerup;
        ImageView mIvPollPowerup;
        TextView mTv2xPowerupCount;
        TextView mTvNonegsPowerupCount;
        TextView mTvPollPowerupCount;

        LinearLayout mLlTournament;
        RelativeLayout mRlShowGameBg;
        TextView mTvShowGames;

        RelativeLayout mRlTimer;

        TextView mTvGamesLeftCount;

        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mTvChallengeName = (TextView) V.findViewById(id.all_challenges_row_matchstage_tv);
            mTvChallengePrice = (TextView) V.findViewById(id.all_challenges_row_tv_price);
            mIvChallengeImage = (HmImageView) V.findViewById(id.all_challenges_row_iv_image);
            mIv2xPowerup = (ImageView) V.findViewById(id.powerups_iv_2x);
            mIvNonegsPowerup = (ImageView) V.findViewById(id.powerups_iv_nonegs);
            mIvPollPowerup = (ImageView) V.findViewById(id.powerups_iv_poll);
            mTv2xPowerupCount = (TextView) V.findViewById(id.powerup_tv_2x_count);
            mTvNonegsPowerupCount = (TextView) V.findViewById(id.powerup_tv_nonegs_count);
            mTvPollPowerupCount = (TextView) V.findViewById(id.powerup_tv_poll_count);
            mTvChallengeUserRank = (TextView) V.findViewById(id.all_challenges_row_tv_leaderboard_rank);
            mTvChallengeHoursLeft = (Button) V.findViewById(id.all_challenges_row_btn_hours_left);
            mTvChallengeDaysLeft = (Button) V.findViewById(id.all_challenges_row_btn_days_left);
            mTvChallengeMinsLeft = (Button) V.findViewById(id.all_challenges_row_btn_mins_left);
            mLlTournament = (LinearLayout) V.findViewById(id.all_challenges_row_tournament_ll);
            mRlShowGameBg = (RelativeLayout) V.findViewById(id.all_challenges_rl_anim_bg);
            mRlTimer = (RelativeLayout) V.findViewById(id.all_challenges_row_rl_timer);
            mTvShowGames = (TextView) V.findViewById(id.all_challenges_row_tv_show_games);
            mTvShowGames.setOnClickListener(this);
//            mTvGamesLeftCount = (TextView) V.findViewById(R.id.all_challenges_row_tv_show_games);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(IntentActions.ACTION_CHALLENGE_CLICK);
            intent.putExtra(BundleKeys.CLICK_POSITION, getAdapterPosition());
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }

    private void updateTimer(ViewHolder viewHolder, long updatedTime) {

        int secs = (int) (updatedTime / 1000);
        int mins = secs / 60;
        int hours = mins / 60;
        int days = hours / 24;
        hours = hours % 24;
        mins = mins % 60;
        secs = secs % 60;

        viewHolder.mTvChallengeDaysLeft.setText(String.format("%02d", days)+"d");
        viewHolder.mTvChallengeHoursLeft.setText(String.format("%02d", hours)+"h");
        viewHolder.mTvChallengeMinsLeft.setText(String.format("%02d", mins)+"m");

    }


    private Drawable getPowerupDrawable(int colorRes) {
        GradientDrawable powerupDrawable = new GradientDrawable();
        powerupDrawable.setShape(GradientDrawable.RECTANGLE);
        powerupDrawable.setCornerRadius(mResources.getDimensionPixelSize(dimen.dp_5));
        powerupDrawable.setColor(mResources.getColor(colorRes));
        return powerupDrawable;
    }

}