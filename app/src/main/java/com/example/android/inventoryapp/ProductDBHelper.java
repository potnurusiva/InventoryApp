package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by PotnuruSiva on 23-07-2016.
 */
public class ProductDBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + ProductContract.ProductItems.TABLE_NAME +  " (" +
                                                      ProductContract.ProductItems._ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                      ProductContract.ProductItems.COLUMN_NAME + " TEXT NOT NULL, "+
                                                      ProductContract.ProductItems.COLUMN_QUANTITY + " INTEGER NOT NULL, "+
                                                      ProductContract.ProductItems.COLUMN_PRICE + " REAL NOT NULL" +
                                                     ");";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ProductContract.ProductItems.TABLE_NAME;

    public ProductDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
    }

    public boolean addNewProduct(String name, int quantity, float price) {
        Log.v("ProductDBHelper","addnewproduct");
        SQLiteDatabase db = this.getWritableDatabase();
        //Inserting Data to Habits
        //Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductItems.COLUMN_NAME, name);
        values.put(ProductContract.ProductItems.COLUMN_QUANTITY, quantity);
        values.put(ProductContract.ProductItems.COLUMN_PRICE, price);
        Log.v("ProductDBHelper","addnewproduct before insert");
        db.insert(ProductContract.ProductItems.TABLE_NAME, null, values);
        Log.v("ProductDBHelper","addnewproduct after insert");
        return true;
    }

    public ArrayList<Product> getAllProductDetails() {
        Log.v("ProductDBHelper","getAllProductDetails");
        ArrayList<Product> productsList = new ArrayList<Product>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("select * from " + ProductContract.ProductItems.TABLE_NAME, null);
        data.moveToFirst();
        //if(data == null){
         //   Toast.makeText(getApplicationContext(),"No Products in database, please add a new product by clciking ADD NEW PRODUCT BUTTON ", Toast.LENGTH_SHORT).show();
        //}
        while (data.isAfterLast() == false){
            productsList.add( new Product(  data.getInt(data.getColumnIndex(ProductContract.ProductItems._ID)),
                                            data.getString(data.getColumnIndex(ProductContract.ProductItems.COLUMN_NAME)),
                                            data.getInt(data.getColumnIndex(ProductContract.ProductItems.COLUMN_QUANTITY)),
                                            data.getFloat(data.getColumnIndex(ProductContract.ProductItems.COLUMN_PRICE))
                                         ));
            data.moveToNext();
        }

        return productsList;

    }

    public Cursor getProductData(int code) {
        Log.v("ProductDBHelper","getProductData code value" + String.valueOf(code));
        //ArrayList<Product> productsList = new ArrayList<Product>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("select * from " + ProductContract.ProductItems.TABLE_NAME + " where "+
                ProductContract.ProductItems._ID + " = " + code + " ;", null);
        data.moveToFirst();
        return data;

    }
    public boolean updateProductDetails(int code, String newName, int newQuantity, float newPrice) {
        Log.v("ProductDBHelper","updateProductDetails");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductItems.COLUMN_NAME, newName);
        values.put(ProductContract.ProductItems.COLUMN_QUANTITY, newQuantity);
        values.put(ProductContract.ProductItems.COLUMN_PRICE, newPrice);

        String selection = ProductContract.ProductItems._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(code)};

        db.update(ProductContract.ProductItems.TABLE_NAME, values, selection, selectionArgs);
        return true;
    }

    public boolean updateQuantity(int code, int newQuantity) {
        Log.v("ProductDBHelper","updateProductDetails");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductItems.COLUMN_QUANTITY, newQuantity);

        String selection = ProductContract.ProductItems._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(code)};

        db.update(ProductContract.ProductItems.TABLE_NAME, values, selection, selectionArgs);
        return true;
    }

    public boolean deleteProductDetails(int code) {
        Log.v("ProductDBHelper","deleteProductDetails");
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = ProductContract.ProductItems._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(code)};
        db.delete(ProductContract.ProductItems.TABLE_NAME, selection, selectionArgs);
       return true;
    }
}
