package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by PotnuruSiva on 23-07-2016.
 */
public class ProductDBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "inventorystock.db";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + ProductContract.ProductItems.TABLE_NAME + " (" +
            ProductContract.ProductItems._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ProductContract.ProductItems.COLUMN_NAME + " TEXT NOT NULL, " +
            ProductContract.ProductItems.COLUMN_QUANTITY + " INTEGER NOT NULL, " +
            ProductContract.ProductItems.COLUMN_PRICE + " REAL NOT NULL, " +
            ProductContract.ProductItems.COLUMN_IMAGE + " BLOB NOT NULL" +
            ");";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ProductContract.ProductItems.TABLE_NAME;

    public ProductDBHelper(Context context) {
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

    public boolean addNewProduct(String name, int quantity, float price, byte[] imageBytes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductItems.COLUMN_NAME, name);
        values.put(ProductContract.ProductItems.COLUMN_QUANTITY, quantity);
        values.put(ProductContract.ProductItems.COLUMN_PRICE, price);
        values.put(ProductContract.ProductItems.COLUMN_IMAGE, imageBytes);
        db.insert(ProductContract.ProductItems.TABLE_NAME, null, values);
        return true;
    }

    public ArrayList<Product> getAllProductDetails() {
        ArrayList<Product> productsList = new ArrayList<Product>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data;
        data = db.rawQuery("select * from " + ProductContract.ProductItems.TABLE_NAME, null);
        data.moveToFirst();

        while (data.isAfterLast() == false) {
            productsList.add(new Product(data.getInt(data.getColumnIndexOrThrow(ProductContract.ProductItems._ID)),
                    data.getString(data.getColumnIndexOrThrow(ProductContract.ProductItems.COLUMN_NAME)),
                    data.getInt(data.getColumnIndexOrThrow(ProductContract.ProductItems.COLUMN_QUANTITY)),
                    data.getFloat(data.getColumnIndexOrThrow(ProductContract.ProductItems.COLUMN_PRICE)),
                    data.getBlob(data.getColumnIndexOrThrow(ProductContract.ProductItems.COLUMN_IMAGE))
            ));
            data.moveToNext();
        }
        db.close();
        return productsList;

    }

    public Cursor getProductData(int code) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("select * from " + ProductContract.ProductItems.TABLE_NAME + " where " +
                ProductContract.ProductItems._ID + " = " + code + " ;", null);
        data.moveToFirst();
        return data;

    }

    public boolean updateProductDetails(int code, String newName, int newQuantity, float newPrice, byte[] imageBytes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductItems.COLUMN_NAME, newName);
        values.put(ProductContract.ProductItems.COLUMN_QUANTITY, newQuantity);
        values.put(ProductContract.ProductItems.COLUMN_PRICE, newPrice);
        values.put(ProductContract.ProductItems.COLUMN_IMAGE, imageBytes);

        String selection = ProductContract.ProductItems._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(code)};

        db.update(ProductContract.ProductItems.TABLE_NAME, values, selection, selectionArgs);
        return true;
    }

    public boolean updateQuantity(int code, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductItems.COLUMN_QUANTITY, newQuantity);

        String selection = ProductContract.ProductItems._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(code)};

        db.update(ProductContract.ProductItems.TABLE_NAME, values, selection, selectionArgs);
        return true;
    }

    public boolean deleteProductDetails(int code) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = ProductContract.ProductItems._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(code)};
        db.delete(ProductContract.ProductItems.TABLE_NAME, selection, selectionArgs);
        return true;
    }
}
