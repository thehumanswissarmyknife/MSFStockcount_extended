package com.humanswissarmyknives.msfstockcount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);

        // init the db
        db = new DatabaseHandler(this);
        //db.populateDB();

        int i = db.getMaxProductId();

        // choose / create the user
/*
        Product myProduct1 = new Product("DORAPARA5T-", "PARACETAMOL (acetaminophen), 500 mg, tab.", 1000, 1);
        Product myProduct2 = new Product("DEXTARTS2RC", "ARTESUNATE, 200 mg, rectal caps.", 1000, 1);
        Product myProduct3 = new Product("DINFRINL1FBF1", "RINGER lactate, 1 l, flex. bag, PVC free", 12, 1);
        Product myProduct4 = new Product("DINJCEFT2V-", "CEFTRIAXONE sodium, eq. 250 mg base, powder, vial", 50, 1);
        Product myProduct5 = new Product("DORAATVR3T-", "ATV 300 mg / r 100 mg, tab.", 36, 1);
        Product myProduct6 = new Product("DEXTSOAP2B-", "SAVON, 200g, barre", 80, 0);

        db.addProduct(myProduct1);
        db.addProduct(myProduct2);
        db.addProduct(myProduct3);
        db.addProduct(myProduct4);
        db.addProduct(myProduct5);
/*
        Batch myBatch1 = new Batch("DINFRINL1FBF1", "PJKASDNK", "02/18", 12);
        Batch myBatch2 = new Batch("DINJCEFT2V-", "KLKJLJAKSDL", "02/18", 50);
        Batch myBatch3 = new Batch("DINFRINL1FBF1", "NAUBA", "02/18", 12);
        Batch myBatch4 = new Batch("DORAATVR3T-", "MOPQNKA", "02/18", 1000);
        Batch myBatch5 = new Batch("DINJCEFT2V-", "MKKNASKDN", "02/18", 50);
        Batch myBatch6 = new Batch("DINFRINL1FBF1", "NKALDN", "02/18", 12);
        Batch myBatch7 = new Batch("DORAPARA5T-", "NKA-12", "02/18", 1000);

        // arraylist to save all products created



        db.addBatch(myBatch1);
        db.addBatch(myBatch2);
        db.addBatch(myBatch3);
        db.addBatch(myBatch4);
        db.addBatch(myBatch5);
        db.addBatch(myBatch6);
        db.addBatch(myBatch7);*/

        // create the arraylist
        arrayOfProducts = new ArrayList<Product>();


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
