package com.humanswissarmyknives.msfstockcount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by dennisvocke on 25.06.17.
 */

class ProductsAdapter extends ArrayAdapter {

    DatabaseHandler db = new DatabaseHandler(this.getContext());

    ProductsAdapter(Context context, ArrayList<Product> products) {
        super(context, R.layout.lv_product_list_row, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        // Get the prodcut at this position
        Product product = (Product) getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.lv_product_list_row, parent, false);

            viewHolder.tvProductCode = (TextView) convertView.findViewById(R.id.tvProductCode);
            viewHolder.tvProductDescription = (TextView) convertView.findViewById(R.id.tvProductDescription);
            viewHolder.tvProductSud = (TextView) convertView.findViewById(R.id.tvProductSud);
            viewHolder.tvProductTotalQty = (TextView) convertView.findViewById(R.id.tvProductQty);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvProductCode.setText(product.getProduct_code());
        viewHolder.tvProductDescription.setText(product.getProduct_description());
        viewHolder.tvProductTotalQty.setText(String.format(Locale.GERMAN, "%,d", product.getTotalProductQty(this.getContext())));
        viewHolder.tvProductSud.setText(String.valueOf(product.getProduct_sud()));

        return convertView;
    }

    private static class ViewHolder {
        TextView tvProductCode;
        TextView tvProductDescription;
        TextView tvProductSud;
        TextView tvProductTotalQty;
    }
}
