package com.example.android.inventoryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_TEXT1 = "caller";
    private static final String previous1 = "MainActivityButton";
    ArrayList<Product> productsList = new ArrayList<Product>();
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProductDBHelper dbHelper = new ProductDBHelper(this);
        productsList = dbHelper.getAllProductDetails();
        if (productsList.size() == 0) {
            TextView mainTextView = (TextView) findViewById(R.id.main_textView);
            mainTextView.setText("No products existed, Please add a new product by clicking \'ADD NEW PRODUCT\' button below");
            mainTextView.setVisibility(View.VISIBLE);
        }
        adapter = new ProductAdapter(this, productsList);
        adapter.notifyDataSetChanged();
        ListView listView = (ListView) findViewById(R.id.products_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product currentProduct = (Product) adapter.getItem(position);
                String codeValue = String.valueOf(currentProduct.getProductCode());
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(EXTRA_TEXT1, codeValue);
                startActivity(intent);
            }
        });
    }

    public void addNewProduct(View view) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(EXTRA_TEXT1, previous1);
        startActivity(intent);
    }

    public void sellProduct(View v) {
        View parentRow = (View) v.getParent();
        ListView listView1 = (ListView) parentRow.getParent();
        final int position = listView1.getPositionForView(parentRow);
        Product currentProduct = (Product) adapter.getItem(position);
        int newQuantity = currentProduct.getProductQuantity() - 1;
        if (newQuantity >= 0) {
            ProductDBHelper dbHelper = new ProductDBHelper(getApplicationContext());
            dbHelper.updateQuantity(currentProduct.getProductCode(), newQuantity);
            String newQuantityValue = String.valueOf(newQuantity);
            TextView prodQuantityTextView = (TextView) findViewById(R.id.prod_quantity);
            prodQuantityTextView.setText(String.valueOf(currentProduct.getProductQuantity()));
            Toast.makeText(getApplicationContext(), "One Product sold of this category", Toast.LENGTH_SHORT).show();
            Intent refresh = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(refresh);

        } else {

            Toast.makeText(getApplicationContext(), "Sorry, Out of stock", Toast.LENGTH_SHORT).show();
        }
    }

    // Below is for exit from the app when back button pressed on Main screen
    //Resource is from stackoverflow
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("no", null).show();
    }
}
