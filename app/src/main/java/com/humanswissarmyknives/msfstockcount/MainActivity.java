package com.humanswissarmyknives.msfstockcount;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    // setuo for password verification: only three attempts to get the password right...
    int passwordCounter = 1;
    int remaining = 3;

    //UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        DownloadTask task = new DownloadTask();
//        task.execute("http:192.168.178.42:3000/users");

        // init the db
        db = new DatabaseHandler(this);

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

    void ocInitDB(View view) {
        SQLiteDatabase dataBase = db.getWritableDatabase();

        db.dropTables(dataBase);
        db = new DatabaseHandler(this);

        db.populateDB();

        // create MSR
        String[] products = {"DORAPARA5T-", "DINJCEFT2V-", "DINJCEFT1V-", "DEXTIODP1S2", "DORAFERF14T", "YDAF0190112"};
        db.createProductList(1, products);

        // create LSR
        String[] productsLSR = {"YDAF0190112", "TVECOILE5405I", "YTOY11176-64010", "PCOLFRESVF1G", "ASTASETSS--"};
        db.createProductList(2, productsLSR);


        User myUser1 = new User(1, "Dillah", "SupplyLogassist", "Counter", "q");
        User myUser2 = new User(2, "Dennis", "UF Trainer", "Admin", "a");
        User myUser3 = new User(3, "Emma", "Outreach Nurse", "Counter", "z");

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

        Warehouse myWarehouse1 = new Warehouse("Medical Warehouse", "MED", 1);
        Warehouse myWarehouse2 = new Warehouse("Logstock", "LOG", 2);
        Warehouse myWarehouse3 = new Warehouse("WatSan", "LOG", 2);

        db.addWarehouse(myWarehouse1);
        db.addWarehouse(myWarehouse2);
        db.addWarehouse(myWarehouse3);


        Intent iGoToUserSelection = new Intent(getApplicationContext(), UserSelectionActivity.class);

        startActivity(iGoToUserSelection);

    }
}
