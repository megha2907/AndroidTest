package in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;

import java.util.ArrayList;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletApiModelImpl;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.module.navigation.wallet.dto.UserWalletResponse;
import in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails.dto.PayoutAddEditItemDto;
import in.sportscafe.nostragamus.module.user.login.dto.UserPaymentInfoBankDto;
import in.sportscafe.nostragamus.module.user.login.dto.UserPaymentInfoPaytmDto;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayoutWalletHomeFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = PayoutWalletHomeFragment.class.getSimpleName();
    private PayoutWalletHomeFragmentListener mFragmentListener;

    private RelativeLayout mFreshChatWithdrawalQueriesButton;

    public PayoutWalletHomeFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PayoutWalletHomeFragmentListener) {
            mFragmentListener = (PayoutWalletHomeFragmentListener) context;
        } else {
            throw  new RuntimeException("Activity must implement " +
                    PayoutWalletHomeFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_payout_wallet, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.payout_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        recyclerView.setAdapter(new PayoutHomeRecyclerAdapter(getContext(), getAdapterItemList(), getAdapterActionListener()));

        updateTextOnUi(rootView);
    }

    private void updateTextOnUi(View rootView) {
        TextView infoTextView = (TextView) rootView.findViewById(R.id.payout_wallet_info_textView);
        mFreshChatWithdrawalQueriesButton = (RelativeLayout) rootView.findViewById(R.id.payout_freshchat_btn_rl);
        mFreshChatWithdrawalQueriesButton.setOnClickListener(this);

        boolean isPaytmAdded = WalletHelper.isPaytmPayoutDetailsProvided();
        boolean isBankAdded = WalletHelper.isBankPayoutDetailsProvided();

        if (!isBankAdded && !isPaytmAdded) {
            infoTextView.setText("We strongly recommend using Paytm for faster, hassle-free transactions.");
        }
    }

    private PayoutHomeRecyclerAdapter.IPayoutAdapterActionListener getAdapterActionListener() {
        return new PayoutHomeRecyclerAdapter.IPayoutAdapterActionListener() {
            @Override
            public void onAddPaytmClicked() {
                if (mFragmentListener != null) {
                    mFragmentListener.onAddPaytmClicked();
                }
            }

            @Override
            public void onAddBankClicked() {
                if (mFragmentListener != null) {
                    mFragmentListener.onAddBankClicked();
                }
            }

            @Override
            public void onEditPaytmClicked(PayoutAddEditItemDto payoutAddEditItemDto, int position) {
                if (mFragmentListener != null) {
                    mFragmentListener.onEditPaytmClicked();
                }
            }

            @Override
            public void onEditBankClicked(PayoutAddEditItemDto payoutAddEditItemDto, int position) {
                if (mFragmentListener != null) {
                    mFragmentListener.onEditBankClicked();
                }
            }
        };
    }

    private ArrayList<PayoutAddEditItemDto> getAdapterItemList() {
        ArrayList<PayoutAddEditItemDto> list = new ArrayList<>();

        boolean isPaytmAdded = WalletHelper.isPaytmPayoutDetailsProvided();
        boolean isBankAdded = WalletHelper.isBankPayoutDetailsProvided();

        /* Paytm */
        if (!isPaytmAdded) {
            /* Only one item can be in the whole list */
            PayoutAddEditItemDto addPaytmDetailDto = new PayoutAddEditItemDto();
            addPaytmDetailDto.setViewType(PayoutHomeRecyclerAdapter.PayoutViewType.ADD_PAYTM);

            list.add(addPaytmDetailDto);
        } else {
            UserPaymentInfoPaytmDto paytmDto = WalletHelper.getPaytm();
            if (paytmDto != null) {
                PayoutAddEditItemDto addPaytmDetailDto = new PayoutAddEditItemDto();
                addPaytmDetailDto.setViewType(PayoutHomeRecyclerAdapter.PayoutViewType.SHOW_PAYTM);
                addPaytmDetailDto.setAccountNumber(paytmDto.getMobile());

                list.add(addPaytmDetailDto);
            }
        }

        /* Bank */
        if (!isBankAdded) {
            /* Show only when user NOT ready for Paytm
             * Only one item can be in the whole list */
            PayoutAddEditItemDto addBankDetailDto = new PayoutAddEditItemDto();
            addBankDetailDto.setViewType(PayoutHomeRecyclerAdapter.PayoutViewType.ADD_BANK);

            list.add(addBankDetailDto);
        } else {
            UserPaymentInfoBankDto bankDto = WalletHelper.getBank();
            if (bankDto != null) {
                PayoutAddEditItemDto addBankDetailDto = new PayoutAddEditItemDto();
                addBankDetailDto.setViewType(PayoutHomeRecyclerAdapter.PayoutViewType.SHOW_BANK);
                addBankDetailDto.setAccountName(bankDto.getName());
                addBankDetailDto.setAccountNumber(bankDto.getAccountNo());

                list.add(addBankDetailDto);
            }
        }

        return list;
    }

    /**
     * Perform screen refresh
     */
    public void updateScreenDetails() {
        fetchUserWalletFromServer();
    }

    private void fetchUserWalletFromServer() {
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
                handleSuccessfulWalletResponse();
            }
        }).performApiCall();
    }

    private void handleSuccessfulWalletResponse() {
        View rootView = getView();
        if (getActivity() != null && rootView != null) {
            initView(rootView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.payout_freshchat_btn_rl:
                mFragmentListener.onFreshChatOptionChosen();
                break;
        }
    }
}
