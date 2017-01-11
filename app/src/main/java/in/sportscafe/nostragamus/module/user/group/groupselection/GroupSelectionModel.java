package in.sportscafe.nostragamus.module.user.group.groupselection;

import android.content.Context;
import android.os.Bundle;

import java.util.List;

import in.sportscafe.nostragamus.module.tournamentFeed.dto.TournamentFeedInfo;
import in.sportscafe.nostragamus.module.user.group.newgroup.GrpTournamentSelectedAdapter;
import in.sportscafe.nostragamus.module.user.group.newgroup.GrpTournamentSelectionAdapter;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;

/**
 * Created by deepanshi on 1/6/17.
 */

public interface GroupSelectionModel {

    void init(long groupId);

    void updateGroupMembers();

    boolean amAdmin();

    void updateTournaments();

    GroupInfo getGroupInfo();

    GrpTournamentSelectionAdapter getSelectedAdapter(Context context, List<TournamentFeedInfo> tournamentInfos);

    GrpTournamentSelectionAdapter getUnSelectedAdapter(Context context, List<TournamentFeedInfo> tournamentInfos);

    void refreshGroupInfo();

    void getAllTournamentsfromServer();
}