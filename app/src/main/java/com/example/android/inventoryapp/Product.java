package com.example.android.inventoryapp;

/**
 * Created by PotnuruSiva on 23-07-2016.
 */
public class Product {
    private int productCode;
    private String productName;
    private int productQuantity;
    private float productPrice;
    private byte[] image;

    public Product(int productCode, String productName, int productQuantity, float productPrice, byte[] image) {
        this.productCode = productCode;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.image = image;
    }

    public int getProductCode() {
        return productCode;
    }

    public String getProductName() {
        return productName;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public byte[] getImage() {
        return image;
    }
}
