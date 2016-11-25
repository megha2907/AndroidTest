package in.sportscafe.nostragamus.module.feed.dto;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.tournamentFeed.dto.Tournament;

/**
 * Created by Jeeva on 24/6/16.
 */
public class Feed {

    private long date;

    private List<Tournament> tournament = new ArrayList<>();

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
        return tournament;
    }

    public void setTournaments(List<Tournament> Tournaments) {
        this.tournament = Tournaments;
    }

    public void addTournament(Tournament Tournament) {
        tournament.add(Tournament);
    }
}