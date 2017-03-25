package in.sportscafe.nostragamus.module.user.group.newgroup;

import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.util.List;

import in.sportscafe.nostragamus.module.user.myprofile.dto.TournamentFeedInfo;


/**
 * Created by Jeeva on 1/7/16.
 */
public interface NewGroupModel {

    TourSelectionAdapter getAdapter(Context context, List<TournamentFeedInfo> tourList);

    void createGroup(String groupName);

    void updateGroupPhoto(File file, String filepath, String filename);

    void onGetImage(Intent data);

    void getAllTournamentsfromServer();
}