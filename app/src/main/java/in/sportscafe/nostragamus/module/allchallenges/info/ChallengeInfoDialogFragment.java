package in.sportscafe.nostragamus.module.allchallenges.info;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeeva.android.Log;

import org.parceler.Parcels;

import java.util.HashMap;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.dto.Challenge;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeInfo;
import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeUserInfo;
import in.sportscafe.nostragamus.module.common.NostragamusDialogFragment;
import in.sportscafe.nostragamus.module.common.OnDismissListener;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by deepanshi on 4/12/17.
 */

public class ChallengeInfoDialogFragment extends NostragamusDialogFragment implements View.OnClickListener {

    private OnDismissListener mDismissListener;

    private int CHALLENGE_INFO_REQUEST_CODE = 58;

    private Challenge mChallenge;

    private String mTitle;

    public static ChallengeInfoDialogFragment newInstance(int requestCode, String title, Challenge challenge) {

        Bundle bundle = new Bundle();
        bundle.putString(Constants.BundleKeys.TITLE, title);
        bundle.putInt(Constants.BundleKeys.DIALOG_REQUEST_CODE, requestCode);
        bundle.putParcelable(Constants.BundleKeys.CHALLENGE_DATA, Parcels.wrap(challenge));

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
        mTitle = bundle.getString(Constants.BundleKeys.TITLE);
        mChallenge = Parcels.unwrap(bundle.getParcelable(Constants.BundleKeys.CHALLENGE_DATA));
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
        TextView challengePayoutDateTxt = (TextView) findViewById(R.id.challenge_info_tv_challenge_payout_txt);
        Button challengeBtnClose = (Button) findViewById(R.id.challenge_info_btn_close);
        ImageView mBtnPopupClose = (ImageView) findViewById(R.id.popup_cross_btn);
        mBtnPopupClose.setVisibility(View.VISIBLE);
        mBtnPopupClose.setOnClickListener(this);

        if (mChallenge != null) {

            ((TextView) findViewById(R.id.challenge_info_tv_title)).setText(mChallenge.getName() + mTitle);
            challengeName.setVisibility(View.GONE);
            challengeDesc.setText(mChallenge.getDescription());
            challengeDesc.setPadding(0, 15, 0, 0);

            int powerUpCount = 0;
            HashMap<String, Integer> powerUps;

            if (mChallenge.getChallengeUserInfo().isUserJoined()) {
                powerUps = mChallenge.getChallengeUserInfo().getPowerUps();
            }else{
                powerUps = mChallenge.getChallengeInfo().getPowerUps();
            }

            ((TextView) findViewById(R.id.challenge_info_tv_2x)).setText(powerUps.get(Constants.Powerups.XX) + "");
            ((TextView) findViewById(R.id.challenge_info_tv_nonegs)).setText(powerUps.get(Constants.Powerups.NO_NEGATIVE) + "");
            ((TextView) findViewById(R.id.challenge_info_tv_poll)).setText(powerUps.get(Constants.Powerups.AUDIENCE_POLL) + "");

            String powerUpTotalCount = String.valueOf(powerUps.get(Constants.Powerups.XX)
                    + powerUps.get(Constants.Powerups.NO_NEGATIVE)
                    + powerUps.get(Constants.Powerups.AUDIENCE_POLL));

            ((TextView) findViewById(R.id.challenge_info_tv_powerup_desc)).setText(
                    String.format("You have "+ powerUpTotalCount
                            +" powerups to use across "
                            + String.valueOf(mChallenge.getMatchesCategorized().getAllMatches().size() +
                            " matches in this challenge, Use them to score higher!")));


            long endTimeMs = TimeUtils.getMillisecondsFromDateString(
                    mChallenge.getEndTime(),
                    Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                    Constants.DateFormats.GMT
            );

            int dayOfMonthinEndTime = Integer.parseInt(TimeUtils.getDateStringFromMs(endTimeMs, "d"));

            if (BuildConfig.IS_PAID_VERSION) {
                challengePayoutDateTxt.setText("Prizes");
                // Setting end date of the challenge
                challengePayoutDate.setText("The NewChallengesResponse ends on the " +
                        dayOfMonthinEndTime + AppSnippet.ordinalOnly(dayOfMonthinEndTime) + " of " +
                        TimeUtils.getDateStringFromMs(endTimeMs, "MMM") + " , Prizes will be handed out within a few hours of challenge completion."
                );

            } else {
                challengePayoutDateTxt.setText("Results");

                // Setting end date of the challenge
                challengePayoutDate.setText("The NewChallengesResponse ends on the " +
                        dayOfMonthinEndTime + AppSnippet.ordinalOnly(dayOfMonthinEndTime) + " of " +
                        TimeUtils.getDateStringFromMs(endTimeMs, "MMM") + " , Results will be out within few hours."
                );
            }


        } else {
            Log.d("Dialog", "ChallengeUserInfo Null");
        }

        challengeBtnClose.setText("Ok, got it!");


    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (null != mDismissListener) {
            mDismissListener.onDismiss(CHALLENGE_INFO_REQUEST_CODE, null);
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