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

class MainActivity extends AppCompatActivity {

    Product currentProduct;
    Batch currentBatch;
    User currentUser;
    DatabaseHandler db;

    ArrayList<User> arrayOfUsers;
    Array array;

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

        User myUser1 = new User(1, "Dillah", "SupplyLogassist", "Counter", "mypassword");
        User myUser2 = new User(2, "Dennis", "UF Trainer", "Admin", "234");
        User myUser3 = new User(3, "Emma", "Outreach Nurse", "Counter", "123");

        db.addUser(myUser1);
        db.addUser(myUser3);
        db.addUser(myUser2);

*/


        arrayOfUsers = new ArrayList<User>();
        arrayOfUsers = db.getAllUsersAsUser();

        // Array of choices
        String users[] = new String[arrayOfUsers.size()];

        for (int x = 0; x < arrayOfUsers.size(); x++) {
            users[x] = arrayOfUsers.get(x).getName();
        }


// Selection of the spinner
        spinner = (Spinner) findViewById(R.id.spUser);

// Application of the Array to the Spinner

        ArrayAdapter<User> adapter = new ArrayAdapter<User>(getApplicationContext(), R.layout.spinner_user_item, arrayOfUsers);
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

        if (currentUser.getPassword().toString().equals(currentPass)) {
            Intent iGoToWarehouseList = new Intent(getApplicationContext(), ProductListActivity.class);

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
