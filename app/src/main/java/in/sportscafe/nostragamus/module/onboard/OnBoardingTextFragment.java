package in.sportscafe.nostragamus.module.onboard;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.ExceptionTracker;
import com.jeeva.android.widgets.HmImageView;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;
import in.sportscafe.nostragamus.module.notifications.InboxFragment;

/**
 * Created by Jeeva on 21/10/16.
 */
public class OnBoardingTextFragment extends NostragamusFragment {

    private static final String ONBOARD_DATA = "onboardData";

    private  TextView mTvReferralCode1;
    private  TextView mTvReferralCode2;
    private  TextView mTvReferralCode3;
    private  TextView mTvReferralCode4;
    private  TextView mTvReferralCode5;
    private  TextView mTvReferralCode6;
    private  TextView mTvReferralCode7;

    private HmImageView mIvOnBoardIn;

    private HmImageView mIvOnBoardOut;

    private RelativeLayout mOnBoardImageLayout;

    public static OnBoardingTextFragment newInstance(OnBoardingDto onBoardingDto) {
        Bundle args = new Bundle();
        args.putParcelable(ONBOARD_DATA, Parcels.wrap(onBoardingDto));

        OnBoardingTextFragment fragment = new OnBoardingTextFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboard_text, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        OnBoardingDto onBoardingDto = Parcels.unwrap(getArguments().getParcelable(ONBOARD_DATA));

        TextView onBoardingTitle = (TextView) findViewById(R.id.onboard_tv_title);
        onBoardingTitle.setText(onBoardingDto.getTitle());
        TextView onBoardingDesc = (TextView) findViewById(R.id.onboard_tv_desc);
        onBoardingDesc.setText(onBoardingDto.getDesc());


        mIvOnBoardIn = (HmImageView) findViewById(R.id.onboard_iv_image_in);
        mIvOnBoardOut = (HmImageView) findViewById(R.id.onboard_iv_image_out);
        mOnBoardImageLayout =(RelativeLayout)findViewById(R.id.onboarding_image_rl);

        mTvReferralCode1 = (TextView) findViewById(R.id.onboard_text_et_referral_code_char_one);
        mTvReferralCode2 = (TextView) findViewById(R.id.onboard_text_et_referral_code_char_two);
        mTvReferralCode3 = (TextView) findViewById(R.id.onboard_text_et_referral_code_char_three);
        mTvReferralCode4 = (TextView) findViewById(R.id.onboard_text_et_referral_code_char_four);
        mTvReferralCode5 = (TextView) findViewById(R.id.onboard_text_et_referral_code_char_five);
        mTvReferralCode6 = (TextView) findViewById(R.id.onboard_text_et_referral_code_char_six);
        mTvReferralCode7 = (TextView) findViewById(R.id.onboard_text_et_referral_code_char_seven);

        LinearLayout referralCode = (LinearLayout) findViewById(R.id.onboard_text_et_referral_code);
        if (onBoardingDto.getReferral()) {
            if (onBoardingDto.getReferralCode()!=null){
                populateUserReferralCode(onBoardingDto.getReferralCode());
            }
            referralCode.setVisibility(View.VISIBLE);
            onBoardingTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.dim_16));

            RelativeLayout.LayoutParams paramsFour = (RelativeLayout.LayoutParams) mOnBoardImageLayout.getLayoutParams();
            paramsFour.topMargin = getResources().getDimensionPixelSize(R.dimen.onboarding_referral_image_gap);
            mOnBoardImageLayout.setLayoutParams(paramsFour);

        } else {
            referralCode.setVisibility(View.GONE);
        }

        mIvOnBoardIn.setImageUrl(onBoardingDto.getImageUrl());
        mIvOnBoardIn.setAlpha(0.1f);
        mIvOnBoardIn.animate().alpha(1).setDuration(1000);

        if (onBoardingDto.getReferralCode()!=null) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mOnBoardImageLayout.getLayoutParams();
            params.height = getResources().getDimensionPixelSize(R.dimen.onboarding_referral_height);
            params.width = getResources().getDimensionPixelSize(R.dimen.onboarding_referral_width);
            mOnBoardImageLayout.setLayoutParams(params);
        }else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mOnBoardImageLayout.getLayoutParams();
            params.height = getResources().getDimensionPixelSize(R.dimen.onboarding_height);
            params.width = getResources().getDimensionPixelSize(R.dimen.onboarding_width);
            mOnBoardImageLayout.setLayoutParams(params);
        }

    }

    public void populateUserReferralCode(String referralCode) {
        try {
            String[] codeSplitter = referralCode.split("");
            mTvReferralCode1.setText(codeSplitter[1]);
            mTvReferralCode2.setText(codeSplitter[2]);
            mTvReferralCode3.setText(codeSplitter[3]);
            mTvReferralCode4.setText(codeSplitter[4]);
            mTvReferralCode5.setText(codeSplitter[5]);
            mTvReferralCode6.setText(codeSplitter[6]);
            mTvReferralCode7.setText(codeSplitter[7]);
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
    }



}