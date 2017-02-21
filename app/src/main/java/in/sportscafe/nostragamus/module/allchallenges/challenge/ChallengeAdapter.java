package in.sportscafe.nostragamus.module.allchallenges.challenge;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import java.security.KeyStore;
import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.IntentActions;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.common.Adapter;

/**
 * Created by deepanshi on 17/2/17.
 */
public class ChallengeAdapter extends Adapter<Challenge, ChallengeAdapter.ViewHolder> {


    private Context context;
    private Boolean ismSwipeView = false;
    private Integer challengeAmount;

    public ChallengeAdapter(Context context, List<Challenge> challenges) {
        super(context);
        this.context = context;
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
        Challenge challenge = getItem(position);

        holder.mTvChallengeName.setText(challenge.getName());

        challengeAmount = challenge.getChallengeInfo().getPaymentInfo().getChallengeFee();
        if (challengeAmount != null) {
            holder.mTvChallengePrice.setText("Paid - Rs." + challengeAmount.toString());
        } else {
            holder.mTvChallengePrice.setVisibility(View.INVISIBLE);
        }

        holder.mIvChallengeImage.setImageUrl(
                challenge.getImage()
        );

        holder.mIv2xPowerup.setBackground(getPowerupDrawable(R.color.dodger_blue));
        holder.mIvNonegsPowerup.setBackground(getPowerupDrawable(R.color.amaranth));
        holder.mIvPollPowerup.setBackground(getPowerupDrawable(R.color.greencolor));

        HashMap<String, Integer> powerUpMap = challenge.getChallengeUserInfo().getPowerUps();
        Integer powerUp2x = powerUpMap.get(Constants.Powerups.XX);
        Integer powerUpNoNegative = powerUpMap.get(Constants.Powerups.NO_NEGATIVE);
        Integer powerUpAudiencePoll = powerUpMap.get(Constants.Powerups.AUDIENCE_POLL);

        holder.mTv2xPowerupCount.setText(String.valueOf(powerUp2x));
        holder.mTvNonegsPowerupCount.setText(String.valueOf(powerUpNoNegative));
        holder.mTvPollPowerupCount.setText(String.valueOf(powerUpAudiencePoll));

        if (null != challenge.getUserRank()) {
            String rank = AppSnippet.ordinal(challenge.getUserRank());
            holder.mTvChallengeUserRank.setText(rank + " Rank");
        } else {
            holder.mTvChallengeUserRank.setText("Did Not Play");
        }

        if (ismSwipeView) {
            holder.mLlShowGames.setBackgroundColor
                    (context.getResources().getColor(R.color.black));
        } else {
            holder.mLlShowGames.setBackground(context.getResources().getDrawable(R.drawable.shape_challenges_show_game_bg));
        }


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

    public void setIsSwipeView(boolean swipeView) {
        ismSwipeView = swipeView;
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
        LinearLayout mLlShowGames;


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
            mLlTournament = (LinearLayout) V.findViewById(R.id.all_challenges_row_tournament_ll);
            mLlShowGames = (LinearLayout) V.findViewById(R.id.all_challenges_row_ll_show_games);
            mTvChallengeHoursLeft = (Button) V.findViewById(R.id.all_challenges_row_btn_hours_left);
            mTvChallengeDaysLeft = (Button) V.findViewById(R.id.all_challenges_row_btn_days_left);
            mTvChallengeMinsLeft = (Button) V.findViewById(R.id.all_challenges_row_btn_mins_left);

            V.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(IntentActions.ACTION_GROUP_CLICK);
            intent.putExtra(BundleKeys.CLICK_POSITION, getAdapterPosition());
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }


    private Drawable getPowerupDrawable(int colorRes) {
        GradientDrawable powerupDrawable = new GradientDrawable();
        powerupDrawable.setShape(GradientDrawable.RECTANGLE);
        powerupDrawable.setCornerRadius(context.getResources().getDimensionPixelSize(R.dimen.dp_5));
        powerupDrawable.setColor(context.getResources().getColor(colorRes));
        return powerupDrawable;
    }

}