package in.sportscafe.nostragamus.module.store;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.HmImageView;
import com.jeeva.android.widgets.customfont.CustomButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.WordUtils;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.store.dto.ProductSaleInfo;
import in.sportscafe.nostragamus.module.store.dto.StoreItems;
import in.sportscafe.nostragamus.module.store.dto.StoreSections;

/**
 * Created by deepanshi on 7/25/17.
 */

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreVH> {


    private List<StoreSections> mStoreSectionsList;
    private Context mContext;
    private boolean showBundleView = false;

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
            holder.mTvCategory.setText(WordUtils.capitalize(storeSections.getSectionName()));
            holder.mTvCategoryDesc.setText(storeSections.getSectionDesc());

            try {
                showBundleView = bundleWithDifferentPowerUps(storeSections);

                if (showBundleView) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            holder.mRlStoreHolder.getResources().getDimensionPixelSize(R.dimen.dim_72));
                    holder.mRlStoreHolder.setLayoutParams(params);
                }

                createStoreItemsList(storeSections.getStoreItemsList(), holder.mLlStoreItemsHolder, showBundleView);

            } catch (Throwable e) {
                e.printStackTrace();
            }

        }
    }

    private boolean bundleWithDifferentPowerUps(StoreSections storeSections) {

        HashMap<String, Integer> powerUpMap = storeSections.getStoreItemsList().get(0).getPowerUps();

        Integer powerUp2xCount = powerUpMap.get(Constants.Powerups.XX);
        Integer powerUpNonNegsCount = powerUpMap.get(Constants.Powerups.NO_NEGATIVE);
        Integer powerUpPlayerPollCount = powerUpMap.get(Constants.Powerups.AUDIENCE_POLL);

        if (powerUp2xCount != null && powerUpNonNegsCount != null && powerUpPlayerPollCount != null) {
            if (!allEqual(powerUp2xCount, powerUpNonNegsCount, powerUpPlayerPollCount)) {
                return true;
            }
        }

        return false;
    }

    public boolean allEqual(Object key, Object... objs) {
        for (Object o : objs) if (!o.equals(key)) return false;
        return true;
    }

    @Override
    public int getItemCount() {
        return (mStoreSectionsList != null) ? mStoreSectionsList.size() : 0;
    }

    private void createStoreItemsList(List<StoreItems> storeItemsList, ViewGroup parent, boolean showBundleView) {
        for (StoreItems storeItems : storeItemsList) {
            parent.addView(getStoreItemView(storeItems, storeItemsList.size(), showBundleView, parent));
        }
    }

    private View getStoreItemView(StoreItems storeItem, int storeItemsListSize, boolean showBundleView, ViewGroup parent) {

        View storeItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflater_store_item, parent, false);

        TextView tvStoreItemName = (TextView) storeItemView.findViewById(R.id.store_item_title_tv);
        CustomButton btnStoreItemPrice = (CustomButton) storeItemView.findViewById(R.id.store_item_buy);
        CustomButton btnSalePercentage = (CustomButton) storeItemView.findViewById(R.id.store_item_sale);
        HmImageView ivStoreItemImage = (HmImageView) storeItemView.findViewById(R.id.store_item_iv);
        TextView tvStoreItemValue = (TextView) storeItemView.findViewById(R.id.store_item_value_tv);
        TextView tvStoreItemSaleValue = (TextView) storeItemView.findViewById(R.id.store_item_sale_value_tv);
        View storeLine = (View) storeItemView.findViewById(R.id.store_item_line);
        LinearLayout storeItemLayout = (LinearLayout) storeItemView.findViewById(R.id.store_item_layout);

        tvStoreItemSaleValue.setVisibility(View.GONE);
        btnSalePercentage.setVisibility(View.GONE);

        if (showBundleView) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    storeItemLayout.getResources().getDimensionPixelSize(R.dimen.dim_72));
            storeItemLayout.setLayoutParams(params);
        }


        tvStoreItemName.setText(WordUtils.capitalize(storeItem.getProductName()));

        if (!TextUtils.isEmpty(storeItem.getProductImage())) {
            ivStoreItemImage.setImageUrl(storeItem.getProductImage());
        } else {
            ivStoreItemImage.setVisibility(View.GONE);
        }


        if (TextUtils.isEmpty(storeItem.getProductDesc())) {
            tvStoreItemValue.setVisibility(View.GONE);
        } else {
            tvStoreItemValue.setVisibility(View.VISIBLE);
            tvStoreItemValue.setText(WordUtils.capitalize(storeItem.getProductDesc()));
        }

        if (parent.getChildCount() == storeItemsListSize - 1) {
            storeLine.setVisibility(View.GONE);
        }

        if (storeItem.getProductPrice() != null) {
            btnStoreItemPrice.setText(WalletHelper.getFormattedStringOfAmount(storeItem.getProductPrice()));
        } else {
            btnStoreItemPrice.setText(Constants.RUPEE_SYMBOL + "0");
        }

        ProductSaleInfo productSaleInfo = storeItem.getProductSaleInfo();

        if (productSaleInfo != null) {

            if (productSaleInfo.getSaleOn()) {

                if (productSaleInfo.getSalePercentage() != 0) {
                    btnSalePercentage.setVisibility(View.VISIBLE);
                    btnSalePercentage.setText(productSaleInfo.getSalePercentage().toString() + " %off");
                }

                int savePrice = 0;
                Integer originalPrice = storeItem.getProductPrice();
                Integer salePrice = productSaleInfo.getSalePrice();

                if (salePrice != null && originalPrice != null) {

                    tvStoreItemSaleValue.setVisibility(View.VISIBLE);
                    savePrice = originalPrice - salePrice;

                    tvStoreItemValue.setText((Constants.RUPEE_SYMBOL + originalPrice.toString()));
                    tvStoreItemValue.setPaintFlags(tvStoreItemValue.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    tvStoreItemSaleValue.setText(" Save " + Constants.RUPEE_SYMBOL + String.valueOf(savePrice));

                    btnStoreItemPrice.setText(WalletHelper.getFormattedStringOfAmount(salePrice));

                }

            } else {
                btnSalePercentage.setVisibility(View.GONE);
            }
        }

        return storeItemView;
    }


    public class StoreVH extends RecyclerView.ViewHolder {

        View mMainView;
        TextView mTvCategory;
        TextView mTvCategoryDesc;
        LinearLayout mLlStoreItemsHolder;
        RelativeLayout mRlStoreHolder;


        public StoreVH(View view) {
            super(view);

            mMainView = view;
            mTvCategory = (TextView) view.findViewById(R.id.store_category_name);
            mTvCategoryDesc = (TextView) view.findViewById(R.id.store_category_desc);
            mLlStoreItemsHolder = (LinearLayout) view.findViewById(R.id.store_items_ll);
            mRlStoreHolder = (RelativeLayout) view.findViewById(R.id.store_category_layout);
        }

    }

}