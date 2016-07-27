package in.sportscafe.scgame.module.feed.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 24/6/16.
 */
public class Tournament {

    private Integer tournamentId;

    private String tournamentName;

    private List<Match> matches = new ArrayList<>();

    public Tournament() {
    }

    public Tournament(Integer tournamentId, String tournamentName) {
        this.tournamentId = tournamentId;
        this.tournamentName = tournamentName;
    }

    public Integer getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Integer tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public void addMatches(Match match) {
        this.matches.add(match);
    }
}