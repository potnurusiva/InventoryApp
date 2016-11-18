package com.example.android.inventoryapp;

import android.provider.BaseColumns;

/**
 * Created by PotnuruSiva on 23-07-2016.
 */
public class ProductContract {

    public ProductContract() {
    }

    public static final class ProductItems implements BaseColumns {
        public static final String TABLE_NAME = "productdetailstable";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_IMAGE = "image";

    }
}
