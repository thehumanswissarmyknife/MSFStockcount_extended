package com.humanswissarmyknives.msfstockcount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    /*    Product currentProduct;
        Batch currentBatch;*/
    User currentUser;
    DatabaseHandler db;

    ArrayList<User> arrayOfUsers;
/*    Array array;*/

    EditText etPass;
    Spinner spinner;

    int passwordCounter = 1;
    int remaining = 3;

    //UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init the db
        db = new DatabaseHandler(this);





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


        User myUser1 = new User(1, "Dillah", "SupplyLogassist", "Counter", "qwer");
        User myUser2 = new User(2, "Dennis", "UF Trainer", "Admin", "asd");
        User myUser3 = new User(3, "Emma", "Outreach Nurse", "Counter", "zxc");

        db.addUser(myUser1);
        db.addUser(myUser3);
        db.addUser(myUser2);

        ReportingList myReportingList1 = new ReportingList(1, "MSR", "all medical items", "MED");
        ReportingList myReportingList2 = new ReportingList(2, "LSR", "all log items", "LOG");
        ReportingList myReportingList3 = new ReportingList(3, "MSR EPREP", "all medical items for emergencies", "MED");
        ReportingList myReportingList4 = new ReportingList(4, "LSR ERU", "all log items of the emergency response unit", "LOG");


        db.addReportingList(myReportingList1);
        db.addReportingList(myReportingList2);
        db.addReportingList(myReportingList3);
        db.addReportingList(myReportingList4);
/*
        Warehouse myWarehouse1 = new Warehouse("Medical Warehouse", "MED", 1);
        Warehouse myWarehouse2 = new Warehouse("Logstock", "LOG", 2);
        Warehouse myWarehouse3 = new Warehouse("WatSan", "LOG", 2);

        db.addWarehouse(myWarehouse1);
        db.addWarehouse(myWarehouse2);
        db.addWarehouse(myWarehouse3);


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
        db.addBatch(myBatch7);

*/


        arrayOfUsers = new ArrayList<>();
        arrayOfUsers = db.getAllUsersAsUser();

        // Selection of the spinner
        spinner = (Spinner) findViewById(R.id.spUser);

        // set up an arrayadapter for the arrayofusers with a custom spinner xml
        ArrayAdapter<User> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_user_item, arrayOfUsers);
        adapter.setDropDownViewResource(R.layout.spinner_user_item);
        spinner.setAdapter(adapter);

        EditText etPass = (EditText) findViewById(R.id.etPassword);


    }

    void login(View view) {

        // check which user is selected and get the user object then check the object' password against the password in the etPass
        currentUser = arrayOfUsers.get(spinner.getSelectedItemPosition());

        //currentUser = db.getUserByName(userName);
        EditText etPass = (EditText) findViewById(R.id.etPassword);
        String currentPass = etPass.getText().toString();

        if (currentUser.getPassword().equals(currentPass)) {
            Intent iGoToWarehouseList = new Intent(getApplicationContext(), WarehouseReportListActivity.class);

            iGoToWarehouseList.putExtra("currentUserId", currentUser.getId());

            startActivity(iGoToWarehouseList);
        } else {
            etPass.setText("");
            passwordCounter++;
            remaining = remaining - 1;
            if (remaining > 0) {
                Toast.makeText(this, "Wrong password " + String.valueOf(remaining) + " attempts remaining", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Too many attempts, try again in five minutes", Toast.LENGTH_SHORT).show();
            }

        }

    }
}
