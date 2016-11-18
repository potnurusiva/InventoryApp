package com.example.android.inventoryapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class DetailActivity extends AppCompatActivity {
    private static final String previous1 = "MainActivityButton";
    private static final String previous2 = "MainActivityListItem";
    private static final int SELECT_PICTURE = 1;
    int column_index;
    String imagePath;
    Cursor cursor;
    String previousValue;
    String selectedImagePath;
    String filemanagerstring;
    ProductDBHelper dbHelper = new ProductDBHelper(this);
    TextView codeKeyTextView;
    TextView codeTextView;
    EditText nameEditText;
    EditText quantityEditText;
    EditText priceEditText;
    ImageView selectedImage;
    Button uploadImageButton;
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

        codeKeyTextView = (TextView) findViewById(R.id.prod_code_textView);
        //String codeKey = codeKeyTextView.getText().toString();

        codeTextView = (TextView) findViewById(R.id.prod_code_value);
        //int codeValue = Integer.parseInt(codeTextView.getText().toString());

        nameEditText = (EditText) findViewById(R.id.prod_name_editText);
        //String nameValue = nameEditText.getText().toString();

        quantityEditText = (EditText) findViewById(R.id.prod_quantity_editText);
        //String quantityValue = quantityEditText.getText().toString();

        priceEditText = (EditText) findViewById(R.id.prod_price_editText);
        //float priveValue = Float.parseFloat(priceEditText.getText().toString());

        selectedImage = (ImageView) findViewById(R.id.prod_image);
        uploadImageButton = (Button) findViewById(R.id.prod_image_button);
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
            Cursor data = dbHelper.getProductData(code);
            byte[] image = data.getBlob(data.getColumnIndex(ProductContract.ProductItems.COLUMN_IMAGE));
            ByteArrayInputStream bis = new ByteArrayInputStream(image);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap theImage = BitmapFactory.decodeStream(bis, null, options);
            selectedImage.setImageBitmap(theImage);
            codeTextView.setText(String.valueOf(data.getInt(data.getColumnIndex(ProductContract.ProductItems._ID))));
            nameEditText.setText(data.getString(data.getColumnIndex(ProductContract.ProductItems.COLUMN_NAME)));
            quantityEditText.setText(String.valueOf(data.getInt(data.getColumnIndex(ProductContract.ProductItems.COLUMN_QUANTITY))));
            priceEditText.setText(String.valueOf(data.getFloat(data.getColumnIndex(ProductContract.ProductItems.COLUMN_PRICE))));
        }

    }

    public void saveProduct(View view) {
        String nameValue = nameEditText.getText().toString();
        String quantityValue = quantityEditText.getText().toString();
        String priceValue = priceEditText.getText().toString();

        Bitmap photo = ((BitmapDrawable) selectedImage.getDrawable()).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        if (nameValue.length() != 0 && quantityValue.length() != 0 && priceValue.length() != 0 && bArray != null) {
            int quantityValueInteger = Integer.parseInt(quantityValue);
            float priceValueFloat = Float.parseFloat(priceValue);
            boolean result = dbHelper.addNewProduct(nameValue, quantityValueInteger, priceValueFloat, bArray);
            if (result == true) {
                Toast.makeText(this, "Product Details added successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Please enter valid product details", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Please enter valid product details", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateProduct(View view) {
        int codeKey = Integer.parseInt(codeTextView.getText().toString());
        String nameValue = nameEditText.getText().toString();
        String quantityValue = quantityEditText.getText().toString();
        String priceValue = priceEditText.getText().toString();
        Bitmap photo = ((BitmapDrawable) selectedImage.getDrawable()).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        if (nameValue.length() != 0 && quantityValue.length() != 0 && priceValue.length() != 0 && bArray != null) {
            int quantityValueInteger = Integer.parseInt(quantityValue);
            float priceValueFloat = Float.parseFloat(priceValue);
            boolean result = dbHelper.updateProductDetails(codeKey, nameValue, quantityValueInteger, priceValueFloat, bArray);
            if (result == true) {
                Toast.makeText(this, "Product Details updated successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please enter valid product details to update", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Please enter valid product details to update", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteProduct(View view) {
        final int codeKey = Integer.parseInt(codeTextView.getText().toString());
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Delete")
                .setMessage("Are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean result = dbHelper.deleteProductDetails(codeKey);
                        if (result == true) {
                            Toast.makeText(getApplicationContext(), "Product Details deleted completely", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error in deletion", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                }).setNegativeButton("no", null).show();
        /*boolean result = dbHelper.deleteProductDetails(codeKey);
        if (result == true) {
            Toast.makeText(this, "Product Details deleted completely", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Error in deletion", Toast.LENGTH_SHORT).show();
        }
        */
    }

    public void orderProduct(View view) {
        String nameValue = nameEditText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"supplier@example.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Order for: " + nameValue);
        intent.putExtra(Intent.EXTRA_TEXT, "Please ship the " + nameValue);
        startActivity(Intent.createChooser(intent, "Send email.."));
    }

    public void uploadImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                filemanagerstring = selectedImageUri.getPath();
                selectedImagePath = getPath(selectedImageUri);

                selectedImage.setImageURI(selectedImageUri);
                imagePath.getBytes();
                //Bitmap bm = BitmapFactory.decodeFile(imagePath);

            }
        }
    }

    public String getPath(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            imagePath = cursor.getString(column_index);
        }
        return cursor.getString(column_index);
    }

}
