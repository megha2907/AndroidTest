package in.sportscafe.nostragamus.module.allchallenges.info;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.HashMap;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeConfig;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeInfo;
import in.sportscafe.nostragamus.module.common.NostragamusDialogFragment;
import in.sportscafe.nostragamus.module.common.OnDismissListener;

/**
 * Created by Jeeva on 28/02/17.
 */
public class ChallengeInfoDialogFragment extends NostragamusDialogFragment {

    private OnDismissListener mDismissListener;

    private int mDialogRequestCode;

    private String mTitle;

    private Challenge mChallenge;

    public static ChallengeInfoDialogFragment newInstance(int requestCode, String title, Challenge challenge) {
        Bundle bundle = new Bundle();
        bundle.putInt(BundleKeys.DIALOG_REQUEST_CODE, requestCode);
        bundle.putString(BundleKeys.TITLE, title);
        bundle.putParcelable(BundleKeys.CHALLENGE_DATA, Parcels.wrap(challenge));

        ChallengeInfoDialogFragment fragment = new ChallengeInfoDialogFragment();
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
        return inflater.inflate(R.layout.fragment_challenge_info, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setCancelable(true);

        openBundle(getArguments());

        initViews();

        populateChallengeDetails();
    }

    private void openBundle(Bundle bundle) {
        mDialogRequestCode = bundle.getInt(BundleKeys.DIALOG_REQUEST_CODE);
        mTitle = bundle.getString(BundleKeys.TITLE);
        mChallenge = Parcels.unwrap(bundle.getParcelable(BundleKeys.CHALLENGE_DATA));
    }

    private void initViews() {
        findViewById(R.id.challenge_info_btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void populateChallengeDetails() {
        ((TextView) findViewById(R.id.challenge_info_tv_title)).setText(mTitle);

        ChallengeInfo challengeInfo = mChallenge.getChallengeInfo();
        ChallengeConfig challengeConfig = challengeInfo.getConfigs().get(mChallenge.getChallengeUserInfo().getConfigIndex());

        ((TextView) findViewById(R.id.challenge_info_tv_challenge_name)).setText(challengeConfig.getConfigName());
        ((TextView) findViewById(R.id.challenge_info_tv_challenge_desc)).setText(mChallenge.getDescription());

        int powerUpCount = 0;
        HashMap<String, Integer> powerUps = challengeInfo.getPowerUps();
        ((TextView) findViewById(R.id.challenge_info_tv_2x)).setText(powerUps.get(Powerups.XX) + "");
        ((TextView) findViewById(R.id.challenge_info_tv_nonegs)).setText(powerUps.get(Powerups.NO_NEGATIVE) + "");
        ((TextView) findViewById(R.id.challenge_info_tv_poll)).setText(powerUps.get(Powerups.AUDIENCE_POLL) + "");

        ((TextView) findViewById(R.id.challenge_info_tv_powerup_desc)).setText(
                String.format(
                        getString(R.string.info_powerup_desc),
                        powerUps.get(Powerups.XX)
                                + powerUps.get(Powerups.NO_NEGATIVE)
                                + powerUps.get(Powerups.AUDIENCE_POLL)
                )
        );
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (null != mDismissListener) {
            mDismissListener.onDismiss(mDialogRequestCode, null);
        }
    }
}