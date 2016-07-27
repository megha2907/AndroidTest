package in.sportscafe.scgame.module.feed.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 24/6/16.
 */
public class Feed {

    private long date;

    private List<Tournament> tournaments = new ArrayList<>();

    public Feed() {
    }

    public Feed(long date) {
        this.date = date;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }

    public void addTournament(Tournament tournament) {
        tournaments.add(tournament);
    }
}