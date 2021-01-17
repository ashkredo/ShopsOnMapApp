package com.example.shopsonmapapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ShopDBHelper extends SQLiteOpenHelper {

    private static final String db_name = "MyDBShop.db";
    private static final int db_version = 1;
    public static final String table_name = "Shop";

    public ShopDBHelper(Context context) {
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String s = "CREATE TABLE " + table_name + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, LATITUDE REAL, LONGITUDE REAL)";
        db.execSQL(s);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String s = "DROP TABLE IF EXISTS " + table_name;
        db.execSQL(s);
        onCreate(db);
    }
}
