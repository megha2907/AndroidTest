package in.sportscafe.nostragamus.module.user.sportselection.profilesportselection;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.user.sportselection.dto.Sport;

/**
 * Created by deepanshi on 12/8/16.
 */

public class ProfileSportSelectionAdapter extends Adapter<Sport, ProfileSportSelectionAdapter.ViewHolder> {

    private List<Integer> mSelectedSportsIdList;
    private Context mContext;

    private ProfileSportSelectionAdapter.OnSportChangedListener mChangedListener;

    public ProfileSportSelectionAdapter(Context context, List<Sport> allSports,
                                        List<Integer> selectedSportsIdList, OnSportChangedListener listener) {
        super(context);
        mContext = context;
        this.mSelectedSportsIdList = selectedSportsIdList;
        this.mChangedListener = listener;

        addAll(allSports);
    }

    @Override
    public Sport getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ProfileSportSelectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProfileSportSelectionAdapter.ViewHolder(getLayoutInflater().inflate(R.layout.inflater_profile_sport_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ProfileSportSelectionAdapter.ViewHolder holder, int position) {
        Sport sport = getItem(position);

        holder.id = sport.getId();
        holder.mTvSport.setText(sport.getName());
        holder.mIvSport.setImageUrl(sport.getImageUrl());

        if (mSelectedSportsIdList.contains((sport.getId()))) {
            holder.mIvSelectedIcon.setBackgroundResource(R.drawable.profile_sport_selected_icon);

        } else {
            holder.mIvSelectedIcon.setBackgroundResource(R.drawable.profile_sport_icon);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int id;

        View mMainView;

        HmImageView mIvSport;

        TextView mTvSport;

        ImageView mIvSelectedIcon;

        public ViewHolder(View V) {
            super(V);
            mMainView = V;
            mIvSport = (HmImageView) V.findViewById(R.id.profile_sport_row_iv_sport_image);
            mTvSport = (TextView) V.findViewById(R.id.profile_sport_row_tv_sport_name);
            mIvSelectedIcon = (ImageView) V.findViewById(R.id.profile_sport_row_selected_icon);

            V.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (mSelectedSportsIdList.contains(id)) {
                mSelectedSportsIdList.remove(mSelectedSportsIdList.indexOf(id));
            } else {
                mSelectedSportsIdList.add(id);
            }
            notifyItemChanged(getAdapterPosition());

            if (null != mChangedListener) {
                mChangedListener.onOnSportChanged(mSelectedSportsIdList);
            }
        }


    }

    public List<Integer> getSelectedSportList() {
        return mSelectedSportsIdList;
    }

    public interface OnSportChangedListener {
        void onOnSportChanged(List<Integer> selectedSportsIdList);
    }
}
