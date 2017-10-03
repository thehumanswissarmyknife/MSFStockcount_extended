package com.humanswissarmyknives.msfstockcount;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// import stuff for socket.io
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.defaultValue;


public class MainActivity extends AppCompatActivity {

    // find out if the inital setup has been completed
    SharedPreferences sharedPref;

    SharedPreferences.Editor editor;

    SingletonClass mySingleton;
    Socket mSocket;
    Stack globalStack;
    Stack pushedStack;
    boolean intialSetup;

    //    static String url = "http://165.227.162.247:3000";
    static String url = "http://192.168.178.42:3000";

    // get the signleton for stack access and socket!


    User currentUser;
    DatabaseHandler db;

    ArrayList<User> arrayOfUsers;

    EditText etPass;
    Spinner spinner;

    // setuo for password verification: only three attempts to get the password right...
    int passwordCounter = 1;
    int remaining = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        intialSetup = sharedPref.getBoolean(getString(R.string.intialSetup), false);

        mySingleton = SingletonClass.getInstance();

        mSocket = SingletonClass.getSocket();
        globalStack = SingletonClass.getGlobalStack();
        pushedStack = SingletonClass.getPushedStack();

        // check the shared preferences, if the initial setup has been done!
        Log.i("Intial Setup", String.valueOf(intialSetup));
        if (!intialSetup) {
            mSocket.emit("getInitialSetup", "");
        }

        mSocket.on("sendInitialSetup", onInitialSetup);

        db = new DatabaseHandler(this);

        arrayOfUsers = new ArrayList<>();
        arrayOfUsers = db.getAllUsersAsUser();


        // establish the socket

        mSocket.connect();


//        // inti the global stack!!!
//        Stack globalStack = ((MyStack) getApplicationContext()).getMyStack();
//        Stack pushedStack = ((MyStack) getApplicationContext()).getPushedStack();

        // init the db



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

    private Emitter.Listener onInitialSetup = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.i("Socket receiving", "initial setup");

            db = new DatabaseHandler(getApplicationContext());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {

                        int qUsers = data.getInt("qUsers");
                        JSONArray users = data.getJSONArray("users");

                        // check if all users were transmitted
                        if (qUsers == users.length()) {
                            // enter the users into the db
                            for (int i = 0; i < users.length(); i++) {
                                User myUser = new User(users.getJSONObject(i));
                                db.addUser(myUser);
                            }
                        }


                        int qProducts = data.getInt("qProducts");
                        JSONArray products = data.getJSONArray("products");

                        // check if all products were transmitted
                        if (qProducts == products.length()) {
                            for (int i = 0; i < products.length(); i++) {
                                Product myProduct = new Product(products.getJSONObject(i));
                                db.addProduct(myProduct);
                            }
                        } else {
                            Log.e("Initial Setup", "not all products received");
                        }

                        int qReportinglists = data.getInt("qReportinglists");
                        JSONArray reportinglists = data.getJSONArray("reportinglists");

                        if (qReportinglists == reportinglists.length()) {
                            for (int i = 0; i < reportinglists.length(); i++) {

                                JSONObject rList = reportinglists.getJSONObject(i);
                                JSONArray tArray = rList.getJSONArray("products");
                                ArrayList tempArrayList = new ArrayList<String>();

                                // create the reportinglist object
                                ReportingList temp = new ReportingList(rList);

                                // writing the date to the db and get the ID back
                                db.addReportingList(temp);
                                int tempId = db.getMaxReportingListId();

                                for (int j = 0; j < tArray.length(); j++) {
                                    tempArrayList.add(tArray.get(j));
                                    Log.i("prouct added", tempArrayList.get(j).toString());
                                }
                                db.createProductList(tempId, tempArrayList);

                            }
                        }

                        int qWarehouses = data.getInt("qWarehouses");
                        JSONArray warehouses = data.getJSONArray("warehouses");

                        if (qWarehouses == warehouses.length()) {
                            for (int i = 0; i < warehouses.length(); i++) {
                                JSONObject temp = warehouses.getJSONObject(i);
                                JSONArray tempIds = temp.getJSONArray("reportingListIds");
//                                Warehouse tempWarehouse = new Warehouse(warehouses.getJSONObject(i));
                                for (int j = 0; j < tempIds.length(); j++) {
                                    Warehouse tempWarehouse = new Warehouse(temp.getString("name"), temp.getString("category"), tempIds.getInt(j));
                                    db.addWarehouse(tempWarehouse);
                                    Log.i("WW", temp.getString("name") + ", " + temp.getString("category") + ", " + tempIds.getInt(j));
                                }
//                                Log.i("warehouse added", tempWarehouse.toString());
                            }
                        }


                        int qMessages = data.getInt("qMessages");
                        JSONArray messages = data.getJSONArray("messages");

                        Log.i("Users received", String.valueOf(qUsers));
                        Log.i("Products received", String.valueOf(qProducts));
                        editor = sharedPref.edit();
                        editor.putBoolean(getString(R.string.intialSetup), true);
                        editor.commit();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
}
