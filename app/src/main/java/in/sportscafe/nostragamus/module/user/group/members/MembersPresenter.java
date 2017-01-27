package in.sportscafe.nostragamus.module.user.group.members;

import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;

/**
 * Created by Jeeva on 2/7/16.
 */
public interface MembersPresenter {

    void onCreateMembers(Integer groupId);

    void onClickLeaveGroup();

    void onGetNewPerson(GroupPerson newPerson);
}