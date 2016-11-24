package in.sportscafe.scgame.module.user.myprofile;

/**
 * Created by Jeeva on 14/6/16.
 */
public interface ProfilePresenter {

    void onCreateProfile();

    void onGetSportsSelectionResult();

    void onGetUpdatedNumberofGroups();

    void onGetGroupCount();

    void onGetPowerUpsCount();

    void onGetBadgesCount();

    void onEditProfileDone();

    void onGroupDetailsUpdated();

    void onClickLogout();
}