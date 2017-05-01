package in.sportscafe.nostragamus.module.allchallenges.challenge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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
import in.sportscafe.nostragamus.module.allchallenges.info.ChallengeConfigsDialogFragment;
import in.sportscafe.nostragamus.module.allchallenges.info.ChallengeInfoDialogFragment;
import in.sportscafe.nostragamus.module.allchallenges.info.ChallengeRewardsFragment;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.user.lblanding.LbLanding;
import in.sportscafe.nostragamus.module.user.points.PointsActivity;
import in.sportscafe.nostragamus.utils.ViewUtils;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

import static in.sportscafe.nostragamus.R.dimen;
import static in.sportscafe.nostragamus.R.id;
import static in.sportscafe.nostragamus.R.layout;

/**
 * Created by deepanshi on 17/2/17.
 */
public class ChallengeAdapter extends Adapter<Challenge, ChallengeAdapter.ViewHolder> {

    private boolean mSwipeView = true;

    private boolean mOpenJoin = false;

    private int CHALLENGE_INFO_DIALOG_TYPE = 0;

    private int CHALLENGE_REWARDS_DIALOG_TYPE = 1;

    private int CHALLENGE_CONFIG_DIALOG_TYPE = 2;

    private int mTagId;

    private int dialogType = 0;

    private String mTabName = "";

    public ChallengeAdapter(Context context, List<Challenge> challenges,
                            boolean swipeView, int tagId, String tabName) {
        super(context);
        mSwipeView = swipeView;
        mTagId = tagId;
        mTabName = tabName;
        addAll(challenges);
    }

    @Override
    public Challenge getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(layout.inflater_new_challenges_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (mSwipeView/* || position == 0*/) {
            holder.mTvShowGamesNew.setText("HIDE GAMES");
            holder.mTvAfrJoinedShowGames.setText("Hide games");
            holder.mTvAfrJoinedShowGames.setCompoundDrawablesWithIntrinsicBounds(R.drawable.challenge_hide_game, 0, 0, 0);
        } else {
            holder.mTvShowGamesNew.setText("SHOW GAMES");
            holder.mTvAfrJoinedShowGames.setText("Show games");
            holder.mTvAfrJoinedShowGames.setCompoundDrawablesWithIntrinsicBounds(R.drawable.challenge_show_game, 0, 0, 0);
        }

        Challenge challenge = getItem(position);
        holder.mTvChallengeName.setText(challenge.getName());
        holder.mIvChallengeImage.setImageUrl(challenge.getImage());

        try {
            int mChallengeAmount = challenge.getChallengeInfo().getPaymentInfo().getPrizeMoney();
            if (mChallengeAmount == 0) {
                holder.mRlCashRewards.setVisibility(View.INVISIBLE);
            } else {
                holder.mRlCashRewards.setVisibility(View.VISIBLE);
                SpannableStringBuilder builder = new SpannableStringBuilder();

                String priceTxt1 = "Worth ";
                SpannableString priceTxt1Spannable = new SpannableString(priceTxt1);
                priceTxt1Spannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, priceTxt1.length(), 0);
                builder.append(priceTxt1Spannable);

                String priceTxt2 = "₹" + mChallengeAmount;
                SpannableString priceTxt2Spannable = new SpannableString(priceTxt2);
                priceTxt2Spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#04B554")), 0, priceTxt2.length(), 0);
                builder.append(priceTxt2Spannable);

                String priceTxt3 = " to be won!";
                SpannableString priceTxt3Spannable = new SpannableString(priceTxt3);
                priceTxt3Spannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, priceTxt3.length(), 0);
                builder.append(priceTxt3Spannable);
                holder.mTvChallengePrice.setText(builder, TextView.BufferType.SPANNABLE);

            }

            /*String prizeMoneyTopLine = challenge.getChallengeInfo().getPrizeMoneyTopline();
            if (TextUtils.isEmpty(prizeMoneyTopLine)) {
                holder.mRlCashRewards.setVisibility(View.INVISIBLE);
            } else {
                holder.mRlCashRewards.setVisibility(View.VISIBLE);
                holder.mTvChallengePrice.setText(Html.fromHtml(prizeMoneyTopLine));
            }*/
        } catch (Exception e) {
            holder.mRlCashRewards.setVisibility(View.INVISIBLE);
        }

        if (challenge.getChallengeUserInfo().isUserJoined()|| challenge.getCountMatchesLeft().equals("0")) {
            holder.mRlAfterJoinedChallenge.setVisibility(View.VISIBLE);
            holder.mRlMatchesLeft.setVisibility(View.INVISIBLE);
            holder.mRlMainPowerup.setVisibility(View.VISIBLE);
        } else {
            holder.mRlAfterJoinedChallenge.setVisibility(View.GONE);
            holder.mRlMatchesLeft.setVisibility(View.VISIBLE);
            holder.mTvMatchesLeft.setText(String.valueOf(challenge.getCountMatchesLeft()) + "/" + String.valueOf(challenge.getMatchesCategorized().getAllMatches().size()) + " Games Left to score!");
            holder.mRlMainPowerup.setVisibility(View.INVISIBLE);
            int percentage = (Integer.parseInt(challenge.getCountMatchesLeft()) * 100) / challenge.getMatchesCategorized().getAllMatches().size();
            setPercentPoll(holder.mTvMatchesLeft,percentage ,holder.mTvMatchesLeft.getContext());
            mOpenJoin=true;
        }

//        Context context = holder.mIv2xPowerup.getContext();
//        holder.mIv2xPowerup.setBackground(getPowerupDrawable(context, color.dodger_blue));
//        holder.mIvNonegsPowerup.setBackground(getPowerupDrawable(context, color.amaranth));
//        holder.mIvPollPowerup.setBackground(getPowerupDrawable(context, color.greencolor));

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
            holder.mTvChallengeUserRank.setText("No Rank");
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
            layoutParams = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.rightMargin = 10;
            textview.setLayoutParams(layoutParams);
            textview.setText(challenge.getTournaments().get(i).getTournamentShortName());
            textview.setTextColor(Color.WHITE);
            textview.setPadding(10, 5, 10, 10);
            textview.setBackground(holder.mLlTournament.getContext().getResources().getDrawable(R.drawable.btn_not_played_bg));
            textview.setTextSize(10);
            textview.setTypeface(tf, Typeface.NORMAL);

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

        //for completed challenges
        if (challenge.getCountMatchesLeft().equals("0")) {
            holder.mTvRewards.setText("Winners");
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mMainView;
        CustomButton mTvChallengePrice;
        TextView mTvChallengeName;
        HmImageView mIvChallengeImage;
        ImageView mIvChallengeInfo;
        TextView mTvChallengeUserRank;
        //TextView mTvTimerText;
        TextView mBtnTimeLeft;
        Button mTvChallengeDaysLeft;
        Button mTvChallengeHoursLeft;
        Button mTvChallengeMinsLeft;
        Button mTvChallengeSecsLeft;
        RelativeLayout mRlMainPowerup;
        ImageView mIv2xPowerup;
        ImageView mIvNonegsPowerup;
        ImageView mIvPollPowerup;
        TextView mTv2xPowerupCount;
        TextView mTvNonegsPowerupCount;
        TextView mTvPollPowerupCount;
        LinearLayout mLlTournament;
        ShadowLayout mSlShowGameBg;
        TextView mTvShowGames;
        TextView mTvShowGamesNew;
        RelativeLayout mRlShowGames;
        RelativeLayout mRlTimer;
        RelativeLayout mRlAfrJoinedShowGames;
        TextView mTvAfrJoinedShowGames;
        TextView mTvGamesLeftCount;
        RelativeLayout mRlCashRewards;
        RelativeLayout mRlJoinChallenge;
        RelativeLayout mRlAfterJoinedChallenge;
        RelativeLayout mRlMatchesLeft;
        RelativeLayout mRlRewards;
        TextView mTvRewards;
        Button mTvMatchesLeft;

        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mTvChallengeName = (TextView) V.findViewById(id.all_challenges_row_matchstage_tv);
            mTvChallengePrice = (CustomButton) V.findViewById(id.all_challenges_row_tv_price);
            mBtnTimeLeft = (TextView) V.findViewById(id.all_challenges_row_btn_time_left);
            mIvChallengeImage = (HmImageView) V.findViewById(id.all_challenges_row_iv_image);
            mIv2xPowerup = (ImageView) V.findViewById(id.powerups_iv_2x);
            mIvNonegsPowerup = (ImageView) V.findViewById(id.powerups_iv_nonegs);
            mIvPollPowerup = (ImageView) V.findViewById(id.powerups_iv_poll);
            mTv2xPowerupCount = (TextView) V.findViewById(id.powerup_tv_2x_count);
            mTvNonegsPowerupCount = (TextView) V.findViewById(id.powerup_tv_nonegs_count);
            mTvPollPowerupCount = (TextView) V.findViewById(id.powerup_tv_poll_count);
            mRlMainPowerup = (RelativeLayout) V.findViewById(id.all_challenges_row_powerup_main_rl);
            mTvChallengeUserRank = (TextView) V.findViewById(id.all_challenges_row_tv_leaderboard_rank);
            mTvChallengeHoursLeft = (Button) V.findViewById(id.all_challenges_row_btn_hours_left);
            mTvChallengeDaysLeft = (Button) V.findViewById(id.all_challenges_row_btn_days_left);
            mTvChallengeMinsLeft = (Button) V.findViewById(id.all_challenges_row_btn_mins_left);
            mTvChallengeSecsLeft = (Button) V.findViewById(id.all_challenges_row_btn_secs_left);
            mLlTournament = (LinearLayout) V.findViewById(id.all_challenges_row_tournament_ll);
            mSlShowGameBg = (ShadowLayout) V.findViewById(id.all_challenges_sl_anim_bg);
            mRlTimer = (RelativeLayout) V.findViewById(id.all_challenges_row_rl_timer);
            mTvShowGames = (TextView) V.findViewById(id.all_challenges_row_tv_show_games);
            mRlShowGames = (RelativeLayout) V.findViewById(id.all_challenges_row_rl_show_games_new);
            mTvShowGamesNew = (TextView) V.findViewById(id.all_challenges_row_tv_show_games_new);
            mRlCashRewards = (RelativeLayout) V.findViewById(id.all_challenges_row_rl_cash_rewards);
            // mTvTimerText = (TextView) V.findViewById(id.all_challenges_row_tv_timer_txt);
            mIvChallengeInfo = (ImageView) V.findViewById(id.challenge_iv_info);
            mRlJoinChallenge = (RelativeLayout) V.findViewById(id.all_challenges_row_rl_join_btn);
            mRlAfterJoinedChallenge = (RelativeLayout) V.findViewById(id.all_challenges_rl_after_joined_challenge);
            mTvAfrJoinedShowGames = (TextView) V.findViewById(id.all_challenges_row_btn_show_games);
            mRlAfrJoinedShowGames = (RelativeLayout) V.findViewById(id.all_challenges_row_rl_show_games_btn);
            mTvMatchesLeft = (Button) V.findViewById(id.all_challenges_row_tv_matches_left);
            mRlMatchesLeft = (RelativeLayout) V.findViewById(id.all_challenges_row_rl_matches_left);
            mRlRewards = (RelativeLayout) V.findViewById(id.all_challenges_row_rl_rewards);
            mTvRewards = (TextView) V.findViewById(id.all_challenges_row_btn_rewards);

            mRlShowGames.setOnClickListener(this);
            mIvChallengeInfo.setOnClickListener(this);
            mRlJoinChallenge.setOnClickListener(this);
            mRlAfrJoinedShowGames.setOnClickListener(this);
            mRlRewards.setOnClickListener(this);
            mRlCashRewards.setOnClickListener(this);

            V.findViewById(R.id.all_challenges_rl_after_joined_challenge).setOnClickListener(this);
//            mTvGamesLeftCount = (TextView) V.findViewById(R.id.all_challenges_row_tv_show_games);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            switch (view.getId()) {
                case R.id.all_challenges_row_rl_show_games_new:
                    Intent intent = new Intent(IntentActions.ACTION_CHALLENGE_CLICK);
                    intent.putExtra(BundleKeys.CLICK_POSITION, getAdapterPosition());
                    intent.putExtra(BundleKeys.CHALLENGE_TAG_ID, mTagId);
                    intent.putExtra(BundleKeys.TAB_ITEM_NAME, mTabName);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                    if (mSwipeView) {
                        intent.putExtra(BundleKeys.CHALLENGE_SWITCH_POS, false);
                    } else {
                        intent.putExtra(BundleKeys.CHALLENGE_SWITCH_POS, true);
                    }
                    break;

                case R.id.all_challenges_rl_after_joined_challenge:
                    NostragamusAnalytics.getInstance().trackLeaderboard(LBLandingType.CHALLENGE);

                    Challenge challenge = getItem(getAdapterPosition());

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(BundleKeys.LB_LANDING_DATA, Parcels.wrap(new LbLanding(
                            challenge.getChallengeId(), 0, challenge.getName(), challenge.getImage(), LBLandingType.CHALLENGE
                    )));
                    navigateToPointsActivity(context, bundle);
                    break;

                case R.id.challenge_iv_info:
                    Challenge challengeInfo = getItem(getAdapterPosition());
                    dialogType = CHALLENGE_INFO_DIALOG_TYPE;
                    showChallengeInfo(context, challengeInfo);
                    break;

                case R.id.all_challenges_row_rl_join_btn:
                    Challenge challengeJoin = getItem(getAdapterPosition());
                    dialogType = CHALLENGE_CONFIG_DIALOG_TYPE;
                    showChallengeInfo(context, challengeJoin);
                    break;

                case R.id.all_challenges_row_rl_rewards:
                    Challenge challengeRewards = getItem(getAdapterPosition());
//                    if (challengeRewards.getChallengeUserInfo().getConfigIndex()!=null) {
                        dialogType = CHALLENGE_REWARDS_DIALOG_TYPE;
                        showChallengeInfo(context, challengeRewards);
//                    }
                    break;

                case R.id.all_challenges_row_rl_cash_rewards:
                    if (mOpenJoin) {
                        Challenge challengeJoinNew = getItem(getAdapterPosition());
                        dialogType = CHALLENGE_CONFIG_DIALOG_TYPE;
                        showChallengeInfo(context, challengeJoinNew);
                    }else {
                        Challenge challengeRewardsNew = getItem(getAdapterPosition());
//                        if (challengeRewardsNew.getChallengeUserInfo().getConfigIndex()!=null) {
                            dialogType = CHALLENGE_REWARDS_DIALOG_TYPE;
                            showChallengeInfo(context, challengeRewardsNew);
//                        }
                    }
                    break;

                case R.id.all_challenges_row_rl_show_games_btn:
                    Intent switchIntent = new Intent(IntentActions.ACTION_CHALLENGE_CLICK);
                    switchIntent.putExtra(BundleKeys.CLICK_POSITION, getAdapterPosition());
                    switchIntent.putExtra(BundleKeys.CHALLENGE_TAG_ID, mTagId);
                    switchIntent.putExtra(BundleKeys.TAB_ITEM_NAME, mTabName);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(switchIntent);

                    if (mSwipeView) {
                        switchIntent.putExtra(BundleKeys.CHALLENGE_SWITCH_POS, false);
                    } else {
                        switchIntent.putExtra(BundleKeys.CHALLENGE_SWITCH_POS, true);
                    }
            }
        }
    }

    private void showChallengeInfo(Context context, Challenge challenge) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();

        if (dialogType == CHALLENGE_CONFIG_DIALOG_TYPE) {
            ChallengeConfigsDialogFragment.newInstance(43, challenge)
                    .show(fragmentManager, "challenge_configs");

        } else if (dialogType == CHALLENGE_REWARDS_DIALOG_TYPE) {
            int configIndex = 0;
            if (challenge.getChallengeUserInfo() != null && challenge.getChallengeUserInfo().getConfigIndex() != null) {
                configIndex = challenge.getChallengeUserInfo().getConfigIndex();
            }
            ChallengeRewardsFragment.newInstance(44, challenge, challenge.getName() + " Prizes",
                    configIndex, challenge.getEndTime())
                    .show(fragmentManager, "challenge_rewards");

        } else {
            ChallengeInfoDialogFragment.newInstance(58, " Info", challenge)
                    .show(fragmentManager, "challenge_info");
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


    private void setPercentPoll(Button button, int percent,Context context) {
        int width = context.getResources().getDimensionPixelSize(R.dimen.dp_140);
        int height = context.getResources().getDimensionPixelOffset(R.dimen.dp_24);

        button.setBackground(getPercentDrawable(
                width,
                height,
                width * percent / 100,
                context.getResources().getColor(R.color.timer_color_bg),
                context
        ));
    }

    private Drawable getPercentDrawable(int fullWidth, int fullHeight, int percentWidth, int percentColor,Context context) {
        Bitmap outputBitmap = Bitmap.createBitmap(fullWidth, fullHeight, Bitmap.Config.ARGB_4444);
        Canvas outputCanvas = new Canvas(outputBitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(percentColor);
        paint.setStyle(Paint.Style.FILL);
        final Rect rect = new Rect(fullWidth - percentWidth, 0, fullWidth, fullHeight);
        final RectF rectF = new RectF(rect);
        final float roundPx = 6;
        outputCanvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        return new BitmapDrawable(context.getResources(), outputBitmap);
    }

}