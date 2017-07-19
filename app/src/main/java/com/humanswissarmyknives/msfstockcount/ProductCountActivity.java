package com.humanswissarmyknives.msfstockcount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class ProductCountActivity extends AppCompatActivity {

    DatabaseHandler db;
    User currentUser;
    Warehouse currentWarehouse;
    ReportingList currentReportingList;
    Product currentProduct;

    public ArrayList<CountedItem> arrayOfCounted;
    public CountedAdapter adapter;
    CountedItem currentCountedItem;

    // textviews in the layout that will be filled
    TextView tvProductCode;
    TextView tvProductDescription;
    TextView tvProductQty;

    TextView tvSUD;
    TextView tvTotalQty;

    // the editTexts
    EditText etSUD;
    EditText etQtySud;

    // set the initial values for the calculation. SUD is 1 to avoid division by zero
    int totalQty = 0;
    int currentScrollPosition;
    boolean existingCountedItem = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_count);

        // init the db
        db = new DatabaseHandler(this);

        tvProductCode = (TextView) findViewById(R.id.tvProductCode);
        tvProductDescription = (TextView) findViewById(R.id.tvProductDescription);
        tvProductQty = (TextView) findViewById(R.id.tvProductQuantity);

        tvSUD = (TextView) findViewById(R.id.tvSUD);
        tvTotalQty = (TextView) findViewById(R.id.tvTotalQty);

        etQtySud = (EditText) findViewById(R.id.etQtySUD);
        etSUD = (EditText) findViewById(R.id.etSUD);

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
            }
            if (handedIntent.hasExtra("currentProductCode")) {
                currentProduct = db.getProductByCode(handedIntent.getStringExtra("currentProductCode"));

                // check if something has already been counted for this product
                if (db.getNumberOfCountedItemByProductCode(currentProduct.getProduct_code()) > 0) {
                    currentCountedItem = db.getCountedItemByProductCode(currentProduct.getProduct_code());
                    existingCountedItem = true;

                    etSUD.setText(String.valueOf(currentCountedItem.getSud()));
                    etQtySud.setText(String.valueOf((
                            Integer.parseInt(String.valueOf(currentCountedItem.getCountedQty())) / Integer.parseInt(String.valueOf(currentCountedItem.getSud())))));
                    tvSUD.setText(String.valueOf(currentCountedItem.getSud()));
                    tvTotalQty.setText(String.valueOf(currentCountedItem.getCountedQty()));
                } else {
                    boolean b = false;
                    currentCountedItem = new CountedItem(currentProduct.getProduct_code(), 0, currentUser.getId(), 1, b);
                    etSUD.setHint("1");
                    etQtySud.setHint("1000");
                    tvSUD.setText("1");
                    tvTotalQty.setText("0");
                }
            }

            tvProductCode.setText(currentProduct.getProduct_code());
            tvProductDescription.setText(currentProduct.getProduct_description());
            //          tvProductQty.setText(String.format(Locale.GERMAN, "%,d", currentCountedItem.getCountedQty()));


            etSUD.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    tvSUD.setText(String.valueOf(s));

                    if ((etQtySud.getText().toString().trim().length() > 0 && etSUD.getText().toString().trim().length() > 0)) {
                        tvTotalQty.setText(String.valueOf(Integer.parseInt(String.valueOf(s)) * Integer.parseInt(String.valueOf(etQtySud.getText()))));
                    }
                }
            });

            // listen to the input for the qty
            etQtySud.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (etQtySud.getText().toString().trim().length() > 0 && tvSUD.getText().toString().trim().length() > 0) {
                        totalQty = Integer.parseInt(tvSUD.getText().toString()) * Integer.parseInt(s.toString());
                        tvTotalQty.setText(String.valueOf(totalQty));
                    }

                }
            });
        }


    }

    public void ocBackToProductList(View view) {
        Intent iGoBackToProductList = new Intent(getApplicationContext(), ProductListActivity.class);

        iGoBackToProductList.putExtra("currentProductCode", currentProduct.getProduct_code());
        iGoBackToProductList.putExtra("currentWarehouseId", currentWarehouse.getId());
        iGoBackToProductList.putExtra("currentReportingListId", currentReportingList.getId());
        iGoBackToProductList.putExtra("currentUserId", currentUser.getId());

        startActivity(iGoBackToProductList);
    }

    public void ocDeleteCountedItem(View view) {

        if (existingCountedItem) {
            db.deleteCountedItem(currentCountedItem);
        }


        Intent iGoBackToProductList = new Intent(getApplicationContext(), ProductListActivity.class);

        iGoBackToProductList.putExtra("currentProductCode", currentProduct.getProduct_code());
        iGoBackToProductList.putExtra("currentWarehouseId", currentWarehouse.getId());
        iGoBackToProductList.putExtra("currentReportingListId", currentReportingList.getId());
        iGoBackToProductList.putExtra("currentUserId", currentUser.getId());

        startActivity(iGoBackToProductList);
    }

    void ocSave(View view) {

        if (etSUD.getText().toString().trim().length() > 0 && etQtySud.getText().toString().trim().length() > 0) {
            currentCountedItem.setCountedQty(Integer.parseInt(etSUD.getText().toString()) * Integer.parseInt(etQtySud.getText().toString()));
            currentCountedItem.setProduct_code(currentProduct.getProduct_code().toString());
            currentCountedItem.setUser_id(currentUser.getId());
            currentCountedItem.setSud(Integer.parseInt(etSUD.getText().toString()));
            currentCountedItem.setBatchNumber_id(0);
        }

        Log.i("maxCountedItemId", String.valueOf(db.getMaxCountItemId()));
        if (existingCountedItem) {
            db.updateCountedItem(currentCountedItem);
        } else {
            db.addCountedItem(currentCountedItem);
        }

        Log.i("maxCountedItemId", String.valueOf(db.getMaxCountItemId()));

        Intent iGoBackToProductList = new Intent(getApplicationContext(), ProductListActivity.class);

        iGoBackToProductList.putExtra("currentProductCode", currentProduct.getProduct_code());
        iGoBackToProductList.putExtra("currentWarehouseId", currentWarehouse.getId());
        iGoBackToProductList.putExtra("currentReportingListId", currentReportingList.getId());
        iGoBackToProductList.putExtra("currentUserId", currentUser.getId());

        startActivity(iGoBackToProductList);

    }

    void ocDeleteCount(View view) {

    }
}
