package in.sportscafe.nostragamus.module.popups.inapppopups;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by deepanshi on 3/27/17.
 */
@Parcel
public class PopUpBody {

    private String desc;

    private int icon;

    private boolean showInfoIcon;

    public PopUpBody(String desc, int icon, boolean showInfoIcon) {
        this.desc = desc;
        this.icon = icon;
        this.showInfoIcon = showInfoIcon;
    }

    @ParcelConstructor
    public PopUpBody(String desc,int icon) {
        this.desc = desc;
        this.icon = icon;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isShowInfoIcon() {
        return showInfoIcon;
    }

    public void setShowInfoIcon(boolean showInfoIcon) {
        this.showInfoIcon = showInfoIcon;
    }

}
