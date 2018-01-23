package in.sportscafe.nostragamus.db.tableDto;

/**
 * Created by sc on 8/1/18.
 */

public class ServerTimeDbDto {

    private long serverTime;
    private long systemElapsedRealTime;

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public long getSystemElapsedRealTime() {
        return systemElapsedRealTime;
    }

    public void setSystemElapsedRealTime(long systemElapsedRealTime) {
        this.systemElapsedRealTime = systemElapsedRealTime;
    }
}
