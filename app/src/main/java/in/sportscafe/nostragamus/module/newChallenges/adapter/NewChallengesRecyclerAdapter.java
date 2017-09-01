package in.sportscafe.nostragamus.module.newChallenges.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.newChallenges.adapter.viewHolder.NewChallengesItemViewHolder;
import in.sportscafe.nostragamus.module.newChallenges.dto.NewChallengesResponse;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case NewChallengeAdapterItemType.CHALLENGE:
                View v1 = inflater.inflate(R.layout.challenge_card_layout, parent, false);
                viewHolder = new NewChallengesItemViewHolder(v1, mChallengeListener);
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
            NewChallengesItemViewHolder newChallengesItemViewHolder = (NewChallengesItemViewHolder) holder;

            if (newChallengesResponse != null) {
                newChallengesItemViewHolder.challengeNameTextView.setText(newChallengesResponse.getChallengeName());
                newChallengesItemViewHolder.challengeTournamentTextView.setText(getTournamentString(newChallengesResponse.getTournaments()));
                newChallengesItemViewHolder.challengeDateTextView.setText(getChallengeDuration(newChallengesResponse.getChallengeStartTime(), newChallengesResponse.getChallengeEndTime()));
                newChallengesItemViewHolder.gameLeftTextView.setText(newChallengesResponse.getMatchesLeft() + "/" + newChallengesResponse.getTotalMatches());
                newChallengesItemViewHolder.prizeTextView.setText(WalletHelper.getFormattedStringOfAmount(newChallengesResponse.getPrizes()));
                newChallengesItemViewHolder.startTimeTextView.setText(getStartTime(newChallengesResponse.getChallengeStartTime()));
            }
        }
    }

    private String getChallengeDuration(String startTime, String endTime) {
        String timeDurationStr = "";

        long startMillis = TimeUtils.getMillisecondsFromDateString(startTime);
        long endMillis = TimeUtils.getMillisecondsFromDateString(endTime);

        int startDayOfMonth = Integer.parseInt(TimeUtils.getDateStringFromMs(startMillis, "d"));
        int endDayOfMonth = Integer.parseInt(TimeUtils.getDateStringFromMs(endMillis, "d"));

        timeDurationStr = startDayOfMonth + AppSnippet.ordinalOnly(startDayOfMonth) + " " + TimeUtils.getDateStringFromMs(startMillis, "MMM")
                + " - " +
                endDayOfMonth + AppSnippet.ordinalOnly(endDayOfMonth) + " " + TimeUtils.getDateStringFromMs(endMillis, "MMM");

        return timeDurationStr;
    }

    private String getStartTime(String startTime) {
        String startTimeStr = "";

        long millis = TimeUtils.getMillisecondsFromDateString(startTime);
        if (millis > 0) {
            long days = TimeUtils.getDaysDifference(millis - Calendar.getInstance().getTimeInMillis());
            if (days > 1) {
                startTimeStr = String.valueOf(days + " days");
            } else {
                startTimeStr = TimeUtils.getDateStringFromMs(millis, Constants.DateFormats.CHALLENGE_START_TIME_FORMAT);
            }
        }

        return startTimeStr;
    }

    private String getTournamentString(List<String> tournamentList) {
        String str = "";
        if (tournamentList != null && tournamentList.size() > 0) {
            for (String s : tournamentList) {
                str = str.concat(", " + s);
            }
        }
        return str;
    }

    @Override
    public int getItemCount() {
        return (mNewChallengesResponseList != null) ? mNewChallengesResponseList.size() : 0;
    }


}
