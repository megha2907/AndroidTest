package in.sportscafe.nostragamus.db;

/**
 * Created by sandip on 23/08/17.
 */

public interface ApiCacheType {

    /**
     * Used for API caching while operation in table to identify multiple apis (If are being saved)
     */
    int NEW_CHALLENGE_API = 11;
    int COMPLETED_CHALLENGE_API = 12;
    int IN_PLAY_API = 13;


}
