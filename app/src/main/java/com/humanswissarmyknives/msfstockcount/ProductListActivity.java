package com.humanswissarmyknives.msfstockcount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class ProductListActivity extends AppCompatActivity {

    User currentUser;
    DatabaseHandler db;
    Warehouse currentWarehouse;
    ReportingList currentReportingList;

    TextView tvWarehouseReport;

    ListView lvProducts;

    ArrayList<Product> arrayOfProducts;

    ProductsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        // init the db
        db = new DatabaseHandler(this);

        // get the values passed from the previous activity
        Intent handedIntent = getIntent();

        if (handedIntent != null) {
            if (handedIntent.hasExtra("currentUserId")) {
                currentUser = db.getUserById(handedIntent.getIntExtra("currentUserId", 0));
            }
            if (handedIntent.hasExtra("currentWarehouseId")) {
                currentWarehouse = db.getWarehousebyId(handedIntent.getIntExtra("currentWarehouseId", 0));
            }
            if (handedIntent.hasExtra("currentReportingListId")) {
                currentReportingList = db.getReportingListById(handedIntent.getIntExtra("currentReportingListId", 0));
                setTitle(currentReportingList.getName());
            }

            tvWarehouseReport = (TextView) findViewById(R.id.tvWarehouseReport);
            tvWarehouseReport.setText(currentWarehouse.getName() + " / " + currentReportingList.getName() + ": " + db.getNumberOfProductsInList(currentReportingList.getId()) + " items");

        }


        int i = db.getMaxProductId();

        // create the arraylist
        arrayOfProducts = new ArrayList<>();

        // populate the arraylist from the db & sort it
        arrayOfProducts = db.getProductsInAList(currentReportingList.getId());
        Collections.sort(arrayOfProducts);

        adapter = new ProductsAdapter(this, arrayOfProducts);


        ListView lvProducts = (ListView) findViewById(R.id.lvProducts);
        lvProducts.setAdapter(adapter);

        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // check if the item is batch managed or not:

                if (arrayOfProducts.get(position).getIsBatchManaged().equals("FALSE")) {
                    Intent iGoToProductCount = new Intent(getApplicationContext(), ProductCountActivity.class);

                    iGoToProductCount.putExtra("currentUserId", currentUser.getId());
                    iGoToProductCount.putExtra("currentWarehouseId", currentWarehouse.getId());
                    iGoToProductCount.putExtra("currentReportingListId", currentReportingList.getId());
                    iGoToProductCount.putExtra("currentProductCode", arrayOfProducts.get(position).getProduct_code());

                    startActivity(iGoToProductCount);


                } else {
                    Intent iGoToBatchList = new Intent(getApplicationContext(), BatchListActivity.class);

                    iGoToBatchList.putExtra("currentUserId", currentUser.getId());
                    iGoToBatchList.putExtra("currentWarehouseId", currentWarehouse.getId());
                    iGoToBatchList.putExtra("currentReportingListId", currentReportingList.getId());
                    iGoToBatchList.putExtra("currentProductCode", arrayOfProducts.get(position).getProduct_code());

                    startActivity(iGoToBatchList);
                }
            }
        });
    }

    public void ocBackToWarehouseSelection(View view) {
        Intent iGoToWarehouseSelection = new Intent(getApplicationContext(), WarehouseReportListActivity.class);

        if (currentReportingList != null && currentWarehouse != null) {
            iGoToWarehouseSelection.putExtra("currentUserId", currentUser.getId());
            iGoToWarehouseSelection.putExtra("currentWarehouseId", currentWarehouse.getId());
            iGoToWarehouseSelection.putExtra("currentReportingListId", currentReportingList.getId());
        }

        startActivity(iGoToWarehouseSelection);
    }
}
