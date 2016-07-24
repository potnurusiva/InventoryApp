package com.example.android.inventoryapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {
    private static final String previous1 = "MainActivityButton";
    private static final String previous2 = "MainActivityListItem";
    String previousValue;
    ProductDBHelper dbHelper = new ProductDBHelper(this);
    TextView codeKeyTextView;
    TextView codeTextView;
    EditText nameEditText;
    EditText quantityEditText;
    EditText priceEditText;
    Button saveButton;
    Button updateButton;
    Button orderButton;
    Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        previousValue = intent.getStringExtra(MainActivity.EXTRA_TEXT1);
        Log.v("DetailActivity", "caller" + previousValue);

        //previousValue = context().getIntent().getStringExtra("caller");
        codeKeyTextView = (TextView) findViewById(R.id.prod_code_textView);
        String codeKey = codeKeyTextView.getText().toString();

        codeTextView = (TextView) findViewById(R.id.prod_code_value);
        int codeValue = Integer.parseInt(codeTextView.getText().toString());

        nameEditText = (EditText) findViewById(R.id.prod_name_editText);
        String nameValue = nameEditText.getText().toString();

        quantityEditText = (EditText) findViewById(R.id.prod_quantity_editText);
        String quantityValue = quantityEditText.getText().toString();
        //int quantityValue = Integer.parseInt(quantityEditText.getText().toString());

        priceEditText = (EditText) findViewById(R.id.prod_price_editText);
        String priveValue = priceEditText.getText().toString();
        //float priveValue = Float.parseFloat(priceEditText.getText().toString());

        saveButton = (Button) findViewById(R.id.prod_details_save);
        updateButton = (Button) findViewById(R.id.prod_details_update);
        orderButton = (Button) findViewById(R.id.prod_details_order);
        deleteButton = (Button) findViewById(R.id.prod_details_delete);

        if (previous1.equals(previousValue)) {
            codeKeyTextView.setVisibility(View.GONE);
            codeTextView.setVisibility(View.GONE);
            updateButton.setVisibility(View.GONE);
            orderButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        } else if (previousValue != null) {
            saveButton.setVisibility(View.GONE);
            int code = Integer.parseInt(previousValue);
            //ArrayList<Product> productsList = new ArrayList<Product>();
            Log.v("DetailActivity","before getProductData");
            Cursor data = dbHelper.getProductData(code);
            Log.v("DetailActivity","after getProductData");
            codeTextView.setText(String.valueOf(data.getInt(data.getColumnIndex(ProductContract.ProductItems._ID))));
            nameEditText.setText(data.getString(data.getColumnIndex(ProductContract.ProductItems.COLUMN_NAME)));
            quantityEditText.setText(String.valueOf(data.getInt(data.getColumnIndex(ProductContract.ProductItems.COLUMN_QUANTITY))));
            priceEditText.setText(String.valueOf(data.getFloat(data.getColumnIndex(ProductContract.ProductItems.COLUMN_PRICE))));
            Log.v("DetailActivity","after displaying values in details");
        }

    }

    public void saveProduct(View view){
        String nameValue = nameEditText.getText().toString();
        Log.v("DetailsActivity", "saveProduct");
        int quantityValue = Integer.parseInt(quantityEditText.getText().toString());
        float priceValue = Float.parseFloat(priceEditText.getText().toString());
        if( nameValue != null && quantityValue > 0 && priceValue > 0){
            boolean result = dbHelper.addNewProduct(nameValue, quantityValue, priceValue);
            if(result == true) {
                Toast.makeText(this, "Product Details added successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Please enter valid product details", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void updateProduct(View view){
        int codeKey = Integer.parseInt(codeTextView.getText().toString());
        String nameValue = nameEditText.getText().toString();
        int quantityValue = Integer.parseInt(quantityEditText.getText().toString());
        float priceValue = Float.parseFloat(priceEditText.getText().toString());
        Log.v("DetailsActivity", "updateProduct");
        if( nameValue != null && quantityValue > 0 && priceValue > 0){
            boolean result = dbHelper.updateProductDetails(codeKey,nameValue, quantityValue, priceValue);
            if(result == true) {
                Toast.makeText(this, "Product Details update successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Please enter valid product details to update", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void deleteProduct(View view){
        Log.v("DetailsActivity", "deleteProduct");
        int codeKey = Integer.parseInt(codeTextView.getText().toString());
        boolean result = dbHelper.deleteProductDetails(codeKey);
        if(result == true) {
            Toast.makeText(this, "Product Details deleted completely", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Error in deletion", Toast.LENGTH_SHORT).show();
        }
    }

    public void orderProduct (View view){
        Log.v("DetailsActivity", "orderProduct");
        String nameValue = nameEditText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"supplier@example.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Order for: " + nameValue);
        intent.putExtra(Intent.EXTRA_TEXT   , "Please ship the " + nameValue);
        startActivity(Intent.createChooser(intent, "Send email.."));
    }
}
