package in.sportscafe.nostragamus.module.newChallenges.dataProvider;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.inPlay.helper.InPlayFilterHelper;
import in.sportscafe.nostragamus.module.newChallenges.dto.SportsTab;
import in.sportscafe.nostragamus.module.newChallenges.helpers.NewChallengesFilterHelper;

/**
 * Created by sandip on 30/08/17.
 */

public class SportsDataProvider {

    public static final int FILTER_ALL_SPORTS_ID = -11;
    public static final int FILTER_DAILY_SPORTS_ID = -12;
    public static final int FILTER_MIXED_SPORTS_ID = 9; // Sent from server

    public List<SportsTab> getSportsList() {
        List<SportsTab> sportsList = new ArrayList<>();
        List<SportsTab> sportsAclList = new ArrayList<>();

        SportsTab all = new SportsTab();
        all.setSportsId(FILTER_ALL_SPORTS_ID);
        if (BuildConfig.IS_ACL_VERSION) {
            all.setSportsName("Others");
        }else {
            all.setSportsName("All");
        }
        all.setSportIconDrawable(R.drawable.sport_all);
        all.setSportIconUnSelectedDrawable(R.drawable.sport_all_grey);

        SportsTab daily = new SportsTab();
        daily.setSportsId(FILTER_DAILY_SPORTS_ID);
        daily.setSportsName("Daily");
        daily.setSportIconDrawable(R.drawable.sport_daily);
        daily.setSportIconUnSelectedDrawable(R.drawable.sport_daily_grey);

        SportsTab mixed = new SportsTab();
        mixed.setSportsId(FILTER_MIXED_SPORTS_ID);
        mixed.setSportsName("Mixed");
        mixed.setSportIconDrawable(R.drawable.sport_mixed);
        mixed.setSportIconUnSelectedDrawable(R.drawable.sport_mix_grey);

        SportsTab cricket = new SportsTab();
        cricket.setSportsId(1);
        cricket.setSportsName("Cricket");
        cricket.setSportIconDrawable(R.drawable.sport_cricket);
        cricket.setSportIconUnSelectedDrawable(R.drawable.sport_cricket_grey);

        SportsTab hockey = new SportsTab();
        hockey.setSportsId(2);
        hockey.setSportsName("Hockey");
        hockey.setSportIconDrawable(R.drawable.sport_hockey);
        hockey.setSportIconUnSelectedDrawable(R.drawable.sport_hockey_grey);

        SportsTab tennis = new SportsTab();
        tennis.setSportsId(3);
        tennis.setSportsName("Tennis");
        tennis.setSportIconDrawable(R.drawable.sport_tennis);
        tennis.setSportIconUnSelectedDrawable(R.drawable.sport_tennis_grey);

        SportsTab football = new SportsTab();
        football.setSportsId(4);
        football.setSportsName("Football");
        football.setSportIconDrawable(R.drawable.sport_football);
        football.setSportIconUnSelectedDrawable(R.drawable.sport_football_grey);

        SportsTab badminton = new SportsTab();
        badminton.setSportsId(6);
        badminton.setSportsName("Badminton");
        badminton.setSportIconDrawable(R.drawable.sport_badminton);
        badminton.setSportIconUnSelectedDrawable(R.drawable.sport_badminton_grey);

        SportsTab basketBall = new SportsTab();
        basketBall.setSportsId(8);
        basketBall.setSportsName("Basketball");
        basketBall.setSportIconDrawable(R.drawable.sport_basketball);
        basketBall.setSportIconUnSelectedDrawable(R.drawable.sport_basketball_grey);

        SportsTab kabbadi = new SportsTab();
        kabbadi.setSportsId(5);
        kabbadi.setSportsName("Kabbadi");
        kabbadi.setSportIconDrawable(R.drawable.sport_kabaddi);
        kabbadi.setSportIconUnSelectedDrawable(R.drawable.sport_kabaddi_grey);

        SportsTab formulaOne = new SportsTab();
        formulaOne.setSportsId(7);
        formulaOne.setSportsName("Formula One");
        formulaOne.setSportIconDrawable(R.drawable.sport_formula_one);
        formulaOne.setSportIconUnSelectedDrawable(R.drawable.sport_racing_grey);

        SportsTab boxing = new SportsTab();
        boxing.setSportsId(10);
        boxing.setSportsName("Boxing");
        boxing.setSportIconDrawable(R.drawable.sport_boxing);
        boxing.setSportIconUnSelectedDrawable(R.drawable.sport_boxing_grey);

        SportsTab golf = new SportsTab();
        golf.setSportsId(11);
        golf.setSportsName("Golf");
        golf.setSportIconDrawable(R.drawable.sport_golf);
        golf.setSportIconUnSelectedDrawable(R.drawable.sport_golf_grey);

        SportsTab rugby = new SportsTab();
        rugby.setSportsId(12);
        rugby.setSportsName("Rugby");
        rugby.setSportIconDrawable(R.drawable.sport_rugby);
        rugby.setSportIconUnSelectedDrawable(R.drawable.sport_rugby_grey);

        SportsTab volleyball = new SportsTab();
        volleyball.setSportsId(14);
        volleyball.setSportsName("Volleyball");
        volleyball.setSportIconDrawable(R.drawable.sport_volleyball);
        volleyball.setSportIconUnSelectedDrawable(R.drawable.sport_volleyball_grey);

        SportsTab motoGP = new SportsTab();
        motoGP.setSportsId(13);
        motoGP.setSportsName("Moto GP");
        motoGP.setSportIconDrawable(R.drawable.sport_motogp);
        motoGP.setSportIconUnSelectedDrawable(R.drawable.sport_racing_grey);

        SportsTab ACL = new SportsTab();
        ACL.setSportsId(15);
        ACL.setSportsName("ACL 2017-18");
        ACL.setSportIconDrawable(R.drawable.acl_sports_icn);
        ACL.setSportIconUnSelectedDrawable(R.drawable.acl_sports_greyed_icn);

        /* This is priority order, to show tabs */
        sportsList.add(all);
        sportsList.add(daily);
        sportsList.add(mixed);
        sportsList.add(cricket);
        sportsList.add(football);
        sportsList.add(kabbadi);
        sportsList.add(tennis);
        sportsList.add(basketBall);
        sportsList.add(badminton);
        sportsList.add(hockey);
        sportsList.add(formulaOne);
        sportsList.add(boxing);
        sportsList.add(golf);
        sportsList.add(rugby);
        sportsList.add(volleyball);
        sportsList.add(motoGP);

        /* For Ahmedabad Champions League , Show only All and ACL Tabs */
        sportsAclList.add(ACL);
        sportsAclList.add(all);

        if (BuildConfig.IS_ACL_VERSION) {
            return sportsAclList;
        }else {
            return sportsList;
        }
    }

}
