package in.sportscafe.nostragamus.module.store;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jeeva.android.widgets.HmImageView;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeConfig;
import in.sportscafe.nostragamus.module.allchallenges.info.ChallengeConfigAdapter;
import in.sportscafe.nostragamus.module.store.dto.StoreItems;
import in.sportscafe.nostragamus.module.store.dto.StoreSections;

/**
 * Created by deepanshi on 7/25/17.
 */

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreVH> {


    private List<StoreSections> mStoreSectionsList;
    private Context mContext;

    public StoreAdapter(Context context) {
        mContext = context;
    }

    public List<StoreSections> getStoreSectionsList() {
        return mStoreSectionsList;
    }

    /**
     * Add more store items
     *
     * @param storeSectionsList
     */
    public void addStoreSectionsIntoList(List<StoreSections> storeSectionsList) {
        if (mStoreSectionsList == null) {
            mStoreSectionsList = new ArrayList<>();
        }
        if (storeSectionsList != null && !storeSectionsList.isEmpty()) {
            mStoreSectionsList.addAll(storeSectionsList);
            notifyDataSetChanged();
        }
    }


    @Override
    public StoreAdapter.StoreVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflater_store, parent, false);

        return new StoreAdapter.StoreVH(itemView);
    }

    @Override
    public void onBindViewHolder(StoreAdapter.StoreVH holder, int position) {

        if (mStoreSectionsList != null && position < mStoreSectionsList.size()) {    /* position < getItemCount() */

            StoreSections storeSections = mStoreSectionsList.get(position);
            holder.mTvCategory.setText(storeSections.getSectionName());
            holder.mTvCategoryDesc.setText(storeSections.getSectionDesc());
            createStoreItemsList(storeSections.getStoreItemsList(), holder.mLlStoreItemsHolder);

        }
    }

    @Override
    public int getItemCount() {
        return (mStoreSectionsList != null) ? mStoreSectionsList.size() : 0;
    }

    private void createStoreItemsList(List<StoreItems> storeItemsList, ViewGroup parent) {
        for (StoreItems storeItems : storeItemsList) {
            parent.addView(getStoreItemView(storeItems, parent));
        }
    }

    private View getStoreItemView(StoreItems storeItem, ViewGroup parent) {

        View storeItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflater_store_item, parent, false);

        TextView tvStoreItemName = (TextView) storeItemView.findViewById(R.id.store_item_title_tv);
        tvStoreItemName.setText(storeItem.getProductName());

        HmImageView ivStoreItemImage = (HmImageView) storeItemView.findViewById(R.id.store_item_iv);
        if (!TextUtils.isEmpty(storeItem.getProductImage())) {
            ivStoreItemImage.setImageUrl(storeItem.getProductImage());
        } else {
            ivStoreItemImage.setVisibility(View.GONE);
        }

        TextView tvStoreItemValue = (TextView) storeItemView.findViewById(R.id.store_item_value_tv);

        if (TextUtils.isEmpty(storeItem.getProductDesc())) {
            tvStoreItemValue.setVisibility(View.GONE);
        } else {
            tvStoreItemValue.setVisibility(View.VISIBLE);
            tvStoreItemValue.setText(storeItem.getProductDesc());
            tvStoreItemValue.setPadding(15, 0, 0, 0);
        }

//        Button btnStoreItemValue = (Button) storeItemView.findViewById(R.id.store_item_buy_btn);
//        btnStoreItemValue.setText(storeItem.getProductPrice());

        return storeItemView;
    }


    public class StoreVH extends RecyclerView.ViewHolder {

        View mMainView;
        TextView mTvCategory;
        TextView mTvCategoryDesc;
        LinearLayout mLlStoreItemsHolder;


        public StoreVH(View view) {
            super(view);

            mMainView = view;
            mTvCategory = (TextView) view.findViewById(R.id.store_category_name);
            mTvCategoryDesc = (TextView) view.findViewById(R.id.store_category_desc);
            mLlStoreItemsHolder = (LinearLayout) view.findViewById(R.id.store_items_ll);
        }

    }

}