package com.softwareproject.chandy.teller.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Chandy on 2016/11/25.
 */

public class DBHelper extends SQLiteOpenHelper {
    private final String TAG = "DBHelper";
    private String createTable ;
    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "record3.db";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public DBHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
        createTable = "create table History (date string primary key,totaltime integer)";
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createTable);
        Log.d("SQL:","database has create");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
