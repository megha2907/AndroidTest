package in.sportscafe.scgame.module.user.group.newgroup;

import android.content.Context;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Jeeva on 1/7/16.
 */
public interface NewGroupModel {

    GrpTournamentSelectionAdapter getAdapter(Context context);

    void createGroup(String groupName);

    void updateGroupPhoto(MultipartBody.Part file, RequestBody filepath, RequestBody filename);
}