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
import java.util.Locale;

public class BatchListActivity extends AppCompatActivity {

    Product currentProduct;
    User currentUser;
    DatabaseHandler db;
    Warehouse currentWarehouse;
    ReportingList currentReportingList;

    public ArrayList<CountedItem> arrayOfCounted;
    public CountedAdapter adapter;
    CountedItem currentCountedItem;

    // textviews in the layout that will be filled
    TextView tvProductCode;
    TextView tvProductDescription;
    TextView tvProductQty;

    //the listview
    ListView lvBatches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_list);

        tvProductCode = (TextView) findViewById(R.id.tvProductCode);
        tvProductDescription = (TextView) findViewById(R.id.tvProductDescription);
        tvProductQty = (TextView) findViewById(R.id.tvProductQuantity);

        // init the db
        db = new DatabaseHandler(this);

        // get the values passed from the previous activity
        Intent handedIntent = getIntent();
        try {
            if (handedIntent != null) {
                if (handedIntent.hasExtra("currentUserId")) {
                    currentUser = db.getUserById(handedIntent.getIntExtra("currentUserId", 0));
                }
                if (handedIntent.hasExtra("currentWarehouseId")) {
                    currentWarehouse = db.getWarehousebyId(handedIntent.getIntExtra("currentWarehouseId", 0));
                }
                if (handedIntent.hasExtra("currentReportingListId")) {
                    currentReportingList = db.getReportingListById(handedIntent.getIntExtra("currentReportingListId", 0));
                }
                if (handedIntent.hasExtra("currentProductCode")) {
                    currentProduct = db.getProductByCode(handedIntent.getStringExtra("currentProductCode"));
                }

                tvProductCode.setText(currentProduct.getProduct_code());
                tvProductDescription.setText(currentProduct.getProduct_description());
                tvProductQty.setText(String.format(Locale.GERMAN, "%,d", db.getTotalQtyCountItemByProductCode(currentProduct.getProduct_code())));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        arrayOfCounted = db.getAllCountedItemsAsBatchByProductCode(currentProduct.getProduct_code());

        adapter = new CountedAdapter(this, arrayOfCounted);

        final ListView lvBatches = (ListView) findViewById(R.id.lvBatches);

        lvBatches.setAdapter(adapter);

        lvBatches.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int index = lvBatches.getFirstVisiblePosition();
                Intent iGoBatchCount = new Intent(getApplicationContext(), BatchCountActivity.class);
                iGoBatchCount.putExtra("currentProductCode", currentProduct.getProduct_code());
                iGoBatchCount.putExtra("currentCountedItemId", arrayOfCounted.get(position).getId());
                iGoBatchCount.putExtra("currentScrollPosition", index);
                iGoBatchCount.putExtra("currentWarehouseId", currentWarehouse.getId());
                iGoBatchCount.putExtra("currentReportingListId", currentReportingList.getId());
                iGoBatchCount.putExtra("currentUserId", currentUser.getId());

                startActivity(iGoBatchCount);
            }
        });
    }

    public void ocBackToProductList(View view) {
        Intent iGoBackToProductList = new Intent(getApplicationContext(), ProductListActivity.class);

        iGoBackToProductList.putExtra("currentProductCode", currentProduct.getProduct_code());
        iGoBackToProductList.putExtra("currentWarehouseId", currentWarehouse.getId());
        iGoBackToProductList.putExtra("currentReportingListId", currentReportingList.getId());
        iGoBackToProductList.putExtra("currentUserId", currentUser.getId());

        startActivity(iGoBackToProductList);
    }

    public void ocNewBatchCount(View view) {

        Intent iGoBatchCount = new Intent(getApplicationContext(), BatchCountActivity.class);

        iGoBatchCount.putExtra("currentProductCode", currentProduct.getProduct_code());
        iGoBatchCount.putExtra("currentWarehouseId", currentWarehouse.getId());
        iGoBatchCount.putExtra("currentReportingListId", currentReportingList.getId());
        iGoBatchCount.putExtra("currentUserId", currentUser.getId());

        startActivity(iGoBatchCount);
    }
}
