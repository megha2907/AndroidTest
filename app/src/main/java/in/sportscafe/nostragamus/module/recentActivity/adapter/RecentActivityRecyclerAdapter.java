package in.sportscafe.nostragamus.module.recentActivity.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.newChallenges.helpers.DateTimeHelper;
import in.sportscafe.nostragamus.module.recentActivity.dto.RecentActivity;

/**
 * Created by deepanshi on 3/22/18.
 */

public class RecentActivityRecyclerAdapter extends RecyclerView.Adapter<RecentActivityRecyclerAdapter.RecentActivityViewHolder> implements Filterable {

    private List<RecentActivity> recentActivityList;
    private List<RecentActivity> recentActivityFilteredList;
    private RecentActivityAdapterListener mRecentActivityAdapterListener;
    Context context;


    public RecentActivityRecyclerAdapter(List<RecentActivity> recentActivityDataList, Context context,
                                         @NonNull RecentActivityAdapterListener listener) {
        recentActivityList = recentActivityDataList;
        this.context = context;
        mRecentActivityAdapterListener = listener;
        recentActivityFilteredList = recentActivityDataList;
    }

    @Override
    public RecentActivityRecyclerAdapter.RecentActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_activity_item_layout, parent, false);

        return new RecentActivityRecyclerAdapter.RecentActivityViewHolder(itemView, mRecentActivityAdapterListener);
    }

    @Override
    public void onBindViewHolder(final RecentActivityRecyclerAdapter.RecentActivityViewHolder viewHolder, final int position) {

        if (recentActivityFilteredList != null && recentActivityFilteredList.size() > position) {

            final RecentActivity recentActivity = recentActivityFilteredList.get(position);
            if (recentActivity != null) {
                viewHolder.mTvActivityType.setText(recentActivity.getActivityType());
                viewHolder.mTvActivityText.setText(recentActivity.getActivityText());

                String recentActivityType = recentActivity.getActivityType();
                if (!TextUtils.isEmpty(recentActivityType)) {
                    if (recentActivityType.equalsIgnoreCase(Constants.RecentActivityTypes.ANNOUNCEMENT)) {
                        viewHolder.mIvActivityType.setImageResource(R.drawable.activity_announcement);
                    } else if (recentActivityType.equalsIgnoreCase(Constants.RecentActivityTypes.PROMOTION)) {
                        viewHolder.mIvActivityType.setImageResource(R.drawable.activity_promo);
                    } else if (recentActivityType.equalsIgnoreCase(Constants.RecentActivityTypes.RESULT)) {
                        viewHolder.mIvActivityType.setImageResource(R.drawable.activity_result);
                    } else {
                        viewHolder.mIvActivityType.setImageResource(R.drawable.activity_announcement);
                    }
                }

                if (!TextUtils.isEmpty(recentActivity.getActivityTime())) {
                    viewHolder.mTvActivityDate.setText(DateTimeHelper.getFormattedDate(recentActivity.getActivityTime()));
                }

                if (recentActivity.isUnread()) {
                    viewHolder.mIvActivityUnReadIcon.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.mIvActivityUnReadIcon.setVisibility(View.GONE);
                }

                if (recentActivity.getNostraRecentActivityInfo() != null &&
                        !TextUtils.isEmpty(recentActivity.getNostraRecentActivityInfo().getScreenName())) {
                    viewHolder.mRecentActivityLinkLayout.setVisibility(View.VISIBLE);
                    viewHolder.mRecentActivityLayout.setClickable(true);
                } else {
                    viewHolder.mRecentActivityLinkLayout.setVisibility(View.GONE);
                    viewHolder.mRecentActivityLayout.setClickable(false);
                }

            }

        }
    }

    public class RecentActivityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTvActivityType;
        public TextView mTvActivityText;
        public TextView mTvActivityDate;
        public ImageView mIvActivityType;
        public ImageView mIvActivityUnReadIcon;
        public LinearLayout mRecentActivityLayout;
        public LinearLayout mRecentActivityLinkLayout;
        private RecentActivityAdapterListener clickListener;

        public RecentActivityViewHolder(View view, @NonNull RecentActivityAdapterListener listener) {
            super(view);
            this.clickListener = listener;
            mIvActivityType = (ImageView) view.findViewById(R.id.recent_activity_type_icon);
            mRecentActivityLayout = (LinearLayout) view.findViewById(R.id.recent_activity_item_root_layout);
            mTvActivityType = (TextView) view.findViewById(R.id.recent_activity_type);
            mTvActivityText = (TextView) view.findViewById(R.id.recent_activity_type_text);
            mTvActivityDate = (TextView) view.findViewById(R.id.recent_activity_date);
            mIvActivityUnReadIcon = (ImageView) view.findViewById(R.id.recent_activity_unread_icon);
            mRecentActivityLinkLayout = (LinearLayout) view.findViewById(R.id.recent_activity_link_layout);
            mRecentActivityLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.recent_activity_item_root_layout:
                    if (clickListener != null) {
                        clickListener.handleItemOnClick(getRecentActivityData(getAdapterPosition()));
                    }
                    break;
            }
        }
    }

    @NonNull
    private Bundle getRecentActivityData(int adapterPos) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.BundleKeys.RECENT_ACTIVITY, Parcels.wrap(recentActivityFilteredList.get(adapterPos)));
        return args;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty() || charString.equalsIgnoreCase(Constants.RecentActivityTypes.ALL)) {
                    recentActivityFilteredList = recentActivityList;
                } else {
                    List<RecentActivity> filteredList = new ArrayList<>();
                    for (RecentActivity recentActivity : recentActivityList) {
                        if (recentActivity.getActivityType().equalsIgnoreCase(charString)) {
                            filteredList.add(recentActivity);
                        }
                    }

                    recentActivityFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = recentActivityFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                recentActivityFilteredList = (ArrayList<RecentActivity>) filterResults.values;

                if (mRecentActivityAdapterListener != null) {
                    if (recentActivityFilteredList.isEmpty() && recentActivityFilteredList.size() <= 0) {
                        mRecentActivityAdapterListener.showEmptyListView(charSequence.toString());
                    } else {
                        mRecentActivityAdapterListener.showFilteredList();
                    }
                }

                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return recentActivityFilteredList.size();
    }


}
