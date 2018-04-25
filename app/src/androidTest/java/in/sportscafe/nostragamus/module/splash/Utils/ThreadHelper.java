package in.sportscafe.nostragamus.module.splash.Utils;

/**
 * Created by sc on 24/4/18.
 */

public class ThreadHelper {

    public static void pauseMainThread(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
