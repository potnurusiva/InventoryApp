package com.example.android.inventoryapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by PotnuruSiva on 23-07-2016.
 */
public class ProductAdapter extends ArrayAdapter {

    public ProductAdapter(Activity context, ArrayList<Product> productArrayList){
        super(context, 0 ,productArrayList);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_view, parent,false);
        }

        final Product currentProduct = (Product) getItem(position);

        //TextView prodCodeTextView = (TextView) listItemView.findViewById(R.id.prod_code);
        //prodCodeTextView.setText(currentProduct.getProductCode());

        TextView prodNameTextView = (TextView) listItemView.findViewById(R.id.prod_name);
        prodNameTextView.setText(currentProduct.getProductName());

        final TextView prodQuantityTextView = (TextView) listItemView.findViewById(R.id.prod_quantity);
        prodQuantityTextView.setText(String.valueOf(currentProduct.getProductQuantity()));

        TextView prodPriceTextView = (TextView) listItemView.findViewById(R.id.prod_price);
        prodPriceTextView.setText(String.valueOf(currentProduct.getProductPrice()));

        /*
        Button sellButton = (Button) listItemView.findViewById(R.id.prod_sell);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = currentProduct.getProductQuantity() - 1;
                if(newQuantity >= 0) {
                    ProductDBHelper dbHelper = new ProductDBHelper(getContext());
                    dbHelper.updateQuantity(currentProduct.getProductCode(), newQuantity);
                    String newQuantityValue = String.valueOf(newQuantity);
                    Log.v("ProductAdapter ", "new quantity value  " + newQuantityValue);
                    prodQuantityTextView.setText(String.valueOf(currentProduct.getProductQuantity()));
                    Toast.makeText(getContext(),"One Product sold of this category", Toast.LENGTH_SHORT).show();
                    //Intent refresh = new Intent(getContext(), MainActivity.class);
                    //startActivity(refresh);

                }
                else{
                    Toast.makeText(getContext(),"Sorry, Out of stock", Toast.LENGTH_SHORT).show();
                }
            }


        });
        */
        return listItemView;
    }
}
