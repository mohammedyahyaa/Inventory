/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.mohamed.inventory;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.mohamed.inventory.data.ProductContract;


public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView QuantityTextView = (TextView) view.findViewById(R.id.Quantity);
        TextView saleTextView = (TextView) view.findViewById(R.id.sells_txt);
        Button sellItemButton = (Button) view.findViewById(R.id.button6);

        int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int QuantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
        int saleColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_SALE);
        int iDColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry._ID);

        final String rowId = cursor.getString(iDColumnIndex);
        String productName = cursor.getString(nameColumnIndex);
        String productQuantity = cursor.getString(QuantityColumnIndex);
        String productprice = cursor.getString(priceColumnIndex);
        String productsale = cursor.getString(saleColumnIndex);
        final Integer prodsellesint = cursor.getInt(saleColumnIndex);


        nameTextView.setText(productName);
        QuantityTextView.setText(productQuantity);
        priceTextView.setText(productprice);
        saleTextView.setText(productsale);


        if (!productQuantity.isEmpty()) {
            final Integer prodQuantityint = Integer.parseInt(productQuantity);


            if (TextUtils.isEmpty(prodQuantityint.toString())) {
                QuantityTextView.setText("unknown");
            }
            sellItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentResolver resolver = v.getContext().getContentResolver();
                    ContentValues values = new ContentValues();
                    if (prodQuantityint > 0) {
                        Integer itemId = Integer.parseInt(rowId);
                        Integer newstockValue = prodQuantityint - 1;
                        Integer newSalesValue = prodsellesint + 1;
                        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, newstockValue);
                        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SALE, newSalesValue);
                        Uri currentItemUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, itemId);
                        resolver.update(currentItemUri, values, null, null);
                    }
                }
            });
        }
    }
}