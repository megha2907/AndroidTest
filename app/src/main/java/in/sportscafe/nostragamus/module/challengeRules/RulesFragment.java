package in.sportscafe.nostragamus.module.challengeRules;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.challengeRules.dto.Rules;
import in.sportscafe.nostragamus.module.common.NostraBaseFragment;
import in.sportscafe.nostragamus.module.contest.dto.PowerUpInfo;

/**
 * Created by deepanshi on 9/8/17.
 */

public class RulesFragment extends NostraBaseFragment implements RulesApiModelImpl.RulesDataListener {

    private static final String TAG = RulesFragment.class.getSimpleName();

    private TextView TvContestRuleOne;
    private TextView TvContestRuleTwo;
    private TextView TvContestRuleThree;
    private TextView TvPowerUpsRuleOne;
    private TextView TvPowerUpsRuleTwo;
    private TextView TvCancelledRuleOne;
    private ImageView IvContestRuleThree;

    private int mContestId = 0;

    private int powerUp2xCount = 0;
    private int powerUpNonNegsCount = 0;
    private int powerUpPlayerPollCount = 0;

    private View rootView;

    private List<String> tournamentsList = new ArrayList<>();

    public RulesFragment() {

    }

    public static RulesFragment newInstance(int contestId) {

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BundleKeys.CONTEST_ID, contestId);
        RulesFragment fragment = new RulesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_rules, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        openBundle();
    }

    private void openBundle() {
        Bundle args = getArguments();
        if (args != null) {
            mContestId = args.getInt(Constants.BundleKeys.CONTEST_ID);
            getRulesData();
        } else {
            showEmptyScreen();
        }
    }

    private void getRulesData() {
        new RulesApiModelImpl().getRulesData(mContestId, this);
    }

    @Override
    public void onData(Rules rules) {
        setInfo(rules);
    }

    private void initViews(View rootView) {
        TvContestRuleOne = (TextView) rootView.findViewById(R.id.contest_rule_one);
        TvContestRuleTwo = (TextView) rootView.findViewById(R.id.contest_rule_two);
        TvContestRuleThree = (TextView) rootView.findViewById(R.id.contest_rule_three);
        IvContestRuleThree = (ImageView) rootView.findViewById(R.id.contest_rule_three_iv);
        TvPowerUpsRuleOne = (TextView) rootView.findViewById(R.id.powerup_rule_one);
        TvPowerUpsRuleTwo = (TextView) rootView.findViewById(R.id.powerup_rule_two);
        TvCancelledRuleOne = (TextView) rootView.findViewById(R.id.cancelled_rule_one);
    }

    private void setInfo(Rules rules) {

        showContestRuleOne(rules);
        showContestRuleTwo(rules);
        showContestRuleThree(rules);

        showPowerUpsRuleOne(rules);
        showPowerUpsRuleTwo(rules);

        showCancelledRuleOne(rules);
    }

    private void showContestRuleOne(Rules rules) {

        tournamentsList = rules.getTournaments();

        if (tournamentsList != null && tournamentsList.size() > 0) {
            TvContestRuleOne.setText("This challenge contains matches from - " + tournamentsList.toString().replaceAll("[\\[\\](){}]", "")
                    + " and " + String.valueOf(rules.getTotalMatches()) + " matches in total");

//            TvContestRuleOne.setText("This challenge has "+ String.valueOf(contest.getTotalMatches()) + " from "
//                    +String.valueOf(contest.getTotalMatches())+" exciting games in "+String.valueOf(tournamentsList.size())
//                    +" tournaments - "+ tournamentsList.toString().replaceAll("[\\[\\](){}]", ""));
        } else {
            if (getView() != null) {
                getView().findViewById(R.id.contest_rule_one_rl).setVisibility(View.GONE);
            }
        }
    }

    private void showContestRuleTwo(Rules rules) {
        TvContestRuleTwo.setText("You can make and edit your predictions half an hr before the matches start");
    }

    private void showContestRuleThree(Rules rules) {
        if (rules.getContestModeInfo() != null) {
            String modeName = rules.getContestModeInfo().getName();
            if (!TextUtils.isEmpty(modeName)) {
                if (modeName.equalsIgnoreCase(Constants.ContestType.GUARANTEED)) {
                    IvContestRuleThree.setBackgroundResource(R.drawable.guaranteed_icon);
                } else if (modeName.equalsIgnoreCase(Constants.ContestType.POOL)) {
                    IvContestRuleThree.setBackgroundResource(R.drawable.pool_icon);
                } else if (modeName.equalsIgnoreCase(Constants.ContestType.NON_GUARANTEED)) {
                    IvContestRuleThree.setBackgroundResource(R.drawable.no_guarantee_icon);
                }

                if (rules.getContestModeInfo().getDescription() != null) {
                    TvContestRuleThree.setText(rules.getContestModeInfo().getName()
                            + " - " + rules.getContestModeInfo().getDescription());
                }
            }
        } else {
            if (getView() != null) {
                getView().findViewById(R.id.contest_rule_three_rl).setVisibility(View.GONE);
            }
        }
    }

    private void showPowerUpsRuleOne(Rules rules) {
        if (rules.getPowerUpInfo() != null) {
            setPowerUps(rules.getPowerUpInfo(), rules);
        } else {
            if (getView() != null) {
                getView().findViewById(R.id.powerups_rule_one_rl).setVisibility(View.GONE);
            }
        }
    }

    private void setPowerUps(PowerUpInfo powerUpInfo, Rules rules) {

        powerUp2xCount = powerUpInfo.getPowerUp2x();
        powerUpNonNegsCount = powerUpInfo.getPowerUpNoNeg();
        powerUpPlayerPollCount = powerUpInfo.getPowerUpPlayerPoll();

        LinearLayout powerUpLayout = (LinearLayout)rootView.findViewById(R.id.powerup_ll);
        ImageView powerUp2xImageView = (ImageView)rootView. findViewById(R.id.powerup_2x);
        ImageView powerUpNoNegativeImageView = (ImageView)rootView. findViewById(R.id.powerup_noNeg);
        ImageView powerUpAudienceImageView = (ImageView)rootView. findViewById(R.id.powerup_audience);

        TextView powerUp2xTextView = (TextView)rootView. findViewById(R.id.powerup_2x_count);
        TextView powerUpNoNegativeTextView = (TextView)rootView.findViewById(R.id.powerup_noNeg_count);
        TextView powerUpAudienceTextView = (TextView)rootView. findViewById(R.id.powerup_audience_count);


        if (powerUp2xCount == 0 && powerUpNonNegsCount == 0 && powerUpPlayerPollCount == 0) {
            powerUpLayout.setVisibility(View.GONE);
        } else {
            powerUpLayout.setVisibility(View.VISIBLE);

            if (powerUp2xCount != 0) {
                powerUp2xImageView.setBackgroundResource(R.drawable.double_powerup_small);
                powerUp2xImageView.setVisibility(View.VISIBLE);
                powerUp2xTextView.setText(String.valueOf(powerUp2xCount));
            } else {
                powerUp2xImageView.setVisibility(View.GONE);
                powerUp2xTextView.setVisibility(View.GONE);
            }

            if (powerUpNonNegsCount != 0) {
                powerUpNoNegativeImageView.setBackgroundResource(R.drawable.no_negative_powerup_small);
                powerUpNoNegativeImageView.setVisibility(View.VISIBLE);
                powerUpNoNegativeTextView.setText(String.valueOf(powerUpNonNegsCount));
            } else {
                powerUpNoNegativeImageView.setVisibility(View.GONE);
                powerUpNoNegativeTextView.setVisibility(View.GONE);
            }

            if (powerUpPlayerPollCount != 0) {
                powerUpAudienceImageView.setBackgroundResource(R.drawable.audience_poll_powerup_small);
                powerUpAudienceImageView.setVisibility(View.VISIBLE);
                powerUpAudienceTextView.setText(String.valueOf(powerUpPlayerPollCount));
            } else {
                powerUpAudienceImageView.setVisibility(View.GONE);
                powerUpAudienceTextView.setVisibility(View.GONE);
            }

            int totalPowerUps = powerUp2xCount + powerUpNonNegsCount + powerUpPlayerPollCount;

            TvPowerUpsRuleOne.setText("You have " + String.valueOf(totalPowerUps)
                    + " powerups to use across " + String.valueOf(rules.getTotalMatches())
                    + " matches. Use them to score higher!");

        }

    }

    private void showPowerUpsRuleTwo(Rules rules) {
        TvPowerUpsRuleTwo.setText("You can transfer a maximum of " + String.valueOf(rules.getMaxTransferPowerUps()) + " powerups each from the bank");
    }


    private void showCancelledRuleOne(Rules rules) {
        TvCancelledRuleOne.setText("Questions will be cancelled in case a player is injured or does not play.In these cases," +
                " half the points would be awarded for the questions.");
    }

    private void showEmptyScreen() {


    }

    @Override
    public void onError(int status) {

    }

    @Override
    public void onNoInternet() {

    }

    @Override
    public void onFailedConfigsApi() {

    }

    @Override
    public void onEmpty() {

    }
}
