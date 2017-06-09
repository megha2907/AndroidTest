package in.sportscafe.nostragamus.module.navigation.wallet;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jeeva.android.BaseFragment;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.utils.AnimationHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletHomeFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = WalletHomeFragment.class.getSimpleName();

    private WalletHomeFragmentListener mFragmentListener;
    private LinearLayout mWalletMoneyInfoLayout;
    private LinearLayout mPromoMoneyInfoLayout;

    public WalletHomeFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WalletHomeFragmentListener) {
            mFragmentListener = (WalletHomeFragmentListener) context;
        } else {
            throw  new RuntimeException("Activity must implement " +
                    WalletHomeFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wallet_home, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        rootView.findViewById(R.id.wallet_add_money_layout_button).setOnClickListener(this);
        rootView.findViewById(R.id.wallet_withdraw_money_layout_button).setOnClickListener(this);
        rootView.findViewById(R.id.wallet_transaction_history_card).setOnClickListener(this);
        rootView.findViewById(R.id.wallet_payout_detail_card).setOnClickListener(this);
        rootView.findViewById(R.id.wallet_home_card_money_layout).setOnClickListener(this);
        rootView.findViewById(R.id.wallet_home_card_promo_layout).setOnClickListener(this);

        mWalletMoneyInfoLayout = (LinearLayout) rootView.findViewById(R.id.wallet_money_info_layout);
        mPromoMoneyInfoLayout = (LinearLayout) rootView.findViewById(R.id.wallet_promo_info_layout);
        mWalletMoneyInfoLayout.setOnClickListener(this);
        mPromoMoneyInfoLayout.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        WalletApiModelImpl.newInstance(new WalletApiModelImpl.WalletApiListener() {
            @Override
            public void noInternet() {
                showMessage("No Net");
            }

            @Override
            public void onApiFailed() {
                showMessage("API failed");
            }

            @Override
            public void onSuccessResponse() {
                showMessage("API Success");
            }
        }).performApiCall();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wallet_add_money_layout_button:
                onAddMoneyClicked();
                break;

            case R.id.wallet_withdraw_money_layout_button:
                onWithdrawMoneyClicked();
                break;

            case R.id.wallet_transaction_history_card:
                onTransactionHistoryClicked();
                break;

            case R.id.wallet_payout_detail_card:
                onPayoutDetailsClicked();
                break;

            case R.id.wallet_home_card_money_layout:
                onMoneyInfoLayoutClicked();
                break;

            case R.id.wallet_home_card_promo_layout:
                onPayoutInfoLayoutClicked();
                break;
        }
    }

    private void onPayoutInfoLayoutClicked() {
        if (mPromoMoneyInfoLayout.getVisibility() == View.VISIBLE) {
            AnimationHelper.collapse(mPromoMoneyInfoLayout);
        } else {
            AnimationHelper.expand(mPromoMoneyInfoLayout);
        }
    }

    private void onMoneyInfoLayoutClicked() {
         if (mWalletMoneyInfoLayout.getVisibility() == View.VISIBLE) {
             AnimationHelper.collapse(mWalletMoneyInfoLayout);
         } else {
             AnimationHelper.expand(mWalletMoneyInfoLayout);
         }
    }

    private void onPayoutDetailsClicked() {
        if (mFragmentListener != null) {
            mFragmentListener.onPayoutDetailsClicked();
        }
    }

    private void onTransactionHistoryClicked() {
        if (mFragmentListener != null) {
            mFragmentListener.onTransactionHistoryClicked();
        }
    }

    private void onWithdrawMoneyClicked() {
        if (mFragmentListener != null) {
            mFragmentListener.onWithdrawMoneyClicked();
        }
    }

    private void onAddMoneyClicked() {
        if (mFragmentListener != null) {
            mFragmentListener.onAddMoneyClicked();
        }
    }
}
