package com.humanswissarmyknives.msfstockcount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class UserSelectionActivity extends AppCompatActivity {

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

        db.populateDB();
        db.createProductList(1);

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
