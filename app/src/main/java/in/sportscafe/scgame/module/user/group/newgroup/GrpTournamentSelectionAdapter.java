package in.sportscafe.scgame.module.user.group.newgroup;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.Adapter;
import in.sportscafe.scgame.module.TournamentFeed.dto.TournamentInfo;

/**
 * Created by rb on 30/11/15.
 */
public class GrpTournamentSelectionAdapter extends Adapter<TournamentInfo, GrpTournamentSelectionAdapter.ViewHolder> {

    private Context context;

    private List<Integer> mSelectedTournamentsIdList;

    private OnGrpTournamentChangedListener mChangedListener;

    public GrpTournamentSelectionAdapter(Context context, List<Integer> SelectedTournamentsIdList) {
        super(context);
        this.mSelectedTournamentsIdList = SelectedTournamentsIdList;
        this.context=context;
    }

    public GrpTournamentSelectionAdapter(Context context, List<Integer> SelectedTournamentsIdList,
                                         OnGrpTournamentChangedListener listener) {
        super(context);
        this.mSelectedTournamentsIdList = SelectedTournamentsIdList;
        this.mChangedListener = listener;
    }

    @Override
    public TournamentInfo getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_group_sport_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TournamentInfo tournamentInfo = getItem(position);

        holder.id = tournamentInfo.getTournamentId();
        holder.mTvSport.setText(tournamentInfo.getTournamentName());

        Picasso.with(context)
                .load(tournamentInfo.getTournamentPhoto())
                .into(holder.mIvSport);

        if (mSelectedTournamentsIdList.contains((tournamentInfo.getTournamentId()))) {
             holder.mMainView.setBackgroundResource(R.drawable.sports_selection_shape);
             holder.mTvSport.setTextColor(Color.BLACK);
             holder.mTvSport.setAlpha((float) 0.9);

        } else {
            holder.mMainView.setBackgroundColor(Color.TRANSPARENT);
            holder.mTvSport.setTextColor(Color.WHITE);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int id;

        View mMainView;

        // CheckBox mCbSport;
        ImageView mIvSport;
        TextView mTvSport;

        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mIvSport = (ImageView) V.findViewById(R.id.group_sport_row_image_sport);
            mTvSport = (TextView) V.findViewById(R.id.group_sport_row_tv_sport);
            V.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            boolean removeOldTournament = mSelectedTournamentsIdList.contains(id);
            if(null == mChangedListener
                    || mChangedListener.onGrpTournamentSelected(!removeOldTournament, mSelectedTournamentsIdList.size())) {
                if (removeOldTournament) {
                    mSelectedTournamentsIdList.remove(mSelectedTournamentsIdList.indexOf(id));
                } else {
                    mSelectedTournamentsIdList.add(id);
                }
                notifyDataSetChanged();

                if(null != mChangedListener) {
                    mChangedListener.onGrpTournamentChanged(mSelectedTournamentsIdList);
                }
            }
        }
    }

    public List<Integer> getSelectedTournamentList() {
        return mSelectedTournamentsIdList;
    }

    public interface OnGrpTournamentChangedListener {

        boolean onGrpTournamentSelected(boolean addNewTournament, int existingTournamentCount);

        void onGrpTournamentChanged(List<Integer> selectedTournamentsIdList);
    }
}