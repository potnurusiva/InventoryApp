package com.example.android.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_TEXT1 = "caller";
    private static final String previous1 = "MainActivityButton";
    private static final String previous2 = "MainActivityListItem";
    ArrayList<Product> productsList = new ArrayList<Product>();
    private ProductAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProductDBHelper dbHelper = new ProductDBHelper(this);
        //adapter.clear();
        productsList = dbHelper.getAllProductDetails();
        if (productsList.size() == 0){
            Toast.makeText(this, "No products existed, please add a new product by clicking \'ADD NEW PRODUCT\' BUTTON BELOW",
                                    Toast.LENGTH_LONG).show();
        }
        Log.v("MainActivity","Getting all product details ");
        adapter = new ProductAdapter(this,productsList);
        adapter.notifyDataSetChanged();
        ListView listView =(ListView) findViewById(R.id.products_list);
        listView.setAdapter(adapter);
        Log.v("MainActivity","setonItemClickListener");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("MainActivity","onItemClickListener");
                Product currentProduct = (Product) adapter.getItem(position);
                String codeValue = String.valueOf(currentProduct.getProductCode());
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(EXTRA_TEXT1, codeValue);
                startActivity(intent);
            }
        });
    }

    public void addNewProduct(View view){
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(EXTRA_TEXT1, previous1);
        startActivity(intent);
    }

    public void sellProduct(View v){
        View parentRow = (View) v.getParent();
        ListView listView1 =  (ListView) parentRow.getParent();
        final int position = listView1.getPositionForView(parentRow);
        Product currentProduct = (Product) adapter.getItem(position);
        int newQuantity = currentProduct.getProductQuantity() - 1;
        if(newQuantity >= 0) {
            ProductDBHelper dbHelper = new ProductDBHelper(getApplicationContext());
            dbHelper.updateQuantity(currentProduct.getProductCode(), newQuantity);
            String newQuantityValue = String.valueOf(newQuantity);
            Log.v("ProductAdapter ", "new quantity value  " + newQuantityValue);
            TextView prodQuantityTextView = (TextView) findViewById(R.id.prod_quantity);
            prodQuantityTextView.setText(String.valueOf(currentProduct.getProductQuantity()));
            Toast.makeText(getApplicationContext(),"One Product sold of this category", Toast.LENGTH_SHORT).show();
            Intent refresh = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(refresh);

        }
        else{
            Toast.makeText(getApplicationContext(),"Sorry, Out of stock", Toast.LENGTH_SHORT).show();
        }
    }
}
