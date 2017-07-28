package in.sportscafe.nostragamus.module.store;

import android.content.Context;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeConfig;
import in.sportscafe.nostragamus.module.allchallenges.info.ChallengeConfigAdapter;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.store.dto.ProductSaleInfo;
import in.sportscafe.nostragamus.module.store.dto.StoreItems;
import in.sportscafe.nostragamus.module.store.dto.StoreSections;

/**
 * Created by deepanshi on 7/25/17.
 */

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreVH> implements View.OnClickListener {

    private BuyButtonListener mBuyListener;
    private List<StoreSections> mStoreSectionsList;
    private Context mContext;

    public StoreAdapter(Context context, @NonNull BuyButtonListener buyButtonListener) {
        mContext = context;
        mBuyListener = buyButtonListener;
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
            parent.addView(getStoreItemView(storeItems, storeItemsList.size(), parent));
        }
    }

    private View getStoreItemView(StoreItems storeItem, int storeItemsListSize, ViewGroup parent) {

        View storeItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflater_store_item, parent, false);

        TextView tvStoreItemName = (TextView) storeItemView.findViewById(R.id.store_item_title_tv);
        CustomButton btnStoreItemPrice = (CustomButton) storeItemView.findViewById(R.id.store_item_buy);
        CustomButton btnSalePercentage = (CustomButton) storeItemView.findViewById(R.id.store_item_sale);
        HmImageView ivStoreItemImage = (HmImageView) storeItemView.findViewById(R.id.store_item_iv);
        TextView tvStoreItemValue = (TextView) storeItemView.findViewById(R.id.store_item_value_tv);
        View storeLine = (View) storeItemView.findViewById(R.id.store_item_line);

        btnStoreItemPrice.setOnClickListener(this);
        btnStoreItemPrice.setTag(storeItem);

        tvStoreItemName.setText(storeItem.getProductName());

        if (!TextUtils.isEmpty(storeItem.getProductImage())) {
            ivStoreItemImage.setImageUrl(storeItem.getProductImage());
        } else {
            ivStoreItemImage.setVisibility(View.GONE);
        }


        if (TextUtils.isEmpty(storeItem.getProductDesc())) {
            tvStoreItemValue.setVisibility(View.GONE);
        } else {
            tvStoreItemValue.setVisibility(View.VISIBLE);
            tvStoreItemValue.setText(storeItem.getProductDesc());
        }

        if (parent.getChildCount() == storeItemsListSize - 1) {
            storeLine.setVisibility(View.GONE);
        }

        if (storeItem.getProductPrice()!=null) {
            btnStoreItemPrice.setText(WalletHelper.getFormattedStringOfAmount(storeItem.getProductPrice()));
        }else {
            btnStoreItemPrice.setText(Constants.RUPEE_SYMBOL + "0");
        }

        ProductSaleInfo productSaleInfo = storeItem.getProductSaleInfo();

        if (productSaleInfo!=null){

            if (productSaleInfo.getSaleOn()){
                btnSalePercentage.setVisibility(View.VISIBLE);
                btnSalePercentage.setText(productSaleInfo.getSalePercentage().toString()+" %off");
            }else {
                btnSalePercentage.setVisibility(View.GONE);
            }
        }

        return storeItemView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.store_item_buy:
                onBuyButtonClicked(view);
                break;
        }
    }

    private void onBuyButtonClicked(View view) {
        Object obj = view.getTag();
        if (obj != null && mBuyListener != null) {
            mBuyListener.onBuyClicked((StoreItems) obj);
        }
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