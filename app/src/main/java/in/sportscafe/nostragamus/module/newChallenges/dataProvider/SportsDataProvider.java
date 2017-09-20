package in.sportscafe.nostragamus.module.newChallenges.dataProvider;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.inPlay.helper.InPlayFilterHelper;
import in.sportscafe.nostragamus.module.newChallenges.dto.SportsTab;
import in.sportscafe.nostragamus.module.newChallenges.helpers.NewChallengesFilterHelper;

/**
 * Created by sandip on 30/08/17.
 */

public class SportsDataProvider {


    //TODO: get sports details from server and make changes appropriately

    public List<SportsTab> getSportsList() {
        List<SportsTab> sportsList = new ArrayList<>();

        SportsTab all = new SportsTab();
        all.setSportsId(NewChallengesFilterHelper.FILTER_ALL_SPORTS_ID);
        all.setSportsName("All");
        all.setSportIconDrawable(R.drawable.sport_all);
        all.setSportIconUnSelectedDrawable(R.drawable.sport_all_grey);

        SportsTab daily = new SportsTab();
        daily.setSportsId(NewChallengesFilterHelper.FILTER_ALL_SPORTS_ID);
        daily.setSportsName("Daily");
        daily.setSportIconDrawable(R.drawable.sport_daily);
        daily.setSportIconUnSelectedDrawable(R.drawable.sport_daily_grey);

        SportsTab mixed = new SportsTab();
        mixed.setSportsId(NewChallengesFilterHelper.FILTER_ALL_SPORTS_ID);
        mixed.setSportsName("Mixed");
        mixed.setSportIconDrawable(R.drawable.sport_mix);
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
        formulaOne.setSportIconDrawable(R.drawable.sport_racing);
        formulaOne.setSportIconUnSelectedDrawable(R.drawable.sport_racing_grey);

        sportsList.add(all);
        sportsList.add(daily);
        sportsList.add(mixed);
        sportsList.add(cricket);
        sportsList.add(basketBall);
        sportsList.add(football);
        sportsList.add(tennis);
        sportsList.add(kabbadi);
        sportsList.add(badminton);
        sportsList.add(hockey);
        sportsList.add(formulaOne);

        return sportsList;
    }

    public List<SportsTab> getInPlaySportsList() {
        List<SportsTab> sportsList = new ArrayList<>();

        SportsTab all = new SportsTab();
        all.setSportsId(NewChallengesFilterHelper.FILTER_ALL_SPORTS_ID);
        all.setSportsName("All");
        all.setSportIconDrawable(R.drawable.sport_all);
        all.setSportIconUnSelectedDrawable(R.drawable.sport_all_grey);

        SportsTab daily = new SportsTab();
        daily.setSportsId(NewChallengesFilterHelper.FILTER_ALL_SPORTS_ID);
        daily.setSportsName("Daily");
        daily.setSportIconDrawable(R.drawable.sport_daily);
        daily.setSportIconUnSelectedDrawable(R.drawable.sport_daily_grey);

        SportsTab mixed = new SportsTab();
        mixed.setSportsId(NewChallengesFilterHelper.FILTER_ALL_SPORTS_ID);
        mixed.setSportsName("Mixed");
        mixed.setSportIconDrawable(R.drawable.sport_mix);
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
        formulaOne.setSportIconDrawable(R.drawable.sport_racing);
        formulaOne.setSportIconUnSelectedDrawable(R.drawable.sport_racing_grey);

        sportsList.add(all);
        sportsList.add(daily);
        sportsList.add(mixed);
        sportsList.add(cricket);
        sportsList.add(basketBall);
        sportsList.add(football);
        sportsList.add(tennis);
        sportsList.add(kabbadi);
        sportsList.add(badminton);
        sportsList.add(hockey);
        sportsList.add(formulaOne);

        return sportsList;
    }
}
