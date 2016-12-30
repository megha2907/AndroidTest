package in.sportscafe.nostragamus.module.user.group.newgroup;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Jeeva on 1/7/16.
 */
public interface NewGroupPresenter {

    void onCreateNewGroup();

    void onClickDone(String groupName);

    void onGroupPhotoDone(File body, String filepath, String filename);
}