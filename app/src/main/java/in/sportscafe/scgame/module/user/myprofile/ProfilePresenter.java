package in.sportscafe.scgame.module.user.myprofile;

/**
 * Created by Jeeva on 14/6/16.
 */
public interface ProfilePresenter {

    void onCreateProfile();

    void onGetSportsSelectionResult();

    void onGetGroupCount();

    void onGetPowerUpsCount();

    void onEditProfileDone();

    void onGroupDetailsUpdated();

    void onClickLogout();
}