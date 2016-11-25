package in.sportscafe.nostragamus.module.user.sportselection.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import in.sportscafe.nostragamus.R;

/**
 * Created by Jeeva on 27/5/16.
 */
public class Sport implements Serializable {

    @JsonProperty("sports_id")
    private int id;
    private int state;

    @JsonProperty("sports_name")
    private String name;

    public Sport() {
    }

    public Sport(int id) {
        this.id = id;
    }

    @JsonProperty("sports_id")
    public int getId() {
        return id;
    }

    @JsonProperty("sports_id")
    public void setId(int id) {
        this.id = id;
    }

    @JsonIgnore
    public int getImageResource() {
        switch (id) {
            case 6:
                return R.drawable.badminton_menu_icon;
            case 8:
                return R.drawable.basketball_menu_icon;
            case 1:
                return R.drawable.cricket_menu_icon;
            case 4:
                return R.drawable.football_menu_icon;
            case 2:
                return R.drawable.hockey_menu_icon;
            case 5:
                return R.drawable.kabaddi_menu_icon;
            case 7:
                return R.drawable.table_tennis_menu_icon;
            case 3:
                return R.drawable.tennis_menu_icon;
            case 9:
                return R.drawable.frisbee_menu_icon;
            case 10:
                return R.drawable.isb_menu_icon;
            default:
                return R.drawable.ic_launcher;
        }
    }

    @JsonIgnore
    public int getSelectedImageResource() {
        switch (id) {
            case 6:
                return R.drawable.badminton_menu_colored_icon;
            case 8:
                return R.drawable.basketball_menu_colored_icon;
            case 1:
                return R.drawable.cricket_menu_colored_icon;
            case 4:
                return R.drawable.football_menu_colored_icon;
            case 2:
                return R.drawable.hockey_menu_colored_icon;
            case 5:
                return R.drawable.kabaddi_menu_colored_icon;
            case 7:
                return R.drawable.table_tennis_menu_colored_icon;
            case 3:
                return R.drawable.tennis_menu_colored_icon;
            case 9:
                return R.drawable.frisbee_menu_colored_icon;
            case 10:
                return R.drawable.isb_menu_colored_icon;
            default:
                return R.drawable.ic_launcher;
        }
    }

    @JsonIgnore
    public int getState() {
        return state;
    }

    @JsonIgnore
    public void setState(int state) {
        this.state = state;
    }

    @JsonProperty("sports_name")
    public String getName() {
        return name;
    }

    @JsonProperty("sports_name")
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        Sport sport = (Sport) o;
        return id == sport.getId();
    }

    @Override
    public String toString() {
        return name;
    }
}