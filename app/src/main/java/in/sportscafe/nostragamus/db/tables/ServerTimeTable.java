package in.sportscafe.nostragamus.db.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jeeva.android.Log;

import java.sql.SQLException;

import in.sportscafe.nostragamus.db.tableDto.ServerTimeDbDto;

/**
 * Created by sc on 8/1/18.
 *
 * Stores server time & updated record everytime it gets server time..
 * Used for InApp notification , to identify actual servertime even in case of no internet
 */

public class ServerTimeTable extends BaseTable {

    private static final String TAG = ServerTimeTable.class.getSimpleName();
    public static final String TABLE_NAME = "ServerTimeTable";

    public interface TableFields {
        String SERVER_TIME = "serverTime";
        String ELAPSE_REAL_TIME = "elapsedRealTime";
    }

    /**
     * Add all Sql queries here
     *
     *
     */
    private interface SqlQueries {
        String TABLE_CREATE_QUERY = "CREATE TABLE " + TABLE_NAME +
                " ( " +
                ServerTimeTable.TableFields.SERVER_TIME + " LONG, " +
                ServerTimeTable.TableFields.ELAPSE_REAL_TIME + " LONG " +
                " ); ";

    }

    public ServerTimeTable() {}

    @Override
    public void create(SQLiteDatabase database) throws SQLException {
        database.execSQL(SqlQueries.TABLE_CREATE_QUERY);
        Log.i(TAG, TABLE_NAME + " created ...");
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion) throws SQLException {
        Log.i(TAG, TABLE_NAME + " upgraded ...");
    }

    @Override
    public long insert(SQLiteDatabase db, Object object) throws SQLException {
        int deleteCount = 0, insertCount = 0;
        try {
            ServerTimeDbDto serverTimeDbDto = (ServerTimeDbDto) object;
            if (serverTimeDbDto != null) {
                db.beginTransaction();

                /* delete all records if exists */
                deleteCount = delete(db, null, null);
                android.util.Log.i(TAG, "Deleted rows : " + deleteCount);


                long result = db.insert(TABLE_NAME, null, getContentValue(serverTimeDbDto));
                if (result != -1) {
                    insertCount++;
                }

                db.setTransactionSuccessful();
                db.endTransaction();

                Log.d(TAG, String.format("ServerTime Saved : \nRecords Inserted : %1$d \n Records Deleted : %2$d", insertCount, deleteCount));
            }
        } catch (Exception ex) {
            Log.e(TAG, "Server Time DB save error");
            ex.printStackTrace();
        }

        return 0;
    }

    private ContentValues getContentValue(ServerTimeDbDto serverTimeDbDto) {
        ContentValues contentValues = null;
        if (serverTimeDbDto != null) {
            contentValues = new ContentValues();
            contentValues.put(TableFields.SERVER_TIME, serverTimeDbDto.getServerTime());
            contentValues.put(TableFields.ELAPSE_REAL_TIME, serverTimeDbDto.getSystemElapsedRealTime());
        }
        return contentValues;
    }

    @Override
    public int update(SQLiteDatabase db, Object object, String whereClause, String[] whereArgs) throws SQLException {
        return 0;
    }

    @Override
    public int delete(SQLiteDatabase db, String whereClause, String[] whereArgs) throws SQLException {
        return db.delete(TABLE_NAME, whereClause, whereArgs);
    }

    @Override
    public Object select(SQLiteDatabase db, String[] columns, String whereClause, String[] whereArgs,
                         String groupBy, String having, String orderBy, String limit) throws SQLException {
        Object result = null;

        if (db != null) {
            Cursor cursor = db.query(TABLE_NAME, columns, whereClause,
                    whereArgs, groupBy, having, orderBy, limit);

            result = getRecords(cursor);
        }
        return result;
    }

    private Object getRecords(Cursor cursor) {
        ServerTimeDbDto serverTimeDbDto = null;

        if (cursor != null) {
            try {
                serverTimeDbDto = new ServerTimeDbDto();

                cursor.moveToFirst();
                serverTimeDbDto.setServerTime(cursor.getLong(cursor.getColumnIndex(TableFields.SERVER_TIME)));
                serverTimeDbDto.setSystemElapsedRealTime(cursor.getLong(cursor.getColumnIndex(TableFields.ELAPSE_REAL_TIME)));

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                cursor.close();
            }
        }

        return serverTimeDbDto;
    }

    @Override
    public Object rawQuery(SQLiteDatabase db, String query, String[] selectionArgs) throws SQLException {
        return null;
    }
}
