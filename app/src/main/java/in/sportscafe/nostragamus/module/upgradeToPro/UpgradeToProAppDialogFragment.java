package in.sportscafe.nostragamus.module.upgradeToPro;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.jeeva.android.BaseDialogFragment;
import com.jeeva.android.ExceptionTracker;
import com.jeeva.android.widgets.HmImageView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.service.NostraFileDownloadService;
import in.sportscafe.nostragamus.utils.StorageUtility;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpgradeToProAppDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    private String mDownloadProAppLink = "";

    public UpgradeToProAppDialogFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_upgrade_to_pro_app_dialog_for, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        Button downloadButton = (Button) rootView.findViewById(R.id.app_upgrade_dialog_button);
        downloadButton.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentImage();
    }

    private void showContentImage() {
        Bundle args = getArguments();
        if (getView() != null && args != null && args.containsKey(Constants.BundleKeys.IMAGE_URL)) {
            HmImageView contentImageView  = (HmImageView) getView().findViewById(R.id.app_upgrade_imgView);

            String url = args.getString(Constants.BundleKeys.IMAGE_URL, "");
            if (!TextUtils.isEmpty(url)) {
                contentImageView.setImageUrl(url);
            }

            mDownloadProAppLink = args.getString(Constants.BundleKeys.DOWNLOAD_URL, "");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.app_upgrade_dialog_button:
                onDownloadButtonClicked();
                break;
        }
    }

    private void onDownloadButtonClicked() {
        if (!TextUtils.isEmpty(mDownloadProAppLink)) {
            Intent intent = new Intent(getContext().getApplicationContext(), NostraFileDownloadService.class);
            intent.putExtra(NostraFileDownloadService.FILE_DOWNLOAD_URL, mDownloadProAppLink);
            intent.putExtra(NostraFileDownloadService.FILE_NAME_WITH_EXTENSION, StorageUtility.getFileNameWithSuffix(mDownloadProAppLink));
            getContext().getApplicationContext().startService(intent);

        } else {
            showMessage(Constants.Alerts.SOMETHING_WRONG);
            try {
                if (getActivity() != null) {
                    getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://nostragamus.in/pro/")));
                }
            } catch (ActivityNotFoundException e) {
                ExceptionTracker.track(e);
            }
        }

        dismissDialog();
    }

    private void dismissDialog() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    UpgradeToProAppDialogFragment.this.dismissAllowingStateLoss();
                } catch (Exception ex) {}
            }
        }, 300);
    }
}
