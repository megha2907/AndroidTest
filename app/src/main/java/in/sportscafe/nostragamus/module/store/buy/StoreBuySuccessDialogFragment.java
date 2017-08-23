package in.sportscafe.nostragamus.module.store.buy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.widgets.HmImageView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusDialogFragment;
import in.sportscafe.nostragamus.module.store.dto.StoreItems;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreBuySuccessDialogFragment extends NostragamusDialogFragment implements View.OnClickListener {

    private StoreBuySuccessDialogListener mDialogListener;

    public StoreBuySuccessDialogFragment() {}

    public void setDialogListener(StoreBuySuccessDialogListener listener) {
        mDialogListener = listener;
    }

    public static StoreBuySuccessDialogFragment newInstance(int requestCode, StoreItems storeItem,
                                                                    StoreBuySuccessDialogListener listener) {

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BundleKeys.DIALOG_REQUEST_CODE, requestCode);
        bundle.putParcelable(Constants.BundleKeys.STORE_ITEM, Parcels.wrap(storeItem));

        StoreBuySuccessDialogFragment fragment = new StoreBuySuccessDialogFragment();
        fragment.setDialogListener(listener);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_store_buy_success_dialog, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        rootView.findViewById(R.id.popup_cross_btn).setOnClickListener(this);
        rootView.findViewById(R.id.store_success_dialog_ok_button).setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    private void initialize() {
        Bundle args = getArguments();
        if (getView() != null && args != null && args.containsKey(Constants.BundleKeys.STORE_ITEM)) {
            StoreItems storeItems = Parcels.unwrap(args.getParcelable(Constants.BundleKeys.STORE_ITEM));

            if (storeItems != null) {
                if (!TextUtils.isEmpty(storeItems.getProductImage())) {
                    HmImageView productImageView = (HmImageView) getView().findViewById(R.id.store_success_dialog_imageView);
                    productImageView.setImageUrl(storeItems.getProductImage());
                }

                if (!TextUtils.isEmpty(storeItems.getProductName())) {
                    TextView descTextView = (TextView) getView().findViewById(R.id.store_buy_success_desc_textView);
                    String msg = storeItems.getProductName() + " have been added to your bank!";
                    descTextView.setText(msg);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.popup_cross_btn:
                if (mDialogListener != null) {
                    mDialogListener.onOkayButtonClicked();
                }
                dismiss();
                break;

            case R.id.store_success_dialog_ok_button:
                onOkButtonClicked();
                dismiss();
                break;
        }
    }

    private void onOkButtonClicked() {
        if (mDialogListener != null) {
            mDialogListener.onOkayButtonClicked();
        }
    }
}
