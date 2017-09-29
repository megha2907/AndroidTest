package in.sportscafe.nostragamus.module.popups.inapppopups;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import in.sportscafe.nostragamus.module.user.points.PointsActivity;

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

    private static final int LEADERBOARD_POPUP = 26;

    private OnDismissListener mDismissListener;

    public static InAppPopupFragment newInstance(int requestCode, Bundle bundle) {

        bundle.putInt(BundleKeys.DIALOG_REQUEST_CODE, requestCode);
        InAppPopupFragment fragment = new InAppPopupFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnDismissListener) {
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
        ImageView mPopupCrossBtn = (ImageView) findViewById(R.id.popup_cross_btn);
        mPopupCrossBtn.setOnClickListener(this);

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
            mPopUpBodyList.add(new PopUpBody("Tap on a player in the leaderboard to check out their profile, matches and comparisons", R.drawable.popup_player_profile_icon));
            mInAppPopup.setBtnTitle("Ok, got it!");
            NostragamusDataHandler.getInstance().setVisitedLeaderBoards(true);
        } else if (inAppPopUpType.equals(Constants.InAppPopups.SECOND_MATCH_PLAYED_WITH_NO_POWERUP)) {
            mInAppPopup.setHeadingTitle("Don't Miss Out");
            mInAppPopup.setHeadingColor(R.drawable.popup_heading_pink_bg);
            mInAppPopup.setBtnColor(R.drawable.popup_action_pink_btn_bg);
            mInAppPopup.setShowCrossBtn(false);
            mPopUpBodyList.add(new PopUpBody("Use your powerups! Winners often use a powerup with almost every prediction they make", R.drawable.popup_level_icon));
            mPopUpBodyList.add(new PopUpBody("Tap on the   icon if you need help with understanding powerups", R.drawable.popup_help_icon, true));

            mInAppPopup.setBtnTitle("Ok, got it!");
            NostragamusDataHandler.getInstance().setPlayedSecondMatchPopUp(true);
        } else if (inAppPopUpType.equals(Constants.InAppPopups.FIRST_MATCH_PLAYED)) {

            mInAppPopup.setHeadingTitle("Great!");
            mInAppPopup.setBtnTitle("Go To Leaderboard");
            mInAppPopup.setOpenScreen("leaderboards");
            mInAppPopup.setShowCrossBtn(true);
            mInAppPopup.setHeadingColor(R.drawable.popup_heading_blue_bg);
            mInAppPopup.setBtnColor(R.drawable.popup_action_blue_btn_bg);
            mPopUpBodyList.add(new PopUpBody("See how you performed once this event gets over - we will send you a notification", R.drawable.popup_thumb_icon));
            mPopUpBodyList.add(new PopUpBody("Meanwhile, you can check out the other players in this challenge from the leaderboard", R.drawable.popup_leaderboards_icon));
            NostragamusDataHandler.getInstance().setPlayedFirstMatch(true);

            mleaderboardBundle = new Bundle();
            mleaderboardBundle.putParcelable(BundleKeys.LB_LANDING_DATA, Parcels.wrap(new LbLanding(
                    mbundle.getInt(BundleKeys.CHALLENGE_ID), 0,
                    mbundle.getString(BundleKeys.CHALLENGE_NAME),
                    mbundle.getString(BundleKeys.CHALLENGE_PHOTO), Constants.LBLandingType.CHALLENGE
            )));

        }else if(inAppPopUpType.equals(Constants.InAppPopups.SUBMIT_QUESTION)){
            mInAppPopup.setHeadingTitle("Submit Questions & Win Rewards");
            mInAppPopup.setHeadingColor(R.drawable.popup_heading_blue_bg);
            mInAppPopup.setBtnColor(R.drawable.popup_action_blue_btn_bg);
            mInAppPopup.setShowCrossBtn(false);
            mPopUpBodyList.add(new PopUpBody("Submit your questions for the upcoming matches (Deadline: 36 hours before the start of the Match)", R.drawable.popup_question_icon));
            mPopUpBodyList.add(new PopUpBody("Get 3 power ups in the powerup bank for every approved question", R.drawable.powerup_icn));
            mPopUpBodyList.add(new PopUpBody("Once you submit a question, we will mail you at your registered mail id within 24-hrs.", R.drawable.popup_mail_icon));
            mInAppPopup.setBtnTitle("Ok, got it!");
            NostragamusDataHandler.getInstance().setVisitedSubmitQuestion(true);
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
            d.setBounds(0, 0, 26, 26);
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
            case R.id.popup_cross_btn:
                dismiss();
                break;
        }
    }

    private void navigateToCreateGroup() {
        startActivityForResult(new Intent(getContext(), NewGroupActivity.class), NEW_GROUP);
    }

    private void navigateToPointsActivity(Bundle bundle) {
        Intent intent = new Intent(getContext(), PointsActivity.class);
        bundle.putInt(BundleKeys.DIALOG_REQUEST_CODE, LEADERBOARD_POPUP);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mDismissListener.onDismiss(getArguments().getInt(BundleKeys.DIALOG_REQUEST_CODE), null);
    }
}
