package in.sportscafe.nostragamus.utils.timeutils;

public class TimeAgo {

    public long totalDiff;

    public long timeDiff;

    public TimeUnit timeUnit;

    public TimeAgo(long totalDiff, long timeDiff, TimeUnit timeUnit) {
        this.totalDiff = totalDiff;
        this.timeDiff = timeDiff;
        this.timeUnit = timeUnit;
    }
}