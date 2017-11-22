package in.sportscafe.nostragamus.module.newChallenges.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.SportsDataProvider;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengeMatchesScreenData;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesResponse;
import in.sportscafe.nostragamus.module.newChallenges.dto.SportsTab;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;
import in.sportscafe.nostragamus.module.nostraHome.helper.TimerHelper;

/**
 * Created by sandip on 23/08/17.
 */

public class NewChallengesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<NewChallengesResponse> mNewChallengesResponseList;
    private NewChallengeAdapterListener mChallengeListener;

    public NewChallengesRecyclerAdapter(Context cxt, @NonNull List<NewChallengesResponse> list,
                                        @NonNull NewChallengeAdapterListener listener) {
        mContext = cxt;
        mNewChallengesResponseList = list;
        mChallengeListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = NewChallengeAdapterItemType.CHALLENGE;
        if (mNewChallengesResponseList != null && !mNewChallengesResponseList.isEmpty()) {
            viewType = mNewChallengesResponseList.get(position).getChallengeAdapterItemType();
        }
        return viewType;
    }

    @Override
    public void onViewAttachedToWindow(final RecyclerView.ViewHolder holder) {
        if (holder instanceof NewChallengesItemViewHolder) {
            holder.setIsRecyclable(false);
        }
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case NewChallengeAdapterItemType.CHALLENGE:
                View v1 = inflater.inflate(R.layout.challenge_card_layout, parent, false);
                viewHolder = new NewChallengesItemViewHolder(v1);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder != null) {
            switch (holder.getItemViewType()) {
                case NewChallengeAdapterItemType.CHALLENGE:
                    bindChallengeValues(holder, position);
                    break;
            }
        }
    }

    private void bindChallengeValues(RecyclerView.ViewHolder holder, int position) {
        if (mNewChallengesResponseList != null && mNewChallengesResponseList.size() > position) {
            NewChallengesResponse newChallengesResponse = mNewChallengesResponseList.get(position);
            final NewChallengesItemViewHolder newChallengesItemViewHolder = (NewChallengesItemViewHolder) holder;

            if (newChallengesResponse != null) {
                newChallengesItemViewHolder.challengeNameTextView.setText(newChallengesResponse.getChallengeName());
                newChallengesItemViewHolder.challengeDateTextView.setText(DateTimeHelper.getChallengeDuration(newChallengesResponse.getChallengeStartTime(), newChallengesResponse.getChallengeEndTime()));
                newChallengesItemViewHolder.gameLeftTextView.setText(String.valueOf(newChallengesResponse.getTotalMatches()));
                newChallengesItemViewHolder.prizeTextView.setText(Constants.RUPEE_SYMBOL+String.valueOf(newChallengesResponse.getPrizes()));

                String startTimeStr = newChallengesResponse.getChallengeStartTime();
                if (!TextUtils.isEmpty(startTimeStr)) {
                    if (DateTimeHelper.isTimerRequired(startTimeStr)) {
                        setTimer(newChallengesItemViewHolder, startTimeStr);
                    } else {
                        newChallengesItemViewHolder.startTimeTextView.setText(DateTimeHelper.getStartTime(startTimeStr));
                    }
                }

                if (newChallengesResponse.getTournaments()!=null && !newChallengesResponse.getTournaments().isEmpty()) {
                    setTournaments(newChallengesItemViewHolder.tournamentsLinearLayout, newChallengesResponse.getTournaments());
                }
                setSportsIcons(newChallengesItemViewHolder.gameIconLinearLayout,newChallengesResponse.getSportsIdArray());

            }
        }
    }

    private void setTournaments(LinearLayout tournamentsLinearLayout, List<String> tournamentList) {

        tournamentsLinearLayout.removeAllViews();
        LinearLayout layout2 = new LinearLayout(tournamentsLinearLayout.getContext());
        layout2.setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tournamentsLinearLayout.setOrientation(LinearLayout.VERTICAL);
        tournamentsLinearLayout.addView(layout2);

        String str = "";

        TextView mType;
        ImageView imageView;

        Context mContext = tournamentsLinearLayout.getContext();

        Typeface face = Typeface.createFromAsset(mContext.getAssets(), "fonts/lato/Lato-Regular.ttf");

        if (tournamentList != null && tournamentList.size() > 0) {

                for (int temp = 0; temp < tournamentList.size(); temp++) {

                    LinearLayout childLayout = new LinearLayout(mContext);

                    LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    childLayout.setLayoutParams(linearParams);

                    mType = new TextView(mContext);
                    imageView = new ImageView(mContext);

                    LinearLayout.LayoutParams lpImage = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    lpImage.setMargins(0,2,0,0);
                    imageView.setLayoutParams(lpImage);

                    LinearLayout.LayoutParams lpText = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    lpText.setMargins(0,0,7,0);
                    mType.setLayoutParams(lpText);

                    mType.setTextSize(TypedValue.COMPLEX_UNIT_PX,mContext.getResources().getDimension(R.dimen.sp_9_5));
                    mType.setTextColor(ContextCompat.getColor(mContext,R.color.grey6));
                    mType.setTypeface(face);

                    mType.setText(tournamentList.get(temp));
                    imageView.setImageResource(R.drawable.grey_circle);
                    imageView.getLayoutParams().height = (int)mContext.getResources().getDimension(R.dimen.dim_2);
                    imageView.getLayoutParams().width = (int) mContext.getResources().getDimension(R.dimen.dim_2);

                    childLayout.addView(mType);

                    if (temp != tournamentList.size()-1) {
                        childLayout.addView(imageView);
                    }

                    childLayout.setGravity(Gravity.CENTER);

                    LinearLayout.LayoutParams relativeParams =
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,  LinearLayout.LayoutParams.WRAP_CONTENT);
                    relativeParams.setMargins(0,0,7,0);
                    layout2.addView(childLayout, relativeParams);

                }
        }

    }

    private void setSportsIcons(LinearLayout gameIconLinearLayout, int[] sportsIdArray) {

        gameIconLinearLayout.removeAllViews();
        LinearLayout layout2 = new LinearLayout(gameIconLinearLayout.getContext());
        layout2.setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        gameIconLinearLayout.setOrientation(LinearLayout.VERTICAL);
        gameIconLinearLayout.addView(layout2);

        SportsDataProvider sportsDataProvider = new SportsDataProvider();
        List<SportsTab> sportsTabList = sportsDataProvider.getSportsList();

        for (SportsTab sportsTab : sportsTabList) {
            if (sportsIdArray != null) {
                for (int temp = 0; temp < sportsIdArray.length; temp++) {

                    if (sportsIdArray[temp] == sportsTab.getSportsId()) {

                        ImageView imageView = new ImageView(gameIconLinearLayout.getContext());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(16,0,0,0);
                        imageView.setLayoutParams(lp);
                        imageView.getLayoutParams().height = (int) gameIconLinearLayout.getContext().getResources().getDimension(R.dimen.dim_12);
                        imageView.getLayoutParams().width = (int) gameIconLinearLayout.getContext().getResources().getDimension(R.dimen.dim_12);
                        imageView.setBackgroundResource(sportsTab.getSportIconDrawable());
                        layout2.addView(imageView);

                    }
                }
            }
        }
    }

    private void setTimer(final NewChallengesItemViewHolder newChallengesItemViewHolder, final String startTimeStr) {
        CountDownTimer countDownTimer = new CountDownTimer(TimerHelper.getCountDownFutureTime(startTimeStr), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                newChallengesItemViewHolder.startTimeTextView.setText(TimerHelper.getTimerFormatFromMillis(millisUntilFinished));
            }

            @Override
            public void onFinish() {
            }
        };
        countDownTimer.start();
    }

    private String getTournamentString(List<String> tournamentList) {
        String str = "";
        if (tournamentList != null && tournamentList.size() > 0) {
            if (tournamentList.size() > 1) {
                for (String s : tournamentList) {
                    str = str.concat(" , " + s);
                }
            }else {
                for (String s : tournamentList) {
                    str = s;
                }
            }
        }
        return str.replaceAll(","," ·");
    }

    @Override
    public int getItemCount() {
        return (mNewChallengesResponseList != null) ? mNewChallengesResponseList.size() : 0;
    }

    /* ********************
            View Holders
    ***********************
     */

    private class NewChallengesItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView challengeNameTextView;
        TextView challengeDateTextView;
        TextView startTimeTextView;
        TextView gameLeftTextView;
        TextView prizeTextView;
        LinearLayout gameIconLinearLayout;
        LinearLayout tournamentsLinearLayout;

        public NewChallengesItemViewHolder(View itemView) {
            super(itemView);

            setIsRecyclable(false);
            itemView.setOnClickListener(this);
            challengeNameTextView = (TextView) itemView.findViewById(R.id.challenge_name_textView);
            tournamentsLinearLayout = (LinearLayout) itemView.findViewById(R.id.challenge_tournaments_layout);
            challengeDateTextView = (TextView) itemView.findViewById(R.id.challenge_date_textView);
            startTimeTextView = (TextView) itemView.findViewById(R.id.challenge_start_time_textView);
            gameLeftTextView = (TextView) itemView.findViewById(R.id.challenge_game_left_textView);
            prizeTextView = (TextView) itemView.findViewById(R.id.challenge_prizes_textView);
            gameIconLinearLayout = (LinearLayout) itemView.findViewById(R.id.challenge_sports_icons_container);
        }

        @Override
        public void onClick(View view) {
            int adapterPos = getAdapterPosition();

            if (mChallengeListener != null && mNewChallengesResponseList != null && mNewChallengesResponseList.size() > adapterPos) {
                Bundle args = new Bundle();
                NewChallengeMatchesScreenData screenData = new NewChallengeMatchesScreenData();
                NewChallengesResponse newChallengesResponse = mNewChallengesResponseList.get(adapterPos);

                screenData.setChallengeId(newChallengesResponse.getChallengeId());
                screenData.setChallengeName(newChallengesResponse.getChallengeName());
                screenData.setStartTime(newChallengesResponse.getChallengeStartTime());
                screenData.setMatchesLeft(newChallengesResponse.getMatchesLeft());
                screenData.setTotalMatches(newChallengesResponse.getTotalMatches());

                args.putParcelable(Constants.BundleKeys.NEW_CHALLENGE_MATCHES_SCREEN_DATA, Parcels.wrap(screenData));
                mChallengeListener.onChallengeClicked(args);
                NostragamusAnalytics.getInstance().trackNewChallenges(newChallengesResponse.getChallengeId(),
                        newChallengesResponse.getChallengeName(),newChallengesResponse.getSportsIdArray(),
                        Constants.AnalyticsCategory.NEW_CHALLENGES);
            }
        }
    }
}
