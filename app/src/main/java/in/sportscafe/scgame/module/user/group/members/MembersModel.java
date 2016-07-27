package in.sportscafe.scgame.module.user.group.members;

import android.content.Context;

import in.sportscafe.scgame.module.user.myprofile.dto.GroupPerson;

/**
 * Created by Jeeva on 2/7/16.
 */
public interface MembersModel {

    MembersAdapter init(Context context, long groupId);

    void leaveGroup();

    void addNewPerson(GroupPerson newPerson);
}