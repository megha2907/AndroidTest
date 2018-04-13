package in.sportscafe.nostragamus.module.recentActivity.announcement;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 3/26/18.
 */
@Parcel
public class AnnouncementScreenData {

    private String announcementTitle;

    private String announcementDesc;

    private String announcementDate;

    public String getAnnouncementTitle() {
        return announcementTitle;
    }

    public void setAnnouncementTitle(String announcementTitle) {
        this.announcementTitle = announcementTitle;
    }

    public String getAnnouncementDesc() {
        return announcementDesc;
    }

    public void setAnnouncementDesc(String announcementDesc) {
        this.announcementDesc = announcementDesc;
    }

    public String getAnnouncementDate() {
        return announcementDate;
    }

    public void setAnnouncementDate(String announcementDate) {
        this.announcementDate = announcementDate;
    }

}

