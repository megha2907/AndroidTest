package in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jeeva.android.BaseFragment;

import java.util.ArrayList;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails.dto.PayoutAddEditItemDto;
import in.sportscafe.nostragamus.module.navigation.wallet.payoutDetails.dto.PayoutChoiceDto;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayoutWalletChoiceFragment extends BaseFragment {

    private PayoutWalletChoiceFragmentListener mFragmentListener;
    private PayoutChoiceRecyclerAdapter mPayoutChoiceRecyclerAdapter;
    private RecyclerView mChoiceRecyclerView;

    public PayoutWalletChoiceFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PayoutWalletChoiceFragmentListener) {
            mFragmentListener = (PayoutWalletChoiceFragmentListener) context;
        } else {
            throw  new RuntimeException("Activity must implement " +
                    PayoutWalletChoiceFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_payout_wallet_choice, container, false);
        initRootView(rootView);
        return rootView;
    }

    private void initRootView(View rootView) {
        mChoiceRecyclerView = (RecyclerView) rootView.findViewById(R.id.payout_choice_recyclerView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        initMembers();
        initWithdrawalAmount();

        // Todo: populate withdraw info
    }

    private void initMembers() {
        /*mPayoutChoiceRecyclerAdapter = new PayoutChoiceRecyclerAdapter(getTestList(), getAdapterListener());

        mChoiceRecyclerView.setLayoutManager(new LinearLayoutManager(mChoiceRecyclerView.getContext()));
        mChoiceRecyclerView.setAdapter(mPayoutChoiceRecyclerAdapter);*/
    }

    private void initWithdrawalAmount() {
        Bundle args = getArguments();
        if (args != null) {
            double amount = args.getDouble(Constants.BundleKeys.WALLET_WITHDRAWAL_AMT, -1);
            if (amount > 0) {
                // TODO :  flow & ui
            }
        }
    }

    private PayoutChoiceRecyclerAdapter.IPayoutChoiceAdapterListener getAdapterListener() {
        return new PayoutChoiceRecyclerAdapter.IPayoutChoiceAdapterListener() {
            @Override
            public void onItemClicked(PayoutChoiceDto payoutChoiceDto, int position) {

                if (mPayoutChoiceRecyclerAdapter != null) {
                    ArrayList<PayoutChoiceDto> dataList = mPayoutChoiceRecyclerAdapter.getDataList();
                    if (dataList != null) {
                        for (PayoutChoiceDto item : dataList) {
                            item.setSelected(false);
                        }

                        if (position >= 0 && position < dataList.size()) {
                            dataList.get(position).setSelected(true);
                            mPayoutChoiceRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        };
    }

    private ArrayList<PayoutChoiceDto> getTestList () {
        ArrayList<PayoutChoiceDto> list = new ArrayList<>();

        PayoutChoiceDto d1 = new PayoutChoiceDto();
        d1.setAccountName("Paytm Account");
        d1.setAccountNumber("1234512345");

        PayoutChoiceDto d2 = new PayoutChoiceDto();
        d2.setAccountName("HDFC Bank A/c");
        d2.setAccountNumber("XXXX-XXXX-XXXX-1234");

        list.add(d1);
        list.add(d2);

        return list;
    }
}
