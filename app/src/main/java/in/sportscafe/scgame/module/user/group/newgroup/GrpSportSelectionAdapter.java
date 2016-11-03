package in.sportscafe.scgame.module.user.group.newgroup;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.common.Adapter;
import in.sportscafe.scgame.module.user.sportselection.dto.Sport;

/**
 * Created by rb on 30/11/15.
 */
public class GrpSportSelectionAdapter extends Adapter<Sport, GrpSportSelectionAdapter.ViewHolder> {

    private List<Integer> mSelectedSportsIdList;

    private OnGrpSportChangedListener mChangedListener;

    public GrpSportSelectionAdapter(Context context, List<Integer> selectedSportsIdList) {
        super(context);
        this.mSelectedSportsIdList = selectedSportsIdList;
    }

    public GrpSportSelectionAdapter(Context context, List<Integer> selectedSportsIdList,
                                    OnGrpSportChangedListener listener) {
        super(context);
        this.mSelectedSportsIdList = selectedSportsIdList;
        this.mChangedListener = listener;
    }

    @Override
    public Sport getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.inflater_group_sport_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Sport sport = getItem(position);

        holder.id = sport.getId();

     //   holder.mTvSport.setText(sport.getName());
        //holder.mCbSport.setChecked(mSelectedSportsIdList.contains(sport.getId()));

        if (mSelectedSportsIdList.contains((sport.getId()))) {
           // holder.mMainView.setBackgroundResource(R.drawable.sports_selection_shape);
//            holder.mTvSport.setAlpha((float) 0.9);
            holder.mIvSport.setImageResource(sport.getSelectedImageResource());
        } else {
//            holder.mMainView.setBackgroundColor(Color.TRANSPARENT);
            holder.mIvSport.setImageResource(sport.getImageResource());
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
            mIvSport = (ImageView) V.findViewById(R.id.group_sport_row_image_tournament);
//            mTvSport = (TextView) V.findViewById(R.id.group_sport_row_tv_sport);
            V.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            boolean removeOldSport = mSelectedSportsIdList.contains(id);
            if(null == mChangedListener
                    || mChangedListener.onGrpSportSelected(!removeOldSport, mSelectedSportsIdList.size())) {
                if (removeOldSport) {
                    mSelectedSportsIdList.remove(mSelectedSportsIdList.indexOf(id));
                } else {
                    mSelectedSportsIdList.add(id);
                }
                notifyDataSetChanged();

                if(null != mChangedListener) {
                    mChangedListener.onGrpSportChanged(mSelectedSportsIdList);
                }
            }
        }
    }

    public List<Integer> getSelectedSportList() {
        return mSelectedSportsIdList;
    }

    public interface OnGrpSportChangedListener {

        boolean onGrpSportSelected(boolean addNewSport, int existingSportsCount);

        void onGrpSportChanged(List<Integer> selectedSportIdList);
    }
}