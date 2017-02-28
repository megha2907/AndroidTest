package in.sportscafe.nostragamus.module.user.lblanding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.Adapter;
import in.sportscafe.nostragamus.module.user.points.PointsActivity;

/**
 * Created by deepanshi on 14/07/16.
 */
public class LBLandingAdapter extends Adapter<LbLanding, LBLandingAdapter.MyViewHolder> {

    private String mLbLandingType;
    private Integer mGroupId;
    private Integer mChallengeId;
    private boolean mNeedPadding = false;

    public LBLandingAdapter(Context context, boolean needPadding) {
        super(context);
        this.mNeedPadding = needPadding;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(getLayoutInflater().inflate(R.layout.inflater_lb_summary, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LbLanding lbLandingDto = getItem(position);

        if (mLbLandingType== Constants.LBLandingType.SPORT){
            holder.ivImage.setAlpha(0.1F);
        }

        if (null !=lbLandingDto.getRankChange()) {
            if (lbLandingDto.getRankChange() < 0) {
                holder.mIvStatus.setImageResource(R.drawable.lb_rank_change_icon);
                holder.mIvStatus.setRotation(180);
            } else {
                holder.mIvStatus.setImageResource(R.drawable.lb_rank_change_icon);
            }
        }

        holder.ivImage.setImageUrl(lbLandingDto.getImgUrl());

        if (null != lbLandingDto.getRank()) {
            String rank = AppSnippet.ordinal(lbLandingDto.getRank());
            holder.tvPosTxt.setText("POSITION");
            holder.tvRank.setVisibility(View.VISIBLE);
            holder.mIvStatus.setVisibility(View.VISIBLE);
            holder.tvRank.setText(rank);
        } else {
            holder.tvRank.setVisibility(View.GONE);
            holder.mIvStatus.setVisibility(View.GONE);
            holder.tvPosTxt.setText("NOT\nPLAYED");
        }

        holder.tvName.setText(lbLandingDto.getName());
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        HmImageView ivImage;

        TextView tvRank;

        TextView tvName;

        TextView tvPosTxt;

        ImageView mIvStatus;


        public MyViewHolder(View view) {
            super(view);
            mIvStatus = (ImageView) view.findViewById(R.id.lb_summary_item_rank_status_iv);
            ivImage = (HmImageView) view.findViewById(R.id.lb_summary_item_iv);
            tvRank = (TextView) view.findViewById(R.id.lb_summary_item_rank_tv);
            tvName = (TextView) view.findViewById(R.id.lb_summary_item_name_tv);
            tvPosTxt= (TextView) view.findViewById(R.id.lb_summary_item_pos_tv);

            if (!mNeedPadding) {
                ivImage.setPadding(0, 0, 0, 0);
            }

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            NostragamusAnalytics.getInstance().trackLeaderboard(mLbLandingType);

            LbLanding lbLanding = getItem(getAdapterPosition());
            mChallengeId = lbLanding.getChallengeId();
            mGroupId =lbLanding.getGroupId();
            if(mChallengeId == 0){
                mLbLandingType = Constants.LBLandingType.GROUP;
            }else {
                mLbLandingType = Constants.LBLandingType.CHALLENGE;
            }
            lbLanding.setType(mLbLandingType);

            Bundle bundle = new Bundle();
            bundle.putParcelable(BundleKeys.LB_LANDING_DATA, Parcels.wrap(lbLanding));
            navigateToPointsActivity(view.getContext(), bundle);
        }

        private void navigateToPointsActivity(Context context, Bundle bundle) {
            Intent intent = new Intent(context, PointsActivity.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }

    }
}