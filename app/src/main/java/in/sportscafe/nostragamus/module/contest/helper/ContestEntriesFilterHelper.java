package in.sportscafe.nostragamus.module.contest.helper;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.contest.dto.ContestRoomDto;

/**
 * Created by sandip on 07/09/17.
 */

public class ContestEntriesFilterHelper {

    public List<ContestRoomDto> getFillingRoomsFileterredList(List<ContestRoomDto> rooms) {
        List<ContestRoomDto> fillingContest = null;

        if (rooms != null && rooms.size() > 0) {
            fillingContest = new ArrayList<>();

            for (ContestRoomDto roomDto : rooms) {
                if (roomDto.getRoomStatus().equalsIgnoreCase("Filling")) {
                    fillingContest.add(roomDto);
                }
            }
        }

        return fillingContest;
    }

    public List<ContestRoomDto> getFilledRoomsFileterredList(List<ContestRoomDto> rooms) {
        List<ContestRoomDto> filledContest = null;

        if (rooms != null && rooms.size() > 0) {
            filledContest = new ArrayList<>();

            for (ContestRoomDto roomDto : rooms) {
                if (roomDto.getRoomStatus().equalsIgnoreCase("Filled")) {
                    filledContest.add(roomDto);
                }
            }
        }

        return filledContest;
    }
}
