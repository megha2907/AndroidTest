package in.sportscafe.nostragamus.db.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jeeva.android.Log;

import java.sql.SQLException;
import java.util.ArrayList;

import in.sportscafe.nostragamus.db.tableDto.ApiCacheDbDto;

/**
 * Created by sandip on 23/08/17.
 */

public class ApiCacheDbTable extends BaseTable {

    private static final String TAG = ApiCacheDbTable.class.getSimpleName();
    public static final String TABLE_NAME = "ApiCacheDbTable";


    public interface TableFields {
        String API_TYPE = "ApiType";
        String API_RESPONSE_CONTENT = "ApiResponseContent";
        String API_SAVED_TIME_STAMP = "savedTimeStamp";
    }

    /**
     * Add all Sql queries here
     *
     */
    private interface SqlQueries {
        String TABLE_CREATE_QUERY = "CREATE TABLE " + TABLE_NAME +
                " ( " +
                TableFields.API_TYPE + " INTEGER, " +
                TableFields.API_RESPONSE_CONTENT + " TEXT, " +
                TableFields.API_SAVED_TIME_STAMP + " LONG " +
                " ); ";

    }

    public ApiCacheDbTable() {
    }

    @Override
    public void create(SQLiteDatabase database) throws SQLException {
        database.execSQL(SqlQueries.TABLE_CREATE_QUERY);
        Log.i(TAG, TABLE_NAME + " created ...");
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion) throws SQLException {
        // Upgrade table
        Log.i(TAG, TABLE_NAME + " upgraded ...");
    }

    @Override
    public long insert(SQLiteDatabase db, Object object) throws SQLException {

        int updateCount = 0, insertCount = 0;
        try {
            ApiCacheDbDto apiCacheDbDto = (ApiCacheDbDto) object;
            if (apiCacheDbDto != null) {
                db.beginTransaction();

                if (isRecordExistInDb(apiCacheDbDto.getApiCacheType(), getApiCachedFromDb(db))) {

                    String whereClause = TableFields.API_TYPE + "=?";
                    String[] whereArgs = { String.valueOf(apiCacheDbDto.getApiCacheType()) };
                    int res = update(db, apiCacheDbDto, whereClause, whereArgs);
                    if (res != -1) {
                        updateCount++;
                    }

                } else {
                    long result = db.insert(TABLE_NAME, null, getContentValue(apiCacheDbDto));
                    if (result != -1) {
                        insertCount++;
                    }
                }

                db.setTransactionSuccessful();
                db.endTransaction();

                Log.d(TAG, String.format("API Cached : \nRecords Inserted : %1$d \n Records Updated : %2$d", insertCount, updateCount));
            }
        } catch (Exception ex) {
            Log.e(TAG, "Api caching error");
            ex.printStackTrace();
        }

        return 0;
    }

    private ContentValues getContentValue(ApiCacheDbDto apiCacheDbDto) {
        ContentValues contentValues = null;
        if (apiCacheDbDto != null) {
            contentValues = new ContentValues();
            contentValues.put(TableFields.API_TYPE, apiCacheDbDto.getApiCacheType());
            contentValues.put(TableFields.API_RESPONSE_CONTENT, apiCacheDbDto.getApiContent());
            contentValues.put(TableFields.API_SAVED_TIME_STAMP, apiCacheDbDto.getTimeStamp());
        }
        return contentValues;
    }

    private boolean isRecordExistInDb(int apiCacheType, ArrayList<ApiCacheDbDto> apiCacheDbDtoArrayList) {
        boolean isAvailable = false;
        if (apiCacheDbDtoArrayList != null && !apiCacheDbDtoArrayList.isEmpty()) {
            for (ApiCacheDbDto dbDto : apiCacheDbDtoArrayList) {
                if (dbDto.getApiCacheType() == apiCacheType) {
                    isAvailable = true;
                    break;
                }
            }
        }
        return isAvailable;
    }

    private ArrayList<ApiCacheDbDto> getApiCachedFromDb(SQLiteDatabase db) {
        ArrayList<ApiCacheDbDto> dbList = null;

        try {
            dbList = (ArrayList<ApiCacheDbDto>) select(db, null, null, null, null, null, null, null);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return dbList;
    }

    @Override
    public int update(SQLiteDatabase db, Object object, String whereClause, String[] whereArgs) throws SQLException {
        int result = -1;
        if (db != null) {
            result = db.update(TABLE_NAME, getContentValue((ApiCacheDbDto) object), whereClause, whereArgs);
        }
        return result;
    }

    @Override
    public int delete(SQLiteDatabase db, String whereClause, String[] whereArgs) throws SQLException {
        int result = -1;
        if (db != null) {
            result = db.delete(TABLE_NAME, whereClause, whereArgs);
        }
        return result;
    }

    @Override
    public Object select(SQLiteDatabase db, String[] columns, String whereClause, String[] whereArgs,
                         String groupBy, String having, String orderBy, String limit) throws SQLException {
        Object result = null;

        if (db != null) {
            Cursor cursor = db.query(TABLE_NAME, columns, whereClause,
                    whereArgs, groupBy, having, orderBy, limit);

            result = getApiCachedRecords(cursor);
        }
        return result;
    }

    private Object getApiCachedRecords(Cursor cursor) {
        ArrayList<ApiCacheDbDto> cacheDbDtos = null;

        if (cursor != null) {
            try {
                cacheDbDtos = new ArrayList<>();
                ApiCacheDbDto dbDto;

                while (cursor.moveToNext()) {
                    dbDto = new ApiCacheDbDto();
                    dbDto.setApiCacheType(cursor.getInt(cursor.getColumnIndex(TableFields.API_TYPE)));
                    dbDto.setApiContent(cursor.getString(cursor.getColumnIndex(TableFields.API_RESPONSE_CONTENT)));
                    dbDto.setTimeStamp(cursor.getLong(cursor.getColumnIndex(TableFields.API_SAVED_TIME_STAMP)));
                    cacheDbDtos.add(dbDto);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                cursor.close();
            }
        }

        return cacheDbDtos;
    }

    @Override
    public Object rawQuery(SQLiteDatabase db, String query, String[] selectionArgs) throws SQLException {
        Object result = null;

        if (db != null) {
            Cursor cursor = db.rawQuery(query, selectionArgs);
            result = getApiCachedRecords(cursor);

        }
        return result;
    }
}
