package in.sportscafe.nostragamus.module.allchallenges.challenge;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import java.util.List;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.IntentActions;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.common.Adapter;

/**
 * Created by deepanshi on 23/8/16.
 */
public class ChallengeAdapter extends Adapter<Challenge, ChallengeAdapter.ViewHolder> {

    public ChallengeAdapter(Context context, List<Challenge> challenges) {
        super(context);
        addAll(challenges);
    }

    @Override
    public Challenge getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_all_groups_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Challenge challenge = getItem(position);
        holder.mTvGroupName.setText(challenge.getName());
//        holder.mIvGroupImage.setImageUrl("test");

        holder.mTvGroupTournaments.setText(challenge.getTournaments().size() + " Tournaments");

        holder.mTvGroupMembers.setText(challenge.getMatches().size() + " Matches");
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mMainView;

        TextView mTvGroupName;

        TextView mTvGroupMembers;

        TextView mTvGroupTournaments;

        HmImageView mIvGroupImage;

        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mTvGroupName = (TextView) V.findViewById(R.id.all_groups_tv_groupName);
            mTvGroupTournaments = (TextView) V.findViewById(R.id.all_groups_tv_GroupTournaments);
            mTvGroupMembers = (TextView) V.findViewById(R.id.all_groups_tv_GroupMembers);
            mIvGroupImage = (HmImageView) V.findViewById(R.id.all_groups_iv_groupImage);
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

}