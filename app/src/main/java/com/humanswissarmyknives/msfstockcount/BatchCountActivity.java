package com.humanswissarmyknives.msfstockcount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class BatchCountActivity extends AppCompatActivity {

    Product currentProduct;
    User currentUser;
    DatabaseHandler db;
    Warehouse currentWarehouse;
    ReportingList currentReportingList;
    CountedItem currentCountedItem;
    Batch currentBatch;

    // immutable textViews
    TextView tvProductCode;
    TextView tvProductDescription;
    TextView tvProductQty;

    // the textViews that we need to access
    TextView tvSUD;
    TextView tvTotalQty;

    // the editTexts
    EditText etBatchNumber;
    EditText etExpiryDate;
    EditText etSUD;
    EditText etQtySud;

    // set the initial values for the calculation. SUD is 1 to avoid division by zero
    int totalQty = 0;
    int currentScrollPosition;
    boolean existingBatch = false;
    boolean existingCountedItem = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_count);

        // init the db
        db = new DatabaseHandler(this);
        etBatchNumber = (EditText) findViewById(R.id.etBatchNumber);
        etBatchNumber.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        etExpiryDate = (EditText) findViewById(R.id.etExpiryDate);
        etSUD = (EditText) findViewById(R.id.etSUD);
        etQtySud = (EditText) findViewById(R.id.etQtySUD);

        tvProductCode = (TextView) findViewById(R.id.tvProductCode);
        tvProductDescription = (TextView) findViewById(R.id.tvProductDescription);
        tvProductQty = (TextView) findViewById(R.id.tvProductQty);

        ImageButton btDeleteBatch;

        // these are the ones to change
        tvSUD = (TextView) findViewById(R.id.tvSUD);
        //tvSUD.setText(String.valueOf(currentBatch.getBatch_sud()));
        tvTotalQty = (TextView) findViewById(R.id.tvTotalQty);

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
            }
            // find the layout elements and bind them to the variales.


            // check, if a countedItemid was handed over. If so, find the correct counted item and the related batch

            if (handedIntent.hasExtra("currentCountedItemId")) {

                existingBatch = true;
                existingCountedItem = true;
                currentCountedItem = db.getCountedItemById(handedIntent.getIntExtra("currentCountedItemId", 0));
                currentBatch = db.getBatchById(currentCountedItem.getBatchNumber_id());
                // update the edittexts from the properties of the counteditem
                etBatchNumber.setText(String.valueOf(currentBatch.getBatch_number()));
                etExpiryDate.setText(String.valueOf(currentBatch.getExpiry_date()));
                etSUD.setText(String.valueOf(currentBatch.getBatch_sud()));
                etQtySud.setText(String.valueOf(currentCountedItem.getCountedQty() / currentBatch.getBatch_sud()));
                tvSUD.setText(String.valueOf(currentBatch.getBatch_sud()));
                tvTotalQty.setText(String.valueOf(currentCountedItem.getCountedQty()));

            } else {
                // if no counted item was handed over: create an empty batch
                int currentLastBatch = db.getMaxBatchId();
                currentBatch = new Batch(currentLastBatch + 1, currentProduct.getProduct_code(), "newBatch", "00/00", 1);
                // update the edittexts from the properties of the counteditem
                etBatchNumber.setHint(String.valueOf(currentBatch.getBatch_number()));
                etExpiryDate.setHint(String.valueOf(currentBatch.getExpiry_date()));
                etSUD.setHint(String.valueOf(currentBatch.getBatch_sud()));
                currentCountedItem = new CountedItem();
            }

            if (handedIntent.hasExtra("currentScrollPosition")) {
                currentScrollPosition = handedIntent.getIntExtra("currentScrollPosition", 0);
            }

        }

        btDeleteBatch = (ImageButton) findViewById(R.id.btDeleteBatch);

        if (!existingCountedItem && !existingBatch) {
            btDeleteBatch.setAlpha(0.5f);
        }

        tvProductCode.setText(currentProduct.getProduct_code());
        tvProductDescription.setText(currentProduct.getProduct_description());
        tvProductQty.setText(String.valueOf(db.getTotalQtyCountItemByProductCode(currentProduct.getProduct_code())));

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

    public void ocBackToBatchList(View view) {

        Intent iGoBatchCount = new Intent(getApplicationContext(), BatchListActivity.class);
        iGoBatchCount.putExtra("currentProductCode", currentProduct.getProduct_code());
        iGoBatchCount.putExtra("currentUserId", currentUser.getId());
        iGoBatchCount.putExtra("currentWarehouseId", currentWarehouse.getId());
        iGoBatchCount.putExtra("currentReportingListId", currentReportingList.getId());
        iGoBatchCount.putExtra("currentScrollPosition", currentScrollPosition);

        startActivity(iGoBatchCount);
    }


    void ocSave(View view) {

        // update the current batch with data from the EditTextFields
        // check if all fields have some info - if not, raise the alarm
        if (etBatchNumber.getText().toString().trim().length() > 0 && etQtySud.getText().toString().trim().length() > 0 && etExpiryDate.getText().toString().trim().length() > 0 && etSUD.getText().toString().trim().length() > 0) {
            currentBatch.setBatch_number(String.valueOf(etBatchNumber.getText()));


        currentBatch.setProduct_code(currentProduct.getProduct_code());

        currentBatch.setExpiryDate(String.valueOf(etExpiryDate.getText()));
        currentBatch.setBatch_sud(Integer.parseInt(String.valueOf(etSUD.getText())));
            currentCountedItem.setSud(currentBatch.getBatch_sud());

        if (existingBatch) {
            db.updateBatch(currentBatch);
        } else {
            db.addBatch(currentBatch);
        }


        if (existingCountedItem) {
            db.updateCountedItem(currentCountedItem);
        } else {
            currentCountedItem = new CountedItem(
                    db.getMaxCountItemId() + 1,      // ID
                    currentProduct.getProduct_code(), // product code
                    currentBatch.getBatch_id(),       // batch ID
                    Integer.parseInt(String.valueOf(etQtySud.getText())) * currentBatch.getBatch_sud(), // total quantity = QTY SUD * SUD
                    currentUser.getId(),
                    currentBatch.getBatch_sud());
            db.addCountedItem(currentCountedItem);
            if (currentCountedItem.getId() != db.getMaxCountItemId()) {
                Log.i("Error", "counteditem not properly saved...");
            }
        }


        Intent iGoToBatchList = new Intent(getApplicationContext(), BatchListActivity.class);

        iGoToBatchList.putExtra("currentUserId", currentUser.getId());
        iGoToBatchList.putExtra("currentWarehouseId", currentWarehouse.getId());
        iGoToBatchList.putExtra("currentReportingListId", currentReportingList.getId());
        iGoToBatchList.putExtra("currentProductCode", currentProduct.getProduct_code());
        iGoToBatchList.putExtra("currentScrollPosition", currentScrollPosition);
        iGoToBatchList.putExtra("currentCountedItemId", currentCountedItem.getId());

        startActivity(iGoToBatchList);
        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Please fill all fields.");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Return to entry",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            builder1.setNegativeButton(
                    "Delete",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent iGoToBatchList = new Intent(getApplicationContext(), BatchListActivity.class);

                            iGoToBatchList.putExtra("currentUserId", currentUser.getId());
                            iGoToBatchList.putExtra("currentWarehouseId", currentWarehouse.getId());
                            iGoToBatchList.putExtra("currentReportingListId", currentReportingList.getId());
                            iGoToBatchList.putExtra("currentProductCode", currentProduct.getProduct_code());
                            iGoToBatchList.putExtra("currentScrollPosition", currentScrollPosition);

                            startActivity(iGoToBatchList);
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

    }

    void ocDeleteBatch(View view) {

        if (existingBatch || existingCountedItem) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Are you sure you want to delete this batch");
            builder1.setCancelable(true);

            builder1.setNegativeButton(
                    "Return to entry",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            builder1.setPositiveButton(
                    "Delete",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            if (existingBatch) {
                                db.deleteBatch(currentBatch);
                            }


                            if (existingCountedItem) {
                                db.deleteCountedItem(currentCountedItem);
                            }


                            Intent iGoToBatchList = new Intent(getApplicationContext(), BatchListActivity.class);

                            iGoToBatchList.putExtra("currentUserId", currentUser.getId());
                            iGoToBatchList.putExtra("currentWarehouseId", currentWarehouse.getId());
                            iGoToBatchList.putExtra("currentReportingListId", currentReportingList.getId());
                            iGoToBatchList.putExtra("currentProductCode", currentProduct.getProduct_code());
                            iGoToBatchList.putExtra("currentScrollPosition", currentScrollPosition);

                            startActivity(iGoToBatchList);
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();


        }

    }

    boolean etIsEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
    }
}
