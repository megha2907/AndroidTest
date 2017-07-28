package in.sportscafe.nostragamus.module.store;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreVH> implements View.OnClickListener {

    private BuyButtonListener mBuyListener;
    private List<StoreSections> mStoreSectionsList;
    private Context mContext;
    private boolean showBundleView = false;


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
            holder.mTvCategory.setText(WordUtils.capitalize(storeSections.getSectionName()));
            holder.mTvCategoryDesc.setText(storeSections.getSectionDesc());

            try {
                showBundleView = bundleWithDifferentPowerUps(storeSections);

                if (showBundleView) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            holder.mRlStoreHolder.getResources().getDimensionPixelSize(R.dimen.dim_72));
                    holder.mRlStoreHolder.setLayoutParams(params);
                }else {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            holder.mRlStoreHolder.getResources().getDimensionPixelSize(R.dimen.dim_60));
                    holder.mRlStoreHolder.setLayoutParams(params);
                }

                createStoreItemsList(storeSections.getStoreItemsList(), holder.mLlStoreItemsHolder, showBundleView);

            } catch (Throwable e) {
                e.printStackTrace();
            }

        }
    }

    private boolean bundleWithDifferentPowerUps(StoreSections storeSections) {

        if (storeSections != null) {

            List<StoreItems> storeItemsList = storeSections.getStoreItemsList();

            if (storeItemsList != null || !storeItemsList.isEmpty()) {

                if (storeItemsList.get(0) != null) {

                    if (storeItemsList.get(0).getPowerUps() != null) {

                        HashMap<String, Integer> powerUpMap = storeItemsList.get(0).getPowerUps();

                        Integer powerUp2xCount = powerUpMap.get(Constants.Powerups.XX);
                        Integer powerUpNonNegsCount = powerUpMap.get(Constants.Powerups.NO_NEGATIVE);
                        Integer powerUpPlayerPollCount = powerUpMap.get(Constants.Powerups.AUDIENCE_POLL);

                        if (powerUp2xCount != null && powerUpNonNegsCount != null && powerUpPlayerPollCount != null) {
                            if (!allEqual(powerUp2xCount, powerUpNonNegsCount, powerUpPlayerPollCount)) {
                                return true;
                            }
                        }

                    }
                }
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
        HmImageView ivStoreItemImage = (HmImageView) storeItemView.findViewById(R.id.store_item_iv);
        TextView tvStoreItemValue = (TextView) storeItemView.findViewById(R.id.store_item_value_tv);
        RelativeLayout storeItemValueLayout = (RelativeLayout) storeItemView.findViewById(R.id.store_item_value_rl);
        View storeLine = (View) storeItemView.findViewById(R.id.store_item_line);
        LinearLayout storeItemLayout = (LinearLayout) storeItemView.findViewById(R.id.store_item_layout);
        TextView tvStoreItemSecondValue = (TextView) storeItemView.findViewById(R.id.store_item_second_value_tv);
        RelativeLayout storeItemSecondValueLayout = (RelativeLayout) storeItemView.findViewById(R.id.store_item_second_value_rl);

        if (showBundleView) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    storeItemLayout.getResources().getDimensionPixelSize(R.dimen.dim_72));
            storeItemLayout.setLayoutParams(params);
            storeItemValueLayout.setVisibility(View.GONE);
            storeItemSecondValueLayout.setVisibility(View.VISIBLE);
            tvStoreItemSecondValue.setText(WordUtils.capitalize(storeItem.getProductDesc()));
            showOrHidePowerUps(storeItemView, storeItem);
        }else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    storeItemLayout.getResources().getDimensionPixelSize(R.dimen.dim_60));
            storeItemLayout.setLayoutParams(params);
            storeItemValueLayout.setVisibility(View.VISIBLE);
            storeItemSecondValueLayout.setVisibility(View.GONE);
        }

        btnStoreItemPrice.setOnClickListener(this);
        btnStoreItemPrice.setTag(storeItem);

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

        checkIfSaleIsON(storeItemView, storeItem);

        return storeItemView;
    }

    private void checkIfSaleIsON(View storeItemView, StoreItems storeItem) {

        CustomButton btnSalePercentage = (CustomButton) storeItemView.findViewById(R.id.store_item_sale);
        TextView tvStoreItemSaleValue = (TextView) storeItemView.findViewById(R.id.store_item_sale_value_tv);
        TextView tvStoreItemValue = (TextView) storeItemView.findViewById(R.id.store_item_value_tv);
        CustomButton btnStoreItemPrice = (CustomButton) storeItemView.findViewById(R.id.store_item_buy);
        TextView tvStoreItemSecondValue = (TextView) storeItemView.findViewById(R.id.store_item_second_value_tv);
        TextView tvStoreItemSecondSaleValue = (TextView) storeItemView.findViewById(R.id.store_item_second_sale_value_tv);

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
                    tvStoreItemValue.setBackground(ResourcesCompat.getDrawable(tvStoreItemValue.getResources(), R.drawable.strike_line, null));

                    tvStoreItemSaleValue.setText(" Save " + Constants.RUPEE_SYMBOL + String.valueOf(savePrice));

                    tvStoreItemSecondValue.setText((Constants.RUPEE_SYMBOL + originalPrice.toString()));
                    tvStoreItemSecondValue.setBackground(ResourcesCompat.getDrawable(tvStoreItemValue.getResources(), R.drawable.strike_line, null));
                    tvStoreItemSecondSaleValue.setText(" Save " + Constants.RUPEE_SYMBOL + String.valueOf(savePrice));

                    btnStoreItemPrice.setText(WalletHelper.getFormattedStringOfAmount(salePrice));

                }

            } else {
                btnSalePercentage.setVisibility(View.GONE);
            }
        }

    }

    private void showOrHidePowerUps(View storeItemView, StoreItems storeItem) {

        LinearLayout powerUpLayout = (LinearLayout) storeItemView.findViewById(R.id.powerup_bottom_layout);
        ImageView powerUp2xImageView = (ImageView) storeItemView.findViewById(R.id.powerup_2x);
        ImageView powerUpNoNegativeImageView = (ImageView) storeItemView.findViewById(R.id.powerup_noNeg);
        ImageView powerUpAudienceImageView = (ImageView) storeItemView.findViewById(R.id.powerup_audience);
        TextView powerUp2xTextView = (TextView) storeItemView.findViewById(R.id.powerup_2x_count);
        TextView powerUpNoNegativeTextView = (TextView) storeItemView.findViewById(R.id.powerup_noNeg_count);
        TextView powerUpAudienceTextView = (TextView) storeItemView.findViewById(R.id.powerup_audience_count);

        HashMap<String, Integer> powerUpMap = storeItem.getPowerUps();

        if (powerUpMap != null) {
            Integer powerUp2xCount = powerUpMap.get(Constants.Powerups.XX);
            Integer powerUpNonNegsCount = powerUpMap.get(Constants.Powerups.NO_NEGATIVE);
            Integer powerUpPlayerPollCount = powerUpMap.get(Constants.Powerups.AUDIENCE_POLL);

            if (null == powerUp2xCount) {
                powerUp2xCount = 0;
            }

            if (null == powerUpNonNegsCount) {
                powerUpNonNegsCount = 0;
            }

            if (null == powerUpPlayerPollCount) {
                powerUpPlayerPollCount = 0;
            }

            if (powerUp2xCount == 0 && powerUpNonNegsCount == 0 && powerUpPlayerPollCount == 0) {
                powerUpLayout.setVisibility(View.GONE);
            } else {
                powerUpLayout.setVisibility(View.VISIBLE);

                if (powerUp2xCount != 0) {
                    powerUp2xImageView.setBackgroundResource(R.drawable.double_powerup_small);
                    powerUp2xImageView.setVisibility(View.VISIBLE);
                    powerUp2xTextView.setText(String.valueOf(powerUp2xCount));
                } else {
                    powerUp2xImageView.setVisibility(View.GONE);
                    powerUp2xTextView.setVisibility(View.GONE);
                }

                if (powerUpNonNegsCount != 0) {
                    powerUpNoNegativeImageView.setBackgroundResource(R.drawable.no_negative_powerup_small);
                    powerUpNoNegativeImageView.setVisibility(View.VISIBLE);
                    powerUpNoNegativeTextView.setText(String.valueOf(powerUpNonNegsCount));
                } else {
                    powerUpNoNegativeImageView.setVisibility(View.GONE);
                    powerUpNoNegativeTextView.setVisibility(View.GONE);
                }

                if (powerUpPlayerPollCount != 0) {
                    powerUpAudienceImageView.setBackgroundResource(R.drawable.audience_poll_powerup_small);
                    powerUpAudienceImageView.setVisibility(View.VISIBLE);
                    powerUpAudienceTextView.setText(String.valueOf(powerUpPlayerPollCount));
                } else {
                    powerUpAudienceImageView.setVisibility(View.GONE);
                    powerUpAudienceTextView.setVisibility(View.GONE);
                }
            }
        }
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