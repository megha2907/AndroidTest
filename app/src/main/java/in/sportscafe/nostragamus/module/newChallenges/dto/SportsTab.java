package in.sportscafe.nostragamus.module.newChallenges.dto;

/**
 * Created by sandip on 30/08/17.
 */

public class SportsTab {

    private int sportsId;
    private String sportsName;
    private String sportsIconUrl;
    private int sportIconDrawable;
    private int sportIconUnselectedDrawable;
    private int challengeCount;

    public int getSportsId() {
        return sportsId;
    }

    public void setSportsId(int sportsId) {
        this.sportsId = sportsId;
    }

    public String getSportsName() {
        return sportsName;
    }

    public void setSportsName(String sportsName) {
        this.sportsName = sportsName;
    }

    public String getSportsIconUrl() {
        return sportsIconUrl;
    }

    public void setSportsIconUrl(String sportsIconUrl) {
        this.sportsIconUrl = sportsIconUrl;
    }

    public int getSportIconDrawable() {
        return sportIconDrawable;
    }

    public void setSportIconDrawable(int sportIconDrawable) {
        this.sportIconDrawable = sportIconDrawable;
    }

    public int getChallengeCount() {
        return challengeCount;
    }

    public void setChallengeCount(int challengeCount) {
        this.challengeCount = challengeCount;
    }

    public int getSportIconUnSelectedDrawable() {
        return sportIconUnselectedDrawable;
    }

    public void setSportIconUnSelectedDrawable(int sportIconUnselectedDrawable) {
        this.sportIconUnselectedDrawable= sportIconUnselectedDrawable;
    }
}
