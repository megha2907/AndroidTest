package in.sportscafe.nostragamus.module.allchallenges.info;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.Log;

import org.parceler.Parcels;

import java.util.HashMap;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Constants.Powerups;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeUserInfo;
import in.sportscafe.nostragamus.module.common.NostragamusDialogFragment;
import in.sportscafe.nostragamus.module.common.OnDismissListener;
import in.sportscafe.nostragamus.module.paytm.JoinedChallengeInfo;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by Jeeva on 28/02/17.
 */
public class ChallengeJoinDialogFragment extends NostragamusDialogFragment implements View.OnClickListener {

    private OnDismissListener mDismissListener;

    private int mRequestCode;

    private JoinedChallengeInfo mJoinedChallengeInfo;

    public static ChallengeJoinDialogFragment newInstance(int requestCode, String title, Bundle bundle) {

        bundle.putString(BundleKeys.TITLE, title);
        bundle.putInt(BundleKeys.DIALOG_REQUEST_CODE, requestCode);

        ChallengeJoinDialogFragment fragment = new ChallengeJoinDialogFragment();
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

        mJoinedChallengeInfo = Parcels.unwrap(bundle.getParcelable(BundleKeys.JOINED_CHALLENGE_INFO));
        mRequestCode = bundle.getInt(BundleKeys.DIALOG_REQUEST_CODE);
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

        TextView challengeName = (TextView) findViewById(R.id.challenge_info_tv_challenge_name);
        TextView challengeDesc = (TextView) findViewById(R.id.challenge_info_tv_challenge_desc);
        TextView challengePayoutDate = (TextView) findViewById(R.id.challenge_info_tv_challenge_payout_date);
        ImageView mBtnPopupClose = (ImageView)findViewById(R.id.popup_cross_btn);
        mBtnPopupClose.setVisibility(View.VISIBLE);
        mBtnPopupClose.setOnClickListener(this);

        if (mJoinedChallengeInfo != null) {

            ((TextView) findViewById(R.id.challenge_info_tv_title)).setText("Joined " + mJoinedChallengeInfo.getConfigName());
            challengeName.setText("Joined");

            if (null == mJoinedChallengeInfo.getEntryFee() || mJoinedChallengeInfo.getEntryFee() == 0) {
                challengeDesc.setText("You have successfully joined this challenge for FREE.");
            } else {
                challengeDesc.setText("₹" + mJoinedChallengeInfo.getEntryFee()
                        + " has been deducted from your wallet."+" You have successfully joined this challenge. Best of luck!");
            }

            ChallengeUserInfo challengeUserInfo = mJoinedChallengeInfo.getChallengeUserInfo();

            int powerUpCount = 0;
            HashMap<String, Integer> powerUps = challengeUserInfo.getPowerUps();
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


            long endTimeMs = TimeUtils.getMillisecondsFromDateString(
                    mJoinedChallengeInfo.getEndTime(),
                    Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                    Constants.DateFormats.GMT
            );

            int dayOfMonthinEndTime = Integer.parseInt(TimeUtils.getDateStringFromMs(endTimeMs, "d"));

            // Setting end date of the challenge
            challengePayoutDate.setText("The Challenge ends on the " +
                    dayOfMonthinEndTime + AppSnippet.ordinalOnly(dayOfMonthinEndTime) + " of " +
                    TimeUtils.getDateStringFromMs(endTimeMs, "MMM") + " , Prizes will be handed out within 3 days."
            );
        } else {
            Log.d("Dialog", "ChallengeUserInfo Null");
        }


    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (null != mDismissListener) {
            mDismissListener.onDismiss(mRequestCode, null);
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