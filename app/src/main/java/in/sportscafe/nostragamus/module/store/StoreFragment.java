package in.sportscafe.nostragamus.module.store;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;
import com.jeeva.android.Log;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeConfig;
import in.sportscafe.nostragamus.module.allchallenges.join.CompletePaymentAndJoinDialogFragment;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletApiModelImpl;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.addMoney.lowBalance.AddMoneyOnLowBalanceActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.dto.UserWalletResponse;
import in.sportscafe.nostragamus.module.store.dto.StoreItems;
import in.sportscafe.nostragamus.module.store.dto.StoreSections;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.powerups.PowerUp;

/**
 * Created by deepanshi on 7/25/17.
 */

public class StoreFragment extends BaseFragment {

    private static final String TAG = StoreFragment.class.getSimpleName();
    private static final int BUY_POWER_UP_COMPLETE_PAYMENT_REQUEST_CODE = 501;
    private static final int ADD_MONEY_ON_LOW_BALANCE_REQUEST_CODE = 502;

    private StoreFragmentListener storeFragmentListener;

    private HashMap<String, PowerUp> mPowerUpMaps;

    private TextView mTvRunningLow;

    private TextView tvPBPowerup2xCount;
    private TextView tvPBPowerupNonegCount;
    private TextView tvPBPowerupPlayerPollCount;

    private ImageView ivPBPowerup2x;
    private ImageView ivPBPowerupNoneg;
    private ImageView ivPBPowerupPlayerPoll;

    private RecyclerView mStoreRecyclerView;
    private StoreAdapter storeAdapter;

    public StoreFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof StoreActivity) {
            storeFragmentListener = (StoreFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + TAG);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_store, container, false);

        fetchStoreDataFromServer();
        initRootView(rootView);
//        initAdapter();
        setInfo();

        return rootView;
    }

    private void initAdapter() {

        storeAdapter = new StoreAdapter(getContext(), new BuyButtonListener() {
            @Override
            public void onBuyClicked(StoreItems storeItem) {
                if (storeItem != null) {
                    confirmWalletBalance(storeItem);
                }
            }
        });

        /*if (mStoreRecyclerView != null) {
            mStoreRecyclerView.setAdapter(storeAdapter);
        }*/

    }

    private void confirmWalletBalance(StoreItems storeItem) {
        double price = storeItem.getProductPrice();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.BundleKeys.STORE_ITEM, Parcels.wrap(storeItem));

        if (WalletHelper.isSufficientBalAvailableInWallet(price)) {
            showCompletePaymentDialog(bundle, CompletePaymentAndJoinDialogFragment.DialogLaunchFlow.STORE_BUY_POWERUP_LAUNCH);

        } else {
            // Add money into wallet as less available
            addMoneyInWalletAsLowBalance(bundle);
        }
    }

    private void showCompletePaymentDialog(Bundle args, int launchMode) {
        StoreItems storeItems = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.STORE_ITEM));

        CompletePaymentAndJoinDialogFragment dialogFragment =
                CompletePaymentAndJoinDialogFragment.newInstance(BUY_POWER_UP_COMPLETE_PAYMENT_REQUEST_CODE,
                        launchMode,
                        args,
                        getCompletePaymentDialoActionListener(storeItems));

        dialogFragment.show(getChildFragmentManager(), CompletePaymentAndJoinDialogFragment.class.getSimpleName());
    }

    private CompletePaymentAndJoinDialogFragment.CompletePaymentActionListener
    getCompletePaymentDialoActionListener(final StoreItems storeItems) {
        return new CompletePaymentAndJoinDialogFragment.CompletePaymentActionListener() {
            @Override
            public void onBackClicked() {
                // No action required
            }

            @Override
            public void onPayConfirmed() {
                buyPowerUpFromStore(storeItems);
            }
        };
    }

    private void buyPowerUpFromStore(StoreItems storeItems) {
        // TODO: Make BUY api call
    }

    private void addMoneyInWalletAsLowBalance(Bundle args) {
        Intent intent = new Intent(getActivity(), AddMoneyOnLowBalanceActivity.class);
        intent.putExtras(args);
        if (getActivity() != null) {
            getActivity().startActivityForResult(intent, ADD_MONEY_ON_LOW_BALANCE_REQUEST_CODE);
        }
    }

    private void fetchStoreDataFromServer() {

        showProgressbar();
        StoreApiModelImpl.newInstance(new StoreApiModelImpl.StoreApiListener() {
            @Override
            public void noInternet() {
                dismissProgressbar();
                showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
            }

            @Override
            public void onApiFailed() {
                dismissProgressbar();
                showMessage(Constants.Alerts.API_FAIL);
            }

            @Override
            public void onSuccessResponse(List<StoreSections> storeSectionsList) {
                dismissProgressbar();
                onStoreListFetchedSuccessful(storeSectionsList);
            }
        }).performApiCall("powerups");
    }


    private void initRootView(View rootView) {

        mTvRunningLow = (TextView) rootView.findViewById(R.id.powerup_bank_low_powerups);
        mStoreRecyclerView = (RecyclerView) rootView.findViewById(R.id.storeItemsRecyclerView);
        mStoreRecyclerView.setHasFixedSize(true);
        mStoreRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        tvPBPowerup2xCount = (TextView) rootView.findViewById(R.id.powerup_bank_2x_powerup_count);
        tvPBPowerupNonegCount = (TextView) rootView.findViewById(R.id.powerup_bank_noneg_powerup_count);
        tvPBPowerupPlayerPollCount = (TextView) rootView.findViewById(R.id.powerup_bank_audience_poll_powerup_count);

        ivPBPowerup2x = (ImageView) rootView.findViewById(R.id.powerup_bank_2x);
        ivPBPowerupNoneg = (ImageView) rootView.findViewById(R.id.powerup_bank_noneg);
        ivPBPowerupPlayerPoll = (ImageView) rootView.findViewById(R.id.powerup_bank_audience_poll);

        ivPBPowerup2x.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.double_powerup));
        ivPBPowerupNoneg.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.no_negative_powerup));
        ivPBPowerupPlayerPoll.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.audience_poll_powerup));

    }

    private void setInfo() {

        if (NostragamusDataHandler.getInstance().getUserInfo() != null) {
            UserInfo userInfo = NostragamusDataHandler.getInstance().getUserInfo();
            getPowerUps(userInfo);
        }

    }

    private void getPowerUps(UserInfo userInfo) {

        if (userInfo.getPowerUps() != null) {

            mPowerUpMaps = getPowerUpMap(userInfo.getPowerUps());

            int count;

            PowerUp powerUp = mPowerUpMaps.get(Constants.Powerups.XX);
            if (null == powerUp) {
                count = 0;
            } else {
                count = powerUp.getCount();
            }
            onGet2xPowerUp(count, count < 1);

            powerUp = mPowerUpMaps.get(Constants.Powerups.NO_NEGATIVE);
            if (null == powerUp) {
                count = 0;
            } else {
                count = powerUp.getCount();
            }
            onGetNonegsPowerUp(count, count < 1);

            powerUp = mPowerUpMaps.get(Constants.Powerups.AUDIENCE_POLL);
            if (null == powerUp) {
                count = 0;
            } else {
                count = powerUp.getCount();
            }
            onGetPollPowerUp(count, count < 1);
        } else {
            int count = 0;
            onGet2xPowerUp(count, count < 1);
            onGetNonegsPowerUp(count, count < 1);
            onGetPollPowerUp(count, count < 1);
        }

    }

    private void onGetPollPowerUp(int count, boolean runningLow) {
        setPowerUpCount(tvPBPowerupPlayerPollCount, count, runningLow, Constants.Powerups.AUDIENCE_POLL);
    }

    private void onGetNonegsPowerUp(int count, boolean runningLow) {
        setPowerUpCount(tvPBPowerupNonegCount, count, runningLow, Constants.Powerups.NO_NEGATIVE);
    }

    private void onGet2xPowerUp(int count, boolean runningLow) {
        setPowerUpCount(tvPBPowerup2xCount, count, runningLow, Constants.Powerups.XX);
    }

    private HashMap<String, PowerUp> getPowerUpMap(HashMap<String, Integer> powerUps) {
        HashMap<String, PowerUp> powerUpMaps = new HashMap<>();
        for (Map.Entry<String, Integer> entry : powerUps.entrySet()) {
            powerUpMaps.put(entry.getKey(), new PowerUp(entry.getKey(), entry.getValue()));
        }
        return powerUpMaps;
    }

    private void setPowerUpCount(TextView textView, int count, boolean runningLow, String powerUp) {
        textView.setText(String.valueOf(count));
        if (runningLow) {
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white_999999));

            if (powerUp.equalsIgnoreCase(Constants.Powerups.XX)) {
                ivPBPowerup2x.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.double_grey_powerup));
            } else if (powerUp.equalsIgnoreCase(Constants.Powerups.NO_NEGATIVE)) {
                ivPBPowerupNoneg.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.no_negative_grey_powerup));
            } else if (powerUp.equalsIgnoreCase(Constants.Powerups.AUDIENCE_POLL)) {
                ivPBPowerupPlayerPoll.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.audience_poll_grey_powerup));
            }

            if (mTvRunningLow.getVisibility() == View.GONE) {
                mTvRunningLow.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showOrHideContentBasedOnAppType() {
        /* if design changes needed for playstore app */
        if (!BuildConfig.IS_PAID_VERSION) {

        }
    }

    private void onStoreListFetchedSuccessful(List<StoreSections> storeSectionsList) {


        initAdapter();
        if (storeAdapter != null) {
            storeAdapter.addStoreSectionsIntoList(storeSectionsList);
        }

        if (mStoreRecyclerView != null) {
            mStoreRecyclerView.setAdapter(storeAdapter);
        }

        RelativeLayout recyclerViewLayout = (RelativeLayout) getView().findViewById(R.id.store_items_rl);
        /* Empty list view */
        if (getActivity() != null && getView() != null && storeAdapter != null) {
            if (storeAdapter.getStoreSectionsList() == null || storeAdapter.getStoreSectionsList().isEmpty()) {
                mStoreRecyclerView.setVisibility(View.GONE);
                recyclerViewLayout.setVisibility(View.GONE);
                LinearLayout noHistoryLayout = (LinearLayout) getView().findViewById(R.id.store_empty_history_layout);
                noHistoryLayout.setVisibility(View.VISIBLE);
            } else {
                recyclerViewLayout.setVisibility(View.VISIBLE);
                mStoreRecyclerView.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_MONEY_ON_LOW_BALANCE_REQUEST_CODE &&
                resultCode == Activity.RESULT_OK &&
                data != null && data.getExtras() != null) {

                /* Fetch wallet details and continue in loop */
                fetchUserWalletFromServer(CompletePaymentAndJoinDialogFragment.DialogLaunchFlow.STORE_BUY_POWERUP_AFTER_LOW_BAL_LAUNCH,
                        data.getExtras());

        }  else {
            showMessage(Constants.Alerts.SOMETHING_WRONG);
        }
    }

    private void fetchUserWalletFromServer(final int launchMode, final Bundle args) {
        showProgressbar();
        WalletApiModelImpl.newInstance(new WalletApiModelImpl.WalletApiListener() {
            @Override
            public void noInternet() {
                dismissProgressbar();
                showMessage(Constants.Alerts.NO_NETWORK_CONNECTION);
            }

            @Override
            public void onApiFailed() {
                dismissProgressbar();
                showMessage(Constants.Alerts.API_FAIL);
            }

            @Override
            public void onSuccessResponse(UserWalletResponse response) {
                dismissProgressbar();
                showCompletePaymentDialog(args, launchMode);
            }
        }).performApiCall();
    }
}
