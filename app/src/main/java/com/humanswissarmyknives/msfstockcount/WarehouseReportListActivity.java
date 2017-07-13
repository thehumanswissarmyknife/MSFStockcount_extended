package com.humanswissarmyknives.msfstockcount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class WarehouseReportListActivity extends AppCompatActivity {

    User currentUser;
    DatabaseHandler db;
    Warehouse selectedWarehouse;
    ReportingList selectedReportingList;

    ArrayList<Warehouse> arrayOfWarehouses;
    ArrayList<ReportingList> arrayOfReportingLists;

    Spinner spinner;
    Spinner spReportingList;

    TextView tvReportingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_report_list);

        // my section after init
        db = new DatabaseHandler(this);

        // get the values passed from the previous activity
        Intent handedIntent = getIntent();
        if (handedIntent != null) {
            int userId = handedIntent.getIntExtra("currentUserId", 0);
            currentUser = db.getUserById(userId);
        }

        // get the warehouses from teh db and polulate the arraylist with them
        arrayOfWarehouses = new ArrayList<>();
        arrayOfWarehouses = db.getAllWarehousesAsWarehouse();

        spinner = (Spinner) findViewById(R.id.spWarehouses);

        // set the arraylist as source for the spinner and set the XML to the spinner_user_item
        final ArrayAdapter<Warehouse> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_user_item, arrayOfWarehouses);
        adapter.setDropDownViewResource(R.layout.spinner_user_item);
        spinner.setAdapter(adapter);

        // listen to the changes of the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                tvReportingList = (TextView) findViewById(R.id.tvReportingList);

                selectedWarehouse = arrayOfWarehouses.get(spinner.getSelectedItemPosition());

                setReportingByCategory(selectedWarehouse.getCategory());

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    void setReportingByCategory(String category) {
        spReportingList = (Spinner) findViewById(R.id.spReportingList);
        arrayOfReportingLists = new ArrayList<>();
        arrayOfReportingLists = db.getAllReportingListsByCategory(selectedWarehouse.getCategory());

        // set the arraylist as source for the spinner and set the XML to the spinner_user_item
        final ArrayAdapter<ReportingList> adapterRL = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_user_item, arrayOfReportingLists);
        adapterRL.setDropDownViewResource(R.layout.spinner_user_item);
        spReportingList.setAdapter(adapterRL);

        spReportingList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedReportingList = arrayOfReportingLists.get(spReportingList.getSelectedItemPosition());

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void ocStartCounting(View view) {

        // gather all info we have: user, warehouse, reportinglist and pass it to the next activity
        Intent iGoToProductList = new Intent(getApplicationContext(), ProductListActivity.class);

        if (selectedReportingList != null && selectedWarehouse != null) {
            iGoToProductList.putExtra("currentUserId", currentUser.getId());
            iGoToProductList.putExtra("selectedWarehouseId", selectedWarehouse.getId());
            iGoToProductList.putExtra("selectedReportingListId", selectedReportingList.getId());
        }

        startActivity(iGoToProductList);
    }
}
