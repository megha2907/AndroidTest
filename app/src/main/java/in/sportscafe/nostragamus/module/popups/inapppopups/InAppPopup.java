package in.sportscafe.nostragamus.module.popups.inapppopups;

import android.graphics.Color;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by deepanshi on 3/11/17.
 */

@Parcel
public class InAppPopup {

    private String headingTitle;

    private int headingColor;

    private String btnTitle;

    private int btnColor;

    private boolean showCrossBtn;

    private String openScreen="";

    private List<PopUpBody> popUpBodyList;

    public String getHeadingTitle() {
        return headingTitle;
    }

    public void setHeadingTitle(String headingTitle) {
        this.headingTitle = headingTitle;
    }

    public int getHeadingColor() {
        return headingColor;
    }

    public void setHeadingColor(int headingColor) {
        this.headingColor = headingColor;
    }

    public String getBtnTitle() {
        return btnTitle;
    }

    public void setBtnTitle(String btnTitle) {
        this.btnTitle = btnTitle;
    }

    public int getBtnColor() {
        return btnColor;
    }

    public void setBtnColor(int btnColor) {
        this.btnColor = btnColor;
    }

    public String getOpenScreen() {
        return openScreen;
    }

    public void setOpenScreen(String openScreen) {
        this.openScreen = openScreen;
    }

    public List<PopUpBody> getPopUpBodyList() {
        return popUpBodyList;
    }

    public void setPopUpBodyList(List<PopUpBody> popUpBodyList) {
        this.popUpBodyList = popUpBodyList;
    }

    public boolean isShowCrossBtn() {
        return showCrossBtn;
    }

    public void setShowCrossBtn(boolean showCrossBtn) {
        this.showCrossBtn = showCrossBtn;
    }


}
