package in.sportscafe.nostragamus.module.allchallenges.challenge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.ShadowLayout;
import com.jeeva.android.widgets.customfont.CustomButton;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.DateFormats;
import in.sportscafe.nostragamus.Constants.IntentActions;
import in.sportscafe.nostragamus.Constants.LBLandingType;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.user.lblanding.LbLanding;
import in.sportscafe.nostragamus.module.user.points.PointsActivity;
import in.sportscafe.nostragamus.utils.ViewUtils;
import in.sportscafe.nostragamus.utils.timeutils.TimeAgo;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

import static in.sportscafe.nostragamus.R.color;
import static in.sportscafe.nostragamus.R.dimen;
import static in.sportscafe.nostragamus.R.id;
import static in.sportscafe.nostragamus.R.layout;

/**
 * Created by deepanshi on 17/2/17.
 */
public class ChallengeAdapter extends Adapter<Challenge, ChallengeAdapter.ViewHolder> {

    private boolean mSwipeView = true;

    private int mTagId;

    public ChallengeAdapter(Context context, List<Challenge> challenges, boolean swipeView, int tagId) {
        super(context);
        mSwipeView = swipeView;
        mTagId = tagId;
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
            holder.mSlShowGameBg.setVisibility(View.GONE);
        } else {
            holder.mSlShowGameBg.setVisibility(View.VISIBLE);
        }

        Challenge challenge = getItem(position);
        holder.mTvChallengeName.setText(challenge.getName());
        holder.mIvChallengeImage.setImageUrl(challenge.getImage());

        try {
            int mChallengeAmount = challenge.getChallengeInfo().getPaymentInfo().getPrizeMoney();
            holder.mTvChallengePrice.setText("Worth Rs" + mChallengeAmount);
        } catch (Exception e) {
            holder.mRlCashRewards.setVisibility(View.INVISIBLE);
        }

        Context context = holder.mIv2xPowerup.getContext();
        holder.mIv2xPowerup.setBackground(getPowerupDrawable(context, color.dodger_blue));
        holder.mIvNonegsPowerup.setBackground(getPowerupDrawable(context, color.amaranth));
        holder.mIvPollPowerup.setBackground(getPowerupDrawable(context, color.greencolor));

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

        /*TimeAgo startTimeLeft = TimeUtils.calcTimeAgo(Calendar.getInstance().getTimeInMillis(),
                TimeUtils.getMillisecondsFromDateString(
                        challenge.getStartTime(),
                        DateFormats.FORMAT_DATE_T_TIME_ZONE,
                        DateFormats.GMT
                )
        );*/

        TimeAgo endTimeLeft = TimeUtils.calcTimeAgo(Calendar.getInstance().getTimeInMillis(),
                TimeUtils.getMillisecondsFromDateString(
                        challenge.getEndTime(),
                        DateFormats.FORMAT_DATE_T_TIME_ZONE,
                        DateFormats.GMT
                )
        );

        /*if(startTimeLeft.totalDiff > 1000) {
            holder.mRlTimer.setVisibility(View.VISIBLE);
            holder.mTvTimerText.setText("STARTS IN");
            holder.mRlTimer.setTag(startTimeLeft.totalDiff);
        } else */if (endTimeLeft.totalDiff > 1000) {
            holder.mRlTimer.setVisibility(View.VISIBLE);
            holder.mTvTimerText.setText("ENDS IN");
            holder.mRlTimer.setTag(endTimeLeft.totalDiff);
        } else {
            holder.mRlTimer.setVisibility(View.GONE);
        }


        String startTime = challenge.getStartTime();
        long startTimeMs = TimeUtils.getMillisecondsFromDateString(
                startTime,
                DateFormats.FORMAT_DATE_T_TIME_ZONE,
                DateFormats.GMT
        );

        int dayOfMonthinStartTime = Integer.parseInt(TimeUtils.getDateStringFromMs(startTimeMs, "d"));


        String endTime = challenge.getEndTime();
        long endTimeMs = TimeUtils.getMillisecondsFromDateString(
                endTime,
                DateFormats.FORMAT_DATE_T_TIME_ZONE,
                DateFormats.GMT
        );

        int dayOfMonthinEndTime = Integer.parseInt(TimeUtils.getDateStringFromMs(endTimeMs, "d"));

        // Setting date of the match
        holder.mBtnTimeLeft.setText(
                dayOfMonthinStartTime + AppSnippet.ordinalOnly(dayOfMonthinStartTime) + " " +
                TimeUtils.getDateStringFromMs(startTimeMs, "MMM")
                         + " to " + dayOfMonthinEndTime + AppSnippet.ordinalOnly(dayOfMonthinEndTime) + " " +
                        TimeUtils.getDateStringFromMs(endTimeMs, "MMM")
        );



    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mMainView;
        CustomButton mTvChallengePrice;
        TextView mTvChallengeName;
        HmImageView mIvChallengeImage;

        TextView mTvChallengeUserRank;

        TextView mTvTimerText;
        Button mBtnTimeLeft;
        Button mTvChallengeDaysLeft;
        Button mTvChallengeHoursLeft;
        Button mTvChallengeMinsLeft;
        Button mTvChallengeSecsLeft;

        ImageView mIv2xPowerup;
        ImageView mIvNonegsPowerup;
        ImageView mIvPollPowerup;
        TextView mTv2xPowerupCount;
        TextView mTvNonegsPowerupCount;
        TextView mTvPollPowerupCount;

        LinearLayout mLlTournament;
        ShadowLayout mSlShowGameBg;
        TextView mTvShowGames;

        RelativeLayout mRlShowGames;
        RelativeLayout mRlTimer;

        TextView mTvGamesLeftCount;

        RelativeLayout mRlCashRewards;

        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mTvChallengeName = (TextView) V.findViewById(id.all_challenges_row_matchstage_tv);
            mTvChallengePrice = (CustomButton) V.findViewById(id.all_challenges_row_tv_price);
            mBtnTimeLeft = (Button) V.findViewById(id.all_challenges_row_btn_time_left);
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
            mTvTimerText = (TextView) V.findViewById(id.all_challenges_row_tv_timer_txt);
            mTvChallengeMinsLeft = (Button) V.findViewById(id.all_challenges_row_btn_mins_left);
            mTvChallengeSecsLeft = (Button) V.findViewById(id.all_challenges_row_btn_secs_left);
            mLlTournament = (LinearLayout) V.findViewById(id.all_challenges_row_tournament_ll);
            mSlShowGameBg = (ShadowLayout) V.findViewById(id.all_challenges_sl_anim_bg);
            mRlTimer = (RelativeLayout) V.findViewById(id.all_challenges_row_rl_timer);
            mTvShowGames = (TextView) V.findViewById(id.all_challenges_row_tv_show_games);
            mRlShowGames = (RelativeLayout) V.findViewById(id.all_challenges_row_rl_show_games);
            mRlCashRewards = (RelativeLayout) V.findViewById(id.all_challenges_row_rl_cash_rewards);
            mRlShowGames.setOnClickListener(this);

            V.findViewById(R.id.all_challenges_rl_leadboard).setOnClickListener(this);
//            mTvGamesLeftCount = (TextView) V.findViewById(R.id.all_challenges_row_tv_show_games);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            switch (view.getId()) {
                case R.id.all_challenges_row_rl_show_games:
                    Intent intent = new Intent(IntentActions.ACTION_CHALLENGE_CLICK);
                    intent.putExtra(BundleKeys.CLICK_POSITION, getAdapterPosition());
                    intent.putExtra(BundleKeys.CHALLENGE_TAG_ID, mTagId);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    break;
                case R.id.all_challenges_rl_leadboard:
                    NostragamusAnalytics.getInstance().trackLeaderboard(LBLandingType.CHALLENGE);

                    Challenge challenge = getItem(getAdapterPosition());

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(BundleKeys.LB_LANDING_DATA, Parcels.wrap(new LbLanding(
                            challenge.getChallengeId(),0,challenge.getName(), challenge.getImage(), LBLandingType.CHALLENGE
                    )));
                    navigateToPointsActivity(context, bundle);
                    break;
            }
        }
    }

    private void navigateToPointsActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PointsActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }



    private Drawable getPowerupDrawable(Context context, int colorRes) {
        GradientDrawable powerupDrawable = new GradientDrawable();
        powerupDrawable.setShape(GradientDrawable.RECTANGLE);
        powerupDrawable.setCornerRadius(context.getResources().getDimensionPixelSize(dimen.dp_5));
        powerupDrawable.setColor(ViewUtils.getColor(context, colorRes));
        return powerupDrawable;
    }

}