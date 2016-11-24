package in.sportscafe.scgame.module.user.points;

import java.util.List;

import in.sportscafe.scgame.module.user.leaderboard.dto.LeaderBoard;

public interface OnLeaderBoardUpdateListener {

    void updateLeaderBoard(List<LeaderBoard> leaderBoardList);
}