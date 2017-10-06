package in.sportscafe.nostragamus.module.inPlay.adapter;

import android.os.Bundle;
import android.os.CountDownTimer;
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

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.contest.ui.DetailScreensLaunchRequest;
import in.sportscafe.nostragamus.module.customViews.TimelineHelper;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestMatchDto;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayListChallengeItem;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayListItem;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;
import in.sportscafe.nostragamus.utils.timeutils.TimeAgo;
import in.sportscafe.nostragamus.utils.timeutils.TimeUnit;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by deepanshi on 9/6/17.
 */

public class InPlayRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<InPlayListItem> mItemsList;
    private InPlayAdapterListener mInPlayAdapterListener;

    public InPlayRecyclerAdapter(@NonNull List<InPlayListItem> list,
                                 @NonNull InPlayAdapterListener listener) {
        mItemsList = list;
        mInPlayAdapterListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = InPlayAdapterItemType.JOINED_CONTEST;
        if (mItemsList != null && !mItemsList.isEmpty()) {
            viewType = mItemsList.get(position).getInPlayAdapterItemType();
        }
        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case InPlayAdapterItemType.CHALLENGE_ITEM:
                View challengeView = inflater.inflate(R.layout.in_play_challenge_item_layout, parent, false);
                viewHolder = new InPlayChallengeItemViewHolder(challengeView);
                break;

            case InPlayAdapterItemType.JOINED_CONTEST:
                View v1 = inflater.inflate(R.layout.in_play_joined_card_layout, parent, false);
                viewHolder = new InPlayJoinedItemViewHolder(v1);
                break;

            case InPlayAdapterItemType.HEADLESS_CONTEST:
                View v3 = inflater.inflate(R.layout.in_play_headless_card_layout, parent, false);
                viewHolder = new InPlayHeadLessItemViewHolder(v3);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null && mItemsList != null && mItemsList.size() > position) {
            InPlayListItem listItem = mItemsList.get(position);

            switch (listItem.getInPlayAdapterItemType()) {
                case InPlayAdapterItemType.CHALLENGE_ITEM:
                    InPlayListChallengeItem challengeItem = (InPlayListChallengeItem) listItem.getItemData();
                    bindChallengeData(holder, challengeItem);
                    break;

                case InPlayAdapterItemType.JOINED_CONTEST:
                    InPlayContestDto contest = (InPlayContestDto) listItem.getItemData();
                    bindInPlayValues(holder, contest);
                    break;

                case InPlayAdapterItemType.HEADLESS_CONTEST:
                    contest = (InPlayContestDto) listItem.getItemData();
                    bindHeadLessValues(holder, contest);
                    break;
            }
        }
    }

    private void bindChallengeData(RecyclerView.ViewHolder holder, InPlayListChallengeItem challengeItem) {
        if (challengeItem != null) {
            InPlayChallengeItemViewHolder viewHolder = (InPlayChallengeItemViewHolder) holder;

            viewHolder.challengeNameTextView.setText(challengeItem.getChallengeName());
            viewHolder.challengeCountTextView.setText("(" + challengeItem.getContestCount() + ")");
            viewHolder.challengeTournamentTextView.setText(getTournamentString(challengeItem.getChallengeTournaments()));

            if (isChallengeStarted(challengeItem.getChallengeStartTime())) {
                viewHolder.challengeDurationTextView.setText(DateTimeHelper.getChallengeDuration(challengeItem.getChallengeStartTime(),
                        challengeItem.getChallengeEndTime()));
                viewHolder.challengeButtonParent.setVisibility(View.GONE);
            } else {
                viewHolder.challengeButtonTextView.setText("JOIN ANOTHER CONTEST");
                viewHolder.challengeDurationTextView.setVisibility(View.GONE);
            }

        }
    }

    private boolean isChallengeStarted(String challengeStartTime) {
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

    private void bindHeadLessValues(RecyclerView.ViewHolder holder, InPlayContestDto contest) {
        if (contest != null && contest.getMatches() != null) {
            InPlayHeadLessItemViewHolder viewHolder = (InPlayHeadLessItemViewHolder) holder;

            viewHolder.contestTitleTextView.setText("No Contest Joined");
            viewHolder.contestModeImageView.setImageResource(R.drawable.add_members_icon);
            viewHolder.joinContestTextView.setText("Join a contest to play the challenge");

            viewHolder.timelineHeaderParent.removeAllViews();
            viewHolder.timelineContentParent.removeAllViews();
            viewHolder.timelineFooterParent.removeAllViews();

            /* Timeline */
            int totalMatches = contest.getMatches().size();
            for (int temp = 0; temp < totalMatches; temp++) {
                InPlayContestMatchDto match = contest.getMatches().get(temp);

                boolean isNodeLineRequired = true;
                if (temp == 0){
                    isNodeLineRequired =false;
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
                TimelineHelper.addNode(viewHolder.timelineContentParent, match.getStatus(), isPlayed,
                        isNodeLineRequired, TimelineHelper.MatchTimelineTypeEnum.IN_PLAY_HEADLESS, contest.getMatches().size());

                    /* Title */
                TimelineHelper.addTextNode(viewHolder.timelineHeaderParent, "Game " + (temp + 1), contest.getMatches().size(),
                        match.getStatus(), TimelineHelper.MatchTimelineTypeEnum.IN_PLAY_HEADLESS, isPlayed);

                    /* Footer */
                TimelineHelper.addFooterTextNode(viewHolder.timelineFooterParent,
                        DateTimeHelper.getInPlayMatchTime(match.getStartTime()),
                        contest.getMatches().size(), match.getStatus(),
                        TimelineHelper.MatchTimelineTypeEnum.IN_PLAY_HEADLESS,
                        isPlayed, match.getStartTime());
            }
        }
    }

    private void bindInPlayValues(RecyclerView.ViewHolder holder, InPlayContestDto contest) {
        if (contest != null && contest.getMatches() != null) {
            InPlayJoinedItemViewHolder viewHolder = (InPlayJoinedItemViewHolder) holder;

            viewHolder.contestTitleTextView.setText(contest.getContestName());
            viewHolder.entryFeeTextView.setText(Constants.RUPEE_SYMBOL + String.valueOf(contest.getEntryFee()));
            viewHolder.prizesTextView.setText(Constants.RUPEE_SYMBOL + String.valueOf(contest.getWinningAmount()));
            viewHolder.currentRankTextView.setText(contest.getRank() + "/" + contest.getTotalParticipants());

            if (contest.getContestMode().equalsIgnoreCase(Constants.ContestType.GUARANTEED)) {
                viewHolder.contestModeImageView.setBackgroundResource(R.drawable.guaranteed_icon);
            } else if (contest.getContestMode().equalsIgnoreCase(Constants.ContestType.POOL)) {
                viewHolder.contestModeImageView.setBackgroundResource(R.drawable.pool_icon);
            }else if (contest.getContestMode().equalsIgnoreCase(Constants.ContestType.NON_GUARANTEED)) {
                viewHolder.contestModeImageView.setBackgroundResource(R.drawable.no_guarantee_icon);
            }

            viewHolder.timelineHeaderParent.removeAllViews();
            viewHolder.timelineContentParent.removeAllViews();
            viewHolder.timelineFooterParent.removeAllViews();

            /* Timeline */
            int totalMatches = contest.getMatches().size();
            for (int temp = 0; temp < totalMatches; temp++) {
                InPlayContestMatchDto match = contest.getMatches().get(temp);

                boolean isNodeLineRequired = true;
                if (temp == 0){
                    isNodeLineRequired =false;
                }

                int matchAttemptedStatus = match.isPlayed();
                boolean isPlayed;
                if (Constants.GameAttemptedStatus.COMPLETELY == matchAttemptedStatus) {
                    isPlayed = true;
                } else if (Constants.GameAttemptedStatus.PARTIALLY == matchAttemptedStatus) {
                    isPlayed = false;
                } else {
                    isPlayed = false;
                }

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)viewHolder.timelineFooterParent.getLayoutParams();
                params.setMargins(0, 0, 0, 0);

                    /* Content */
                TimelineHelper.addNode(viewHolder.timelineContentParent, match.getStatus(), isPlayed,
                        isNodeLineRequired, TimelineHelper.MatchTimelineTypeEnum.IN_PLAY_JOINED, contest.getMatches().size());

                    /* Title */
                TimelineHelper.addTextNode(viewHolder.timelineHeaderParent, "Game " + (temp + 1), contest.getMatches().size(),
                        match.getStatus(), TimelineHelper.MatchTimelineTypeEnum.IN_PLAY_JOINED, isPlayed);

                /* Footer */
                String footerStr = DateTimeHelper.getInPlayMatchTime(match.getStartTime());
                if (match.getStatus().equalsIgnoreCase(Constants.InPlayMatchStatus.COMPLETED)) {
                    if (isPlayed) {
                         footerStr = String.valueOf(match.getScore()) + " Points";
                    } else {
                        footerStr = "   DNP    ";
                    }
                    params.setMargins(10, 0, 0, 0);
                }else if (match.getStatus().equalsIgnoreCase(Constants.InPlayMatchStatus.LIVE)){
                    footerStr = "In Progress";
                }

                TimelineHelper.addFooterTextNode(viewHolder.timelineFooterParent, footerStr,
                        contest.getMatches().size(), match.getStatus(),
                        TimelineHelper.MatchTimelineTypeEnum.IN_PLAY_JOINED,
                        isPlayed, match.getStartTime());

                /* Layout params */
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
            for (InPlayListItem inPlayListItem : mItemsList) {
                if (inPlayListItem.getItemData() instanceof InPlayContestDto) {
                    InPlayContestDto inPlayContestDto = (InPlayContestDto) inPlayListItem.getItemData();

                    if (inPlayContestDto != null && inPlayContestDto.getContestId() == contestId) {
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

    /* ================== View Holders ===================== */

    private class InPlayJoinedItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout root;
        LinearLayout timelineHeaderParent;
        LinearLayout timelineContentParent;
        LinearLayout timelineFooterParent;
        LinearLayout currentRankLayout;
        LinearLayout prizesLayout;
        TextView contestTitleTextView;
        ImageView contestModeImageView;
        TextView entryFeeTextView;
        TextView currentRankTextView;
        TextView prizesTextView;

        public InPlayJoinedItemViewHolder(View itemView) {
            super(itemView);
            root = (LinearLayout) itemView.findViewById(R.id.in_play_joined_card_parent);
            timelineHeaderParent = (LinearLayout) itemView.findViewById(R.id.inplay_joined_card_timeline_heading_parent);
            timelineContentParent = (LinearLayout) itemView.findViewById(R.id.inplay_joined_card_timeline_content_parent);
            timelineFooterParent = (LinearLayout) itemView.findViewById(R.id.inplay_joined_card_timeline_footer_parent);
            currentRankLayout = (LinearLayout) itemView.findViewById(R.id.inplay_contest_list_current_rank_layout);
            prizesLayout = (LinearLayout) itemView.findViewById(R.id.inplay_contest_list_prizes_layout);
            contestTitleTextView = (TextView) itemView.findViewById(R.id.inplay_joined_card_title_textView);
            contestModeImageView = (ImageView) itemView.findViewById(R.id.inplay_contest_card_header_mode_imgView);
            entryFeeTextView = (TextView) itemView.findViewById(R.id.inplay_contest_card_header_entry_fee_textView);
            currentRankTextView = (TextView) itemView.findViewById(R.id.inplay_contest_card_header_current_rank_textView);
            prizesTextView = (TextView) itemView.findViewById(R.id.inplay_contest_card_header_prizes_textView);
            root.setOnClickListener(this);
            currentRankLayout.setOnClickListener(this);
            prizesLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.in_play_joined_card_parent:
                    if (mInPlayAdapterListener != null) {
                        Bundle args = getContestDataBundle();
                        mInPlayAdapterListener.onJoinedContestCardClicked(args);
                    }
                    break;

                case R.id.inplay_contest_list_current_rank_layout:
                    if (mInPlayAdapterListener != null) {
                        Bundle args = getContestDataBundle();
                        args.putInt(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, DetailScreensLaunchRequest.MATCHES_LEADER_BOARD_SCREEN);
                        mInPlayAdapterListener.onJoinedContestCurrentRankClicked(args);
                    }
                    break;

                case R.id.inplay_contest_list_prizes_layout:
                    if (mInPlayAdapterListener != null) {
                        Bundle args = getContestDataBundle();
                        args.putInt(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, DetailScreensLaunchRequest.MATCHES_REWARDS_SCREEN);
                        mInPlayAdapterListener.onJoinedContestPrizesClicked(args);
                    }
                    break;
            }
        }

        @NonNull
        private Bundle getContestDataBundle() {
            Bundle args = new Bundle();
            InPlayListItem listItem = mItemsList.get(getAdapterPosition());
            if (listItem != null && listItem.getInPlayAdapterItemType() == InPlayAdapterItemType.JOINED_CONTEST) {
                InPlayContestDto contestDto = (InPlayContestDto) listItem.getItemData();
                args.putParcelable(Constants.BundleKeys.INPLAY_CONTEST, Parcels.wrap(contestDto));
            }
            return args;
        }
    }

    private class InPlayHeadLessItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout root;
        LinearLayout timelineHeaderParent;
        LinearLayout timelineContentParent;
        LinearLayout timelineFooterParent;
        TextView contestTitleTextView;
        ImageView contestModeImageView;
        TextView joinContestTextView;
        TextView currentRankTextView;
        TextView prizesTextView;

        public InPlayHeadLessItemViewHolder(View itemView) {
            super(itemView);
            root = (LinearLayout) itemView.findViewById(R.id.in_play_headless_card_parent);
            timelineHeaderParent = (LinearLayout) itemView.findViewById(R.id.inplay_headless_card_timeline_heading_parent);
            timelineContentParent = (LinearLayout) itemView.findViewById(R.id.inplay_headless_card_timeline_content_parent);
            timelineFooterParent = (LinearLayout) itemView.findViewById(R.id.inplay_headless_card_timeline_footer_parent);
            contestTitleTextView = (TextView) itemView.findViewById(R.id.inplay_headless_card_title_textView);
            contestModeImageView = (ImageView) itemView.findViewById(R.id.inplay_headless_card_header_mode_imgView);
            joinContestTextView = (TextView) itemView.findViewById(R.id.inplay_contest_card_header_join_contest_textView);
            currentRankTextView = (TextView) itemView.findViewById(R.id.inplay_contest_card_header_current_rank_textView);
            prizesTextView = (TextView) itemView.findViewById(R.id.inplay_contest_card_header_prizes_textView);

            root.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.in_play_headless_card_parent:
                    if (mInPlayAdapterListener != null) {
                        Bundle args = getHeadLessContestDataBundle();

                        mInPlayAdapterListener.onHeadLessContestCardClicked(args, getHeadLessContest());
                    }
                    break;
            }
        }

        @NonNull
        private Bundle getHeadLessContestDataBundle() {
            Bundle args = new Bundle();
            InPlayListItem listItem = mItemsList.get(getAdapterPosition());
            if (listItem != null && listItem.getInPlayAdapterItemType() == InPlayAdapterItemType.HEADLESS_CONTEST) {
                InPlayContestDto contestDto = (InPlayContestDto) listItem.getItemData();
                args.putParcelable(Constants.BundleKeys.INPLAY_CONTEST, Parcels.wrap(contestDto));
            }
            return args;
        }

        @NonNull
        private InPlayContestDto getHeadLessContest() {
            InPlayContestDto inPlayContestDto = null;
            InPlayListItem listItem = mItemsList.get(getAdapterPosition());
            if (listItem != null && listItem.getInPlayAdapterItemType() == InPlayAdapterItemType.HEADLESS_CONTEST) {
                 inPlayContestDto = (InPlayContestDto) listItem.getItemData();
            }
            return inPlayContestDto;
        }
    }

    private class InPlayChallengeItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView challengeNameTextView;
        TextView challengeCountTextView;
        TextView challengeTournamentTextView;
        LinearLayout challengeButton;
        TextView challengeButtonTextView;
        TextView challengeDurationTextView;
        LinearLayout challengeButtonParent;

        public InPlayChallengeItemViewHolder(View itemView) {
            super(itemView);
            challengeNameTextView = (TextView) itemView.findViewById(R.id.inplay_challenge_title_textView);
            challengeCountTextView = (TextView) itemView.findViewById(R.id.inplay_challenge_count_textView);
            challengeTournamentTextView = (TextView) itemView.findViewById(R.id.inplay_challenge_tournaments_textView);
            challengeButtonTextView = (TextView) itemView.findViewById(R.id.inplay_challenge_button_textView);
            challengeButton = (LinearLayout) itemView.findViewById(R.id.inplay_challenge_button);
            challengeDurationTextView = (TextView) itemView.findViewById(R.id.inplay_challenge_duration_tv);
            challengeButtonParent = (LinearLayout) itemView.findViewById(R.id.inplay_challenge_button);
            challengeButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.inplay_challenge_button:
                    if (mInPlayAdapterListener != null) {
                        Bundle args = new Bundle();
                        InPlayListItem listItem = mItemsList.get(getAdapterPosition());
                        if (listItem != null && listItem.getInPlayAdapterItemType() == InPlayAdapterItemType.CHALLENGE_ITEM) {
                            InPlayListChallengeItem challengeItem = (InPlayListChallengeItem) listItem.getItemData();
                            args.putParcelable(Constants.BundleKeys.INPLAY_CHALLENGE_LIST_ITEM, Parcels.wrap(challengeItem));
                        }

                        mInPlayAdapterListener.onJoinAnotherContestClicked(args);
                    }
                    break;
            }
        }
    }
}
