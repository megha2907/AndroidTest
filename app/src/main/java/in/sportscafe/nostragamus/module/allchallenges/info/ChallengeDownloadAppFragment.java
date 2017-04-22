package in.sportscafe.nostragamus.module.allchallenges.info;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.ExceptionTracker;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusDialogFragment;
import in.sportscafe.nostragamus.module.common.OnDismissListener;

/**
 * Created by deepanshi on 4/13/17.
 */

public class ChallengeDownloadAppFragment extends NostragamusDialogFragment implements View.OnClickListener {

    private OnDismissListener mDismissListener;

    private int CHALLENGE_DOWNLOAD_REQUEST_CODE = 59;

    public static ChallengeDownloadAppFragment newInstance(int requestCode) {

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BundleKeys.DIALOG_REQUEST_CODE, requestCode);

        ChallengeDownloadAppFragment fragment = new ChallengeDownloadAppFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDismissListener) {
            mDismissListener = (OnDismissListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_challenge_download_app, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setCancelable(true);

        openBundle(getArguments());

        initViews();

    }

    private void openBundle(Bundle bundle) {

    }

    private void initViews() {
        findViewById(R.id.challenge_dw_app_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToAppUrl(NostragamusDataHandler.getInstance().getPaidNormalApkLink());
            }
        });

        TextView downloadText = (TextView)findViewById(R.id.challenge_dw_app_tv);
        if (!TextUtils.isEmpty(NostragamusDataHandler.getInstance().getDownloadPaidApp())) {
            downloadText.setText(NostragamusDataHandler.getInstance().getDownloadPaidApp());
        }else {
            downloadText.setText(getResources().getString(R.string.download_app_text));
        }

        ImageView mBtnPopupClose = (ImageView)findViewById(R.id.popup_cross_btn);
        mBtnPopupClose.setVisibility(View.VISIBLE);
        mBtnPopupClose.setOnClickListener(this);

    }

    private void navigateToAppUrl(String appUrl) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(appUrl)));
        } catch (ActivityNotFoundException e) {
            ExceptionTracker.track(e);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (null != mDismissListener) {
            mDismissListener.onDismiss(CHALLENGE_DOWNLOAD_REQUEST_CODE, null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_cross_btn:
                dismiss();
                break;
        }
    }
}