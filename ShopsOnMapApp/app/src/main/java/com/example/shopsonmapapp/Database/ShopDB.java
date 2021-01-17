package com.example.shopsonmapapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import com.example.shopsonmapapp.Models.Shop;
import java.util.ArrayList;
import java.util.List;

public class ShopDB {
    private SQLiteOpenHelper dbhandler;
    private SQLiteDatabase database;
    private Context context;

    private static final String s_id = "ID";
    private static final String s_name = "NAME";
    private static final String s_latitude = "LATITUDE";
    private static final String s_longitude = "LONGITUDE";

    private static final String[] allColumns = {
            s_id,
            s_name,
            s_latitude,
            s_longitude
    };

    public ShopDB(Context context){
        dbhandler = new ShopDBHelper(context);
        this.context = context;
    }

    public void open(){
        database = dbhandler.getWritableDatabase();
    }

    public void close(){
        dbhandler.close();
    }

    public Shop addShop(Shop shop){
        database = dbhandler.getWritableDatabase();
        ContentValues values  = new ContentValues();
        values.put(s_name, shop.getName());
        values.put(s_latitude, shop.getLatitude());
        values.put(s_longitude, shop.getLongitude());
        long insertid = database.insert(ShopDBHelper.table_name,null,values);
        shop.setID(insertid);
        dbhandler.close();
        return shop;
    }

    public Shop getShop(long id) {
        database = dbhandler.getReadableDatabase();
        Cursor cursor = database.query(ShopDBHelper.table_name, allColumns,s_id + "=?",new String[]{String.valueOf(id)},null,null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Shop shop = new Shop((int)Long.parseLong(cursor.getString(0)), cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3));
        dbhandler.close();
        return shop;
    }

    public List<Shop> getAllShops() {
        database = dbhandler.getReadableDatabase();
        Cursor cursor = database.query(ShopDBHelper.table_name,allColumns,null,null,null, null, null);

        List<Shop> shops = new ArrayList<>();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Shop shop = new Shop();
                shop.setID(cursor.getLong(cursor.getColumnIndex(s_id)));
                shop.setName(cursor.getString(cursor.getColumnIndex(s_name)));
                shop.setLatitude(cursor.getDouble(cursor.getColumnIndex(s_latitude)));
                shop.setLongitude(cursor.getDouble(cursor.getColumnIndex(s_longitude)));
                shops.add(shop);
            }
        }
        dbhandler.close();
        return shops;
    }

    public int updateShop(Shop shop) {

        database = dbhandler.getWritableDatabase();
        ContentValues values  = new ContentValues();
        values.put(s_name, shop.getName());
        values.put(s_latitude, shop.getLatitude());
        values.put(s_longitude, shop.getLongitude());
        dbhandler.close();

        return database.update(ShopDBHelper.table_name, values,
                s_id + "=?",new String[] { String.valueOf(shop.getID())});
    }

    public void deleteShop(Shop shop) {
        database = dbhandler.getWritableDatabase();
        database.delete(ShopDBHelper.table_name, s_id + "=" + shop.getID(), null);
    }

    public void message(String s){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
