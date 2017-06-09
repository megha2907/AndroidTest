package in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeeva.android.BaseFragment;

import java.util.ArrayList;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails.dto.PayoutAddEditItemDto;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayoutWalletHomeFragment extends BaseFragment {

    private static final String TAG = PayoutWalletHomeFragment.class.getSimpleName();
    private PayoutWalletHomeFragmentListener mFragmentListener;

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


        /* Only one item can be in the whole list */
        PayoutAddEditItemDto addPaytmDetailDto = new PayoutAddEditItemDto();
        addPaytmDetailDto.setViewType(PayoutHomeRecyclerAdapter.PayoutViewType.ADD_PAYTM);

        /* Show only when user NOT ready for Paytm
         * Only one item can be in the whole list */
        PayoutAddEditItemDto addBankDetailDto = new PayoutAddEditItemDto();
        addBankDetailDto.setViewType(PayoutHomeRecyclerAdapter.PayoutViewType.ADD_BANK);

        list.add(addPaytmDetailDto);
        list.add(addBankDetailDto);

        return list;
    }

}