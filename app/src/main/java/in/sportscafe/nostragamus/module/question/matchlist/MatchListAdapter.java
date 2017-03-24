package in.sportscafe.nostragamus.module.question.matchlist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.DateFormats;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.feed.dto.Parties;
import in.sportscafe.nostragamus.module.question.add.AddQuestionActivity;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by Jeeva on 15/6/16.
 */
public class MatchListAdapter extends Adapter<Match, MatchListAdapter.MatchVH> {

    public MatchListAdapter(Context context) {
        super(context);
    }

    @Override
    public Match getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public MatchVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MatchVH(getLayoutInflater().inflate(R.layout.inflater_match_row, parent, false));
    }

    @Override
    public void onBindViewHolder(MatchVH holder, int position) {
        populateMatchDetails(getItem(position), holder);
    }

    private void populateMatchDetails(Match match, MatchVH holder) {
        String startTime = match.getStartTime().replace("+00:00", ".000Z");
        Log.d("StartTime", startTime);
        long startTimeMs = TimeUtils.getMillisecondsFromDateString(
                startTime,
                DateFormats.FORMAT_DATE_T_TIME_ZONE,
                DateFormats.GMT
        );

        int dayOfMonth = Integer.parseInt(TimeUtils.getDateStringFromMs(startTimeMs, "d"));
        // Setting date of the match
        holder.mTvDate.setText(dayOfMonth + AppSnippet.ordinalOnly(dayOfMonth) + " " +
                TimeUtils.getDateStringFromMs(startTimeMs, "MMM") + ", "
                + TimeUtils.getDateStringFromMs(startTimeMs, DateFormats.HH_MM_AA)
        );

        List<Parties> parties = match.getParties();
        holder.mTvPartyAName.setText(parties.get(0).getPartyName());
        holder.mTvPartyBName.setText(parties.get(1).getPartyName());
        holder.mIvPartyAPhoto.setImageUrl(parties.get(0).getPartyImageUrl());
        holder.mIvPartyBPhoto.setImageUrl(parties.get(1).getPartyImageUrl());
        holder.mTvMatchResult.setText(match.getStage());
    }

    class MatchVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mMainView;

        LinearLayout mLlCardLayout;

        TextView mTvPartyAName;

        TextView mTvPartyBName;

        HmImageView mIvPartyAPhoto;

        HmImageView mIvPartyBPhoto;

        TextView mTvDate;

        TextView mTvMatchResult;

        public MatchVH(View V) {
            super(V);
            mMainView = V;
            mLlCardLayout = (LinearLayout) V.findViewById(R.id.schedule_row_ll);
            mTvPartyAName = (TextView) V.findViewById(R.id.schedule_row_tv_party_a_name);
            mTvPartyBName = (TextView) V.findViewById(R.id.schedule_row_tv_party_b_name);
            mIvPartyAPhoto = (HmImageView) V.findViewById(R.id.swipe_card_iv_left);
            mIvPartyBPhoto = (HmImageView) V.findViewById(R.id.swipe_card_iv_right);
            mTvDate = (TextView) V.findViewById(R.id.schedule_row_tv_date);
            mTvMatchResult = (TextView) V.findViewById(R.id.schedule_row_tv_match_result);

            mMainView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();

            Intent intent = new Intent(context, AddQuestionActivity.class);
            intent.putExtra(BundleKeys.MATCH_ID, getItem(getAdapterPosition()).getId());
            context.startActivity(intent);
        }

    }
}