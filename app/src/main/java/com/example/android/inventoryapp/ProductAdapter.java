package com.example.android.inventoryapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 * Created by PotnuruSiva on 23-07-2016.
 */
public class ProductAdapter extends ArrayAdapter {


    private String price = "Price : Rs. ";
    private String quantity = "Quantity available :";

    public ProductAdapter(Activity context, ArrayList<Product> productArrayList) {
        super(context, 0, productArrayList);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_view, parent, false);
        }

        final Product currentProduct = (Product) getItem(position);

        TextView prodNameTextView = (TextView) listItemView.findViewById(R.id.prod_name);
        prodNameTextView.setText(currentProduct.getProductName());

        final TextView prodQuantityTextView = (TextView) listItemView.findViewById(R.id.prod_quantity);
        prodQuantityTextView.setText(quantity + String.valueOf(currentProduct.getProductQuantity()));

        TextView prodPriceTextView = (TextView) listItemView.findViewById(R.id.prod_price);
        prodPriceTextView.setText(price + String.valueOf(currentProduct.getProductPrice()));

        ImageView prodImageView = (ImageView) listItemView.findViewById(R.id.prod_image);
        ByteArrayInputStream bis = new ByteArrayInputStream(currentProduct.getImage());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap theImage = BitmapFactory.decodeStream(bis, null, options);
        prodImageView.setImageBitmap(theImage);

        return listItemView;
    }
}
