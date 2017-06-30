package in.sportscafe.nostragamus.module.navigation.referfriends;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeeva.android.BaseFragment;

import org.parceler.Parcels;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.navigation.wallet.WalletHelper;
import in.sportscafe.nostragamus.webservice.UserReferralInfo;
import in.sportscafe.nostragamus.webservice.UserReferralResponse;


/**
 * Created by deepanshi on 6/21/17.
 */

public class ReferFriendFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = ReferFriendFragment.class.getSimpleName();

    private ReferFriendFragmentListener mReferFriendFragmentListener;

    private TextView tvReferFriendHeading;
    private TextView tvReferFriendOne;
    private TextView tvReferFriendTwo;
    private TextView tvReferralCreditAmount;
    private TextView tvNumberOfFriendsReferred;
    private TextView tvUserReferralCode;

    private Bundle mBundle;

    String mReferralCode;
    String mWalletInit;

    public ReferFriendFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ReferFriendActivity) {
            mReferFriendFragmentListener = (ReferFriendFragmentListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + TAG);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_refer_friend, container, false);

        fetchUserReferralInfoFromServer();
        initRootView(rootView);

        return rootView;
    }

    private void fetchUserReferralInfoFromServer() {

        String appFlavor;
        if (BuildConfig.IS_PAID_VERSION) {
            appFlavor="PRO";
        } else {
            appFlavor=null;
        }
        showProgressbar();
        ReferFriendApiModelImpl.newInstance(new ReferFriendApiModelImpl.ReferFriendApiListener() {
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
            public void onSuccessResponse(UserReferralResponse response) {
                dismissProgressbar();
                setUserReferralInfo(response.getUserReferralInfo());
            }
        }).performApiCall(appFlavor);
    }

    private void initRootView(View rootView) {
        rootView.findViewById(R.id.refer_referral_credit_layout).setOnClickListener(this);
        rootView.findViewById(R.id.refer_terms_layout).setOnClickListener(this);
        rootView.findViewById(R.id.refer_friend_btn).setOnClickListener(this);
        tvReferFriendHeading = (TextView) rootView.findViewById(R.id.refer_friend_heading);
        tvReferFriendOne = (TextView) rootView.findViewById(R.id.refer_friend_subheading_tv_one);
        tvReferFriendTwo = (TextView) rootView.findViewById(R.id.refer_friend_subheading_tv_two);
        tvReferralCreditAmount = (TextView) rootView.findViewById(R.id.refer_referral_credit_amount);
        tvNumberOfFriendsReferred = (TextView) rootView.findViewById(R.id.refer_number_of_friends_referred);
        tvUserReferralCode = (TextView) rootView.findViewById(R.id.refer_referral_code);
    }

    private void setUserReferralInfo(UserReferralInfo userReferralInfo){

        mBundle = new Bundle();
        mBundle.putParcelable(Constants.BundleKeys.USER_REFERRAL_INFO, Parcels.wrap(userReferralInfo));

        if (!TextUtils.isEmpty(userReferralInfo.getReferHeading())){
            tvReferFriendHeading.setText(userReferralInfo.getReferHeading());
        }else {
            tvReferFriendHeading.setText("Give ₹20, get ₹10");
        }

        if (!TextUtils.isEmpty(userReferralInfo.getReferSubHeadingOne())){
            tvReferFriendOne.setText(Html.fromHtml(userReferralInfo.getReferSubHeadingOne()), TextView.BufferType.SPANNABLE);
        }else {
            String styledText = "- When a Friend signs up with your code , they will get <b><font color='#ffffff'>₹ 20</font></b> in their wallet and you receive <b><font color='#ffffff'>2 powerups!</font></b>";
            tvReferFriendOne.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
        }

        if (!TextUtils.isEmpty(userReferralInfo.getReferSubHeadingTwo())){
            tvReferFriendTwo.setText(Html.fromHtml(userReferralInfo.getReferSubHeadingTwo()), TextView.BufferType.SPANNABLE);
        }else {
            String styledTextTwo = "- When a Friend deposits <b><font color='#ffffff'>₹ 20</font></b> in their wallet, you get <b><font color='#ffffff'>₹ 10</font></b> added to your wallet!";
            tvReferFriendTwo.setText(Html.fromHtml(styledTextTwo), TextView.BufferType.SPANNABLE);
        }

        if (userReferralInfo.getReferralCredits()!=null  && userReferralInfo.getReferralCredits() > 0) {
            tvReferralCreditAmount.setText(WalletHelper.getFormattedStringOfAmount(userReferralInfo.getReferralCredits()));
        }
        tvUserReferralCode.setText(userReferralInfo.getReferralCode());
        tvNumberOfFriendsReferred.setText(String.valueOf(userReferralInfo.getFriendsReferred())+" friends added, "
                +String.valueOf(userReferralInfo.getTotalPowerUps())+ " powerups received");

        mReferralCode = userReferralInfo.getReferralCode();
        mWalletInit = String.valueOf(userReferralInfo.getWalletInitialAmount());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refer_referral_credit_layout:
                if (mReferFriendFragmentListener != null) {
                    mReferFriendFragmentListener.onReferralCreditsClicked(mBundle);
                }
                break;

            case R.id.refer_terms_layout:
                if (mReferFriendFragmentListener != null) {
                    mReferFriendFragmentListener.onTermsClicked();
                }
                break;

            case R.id.refer_friend_btn:
                if (mReferFriendFragmentListener != null) {
                    mReferFriendFragmentListener.onReferAFriendClicked(mReferralCode,mWalletInit);
                }
                break;
        }
    }
}