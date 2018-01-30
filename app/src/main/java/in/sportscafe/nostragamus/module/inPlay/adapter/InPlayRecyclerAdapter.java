package in.sportscafe.nostragamus.module.inPlay.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
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
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.contest.ui.DetailScreensLaunchRequest;
import in.sportscafe.nostragamus.module.customViews.TimelineHelper;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestMatchDto;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayListChallengeItem;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayListItem;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;
import in.sportscafe.nostragamus.utils.CodeSnippet;
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

            if (challengeItem.getChallengeTournaments() != null && !challengeItem.getChallengeTournaments().isEmpty()) {
                setTournaments(viewHolder.challengeTournamentsLayout, challengeItem.getChallengeTournaments());
            }

            if (isChallengeStarted(challengeItem.getChallengeStartTime())) {
                viewHolder.challengeDurationTextView.setText(DateTimeHelper.getChallengeDuration(challengeItem.getChallengeStartTime(),
                        challengeItem.getChallengeEndTime()));
                viewHolder.challengeButtonParent.setVisibility(View.GONE);
            } else if (challengeItem.isOnlyHeadlessStateExist()) {
                viewHolder.challengeButtonParent.setVisibility(View.GONE);
            } else {
                viewHolder.challengeButtonParent.setVisibility(View.VISIBLE);
                viewHolder.challengeButtonTextView.setText("JOIN ANOTHER CONTEST");
                viewHolder.challengeDurationTextView.setVisibility(View.GONE);
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

        TextView tournamentName;
        ImageView tournamentImageView;

        Context mContext = tournamentsLinearLayout.getContext();
        Typeface face = Typeface.createFromAsset(mContext.getAssets(), "fonts/lato/Lato-Regular.ttf");

        if (tournamentList != null && tournamentList.size() > 0) {

            for (int temp = 0; temp < tournamentList.size(); temp++) {

                LinearLayout childLayout = new LinearLayout(mContext);

                LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                childLayout.setLayoutParams(linearParams);

                tournamentName = new TextView(mContext);
                tournamentImageView = new ImageView(mContext);

                LinearLayout.LayoutParams lpImage = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lpImage.setMargins(0, 2, 0, 0);
                tournamentImageView.setLayoutParams(lpImage);

                LinearLayout.LayoutParams lpText = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                lpText.setMargins(0, 0, 8, 0);
                tournamentName.setLayoutParams(lpText);

                tournamentName.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp_9_5));
                tournamentName.setTextColor(ContextCompat.getColor(mContext, R.color.grey6));
                tournamentName.setTypeface(face);

                tournamentName.setText(tournamentList.get(temp));
                tournamentImageView.setImageResource(R.drawable.grey_circle);
                tournamentImageView.getLayoutParams().height = (int) mContext.getResources().getDimension(R.dimen.dim_2);
                tournamentImageView.getLayoutParams().width = (int) mContext.getResources().getDimension(R.dimen.dim_2);

                childLayout.addView(tournamentName);

                if (temp != tournamentList.size() - 1) {
                    childLayout.addView(tournamentImageView);
                }

                childLayout.setGravity(Gravity.CENTER);

                LinearLayout.LayoutParams relativeParams =
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                relativeParams.setMargins(0, 0, 8, 0);
                layout2.addView(childLayout, relativeParams);

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

            viewHolder.contestTitleTextView.setText("Join a contest and continue playing");
            viewHolder.contestModeImageView.setBackgroundResource(R.drawable.exclamation_mark);
            viewHolder.joinContestTextView.setText("You can only win prizes once you join a contest");

            viewHolder.timelineHeaderParent.removeAllViews();
            viewHolder.timelineContentParent.removeAllViews();
            viewHolder.timelineFooterParent.removeAllViews();

            /* Timeline */
            int totalMatches = contest.getMatches().size();
            for (int temp = 0; temp < totalMatches; temp++) {
                InPlayContestMatchDto match = contest.getMatches().get(temp);

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

                boolean isNodeLineRequired = true;
                if (temp == 0) {
                    isNodeLineRequired = false;
                }

                    /* Content */
                TimelineHelper.addNode(viewHolder.timelineContentParent, match.getStatus(), matchAttemptedStatus, isPlayed,
                        isNodeLineRequired, TimelineHelper.MatchTimelineTypeEnum.IN_PLAY_HEADLESS, contest.getMatches().size());

                    /* Title */
                TimelineHelper.addTextNode(viewHolder.timelineHeaderParent, "Game " + (temp + 1), contest.getMatches().size(),
                        match.getStatus(), TimelineHelper.MatchTimelineTypeEnum.IN_PLAY_HEADLESS, isPlayed, matchAttemptedStatus);

                    /* Footer */
                TimelineHelper.addFooterTextNode(viewHolder.timelineFooterParent,
                        DateTimeHelper.getInPlayMatchTime(match.getStartTime()),
                        contest.getMatches().size(), match.getStatus(),
                        TimelineHelper.MatchTimelineTypeEnum.IN_PLAY_HEADLESS,
                        isPlayed, match.getStartTime(), matchAttemptedStatus);
            }
        }
    }

    private void bindInPlayValues(RecyclerView.ViewHolder holder, InPlayContestDto contest) {
        if (contest != null && contest.getMatches() != null) {
            InPlayJoinedItemViewHolder viewHolder = (InPlayJoinedItemViewHolder) holder;

            viewHolder.contestTitleTextView.setText(contest.getContestName());
            viewHolder.entryFeeTextView.setText(Constants.RUPEE_SYMBOL + String.valueOf(contest.getEntryFee()));

            double winningAmount = contest.getWinningAmount();
            int winningAmountValue = (int) winningAmount;

            viewHolder.prizesTextView.setText(Constants.RUPEE_SYMBOL + CodeSnippet.getFormattedAmount(winningAmountValue));

            if (contest.getRank() > 0) {
                viewHolder.currentRankTextView.setText(AppSnippet.ordinal(contest.getRank()));
            } else {
                viewHolder.currentRankTextView.setText("NA");
            }

            if (contest.getTotalParticipants() > 0) {
                viewHolder.totalPlayersTextView.setText(" / " + contest.getTotalParticipants());
            }

            if (contest.getContestMode().equalsIgnoreCase(Constants.ContestType.GUARANTEED)) {
                viewHolder.contestModeImageView.setImageResource(R.drawable.guaranteed_icon);
            } else if (contest.getContestMode().equalsIgnoreCase(Constants.ContestType.POOL)) {
                viewHolder.contestModeImageView.setImageResource(R.drawable.pool_icon);
            } else if (contest.getContestMode().equalsIgnoreCase(Constants.ContestType.NON_GUARANTEED)) {
                viewHolder.contestModeImageView.setImageResource(R.drawable.no_guarantee_icon);
            }

            viewHolder.timelineHeaderParent.removeAllViews();
            viewHolder.timelineContentParent.removeAllViews();
            viewHolder.timelineFooterParent.removeAllViews();

            /* Timeline */
            int totalMatches = contest.getMatches().size();
            for (int temp = 0; temp < totalMatches; temp++) {
                InPlayContestMatchDto match = contest.getMatches().get(temp);

                boolean isNodeLineRequired = true;
                if (temp == 0) {
                    isNodeLineRequired = false;
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

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.timelineFooterParent.getLayoutParams();
                params.setMargins(0, 0, 0, 0);

                    /* Content */
                TimelineHelper.addNode(viewHolder.timelineContentParent, match.getStatus(), matchAttemptedStatus, isPlayed,
                        isNodeLineRequired, TimelineHelper.MatchTimelineTypeEnum.IN_PLAY_JOINED, contest.getMatches().size());

                    /* Title */
                TimelineHelper.addTextNode(viewHolder.timelineHeaderParent, "Game " + (temp + 1), contest.getMatches().size(),
                        match.getStatus(), TimelineHelper.MatchTimelineTypeEnum.IN_PLAY_JOINED, isPlayed, matchAttemptedStatus);

                /* Footer */
                String footerStr = DateTimeHelper.getInPlayMatchTime(match.getStartTime());
                if (match.getStatus().equalsIgnoreCase(Constants.InPlayMatchStatus.COMPLETED)) {

                    if (Constants.GameAttemptedStatus.COMPLETELY == matchAttemptedStatus ||
                            Constants.GameAttemptedStatus.PARTIALLY == matchAttemptedStatus) {
                        footerStr = String.valueOf(match.getScore()) + " Points";
                    } else {
                        footerStr = "   DNP    ";
                    }

                    params.setMargins(10, 0, 0, 0);

                } else if (match.getStatus().equalsIgnoreCase(Constants.InPlayMatchStatus.LIVE)) {
                    footerStr = "In Progress";
                }

                TimelineHelper.addFooterTextNode(viewHolder.timelineFooterParent, footerStr,
                        contest.getMatches().size(), match.getStatus(),
                        TimelineHelper.MatchTimelineTypeEnum.IN_PLAY_JOINED,
                        isPlayed, match.getStartTime(), matchAttemptedStatus);

                /* Layout params */
                viewHolder.timelineFooterParent.setLayoutParams(params);
            }
        }
    }

    /*
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
                        lp.setMargins(16, 0, 0, 0);
                        imageView.setLayoutParams(lp);
                        imageView.getLayoutParams().height = (int) gameIconLinearLayout.getContext().getResources().getDimension(R.dimen.dim_12);
                        imageView.getLayoutParams().width = (int) gameIconLinearLayout.getContext().getResources().getDimension(R.dimen.dim_12);
                        imageView.setBackgroundResource(sportsTab.getSportIconDrawable());
                        layout2.addView(imageView);

                    }
                }
            }
        }
    } */


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
                        break;
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
        TextView totalPlayersTextView;
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
            totalPlayersTextView = (TextView) itemView.findViewById(R.id.inplay_contest_card_header_total_players);
            root.setOnClickListener(this);
            currentRankLayout.setOnClickListener(this);
            prizesLayout.setOnClickListener(this);
            contestModeImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.in_play_joined_card_parent:
                    if (mInPlayAdapterListener != null) {
                        Bundle args = getContestDataBundle();
                        mInPlayAdapterListener.onJoinedContestCardClicked(args);
                        NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.IN_PLAY, Constants.AnalyticsClickLabels.JOINED_CARD);
                    }
                    break;

                case R.id.inplay_contest_list_current_rank_layout:
                    if (mInPlayAdapterListener != null) {
                        Bundle args = getContestDataBundle();
                        args.putInt(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, DetailScreensLaunchRequest.MATCHES_LEADER_BOARD_SCREEN);
                        mInPlayAdapterListener.onJoinedContestCurrentRankClicked(args);
                        NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.IN_PLAY, Constants.AnalyticsClickLabels.RANK);
                    }
                    break;

                case R.id.inplay_contest_list_prizes_layout:
                    if (mInPlayAdapterListener != null) {
                        Bundle args = getContestDataBundle();
                        args.putInt(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, DetailScreensLaunchRequest.MATCHES_REWARDS_SCREEN);
                        mInPlayAdapterListener.onJoinedContestPrizesClicked(args);
                        NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.IN_PLAY, Constants.AnalyticsClickLabels.PRIZES);
                    }
                    break;

                case R.id.inplay_contest_card_header_mode_imgView:
                    if (mInPlayAdapterListener != null) {
                        Bundle args = getContestDataBundle();
                        args.putInt(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, DetailScreensLaunchRequest.MATCHES_RULES_SCREEN);
                        mInPlayAdapterListener.onContestModeClicked(args);
                        NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.IN_PLAY, Constants.AnalyticsClickLabels.MODE);
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
                        NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.IN_PLAY, Constants.AnalyticsClickLabels.HEADLESS_CARD);
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
        LinearLayout challengeTournamentsLayout;
        LinearLayout challengeButton;
        TextView challengeButtonTextView;
        TextView challengeDurationTextView;
        LinearLayout challengeButtonParent;
        LinearLayout gameIconLinearLayout;

        public InPlayChallengeItemViewHolder(View itemView) {
            super(itemView);
            challengeNameTextView = (TextView) itemView.findViewById(R.id.inplay_challenge_title_textView);
            challengeCountTextView = (TextView) itemView.findViewById(R.id.inplay_challenge_count_textView);
            challengeTournamentsLayout = (LinearLayout) itemView.findViewById(R.id.inplay_challenge_tournaments_layout);
            challengeButtonTextView = (TextView) itemView.findViewById(R.id.inplay_challenge_button_textView);
            challengeButton = (LinearLayout) itemView.findViewById(R.id.inplay_challenge_button);
            challengeDurationTextView = (TextView) itemView.findViewById(R.id.inplay_challenge_duration_tv);
            challengeButtonParent = (LinearLayout) itemView.findViewById(R.id.inplay_challenge_button);
            gameIconLinearLayout = (LinearLayout) itemView.findViewById(R.id.inplay_challenge_sports_icons_container);
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
                        NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.IN_PLAY, Constants.AnalyticsClickLabels.JOIN_CONTEST);
                    }
                    break;
            }
        }
    }
}
