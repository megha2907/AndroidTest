package in.sportscafe.nostragamus.module.user.group.newgroup;

import android.content.Context;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Jeeva on 1/7/16.
 */
public interface NewGroupModel {

    GrpTournamentSelectionAdapter getAdapter(Context context);

    void createGroup(String groupName);

    void updateGroupPhoto(File file, String filepath, String filename);
}