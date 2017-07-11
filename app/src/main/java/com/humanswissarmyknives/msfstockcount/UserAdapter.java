package com.humanswissarmyknives.msfstockcount;

import android.content.Context;
import android.database.ContentObservable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dennisvocke on 10.07.17.
 */

class UserAdapter extends ArrayAdapter {

    DatabaseHandler db = new DatabaseHandler(this.getContext());

    public UserAdapter(Context context, ArrayList<User> users) {
        super(context, R.layout.lv_user_list_row, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = (User) getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.lv_user_list_row, parent, false);

            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvFunction = (TextView) convertView.findViewById(R.id.tvFunction);
            viewHolder.tvLevel = (TextView) convertView.findViewById(R.id.tvLevel);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(user.getName());
        viewHolder.tvFunction.setText(user.getFunction());
        viewHolder.tvLevel.setText(user.getLevel());

        return convertView;
    }

    private static class ViewHolder {
        TextView tvName;
        TextView tvFunction;
        TextView tvLevel;
    }

}
