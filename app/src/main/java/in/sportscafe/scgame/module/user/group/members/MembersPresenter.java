package in.sportscafe.scgame.module.user.group.members;

import in.sportscafe.scgame.module.user.myprofile.dto.GroupPerson;

/**
 * Created by Jeeva on 2/7/16.
 */
public interface MembersPresenter {

    void onCreateMembers(long groupId);

    void onClickLeaveGroup();

    void onGetNewPerson(GroupPerson newPerson);
}