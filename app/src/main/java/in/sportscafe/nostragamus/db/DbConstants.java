package in.sportscafe.nostragamus.db;

/**
 * Created by sandip on 23/08/17.
 */

public class DbConstants {

    public interface Database {

        /*** Database name - should not modify */
        String DB_NAME = "Nostragamus.db";

        /*** Database version - can be upgraded each time when DB has been modified
         * e.g. Tables schema modified, tables added, updated, altered .... */

        int DB_VERSION = 1;
    }

    public interface TableIds {
        int API_CACHE_TABLE = 1;
    }

    public static final String ON_DELETE_CASCADE_STR = "ON DELETE CASCADE";
    public static final String ON_UPDATE_CASCADE_STR = "ON UPDATE CASCADE";
}
