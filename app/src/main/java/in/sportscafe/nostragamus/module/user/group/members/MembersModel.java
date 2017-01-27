package in.sportscafe.nostragamus.module.user.group.members;

import android.content.Context;

import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;

/**
 * Created by Jeeva on 2/7/16.
 */
public interface MembersModel {

    MembersAdapter init(Context context, Integer groupId);

    void leaveGroup();

    void addNewPerson(GroupPerson newPerson);
}