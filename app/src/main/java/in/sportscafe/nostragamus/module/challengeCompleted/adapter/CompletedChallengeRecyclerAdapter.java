package in.sportscafe.nostragamus.module.challengeCompleted.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedContestDto;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedContestMatchDto;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedListChallengeItem;
import in.sportscafe.nostragamus.module.challengeCompleted.dto.CompletedListItem;
import in.sportscafe.nostragamus.module.contest.ui.DetailScreensLaunchRequest;
import in.sportscafe.nostragamus.module.customViews.TimelineHelper;
import in.sportscafe.nostragamus.module.inPlay.adapter.InPlayAdapterItemType;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayListChallengeItem;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayListItem;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;
import in.sportscafe.nostragamus.utils.timeutils.TimeAgo;
import in.sportscafe.nostragamus.utils.timeutils.TimeUnit;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by deepanshi on 9/27/17.
 */

public class CompletedChallengeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CompletedListItem> mItemsList;
    private CompletedChallengeAdapterListener mCompletedAdapterListener;

    public CompletedChallengeRecyclerAdapter(@NonNull List<CompletedListItem> list,
                                             @NonNull CompletedChallengeAdapterListener listener) {
        mItemsList = list;
        mCompletedAdapterListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = CompletedChallengeAdapterItemType.COMPLETED_CONTEST;
        if (mItemsList != null && !mItemsList.isEmpty()) {
            viewType = mItemsList.get(position).getCompletedAdapterItemType();
        }
        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case CompletedChallengeAdapterItemType.CHALLENGE_ITEM:
                View challengeView = inflater.inflate(R.layout.completed_challenge_item_layout, parent, false);
                viewHolder = new CompletedChallengeItemViewHolder(challengeView);
                break;

            case CompletedChallengeAdapterItemType.COMPLETED_CONTEST:
                View v2 = inflater.inflate(R.layout.completed_contest_card_layout, parent, false);
                viewHolder = new CompletedContestItemViewHolder(v2);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null && mItemsList != null && mItemsList.size() > position) {
            CompletedListItem listItem = mItemsList.get(position);

            switch (listItem.getCompletedAdapterItemType()) {
                case CompletedChallengeAdapterItemType.CHALLENGE_ITEM:
                    CompletedListChallengeItem challengeItem = (CompletedListChallengeItem) listItem.getItemData();
                    bindChallengeData(holder, challengeItem);
                    break;

                case CompletedChallengeAdapterItemType.COMPLETED_CONTEST:
                    CompletedContestDto contest = (CompletedContestDto) listItem.getItemData();
                    bindCompletedValues(holder, contest);
                    break;
            }
        }
    }

    private void bindChallengeData(RecyclerView.ViewHolder holder, CompletedListChallengeItem challengeItem) {
        if (challengeItem != null) {
            CompletedChallengeRecyclerAdapter.CompletedChallengeItemViewHolder viewHolder = (CompletedChallengeRecyclerAdapter.CompletedChallengeItemViewHolder) holder;

            viewHolder.challengeNameTextView.setText(challengeItem.getChallengeName());
            viewHolder.challengeCountTextView.setText("(" + challengeItem.getContestCount() + ")");
            viewHolder.challengeTournamentTextView.setText(getTournamentString(challengeItem.getChallengeTournaments()));

            if (getChallengeStarted(challengeItem.getChallengeStartTime())) {
                viewHolder.challengeDurationTextView.setText(DateTimeHelper.getChallengeDuration(challengeItem.getChallengeStartTime(),
                        challengeItem.getChallengeEndTime()));
                viewHolder.challengeButtonParent.setVisibility(View.GONE);
            } else {
                viewHolder.challengeButtonTextView.setText("JOIN ANOTHER CONTEST");
                viewHolder.challengeDurationTextView.setVisibility(View.GONE);
            }

        }
    }

    private boolean getChallengeStarted(String challengeStartTime) {

        boolean isChallengeStarted = false;

        if (!TextUtils.isEmpty(challengeStartTime)) {
            String startTime = challengeStartTime.replace("+00:00", ".000Z");
            long startTimeMs = TimeUtils.getMillisecondsFromDateString(
                    startTime,
                    Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                    Constants.DateFormats.GMT
            );
            TimeAgo timeAgo = TimeUtils.calcTimeAgo(Nostragamus.getInstance().getServerTime(), startTimeMs);

            isChallengeStarted = timeAgo.timeDiff <= 0
                    || timeAgo.timeUnit == TimeUnit.MILLISECOND
                    || timeAgo.timeUnit == TimeUnit.SECOND;
        }

        return isChallengeStarted;
    }

    private String getTournamentString(List<String> tournamentList) {
        String str = "";
        if (tournamentList != null && tournamentList.size() > 0) {
            if (tournamentList.size() > 1) {
                for (String s : tournamentList) {
                    str = str.concat(" , " + s);
                }
            } else {
                for (String s : tournamentList) {
                    str = s;
                }
            }
        }
        return str.replaceAll(",", " ·");
    }

    private void bindCompletedValues(RecyclerView.ViewHolder holder, CompletedContestDto contest) {
        if (contest != null && contest.getMatches() != null) {
            CompletedContestItemViewHolder viewHolder = (CompletedContestItemViewHolder) holder;

            viewHolder.contestTitleTextView.setText(contest.getContestName());
            viewHolder.contestModeImageView.setImageResource(R.drawable.add_members_icon);
            viewHolder.entryFeeTextView.setText(Constants.RUPEE_SYMBOL + String.valueOf(contest.getEntryFee()));
            viewHolder.prizesTextView.setText(Constants.RUPEE_SYMBOL + String.valueOf(contest.getWinningAmount()));
            viewHolder.currentRankTextView.setText(contest.getRank() + "/" + contest.getTotalParticipants());

            viewHolder.timelineHeaderParent.removeAllViews();
            viewHolder.timelineContentParent.removeAllViews();
            viewHolder.timelineFooterParent.removeAllViews();

            /* Timeline */
            int totalMatches = contest.getMatches().size();
            for (int temp = 0; temp < totalMatches; temp++) {
                CompletedContestMatchDto match = contest.getMatches().get(temp);

                boolean isNodeLineRequired = true;
                if (temp == (totalMatches - 1)) {
                    isNodeLineRequired = false;
                }

                int matchAttemptedStatus = match.isPlayed();
                boolean isMatchCompleted;
                boolean isPlayed;
                if (Constants.GameAttemptedStatus.COMPLETELY == matchAttemptedStatus) {
                    isPlayed = true;
                } else if (Constants.GameAttemptedStatus.PARTIALLY == matchAttemptedStatus) {
                    isPlayed = false;
                } else {
                    isPlayed = false;
                }

                    /* Content */
                String matchPoints;
                if (isPlayed) {
                    matchPoints = String.valueOf(match.getScore()) + " Points";
                }else {
                    matchPoints = "   DNP    ";
                }

                       /* Content */
                TimelineHelper.addNode(viewHolder.timelineContentParent, match.getStatus(), isPlayed,
                        isNodeLineRequired, TimelineHelper.MatchTimelineTypeEnum.IN_PLAY_JOINED, contest.getMatches().size());


                    /* Title */
                TimelineHelper.addTextNode(viewHolder.timelineHeaderParent, "Game " + (temp + 1), contest.getMatches().size(), match.getStatus(),
                        TimelineHelper.MatchTimelineTypeEnum.IN_PLAY_JOINED, isPlayed);

                    /* Footer */
                TimelineHelper.addFooterTextNode(viewHolder.timelineFooterParent, matchPoints,
                        contest.getMatches().size(), match.getStatus(),
                        TimelineHelper.MatchTimelineTypeEnum.IN_PLAY_JOINED, isPlayed,
                        match.getStartTime());

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)viewHolder.timelineFooterParent.getLayoutParams();
                params.setMargins(10, 0, 0, 0);
                viewHolder.timelineFooterParent.setLayoutParams(params);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mItemsList != null) ? mItemsList.size() : 0;
    }


    public int getAdapterPositionFromContestId(int contestId) {
        int counter = -1;
        boolean isFound = false;
        if (mItemsList != null) {
            for (CompletedListItem CompletedListItem : mItemsList) {
                if (CompletedListItem.getItemData() instanceof CompletedContestDto) {
                    CompletedContestDto CompletedContestDto = (CompletedContestDto) CompletedListItem.getItemData();

                    if (CompletedContestDto != null && CompletedContestDto.getContestId() == contestId) {
                        isFound = true;
                    }
                }
                counter++;
            }
        }

        if (!isFound) {
            counter = -1;
        }

        return counter;
    }

    private class CompletedContestItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout root;
        LinearLayout timelineHeaderParent;
        LinearLayout timelineContentParent;
        LinearLayout timelineFooterParent;
        TextView contestTitleTextView;
        ImageView contestModeImageView;
        TextView entryFeeTextView;
        TextView currentRankTextView;
        TextView prizesTextView;


        public CompletedContestItemViewHolder(View itemView) {
            super(itemView);
            root = (LinearLayout) itemView.findViewById(R.id.completed_completed_card_parent);
            timelineHeaderParent = (LinearLayout) itemView.findViewById(R.id.completed_completed_card_timeline_heading_parent);
            timelineContentParent = (LinearLayout) itemView.findViewById(R.id.completed_completed_card_timeline_content_parent);
            timelineFooterParent = (LinearLayout) itemView.findViewById(R.id.completed_completed_card_timeline_footer_parent);
            contestTitleTextView = (TextView) itemView.findViewById(R.id.completed_completed_card_title_textView);
            contestModeImageView = (ImageView) itemView.findViewById(R.id.completed_contest_card_header_mode_imgView);
            entryFeeTextView = (TextView) itemView.findViewById(R.id.completed_contest_card_header_entry_fee_textView);
            currentRankTextView = (TextView) itemView.findViewById(R.id.completed_contest_card_header_current_rank_textView);
            prizesTextView = (TextView) itemView.findViewById(R.id.completed_contest_card_header_prizes_textView);
            itemView.findViewById(R.id.completed_challenge_rank_layout).setOnClickListener(this);
            itemView.findViewById(R.id.completed_challenge_winnings_layout).setOnClickListener(this);
            root.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.completed_completed_card_parent:
                    if (mCompletedAdapterListener != null) {
                        Bundle args = getContestDataBundle();
                        mCompletedAdapterListener.onCompletedCardClicked(args);
                    }
                    break;

                case R.id.completed_challenge_rank_layout:
                    if (mCompletedAdapterListener != null) {
                        Bundle args = getContestDataBundle();
                        args.putInt(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, DetailScreensLaunchRequest.MATCHES_LEADER_BOARD_SCREEN);
                        mCompletedAdapterListener.onCompletedContestCurrentRankClicked(args);
                    }
                    break;

                case R.id.completed_challenge_winnings_layout:
                    if (mCompletedAdapterListener != null) {
                        Bundle args = getContestDataBundle();
                        args.putInt(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, DetailScreensLaunchRequest.MATCHES_REWARDS_SCREEN);
                        mCompletedAdapterListener.onCompletedWinningClicked(args);
                    }
                    break;

            }
        }

        @NonNull
        private Bundle getContestDataBundle() {
            Bundle args = new Bundle();
            CompletedListItem listItem = mItemsList.get(getAdapterPosition());
            if (listItem != null && listItem.getCompletedAdapterItemType() == CompletedChallengeAdapterItemType.COMPLETED_CONTEST) {
                CompletedContestDto contestDto = (CompletedContestDto) listItem.getItemData();
                args.putParcelable(Constants.BundleKeys.COMPLETED_CONTEST, Parcels.wrap(contestDto));
            }
            return args;
        }
    }

    private class CompletedChallengeItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView challengeNameTextView;
        TextView challengeCountTextView;
        TextView challengeTournamentTextView;
        TextView challengeButtonTextView;
        TextView challengeDurationTextView;
        LinearLayout challengeButtonParent;

        public CompletedChallengeItemViewHolder(View itemView) {
            super(itemView);
            challengeNameTextView = (TextView) itemView.findViewById(R.id.completed_challenge_title_textView);
            challengeCountTextView = (TextView) itemView.findViewById(R.id.completed_challenge_count_textView);
            challengeTournamentTextView = (TextView) itemView.findViewById(R.id.completed_challenge_tournaments_textView);
            challengeButtonTextView = (TextView) itemView.findViewById(R.id.completed_challenge_button_textView);
            challengeDurationTextView = (TextView) itemView.findViewById(R.id.completed_challenge_duration_tv);
            challengeButtonParent = (LinearLayout) itemView.findViewById(R.id.completed_challenge_button);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.inplay_challenge_button:
            }
        }
    }
}
