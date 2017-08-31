package in.sportscafe.nostragamus.module.newChallenges.dataProvider;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.R;
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
        all.setSportIconDrawable(R.drawable.profile_sport_icon);

        SportsTab daily = new SportsTab();
        daily.setSportsId(NewChallengesFilterHelper.FILTER_ALL_SPORTS_ID);
        daily.setSportsName("Daily");
        daily.setSportIconDrawable(R.drawable.profile_sport_icon);

        SportsTab cricket = new SportsTab();
        cricket.setSportsId(1);
        cricket.setSportsName("Cricket");
        cricket.setSportIconDrawable(R.drawable.profile_sport_icon);

        SportsTab hockey = new SportsTab();
        hockey.setSportsId(2);
        hockey.setSportsName("Hockey");
        hockey.setSportIconDrawable(R.drawable.profile_sport_icon);

        SportsTab tennis = new SportsTab();
        tennis.setSportsId(3);
        tennis.setSportsName("Tennis");
        tennis.setSportIconDrawable(R.drawable.profile_sport_icon);

        SportsTab football = new SportsTab();
        football.setSportsId(4);
        football.setSportsName("Football");
        football.setSportIconDrawable(R.drawable.profile_sport_icon);

        SportsTab badminton = new SportsTab();
        badminton.setSportsId(6);
        badminton.setSportsName("Badminton");
        badminton.setSportIconDrawable(R.drawable.profile_sport_icon);

        SportsTab basketBall = new SportsTab();
        basketBall.setSportsId(8);
        basketBall.setSportsName("Basketball");
        basketBall.setSportIconDrawable(R.drawable.profile_sport_icon);

        SportsTab kabbadi = new SportsTab();
        kabbadi.setSportsId(5);
        kabbadi.setSportsName("Kabbadi");
        kabbadi.setSportIconDrawable(R.drawable.profile_sport_icon);

        SportsTab formulaOne = new SportsTab();
        formulaOne.setSportsId(7);
        formulaOne.setSportsName("Formula One");
        formulaOne.setSportIconDrawable(R.drawable.profile_sport_icon);

        sportsList.add(all);
        sportsList.add(daily);
        sportsList.add(cricket);
        sportsList.add(hockey);
        sportsList.add(tennis);
        sportsList.add(football);
        sportsList.add(badminton);
        sportsList.add(basketBall);
        sportsList.add(kabbadi);
        sportsList.add(formulaOne);

        return sportsList;
    }
}
