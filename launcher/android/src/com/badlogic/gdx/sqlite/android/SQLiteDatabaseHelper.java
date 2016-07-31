
package com.badlogic.gdx.sqlite.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import com.badlogic.gdx.files.FileHandle;

/**
 * @author M Rafay Aleem
 */
public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    private final String dbName;
    private final int dbVersion;
    private final String dbOnCreateQuery;
    private final String dbOnUpgradeQuery;

    public SQLiteDatabaseHelper(Context context, FileHandle dbFileHandle, CursorFactory factory, int version, String dbOnCreateQuery,
                                String dbOnUpgradeQuery) {
        super(context, dbFileHandle.file().getAbsolutePath(), factory, version);
        this.dbName = dbFileHandle.file().getAbsolutePath();
        this.dbVersion = version;
        this.dbOnCreateQuery = dbOnCreateQuery;
        this.dbOnUpgradeQuery = dbOnUpgradeQuery;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        if (dbOnCreateQuery != null) database.execSQL(dbOnCreateQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        if (dbOnUpgradeQuery != null) {
            database.execSQL(dbOnUpgradeQuery);
            onCreate(database);
        }
    }

}
