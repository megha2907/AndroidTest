package in.sportscafe.nostragamus.module.newChallenges.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeeva.android.widgets.HmImageView;

import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.newChallenges.dto.BannerResponseData;


/**
 * Created by deepanshi on 2/1/18.
 */

public class BannerRecyclerAdapter extends RecyclerView.Adapter<BannerRecyclerAdapter.MyViewHolder> {

    private List<BannerResponseData> mBannerResponseDataList;
    Context context;


    public BannerRecyclerAdapter(List<BannerResponseData> bannerResponseDataList, Context context) {
        mBannerResponseDataList = bannerResponseDataList;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (mBannerResponseDataList != null && mBannerResponseDataList.size() > position) {

            final BannerResponseData bannerResponseData = mBannerResponseDataList.get(position);

            holder.bannerImage.setImageUrl(bannerResponseData.getBannerImageUrl());
            holder.bannerImage.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                }

            });
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        HmImageView bannerImage;

        public MyViewHolder(View view) {
            super(view);
            bannerImage = (HmImageView) view.findViewById(R.id.banner_image);
        }
    }


    @Override
    public int getItemCount() {
        return mBannerResponseDataList.size();
    }

}
