package in.sportscafe.nostragamus.db.tableDto;

/**
 * Created by sandip on 23/08/17.
 */

public class ApiCacheDbDto {

    private int apiCacheType;
    private String apiContent;
    private long timeStamp;

    public int getApiCacheType() {
        return apiCacheType;
    }

    public void setApiCacheType(int apiCacheType) {
        this.apiCacheType = apiCacheType;
    }

    public String getApiContent() {
        return apiContent;
    }

    public void setApiContent(String apiContent) {
        this.apiContent = apiContent;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
