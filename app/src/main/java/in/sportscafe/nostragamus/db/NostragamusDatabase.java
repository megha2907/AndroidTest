package in.sportscafe.nostragamus.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jeeva.android.Log;

import java.sql.SQLException;

import in.sportscafe.nostragamus.db.tables.ApiCacheDbTable;
import in.sportscafe.nostragamus.db.tables.BaseTable;

/**
 * Created by sandip on 23/08/17.
 */

public class NostragamusDatabase extends SQLiteOpenHelper {
    private static final String TAG = NostragamusDatabase.class.getSimpleName();

    /** A single static instance for database  */
    private static NostragamusDatabase sDbHelperInstance = null;

    private NostragamusDatabase(Context context) {
        super(context, DbConstants.Database.DB_NAME, null, DbConstants.Database.DB_VERSION);
    }

    /**
     * A singleton instance to manage Database operations through it...
     * @param context - Application Context for DB
     * @return - DB Instance
     */
    public synchronized static NostragamusDatabase getInstance(Context context) {
        if (sDbHelperInstance == null) {
            sDbHelperInstance = new NostragamusDatabase(context);
        }

        return sDbHelperInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        upgradeTables(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON;");
        }
    }

    /**
     * Performs insert operation for any table
     * @param tabletId - Table tobe performed insertion
     * @param object - Values
     * @return - successful row Id
     */
    public synchronized long insert(int tabletId, Object object) {
        long result = -1;

        BaseTable baseTable = getTable(tabletId);
        if (baseTable != null) {
            try {
                result = baseTable.insert(getWritableDatabase(), object);
            } catch (SQLException ex) {
                Log.i(TAG, "Exception while inserting.");
                ex.printStackTrace();
            }

        }

        return result;
    }

    /**
     *  Performs record update into table
     * @param tabletId - table
     * @param object - values to be updated
     * @param whereClause - where (fields) to be updated
     * @param whereArgs - values tobe replaced/updated with mentioned fields
     * @return - rows updated
     */
    public synchronized int update(int tabletId, Object object, String whereClause, String[] whereArgs) {
        int result = -1;

        BaseTable baseTable = getTable(tabletId);
        if (baseTable != null) {

            try {
                result = baseTable.update(getWritableDatabase(), object, whereClause, whereArgs);
            } catch (SQLException ex) {
                Log.i(TAG, "Error while updating ...");
                ex.printStackTrace();
            }

        }

        return result;
    }

    /**
     * Performs delete operation for tables
     * @param tabletId - table
     * @param whereClause - record/where tobe deleted
     * @param whereArgs - values to consider for deletion against its where field
     * @return - rows deleted
     */
    public synchronized int delete(int tabletId, String whereClause, String[] whereArgs) {
        int result = -1;

        BaseTable baseTable = getTable(tabletId);
        if (baseTable != null) {

            try {
                result = baseTable.delete(getWritableDatabase(), whereClause, whereArgs);
            } catch (SQLException ex) {
                Log.i(TAG, "Error while deleting ...");
                ex.printStackTrace();
            }
        }

        return result;
    }

    /**
     * Performs select operation from table
     * @param tabletId - table
     * @param whereClause - fields to consider while selection
     * @param whereArgs - field values to consider while selection
     * @param groupBy - selection result grouped by field
     * @param having - consider selection having value for a field
     * @param orderBy - order to produce result based on fields
     * @param limit - rows to return finally
     * @return - values from table based on criteria mentioned
     */
    public synchronized Object select(int tabletId, String[] columns, String whereClause, String[] whereArgs,
                                      String groupBy, String having, String orderBy, String limit) {
        Object resultValues = null;

        BaseTable baseTable = getTable(tabletId);
        if (baseTable != null) {

            try {
                resultValues = baseTable.select(getReadableDatabase(), columns, whereClause, whereArgs,
                        groupBy, having, orderBy, limit);
            } catch (SQLException ex) {
                Log.i(TAG, "Error while selecting ...");
                ex.printStackTrace();
            }
        }

        return resultValues;
    }

    /**
     * Selects all values from table
     * @param tabletId - Table-id
     * @return
     */
    public synchronized Object select(int tabletId) {
        Object resultValues = null;

        BaseTable baseTable = getTable(tabletId);
        if (baseTable != null) {
            try {
                resultValues = baseTable.select(getReadableDatabase(),
                        null, null, null, null, null, null, null);
            } catch (SQLException ex) {
                Log.i(TAG, "Error while selecting ...");
                ex.printStackTrace();
            }
        }

        return resultValues;
    }

    /**
     *
     * @param tabletId
     * @param whereClause where clause
     * @param whereArgs args to pass for each param as mentioned into whereClause
     * @return
     */
    public synchronized Object select(int tabletId, String whereClause, String[] whereArgs) {
        Object resultValues = null;

        BaseTable baseTable = getTable(tabletId);
        if (baseTable != null) {
            try {
                resultValues = baseTable.select(getReadableDatabase(),
                        null, whereClause, whereArgs, null, null, null, null);
            } catch (SQLException ex) {
                Log.i(TAG, "Error while selecting ...");
                ex.printStackTrace();
            }
        }

        return resultValues;
    }

    /**
     * Performs unstructured query
     * @param tabletId - table
     * @param query - SQL query to execute
     * @param selectionArgs - values to consider for SQL query
     * @return - result from SQL execution
     */
    public synchronized Object rawQuery(int tabletId, String query, String[] selectionArgs) {
        Object resultValues = null;

        BaseTable baseTable = getTable(tabletId);
        if (baseTable != null) {

            try {
                resultValues = baseTable.rawQuery(getReadableDatabase(), query, selectionArgs);
            } catch (SQLException ex) {
                Log.i(TAG, "Error while raw-querying ...");
                ex.printStackTrace();
            }
        }

        return resultValues;
    }

    /*
    ==============================================
     */

    /**
     *
     * @param tableId - table to be returned
     * @return DbTable instance to perform table operations
     */
    public synchronized BaseTable getTable(int tableId) {
        BaseTable table = null;

        switch (tableId) {
            case DbConstants.TableIds.API_CACHE_TABLE:
                table = new ApiCacheDbTable();
                break;

            default:
                break;
        }

        return table;
    }

    /**
     * Mention all tables to be created when App is launched first time for user
     * @param db
     */
    private void createTables(SQLiteDatabase db) {
        Log.d(TAG, "Creating DB, current DB Version" + DbConstants.Database.DB_VERSION);

        try {
            getTable(DbConstants.TableIds.API_CACHE_TABLE).create(db);
        } catch (SQLException ex) {
            Log.i(TAG, "Exception while creating Db-Tables.");
            ex.printStackTrace();
        }
    }

    /**
     * Upgrade tables while App Updated if requires,
     * e,g., New tables added, existing table modified for schema etc...
     *
     * Keep Database version upgraded when database updated (while later on App updates)
     *
     * @param db
     * @param oldVersion - older DB version
     * @param newVersion - new DB version
     */
    private void upgradeTables(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, String.format("DB upgrade, oldVersion : %1$s, newVersion : %2$s", oldVersion, newVersion));

        try {
            upgradeDbForLatestVersion(db, oldVersion);
        } catch (SQLException ex) {
            Log.i(TAG, "Exception while upgrading Db-Tables.");
            ex.printStackTrace();
        }
    }

    /**
     * Update database logic for db alteration
     * @param db
     * @param oldVersion
     * @throws SQLException
     */
    private void upgradeDbForLatestVersion(SQLiteDatabase db, int oldVersion) throws SQLException {

    }

}
