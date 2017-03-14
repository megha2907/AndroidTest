package in.sportscafe.nostragamus.module.popups.inapppopups;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 3/11/17.
 */

@Parcel
public class InAppPopup {

    private String title;

    private String body;

    private String btnTitle;

    private String screenName;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBtnTitle() {
        return btnTitle;
    }

    public void setBtnTitle(String btnTitle) {
        this.btnTitle = btnTitle;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

}
