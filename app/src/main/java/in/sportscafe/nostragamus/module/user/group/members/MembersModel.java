package in.sportscafe.nostragamus.module.user.group.members;

import android.content.Context;
import android.os.Bundle;

import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupPerson;

/**
 * Created by Jeeva on 2/7/16.
 */
public interface MembersModel {

    MembersAdapter init(Context context, Bundle bundle);

    void leaveGroup();

    void addNewPerson(GroupPerson newPerson);
}