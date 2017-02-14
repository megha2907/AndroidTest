package in.sportscafe.nostragamus.module.user.group.editgroupinfo;

import com.jeeva.android.View;

/**
 * Created by Jeeva on 12/6/16.
 */
public interface EditGroupInfoView extends View {

    void setGroupName(String groupName);

    void setGroupImage(String imageUrl);

    void navigateToAddPhoto(int addPhotoRequestCode);

    void goBack();
}