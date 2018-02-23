package in.sportscafe.nostragamus.module.navigation.help.howtoplay.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepanshi on 2/22/18.
 */

@Parcel
public class HowToPlayDetails {

    @SerializedName("slides")
    private List<HowToPlay> howToPlayList = new ArrayList<>();

    public List<HowToPlay> getHowToPlayList() {
        return howToPlayList;
    }

    public void setHowToPlayList(List<HowToPlay> howToPlayList) {
        this.howToPlayList = howToPlayList;
    }
}
