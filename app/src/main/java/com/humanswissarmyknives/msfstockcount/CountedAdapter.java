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
 * Created by dennisvocke on 27.06.17.
 */

public class CountedAdapter extends ArrayAdapter {

    DatabaseHandler db = new DatabaseHandler(this.getContext());

    public CountedAdapter(Context context, ArrayList<CountedItem> batches) {
        super(context, R.layout.lv_batch_list_row, batches);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        // Get the prodcut at this position
        CountedItem countedItem = (CountedItem) getItem(position);
        Batch batch = db.getBatchById(countedItem.getBatchNumber_id());

        CountedAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new CountedAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.lv_batch_list_row, parent, false);

            viewHolder.tvBatchNumber = (TextView) convertView.findViewById(R.id.tvBatchNumber);
            viewHolder.tvBatchExpDate = (TextView) convertView.findViewById(R.id.tvBatchExpDate);
            //viewHolder.tvBatchSud = (TextView) convertView.findViewById(R.id.tvBatchSud);
            viewHolder.tvBatchQty = (TextView) convertView.findViewById(R.id.tvBatchQty);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CountedAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.tvBatchNumber.setText(batch.getBatch_number());
        viewHolder.tvBatchExpDate.setText(batch.getExpiry_date());
        //viewHolder.tvBatchSud.setText(String.valueOf(batch.getBatch_sud()));
        viewHolder.tvBatchQty.setText(String.format(Locale.GERMAN, "%,d", batch.getTotalBatchQty(this.getContext())));

        return convertView;
    }

    private static class ViewHolder {
        TextView tvBatchNumber;
        TextView tvBatchExpDate;
        //TextView tvBatchSud;
        TextView tvBatchQty;
    }
}

