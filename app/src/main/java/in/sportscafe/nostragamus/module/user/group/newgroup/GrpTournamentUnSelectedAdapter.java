package in.sportscafe.nostragamus.module.user.group.newgroup;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedInfo;

/**
 * Created by deepanshi on 1/9/17.
 */

public class GrpTournamentUnSelectedAdapter extends Adapter<TournamentFeedInfo, GrpTournamentUnSelectedAdapter.ViewHolder> {

    private Context mcontext;

    private List<Integer> mUnSelectedTournamentsIdList;

    private GrpTournamentUnSelectedAdapter.OnGrpTournamentChangedListener mChangedListener;

    public GrpTournamentUnSelectedAdapter(Context context, List<Integer> UnSelectedTournamentsIdList) {
        super(context);
        mcontext=context;
        this.mUnSelectedTournamentsIdList = UnSelectedTournamentsIdList;
    }

    public GrpTournamentUnSelectedAdapter(Context context, List<Integer> UnSelectedTournamentsIdList,
                                          GrpTournamentUnSelectedAdapter.OnGrpTournamentChangedListener listener) {
        super(context);
        mcontext=context;
        this.mUnSelectedTournamentsIdList = UnSelectedTournamentsIdList;
        this.mChangedListener = listener;
    }

    @Override
    public TournamentFeedInfo getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public GrpTournamentUnSelectedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GrpTournamentUnSelectedAdapter.ViewHolder(getLayoutInflater().inflate(R.layout.inflater_group_sport_row, parent, false));
    }

    @Override
    public void onBindViewHolder(GrpTournamentUnSelectedAdapter.ViewHolder holder, int position) {
        TournamentFeedInfo tournamentInfo = getItem(position);

        holder.id = tournamentInfo.getTournamentId();
        holder.mTvTournament.setText(tournamentInfo.getTournamentName());
        holder.mTvSport.setText(tournamentInfo.getSportsName());
        holder.mIvTournament.setImageUrl(tournamentInfo.getTournamentPhoto());


        if (mUnSelectedTournamentsIdList.contains((tournamentInfo.getTournamentId()))) {
            holder.mIvSelectedIcon.setBackgroundResource(R.drawable.tick_icon);
            holder.mTvTournament.setTextColor(mcontext.getResources().getColor(R.color.btn_powerup_screen_color));
            holder.mTvTournament.setAlpha((float) 0.9);

        } else {
            holder.mTvTournament.setTextColor(Color.WHITE);
            holder.mIvSelectedIcon.setBackgroundResource(R.drawable.add_icon);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int id;

        View mMainView;

        // CheckBox mCbSport;
        TextView mTvSport;
        HmImageView mIvTournament;
        ImageView mIvSelectedIcon;
        TextView mTvTournament;

        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mIvTournament = (HmImageView) V.findViewById(R.id.group_sport_row_image_tournament);
            mTvSport = (TextView) V.findViewById(R.id.group_sport_row_tv_sport_name);
            mIvSelectedIcon = (ImageView) V.findViewById(R.id.group_sport_row_selected_icon);
            mTvTournament = (TextView) V.findViewById(R.id.group_sport_row_tv_tournament_name);
            V.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            boolean removeOldTournament = mUnSelectedTournamentsIdList.contains(id);
            if(null == mChangedListener
                    || mChangedListener.onGrpTournamentUnSelected(!removeOldTournament, mUnSelectedTournamentsIdList.size())) {
                if (removeOldTournament) {
                    mUnSelectedTournamentsIdList.remove(mUnSelectedTournamentsIdList.indexOf(id));
                } else {
                    mUnSelectedTournamentsIdList.add(id);
                }
                notifyItemChanged(getAdapterPosition());

                if(null != mChangedListener) {
                    mChangedListener.onGrpTournamentChanged(mUnSelectedTournamentsIdList);
                }
            }
        }
    }

    public List<Integer> getUnSelectedTournamentList() {
        return mUnSelectedTournamentsIdList;
    }

    public interface OnGrpTournamentChangedListener {

        boolean onGrpTournamentUnSelected(boolean addNewTournament, int existingTournamentCount);

        void onGrpTournamentChanged(List<Integer> selectedTournamentsIdList);
    }
}