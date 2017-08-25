/*
 * Copyright © 2016, Simpal Mobile Payment and Information Services Pvt Ltd.
 *
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package in.sportscafe.nostragamus.db.tables;

import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

public abstract class BaseTable {

    public abstract void create(SQLiteDatabase database)  throws SQLException;

    public abstract void upgrade(SQLiteDatabase db, int oldVersion) throws SQLException;

    public abstract long insert(SQLiteDatabase db, Object object)  throws SQLException;

    public abstract int update(SQLiteDatabase db, Object object, String whereClause, String[] whereArgs)  throws SQLException;

    public abstract int delete(SQLiteDatabase db, String whereClause, String[] whereArgs)  throws SQLException;

    public abstract Object select(SQLiteDatabase db, String[] columns, String whereClause, String[] whereArgs,
                                  String groupBy, String having, String orderBy, String limit)  throws SQLException;

    public abstract Object rawQuery(SQLiteDatabase db, String query, String[] selectionArgs) throws SQLException;
}
