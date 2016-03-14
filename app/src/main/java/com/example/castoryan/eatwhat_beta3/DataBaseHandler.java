package com.example.castoryan.eatwhat_beta3;

/**
 * Created by bohou on 2015/1/4.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "eatwhat1.db";

    //  table name
    private static final String TABLE_RESTAURANTS = "resturant";

    //  Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PHONENUMBER = "tele_num";
    private static final String KEY_MYGRADE = "mygrade";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_LATITUTE = "latitute";
    private static final String KEY_LONGTITUE = "longtitue";


    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_RESTAURANTS_TABLE = "CREATE TABLE " + TABLE_RESTAURANTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " VARCHAR," + KEY_ADDRESS + " VARCHAR,"+ KEY_PHONENUMBER + " VARCHAR,"
                + KEY_MYGRADE + " VARCHAR, " + KEY_IMAGE + " BLOB," + KEY_LATITUTE + " REAL,"+ KEY_LONGTITUE + " REAL"+ ")";
        db.execSQL(CREATE_RESTAURANTS_TABLE);
       // db.execSQL("Create table restaurants ( resID INTEGER PRIMARY KEY AUTOINCREMENT,name varchar(100),address varchar,address varchar,image BLOB );");

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public long addRestaurant(Resturant resturant) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, resturant.name);
        values.put(KEY_ADDRESS, resturant.address);
        values.put(KEY_PHONENUMBER, resturant.tele_num);
        values.put(KEY_IMAGE, resturant.image);
        values.put(KEY_MYGRADE, resturant.grade_my);
        values.put(KEY_LATITUTE,resturant.latitute);
        values.put(KEY_LONGTITUE,resturant.longtitue);

        // Returns the row ID of the newly inserted row, or -1 if an error occurred
        long row = db.insert(TABLE_RESTAURANTS, null, values);
        db.close();
        return row;
    }
    // Getting single Resturant by id
    public Resturant getResturantById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String com = "SELECT * FROM "+ TABLE_RESTAURANTS+ " WHERE _id ="+(id);
        Cursor cursor = db.rawQuery(com, null);
        if (cursor != null){
            cursor.moveToFirst();
        }

        int _id = cursor.getInt(0);
        String name = cursor.getString(1);
        String address = cursor.getString(2);
        String number = cursor.getString(3);
        int grade = cursor.getInt(4);
        String image = cursor.getString(5);
        double lat = cursor.getDouble(6);
        double lon = cursor.getDouble(7);
        Resturant res = new Resturant(_id,name,address,number,grade,image,lat,lon);
        // return contact
        return res;
    }

    // Getting single Resturant by id
    public Resturant getResturantByName(String _name) {
        SQLiteDatabase db = this.getReadableDatabase();

        String com = "SELECT * FROM "+ TABLE_RESTAURANTS+ " WHERE "+KEY_NAME + " = '"+_name+"'";
        Cursor cursor = db.rawQuery(com, null);
        if (cursor != null){
            cursor.moveToFirst();
        }

        int _id = cursor.getInt(0);
        String name = cursor.getString(1);
        String address = cursor.getString(2);
        String number = cursor.getString(3);
        int grade = cursor.getInt(4);
        String image = cursor.getString(5);
        double lat = cursor.getDouble(6);
        double lon = cursor.getDouble(7);
        Resturant res = new Resturant(_id,name,address,number,grade,image,lat,lon);
        // return contact
        return res;
    }
//
    // Getting All Resturant
    public List<Resturant> getAllResturants() {
        List<Resturant> resList = new ArrayList<Resturant>();
        // Select All Query
        String selectQuery = "SELECT * FROM resturant";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        //cursor.moveToFirst();
        while(cursor.moveToNext()){
            Resturant res = new Resturant();
            res._id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            res.name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            res.address = cursor.getString(cursor.getColumnIndex(KEY_ADDRESS));
            res.tele_num = cursor.getString(cursor.getColumnIndex(KEY_PHONENUMBER));
            res.grade_my = cursor.getInt(cursor.getColumnIndex(KEY_MYGRADE));
            res.image = cursor.getString(cursor.getColumnIndex(KEY_IMAGE));
            res.latitute = cursor.getDouble(cursor.getColumnIndex(KEY_LATITUTE));
            res.longtitue = cursor.getDouble(cursor.getColumnIndex(KEY_LONGTITUE));
            resList.add(res);
        }
        // close inserting data from database
        db.close();
        // return contact list
        return resList;
    }


    // Getting All Resturants' name
    public List<String> getAllResturantsString() {
        List<String> resList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT * FROM resturant";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        while(cursor.moveToNext()){
            Resturant res = new Resturant();
            res._id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            res.name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            String st = res.name;
            resList.add(st);
        }
        // close inserting data from database
        db.close();
        // return resturant list
        return resList;
    }


    // Updating single restaurant
   public void updateRestaurantImage(String image, String name) {
       SQLiteDatabase db = this.getWritableDatabase();
       String com = "UPDATE "+ TABLE_RESTAURANTS +" SET "+KEY_IMAGE+" = '"+image+"' WHERE "+ KEY_NAME +" = '"+ name+"'";
       db.execSQL(com);
    }

    // Deleting single contact
    public void deleteResturant(Resturant res) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RESTAURANTS, KEY_ID + " = ?",
                new String[] { String.valueOf(res.getID()) });
        db.close();
    }

    // Deleting single contact
    public void deleteResturantById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RESTAURANTS, KEY_ID + " = ?",
                new String[] { String.valueOf(id)});
        db.close();
    }

    // Deleting single contact
    public void deleteResturantByName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_RESTAURANTS, KEY_NAME + " = ?",new String[] {name});
        db.close();
    }

//    // Getting contacts Count
    public int getResturantCount() {
        String countQuery = "SELECT  * FROM " + TABLE_RESTAURANTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int k = cursor.getCount();
        cursor.close();

        // return count
        return k;
    }

}

