package in.sportscafe.nostragamus.module.popups.inapppopups;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeeva.android.Log;
import com.jeeva.android.widgets.ShadowLayout;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusDialogFragment;
import in.sportscafe.nostragamus.module.common.OnDismissListener;
import in.sportscafe.nostragamus.module.user.group.newgroup.NewGroupActivity;
import in.sportscafe.nostragamus.module.user.lblanding.LbLanding;
import in.sportscafe.nostragamus.module.user.points.PointsActivity;

import static android.R.id.list;

/**
 * Created by deepanshi on 3/25/17.
 */

public class InAppPopupFragment extends NostragamusDialogFragment implements View.OnClickListener {

    private LinearLayout mLlPopupBodyHolder;

    private LayoutInflater mLayoutInflater;

    private Bundle mbundle;

    private String inAppPopUpType;

    private InAppPopup mInAppPopup;

    private Bundle mleaderboardBundle;

    private List<PopUpBody> mPopUpBodyList;

    private static final int NEW_GROUP = 25;

    private OnDismissListener mDismissListener;

    public static InAppPopupFragment newInstance(int requestCode,Bundle bundle) {

        bundle.putInt(BundleKeys.DIALOG_REQUEST_CODE, requestCode);
        InAppPopupFragment fragment = new InAppPopupFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof DialogInterface.OnDismissListener) {
            mDismissListener = (OnDismissListener) context;
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popups, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPopUpBodyList = new ArrayList<PopUpBody>();

        mLlPopupBodyHolder = (LinearLayout) findViewById(R.id.popup_ll_bodytext_holder);

        mLayoutInflater = LayoutInflater.from(getContext());

        mInAppPopup = new InAppPopup();

        findViewById(R.id.popup_btn).setOnClickListener(this);
        findViewById(R.id.popup_iv_close).setOnClickListener(this);

        mbundle = this.getArguments();
        inAppPopUpType = mbundle.getString(Constants.InAppPopups.IN_APP_POPUP_TYPE);
        checkPopupType(inAppPopUpType);

    }

    private void checkPopupType(String inAppPopUpType) {

       if (inAppPopUpType.equals(Constants.InAppPopups.NOT_VISITED_OTHER_PROFILE)) {
            mInAppPopup.setHeadingTitle("Remember!");
            mInAppPopup.setHeadingColor(R.drawable.popup_heading_purple_bg);
            mInAppPopup.setBtnColor(R.drawable.popup_action_purple_btn_bg);
            mInAppPopup.setShowCrossBtn(false);
            mPopUpBodyList.add(new PopUpBody("Tap on any player in the leaderboard to view their profile and the matches they have played", R.drawable.popup_player_profile_icon));
            mInAppPopup.setBtnTitle("Ok, got it!");
            NostragamusDataHandler.getInstance().setVisitedLeaderBoards(true);
        } else if (inAppPopUpType.equals(Constants.InAppPopups.SECOND_MATCH_PLAYED_WITH_NO_POWERUP)) {
            mInAppPopup.setHeadingTitle("Don't Miss Out");
            mInAppPopup.setHeadingColor(R.drawable.popup_heading_pink_bg);
            mInAppPopup.setBtnColor(R.drawable.popup_action_pink_btn_bg);
            mInAppPopup.setShowCrossBtn(false);
            mPopUpBodyList.add(new PopUpBody("Use powerups regularly! Leaderboard toppers often use a powerup in every question", R.drawable.popup_level_icon));
            mPopUpBodyList.add(new PopUpBody("Tap on the   icon if you need help with using powerups", R.drawable.popup_help_icon, true));

            mInAppPopup.setBtnTitle("Ok, got it!");
            NostragamusDataHandler.getInstance().setPlayedSecondMatchPopUp(true);
        }else if (inAppPopUpType.equals(Constants.InAppPopups.FIRST_MATCH_PLAYED)) {

            mInAppPopup.setHeadingTitle("Great!");
            mInAppPopup.setBtnTitle("Go To Leaderboard");
            mInAppPopup.setOpenScreen("leaderboards");
            mInAppPopup.setHeadingColor(R.drawable.popup_heading_blue_bg);
            mInAppPopup.setBtnColor(R.drawable.popup_action_blue_btn_bg);
            mPopUpBodyList.add(new PopUpBody("You can see how you performed once this game is over - we will notify you!", R.drawable.popup_thumb_icon));
            mPopUpBodyList.add(new PopUpBody("Meanwhile, see how others playing this challenge are doing on the leaderboard", R.drawable.popup_leaderboards_icon));
            NostragamusDataHandler.getInstance().setPlayedFirstMatch(true);

            mleaderboardBundle = new Bundle();
            mleaderboardBundle.putParcelable(BundleKeys.LB_LANDING_DATA, Parcels.wrap(new LbLanding(
                    mbundle.getInt(BundleKeys.CHALLENGE_ID), 0,
                    mbundle.getString(BundleKeys.CHALLENGE_NAME),
                    mbundle.getString(BundleKeys.CHALLENGE_PHOTO), Constants.LBLandingType.CHALLENGE
            )));

        }
        showPopUp();
    }

    private void showPopUp() {

        populatePopupBody();

        ((TextView) findViewById(R.id.popup_tv_heading)).setText(mInAppPopup.getHeadingTitle());

        Button mPopupBtn = (Button) findViewById(R.id.popup_btn);
        mPopupBtn.setText(mInAppPopup.getBtnTitle());
        if (mInAppPopup.getBtnColor() != 0) {
            mPopupBtn.setBackgroundResource(mInAppPopup.getBtnColor());
        }

        LinearLayout mPopupHeading = (LinearLayout) findViewById(R.id.popup_ll_heading_layout);
        if (mInAppPopup.getBtnColor() != 0) {
            mPopupHeading.setBackgroundResource(mInAppPopup.getHeadingColor());
        }

        RelativeLayout mPopupCrossBtn = (RelativeLayout) findViewById(R.id.popup_rl_cross_btn);
        if (!mInAppPopup.isShowCrossBtn()) {
            mPopupCrossBtn.setVisibility(View.GONE);
        }

    }

    private void populatePopupBody() {
        View bodyView = null;
        for (int i = 0; i < mPopUpBodyList.size(); i++) {
            bodyView = getBodyView(mPopUpBodyList.get(i));
            mLlPopupBodyHolder.addView(bodyView);
        }
    }


    private View getBodyView(PopUpBody popUpBody) {

        View powerUpView = mLayoutInflater.inflate(R.layout.inflater_popup_body_row, mLlPopupBodyHolder, false);

        ImageView imageView = (ImageView) powerUpView.findViewById(R.id.popup_body_iv_icon);
        imageView.setImageResource(popUpBody.getIcon());

        if (popUpBody.isShowInfoIcon()) {
            SpannableString spannableString = new SpannableString(popUpBody.getDesc());
            Drawable d = getResources().getDrawable(R.drawable.popup_help_icon);
            d.setBounds(0, 0, 23, 23);
            ImageSpan imageSpan = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
            spannableString.setSpan(imageSpan, 11, 12, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            ((TextView) powerUpView.findViewById(R.id.popup_body_tv_desc)).setText(spannableString);
        } else {
            ((TextView) powerUpView.findViewById(R.id.popup_body_tv_desc)).setText(popUpBody.getDesc());
        }

        return powerUpView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_btn:
                if (mInAppPopup.getOpenScreen().equals("groups")) {
                    navigateToCreateGroup();
                } else if (mInAppPopup.getOpenScreen().equals("leaderboards")) {
                    navigateToPointsActivity(mleaderboardBundle);
                } else {
                    dismiss();
                }
                break;
            case R.id.popup_iv_close:
                dismiss();
                break;
        }
    }

    private void navigateToCreateGroup() {
        startActivityForResult(new Intent(getContext(), NewGroupActivity.class), NEW_GROUP);
    }

    private void navigateToPointsActivity(Bundle bundle) {
        Intent intent = new Intent(getContext(), PointsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mDismissListener.onDismiss(getArguments().getInt(BundleKeys.DIALOG_REQUEST_CODE));
    }
}
