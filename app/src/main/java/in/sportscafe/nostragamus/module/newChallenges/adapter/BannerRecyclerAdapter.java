package in.sportscafe.nostragamus.module.newChallenges.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.jeeva.android.widgets.HmImageView;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.newChallenges.dto.BannerResponseData;


/**
 * Created by deepanshi on 2/1/18.
 */

public class BannerRecyclerAdapter extends RecyclerView.Adapter<BannerRecyclerAdapter.BannerViewHolder> {

    private List<BannerResponseData> mBannerResponseDataList;
    private BannerAdapterListener mBannerAdapterListener;
    Context context;

    public BannerRecyclerAdapter(List<BannerResponseData> bannerResponseDataList, Context context,
                                 @NonNull BannerAdapterListener listener) {
        mBannerResponseDataList = bannerResponseDataList;
        this.context = context;
        mBannerAdapterListener = listener;
    }

    @Override
    public BannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_item_layout, parent, false);

        return new BannerViewHolder(itemView, mBannerAdapterListener);
    }

    @Override
    public void onBindViewHolder(final BannerViewHolder holder, final int position) {

        if (mBannerResponseDataList != null && mBannerResponseDataList.size() > position) {

            final BannerResponseData bannerResponseData = mBannerResponseDataList.get(position);

            holder.bannerImage.setImageUrl(bannerResponseData.getBannerImageUrl());

        }
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        HmImageView bannerImage;
        private BannerAdapterListener clickListener;

        public BannerViewHolder(View view, @NonNull BannerAdapterListener listener) {
            super(view);
            this.clickListener = listener;
            bannerImage = (HmImageView) view.findViewById(R.id.banner_image);
            bannerImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.banner_image:
                    if (clickListener != null) {
                        clickListener.handleBannerOnClick(getBannerData(getAdapterPosition()));
                    }
                    break;
            }
        }
    }

    @NonNull
    private Bundle getBannerData(int adapterPos) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.BundleKeys.BANNER, Parcels.wrap(mBannerResponseDataList.get(adapterPos)));
        return args;
    }


    @Override
    public int getItemCount() {
        return mBannerResponseDataList.size();
    }
}
