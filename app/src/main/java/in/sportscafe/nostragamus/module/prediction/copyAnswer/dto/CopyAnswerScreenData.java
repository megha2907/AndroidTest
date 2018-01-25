package in.sportscafe.nostragamus.module.prediction.copyAnswer.dto;

import org.parceler.Parcel;

import in.sportscafe.nostragamus.module.inPlay.dto.InPlayMatch;
import in.sportscafe.nostragamus.module.prediction.playScreen.dto.PlayScreenDataDto;

/**
 * Created by sc on 23/1/18.
 */
@Parcel
public class CopyAnswerScreenData {

    private PlayScreenDataDto playScreenDataDto;
    private InPlayMatch inPlayMatch;


    public PlayScreenDataDto getPlayScreenDataDto() {
        return playScreenDataDto;
    }

    public void setPlayScreenDataDto(PlayScreenDataDto playScreenDataDto) {
        this.playScreenDataDto = playScreenDataDto;
    }

    public InPlayMatch getInPlayMatch() {
        return inPlayMatch;
    }

    public void setInPlayMatch(InPlayMatch inPlayMatch) {
        this.inPlayMatch = inPlayMatch;
    }
}
