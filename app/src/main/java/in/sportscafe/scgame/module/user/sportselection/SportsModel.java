package in.sportscafe.scgame.module.user.sportselection;

import java.util.List;

import in.sportscafe.scgame.module.user.sportselection.dto.Sport;

/**
 * Created by Jeeva on 27/5/16.
 */
public interface SportsModel {

    void getAllSportsFromServer();

    List<Sport> getAllSportsFromLocal();
}