package in.sportscafe.nostragamus.module.allchallenges.challenge;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.IntentActions;
import in.sportscafe.nostragamus.Constants.LBLandingType;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.user.lblanding.LbLanding;
import in.sportscafe.nostragamus.module.user.points.PointsActivity;

/**
 * Created by deepanshi on 17/2/17.
 */
public class ChallengeAdapter extends Adapter<Challenge, ChallengeAdapter.ViewHolder> {

    private Resources mResources;

    private boolean mSwipeView = true;

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
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_all_challenges_row, parent, false));
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

        holder.mIv2xPowerup.setBackground(getPowerupDrawable(R.color.dodger_blue));
        holder.mIvNonegsPowerup.setBackground(getPowerupDrawable(R.color.amaranth));
        holder.mIvPollPowerup.setBackground(getPowerupDrawable(R.color.greencolor));

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

//        HorizontalScrollView.LayoutParams layoutParams =
//                (HorizontalScrollView.LayoutParams) holder.mLlTournament.getLayoutParams();
//        holder.mLlTournament.setLayoutParams(layoutParams);
//
//        LinearLayout layout2 = new LinearLayout(context);
//        layout2.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
//        holder.mLlTournament.setOrientation(LinearLayout.VERTICAL);
//        holder.mLlTournament.addView(layout2);
//
//
//        for (int i = 0; i < challenge.getTournaments().size(); i++) {
//
//            TextView textview = new TextView(context);
//            textview.setLayoutParams(new RelativeLayout.LayoutParams
//                    (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
//            textview.setText(challenge.getTournaments().get(i).getName());
//            layout2.addView(textview);
//
//        }


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

        TextView mTvGamesLeftCount;

        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mTvChallengeName = (TextView) V.findViewById(R.id.all_challenges_row_matchstage_tv);
            mTvChallengePrice = (TextView) V.findViewById(R.id.all_challenges_row_tv_price);
            mIvChallengeImage = (HmImageView) V.findViewById(R.id.all_challenges_row_iv_image);
            mIv2xPowerup = (ImageView) V.findViewById(R.id.powerups_iv_2x);
            mIvNonegsPowerup = (ImageView) V.findViewById(R.id.powerups_iv_nonegs);
            mIvPollPowerup = (ImageView) V.findViewById(R.id.powerups_iv_poll);
            mTv2xPowerupCount = (TextView) V.findViewById(R.id.powerup_tv_2x_count);
            mTvNonegsPowerupCount = (TextView) V.findViewById(R.id.powerup_tv_nonegs_count);
            mTvPollPowerupCount = (TextView) V.findViewById(R.id.powerup_tv_poll_count);
            mTvChallengeUserRank = (TextView) V.findViewById(R.id.all_challenges_row_tv_leaderboard_rank);
            mTvChallengeHoursLeft = (Button) V.findViewById(R.id.all_challenges_row_btn_hours_left);
            mTvChallengeDaysLeft = (Button) V.findViewById(R.id.all_challenges_row_btn_days_left);
            mTvChallengeMinsLeft = (Button) V.findViewById(R.id.all_challenges_row_btn_mins_left);
            mLlTournament = (LinearLayout) V.findViewById(R.id.all_challenges_row_tournament_ll);
            mRlShowGameBg = (RelativeLayout) V.findViewById(R.id.all_challenges_rl_anim_bg);
            mTvShowGames = (TextView) V.findViewById(R.id.all_challenges_row_tv_show_games);
            mTvShowGames.setOnClickListener(this);

            V.findViewById(R.id.all_challenges_rl_leadboard).setOnClickListener(this);
//            mTvGamesLeftCount = (TextView) V.findViewById(R.id.all_challenges_row_tv_show_games);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            switch (view.getId()) {
                case R.id.all_challenges_row_tv_show_games:
                    Intent intent = new Intent(IntentActions.ACTION_CHALLENGE_CLICK);
                    intent.putExtra(BundleKeys.CLICK_POSITION, getAdapterPosition());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    break;
                case R.id.all_challenges_rl_leadboard:
                    NostragamusAnalytics.getInstance().trackLeaderboard(LBLandingType.CHALLENGE);

                    Challenge challenge = getItem(getAdapterPosition());

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(BundleKeys.LB_LANDING_DATA, Parcels.wrap(new LbLanding(
                            challenge.getChallengeId(), challenge.getName(), challenge.getImage(), LBLandingType.CHALLENGE
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

    private Drawable getPowerupDrawable(int colorRes) {
        GradientDrawable powerupDrawable = new GradientDrawable();
        powerupDrawable.setShape(GradientDrawable.RECTANGLE);
        powerupDrawable.setCornerRadius(mResources.getDimensionPixelSize(R.dimen.dp_5));
        powerupDrawable.setColor(mResources.getColor(colorRes));
        return powerupDrawable;
    }

}