package com.humanswissarmyknives.msfstockcount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class ProductListActivity extends AppCompatActivity {

    Product currentProduct;
    Batch currentBatch;
    User currentUser;
    DatabaseHandler db;

    ListView lvProducts;

    ArrayList<Product> arrayOfProducts;

    ProductsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);


        // init the db
        db = new DatabaseHandler(this);
        //db.populateDB();

        int i = db.getMaxProductId();


        // create the arraylist
        arrayOfProducts = new ArrayList<>();


        // populate the arraylist from the db & sort it
        arrayOfProducts = db.getAllProductsAsProduct();
        Collections.sort(arrayOfProducts);

        adapter = new ProductsAdapter(this, arrayOfProducts);


        ListView lvProducts = (ListView) findViewById(R.id.lvProducts);
        lvProducts.setAdapter(adapter);

        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent iGoToBatchList = new Intent(getApplicationContext(), BatchListActivity.class);

                iGoToBatchList.putExtra("currentProductCode", arrayOfProducts.get(position).getProduct_code());

                startActivity(iGoToBatchList);
            }
        });
    }
}
