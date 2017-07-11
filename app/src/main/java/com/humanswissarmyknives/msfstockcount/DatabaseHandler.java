package com.humanswissarmyknives.msfstockcount;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by dennisvocke on 10.07.17.
 */

class DatabaseHandler extends SQLiteOpenHelper {



    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "stockcount";

    private static final String TABLE_PRODUCTS = "Product";
    private static final String KEY_PRODUCT_ID = "id";
    private static final String KEY_PRODUCT_CODE = "code";
    private static final String KEY_PRODUCT_DESCRIPTION = "description";
    private static final String KEY_PRODUCT_SUD = "sud";
    private static final String KEY_PRODUCT_BATCHMANAGED = "is_batch_managed";

    private static final String TABLE_BATCHES = "Batch";
    private static final String KEY_BATCH_ID = "id";
    //private static final String KEY_PRODUCT_CODE = "product_code";        already defined
    private static final String KEY_BATCH_NUMBER = "batch_number";
    private static final String KEY_BATCH_EXPDATE = "expiry_date";
    private static final String KEY_BATCH_SUD = "sud";

    private static final String TABLE_COUNTEDITEMS = "CountedItem";
    private static final String KEY_COUNTED_ID = "id";
    private static final String KEY_COUNTED_PRODUCT_CODE = "product_code";
    private static final String KEY_COUNTED_BATCH_ID = "batch_id";
    private static final String KEY_COUNTED_TOTALQTY = "total_qty";
    private static final String KEY_COUNTED_USER_ID = "user_id";
    private static final String KEY_TIMESTAMP = "timestamp";

    private static final String TABLE_USERS = "User";
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_FUNCTION = "function";
    private static final String KEY_USER_LEVEL = "level";
    private static final String KEY_USER_PASSWORD = "password";

    DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BATCHES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTEDITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // tabel for products
        String createDb = "CREATE TABLE "
                + TABLE_PRODUCTS + " ("
                + KEY_PRODUCT_ID + " INTEGER PRIMARY KEY, "
                + KEY_PRODUCT_CODE + " TEXT, "
                + KEY_PRODUCT_DESCRIPTION + " TEXT, "
                + KEY_PRODUCT_SUD + " INTEGER, "
                + KEY_PRODUCT_BATCHMANAGED + " )";
        db.execSQL(createDb);

        //table for batch numbers
        String createBatches = "CREATE TABLE "
                + TABLE_BATCHES + " ("
                + KEY_BATCH_ID + " INTEGER PRIMARY KEY, "
                + KEY_PRODUCT_CODE + " TEXT, "
                + KEY_BATCH_NUMBER + " TEXT, "
                + KEY_BATCH_EXPDATE + " TEXT, "
                + KEY_BATCH_SUD + " INTEGER, FOREIGN KEY ("
                + KEY_PRODUCT_CODE + ") REFERENCES "
                + TABLE_PRODUCTS + " ("
                + KEY_PRODUCT_CODE + ") ON UPDATE CASCADE)";
        db.execSQL(createBatches);

        String createCountedItems = "CREATE TABLE "
                + TABLE_COUNTEDITEMS + " ("
                + KEY_COUNTED_ID + " INTEGER PRIMARY KEY, "
                + KEY_COUNTED_PRODUCT_CODE + " TEXT, "
                + KEY_COUNTED_BATCH_ID + " TEXT, "
                + KEY_COUNTED_TOTALQTY + " INTEGER, "
                + KEY_COUNTED_USER_ID + " TEXT, "
                + KEY_TIMESTAMP + " DATETIME, FOREIGN KEY ("
                + KEY_COUNTED_PRODUCT_CODE + ") REFERENCES "
                + TABLE_PRODUCTS + " ("
                + KEY_PRODUCT_CODE + ") ON UPDATE CASCADE, FOREIGN KEY ("
                + KEY_COUNTED_BATCH_ID + ") REFERENCES "
                + TABLE_BATCHES + " ("
                + KEY_BATCH_ID + ") ON UPDATE CASCADE, FOREIGN KEY ("
                + KEY_COUNTED_USER_ID + ") REFERENCES "
                + TABLE_USERS + " ("
                + KEY_USER_ID + ") ON UPDATE CASCADE)";
        db.execSQL(createCountedItems);

        String createUsers = "CREATE TABLE "
                + TABLE_USERS + " ("
                + KEY_USER_ID + " INTEGER PRIMARY KEY, "
                + KEY_USER_NAME + " TEXT, "
                + KEY_USER_FUNCTION + " TEXT, "
                + KEY_USER_LEVEL + " TEXT, "
                + KEY_USER_PASSWORD + " TEXT )";
        db.execSQL(createUsers);
    }

    void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        int nextId = getMaxProductId() + 1;

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_ID, nextId);
        values.put(KEY_PRODUCT_CODE, product.getProduct_code());
        values.put(KEY_PRODUCT_DESCRIPTION, product.getProduct_description());
        values.put(KEY_PRODUCT_SUD, product.getProduct_sud());

        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    void addCountedItem(CountedItem countedItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        int nextId = getMaxCountItemId() + 1;

        ContentValues values = new ContentValues();
        values.put(KEY_COUNTED_ID, nextId);
        values.put(KEY_COUNTED_PRODUCT_CODE, countedItem.getProduct_code());
        values.put(KEY_COUNTED_BATCH_ID, countedItem.getBatchNumber_id());
        values.put(KEY_COUNTED_TOTALQTY, countedItem.getCountedQty());
        values.put(KEY_COUNTED_USER_ID, countedItem.getUser_id());

        db.insert(TABLE_COUNTEDITEMS, null, values);
        db.close();

    }

    void addBatch(Batch batch) {
        SQLiteDatabase db = this.getWritableDatabase();

        int nextId = getMaxBatchId() + 1;

        ContentValues values = new ContentValues();
        values.put(KEY_BATCH_ID, nextId);
        values.put(KEY_PRODUCT_CODE, batch.getProduct_code());
        values.put(KEY_BATCH_NUMBER, batch.getBatch_number());
        values.put(KEY_BATCH_EXPDATE, String.valueOf(batch.getExpiry_date()));
        values.put(KEY_BATCH_SUD, batch.getBatch_sud());

        db.insert(TABLE_BATCHES, null, values);
        db.close();

    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        int nextId = getMaxUserId() + 1;

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, nextId);
        values.put(KEY_USER_NAME, user.getName());
        values.put(KEY_USER_FUNCTION, user.getFunction());
        values.put(KEY_USER_LEVEL, user.getLevel());
        values.put(KEY_USER_PASSWORD, user.getPassword());

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    int updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_CODE, product.getProduct_code());
        values.put(KEY_PRODUCT_DESCRIPTION, product.getProduct_description());
        values.put(KEY_PRODUCT_SUD, product.getProduct_sud());

        return db.update(TABLE_PRODUCTS, values, KEY_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(product.getProduct_id())});
    }

    int updateCountedItem(CountedItem countedItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COUNTED_ID, countedItem.getId());
        values.put(KEY_COUNTED_PRODUCT_CODE, countedItem.getProduct_code());
        values.put(KEY_COUNTED_BATCH_ID, countedItem.getBatchNumber_id());
        values.put(KEY_COUNTED_TOTALQTY, countedItem.getCountedQty());
        values.put(KEY_COUNTED_USER_ID, countedItem.getUser_id());

        return db.update(TABLE_COUNTEDITEMS, values, KEY_COUNTED_ID + " = ?",
                new String[]{String.valueOf(countedItem.getId())});
    }

    int updateBatch(Batch batch) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_CODE, batch.getProduct_code());
        values.put(KEY_BATCH_NUMBER, batch.getBatch_number());
        values.put(KEY_BATCH_EXPDATE, String.valueOf(batch.getExpiry_date()));
        values.put(KEY_BATCH_SUD, batch.getBatch_sud());

        return db.update(TABLE_BATCHES, values, KEY_BATCH_ID + " =?",
                new String[]{String.valueOf(batch.getBatch_id())});
    }

    int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, user.getName());
        values.put(KEY_USER_FUNCTION, user.getFunction());
        values.put(KEY_USER_LEVEL, user.getLevel());
        values.put(KEY_USER_PASSWORD, user.getPassword());

        return db.update(TABLE_USERS, values, KEY_USER_ID + " =?",
                new String[]{String.valueOf(user.getId())});
    }

    Product getProductByCode(String code) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_PRODUCTS,
                new String[]{KEY_PRODUCT_ID, KEY_PRODUCT_CODE, KEY_PRODUCT_DESCRIPTION, KEY_PRODUCT_SUD, KEY_PRODUCT_BATCHMANAGED}, KEY_PRODUCT_CODE + "=?",
                new String[]{String.valueOf(code)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Product product = new Product(
                cursor.getInt(cursor.getColumnIndex(KEY_PRODUCT_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_CODE)),
                cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_DESCRIPTION)),
                cursor.getInt(cursor.getColumnIndex(KEY_PRODUCT_SUD)),
                cursor.getInt(cursor.getColumnIndex(KEY_PRODUCT_BATCHMANAGED)));
        cursor.close();
        return product;
    }

    CountedItem getCountedItemById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COUNTEDITEMS,
                new String[]{KEY_COUNTED_ID, KEY_COUNTED_PRODUCT_CODE, KEY_COUNTED_BATCH_ID, KEY_COUNTED_TOTALQTY, KEY_COUNTED_USER_ID}, KEY_COUNTED_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        CountedItem countedItem = new CountedItem(
                cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_COUNTED_PRODUCT_CODE)),
                cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_BATCH_ID)),
                cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_TOTALQTY)),
                cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_USER_ID)));
        cursor.close();

        Log.i("id", String.valueOf(countedItem.getId()));
        return countedItem;
    }

    Batch getBatchById(int batchId) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_BATCHES,
                new String[]{KEY_BATCH_ID, KEY_PRODUCT_CODE, KEY_BATCH_NUMBER, KEY_BATCH_EXPDATE, KEY_BATCH_SUD}, KEY_BATCH_ID + " =?",
                new String[]{String.valueOf(batchId)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Batch batch = new Batch(
                cursor.getInt(cursor.getColumnIndex(KEY_BATCH_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_CODE)),
                cursor.getString(cursor.getColumnIndex(KEY_BATCH_NUMBER)),
                cursor.getString(cursor.getColumnIndex(KEY_BATCH_EXPDATE)),
                cursor.getInt(cursor.getColumnIndex(KEY_BATCH_SUD)));
        cursor.close();
        return batch;
    }

    User getUserById(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_USERS,
                new String[]{KEY_USER_ID, KEY_USER_NAME, KEY_USER_FUNCTION, KEY_USER_LEVEL, KEY_USER_PASSWORD}, KEY_USER_ID + " =?",
                new String[]{String.valueOf(userId)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        User user = new User(
                cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)),
                cursor.getString(cursor.getColumnIndex(KEY_USER_FUNCTION)),
                cursor.getString(cursor.getColumnIndex(KEY_USER_LEVEL)),
                cursor.getString(cursor.getColumnIndex(KEY_USER_PASSWORD)));
        cursor.close();
        return user;
    }

    User getUserByName(String userName) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_USERS,
                new String[]{KEY_USER_ID, KEY_USER_NAME, KEY_USER_FUNCTION, KEY_USER_LEVEL, KEY_USER_PASSWORD}, KEY_USER_NAME + " =?",
                new String[]{String.valueOf(userName)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        User user = new User(
                cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)),
                cursor.getString(cursor.getColumnIndex(KEY_USER_FUNCTION)),
                cursor.getString(cursor.getColumnIndex(KEY_USER_LEVEL)),
                cursor.getString(cursor.getColumnIndex(KEY_USER_PASSWORD)));
        cursor.close();
        return user;
    }

    ArrayList<Product> getAllProductsAsProduct() {
        ArrayList<Product> productList = new ArrayList<Product>();

        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndex(KEY_PRODUCT_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_CODE)),    // code
                        cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_DESCRIPTION)),    // Description
                        cursor.getInt(cursor.getColumnIndex(KEY_PRODUCT_SUD)),
                        cursor.getInt(cursor.getColumnIndex(KEY_PRODUCT_BATCHMANAGED)));      // sud
                productList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return productList;
    }

    ArrayList<Batch> getAllBatchesAsBatchByProductCode(String productCode) {
        ArrayList<Batch> batchList = new ArrayList<Batch>();

        String selectQuery = "SELECT * FROM " + TABLE_BATCHES + " WHERE " + KEY_PRODUCT_CODE + " = '" + productCode + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Batch batch = new Batch(
                        cursor.getInt(cursor.getColumnIndex(KEY_BATCH_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_CODE)),    // code
                        cursor.getString(cursor.getColumnIndex(KEY_BATCH_NUMBER)),    // Description
                        cursor.getString(cursor.getColumnIndex(KEY_BATCH_EXPDATE)),
                        cursor.getInt(cursor.getColumnIndex(KEY_BATCH_SUD)));      // sud
                batchList.add(batch);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return batchList;
    }

    ArrayList<Batch> getAllCountedBatchesAsBatchByProductCode(String productCode) {
        ArrayList<Batch> batchList = new ArrayList<Batch>();

        String selectQuery = "SELECT B." +
                KEY_BATCH_ID + ", " +
                //KEY_COUNTED_ID + ", " +
                KEY_PRODUCT_CODE + ", " +
                KEY_BATCH_NUMBER + ", " +
                KEY_BATCH_EXPDATE + ", " +
                KEY_BATCH_SUD + " " +
                "FROM " + TABLE_BATCHES + " AS B JOIN " + TABLE_COUNTEDITEMS + " AS C " +
                "WHERE " + KEY_PRODUCT_CODE + " = '" + productCode + "' AND B." + KEY_BATCH_ID + " = C." + KEY_COUNTED_BATCH_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Batch batch = new Batch(
                        cursor.getInt(cursor.getColumnIndex(KEY_BATCH_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_CODE)),    // code
                        cursor.getString(cursor.getColumnIndex(KEY_BATCH_NUMBER)),    // Description
                        cursor.getString(cursor.getColumnIndex(KEY_BATCH_EXPDATE)),
                        cursor.getInt(cursor.getColumnIndex(KEY_BATCH_SUD)));      // sud
                batchList.add(batch);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return batchList;
    }

    ArrayList<CountedItem> getAllCountedItemsAsBatchByProductCode(String productCode) {
        ArrayList<CountedItem> batchList = new ArrayList<CountedItem>();

        String selectQuery = "SELECT C." +
                KEY_COUNTED_BATCH_ID + ", C." +
                KEY_COUNTED_ID + ", " +
                KEY_PRODUCT_CODE + ", " +
                KEY_COUNTED_TOTALQTY + ", " +
                KEY_BATCH_EXPDATE + ", " +
                KEY_COUNTED_USER_ID + " " +
                "FROM " + TABLE_BATCHES + " AS B JOIN " + TABLE_COUNTEDITEMS + " AS C " +
                "WHERE " + KEY_PRODUCT_CODE + " = '" + productCode + "' AND B." + KEY_BATCH_ID + " = C." + KEY_COUNTED_BATCH_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CountedItem batch = new CountedItem(
                        cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_CODE)),    // code
                        cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_BATCH_ID)),    // Description
                        cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_TOTALQTY)),
                        cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
                batchList.add(batch);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return batchList;
    }

    ArrayList<User> getAllUsersAsUser() {
        ArrayList<User> userList = new ArrayList<User>();

        String selectQuery = "SELECT * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)),    // code
                        cursor.getString(cursor.getColumnIndex(KEY_USER_FUNCTION)),    // Description
                        cursor.getString(cursor.getColumnIndex(KEY_USER_LEVEL)),
                        cursor.getString(cursor.getColumnIndex(KEY_USER_PASSWORD)));      // sud
                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return userList;
    }

    void deleteProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, KEY_PRODUCT_ID + " = ?", new String[]{String.valueOf(product.getProduct_id())});
        db.close();
    }

    void deleteBatch(Batch batch) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BATCHES, KEY_BATCH_ID + " =?", new String[]{String.valueOf(batch.getBatch_id())});
        db.close();
    }

    void deleteCountedItem(CountedItem countedItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COUNTEDITEMS, KEY_COUNTED_ID + " =?", new String[]{String.valueOf(countedItem.getId())});
        db.close();
    }

    void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_USER_ID + " =?", new String[]{String.valueOf(user.getId())});
        db.close();
    }

    int getMaxProductId() {
        int maxId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX( " + KEY_PRODUCT_ID + " ) FROM " + TABLE_PRODUCTS, null);

        cursor.moveToLast();
        maxId = cursor.getInt(0);

        cursor.close();
        return maxId;
    }

    int getMaxCountItemId() {
        int maxId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX( " + KEY_COUNTED_ID + " ) FROM " + TABLE_COUNTEDITEMS, null);

        cursor.moveToLast();
        maxId = cursor.getInt(0);

        cursor.close();
        return maxId;
    }

    int getTotalQtyCountItemByProductCode(String product_code) {
        int totalQty;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SUM( " + KEY_COUNTED_TOTALQTY + " ) " +
                        "AS " + KEY_COUNTED_TOTALQTY + " " +
                        "FROM " + TABLE_COUNTEDITEMS + " " +
                        "WHERE " + KEY_COUNTED_PRODUCT_CODE + " = '" + product_code + "'", null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        totalQty = cursor.getInt(0);

        cursor.close();
        return totalQty;
    }

    int getBatchQtyCountItemByBatchId(int batchId) {
        int totalQty;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SUM( " + KEY_COUNTED_TOTALQTY + " ) " +
                        "AS " + KEY_COUNTED_TOTALQTY + " " +
                        "FROM " + TABLE_COUNTEDITEMS + " " +
                        "WHERE " + KEY_COUNTED_BATCH_ID + " = '" + batchId + "'", null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        totalQty = cursor.getInt(0);

        cursor.close();

        return totalQty;

    }

    int getMaxBatchId() {
        int maxId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX( " + KEY_BATCH_ID + " ) FROM " + TABLE_BATCHES, null);

        cursor.moveToLast();
        maxId = cursor.getInt(0);

        cursor.close();
        return maxId;
    }

    int getMaxUserId() {
        int maxId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX( " + KEY_USER_ID + " ) FROM " + TABLE_USERS, null);

        cursor.moveToLast();
        maxId = cursor.getInt(0);

        cursor.close();
        return maxId;
    }

}
