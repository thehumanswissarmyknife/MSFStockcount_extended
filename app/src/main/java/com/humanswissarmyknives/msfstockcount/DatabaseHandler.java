package com.humanswissarmyknives.msfstockcount;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.net.FileNameMap;
import java.util.ArrayList;

/**
 * Created by dennisvocke on 10.07.17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {


    //region KEYWORDS
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "stockcount";

    private static final String TABLE_PRODUCTS = "Product";
    private static final String KEY_PRODUCT_ID = "id";
    private static final String KEY_PRODUCT_CODE = "code";
    private static final String KEY_PRODUCT_DESCRIPTION = "description";
    private static final String KEY_PRODUCT_SUD = "sud";
    private static final String KEY_PRODUCT_BATCHMANAGED = "is_batch_managed";

    // add old codes!!!!!!!

    private static final String TABLE_BATCHES = "Batch";
    private static final String KEY_BATCH_ID = "id";
    private static final String KEY_SERVER_BATCH_ID = "server_id";
    //private static final String KEY_PRODUCT_CODE = "product_code";        already defined
    private static final String KEY_BATCH_NUMBER = "batch_number";
    private static final String KEY_BATCH_EXPDATE = "expiry_date";
    private static final String KEY_BATCH_SUD = "sud";

    private static final String TABLE_COUNTEDITEMS = "CountedItem";
    private static final String KEY_COUNTED_ID = "id";
    private static final String KEY_SERVER_COUNTED_ID = "server_id";
    private static final String KEY_COUNTED_PRODUCT_CODE = "product_code";
    private static final String KEY_COUNTED_BATCH_ID = "batch_id";
    private static final String KEY_COUNTED_TOTALQTY = "total_qty";
    private static final String KEY_COUNTED_USER_ID = "user_id";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_COUNTED_SUD = "sud";

    private static final String TABLE_USERS = "User";
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_FUNCTION = "function";
    private static final String KEY_USER_LEVEL = "level";
    private static final String KEY_USER_PASSWORD = "password";

    private static final String TABLE_WAREHOUSES = "Warehouse";
    private static final String KEY_WAREHOUSE_ID = "id";
    private static final String KEY_WAREHOUSE_NAME = "warehousename";
    private static final String KEY_WAREHOUSE_CATEGORY = "category";
    private static final String KEY_WAREHOUSE_LIST_ID = "list_id";

    private static final String TABLE_REPORTINGLISTS = "Reportinglist";
    private static final String KEY_LIST_ID = "id";
    private static final String KEY_LIST_NAME = "reportinglistname";
    private static final String KEY_LIST_COMMENT = "comment";
    private static final String KEY_LIST_CATEGORY = "category";

    private static final String TABLE_PROD_LIST_REF = "Product_List_Reference";
    private static final String KEY_PL_ID = "id";
    private static final String KEY_PL_PRODUCT_CODE = "product_code";
    private static final String KEY_PL_LIST_ID = "list_id";
    //endregion

    DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BATCHES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTEDITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WAREHOUSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTINGLISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROD_LIST_REF);
        onCreate(db);
    }

    public void dropTables(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BATCHES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTEDITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WAREHOUSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTINGLISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROD_LIST_REF);
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
                + KEY_PRODUCT_BATCHMANAGED + " TEXT)";
        db.execSQL(createDb);

        //table for batch numbers
        String createBatches = "CREATE TABLE "
                + TABLE_BATCHES + " ("
                + KEY_BATCH_ID + " INTEGER PRIMARY KEY, "
                + KEY_SERVER_BATCH_ID + " TEXT, "
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
                + KEY_SERVER_COUNTED_ID + " TEXT, "
                + KEY_COUNTED_PRODUCT_CODE + " TEXT, "
                + KEY_COUNTED_BATCH_ID + " TEXT, "
                + KEY_COUNTED_TOTALQTY + " INTEGER, "
                + KEY_COUNTED_USER_ID + " TEXT, "
                + KEY_COUNTED_SUD + " INTEGER, "
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

        String createWarehouse = "CREATE TABLE "
                + TABLE_WAREHOUSES + " ("
                + KEY_WAREHOUSE_ID + " INTEGER PRIMARY KEY, "
                + KEY_WAREHOUSE_NAME + " TEXT, "
                + KEY_WAREHOUSE_CATEGORY + " TEXT, "
                + KEY_WAREHOUSE_LIST_ID + " INTEGER )";
        db.execSQL(createWarehouse);

        String createReportingList = "CREATE TABLE "
                + TABLE_REPORTINGLISTS + " ("
                + KEY_LIST_ID + " INTEGER PRIMARY KEY, "
                + KEY_LIST_NAME + " TEXT, "
                + KEY_LIST_COMMENT + " TEXT, "
                + KEY_LIST_CATEGORY + " TEXT )";
        db.execSQL(createReportingList);

        String createReferenceTable = "CREATE TABLE "
                + TABLE_PROD_LIST_REF + " ("
                + KEY_PL_ID + " INTEGER, "
                + KEY_PL_PRODUCT_CODE + " TEXT, "
                + KEY_PL_LIST_ID + " TEXT, FOREIGN KEY ("
                + KEY_PL_LIST_ID + ") REFERENCES "
                + TABLE_PROD_LIST_REF + " ("
                + KEY_LIST_ID + ") ON UPDATE CASCADE, FOREIGN KEY ("
                + KEY_PL_PRODUCT_CODE + ") REFERENCES "
                + TABLE_PRODUCTS + " ("
                + KEY_PRODUCT_CODE + ") ON UPDATE CASCADE)";
        db.execSQL(createReferenceTable);
    }

    //region Products
    void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (product.getProduct_id() == 0) {
            product.setProduct_id(getMaxProductId() + 1);
        }


        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_ID, product.getProduct_id());
        values.put(KEY_PRODUCT_CODE, product.getProduct_code());
        values.put(KEY_PRODUCT_DESCRIPTION, product.getProduct_description());
        values.put(KEY_PRODUCT_SUD, product.getProduct_sud());

        db.insert(TABLE_PRODUCTS, null, values);
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
                cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_BATCHMANAGED)));
        cursor.close();
        return product;
    }

    ArrayList<Product> getAllProductsAsProduct() {
        ArrayList<Product> productList = new ArrayList<>();

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
                        cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_BATCHMANAGED)));      // sud
                productList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return productList;
    }

    ArrayList<Product> getProductsInAList(int list_id) {
        ArrayList<Product> productList = new ArrayList<>();

        String selectQuery = "SELECT * "
                + "FROM (" + TABLE_PRODUCTS + " p "
                + "INNER JOIN " + TABLE_PROD_LIST_REF + " r ON p." + KEY_PRODUCT_CODE + " = r." + KEY_PL_PRODUCT_CODE + ") "
                + "INNER JOIN " + TABLE_REPORTINGLISTS + " l ON l." + KEY_LIST_ID + " = r." + KEY_PL_LIST_ID
                + " WHERE l." + KEY_LIST_ID + " = '" + list_id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndex(KEY_PRODUCT_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_CODE)),    // code
                        cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_DESCRIPTION)),    // Description
                        cursor.getInt(cursor.getColumnIndex(KEY_PRODUCT_SUD)),
                        cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_BATCHMANAGED)));      // sud
                productList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return productList;
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

    void deleteProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, KEY_PRODUCT_ID + " = ?", new String[]{String.valueOf(product.getProduct_id())});
        db.close();
    }
    //endregion

    // region Counted items
    void addCountedItem(CountedItem countedItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (countedItem.getId() == 0) {
            countedItem.setId(getMaxCountItemId() + 1);
        }

        ContentValues values = new ContentValues();
        values.put(KEY_COUNTED_ID, countedItem.getId());
        values.put(KEY_SERVER_COUNTED_ID, countedItem.getServerId());
        values.put(KEY_COUNTED_PRODUCT_CODE, countedItem.getProduct_code());
        values.put(KEY_COUNTED_BATCH_ID, countedItem.getBatchNumber_id());
        values.put(KEY_COUNTED_TOTALQTY, countedItem.getCountedQty());
        values.put(KEY_COUNTED_USER_ID, countedItem.getUser_id());
        values.put(KEY_COUNTED_SUD, countedItem.getSud());

        db.insert(TABLE_COUNTEDITEMS, null, values);
        db.close();

    }

    int updateCountedItem(CountedItem countedItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COUNTED_ID, countedItem.getId());
        values.put(KEY_SERVER_COUNTED_ID, countedItem.getServerId());
        values.put(KEY_COUNTED_PRODUCT_CODE, countedItem.getProduct_code());
        values.put(KEY_COUNTED_BATCH_ID, countedItem.getBatchNumber_id());
        values.put(KEY_COUNTED_TOTALQTY, countedItem.getCountedQty());
        values.put(KEY_COUNTED_USER_ID, countedItem.getUser_id());
        values.put(KEY_COUNTED_SUD, countedItem.getSud());

        return db.update(TABLE_COUNTEDITEMS, values, KEY_COUNTED_ID + " = ?",
                new String[]{String.valueOf(countedItem.getId())});
    }

    CountedItem getCountedItemById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COUNTEDITEMS,
                new String[]{KEY_COUNTED_ID, KEY_SERVER_COUNTED_ID, KEY_COUNTED_PRODUCT_CODE, KEY_COUNTED_BATCH_ID, KEY_COUNTED_TOTALQTY, KEY_COUNTED_USER_ID, KEY_COUNTED_SUD}, KEY_COUNTED_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        CountedItem countedItem = new CountedItem(
                cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_SERVER_BATCH_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_COUNTED_PRODUCT_CODE)),
                cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_BATCH_ID)),
                cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_TOTALQTY)),
                cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_USER_ID)),
                cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_SUD)));
        cursor.close();

        Log.i("id", String.valueOf(countedItem.getId()));
        return countedItem;
    }

    CountedItem getCountedItemByProductCode(String productCode) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COUNTEDITEMS,
                new String[]{KEY_COUNTED_ID, KEY_SERVER_BATCH_ID, KEY_COUNTED_PRODUCT_CODE, KEY_COUNTED_BATCH_ID, KEY_COUNTED_TOTALQTY, KEY_COUNTED_USER_ID, KEY_COUNTED_SUD}, KEY_COUNTED_PRODUCT_CODE + " = ?",
                new String[]{String.valueOf(productCode)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        CountedItem countedItem = new CountedItem(
                cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_SERVER_BATCH_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_COUNTED_PRODUCT_CODE)),
                cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_BATCH_ID)),
                cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_TOTALQTY)),
                cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_USER_ID)),
                cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_SUD)));
        cursor.close();

        Log.i("id", String.valueOf(countedItem.getId()));
        return countedItem;
    }

    int getNumberOfCountedItemByProductCode(String productCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) AS COUNT FROM " + TABLE_COUNTEDITEMS + " WHERE " + KEY_COUNTED_PRODUCT_CODE + " = '" + productCode + "'", null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        int count = cursor.getInt(0);

        cursor.close();

        return count;
    }

    ArrayList<CountedItem> getAllCountedItemsAsCountedItemByProductCode(String productCode) {
        ArrayList<CountedItem> itemList = new ArrayList<>();

        String selectQuery = "SELECT C." +
                KEY_COUNTED_BATCH_ID + ", C." +
                KEY_COUNTED_ID + " , C." +
                KEY_SERVER_COUNTED_ID + " , " +
                KEY_PRODUCT_CODE + " , " +
                KEY_COUNTED_TOTALQTY + ", " +
                KEY_BATCH_EXPDATE + ", " +
                KEY_COUNTED_USER_ID + ", C." +
                KEY_COUNTED_SUD +
                " FROM " + TABLE_BATCHES + " AS B JOIN " +
                TABLE_COUNTEDITEMS + " AS C " +
                "WHERE " + KEY_PRODUCT_CODE + " = '" + productCode +
                "' AND B." + KEY_BATCH_ID + " = C." +
                KEY_COUNTED_BATCH_ID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        if (cursor.moveToFirst()) {
            do {
                CountedItem countedItem = new CountedItem(
                        cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_SERVER_COUNTED_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_CODE)),    // code
                        cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_BATCH_ID)),    // Description
                        cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_TOTALQTY)),
                        cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)),
                        cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_SUD)));
                itemList.add(countedItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return itemList;
    }

    ArrayList<CountedItem> getAllCountedItemsAsCountedItemsByProductCodeNoBatches(String productCode) {
        ArrayList<CountedItem> itemList = new ArrayList<>();

        String selectQuery = "SELECT " +
                KEY_COUNTED_ID + " , " +
                KEY_SERVER_COUNTED_ID + " , " +
                KEY_COUNTED_PRODUCT_CODE + " , " +
                KEY_COUNTED_TOTALQTY + ", " +
                KEY_COUNTED_USER_ID +
                " FROM " + TABLE_COUNTEDITEMS +
                "WHERE " + KEY_PRODUCT_CODE + " = '" + productCode +
                "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        if (cursor.moveToFirst()) {
            do {
                CountedItem countedItem = new CountedItem(
                        cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_SERVER_COUNTED_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_CODE)),    // code
                        cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_BATCH_ID)),    // Description
                        cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_TOTALQTY)),
                        cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)),
                        cursor.getInt(cursor.getColumnIndex(KEY_COUNTED_SUD)));
                itemList.add(countedItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return itemList;
    }

    void deleteCountedItem(CountedItem countedItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COUNTEDITEMS, KEY_COUNTED_ID + " =?", new String[]{String.valueOf(countedItem.getId())});
        db.close();
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

    // endregion

// region Batches

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

    int updateBatch(Batch batch) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SERVER_BATCH_ID, batch.getServerBatchId());
        values.put(KEY_PRODUCT_CODE, batch.getProduct_code());
        values.put(KEY_BATCH_NUMBER, batch.getBatch_number());
        values.put(KEY_BATCH_EXPDATE, String.valueOf(batch.getExpiry_date()));
        values.put(KEY_BATCH_SUD, batch.getBatch_sud());

        return db.update(TABLE_BATCHES, values, KEY_BATCH_ID + " =?",
                new String[]{String.valueOf(batch.getBatch_id())});
    }

    Batch getBatchById(int batchId) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_BATCHES,
                new String[]{KEY_BATCH_ID, KEY_SERVER_BATCH_ID, KEY_PRODUCT_CODE, KEY_BATCH_NUMBER, KEY_BATCH_EXPDATE, KEY_BATCH_SUD}, KEY_BATCH_ID + " =?",
                new String[]{String.valueOf(batchId)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Batch batch = new Batch(
                cursor.getInt(cursor.getColumnIndex(KEY_BATCH_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_SERVER_BATCH_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_CODE)),
                cursor.getString(cursor.getColumnIndex(KEY_BATCH_NUMBER)),
                cursor.getString(cursor.getColumnIndex(KEY_BATCH_EXPDATE)),
                cursor.getInt(cursor.getColumnIndex(KEY_BATCH_SUD)));
        cursor.close();
        return batch;
    }

    ArrayList<Batch> getAllBatchesAsBatchByProductCode(String productCode) {
        ArrayList<Batch> batchList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_BATCHES + " WHERE " + KEY_PRODUCT_CODE + " = '" + productCode + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Batch batch = new Batch(
                        cursor.getInt(cursor.getColumnIndex(KEY_BATCH_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_SERVER_BATCH_ID)),
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
        ArrayList<Batch> batchList = new ArrayList<>();

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
                        cursor.getString(cursor.getColumnIndex(KEY_SERVER_BATCH_ID)),
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

    void deleteBatch(Batch batch) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BATCHES, KEY_BATCH_ID + " =?", new String[]{String.valueOf(batch.getBatch_id())});
        db.close();
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


// endregion

// region User

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (user.getId() == 0) {
            user.setId(getMaxUserId() + 1);
        }


        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, user.getId());
        values.put(KEY_USER_NAME, user.getName());
        values.put(KEY_USER_FUNCTION, user.getFunction());
        values.put(KEY_USER_LEVEL, user.getLevel());
        values.put(KEY_USER_PASSWORD, user.getPassword());

        db.insert(TABLE_USERS, null, values);
        db.close();
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

    ArrayList<User> getAllUsersAsUser() {
        ArrayList<User> userList = new ArrayList<>();

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

    void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_USER_ID + " =?", new String[]{String.valueOf(user.getId())});
        db.close();
    }

    private int getMaxUserId() {
        int maxId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX( " + KEY_USER_ID + " ) FROM " + TABLE_USERS, null);

        cursor.moveToLast();
        maxId = cursor.getInt(0);

        cursor.close();
        return maxId;
    }

// endregion

// region Warehouses

    void addWarehouse(Warehouse warehouse) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (warehouse.getId() == 0) {
            warehouse.setId(getMaxWarehouseId() + 1);
        }

        ContentValues values = new ContentValues();
        values.put(KEY_WAREHOUSE_ID, warehouse.getId());
        values.put(KEY_WAREHOUSE_NAME, warehouse.getName());
        values.put(KEY_WAREHOUSE_CATEGORY, warehouse.getCategory());
        values.put(KEY_WAREHOUSE_LIST_ID, warehouse.getList_id());

        db.insert(TABLE_WAREHOUSES, null, values);
        db.close();
    }

    int updateWarehouse(Warehouse warehouse) {
        SQLiteDatabase db = this.getWritableDatabase();

        int nextId = getMaxWarehouseId();

        ContentValues values = new ContentValues();
        values.put(KEY_WAREHOUSE_ID, nextId);
        values.put(KEY_WAREHOUSE_NAME, warehouse.getName());
        values.put(KEY_WAREHOUSE_CATEGORY, warehouse.getCategory());
        values.put(KEY_WAREHOUSE_LIST_ID, warehouse.getList_id());

        return db.update(TABLE_WAREHOUSES, values, KEY_WAREHOUSE_ID + " =?",
                new String[]{String.valueOf(warehouse.getId())});
    }

    Warehouse getWarehousebyId(int warehouseId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_WAREHOUSES,
                new String[]{KEY_WAREHOUSE_ID, KEY_WAREHOUSE_NAME, KEY_WAREHOUSE_CATEGORY, KEY_WAREHOUSE_LIST_ID}, KEY_WAREHOUSE_ID + " =?",
                new String[]{String.valueOf(warehouseId)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Warehouse warehouse = new Warehouse(
                cursor.getInt(cursor.getColumnIndex(KEY_WAREHOUSE_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_WAREHOUSE_NAME)),
                cursor.getString(cursor.getColumnIndex(KEY_WAREHOUSE_CATEGORY)),
                cursor.getInt(cursor.getColumnIndex(KEY_WAREHOUSE_LIST_ID)));
        cursor.close();
        return warehouse;
    }

    ArrayList<Warehouse> getAllWarehousesAsWarehouse() {
        ArrayList<Warehouse> warehouseList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_WAREHOUSES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Warehouse warehouse = new Warehouse(
                        cursor.getInt(cursor.getColumnIndex(KEY_WAREHOUSE_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_WAREHOUSE_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_WAREHOUSE_CATEGORY)),
                        cursor.getInt(cursor.getColumnIndex(KEY_WAREHOUSE_LIST_ID))
                );
                warehouseList.add(warehouse);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return warehouseList;
    }

    private int getMaxWarehouseId() {
        int maxId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX( " + KEY_WAREHOUSE_ID + " ) FROM " + TABLE_WAREHOUSES, null);

        cursor.moveToLast();
        maxId = cursor.getInt(0);

        cursor.close();
        return maxId;
    }

    // endregion

// region Reportinglissts

    void addReportingList(ReportingList reportingList) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (reportingList.getId() == 0 || getReportingListById(reportingList.id) == null) {
            reportingList.setId(getMaxReportingListId() + 1);
        }

        ContentValues values = new ContentValues();
        values.put(KEY_LIST_ID, reportingList.getId());
        values.put(KEY_LIST_NAME, reportingList.getName());
        values.put(KEY_LIST_CATEGORY, reportingList.getCategory());
        values.put(KEY_LIST_COMMENT, reportingList.getComment());

        db.insert(TABLE_REPORTINGLISTS, null, values);
        db.close();
    }

    int updateReportingList(ReportingList reportingList) {
        SQLiteDatabase db = this.getWritableDatabase();

        int nextId = getMaxReportingListId();
        ContentValues values = new ContentValues();
        values.put(KEY_LIST_ID, nextId);
        values.put(KEY_LIST_NAME, reportingList.getName());
        values.put(KEY_LIST_CATEGORY, reportingList.getCategory());
        values.put(KEY_LIST_COMMENT, reportingList.getComment());

        return db.update(TABLE_REPORTINGLISTS, values, KEY_LIST_ID + " =?",
                new String[]{String.valueOf(reportingList.getId())});
    }

    ReportingList getReportingListById(int reportingListId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REPORTINGLISTS,
                new String[]{KEY_LIST_ID, KEY_LIST_NAME, KEY_LIST_COMMENT, KEY_LIST_CATEGORY}, KEY_LIST_ID + " =?",
                new String[]{String.valueOf(reportingListId)}, null, null, null, null);

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
        } else {
            return null;
        }

        ReportingList reportingList = new ReportingList(
                cursor.getInt(cursor.getColumnIndex(KEY_LIST_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_LIST_NAME)),
                cursor.getString(cursor.getColumnIndex(KEY_LIST_COMMENT)),
                cursor.getString(cursor.getColumnIndex(KEY_LIST_CATEGORY))
        );
        cursor.close();
        return reportingList;
    }

    ArrayList<ReportingList> getAllReportingListsAsReportingLists() {
        ArrayList<ReportingList> reportingListList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_REPORTINGLISTS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ReportingList reportingList = new ReportingList(
                        cursor.getInt(cursor.getColumnIndex(KEY_LIST_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_LIST_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_LIST_COMMENT)),
                        cursor.getString(cursor.getColumnIndex(KEY_LIST_CATEGORY))
                );
                reportingListList.add(reportingList);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reportingListList;
    }

    ArrayList<ReportingList> getAllReportingListsByCategory(String category) {
        ArrayList<ReportingList> reportingListList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_REPORTINGLISTS + " WHERE " + KEY_LIST_CATEGORY + " = '" + category + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ReportingList reportingList = new ReportingList(
                        cursor.getInt(cursor.getColumnIndex(KEY_LIST_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_LIST_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_LIST_COMMENT)),
                        cursor.getString(cursor.getColumnIndex(KEY_LIST_CATEGORY))
                );
                reportingListList.add(reportingList);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reportingListList;
    }

    int getMaxReportingListId() {
        int maxId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX( " + KEY_LIST_ID + " ) FROM " + TABLE_REPORTINGLISTS, null);

        cursor.moveToLast();
        maxId = cursor.getInt(0);

        cursor.close();
        return maxId;
    }

    // endregion


    //region special stuff
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

    int getNumberOfProductsInList(int list_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) AS COUNT FROM " + TABLE_PROD_LIST_REF + " WHERE " + KEY_PL_LIST_ID + " = '" + list_id + "'", null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        int count = cursor.getInt(0);

        cursor.close();

        return count;
    }

    void createProductList(int list_id, String[] products) {
        //String[] products = {"DORAPARA5T-", "DINJCEFT2V-", "DINJCEFT1V-", "DEXTIODP1S2", "DORAFERF14T"};

        for (int i = 0; i < products.length; i++) {
            SQLiteDatabase db = this.getWritableDatabase();


            ContentValues values = new ContentValues();
            values.put(KEY_PL_ID, i);
            values.put(KEY_PL_LIST_ID, list_id);
            values.put(KEY_PL_PRODUCT_CODE, products[i]);

            db.insert(TABLE_PROD_LIST_REF, null, values);
            db.close();
        }
        Log.i("List", String.valueOf(list_id));
        Log.i("Items added", String.valueOf(products.length));
    }

    void createProductList(int list_id, ArrayList products) {
        //String[] products = {"DORAPARA5T-", "DINJCEFT2V-", "DINJCEFT1V-", "DEXTIODP1S2", "DORAFERF14T"};

        for (int i = 0; i < products.size(); i++) {
            SQLiteDatabase db = this.getWritableDatabase();


            ContentValues values = new ContentValues();
            values.put(KEY_PL_ID, i);
            values.put(KEY_PL_LIST_ID, list_id);
            values.put(KEY_PL_PRODUCT_CODE, products.get(i).toString());

            db.insert(TABLE_PROD_LIST_REF, null, values);
            db.close();
        }
        Log.i("List", String.valueOf(list_id));
        Log.i("Items added", String.valueOf(products.size()));
    }

    public void populateDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("BEGIN");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1001, 'ALIFSETSC2-', 'SURVIVAL SET COOKING AND TENT, pour 2 persons', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1002, 'ASTASETSS--', 'MATERIEL DE PAPETERIE MSF-H, boîte, pour start-up', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1003, 'CBUIVERM01-', 'VERMICULITE, granulométrie nº1, pour 100l, sac de 10kg', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1004, 'CBUIVERM03-', 'VERMICULITE, granulométrie nº3, pour 100l, sac de 10kg', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1005, 'CSHEBLANWMD', 'COUVERTURE, en laine min. 40%, 1,5x2m, couleur foncée', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1006, 'CSHEPLASWR4', 'BACHE PLASTIQUE, 4x60m, blanc/blanc, 6 bandes, rouleau', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1007, 'CSHEPLASWS4', 'BACHE PLASTIQUE, 4x6m, blanc/blanc, 6 bandes, feuille', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1008, 'CSHETENM45C', 'TENTE polyvalente, 45m², 6x7,5m + filet à ombre', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1009, 'CWATBEDND0P22', 'MOSQUITO NET deltamethrin (Permanet 2.0) 2 persons', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1010, 'CWATBUCK2FS0L', 'SEAU + COUVERCLE, plastique alimentaire, 20l, empilable', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1011, 'CWATDISIAP1', 'ALUMINIUM sulfate, poudre, le kg', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1012, 'CWATDISING1', 'CHLORE NaDDC, 1kg, granules, pot', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1013, 'CWATDISINTF', 'CHLORE, 5mg, NaDDC 8,5mg, pour désinfecter 1l deau, comp', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1014, 'CWATDISINTF', 'CHLORE, 5mg, NaDDC 8,5mg, pour désinfecter 1l deau, comp', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1015, 'CWATFILTB1-', 'FILTRE A EAU fontaine (Berkefeld) 10l, 4 bougies', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1016, 'CWATFILTB1C', '(Berkefeld10l) BOUGIE, molette + joint, lot', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1017, 'CWATFILTB1G', '(Berkefeld10l) GAUGE', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1018, 'CWATFILTB1T', '(Berkefeld10l) ROBINET', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1019, 'CWATINCIT13', 'THERMOMETRE, jusquà 1370°C, pour incinérateur', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1020, 'CWATINSECF5G', 'INSECTICIDE fipronil (Goliath Gel) 4x35g, cafards + pistolet', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1021, 'CWATINSERA5P', 'INSECTICIDE res. spray. (α-cypermethrin, Fendona) 150g, WP', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1022, 'CWATINSERA6S', 'INSECTICIDE res. spray. (α-cypermethrin, Fendona) 500ml, SC', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1023, 'CWATJERR1FCC', 'NOURRICE pliable, plastique alimentaire, 10l + bouchon vis', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1024, 'CWATJERR2F5', '(nourrice pliable, 20l) ROBINET, 5cm, sans vis', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1025, 'CWATJERR2FCC', 'NOURRICE pliable, plastique alimentaire, 20l + bouchon Ø 5cm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1026, 'CWATJERRFFCT', 'NOURRICE pliable, plastique alimentaire, 15l + robinet', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1027, 'CWATPLUMT3H', '(lavage main) ROBINET, plastic/PVC, 3/4inch + nut&washer', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1028, 'CWATSEALTT9', 'RUBAN (Teflon) 19mmx15m, 0,2mm épaisseur, rouleau', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1029, 'CWATSPRAHX-', 'PULVERISATEUR (Hudson X-pert) 10l', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1030, 'CWATSPRAIB-', 'PULVERISATEUR (IK-12 BS) 12l', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1031, 'CWATSPRAIBR', '(pulvérisateur IK-12 BS) NECESSAIRE DE REPARATION, jeu', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1032, 'CWATTESTCL11', 'COLILERT TEST predispensé (Idexx W100I) 10ml, 100 tubes', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1033, 'CWATTESTLCA3', '(Checkit Com.) BOITE TEST ALUM. complet (147200) 0-0,3mg/l', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1034, 'CWATTESTLCI1R', '(Checkit Com.)TESTPAK (discs, reagents) IRON LR 0.05-1mg/l ', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1035, 'CWATTESTLCIR', '(Checkit Com.) FER GAMME BASSE TABS (515371BT) 250tabs', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1036, 'CWATTESTLP-', 'PISCINE TESTEUR (Lovibond) ', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1037, 'CWATTESTLP1D', '(testeur Lovibond) DPD Nº1 RAPID, pastille', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1038, 'CWATTESTLPPR', '(testeur Lovibond) PHENOL RED RAPID, pastille', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1039, 'CWATTESTT2-', 'TEST DE TURBIDITE tube, plastique, 5-2000 UTN', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1040, 'CWATTESTW5C4', 'PLAQUE DETECTION Escherishia coli (Compact Dry EC) 40 pcs', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1041, 'CWATTESTWSBP', 'BIOASSAY CONE conical chamber (WHO standard) plastic', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1042, 'CWATTOILS2-', 'BLOC LATRINE autoportant, 80x120cm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1043, 'CWATTOILS8-', 'PLAQUE LATRINE, plastique moulé, 59x79cm + couvercle', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1044, 'CWATVECTRB1', 'POSTE DAPPATAGE, pour rat, 12pcs', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1045, 'DEXOCHLO5D1', 'CHLORAMPHENICOL, 0,5%, collyre, stérile, 10 ml, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1046, 'DEXOTETR1O5', 'TETRACYCLINE chlorhydrate, 1%, pommade opht., stér, 5g, tube', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1047, 'DEXTALCO1S-', 'HYDRO-ALCOOLIQUE, solution, 100 ml, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1048, 'DEXTALCO5S-', 'HYDRO-ALCOOLIQUE, solution, 500 ml, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1049, 'DEXTBENS6O4', 'ACIDE BENZOIQUE 6% / ACIDE SALICYLIQUE 3%, pom., 40 g, tube', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1050, 'DEXTBENZ2L1', 'BENZOATE DE BENZYLE, 25%, lotion, 1 l, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1051, 'DEXTCALA1L5', 'CALAMINE, 15%, lotion, 500 ml, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1052, 'DEXTCHLH5S1', 'CHLORHEXIDINE digluconate 5%, solution, 1 l, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1053, 'DEXTCLOT5T-', 'CLOTRIMAZOLE, 500 mg, comp. vaginal + applicateur', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1054, 'DEXTDEET1C-', 'D.E.E.T., lotion répulsive anti-moustique', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1055, 'DEXTHYDR1O1', 'HYDROCORTISONE acétate, 1%, pommade, 15 g, tube', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1056, 'DEXTIODP1S2', 'POLYVIDONE IODEE, 10%, solution, 200 ml, fl. verseur', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1057, 'DEXTLIDO2J3', 'LIDOCAINE, 2%, gel, stérile, tube', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1058, 'DEXTLUBR8J-', 'LUBRIFIANT à usage général, soluble ds leau, gel, 82g, tube', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1059, 'DEXTMICO2C3', 'MICONAZOLE nitrate, 2%, crème, 30 g, tube', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1060, 'DEXTPARA12SU', 'PARACETAMOL, 120mg, suppositoire', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1061, 'DEXTPODO5S3', 'PODOPHYLLOTOXINE, 0,5%, solution, 3,5 ml, + 30 applicateurs', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1062, 'DEXTSOAP1B2', 'SAVON, 200 g, barre', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1063, 'DEXTSULZ1C5', 'SULFADIAZINE ARGENTIQUE, 1%, crème, stérile, 50 g, tube', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1064, 'DEXTYINO1O1', 'OXYDE DE ZINC, 10%, pommade, 100 g, tube', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1065, 'DINFDEXT1FBF5', 'GLUCOSE, 10%, 500 ml, poche souple, sans PVC', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1066, 'DINFDEXT5FBF5', 'GLUCOSE, 5%, 500 ml, poche souple, sans PVC', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1067, 'DINFPLAS1SRF5', 'PLASMA SUBSTITUT, gélatine, 500 ml, fl. semi-rigide, ss PVC', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1068, 'DINFRINL1FBF1', 'RINGER lactate, 1 l, poche souple, sans PVC', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1069, 'DINFRINL1FBF5', 'RINGER lactate, 500 m l, poche souple, sans PVC', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1070, 'DINFSODC9FBF0', 'SODIUM chlorure, 0,9%, 100 ml, poche souple, sans PVC', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1071, 'DINFSODC9FBF5', 'SODIUM chlorure, 0,9%, 500 ml, poche souple, sans PVC', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1072, 'DINJAMBL5V-', 'AMPHOTERICINE B complexe liposomal, 50 mg, poudre, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1073, 'DINJAMOC1V2', 'AMOXICILLINE 1g / acide CLAVULANIQUE 200mg, poudre', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1074, 'DINJAMPI1V-', 'AMPICILLINE, 1 g, poudre, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1075, 'DINJAMPI5V-', 'AMPICILLINE, 500 mg, poudre, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1076, 'DINJARTS6V-', 'ARTESUNATE 60 mg, poudre, fl +NaHCO3  5% 1ml +NaCl 0.9% 5ml', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1077, 'DINJATRO1A-', 'ATROPINE sulfate, 1 mg/ml, 1 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1078, 'DINJCAFC1A-', 'CAFEINE CITRATE, 10 mg/ml, éq. 5 mg caféine base, 1ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1079, 'DINJCALG1A-', 'CALCIUM GLUCONATE, 100 mg/ml, 10 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1080, 'DINJCEFA1V-', 'CEFAZOLINE, 1 g, (IV), poudre, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1081, 'DINJCEFO5V-', 'CEFOTAXIME sodique, éq. 500 mg base, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1082, 'DINJCEFT1V-', 'CEFTRIAXONE sodique, éq. 1 g base,  poudre, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1083, 'DINJCEFT2V-', 'CEFTRIAXONE sodique, éq. 250 mg base, poudre, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1084, 'DINJCHLM5A-', 'CHLORPROMAZINE, 25 mg/ml, 2 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1085, 'DINJCHLO1V-', 'CHLORAMPHENICOL, 1 g, poudre,  fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1086, 'DINJCLIN3A-', 'CLINDAMYCINE phosphate, éq. 150 mg base/ml, 2 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1087, 'DINJCLOX5VV', 'CLOXACILLINE sodique, éq. 500 mg base, poudre, fl. IV', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1088, 'DINJDEXA4A-', 'DEXAMETHASONE phosphate, 4 mg/ml, 1 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1089, 'DINJDIAZ1A-', 'DIAZEPAM, 5 mg/ml, 2 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1090, 'DINJDICL7A-', 'DICLOFENAC sodique, 25 mg/ml, 3 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1091, 'DINJEPHE3A-', 'EPHEDRINE chlorhydrate, 30 mg/ml, 1 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1092, 'DINJEPIN1AV', 'EPINEPHRINE (adrenaline) tartrate,éq.1mg/ml base, 1ml amp IV', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1093, 'DINJFLUC2FBF', 'FLUCONAZOLE, 2 mg/ml, 100 ml, poche souple sans PVC', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1094, 'DINJFURO2A-', 'FUROSEMIDE, 10 mg/ml, 2 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1095, 'DINJGENT8A-', 'GENTAMICINE sulfate, eq. 40 mg/ml base, 2 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1096, 'DINJGLUC5V5', 'GLUCOSE hypertonique, 50%, 50 ml, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1097, 'DINJHALP5A-', 'HALOPERIDOL, 5 mg/ml, 1 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1098, 'DINJHYDA2A-', 'HYDRALAZINE chlorhydrate, 20 mg, poudre, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1099, 'DINJHYDR1V-', 'HYDROCORTISONE succinate sodique, eq.100mg base, poudre,fl', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1100, 'DINJHYOS2A-', 'BUTYLBROMURE HYOSCINE (butylbrom.scopolamine), 20 mg/1ml,amp', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1101, 'DINJINSHI1VN', 'INSULINE HUMAINE, ISOPHANE (NPH) 100 UI/ml, 10 ml, fl. N', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1102, 'DINJINSHR1VN', 'INSULINE HUMAINE, RAPIDE 100 UI/ml, 10 ml, fl. N', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1103, 'DINJKANA1V-', 'KANAMYCINE sulfate, éq. 1 g base, poudre, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1104, 'DINJKETA2A-', 'KETAMINE chlorhydrate, éq. 50 mg/ml base, 5 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1105, 'DINJLABE1A-', 'LABETALOL chlorhydrate, 5 mg/ml, 20 ml amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1106, 'DINJLIDO1A5', 'LIDOCAINE chlorhydrate, 1% ,sans conservateur, 5ml,amp.plast', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1107, 'DINJMAGS5A-', 'MAGNESIUM sulfate, 0,5 g/ml, 10 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1108, 'DINJMEDR1V-', 'MEDROXYPROGESTERONE acétate, 150 mg, 1 ml, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1109, 'DINJMERG2A-', 'METHYLERGOMETRINE maleate, 0,2 mg/ml, 1 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1110, 'DINJMETN5FBF', 'METRONIDAZOLE, 5 mg/ml, 100 ml, flex. bag PVC free', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1111, 'DINJMETO1A-', 'METOCLOPRAMIDE chlorhydrate, 5 mg/ml, 2 ml, amp', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1112, 'DINJMORP1A-', 'MORPHINE chlorhydrate, 10 mg/ml, 1 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1113, 'DINJNALO4A-', 'NALOXONE chlorhydrate, 0,4 mg/ml, 1 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1114, 'DINJOMEP4V-', 'OMEPRAZOLE sodique, éq.40 mg base, poudre, fl. pr perfusion', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1115, 'DINJONDA4A-', 'ONDANSETRON chlorhydrate, éq. 2mg/ml base, 2 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1116, 'DINJOXYT1A-', 'OXYTOCINE, 10 UI/ml, 1 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1117, 'DINJPARA5B-', 'PARACETAMOL (acétaminophène),10 mg/ml, 50 ml, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1118, 'DINJPENB1VS', 'BENZATHINE BENZYLPENICILLINE, 1,2 M  UI, poudre, fl.+solvant', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1119, 'DINJPENB2VS', 'BENZATHINE BENZYLPENICILLINE, 2,4 M  UI, poudre, fl.+solvant', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1120, 'DINJPENG5V-', 'BENZYLPENICILLINE (peni G, cristal peni), 5 MUI, poudre, fl', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1121, 'DINJPHEN2A1', 'PHENOBARBITAL sodique, 200 mg/ml, 1 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1122, 'DINJPHYT2AN', 'PHYTOMENADIONE (vitamine K1), 10mg/ml (2mg/0,2ml),0.2ml amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1123, 'DINJPOTC1A-', 'POTASSIUM chlorure, 100 mg/ml, 10 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1124, 'DINJPROM5A-', 'PROMETHAZINE chlorhydrate, éq. 25 mg/ml base, 2 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1125, 'DINJSALB5A-', 'SALBUTAMOL sulfate, éq. 0,5 mg/ml base, 1 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1126, 'DINJSODB8A2', 'SODIUM BICARBONATE, 8,4%, 1 mEq/ml, 20 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1127, 'DINJSODC9A5', 'SODIUM chlorure, 0,9%, 5 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1128, 'DINJSSGL1V3', 'SODIUM STIBOGLUCONATE, antimoine pentaval. 100mg/ml, 30ml fl', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1129, 'DINJTRAM1A-', 'TRAMADOL chlorhydrate, 50 mg/ml, 2 ml, amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1130, 'DINJWATE1A-', 'EAU pour injection, 10 ml, amp. plastique', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1131, 'DORAABLA1TD', 'ABC 60 mg / 3TC 30 mg, comp. disp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1132, 'DORAACIV2T-', 'ACICLOVIR, 200 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1133, 'DORAACIV8T-', 'ACICLOVIR, 800 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1134, 'DORAACSA3T-', 'Acide ACETYLSALICYLIQUE (aspirine), 300 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1135, 'DORAALBE4T-', 'ALBENDAZOLE, 400 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1136, 'DORAALUM44T', 'ALUMINIUM hydroxyde 400mg / MAGNESIUM hydroxyde 400mg,cp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1137, 'DORAAMIT2T-', 'AMITRIPTYLINE chlorhydrate, 25 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1138, 'DORAAMOC4S5', 'AMOXICILLINE 400mg/ ac.CLAV. 57mg/5ml,poudre susp.orale 70ml', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1139, 'DORAAMOC81T', 'AMOXICILLINE 875mg / ac. CLAVULANIQUE 125mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1140, 'DORAAMOX2T-', 'AMOXICILLINE, 250 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1141, 'DORAAMOX5T-', 'AMOXICILLINE, 500 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1142, 'DORAARLU1TD1', 'AL 20/120 mg, 6 comp. disp., blister, 5-14 kg', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1143, 'DORAARLU2TD1', 'AL 20/120 mg, 12 comp. disp., blister, 15-24 kg', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1144, 'DORAARLU3T1', 'AL 20/120 mg, 18 comp., blister, 25-34 kg', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1145, 'DORAARLU4T1', 'AL 20/120 mg, 24 comp., blister, >35 kg', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1146, 'DORAASCA2T-', 'Acide ASCORBIQUE (vitamine C), 250 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1147, 'DORAATEN5T-', 'ATENOLOL, 50 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1148, 'DORAATOP2T1', 'ATOVAQUONE 250mg / PROGUANIL HCl 100mg, comp, blister,>40 kg', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1149, 'DORAATVR3T-', 'ATV 300 mg / r 100 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1150, 'DORAAZIT1S-', 'AZITHROMYCINE, 200mg/5ml, poudre susp. orale, 15ml, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1151, 'DORAAZIT2T-', 'AZITHROMYCINE, 250 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1152, 'DORABECL1SF', 'BECLOMETASONE dipropionate, 0,10mg/bouffée, 200 b.,aérosol', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1153, 'DORABIPE2T-', 'BIPERIDENE chlorhydrate, 2 mg, comp', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1154, 'DORACALC5TC', 'CALCIUM carbonate, éq. 500mg Ca, comp. à mâcher', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1155, 'DORACARB2T-', 'CARBAMAZEPINE, 200 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1156, 'DORACEFI1S-', 'CEFIXIME, 100mg/5ml, poudre pour susp. orale, 40 ml, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1157, 'DORACEFI2T-', 'CEFIXIME, 200 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1158, 'DORACHLO2C-', 'CHLORAMPHENICOL, 250 mg, gél.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1159, 'DORACHLP4T-', 'CHLORPHENAMINE maléate, 4 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1160, 'DORACIPR5T-', 'CIPROFLOXACINE chlorhydrate, éq. 500 mg base, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1161, 'DORACLIN3C-', 'CLINDAMYCINE chlorhydrate, éq. 300 mg base, gél.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1162, 'DORACLOF1C-', 'CLOFAZIMINE, 100 mg, caps. molle', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1163, 'DORACLOX2C-', 'CLOXACILLINE sodique, éq. 250 mg base, gél.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1164, 'DORACODE3T-', 'CODEINE phosphate, 30 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1165, 'DORACOTR2S1', 'COTRIMOXAZOLE, 200mg/40mg/5ml, susp orale, 100 ml, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1166, 'DORACOTR4T-', 'COTRIMOXAZOLE, 400 mg / 80 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1167, 'DORACOTR8T-', 'COTRIMOXAZOLE, 800 mg / 160 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1168, 'DORADAPS1TB', 'DAPSONE, 100 mg, comp. sécable', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1169, 'DORADIAZ5T-', 'DIAZEPAM, 5 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1170, 'DORADIGO2T-', 'DIGOXINE, 0,25 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1171, 'DORADOXY1T-', 'DOXYCYCLINE sel, éq. 100 mg base, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1172, 'DORAEFAV2C-', 'EFAVIRENZ (EFV), 200 mg, gél.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1173, 'DORAEHZR2T1', 'E 275 mg / H 75 mg / Z 400 mg / R 150 mg, comp., blister', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1174, 'DORAERYT1S1', 'ERYTHROMYCINE ethylsucc,125mg/5ml,poudre susp.orale,100ml,fl', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1175, 'DORAERYT2T-', 'ERYTHROMYCINE stéarate, eq. 250 mg base, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1176, 'DORAERYT5T-', 'ERYTHROMYCINE stéarate, eq. 500 mg base, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1177, 'DORAETHA1T1', 'ETHAMBUTOL chlorhydrate (E), éq. 100 mg base, comp. blister', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1178, 'DORAETHA4T1', 'ETHAMBUTOL chlorhydrate (E), éq. 400 mg base, comp. blister', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1179, 'DORAETHL31T', 'ETHINYLESTR. 0,03mg / LEVONORGESTREL 0,15 mg, plaq. 28 comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1180, 'DORAFERF14T', 'sel de FER éq. 60 mg fer / acide FOLIQUE 0,4 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1181, 'DORAFLUC1S-', 'FLUCONAZOLE, 50mg/5ml, poudre susp. orale, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1182, 'DORAFLUC2C-', 'FLUCONAZOLE, 200 mg, gél.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1183, 'DORAFLUC5C-', 'FLUCONAZOLE, 50 mg, gél.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1184, 'DORAFOLA5T-', 'Acide FOLIQUE, 5 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1185, 'DORAFURO4T-', 'FUROSEMIDE, 40 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1186, 'DORAGLIB5TB', 'GLIBENCLAMIDE, 5 mg, comp. sécable', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1187, 'DORAGRIS1T-', 'GRISEOFULVINE, 125 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1188, 'DORAGRIS5T-', 'GRISEOFULVINE, 500 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1189, 'DORAHALP3D-', 'HALOPERIDOL, 2mg/ml/20 gouttes, 30 ml, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1190, 'DORAHALP5T-', 'HALOPERIDOL, 5 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1191, 'DORAHRIF3TD1', 'H 30 mg / R 60 mg, comp. disp., blister', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1192, 'DORAHRIF7T1', 'H 75 mg / R 150 mg, comp., blister', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1193, 'DORAHYDO5T-', 'HYDROCHLOROTHIAZIDE, 50 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1194, 'DORAHYOS1T-', 'BUTYLBROMURE HYOSCINE (butylbromure scopolamine), 10 mg, cp', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1195, 'DORAHZRI3TD1', 'H 30 mg / Z 150 mg / R 60 mg, comp. disp., blister', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1196, 'DORAIBUP2T-', 'IBUPROFENE, 200 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1197, 'DORAIBUP4T-', 'IBUPROFENE, 400 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1198, 'DORAISOB5T-', 'ISOSORBIDE DINITRATE, 5 mg, comp. sublingual', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1199, 'DORAISON1T1', 'ISONIAZIDE (H), 100 mg, comp. sécable, blister', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1200, 'DORAISON3T1', 'ISONIAZIDE (H), 300 mg, comp., blister', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1201, 'DORAIVER3TS', 'IVERMECTINE (gale + autres indic.), 3 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1202, 'DORALABE1T-', 'LABETALOL chlorhydrate, 100 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1203, 'DORALACT1S-', 'LACTULOSE, min. 3,1g/5ml, sol. orale, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1204, 'DORALEVN1T-', 'LEVONORGESTREL, 1,5 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1205, 'DORALOPE2T-', 'LOPERAMIDE chlorhydrate, 2 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1206, 'DORALPVR2S-', 'LPV / r 400/100mg/5ml, sol.orale, 60 ml, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1207, 'DORALPVR4T-', 'LPV 100 mg / r 25 mg, comp. gastrorésistant', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1208, 'DORALPVR5T-', 'LPV 200 mg / r 50 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1209, 'DORAMEFL2T-', 'MEFLOQUINE chlorhydrate, éq. 250 mg base, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1210, 'DORAMETF5T-', 'METFORMINE chlorhydrate, 500 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1211, 'DORAMETN2S-', 'METRONIDAZOLE benzoate, éq.200mg/5ml base, susp. orale,100ml', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1212, 'DORAMETN2T-', 'METRONIDAZOLE, 250 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1213, 'DORAMETN5T-', 'METRONIDAZOLE, 500 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1214, 'DORAMETO1T-', 'METOCLOPRAMIDE chlorhydrate anhydre, 10 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1215, 'DORAMETY2T-', 'METHYLDOPA, 250 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1216, 'DORAMIFP2T-', 'MIFEPRISTONE, 200 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1217, 'DORAMISP2T-', 'MISOPROSTOL, 200 µg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1218, 'DORAMOXI4T-', 'MOXIFLOXACINE chlorhydrate, éq 400 mg base, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1219, 'DORAMULT1T-', 'MULTIVITAMINES, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1220, 'DORANEVI1S1', 'NEVIRAPINE (NVP), 50mg/5ml, susp. orale, 100 ml, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1221, 'DORANEVI2T-', 'NEVIRAPINE (NVP), 200 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1222, 'DORANIFE1TI', 'NIFEDIPINE, 10 mg, comp. lib. immédiate', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1223, 'DORANYST1S-', 'NYSTATINE, 100.000 UI/ml, susp. orale', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1224, 'DORAOMEP2CG', 'OMEPRAZOLE, 20 mg, gél. gastrorésistante', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1225, 'DORAORMA2S8', 'RESOMAL, réhydratation malnut. aiguë compliq, sach. 84g/2l', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1226, 'DORAORSA2S-', 'SELS REHYDRATATION ORALE (SRO) basse osmol. sachet 20,5 g/1l', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1227, 'DORAPARA1S-', 'PARACETAMOL (acétaminophène), 120mg/5ml,susp.orale,100ml fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1228, 'DORAPARA1T-', 'PARACETAMOL (acétaminophène), 100 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1229, 'DORAPARA5T-', 'PARACETAMOL (acétaminophène), 500 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1230, 'DORAPENV2T-', 'PHENOXYMETHYLPENICILLINE, 250 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1231, 'DORAPHEN5T-', 'PHENOBARBITAL, 50 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1232, 'DORAPRAZ6T-', 'PRAZIQUANTEL, 600 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1233, 'DORAPRED5T-', 'PREDNISOLONE, 5 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1234, 'DORAPROM2T-', 'PROMETHAZINE chlorhydrate, éq. 25 mg base, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1235, 'DORAPRON2T-', 'PROTHIONAMIDE, 250 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1236, 'DORAPYRI1T-', 'PYRIDOXINE chlorhydrate (vitamine B6), 10 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1237, 'DORAPYRI5T-', 'PYRIDOXINE chlorhydrate (vitamine B6), 50 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1238, 'DORAPYRZ4T1', 'PYRAZINAMIDE (Z), 400 mg, comp., blister', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1239, 'DORAQUIN3T-', 'QUININE sulfate, 300 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1240, 'DORARETI2C-', 'RETINOL (vitamine A) stabilisé, 200.000 UI, caps. molle', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1241, 'DORARIFA1C1', 'RIFAMPICINE (R), 150 mg, gél. blister', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1242, 'DORARIFA3C1', 'RIFAMPICINE (R), 300 mg, gél. blister', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1243, 'DORASALB1N-', 'SALBUTAMOL, solution pour nébuliseur, 2 mg/ml, 2,5ml unidose', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1244, 'DORASALB2SF', 'SALBUTAMOL sulfate, éq.0,1mg base/bouffée, 200 bouff.aérosol', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1245, 'DORASPAQ1T2', 'SP 1x250/12.5mg + AQ 3x eq.75mg base, comp,coblister,4.5-8kg', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1246, 'DORASPAQ2T2', 'SP 1x 500/25mg + AQ 3x eq.150mg base, comp, coblister,9-17kg', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1247, 'DORASULP5T-', 'SULFADOXINE, 500 mg / PYRIMETHAMINE, 25 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1248, 'DORATEEF1T-', 'TDF 300 mg / FTC 200 mg / EFV 600 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1249, 'DORATELA1T-', 'TDF 300 mg / 3TC 300 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1250, 'DORATINI5T-', 'TINIDAZOLE, 500 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1251, 'DORATRAM5C-', 'TRAMADOL chlorhydrate, 50 mg, gél.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1252, 'DORAYILA1TD', 'AZT 60 mg / 3TC 30 mg, comp. disp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1253, 'DORAYILA2T-', 'AZT 300 mg / 3TC 150 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1254, 'DORAYILE1T2', 'AZT 300 mg / 3TC 150 mg x 2 + EFV 600 mg x 1 caps, coblister', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1255, 'DORAYILN1TD', 'AZT 60 mg / 3TC 30 mg / NVP 50 mg, comp. dispersible', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1256, 'DORAYILN2T-', 'AZT 300 mg / 3TC 150 mg / NVP 200 mg, comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1257, 'DORAYINS2T-', 'ZINC sulfate, éq. à 20 mg de zinc minéral, comp. dispers.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1258, 'DVACIMAS2V-', 'IMMUNOGLOBULIN AFRICAN SNAKES ANTIVENOM EchiTab-Plus, vial', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1259, 'DVACIMAS3A-', 'IMMUNOGLOBULIN AFRICAN SNAKE ANTIVENOM, SAIMR, 10ml amp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1260, 'DVACIMHR2S-', 'IMMUNOGLOBULINE HUM. ANTIRABIQUES, 150 UI/ml, 5 ml seringue', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1261, 'DVACIMTE2S-', 'IMMUNOGLOBULINE HUM. ANTITETANIQUE, 250 UI/ml, sering.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1262, 'DVACVBCG3SD', '(vaccin BCG) SOLVANT, 1 dose, multidose fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1263, 'DVACVBCG3VD', 'VACCIN BCG, 1 dose, fl. multidose, 0.05 ml/dose', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1264, 'DVACVDHH1VD', 'VACCIN DTC / HEPATITE B / Hib, 1 dose, fl. multidose', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1265, 'DVACVHEA1S-', 'VACCIN HEPATITE A, 1 dose, adult, monodose, seringue', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1266, 'DVACVHEB1VD', 'VACCIN HEPATITE B, 1 dose adulte, fl. multidose', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1267, 'DVACVHEB3V-', 'VACCIN HEPATITE B, 1 dose enfant, monodose, 0,5ml, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1268, 'DVACVMEA2SD', '(vaccin rougeole) SOLVANT, 1 dose, fl. multidose', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1269, 'DVACVMEA2VD', 'VACCIN ROUGEOLE, 1 dose, fl. multidose', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1270, 'DVACVMEN1VWCJ', 'VACCIN MENINGITE CJ A+C+W135+Y, monod.+ solv.0,5ml (Menveo)', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1271, 'DVACVMENA1SD', '(vaccin méning. A conj. 1-29ans) SOLVANT 1 dose,fl.multidose', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1272, 'DVACVMENA1VD', 'VACCIN MENINGOCOQUE A CONJUGUE, 1-29 ans, 1dose, fl. multid.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1273, 'DVACVPOL13BD', 'VACCIN POLIO, BIVALENT ORAL, 1 dose, fl. multidose', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1274, 'DVACVRAB1V-', 'VACCIN ANTIRABIQUE, VCC, culture cellulaire, monodose, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1275, 'DVACVTET1VD', 'VACCIN TT (tétanos), 1 dose, fl. multidose', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1276, 'DVACVTYP1S-', 'VACCIN TYPHOIDIQUE polyosidique 25µg,monodose,0,5ml,seringue', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1277, 'DVACVYEF1VD', 'VACCIN FIEVRE JAUNE, 1 dose, fl.  multidose + solvant', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1278, 'EANEAIRG0--', 'CANULE DE GUEDEL, réutilisable n°0, pédiatrique', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1279, 'EANEAIRG00-', 'CANULE DE GUEDEL, réutilisable n°00, néo-natal', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1280, 'EANEAIRG1--', 'CANULE DE GUEDEL, réutilisable n°1, petit enfant', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1281, 'EANEAIRG2--', 'CANULE DE GUEDEL, réutilisable n°2, enfant', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1282, 'EANEAIRG3--', 'CANULE DE GUEDEL, réutilisable n°3, adolescent', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1283, 'EANEAIRG4--', 'CANULE DE GUEDEL, réutilisable n°4, adulte', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1284, 'EANEAIRG5--', 'CANULE DE GUEDEL, réutilisable n°5, grand adulte', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1285, 'EANEMASARH4', 'MASQUE DANESTHESIE + BOURRELET + CROCHET, T 4, adolescent', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1286, 'EANEMASARH5', 'MASQUE DANESTHESIE + BOURRELET + CROCHET, T 5, adulte', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1287, 'EANEMASAS0-', 'MASQUE DANESTHESIE, silicone, taille 0, prématuré', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1288, 'EANEMASAS1-', 'MASQUE DANESTHESIE, silicone, taille 1, nouveau-né', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1289, 'EANERESU102', 'POIRE DASPIRATION (Laerdal NeoNatalie), silicone 986000', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1290, 'EANERESU202', 'VALVE pour réanimateur manuel Ambu, ADULTE complète 1 clapet', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1291, 'EANERESU2AC', 'REANIMATEUR MANUEL (Ambu), ad./enfant + masques RH5 / RH2', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1292, 'EANERESU2CN', 'REANIMATEUR MANUEL (Ambu), enfant + masques RH2/S1', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1293, 'EANERESU2NN', 'REANIMATEUR MANUEL (Ambu), nouveau-né + masques S0/S1', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1294, 'EANTBRAB115', 'BRACELET PERIMETRE BRACHIAL (MUAC), pédiatrique, PP, 115 mm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1295, 'EANTBRAB2A-', 'BRACELET PERIMETRE BRACHIAL (MUAC), adulte, polypropylène', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1296, 'EANTMEAA1P-', 'MICROTOISE, ruban, verticale, 200 cm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1297, 'EANTMEAA3P-', 'TOISE pédiatrique, hor./vert., 130 cm, bois', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1298, 'EANTSCAA001', '(balance de type Salter) CULOTTE', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1299, 'EANTSCAL1B-', 'BALANCE (Seca 725), méc. à curseurs, bébé, 0-15kg, grad. 10g', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1300, 'EANTSCAL25-', 'BALANCE DE TYPE SALTER, 0-25 kg, sans culotte, grad. 100 g', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1301, 'EANTSCAL3A-', 'BALANCE mécanique, adulte 0-150 kg, grad. 500 g', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1302, 'EANTSCAL50-', 'BALANCE DE TYPE SALTER, 0-50 kg, sans culotte, grad. 200 g', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1303, 'EANTSCAL6--', 'PESE-BEBE (Seca 354), électronique, 0-20 kg', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1304, 'EANTTAPM1--', 'METRE, RUBAN, 1,5 m, fibre de verre', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1305, 'EANTTAPM1A-', 'METRE, RUBAN, 1 m, adhésif', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1306, 'EDDCBOTP1--', 'BOUTEILLE, plastique, 1 l, pour dilution + bouchon à visser', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1307, 'EDDCBOTP2--', 'BOUTEILLE, plastique, 200/250 ml, avec BEC VERSEUR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1308, 'EDDCMORT1P-', 'MORTIER, porcelaine 150 ml + PILON', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1309, 'EDDCTACO15P', 'TABLET COUNTER, triangular, plastic, 15 cm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1310, 'EDDCTACU1--', 'COUPEUR DE COMPRIMES, lame inoxydable', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1311, 'EDIMULSC1CA', '(Echographe) GEL DE TRANSMISSION, 250 ml fl.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1312, 'EDIMULSE4--', 'ECHOGRAPHE (Sonosite M-Turbo) + SONDE C60x', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1313, 'EDIMULSS401', '(echographe M-Turbo) BATTERIE', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1314, 'EEMDCONA001', 'HUMIDIFICATEUR, autoclavable (FLOWMETER CH200)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1315, 'EEMDCONA603', '(conc. Eclipse 3/5) BATTERIE Power Cartridge 7082-SEQ', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1316, 'EEMDCONC313', '(conc. DeVilbiss 515KS/AKS/525KS) FILTRE ANTIBACT. PV5LD-651', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1317, 'EEMDCONE4--', 'CONCENTRATEUR O2 (DeVilbiss 525KS) 220V + access.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1318, 'EEMDCONE5--', 'CONCENTRATEUR O2 (New Life Intensity) 10l, 230V, 50 Hz + acc', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1319, 'EEMDCONE6--', 'CONCENTRATEUR O2 portable (Eclipse 5) + acc.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1320, 'EEMDCONS301', '(conc. De Vilbiss 515KS/AKS/525KS)REPARTITEUR MULTI-PATIENTS', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1321, 'EEMDCONS304', '(conc. DeVilbiss 515KS/AKS/525KS)TUYAU SORTIE COMP 505DZ-634', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1322, 'EEMDCONS309', '(conc. DeVilbiss 515KS/AKS/525KS)MANOMETRE DESSAI PV02D-601', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1323, 'EEMDCONS313', '(conc. DeVilbiss515KS/AKS/525KS)2CLAPETS ANTIRETOUR PV02D607', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1324, 'EEMDCONS319', '(conc. DeVilbiss 515KS/AKS/525KS) DEBIMETRE 505DZ-607', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1325, 'EEMDCONS320', '(conc. DeVilbiss 515KS/AKS/525KS) INTERRUPTEUR 505DZ-508', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1326, 'EEMDCONS321', '(conc. DeVilbiss 515KS/AKS/525KS) COMPTEUR HORAIRE PV5LD-617', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1327, 'EEMDCONS332', '(conc. DeVilbiss 515KS/AKS/525KS)RACCORD SORTIE O2 525D-170', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1328, 'EEMDCONS335', '(conc. DeVilbiss 515KS/AKS/525KS) EMBOUT SORTIE plastiq.vert', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1329, 'EEMDCONS339', '(conc. DeVilbiss 515AKS/525KS) CONDENSATEUR MC44I-626', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1330, 'EEMDCONS342', '(conc. DeVilbiss 515KS/AKS/525KS) RACCORD MALE de sortie', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1331, 'EEMDCONS343', '(conc. DeVilbiss 515KS/AKS/525KS) RACCORD FEMELLE de sortie', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1332, 'EEMDCONS401', '(conc. DeVilbiss 525KS) FILTRE POUSSIERE 303DZ-605', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1333, 'EEMDCONS402', '(conc. DeVilbiss 525KS) FILTRE LONGUE DUREE MC44D-605', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1334, 'EEMDCONS404', '(conc. DeVilbiss 525KS) KIT REPAR. COMPRESSEUR 525K-643', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1335, 'EEMDCONS405', '(conc. DeVilbiss 525KS) FILTRE COMPRESSEUR 525D-622', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1336, 'EEMDCONS406', '(conc. DeVilbiss 515AKS/525KS) VALVE, 4 VOIES  515ADZ-702', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1337, 'EEMDCONS409', '(conc. DeVilbiss 525KS) PANNEAU AVANT PRINCIPAL 525D-604', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1338, 'EEMDCONS410', '(conc. DeVilbiss 525KS) COMPRESSEUR 525K-541', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1339, 'EEMDCONS412', '(conc. DeVilbiss 525KS) TAMIS, paire 525D-619', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1340, 'EEMDCONS413', '(conc. DeVilbiss 525KS) FUSIBLE CARTE MERE,80mA 250V 1123215', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1341, 'EEMDCONS501', '(conc. NL Intensity 10l) FILTRE ENTREE AIR MI161-2/TOF-010', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1342, 'EEMDCONS502', '(conc. NL Intensity 10l) FILTRE POUSSIERE, mousse FI002-1', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1343, 'EEMDCONS504', '(conc. NL Intensity 10l) TAMIS BE186-1R', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1344, 'EEMDCONS505', '(conc. NL Intensity 10l) CARTE ELECTRONIQUE CB160-1', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1345, 'EEMDCONS508', '(conc. NL Intensity 10l) VENTILATEUR, 220V FN022-2', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1346, 'EEMDCONS512', '(conc. NL Intensity 10l) DEBIMETRE FM056-1', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1347, 'EEMDCONS516', '(conc. NL Intensity 10l) ROULETTES CS001-1S', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1348, 'EEMDCONS521', '(conc. NL Intensity 10l) CABLE DALIMENTATION EU CD011-4', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1349, 'EEMDCONS522', '(conc. NL Intensity 10l) CIRCUIT BREAKER, 220V CR001-6', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1350, 'EEMDCONS523', '(conc. NL Intensity 10l)  VALVE, EQ, 220V, VA003-2', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1351, 'EEMDCONS525', '(conc. NL Intensity 10l) CONNECT. SORTIE, ADAPT., O2 F0025-1', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1352, 'EEMDCONS528', '(conc. NL Intensity 10l) BUZZER, W/WIRE HARNESS WH096-2', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1353, 'EEMDCONS529', '(conc. NL Intensity 10l) PRESS.REGULATOR, PROD. TANK RG088-1', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1354, 'EEMDCONS531', '(conc. NL Intensity 10l) HOUR METER, HM009-2', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1355, 'EEMDCONS533', '(conc. NL Intensity 10l) KIT REPAIR COMPRESSOR, CO012-9', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1356, 'EEMDCONS601', '(conc. Eclipse 5) FILTRE HEPA 4470-SEQ', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1357, 'EEMDCONS612', '(conc. Eclipse 3/5) FILTRE MOUSSE ENTREE, unité 7028-SEQ', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1358, 'EEMDCONS613', '(conc. Eclipse 3/5) FILTRE COMPRESSEUR ENTREE,x12, 3895-SEQ ', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1359, 'EEMDCONS616', '(conc. Eclipse 3/5) TRANSFOMATEUR  5941', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1360, 'EEMDCONS617', '(conc. Eclipse 3/5) CHARGEUR pr batterie 7112', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1361, 'EEMDFHDA301', '(Sonotrax basic) SONDE 2 MHz, étanche MS3-14320', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1362, 'EEMDFHDE3--', 'DETECTEUR RYTHME CARDIAQUE FOETAL ultrasons (Sonotrax)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1363, 'EEMDLEXE2--', 'LAMPE DECLAIRAGE, EXAMEN (LID medical), mobile, 230 V', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1364, 'EEMDLEXS102', '(nv lampe dexamen mobile) AMPOULE halogène dichr.12V 45W', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1365, 'EEMDLOPA301', '(lampe dop. MACH LED) POIGNEE, autoclavable 21150002', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1366, 'EEMDLOPA302', '(lampe dop. MACH LED 200F) TETE LED pann.contrôle 31090201', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1367, 'EEMDLOPA304', '(lampe dop. MACH LED) ADAPTATEUR ALIM., 60W 67010414', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1368, 'EEMDLOPA305', '(lampe dop. MACH LED) PANNEAU CONTROLE 14117002', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1369, 'EEMDLOPS311', '(lampe dop. MACH LED) FUSIBLE T2.OA 67370005', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1370, 'EEMDNECA201', '(nébuliseur PariMobile S) NEBULISEUR LC SPRINT, réut.23G1001', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1371, 'EEMDNECA202', '(nébuliseur PariMobile S) MASQUE ADULTE,réutilisable 41G0740', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1372, 'EEMDNECA203', '(nébuliseur PariMobile S) MASQUE ENFANT,réutilisable 41G0741', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1373, 'EEMDNECC211', '(nébuliseur PariMobile S) FILTRE A AIR , 5 pcs, 41E4852', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1374, 'EEMDNECE2--', 'NEBULISEUR + COMPRESSEUR (PariMobile S) + accessoires', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1375, 'EEMDNECS212', '(nébuliseur PariMobile S) BATTERIE 47G2000', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1376, 'EEMDPOXA401', '(oxymètre Masimo) CAPTEUR adulte, LNCS-DCI 1863', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1377, 'EEMDPOXA403', '(oxymètre Masimo) CABLE dextension <2010, blanc, LNC-4 2017', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1378, 'EEMDPOXA406', '(oxymètre Masimo) CABLE dextension, rouge, LNC-04 2055', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1379, 'EEMDPOXE4--', 'OXYMETRE DE POULS (Masimo RAD-5) + accessoires', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1380, 'EEMDPUMA303', '(aspirateur VacuAide) BOCAL autoclavable 01-7305D-601S', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1381, 'EEMDPUMA304', '(aspirateur VacuAide) TUYAU ASPIRATION, Ø 7,5mm 01-6305D.611', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1382, 'EEMDPUMC301', '(aspirat VacuAide) FILTRE ANTIBACTERIEN, 12pcs, 01-7305D-608', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1383, 'EEMDPUME3--', 'ASPIRATEUR ELECTRIQUE (DeVilbiss VacuAide),100-240V 7314', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1384, 'EEMDPUMS302', '(aspirateur VacuAide) VACUOMETRE 01-05D-607', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1385, 'EEMDPUMS305', '(aspirateur VacuAide) TRANSFORMATEUR 01-7305P-613', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1386, 'EEMDPUMS306', '(aspirateur VacuAide) BATTERIE 01-7305P-614', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1387, 'EEMDVACA103', '(aspirateur chir. AS72/AS64R) BOCAL, 4 l, polysulfone', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1388, 'EEMDVACA401', '(aspirateur chir. AS64R) PEDALE PNEUMATIQUE', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1389, 'EEMDVACA412', '(aspirateur chir. AS64R) BOUTEILLE DE SECURITE, 300 ml', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1390, 'EEMDVACC102', '(aspirat chir. AS72/AS64R) TUYAU ASPIRATION, Ø7mm, 25m, 1pc', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1391, 'EEMDVACC405', '(aspir. chir. AS64R) FILTRE antibact hydrophobe, 1pc V21FI50', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1392, 'EEMDVACS403', '(aspirateur chir. AS64R) BOUTON de COMMANDE', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1393, 'EEMDVACS404', '(aspirateur chir. AS64R) VIS FIXATION BOUTON CHTC Ø 4 X 6', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1394, 'EEMDVACS407', '(aspirateur chir. AS64R) FUSIBLE', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1395, 'EEMDWAIE3--', 'RECHAUFFEUR NEONATAL (Ceratherm 600-3), mobile', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1396, 'EEMDWAIS101', '(réch.néon..Ceratherm 600-2) ELEMENT CHAUFFANT 521-753011', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1397, 'EEMDWAIS102', '(réch.néon.Ceratherm 600-2) LAMPE HALOGENE 12V/20W 521-0-BAB', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1398, 'EEMDWAIS103', '(réch.néon.Ceratherm 600-2) SET de 3 ROUES 521-59042', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1399, 'EEMDWAIS112', '(réch.nn.Ceratherm600-2) FUSIBLE 3.15 AT,10pcs 521-533599', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1400, 'EEMDWAIS301', '(réch.néon.Ceratherm600-3) ELEMENT CHAUFFANT521-75.3012.S2-3', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1401, 'EEMDWAIS303', '(réch.néon. Ceratherm 600-3) LAMPE LED 521-LED-CT-3', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1402, 'EEMDWAIS304', '(réch.néon. Ceratherm 600-3) SET 3 ROUES 521-MZL-RSC-3', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1403, 'EHOEMATT2W-', 'HOUSSE MATELAS, lavable, fermeture éclair, 220 cm, épidémies', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1404, 'EHOEPUMA110', '(aspirateur, Twin Pump) BOCAL COLLECTEUR, 2 litres, verre', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1405, 'EHOEPUME1--', 'ASPIRATEUR MECANIQUE (Twin Pump) + bocaux collecteurs', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1406, 'EHOEPUMS101', '(aspirateur, Twin Pump) TUBE DE REPARTITION', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1407, 'EHOEPUMS102', '(aspirateur, Twin Pump) CYLINDRE, ATTACHE, MEMBRANE DE VAL.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1408, 'EHOEPUMS103', '(aspirateur, Twin Pump) SEGMENT', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1409, 'EHOEPUMS104', '(aspirateur, Twin Pump) JOINT TORIQUE', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1410, 'EHOEPUMS106', '(aspirateur, Twin Pump) TUBE DASPIRATION, 135 cm + embout', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1411, 'EHOESTAI2--', 'POTENCE A PERFUSION, 2 crochets, sur roulettes', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1412, 'EHOESTRT2--', 'BRANCARD, pliant en long/large, alu, 4 pieds, 215 x 58 cm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1413, 'EHOETABE3DF', 'TABLE DEXAMEN, démontable ou pliable, têtière réglable', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1414, 'EHOETABE4G-', 'TABLE DEXAMEN, gynécologique, démontable', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1415, 'ELABBOTD0060', 'FLACON COMPTE-GOUTTES, plastique, 60 ml', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1416, 'ELABBOTW0250', 'PISSETTE, col de cygne, plastique, 250 ml', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1417, 'ELABBOXL100', 'BOITE RANGE LAMES, plastique, pour 100 lames', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1418, 'ELABCOCN5D-', 'CELLULE A NUMERATION, NEUBAUER, modifiée, double quadrillage', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1419, 'ELABCOVG22-', 'LAMELLE COUVRE-OBJET, 22 x 22 mm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1420, 'ELABCOVGP26', 'LAMELLE PLANEE, pour cellule à numération, 20 x 26 mm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1421, 'ELABCYLG0500', 'EPROUVETTE, verre, graduée, à bec verseur 500 ml', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1422, 'ELABCYLP0100', 'EPROUVETTE, plastique, graduée, à bec verseur, 100 ml', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1423, 'ELABCYLP1000', 'EPROUVETTE, plastique, graduée, à bec verseur, 1 l', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1424, 'ELABFILM1R-', 'FILM POUR SCELLER (Parafilm), plastique, roul., 10 cm x 38 m', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1425, 'ELABFOBR2--', 'PINCE pour lames, courbe, inox', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1426, 'ELABFUNG150', 'ENTONNOIR, verre, Ø 150 mm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1427, 'ELABLAMP7S-', 'LAMPE à alcool, 65-100 ml, complète, mèche 7 mm Ø', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1428, 'ELABMARK1B-', 'MARQUEUR, permanent, noir, pointe fine', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1429, 'ELABMILP1--', 'MANCHE PASTEUR + anse, nickel chrome', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1430, 'ELABPAPE1L-', 'PAPIER OPTIQUE, la feuille', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1431, 'ELABPAPFD150', 'DISQUE PAPIER FILTRE, plissé, non imprégné, Ø 150 mm approx.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1432, 'ELABPAPP5--', 'PAPIER DE PROTECTION POUR TABLE, absorbant/PE, 50 cm x 50 m', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1433, 'ELABPAPW3D-', 'PAPIER, WHATMAN, n°3, disque', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1434, 'ELABPENC1D-', 'STYLO, diamant, pour écrire sur le verre', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1435, 'ELABPIAA0100', 'PIPETTE AUTOMATIQUE, vol. réglable 10-100 µl (Eppendorf)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1436, 'ELABPIAA1000', 'PIPETTE AUTOMATIQUE, vol. réglable 100-1000 µl (Eppendorf)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1437, 'ELABPIATB--', '(pip.aut.) EMBOUT BLEU, 50-1000µl (Eppdf)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1438, 'ELABPIATBRF', '(pip.aut.) EMBOUT BLEU, 50-1000µl, rack,ster,filter (Eppdf)', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1439, 'ELABPIATY--', '(pip.aut.) EMBOUT JAUNE, 2-200µl (Eppdf)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1440, 'ELABPIATYR-', '(pip.aut.) EMBOUT JAUNE, 2-200µl, rack (Eppdf)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1441, 'ELABPIDF50-', 'PIPETTE, volume fixe (MiniPet), 50 µl jaune', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1442, 'ELABPIPT3N-', 'PIPETTE DE TRANSFERT, graduée, plastique, non stérile, u.u.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1443, 'ELABRACK1--', 'SUPPORT POUR COLORATION DE LAMES, extensible, inox', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1444, 'ELABRACK2D-', 'SUPPORT POUR SECHAGE DE LAMES, vertical, plastique 10 lames', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1445, 'ELABSLID1--', 'LAME PORTE-OBJETS, 76 x 26 mm, épaisseur 1-1,2 mm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1446, 'ELABSLID1FT', 'LAME PORTE-OBJETS, dépolie, 76 x 26 mm,emballage tropicalisé', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1447, 'ELABSTIC1W-', 'APPLICATEUR en bois', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1448, 'ELABTILE5--', 'PLAQUE, groupage sanguin, lisse, avec 5 cavités', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1449, 'ELABTIME1E-', 'MINUTEUR électronique', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1450, 'ELABTUBE12-', 'TUBE, Ø 12 mm, plastique, stérile, 5 ml + BOUCHON', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1451, 'ELABTUBE12R', '(tube Ø 13/15 mm, 5 ml) PORTOIR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1452, 'ELABTUCA1E-', '(Determine test rapide) TUBE CAPILLAIRE EDTA ref.7D2222/27', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1453, 'ELABTUCE1NG', 'TUBE A CENTRIFUGER, 15 ml, fond conique, verre', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1454, 'ELABTUCE2SPP', 'TUBE A CENTRIFUGER, 15 ml, fond conique, PP stérile ss grad', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1455, 'ELABTUCE5R-', '(tube 50 ml à centrifuger, Ø 35 mm) PORTOIR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1456, 'ELABTUCE5SPP', 'TUBE A CENTRIFUGER, 50 ml, fond conique, PP, bouchon,stérile', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1457, 'ELABTUCER1-', '(tube à centrifuger, Ø 18 mm) PORTOIR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1458, 'ELABTUMS15AS', 'MICROTUBE, 1.5ml, conique, bouch. à vis attaché, stérile', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1459, 'ELABTUMS20CP', 'MICROTUBE, 2.0ml, jupé, bouch. à viss. assembl., PCR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1460, 'ELABTUMT15CN', 'MICROTUBE, 1.5ml, conique, cap. attaché, classique, non stér', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1461, 'ELAECD4A106', '(CD4 Analyseur PIMA) TRANSFORMATEUR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1462, 'ELAECD4C113', '(CD4 analyseur PIMA) PAPIER THERMIQUE,10 rouleaux, 260400009', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1463, 'ELAECD4T101', '(CD4 analyseur PIMA) CARTOUCHE TEST, 260100100', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1464, 'ELAECD4T102', '(CD4 analyseur PIMA) CONTROLE BEAD STANDARD CART. 260400011', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1465, 'ELAECENA902', '(centrifuge Hettich EBA200) ADAPTOR, 13.5 x 79 mm  1058', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1466, 'ELAECENE1M-', 'CENTRIFUGEUSE, manuelle pour 4 tubes 15 ml', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1467, 'ELAECENE9--', 'CENTRIFUGEUSE électrique (Hettich EBA 200), 8 tubes, 230V', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1468, 'ELAECENS903', '(cent. Hettich EBA200) VOYANT EXTERIEUR+bague collante E1323', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1469, 'ELAECENS904', '(cent. Hettich EBA200) CONNECTEUR mâle E3329', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1470, 'ELAECENS906', '(cent. Hettich EBA200) JOINT TORIQUE E3772', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1471, 'ELAECENS907', '(cent. Hettich EBA200) PIED en caoutchouc, cpl E3811', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1472, 'ELAEGLUA203', '(glucomètre Nova StatStrip) PILE 3V, DL2450', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1473, 'ELAEGLUE2--', 'GLUCOMETRE, lecteur glycémie (Nova StatStrip) mg/dl', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1474, 'ELAEGLUT201', '(glucomètre Nova StatStrip) BANDELETTE réf.42214', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1475, 'ELAEGLUT211', '(glucomètre Nova StatStrip) SOL. DE CONTROLE bas 4ml 46947', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1476, 'ELAEGLUT212', '(glucomètre Nova StatStrip) SOL.DE CONTROLE normal 4ml 46948', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1477, 'ELAEGLUT213', '(glucomètre Nova StatStrip) SOL. DE CONTROLE haut 4ml 46949', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1478, 'ELAEHAEC001', '(HemoCue Hb 201+/301) NETTOYANT, 5pcs, HE139123', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1479, 'ELAEHAEE3--', 'PHOTOMETRE HEMOGLOBINE (HemoCue Hb 301), tropicalisé', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1480, 'ELAEHAET302', '(HemoCue Hb 301) SOLUTION DE CONTROLE, bas, 2 x 1 ml fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1481, 'ELAEHAET303', '(HemoCue Hb 301) SOLUTION DE CONTROLE, normale, 2 x 1 ml fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1482, 'ELAEHAET304', '(HemoCue Hb 301) SOLUTION DE CONTROLE, haut, 2 x 1 ml fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1483, 'ELAEHAET305', '(HemoCue Hb 301) MICROCUVETTE, u.u.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1484, 'ELAEMBIC114', '(bm GeneXpert GX-IV) KIT Xpert CHECK, XPERTCHECK-CE-5', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1485, 'ELAEMBIT101', '(bm GeneXpert)TEST MTB/RIF, cartouche', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1486, 'ELAEMBIT104', '(bm GeneXpert) TEST HIV-1 Qual EID, cartouche', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1487, 'ELAEMBIT105', '(bm GeneXpert) TEST HIV-1 VL, cartouche', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1488, 'ELAEMICA601', '(microscope PrimoStar iLED) UNITE ACCUMULATEUR 415500-1814', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1489, 'ELAEMICA602', '(microscope PrimoStar iLED) MIROIR réf 415500-1202', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1490, 'ELAEMICA603', '(microscope PrimoStar iLED) COFFRET DE TRANSPORT', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1491, 'ELAEMICA604', '(microscope PrimoStar iLED) LED module réf 000000 1520 246', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1492, 'ELAEMICC002', 'LENS CLEANING SOLUTION, 1 l, bot. OT9624', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1493, 'ELAEMICE6--', 'MICROSCOPE (PrimoStar iLED), lumière & fluorescence 100-240V', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1494, 'ELAEROTE2--', 'AGITATEUR, orbital, pour test agglutination, 230 V', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1495, 'ELAESCAE5--', 'BALANCE électronique banque de sang (Kern), 0 - 2200 g, 1 g', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1496, 'ELAESPEC403', '(spectro Humalyzer 2000) PAPIER THERMIQUE, 5 roul.18144/5', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1497, 'ELAESPEC404', '(spectrophotomètre) SEMI-MICROCUVETTES, PS, 1,5 ml, u.u.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1498, 'ELAESPEC501', '(spectrophotometer) MACROCUVETTE, 10 mm, PS, 2.5 - 4.5 ml', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1499, 'ELAESPEE4--', 'SPECTROPHOTOMETRE (Humalyzer 2000), thermorégulé 110-230V', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1500, 'ELAESPES402', '(spectrophotomètre Humalyzer 2000) FILTRE AEROSOL', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1501, 'ELAESPES405', '(spectro Humalyzer 2000) KIT TUYAUX INTERNES  18340/21', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1502, 'ELAESPES406', '(spectro Humalyzer 2000-3000) AMPOULE HALOGENE 18340/05', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1503, 'ELAESPES407', '(spectro Humalyzer 2000) KIT TUYAUX CELLULE d.FLUX 18320/10', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1504, 'ELAESPES409', '(spectro Humalyzer 2000) VALVE ASSEMBLY 18360/10', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1505, 'ELAESPES410', '(spectro Humalyzer 2000) WASTE BOTTLE TUBING KIT 18320/29', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1506, 'ELAESPES415', '(spectro Humalyzer 2000) VALVE BRACKET REPAIR KIT 16700/99', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1507, 'ELAESPES416', '(spectro Humalyzer 2000) PRINTER MECHANISM 18360/15', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1508, 'ELAESPES419', '(spectro Humalyzer 2000) SPARE PARTS KIT 18398', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1509, 'ELAESPET10051', '(spectrophotomètre) KIT, CREATININE liquicolor 200ml 10051', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1510, 'ELAESPET12212', '(spectrophotomètre) KIT, ALAT, IFCC 16 x 5ml 12212', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1511, 'ELAESPET13151', '(spectro) CONTROLE SERODOS PLUS (Human) anormal 6 x 5 ml', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1512, 'ELAESPET13951', '(spectro) CONTROLE SERODOS  (Human) normal 6 x 5 ml', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1513, 'ELINAPRP1P-', 'TABLIER DE PROTECTION, PVC, réutilisable', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1514, 'ELINCADS1S-', 'COIFFE CHIRURGICALE, charlotte, non-tissée, u.u.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1515, 'ELINCADS1W-', 'COIFFE CHIRURGICALE, tissée', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1516, 'ELINCLOG41-', 'SABOTS, salle dopération, polyuréthane,lavables,paire 40-41', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1517, 'ELINCLOG43-', 'SABOTS, salle dopération, polyuréthane,lavables,paire 42-43', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1518, 'ELINCOAW1L-', 'BLOUSE MEDICALE, blanche, manches courtes, L', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1519, 'ELINCOAW1M-', 'BLOUSE MEDICALE, blanche, manches courtes, M', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1520, 'ELINCOAW1S-', 'BLOUSE MEDICALE, blanche, manches courtes, S', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1521, 'ELINDRAS1W-', 'CHAMP CHIRURGICAL, tissé, 1 x 1 m', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1522, 'ELINDRAS2W-', 'CHAMP CHIRURGICAL, tissé, 1,5 x 1,5 m', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1523, 'ELINDRAW1R-', 'ALEZE, caoutchouc, 100 x 180 cm, avec rivets', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1524, 'ELINDRAW6D-', 'ALEZE, u.u., plastifiée, absorbante, 60 x 60 cm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1525, 'ELINDRSS01-', 'CHAMP STERILE, non-tissé, u.u., 45 x 75 cm', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1526, 'ELINMASP001', 'APPAREIL PROTECTION RESP, FFP2ouN95 (Fluidshield N95) S', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1527, 'ELINMASP002', 'APPAREIL PROTECTION RESP, FFP2ouN95 (Fluidshield N95) M', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1528, 'ELINMASS3--', 'MASQUE CHIRURGICAL, type IIR, u.u.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1529, 'ELINTROS1WL', 'PANTALON CHIRURGICAL, tissé, L', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1530, 'ELINTROS1WM', 'PANTALON CHIRURGICAL, tissé, M', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1531, 'ELINTROS1WS', 'PANTALON CHIRURGICAL, tissé, S', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1532, 'ELINTUNS1WL', 'TUNIQUE CHIRURGICALE, tissé, L', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1533, 'ELINTUNS1WM', 'TUNIQUE CHIRURGICALE, tissé, M', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1534, 'ELINTUNS1WS', 'TUNIQUE CHIRURGICALE, tissé, S', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1535, 'EMEQBEDP1P-', 'BASSIN DE LIT, avec poignée, polypropylène', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1536, 'EMEQBRUS1--', 'BROSSE à ongles, plastique, autoclavable', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1537, 'EMEQCUFF5--', 'MANCHETTE A PRESSION, pour poche 500/1000 ml', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1538, 'EMEQDBOT2--', 'BOCAL, drainage thoracique, 2 l autocl. + bouchon 2 tubes', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1539, 'EMEQGLAS1P-', 'LUNETTES DE PROTECTION, plastique', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1540, 'EMEQJARF1--', 'PORTOIR pour pince à servir + couvercle, inox', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1541, 'EMEQKIDD26-', 'BASSIN RENIFORME, 26 cm x 14 cm, inox (haricot)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1542, 'EMEQOPHT1--', 'OPHTALMOSCOPE, halogène', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1543, 'EMEQOPHT1B-', '(ophtalmoscope) AMPOULE de rechange, halogène', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1544, 'EMEQOTOS1--', 'OTOSCOPE, halogène + SPECULUMS', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1545, 'EMEQOTOS1B-', '(otoscope Heine mini 3000) AMPOULE de rechange, XHL105 2,5V', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1546, 'EMEQOTOS1S-', '(otoscope) SPECULUMS reutilisables, le jeu', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1547, 'EMEQSPAC2--', 'CHAMBRE DINHALATION, 155 ml, avec masques + embout', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1548, 'EMEQSPAC201', '(chambre dinhalation) MASQUE, silicone, taille 1', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1549, 'EMEQSPAC202', '(chambre dinhalation) MASQUE, silicone, taille 2', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1550, 'EMEQSPAC2MO', '(chambre dinhalation) EMBOUT avec valve', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1551, 'EMEQSPHY1A-', 'SPHYGMOMANOMETRE, manopoire, velcro, adulte', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1552, 'EMEQSPHY1P-', 'SPHYGMOMANOMETRE, manopoire, velcro, enfant', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1553, 'EMEQSPLI1A-', 'ATTELLE gonflable, fermeture éclair, bras', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1554, 'EMEQSPLI1L-', 'ATTELLE gonflable, fermeture éclair, jambe', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1555, 'EMEQSPLK1A-', 'ATTELLE DE CRAMER, métallique, pliable, bras', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1556, 'EMEQSTET1--', 'STETHOSCOPE, une face, infirmier', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1557, 'EMEQSTET2--', 'STETHOSCOPE, double face, clinicien', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1558, 'EMEQSTET3OM', 'STETHOSCOPE obstétrical, métallique', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1559, 'EMEQSTET4--', 'STETHOSCOPE, double face, nourisson  (Littmann Classic II)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1560, 'EMEQTOUR1--', 'GARROT élastique, 100 x 1,8 cm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1561, 'EMEQTRAD3--', 'PLATEAU A PANSEMENTS, 30 x 20 x 3 cm, inox', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1562, 'EMEQVENT2--', 'VENTOUSE obstétricale (Vacca Reusable OmniCup), manuelle', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1563, 'ESTEAUTC399', '(autoclave 24/39 l) VASELINE pour couvercle, tube', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1564, 'ESTEAUTE24-', 'AUTOCLAVE 24 l (All American), sans réchaud, Ø int. 31.5 cm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1565, 'ESTEAUTE39-', 'AUTOCLAVE 39 l (All American), sans réchaud, Ø int. 35 cm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1566, 'ESTEAUTS392', '(autoclave 39 l) TUBE DECHAPPEMENT DAIR 2155-41', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1567, 'ESTEAUTS393', '(autoclave 24/39 l) POIGNEE BAKELITE COUVERCLE', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1568, 'ESTEAUTS394', '(autoclave 24/39 l) BOUTON Bakelite, fermeture couvercle', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1569, 'ESTEAUTS395', '(autoclave 24/39 l) MANOMETRE DE PRESSION', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1570, 'ESTEAUTS396', '(autoclave 24/39 l) BOUCHON DE SURPRESSION (rouge)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1571, 'ESTEAUTS397', '(autoclave 24/39 l) VALVE DE SURPRESSION', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1572, 'ESTEAUTS398', '(autoclave 24/39 l) VALVE DE CONTROLE', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1573, 'ESTEAUTS803', '(Matachana 80LR1, TBM) FILTRE A AIR, stérile 41608.1', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1574, 'ESTEAUTS904', '(autoclave TBM 90 l) JOINT porte, Ø 13 mm réf. 191019', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1575, 'ESTEAUTS916', '(autoclave TBM 90 l) SERPENTIN LAITON 600491', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1576, 'ESTEAUTS919', '(autoclave TBM 90 l) KIT  ADAPTATION, T700010', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1577, 'ESTEAUTS924', '(autoclave TBM 90 l) RESISTANCE thermopl., 4,5 KW 330004', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1578, 'ESTEAUTS925', '(autoclave TBM 90 l) VOYANT LUMINEUX, complet, bleu 240051', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1579, 'ESTEAUTS926', '(autoclave TBM 90 l) JOINT pour thermoplongeur 330020', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1580, 'ESTEAUTS932', '(autoclave TBM 90 l) SOUPAPE DE SECURITE 310679', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1581, 'ESTEDRUM15-', 'TAMBOUR, éclipses latérales,  Ø 15 cm, h. 10 cm, inox', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1582, 'ESTEDRUM34-', 'TAMBOUR, éclipses latérales, Ø 34 cm, h. 24 cm, inox', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1583, 'ESTEPAPS1--', 'FEUILLE DE STERILISATION, papier crêpe, 1,2 x 1,2 m', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1584, 'ESTETAAU1--', 'RUBAN INDICATEUR DE PASSAGE A LAUTOCLAVE, 50 m x 19 mm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1585, 'ESTETIME1--', 'MINUTEUR DE TABLE, 60 mn', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1586, 'ESTETSTI20A', 'BANDE INDICATRICE TVT, adhésive, 20 minutes', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1587, 'ESURCLTB13-', 'PINCE A CHAMP DE BACKHAUS, 13 cm 17-55-13', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1588, 'ESURDIUHD03', 'DILATATEUR UTERIN DE HEGAR, double 3+4 mm, 52-35-03', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1589, 'ESURDIUHD05', 'DILATATEUR UTERIN DE HEGAR, double 5+6 mm, 52-35-05', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1590, 'ESURDIUHD07', 'DILATATEUR UTERIN DE HEGAR, double 7+8 mm, 52-35-07', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1591, 'ESURDIUHD09', 'DILATATEUR UTERIN DE HEGAR, double 9+10 mm, 52-35-09', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1592, 'ESURDIUHD11', 'DILATATEUR UTERIN DE HEGAR, double 11+12 mm, 52-35-11', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1593, 'ESURDIUHD13', 'DILATATEUR UTERIN DE HEGAR, double 13+14 mm, 52-35-13', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1594, 'ESURDIUHD15', 'DILATATEUR UTERIN DE HEGAR, double 15+16 mm, 52-35-15', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1595, 'ESURDIUHD17', 'DILATATEUR UTERIN DE HEGAR, double 17+18 mm, 52-35-17', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1596, 'ESURFOAB20C', 'PINCE HEMOST. BENGOLEA, 20 cm, courbe, striée 16-57-20', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1597, 'ESURFOAK14C', 'PINCE HEMOST. DE KOCHER, 14 cm, 1x2 dents courbe, 16-13-14', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1598, 'ESURFOAP14S', 'PINCE HEMOST. DE PEAN, 14 cm, droite 16-10-14', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1599, 'ESURGALI100', 'CUPULE, inox 100 ml, 8 cm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1600, 'ESURGALI500', 'CUPULE, inox, 500 ml 12 cm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1601, 'ESURSCAL4--', 'BISTOURI, MANCHE, nº4 standard 01-28-04', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1602, 'ESURSCALB11', '(bistouri nº 3 et 7) LAME, u.u., stérile, nº 11, 01-22-11', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1603, 'ESURSCALB22', '(bistouri nº 4) LAME, u.u., stérile, nº 22, 01-22-22', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1604, 'ESURSCOP4SB', 'CISEAUX, mous./mous., droits, A PANSEMENTS, 14,5 cm 03-02-14', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1605, 'ESURSPVC16-', 'SPECULUM VAGINAL DE COLLIN, pr vierge, 90 x 16 mm 52-10-11', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1606, 'ESURSPVC35-', 'SPECULUM VAGINAL DE COLLIN, standard, 100 x 35 mm 52-11-12', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1607, 'ESURVENT1S-', 'VENTOUSE, obstétricale, manuelle, 40-50-60 mm + PIECES DET.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1608, 'ETMANRES2--', 'MANNEQUIN NOUVEAU-NE, réanimation, foncé, basic (NeoNatalie)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1609, 'KADMZNL0005', 'SURVIVAL SET SLEEPING for 2 persons', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1610, 'KMEDKFAI5RS', 'KIT MALLE DURGENCE, sac à dos  2013', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1611, 'KMEDKMSU1--', 'KIT MEDICO-CHIRURGICAL, 150 blesses', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1612, 'KMEDKNUT4M-', 'KIT ANTHROPOMETRIQUE, ENQUETE & SURVEILLANCE NUTRITIONNELLE', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1613, 'KMEDMCHO01-', '(module 001) MEDICAMENTS', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1614, 'KMEDMCHO02-', '(module 001) MATERIEL RENOUVELABLE', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1615, 'KMEDMDRE1--', 'MODULE MATERIEL DE SOINS', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1616, 'KMEDMDRS40B', 'MODULE PANSEMENTS, 40 pansements, brûlures', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1617, 'KMEDMDRS50-', 'MODULE PANSEMENTS, 50 pansements', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1618, 'KMEDMTUB1--', '(concentrateur doxygène 5-10 litres) MODULE TUBERCULOSE', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1619, 'KPROKTOO1--', 'KIT OUTILLAGE, basique', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1620, 'KPROKTOO4--', 'KIT OUTILLAGE, charpenterie', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1621, 'KPROMLIG4G4', 'MODULE ECLAIRAGE, pour tente, 4 guirlandes 4x9W fluocompact', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1622, 'KPROMLIGCF-', 'MODULE ECLAIRAGE, faible consommation, proj. + baladeuse', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1623, 'KPROMLOG01-', 'MODULE MATERIEL LOGISTIQUE DIVERS', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1624, 'KPROZNL0007', 'SET OF SMALL COMPRESSOR + SMALL PRESSURE WASHER + TOOLS', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1625, 'KPROZNL0019', '(SDMO T9KM/T16K) SPARES 5000hrs', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1626, 'KPROZNL0020', '(SDMO T6KM) SPARES 5000hrs', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1627, 'KPROZNL0021', '(SDMO T33K) SPARES 5000hrs', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1628, 'KSURBCES23-', 'SET CESARIENNE, 23 instruments', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1629, 'KSURBCUR19-', 'SET CURETAGE, 11 instruments + 8 dilatateurs doubles', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1630, 'KSURBDEL7--', 'SET ACCOUCHEMENT & EPISIOTOMIE, 7 instruments', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1631, 'KSURBDRE3N-', 'SET A PANSEMENT, 3 instruments, sans boîte', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1632, 'KSURBIUD7--', 'SET POSE DE STERILET, 7 instruments', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1633, 'KSURBMVA13S', 'SET ASPIRATION MAN. PAR LE VIDE ser.autocl, 8 instr.+ 5 dil.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1634, 'KSURBSUT7N-', 'SET ABCES SUTURE, 7 instruments, sans boîte', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1635, 'KTRAKVEH1--', 'KIT LOT DE BORD, pour tout véhicule', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1636, 'KTRAKVEH11-', 'KIT EQUIPEMENT COMPLEMENTAIRE, pour 4WD véhicule', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1637, 'KTRAKVEH13-', 'KIT DESEMBOURBAGE/DESENSABLAGE, tout véhicule', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1638, 'KTRAMMAIL4A', 'MODULE MAINTENANCE, 10 entretiens, HILUX 4X4 / D4D KUN25', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1639, 'KTRAZNL0010', '(HZJ78/79) SPARES FOR 10 SERVICES MAINTENANCE', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1640, 'KTRAZNL0019', '(HZJ78) MINEMATS just the labour', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1641, 'KTRAZNL0034', '(HZJ78/79) SET OF SPARES FOR 25000KM, part 1 of 2', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1642, 'KTRAZNL0035', '(HZJ78/79) SET OF SPARES FOR 25000/50000/75000KM, part 2 of2', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1643, 'KTRAZNL0036', '(HZJ78/79) SET OF SPARES FOR 50000KM, part 1 of 2', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1644, 'KTRAZNL0038', '(HZJ78/79) SET OF SPARES FOR 75000KM, part 1 of 2', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1645, 'KWATKJET3--', 'KIT PUITS AU JET, pour 3 puits', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1646, 'KWATKPUD060', 'KIT MOTOPOMPE DIESEL, 60 m³/h max, 27 m max, 3inch', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1647, 'KWATKPUP30-', 'KIT MOTOPOMPE, ESSENCE, 30 m³/h, 30m HMT (Koshin + Honda)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1648, 'KWATKPUPLE-', 'KIT VIDANGE DE LATRINES, pompe à membrane (Libellula +Honda)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1649, 'KWATKSPRCL-', 'KIT PULVERISATION DE CHLORE', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1650, 'KWATKTAN15-', 'KIT RESERVOIR, 15 m³, souple, stockage', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1651, 'KWATKTAN30O', 'KIT RESERVOIR, 30 m³, forme oignon, stockage/traitement', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1652, 'KWATKTANT02', 'KIT RESERVOIR, 2 m³, souple, transport', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1653, 'KWATKTANT05', 'KIT RESERVOIR, 5 m³, souple, transport', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1654, 'KWATMADA02-', 'MODULE ADAPTATION, 2inch/3inch/camion citerne/autres systèmes', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1655, 'KWATMADAOT-', 'MODULE ADAPTATION, accouplement, réservoirs Oxfam', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1656, 'KWATMCHL01A', '(module chloration et contrôle de leau) ARTICLES STANDARD', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1657, 'KWATMDIS01W', 'MODULE DISTRIBUTION DEAU, 2 rampes x 6 robinets', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1658, 'KWATMDOS01-', 'MODULE POMPE DOSEUSE, 10 l/h - 3 m³/h (Dosatron)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1659, 'KWATMHOS02-', 'MODULE TUYAUTERIE SUPPLEMENTAIRE + raccords, 2inch', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1660, 'KWATMSPR10R', '(pulvérisateur HD X-PERT) SET DE REPARATION', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1661, 'KWATMTRES1-', 'MODULE DOSEUR LATERAL DASPIRATION, débit 15-150 litres/h', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1662, 'L002CLIM01F-P', 'Guide clinique et thérapeutique', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1663, 'L002TRFM01E-P', 'Blood transfusion + CD-Rom', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1664, 'L002TRFM01F-P', 'Transfusion + CD-Rom', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1665, 'L003MEAM01F-P', 'Prise en charge dune épidémie de rougeole', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1666, 'L004CHOX01F-P', 'Guide pour la lutte contre le choléra', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1667, 'L004MENM01E-P', 'Management of epidemic meningococcal meningitis', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1668, 'L004MENM01F-P', 'PEC dune épidémie de méningite méningocoque', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1669, 'L004TUBM02F-P', 'Tuberculose', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1670, 'L007AIDM01F-P', 'PEC clinique du VIH /SIDA - Recommendations pour les milieux', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1671, 'L009OBSM02F-P', 'Soins obstétricaux et néonatals essentiels', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1672, 'L014DRUM01F-P', 'Médicaments essentiels - guide pratique dutilisation', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1673, 'L028STHM16E-P', 'PEP: proced.to be followed i.c.o. accid.exposure to blood(H)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1674, 'L030HYGM01F-P', 'Lhygiène dans les soins de santé. Guide pour  projets MSF.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1675, 'L045CATM20EFP', 'SET de CATALOGUES MSF (7 catalogues), an/fr, A4', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1676, 'NFOSRUTFCEB51', 'RUTF (BP100), biscuit céréales, 510 g (= 9 barres)', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1677, 'NFOSRUTFPEP92', 'RUTF, pâte darachide, 92 g', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1678, 'NFOSTHMI10F1', 'LAIT THERAPEUTIQUE, F100, poudre, 114 g', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1679, 'NFOSTHMI75F1', 'LAIT THERAPEUTIQUE, F75, poudre, 102,5 g', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1680, 'PCOLBOXCE1G', '(Electrolux RWC12/CF) JOINT, pour couvercle', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1681, 'PCOLBOXCE2-', 'GLACIERE (Electrolux RCW25/CF) 20,7l + 24 accumul. 0,6l, lot', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1682, 'PCOLBOXCE2G', '(Electrolux RWC25/CF) JOINT, pour couvercle', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1683, 'PCOLBOXCG6-', 'PORTE-VACCINS (GioStyle) 2,6l + 8 accumulateurs 0,4l, lot', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1684, 'PCOLBURN32W', '(Aladdin 32) MECHE, coton', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1685, 'PCOLFREEVF2', 'CONGELATEUR (Vestfrost MF214) 213l, 230V', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1686, 'PCOLFREEVF3', 'CONGELATEUR (Vestfrost MF314) 323l, 230V', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1687, 'PCOLFRESVF1G', '(Vestfrost MF114) JOINT, pour couvercle', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1688, 'PCOLGASDIB-', 'INDICATEUR DE NIVEAU, pour bouteille de gaz', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1689, 'PCOLMONI1CE', 'CARTE DE CONTROLE chaîne de froid (3M) anglais', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1690, 'PCOLMONIHLH', 'THERMO-HYGROMETRE traceur (Logtag Haxo-8) ', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1691, 'PCOLMONITLXI', '(LogTag TRIX-8) INTERFACE, LTI/USB', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1692, 'PCOLPACKW04', 'ACCUMULATEUR DE FROID vide (GioStyle) 0,4l, pour eau', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1693, 'PCOLPACKW06', 'ACCUMULATEUR DE FROID vide (Dometic) 0,6l, pour eau', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1694, 'PCOLREFAVMKBB', '(Vestfrost MK) PANIER de fond', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1695, 'PCOLREFAVMKBT', '(Vestfrost MK) PANIER supérieur, 440x220x270mm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1696, 'PCOLREFEVK2', 'REFRIGERATEUR (Vestfrost MK204) 137l, 230V', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1697, 'PCOLREFSVK1H', '(Vestfrost MK144) CHARNIERE (1510029) + ressort', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1698, 'PCOLREFSVK3G', '(Vestfrost MK304) JOINT, caoutchouc, pour couvercle', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1699, 'PCOLREFSVK3H', '(Vestfrost MK304) CHARNIERE (1510031) + ressort', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1700, 'PCOLTHER02L', 'THERMOMETRE à cristaux liquides, 0 à +20°C', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1701, 'PCOLTHER8H-', 'THERMOMETRE - HYGROMETRE affichage digital, R3/AAA', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1702, 'PCOMMEGA06C', 'MEGAPHONE, 6W, w/out C batteries', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1703, 'PELEBATTA14', 'PILE sèche alcaline (R14/C) 1,5V', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1704, 'PELEBATTR06', 'PILE rechargeable (R6/AA) 1,2V, NiMH', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1705, 'PELEBATTR14', 'PILE rechargeable (R14/C) 1,2V, NiMH', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1706, 'PELECABL3G2A1', 'CABLE rigide, 3x2,5mm², armé, par metre', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1707, 'PELECHAR60L1', 'BATTERY CHARGER Centaur 12V/60A incl cabling', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1708, 'PELECHARDB2', 'CHARGEUR DE PILES domestiques, R3/R6/R14/R20/9V, in 220V', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1709, 'PELECHIN61212', 'BATTERY CHARGER/INVERTER, 230/12/230 V, 1200W, 60A+cabling', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1710, 'PELECOMMNA2TW', 'INTERRUPTEUR CREPUSCULAIRE, 1000W', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1711, 'PELECONNR025', 'CONNECTEUR RAPIDE Wago, 5x0,75-2,5mm², pour fil rig', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1712, 'PELEEARTB02T', 'TRESSE DE MASSE, 10mm², 280mm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1713, 'PELEEXTD0314W', 'RALLONGE MULTIPRISE, 3m/3G1,5mm²/4 Schuko 2P+T/16A + inter.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1714, 'PELEEXTD1014E', 'RALLONGE MULTIPRISE, 10m/3G1,5mm²/4prises2P+T/16A, usage ext', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1715, 'PELEEXTD2521E', 'RALLONGE, 25m, 3G2,5mm², fiche EUR, usage ext.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1716, 'PELEFIXAT202B', 'COLLIER, plastique, 2,5x200mm, tête auto-bloquante', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1717, 'PELEFUSFA05A', 'FUSIBLE à fourche (ATO normal/US) 5A', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1718, 'PELEFUSFA10A', 'FUSIBLE à fourche (ATO normal/US) 10A', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1719, 'PELEFUSFA15A', 'FUSIBLE à fourche (ATO normal/US) 15A', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1720, 'PELEFUSFA20A', 'FUSIBLE à fourche (ATO normal/US) 20A', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1721, 'PELEFUSFA30A', 'FUSIBLE à fourche (ATO normal/US) 30A', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1722, 'PELEFUSFA7A5', 'FUSIBLE à fourche (ATO normal/US) 7,5A', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1723, 'PELEFUSFM10A', 'FUSIBLE à fourche (ATO mini/mini US) 10A', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1724, 'PELEFUSFM15A', 'FUSIBLE à fourche (ATO mini/mini US) 15A', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1725, 'PELEFUSFM20A', 'FUSIBLE à fourche (ATO mini/mini US) 20A', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1726, 'PELEFUSFM30A', 'FUSIBLE à fourche (ATO mini/mini US) 30A', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1727, 'PELEFUSFM7A5', 'FUSIBLE à fourche (ATO mini/mini US) 7,5A', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1728, 'PELEFUSFMS10', 'FUSE, minitype for Suzuki Jimny, assortment 10 pcs (1623908)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1729, 'PELEGENES8S25', 'GENERATEUR ins. (SDMO T9KM) 8,6kVA 230V 50Hz, diesel + pcs', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1730, 'PELEGENSS00F1', '(SDMO) FUSE, 1A, clear glass, pour relay block', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1731, 'PELEGENSS00F3', '(Generat.SDMO) FUSE, 3,15A, clear glass, pour control panel', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1732, 'PELEGENSS00GR', '(SDMO tous modèle) RELAIS DE PRÉCHAUFFAGE (5039708A) 12V/75A', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1733, 'PELEGENSS881M', '(GENERATOR 88KVA) SDMO 1000H SPARES KIT', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1734, 'PELEINVE1212', 'INVERTER 12/1200 VA, Phoenix, Schuko incl cabling', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1735, 'PELELIGB70502', '(Bulleye) AMPOULE fluocompacte E27, 5W, 230V, 2700K', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1736, 'PELELIGB71004', 'Bulb, LED, 4000K white, E27, 1055 lumen, 230V', 1, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1737, 'PELELIGFLS2PG', 'LANTERNE rech. & sol. (SoluxLED-105) 2,5Wc, LED + charg GSM', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1738, 'PELELIGLED1', 'ECLAIRAGE DE SECOURS tube fluo., 2x18W, 230V, auton. 18W/1H', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1739, 'PELEMCBO2B103', 'DISJ. DIFFERENTIEL  courbe B, 10A/30mA, 2P modulaire', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1740, 'PELEMCBO2B163', 'DISJ. DIFFERENTIEL  courbe B, 16A/30mA, 2P modulaire', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1741, 'PELEMMCB4B16M', 'DISJONCTEUR MCB courbe B, 16A/Icu 6kA, 4P, modulaire', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1742, 'PELEMMCB4B32M', 'DISJONCTEUR MCB courbe B, 32A/Icu 6kA, 4P, modulaire', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1743, 'PELEMMCB4B63M', 'DISJONCTEUR MCB courbe B, 63A/Icu 6kA, 4P, modulaire', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1744, 'PELEMMCB4C20M', 'DISJONCTEUR MCB courbe C, 20A/Icu 6kA, 4P, modulaire', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1745, 'PELEMMCBPB10S', 'DISJONCTEUR MCB courbe B, 10A/Icu 4,5kA, 1P+N, modulaire', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1746, 'PELEMMCBPB16S', 'DISJONCTEUR MCB courbe B, 16A/Icu 4,5kA, 1P+N, modulaire', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1747, 'PELEMMCBPB20S', 'DISJONCTEUR MCB courbe B, 20A/Icu 4,5kA, 1P+N, modulaire', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1748, 'PELEMMCBPC32S', 'DISJONCTEUR MCB courbe C, 32A/Icu 4,5kA, 1P+N, modulaire', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1749, 'PELEMRCD4A63I', 'INTERRUPTEUR DIFFERENTIEL type A, 63A/30mA, 4P, inst', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1750, 'PELEPROT02M', 'PARAFOUDRE/PARASURTENSEUR, 2P, modulaire', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1751, 'PELEPROT04M', 'PARAFOUDRE/PARASURTENSEUR, 4P, modulaire', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1752, 'PELESWIC463', 'INVERSEUR DE SOURCE manuel, 4P/63A + coffret+presse étoupes', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1753, 'PELESWICC634', 'INVERSEUR DE SOURCE automatique, 63A, 3P', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1754, 'PELETERM0SDS4', 'PRISE double, 2P+E/16A, apparente, Schuko std., IP54', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1755, 'PELEVOLL216', 'LIMITEUR DE TENSION, 185/245V, 16A', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1756, 'PELEVOLS4300', 'STABILISATEUR DE TENSION, 210-400V, 30kVA, triphasé', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1757, 'PELEVOLS4450', 'STABILISATEUR DE TENSION, 210-400V, 45kVA, triphasé', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1758, 'PELEVOLS4600', 'STABILISATEUR DE TENSION, 210-400V, 60kVA, triphasé', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1759, 'PELEVOLS51C0', 'VOLTAGE STABILISOR 100KVA,3phase+voltage protection 275-450V', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1760, 'PHDWROPE03N', 'CORDE, nylon, Ø 3mm, torsadée, le mètre', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1761, 'PHDWROPE06N', 'CORDE, nylon, Ø 6mm, tressée, le mètre', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1762, 'PHDWROPE08P', 'CORDE, polypropylène, Ø 8mm, torsadée, le mètre', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1763, 'PHDWROPE14P', 'CORDE, polypropylène, Ø 14mm, torsadée, le mètre', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1764, 'PHDWWIREG11', 'FIL, acier galvanisé, Ø 1,1mm, rouleau de 50m', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1765, 'PHYGBAGR60YI', 'SAC POUBELLE, 60l, jaune, pour déchets infectieux', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1766, 'PIDEFLAG8A1', 'DRAPEAU logo MSF, 80x100cm, arabe', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1767, 'PIDESTICM30A', 'AUTOCOLLANT logo MSF, 30x65cm, arabe', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1768, 'PIDEVEST1L-', 'GILET MSF, coton, taille L, ss manche + poches', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1769, 'PIDEVEST1M-', 'GILET MSF, coton, taille M, ss manche + poches', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1770, 'PIDEVEST1X-', 'GILET MSF, coton, taille XL, ss manche + poches', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1771, 'PIDEVEST2O-', 'DOSSARD logo MSF, coton, taille unique', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1772, 'PPACTAPERT-', 'RUBAN adhésif, renforcé, translucide, rouleau', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1773, 'PSAFALARCB1', 'DETECTEUR MONOXYDE DE CARBONE (Brennestuhl BCN1221) 9V', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1774, 'PSAFALARSD-', 'DETECTEUR DE FUMEE', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1775, 'PSAFCROWN1R', 'FILET DE BALISAGE, 1x50m, rouleau', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1776, 'PSAFFIREPP1', 'EXTINCTEUR à poudre polyvalente, 1kg, pour véhicules', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1777, 'PSAFGOGGW1-', 'MASQUE DE PROTECTION, pour souder', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1778, 'PSAFGOGGW1D', '(masque de soudure) VERRE FUME, 105x50mm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1779, 'PSAFGOGGW1T', '(masque de soudure) VERRE TRANSPARENT, 105x50mm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1780, 'PSAFTAPEB52', 'RUBAN DE BALISAGE, 500m, blanc/orange, fluorescent, rouleau', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1781, 'PTOODRIFM07SE', 'JEU DOUTIL DE FRAPPE 7pc, mart./mass./burin/point., MOD.MI1', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1782, 'PTOODRIL307PS', 'FORETS, Ø4-12mm, pour béton, 7 pcs, 223.SJ7', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1783, 'PTOOFILE520', 'JEU DE LIMES manche, 5 pces, 200mm, pour métal, MOD.LIM', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1784, 'PTOOGRINBE-', 'TOURET A MEULER, Ø150mm, 230V, gros + fin grain', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1785, 'PTOOHAMMR04', 'MARTEAU RIVOIR, 28x28mm, 380g, 200H.28', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1786, 'PTOOPLIE02PD', 'JEU DE PINCES 2 pce, réglables, multigrip/a étau, MOD.PR8', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1787, 'PTOOPLIEC00I', 'PINCE UNIVERSELLE MOD. ELECTRICIEN', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1788, 'PTOOSETSES-', 'OUTILS DÉLECTRICIEN, trousse', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1789, 'PTOOSETSS36H', 'JEU DOUILLE & EMB métr, 1/4inch, 36 pc + clés hex., MOD.R1', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1790, 'PTOOWRENA027', 'CLE A MOLETTE, chromée, 8inch, max 27mm, 206mm, 113A.8C', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1791, 'SCTDBAGU2VS', 'POCHE A URINE, 2 l, valves de vidange + anti-retour, stérile', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1792, 'SCTDBAPP1--', 'BALLONNET + SONDE, HEMORRAGIE POST-PARTUM (Bakri), 500 ml', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1793, 'SCTDBRCF1A-', 'FILTRE CIRCUIT RESPIRATOIRE, 22M/15F, adulte/enfant, u.u.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1794, 'SCTDBRCF1N-', 'FILTRE CIRCUIT RESPIRATOIRE, 15M/15F,nouveau-né, u.u.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1795, 'SCTDCANN2A-', 'LUNETTES A OXYGENE, 2 embouts + tube, adulte', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1796, 'SCTDCANN2N-', 'LUNETTES A OXYGENE, 2 embouts + tube, nouveau-né', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1797, 'SCTDCANN2P-', 'LUNETTES A OXYGENE, 2 embouts + tube, enfant', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1798, 'SCTDCAUN10-', 'SONDE VESICALE, nelaton, stérile, u.u., CH 10, 40 cm', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1799, 'SCTDCAUR08F', 'SONDE VESICALE, FOLEY 2 voies, ballonnet, stérile, u.u.,CH08', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1800, 'SCTDCAUR10F', 'SONDE VESICALE, FOLEY 2 voies, ballonnet, stérile, u.u.,CH10', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1801, 'SCTDCAUR12F', 'SONDE VESICALE, FOLEY 2 voies, ballonnet, stérile, u.u.,CH12', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1802, 'SCTDCAUR14F', 'SONDE VESICALE, FOLEY 2 voies, ballonnet, stérile, u.u.,CH14', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1803, 'SCTDCAUR16F', 'SONDE VESICALE, FOLEY 2 voies, ballonnet, stérile, u.u.,CH16', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1804, 'SCTDCAUR18F', 'SONDE VESICALE, FOLEY 2 voies, ballonnet, stérile, u.u.,CH18', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1805, 'SCTDCONN7S-', 'RACCORD biconique, symétrique, Ø ext. 7-11 mm, autoclavable', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1806, 'SCTDDRTH12-', 'DRAIN THORACIQUE, stérile, u.u., CH12', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1807, 'SCTDDRTH28-', 'DRAIN THORACIQUE, stérile, u.u., CH28', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1808, 'SCTDDRTV1--', '(drainage thoracique) VALVE ANTI-RETOUR aspirante & foulante', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1809, 'SCTDEXMU10-', 'EXTRACTEUR A MUCOSITES, pour nouveau-né, CH10, stérile, u.u.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1810, 'SCTDMASO1A-', 'MASQUE FACIAL A OXYGENE, simple avec tubulure, taille adulte', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1811, 'SCTDMASO1P-', 'MASQUE FACIAL A OXYGENE, simple avec tubulure, taille pédia.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1812, 'SCTDMVAC04A', '(disp. daspiration MVA Plus) CANULE EasyGRIP, Ø 4 mm', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1813, 'SCTDMVAC05A', '(disp. daspiration MVA Plus) CANULE EasyGRIP, Ø 5 mm', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1814, 'SCTDMVAC06A', '(disp. daspiration MVA Plus) CANULE EasyGRIP, Ø 6 mm', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1815, 'SCTDMVAC07A', '(disp. daspiration MVA Plus) CANULE EasyGRIP, Ø 7 mm', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1816, 'SCTDMVAC08A', '(disp. daspiration MVA Plus) CANULE EasyGRIP, Ø 8 mm', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1817, 'SCTDMVAC09A', '(disp. daspiration MVA Plus) CANULE EasyGRIP, Ø 9 mm', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1818, 'SCTDMVAC10A', '(disp. daspiration MVA Plus) CANULE EasyGRIP, Ø 10mm', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1819, 'SCTDMVAC12A', '(disp. daspiration MVA Plus) CANULE EasyGRIP, Ø 12mm', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1820, 'SCTDMVAS1--', '(disp. daspiration MVA Plus) SET ACCESSOIRES DE RECHANGE', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1821, 'SCTDMVAS60A', 'DISPOSITIF DASPIRATION, MVA PLUS (Ipas), 60 ml, autocl.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1822, 'SCTDREDP412', 'REDON, 450ml, accordéon + AIG. ALENE stér. u.u. + DRAIN CH12', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1823, 'SCTDREDP416', 'REDON, 450ml, accordéon + AIG. ALENE stér. u.u. + DRAIN CH16', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1824, 'SCTDSYDF60C', 'SERINGUE, u.u., 60 ml, gavage, embout godet', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1825, 'SCTDTUBE052', 'TUYAU, silicone, autoclavable, Ø int. 5 mm, 25 m', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1826, 'SCTDTUBE081', 'TUYAU, silicone, autoclavable, Ø int. 8 mm, 10 m', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1827, 'SCTDTUGA06-', 'SONDE GASTRIQUE, embout conique, 125 cm, u.u., CH06', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1828, 'SCTDTUGA08-', 'SONDE GASTRIQUE, embout conique, 125 cm, u.u., CH08', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1829, 'SCTDTUGA10-', 'SONDE GASTRIQUE, embout conique, 125 cm, u.u., CH10', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1830, 'SCTDTUGA12-', 'SONDE GASTRIQUE, embout conique, 125 cm, u.u., CH12', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1831, 'SCTDTUGA16-', 'SONDE GASTRIQUE, embout conique, 125 cm, u.u., CH16', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1832, 'SCTDTUGL06-', 'SONDE GASTRIQUE, embout Luer, u.u., 40 cm, CH06', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1833, 'SCTDTUGL08-', 'SONDE GASTRIQUE, embout Luer, u.u., 40 cm, CH08', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1834, 'SCTDTUGL10-', 'SONDE GASTRIQUE, embout Luer, u.u., 60 cm, CH10', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1835, 'SCTDTUGS10-', 'SONDE GASTRIQUE, dble can. (Salem) conique, 125 cm u.u. CH10', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1836, 'SCTDTUGS16-', 'SONDE GASTRIQUE, dble can. (Salem) conique, 125 cm u.u. CH16', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1837, 'SCTDTUSU08-', 'SONDE ASPIRATION, embout conique, 50 cm, u.u., CH08', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1838, 'SCTDTUSU10-', 'SONDE ASPIRATION, embout conique, 50 cm, u.u., CH10', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1839, 'SCTDTUSU14-', 'SONDE ASPIRATION, embout conique, 50 cm, u.u., CH14', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1840, 'SCTDTUSU16-', 'SONDE ASPIRATION, embout conique, 50 cm, u.u., CH16', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1841, 'SCTDTUSY20D', 'CANULE ASPIRATION YANKAUER, aspir. régl., stérile, u.u. CH20', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1842, 'SDDCBAGP06-', 'SACHET, plastique, pour médicaments, 6 x 8 cm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1843, 'SDISMDQA1T5', 'DISINFECTANT for med. equip., QA combi, 5l tin + dosing pump', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1844, 'SDISNADC1T-', 'CHLORE, 1 g (NaDCC / dichloroisocyan. sodium 1,67 g), comp.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1845, 'SDISSUQA2B-', 'DETERGENT/DISINFECTANT for surfaces, 2 l tin + dosing pump', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1846, 'SDREBANA103', 'BANDE ADHESIVE, élastique, 10 cm x 3 m', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1847, 'SDREBANC1--', 'PANSEMENT COMPRESSIF, compr.+ bande+ barre pression, stérile', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1848, 'SDREBANE06N', 'BANDE EXTENSIBLE, non adhésive, 6 à 7 cm x 4 m', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1849, 'SDREBANE104', 'BANDE CREPE (Velpeau), 10 cm x 4 m', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1850, 'SDREBANP153', 'BANDE PLATREE, 15 cm x 3 m', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1851, 'SDREBANT1T2', 'BANDE JERSEY, TUBULAIRE, 10 cm x 25 m', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1852, 'SDREBHAE1--', 'PANSEMENT HEMOSTATIQUE (Quikclot ACS+), pièce', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1853, 'SDRECOMN7N-', 'COMPRESSE, NON TISSEE, 4 plis, 7,5 cm, non stérile', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1854, 'SDRECOMP1P-', 'COMPRESSE, TULLE, gras, 10 cm x 10 cm, stérile', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1855, 'SDRECOMP1P7', 'TULLE, gras, 10 cm x 7 m, stérile, boîte', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1856, 'SDRECOMP1S-', 'COMPRESSE DE GAZE, 10 cm, 12 plis, 17 fils, stérile', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1857, 'SDRECOMP4A4', 'COMPRESSE abdominale, 45x45 cm, 4 ép.,17 fils, stérile', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1858, 'SDRECOTW5R-', 'COTON hydrophile, rouleau, 500 g', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1859, 'SDREPADM1--', 'SERVIETTE MENSTRUELLE, usage maternité, non stérile', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1860, 'SDREPADM101', 'SLIP DE MAINTIEN POUR SERVIETTES, filet élastique', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1861, 'SDRETAPA025', 'SPARADRAP, rouleau, 2 cm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1862, 'SDRETAPA1P5', 'SPARADRAP, rouleau, perforé, 10 cm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1863, 'SDREUMBC1--', 'FIL POUR CORDON OMBILICAL, coton, rouleau, 100 m', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1864, 'SDREUMBC2--', 'PINCE POUR CORDON OMBILICAL, stérile, u.u.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1865, 'SINSBABT1--', 'POCHE A SANG, simple, CPDA1, 150 ml, u.u.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1866, 'SINSBABT2--', 'POCHE A SANG, simple, CPDA1, 250 ml, u.u.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1867, 'SINSBABT4--', 'POCHE A SANG, simple, CPDA1, 450 ml, u.u.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1868, 'SINSBABT4B4', 'POCHE A SANG Penta, CPDA1, 450 ml + 4 x 100 ml, u.u.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1869, 'SINSCONT03P', 'CONTAINER, récupération aiguilles, 0,3 l, plastique', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1870, 'SINSCONT15C', 'CONTAINER, aiguilles/seringues, 15l, carton pr incinération', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1871, 'SINSCONT1R-', 'CONTAINER REUTILISABLE POUR OBJETS TRANCHANTS, 1,2 litre', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1872, 'SINSCONT5C-', 'CONTAINER, aiguilles/seringues, 5 l, carton pr incinération', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1873, 'SINSIVPP16-', 'CATHETER IV, site dinjection, u.u. 16 G (1,7 x 55 mm), gris', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1874, 'SINSIVPP18-', 'CATHETER IV, site dinjection, u.u. 18 G (1,2 x 45 mm), vert', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1875, 'SINSIVPP20-', 'CATHETER IV, site dinjection, u.u. 20 G (1,0 x 32 mm), rose', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1876, 'SINSIVPP22-', 'CATHETER IV, site dinjection, u.u. 22 G ( 0,8 x 25 mm) bleu', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1877, 'SINSIVPP24-', 'CATHETER IV, site dinjection, u.u. 24 G (0.7 x 19 mm) jaune', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1878, 'SINSNEED19-', 'AIGUILLE, u.u., Luer, 19 G (1,1 x 40 mm) crème, IV', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1879, 'SINSNEED21-', 'AIGUILLE, u.u., Luer, 21 G (0,8 x 40 mm) vert, IM', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1880, 'SINSNEED23-', 'AIGUILLE, u.u., Luer, 23 G (0,6 x 30 mm), bleu,SC, IM enfant', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1881, 'SINSNEED26-', 'AIGUILLE, u.u., Luer, 26 G (0,45 x 13 mm), brun, ID', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1882, 'SINSNEIO16-', 'AIGUILLE INTRAOSSEUSE, Luer lock, u.u., 16G', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1883, 'SINSNEIO18-', 'AIGUILLE INTRAOSSEUSE, Luer lock, u.u., 18G', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1884, 'SINSNESD20-', 'AIGUILLE PONCTION LOMBAIRE, Luer, u.u., 20 G (0,9 x 90 mm)', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1885, 'SINSNESD22-', 'AIGUILLE PONCTION LOMBAIRE, Luer, u.u., 22 G (0,7 x 40 mm)', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1886, 'SINSSCAV21-', 'AIGUILLE EPICRAN. A AILETTES, u.u., 21G (0,8 x 19 mm), vert', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1887, 'SINSSCAV25-', 'AIGUILLE EPICRAN. A AILETTES, u.u., 25G (0,5 x 19 mm) orange', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1888, 'SINSSEBG1--', 'TRANSFUSEUR, avec filtre 200 µ, stérile, u.u.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1889, 'SINSSETI2--', 'PERFUSEUR Y, Luer lock, prise dair, stérile, u.u.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1890, 'SINSSETP150', 'PERFUSEUR, pédiatrique, à précision, stérile, u.u.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1891, 'SINSSYAI005', 'SERINGUE AUTOBLOQUANTE avec aig., u.u., vacci.,0,5 ml', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1892, 'SINSSYAI05B', 'SERINGUE AUTOBLOQUANTE avec aig., u.u., vacci. BCG, 0,05 ml', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1893, 'SINSSYDL01-', 'SERINGUE, u.u., Luer, 1 ml, graduée au 1/100ème', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1894, 'SINSSYDL02-', 'SERINGUE, u.u., Luer, 2 ml', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1895, 'SINSSYDL05-', 'SERINGUE, u.u., Luer, 5 ml', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1896, 'SINSSYDL10-', 'SERINGUE, u.u., Luer, 10 ml', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1897, 'SINSSYDL20-', 'SERINGUE, u.u., Luer, 20 ml', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1898, 'SINSSYDL60-', 'SERINGUE, u.u., Luer, 60 ml', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1899, 'SINSSYLIN01', 'SERINGUE, u.u., Luer, insuline, 100 UI/1 ml + aiguille fixe', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1900, 'SINSTRFDF4F1', 'TIGE DE FILTRE, 5 µm filtre, 4.5 cm tige, stérile, u.u.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1901, 'SLASBUFF7P2', 'TAMPON pH 7.2 (Merck), comp. pr 1 l', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1902, 'SLASETHA9B1', 'ETHANOL, dénaturé, 95%, 1 l, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1903, 'SLASFUCZ1B5', 'FUCHSIN, ZIEHL, solution, 500 ml, bot. [Merck-109215]', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1904, 'SLASGENB1P-', 'VIOLET DE GENTIANE, pr bactériologie, poudre, 100 g, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1905, 'SLASGIEM1B5', 'GIEMSA, colorant (Merck), solution, 500 ml, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1906, 'SLASGRAM4--', 'GRAM HUCKER, bactério. (RAL), 4 x 230 ml, kit', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1907, 'SLASMETA1B1', 'METHANOL, 1 l, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1908, 'SLASOILI2B5', 'HUILE IMMERSION, 50 ml, fl. compte-gouttes', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1909, 'SLASSODC9B5', 'SERUM PHYSIOLOGIQUE, NaCl, 0,9%, 5 ml, fl. plastique', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1910, 'SLASWATE1B1', 'EAU DISTILLEE, 1 l, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1911, 'SMSTCARD1HE', 'CARTE DE SANTE, anglais/français, A5 recto/verso', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1912, 'SMSTCARD4OE', 'FICHE DHOSPITALISATION, français/anglais, A3 recto/verso', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1913, 'SMSTCARI01E', 'CARTE DE VACCINATION, français/anglais, A5, recto/verso', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1914, 'SMSTCARNA1E', 'FICHE NUTRITION THERAPEUTIQUE AMBULATOIRE, anglais, A4 r/v', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1915, 'SMSTCARNI1F', 'FICHE NUTRITION THER. HOSPITALISATION, français, A3 r/v', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1916, 'SMSTCARNWWF', 'FICHE POIDS/TAILLE, Z-score OMS 2006, français, A4 r/v', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1917, 'SMSUAMNH28-', 'PERCE-MEMBRANE, polypropylène, 28 cm, stérile, u.u.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1918, 'SMSUBAGB4A-', 'SAC MORTUAIRE, plastique, blanc, 300 microns, ad., 250x120cm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1919, 'SMSUBAGB4C-', 'SAC MORTUAIRE, plastique, blanc, 300 microns, enf.,150x100cm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1920, 'SMSUBAGP16-', 'SACHET, plastique, pour carte de santé, 16 x 22 cm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1921, 'SMSUBLAN1--', 'COUVERTURE DE SURVIE, 220 x 140 cm, épaisseur 12 microns', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1922, 'SMSUBOXP1--', 'BOITE PLASTIQUE transparent,135 x 95 x 30 mm,malle durgence', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1923, 'SMSUBOXP2--', 'BOITE PLASTIQUE transparent,150 x 95 x 55 mm,malle durgence', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1924, 'SMSUBRAI1B-', 'BRACELET DIDENTIFICATION (Ident-A-Band), centre nut., bleu', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1925, 'SMSUBRAI1R-', 'BRACELET DIDENTIFICATION (Ident-A-Band), centre nut., rouge', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1926, 'SMSUBRAI1W-', 'BRACELET DIDENTIFICATION (Ident-A-Band), centre nut., blanc', 800, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1927, 'SMSUBRAI1Y-', 'BRACELET DIDENTIFICATION (Ident-A-Band), centre nut., jaune', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1928, 'SMSUCOND1--', 'PRESERVATIF MASCULIN, lubrifié + RESERVOIR, u.u.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1929, 'SMSUDEPT1W-', 'ABAISSE LANGUE de bois !', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1930, 'SMSUFILE1--', 'LIME pour ampoules', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1931, 'SMSUGLEN1M-', 'GANT DEXAMEN, nitrile, u.u., non stérile, moyen', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1932, 'SMSUGLOE1L-', 'GANT DEXAMEN, latex, u.u. non stérile, grand', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1933, 'SMSUGLOE1M-', 'GANT DEXAMEN, latex, u.u. non stérile, moyen', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1934, 'SMSUGLOE1S-', 'GANT DEXAMEN, latex, u.u. non stérile, petit', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1935, 'SMSUGLOG6--', 'GANTS GYNECOLOGIQUES, latex, u.u., stériles, paire, 6.5', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1936, 'SMSUGLOG7--', 'GANTS GYNECOLOGIQUES, latex, u.u., stériles, paire, 7.5', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1937, 'SMSUGLOG8--', 'GANTS GYNECOLOGIQUES, latex, u.u., stériles, paire, 8.5', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1938, 'SMSUGLOS65-', 'GANTS CHIRURGICAUX, latex, u.u., stériles, paire, 6,5', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1939, 'SMSUGLOS70-', 'GANTS CHIRURGICAUX, latex, u.u., stériles, paire, 7', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1940, 'SMSUGLOS75-', 'GANTS CHIRURGICAUX, latex, u.u., stériles, paire, 7,5', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1941, 'SMSUGLOS80-', 'GANTS CHIRURGICAUX, latex, u.u., stériles, paire, 8', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1942, 'SMSUGLOS85-', 'GANTS CHIRURGICAUX, latex, u.u., stériles, paire, 8,5', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1943, 'SMSUIUDE1C-', 'DISPOSITIF INTRA UTERIN, (stérilet), cuivre, TCU 380 A', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1944, 'SMSUPINS1--', 'EPINGLE DE SURETE', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1945, 'SMSURAZO1B-', '(rasor, reusable) RAZOR BLADES, disposable, 10 units, box', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1946, 'SMSURAZO1D-', 'RASOIR, jetable', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1947, 'SMSUSHIE1--', 'PROTECTEUR FACIAL, valve unidirectionnelle, u.u., PVC', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1948, 'SMSUTHER1D-', 'THERMOMETRE, ELECTRONIQUE, précision 0,1º C + étui', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1949, 'SSDTBLOC200', 'CARD, BEDSIDE CONTROL, ABO comp., 100 cards+accessories, kit', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1950, 'SSDTBLOG1A-', 'TEST GROUPE SANGUIN, anti A (Lorne), 10 ml, fl. compte-gtt', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1951, 'SSDTBLOG1AB', 'TEST GROUPE SANGUIN, anti AB (Lorne), 10 ml, fl. compte-gtt', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1952, 'SSDTBLOG1B-', 'TEST GROUPE SANGUIN, anti B (Lorne), 10 ml, fl. compte-gtt', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1953, 'SSDTBLOG1C-', 'RH CONTROLE NEGATIF (Lorne),anticorps monoclonaux, 10ml, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1954, 'SSDTBLOG1D-', 'TEST GROUPE SANGUIN, RHESUS anti D (Lorne), 10 ml, fl.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1955, 'SSDTCRYP50T', 'TEST CRYPTOCOQUES (CrAg LFA), sérum/plasma/LCR, 1test CR2003', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1956, 'SSDTDETC101', '(test rapide Determine) TAMPON, 2,5 ml, 7D2243', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1957, 'SSDTHBTE10T', 'TEST HEPATITE B AgHBs (Determine), sér/pl/st, 1 test 7D2543', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1958, 'SSDTHCTE25T', 'TEST HEPATITE C (OraQuick HCV), sér/pl/st, 1 test 1001-0270', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1959, 'SSDTHETE20T', 'TEST, HEPATITE E (Assure HEV), ser/pl/st, 1 test 0743160020', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1960, 'SSDTHIVD10T', 'TEST VIH 1 + 2 (Determine), sér/pl/st, 1 test 7D2343', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1961, 'SSDTHIVU20T', 'TEST VIH 1 + 2 (Uni-Gold), sér/pl/st, 1 test 1206502', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1962, 'SSDTMALF25T', 'TEST MALARIA HRP-2 (SD Bioline), sang total, 1 test 05FK50', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1963, 'SSDTMENT25T', 'TEST MENINGITE A, B, C, Y/W135 (Pastorex), LCR,1 test 61607', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1964, 'SSDTPREG1S-', 'TEST DE GROSSESSE RST/hCG, urine, 1 bandelette', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1965, 'SSDTSYPT30T', 'TEST SYPHILIS (SD Bioline 3.0), sér/pl/st, 1 test 06FK10', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1966, 'SSDTURIS4--', 'TEST, URINE, glucose, sang, protéine, pH, 1 bandelette', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1967, 'SSDTURIS8--', 'TEST URINE pH,dens,prot,gluc,acet,sang,nit,leuco, 1 bandel.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1968, 'SSUTSABB0T1', 'SUT. RESOR. tressé (0) aiguille 1/2 31mm ronde', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1969, 'SSUTSABB1T1', 'SUT. RES. tressé (1) aiguille 1/2 40mm ronde', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1970, 'SSUTSABB1T2', 'SUT. RESOR. tressé (1) aiguille 3/8 50 mm ronde', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1971, 'SSUTSABB20T1', 'SUT. RESOR. tressé (2/0) aiguille 1/2 30 mm ronde', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1972, 'SSUTSABB30T1', 'SUT. RESOR. tressé (3/0) aiguille 3/8 18 mm ronde', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1973, 'SSUTSABR30C1', 'SUT. RES. rapide, tressé (3/0) aiguille 1/2  26mm tri', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1974, 'SSUTSNAM1T1', 'SUT. NON RESOR. mono (1) aiguille 1/2 30mm ronde', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1975, 'SSUTSNAM20R1', 'SUT. NON RESOR. mono (2/0) aiguille 3/8 30mm tri. inv.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1976, 'SSUTSNAM30R1', 'SUT. NON RESOR. mono. (3/0) aiguille 3/8 18mm tri. inv.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1977, 'SSUTSNAM30T1', 'SUT. NON RESOR. mono. (3/0) aiguille 1/2 18mm ronde', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1978, 'SSUTSTRI001', 'SUTURE CUTANEE ADHESIVE, 6 mm, par 3', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1979, 'STSSBSDM1L-', 'TUBE, PRELEVEMENT CAPILLAIRE, K2EDTA, mauve (Microtainer)', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1980, 'STSSBSDM1R-', 'TUBE, PRELEVEMENT CAPILLAIRE,ss additif, rouge (Microtainer)', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1981, 'STSSBSVT2E-', '(s.prél.sang.) TUBE SOUS VIDE, plastique, K2EDTA, 2ml, mauve', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1982, 'STSSBSVT4S-', '(s.prél.sang.) TUBE SOUS VIDE, plastique,SERUM SEC,4ml,rouge', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1983, 'STSSBSVT5E-', '(s.prél.sang.) TUBE SOUS VIDE, plastique, K2EDTA, 4ml, mauve', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1984, 'STSSBSVVH1-', '(s.prél.sang.) CORPS PORTE TUBE avec éjecteur daiguille', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1985, 'STSSBSVVN21', '(s.prél.sang) AIGUILLE, stérile, 21G (Vacutainer)', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1986, 'STSSBSVVN23W', '(s.prél.sang) UNITE PRELEVEMENT à ailettes, 23G (Vacutainer)', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1987, 'STSSCONP030P', 'RECIPIENT PROTECTEUR, transport échantillon, plast., Ø 30 mm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1988, 'STSSCONT1S-', 'POT A PRELEVEMENT, crachoir, plastique, non stérile', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1989, 'STSSCONT6S-', 'POT A PRELEVEMENT, plast., 60ml, non stér., selles + cuiller', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1990, 'STSSCONT6U-', 'POT A PRELEVEMENT, plast., 60ml, non stérile,  urine', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1991, 'STSSLANC1D-', 'LANCETTE, u.u., stérile, pointe normale', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1992, 'STSSLANC2H-', 'LANCETTE POUR TALON, pour nourrissons', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1993, 'STSSLANCSHL1', 'LANCETTE DE SECURITE petit débit, aig. 28Gx1,6mm, bleu, u.u.', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1994, 'STSSSACC101', '(carte prélèvelement éch) PORTOIR séchage ss velcro 10537173', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1995, 'STSSSACC102', '(903 card) SACHET plastique, gaz imperméable, glissière', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1996, 'STSSSACC2--', 'SAMPLE COLLECTION CARD, 5 circles (MunktellTFN)', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1997, 'STSSTRTUCB1', 'CARYBLAIR GEL+ ECOUVILLON, embout floqué, tige plast', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1998, 'STSSTRTUTI1', 'MILIEU TRANSPORT LCR (Trans-Isolate) ', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (1999, 'STSSTRTUUTM1', 'MILIEU TRANSP. UNIVERSEL+double ECOUV.,3ml, embout polyester', 0, 'TRUE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2000, 'STSSUN62DS2', 'BOX, triple packaging, biological substance UN3373+container', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2001, 'STSSUN62DSI2', 'BOX ISOTHERMAL, triple pack, biological subst.UN3373 +cont.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2002, 'STSSUN62IS-', 'BOITE, triple emballage, matière infectieuse UN2814', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2003, 'STSSUN62ISI', 'BOITE ISOTHERME, triple emb., matière infectieuse UN2814', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2004, 'TBOALIFEJL-', 'GILET DE SAUVETAGE, taille L, 100N, pour personne de 70-90kg', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2005, 'TTRUDYA4YG1', '(DAF YA4440) JOINT (190078) ', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2006, 'TTRUDYA4YG4', '(DAF YA4440) JOINT ET ANNEAU CUIVRE, 14x20', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2007, 'TTRUDYA4YG6', '(DAF YA4440) JOINT ET ANNEAU CUIVRE, 6,5x9,5', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2008, 'TTRUDYA4YGW', '(DAF YA4440) GARNIT. (DIN3760-a 72x100x10NB-G) pour roue ar.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2009, 'TTYRINNE1623F', 'FLAP, 7,5R16', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2010, 'TTYRINNE2032B', 'CHAMBRE A AIR, 12,00x20 ou 295x20, valve coudée, pour PL', 1, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2011, 'TVEACABL25BC', 'CABLE de démarrage, 25mm², 0,3m, rouge + cosse 8,5mm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2012, 'TVEACABL25RC', 'CABLE de démarrage, 25mm², 0,3m, noir + cosse 8,5mm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2013, 'TVEACABL353P', 'CABLES de démarrage, 300A, 35mm², 3m, paire', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2014, 'TVEACLAM1N-', 'COSSE DE BATTERIE, pôle négatif', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2015, 'TVEACLAM1P-', 'COSSE DE BATTERIE, pôle positif', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2016, 'TVEAFUNN00B', 'ENTONNOIR coudé, pour kerosene', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2017, 'TVEAINVEN91', 'PANIER BAGAGES, 90x150cm, élastique + pattes', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2018, 'TVEAPUMPDES', 'POMPE electrique, pour diesel + compteur + accessoires, lot', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2019, 'TVEASCRER2-', 'MOUSTIQUAIRE protection de radiateur, pour HZJ7# Mk2', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2020, 'TVEAWINC16P', '(tire fort 1600/2500kg) GOUPILLE DE SECURITE', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2021, 'TVECADDI3EB', 'NETTOYANT MOTEUR interne, bouteille ± 350ml', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2022, 'TVECBULB1FL', 'AMPOULE clignotant, 21W, 12V', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2023, 'TVECBULB1H4', 'AMPOULE halogène H4, 12V, 3 branches', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2024, 'TVECBULB1RO', 'AMPOULE navette plafonnier, 10W, 12V, Ø ±10mm, 35mm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2025, 'TVECBULB1S3', 'JEU DAMPOULES, 12V, H4 culot, 3 pins, le coffret', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2026, 'TVECBULB1ST', 'AMPOULE veilleuse/stop, 21/5W, 12V', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2027, 'TVECFILTF1-', 'FILTRE CARBURANT (Fleetguard FS1000) ', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2028, 'TVECFLUIBJ1', 'LIQUIDE DE FREIN (J1703E/DOT3) 1l, bidon', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2029, 'TVECGREA05C', 'GRAISSE cuivre, 500ml, boîte', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2030, 'TVECOILE5405I', 'ENGINE OIL 15W40 API SL/CI, 5l, petrol / diesel, can', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2031, 'TVECOILG8902', 'HUILE TRANSMISSION, 2l, EP 80W90 GL5, bidon', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2032, 'TVECPASTWT-', 'PATE DETECTEUR DEAU, pour carburant, tube', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2033, 'TVECSEALGT-', 'PATE A JOINT, pour usage général moteur, tube', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2034, 'TVEMPOWECR-', 'CABLES mise en parallèle + cosses pour 2 batteries, paire', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2035, 'YDAF0038193', '(YA4440) MOTOR, WIPER BLADE 29124102/0038193', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2036, 'YDAF0098499', '(YA4440) GASKET for power steering pump, 0098499', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2037, 'YDAF0102124215', '(YA4440) GLOW PLUG, 0102124215', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2038, 'YDAF0107334', '(YA4440) WINDSHIELD 0107334', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2039, 'YDAF0114786', '(YA4440) FILTER ELEMENT OIL 114786', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2040, 'YDAF0166201', '(YA4440) NUT FOR TURBO ON OUTLET PIPE 166201', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2041, 'YDAF0168153', '(YA4440) HOSE COOLING, LOWER ELBOW,  0168153', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2042, 'YDAF0176628', '(YA4440) PARK BRAKE VALVE COMPLETE 176628', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2043, 'YDAF0190068', '(YA4440) WASHER, RECESSED 190068', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2044, 'YDAF0190078', '(YA4440) GASKET 190078', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2045, 'YDAF0190112', '(YA4440) Retainer, Packing 0190112', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2046, 'YDAF0201447', '(YA4440) CONTROL (A.O. TURN SIGNAL)   201.447,', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2047, 'YDAF0210219', '(YA4440) NOZZLE SEAT GASKET 210219', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2048, 'YDAF0218592', '(YA4440) NUT, PLAIN, HEXAGON 218592', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2049, 'YDAF0229525', '(YA4440) LIGHT, lens, turn signal, 0229525', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2050, 'YDAF0229963', '(YA4440) TEMPERATURE INDICATOR on dashboard (0229963)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2051, 'YDAF0233473', '(YA4440) GASKET FOR TURBO ON OUTLET MANIFOLD 233473', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2052, 'YDAF0240315', '(YA4440) BEARING, BALL, waterpump, 0240315', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2053, 'YDAF0240899', '(YA4440) RADIATOR COVER HOUSING 240899', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2054, 'YDAF0240953', '(YA4440) GASKET FOR THERMOSTAT 240953', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2055, 'YDAF0241922', '(YA4440) PULLEY, FOR DYNAMO 241922', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2056, 'YDAF0241955', '(YA4440) THERMOSTAT 241955', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2057, 'YDAF0242097', '(YA4440) WASHER for bolt fuel strainer 0242097', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2058, 'YDAF0244968', '(YA4440) HOSE COOLING, ELBOW TO EXPANSION TANK,  0244968', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2059, 'YDAF0249931', '(YA4440) engine support bracket rear 0249931', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2060, 'YDAF0255713', '(YA4440) Pin, front tow in bumper  0255713', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2061, 'YDAF0265045', '(YA4440) AIR FILTER ELEMENT  0265045', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2062, 'YDAF0312673', '(YA4440) ADAPTER, SPEEDOMETER DRIVE  0312673', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2063, 'YDAF0324723', '(YA4440) RUBBER for slide rail on glass in door, 0324723', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2064, 'YDAF0331517', '(YA4440) Flexible hose for clutch slave cylinder 0331517', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2065, 'YDAF0331815', '(YA4440) DISK, CLUTCH 331815', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2066, 'YDAF0386930', '(YA4440) TIE ROD END SHIFT MECHANISM, 0386930', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2067, 'YDAF0390487', '(YA4440) Cylinder assembly, actuating, linear  0390487', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2068, 'YDAF0500161', '(YA4440) SEAL,PLAIN ENCASED FOR FRONT DRIVE SHAFT 500161', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2069, 'YDAF0500162', '(YA4440) SEAL for front and rear wheel hub, inner, 500162', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2070, 'YDAF0500643', '(YA4440) Window Regulator LH, 500643', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2071, 'YDAF0500644', '(YA4440) Window Regulator RH, 500644', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2072, 'YDAF0508389', '(YA4440) RADIATOR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2073, 'YDAF0508473', '(YA4440) VALVE, MULTICIRCUIT AIR BRAKE, 0508473', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2074, 'YDAF0508542', '(YA4440) WATERPUMP, PULLEY 508542', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2075, 'YDAF0508718', '(YA4440) HOSE, COOLING,  0508718', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2076, 'YDAF0509162', '(YA4440) CLUTCH SLAVE CYL COMPLETE (FAG 86265-34), 0509162', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2077, 'YDAF0509424', '(YA4440) SHOCK ABSORBER, REAR,  0509424,', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2078, 'YDAF0509623', '(YA4440) STRAINER ELEMENT FUEL 0509623', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2079, 'YDAF0511188', '(YA4440) Starter motor, complete 0511188', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2080, 'YDAF0512605', '(YA4440) Door handle, 512605', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2081, 'YDAF0514455', '(YA4440) CLUTCH RELEASE BEARING 514455', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2082, 'YDAF0523803', '(YA4440) BATTERY MAIN SWITCH (RELAY) 0523803', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2083, 'YDAF0546308', '(YA4440) NUT, SELF LOCKING, HEXAGON 546308', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2084, 'YDAF0572871', '(YA4440) BRAKE DRUM 572871', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2085, 'YDAF0597517', '(YA4440) GLASS, RH&LH, SMALL ONE BESIDES WINDSHIELD, 597517', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2086, 'YDAF0597520', '(YA4440) WINDSHIELD RUBBER SEAL 597520', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2087, 'YDAF0607998', '(YA4440) TIE ROD END,STEERING RIGHT 0607998', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2088, 'YDAF0608932', '(YA4440) POMP, HAND, FUEL PUMP 608932', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2089, 'YDAF0609057', '(YA4440) SPIDER UNIVERSAL JOINT 0609057', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2090, 'YDAF0609539', '(YA4440) Relay on starter motor, 609539', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2091, 'YDAF0609921', '(YA4440) OVERHAUL KIT CLUTCH SLAVE CYLINDER 0609921', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2092, 'YDAF0612564', '(YA4440) WIPER BLADE LH, windshield, 0612564', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2093, 'YDAF0613580', '(YA4440) STUD FOR TURBO ON OUTLET PIPE 613580', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2094, 'YDAF0614915', '(YA4440) RELAIS, AIR SIGNAL 0614915/FT550HZ/24V', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2095, 'YDAF0615104', '(YA4440) SHOCK ABSORBER, FRONT, 0615104', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2096, 'YDAF0620779', '(YA4440) Spring bumper (front), 0620779', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2097, 'YDAF0621089', '(YA4440) DRAG LINK-TIE ROD 621089', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2098, 'YDAF0621798', '(YA4440) Insulator rubber, for cabin, 0621798', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2099, 'YDAF0622103', '(YA4440) BEARING ROLLER TAPERED 0622103 OU 32019X', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2100, 'YDAF0622178', '(YA4440) HOSE, COOLING, 0622178', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2101, 'YDAF0622618', '(YA4440) VISOR, SUN RH 622618', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2102, 'YDAF0628170', '(YA4440) Cylinder assembly, actuating, linear 0628170', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2103, 'YDAF0629349', '(YA4440) FLEXIBLE PIPE FRONT BRAKES 0629349', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2104, 'YDAF0629457', '(YA4440) DRIVE  MECHANISM FOR SPEEDOMETER, 0629457', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2105, 'YDAF0631425', '(YA4440) WASHER for U-bolt, 0631425', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2106, 'YDAF0633981', '(YA4440) MOUNT, insulator cabin support bridge, 0633981', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2107, 'YDAF0634179', '(YA4440) SHAFT SPEEDOMETER CABLE, FLEXIBLE    0634179', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2108, 'YDAF0636259', '(YA4440) SWITCH PUSH 636259', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2109, 'YDAF0637024', '(YA4440) SWITCH PUSH 637024', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2110, 'YDAF0645245', '(YA4440) Bush for SPRING pin 645245', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2111, 'YDAF0647006', '(YA4440) PIN, SPRING 647006', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2112, 'YDAF0656395', '(YA4440) SHOCK ABSORBER, FOR ,CABIN,  0656395', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2113, 'YDAF0668749', '(YA4440) WATER RESERVOIR, wiper installation 0668749', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2114, 'YDAF0680734', '(YA4440) WATERPUMP,   0680734', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2115, 'YDAF0683390', '(YA4440) BRAKE LINING,SET FOR FR OR RR AXLEINCLRIVETS0683390', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2116, 'YDAF0751820', '(YA4440) CLUTCH MASTER CYLINDER,  0751820', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2117, 'YDAF0754145', '(YA4440) GASKET  FOR WATERPUMP 0754145', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2118, 'YDAF0754153', '(YA4440) GASKET FOR TURBO ON OUTLET PIPE 754153', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2119, 'YDAF1212827', '(YA4440) MOTOR BRAKE', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2120, 'YDAF1318695', '(YA4440) FILTER ELEMENT FUEL MAIN 1318695', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2121, 'YDAF13225', '(YA4440) Gasket, rear axle outer shaft flange 13225', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2122, 'YDAF9253219050', '(YA4440) Actuator, spring brake  9253219050, PAIR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2123, 'YDAFD81240312671', '(YA4440) SHAFT, FOR SPEEDMETER DRIVE, D8124 0312671', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2124, 'YSDM308-000-22401', '(SDMO T6KM) AIRFILTER INLET PIPE (30800022401) ', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2125, 'YSDM308-010-11201', '(XP-T6, T9, T12, T16, T22) FILTRE A AIR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2126, 'YSDM308-010-11202', '(T33) FILTRE A AIR element', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2127, 'YSDM308-011-03201', '(SDMO T16KM) TUYAU DENTRÉE DE FILTRE A AIR (30801103201)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2128, 'YSDM313-030-02701', '(T6, T9, T16 & T33) SILENT BLOC SUPERIEUR de RADIATEUR paire', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2129, 'YSDM313-030-02901', '(T6, T8, T16 & T33) SILENT BLOC INFERIEUR de RADIATEUR paire', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2130, 'YSDM330-170-039', '(SDMO generator, T6KM) THERMOSTAT SEAL 330170039', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2131, 'YSDM330-170-049', '(SDMO generator, T9,16KM) THERMOSTAT 330170049', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2132, 'YSDM330-170-099', '(T6KM,XP-T6KM&SDMO à bouch sup) COURROIE VENTIL (330170099)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2133, 'YSDM330-170-193', '(SDMO generator, T9,16KM) RADIATOR CAP 330170193', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2134, 'YSDM330-170-302', '(SDMO generator T6KM) RADIATOR  CAP 330170302', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2135, 'YSDM330-170-335', '330 170 335  COURROIE VENTILATEUR  SDMO T33K', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2136, 'YSDM330-170-336', '(GENERATOR 30KVA T33K SDMO) ROCKER COVER GASKET 330170336', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2137, 'YSDM330-170-340', '(Generator 10.7 KVA&T9KM SDMO) ROCKER COVER GASKET 330170340', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2138, 'YSDM330-170-341', '(SDMO XP-T9KM/T16K) COURROIE VENTILATEUR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2139, 'YSDM330-170-342', '(SDMO T6KM & 7.5KVA) JOINT DE CULBUTEUR (330170342)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2140, 'YSDM330-170-359', '(SDMO generator, T6KM) THERMOSTAT 330170359', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2141, 'YSDM330-170-377', '(SDMO generator, T33KM) THERMOSTAT 330170377 (ONE OF TWO)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2142, 'YSDM330-170-389', '(SDMO generator, T9,16KM) THERMOSTAT SEAL 330170389', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2143, 'YSDM330-171-211', '(SDMO generator, T33KM) THERMOSTAT 330171211 (SECOND OF TWO)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2144, 'YSDM330-510-019', '(SDMO XP-T6KM/XP-T9KM/XP-T16K) FILTRE A HUILE', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2145, 'YSDM330-510-039', '330 510 039  FILTRE à HUILE SDMO T6-T9-T12-T16K', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2146, 'YSDM330-510-049', '330 510 049  FILTRE à HUILE SDMO T33K & T44K', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2147, 'YSDM342-610-9011', '(Generat.SDMO T6-9-16KM) FILTRE CARBURANT, petit, pour pompe', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2148, 'YSDM503-997-8A', '(GENERATOR, SDMO ALL MODELS)  Relay start (Wehrle 12V/70A)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2149, 'YSDM504-209-8A', '(GENERATOR, SDMO ALL MODELS) Change relay(Wehrle 12V 20/30A)', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2150, 'YTOY04234-68010', '(HZJ/78/79) PREFILTRE à gasoil', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2151, 'YTOY04313-28020', '(HZJ78/79) RECHANGES cylindre récepteur embrayage, jeu', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2152, 'YTOY04434-60080', '(HZJ78/79) JOINTS moyeu dessieu AV, jeu', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2153, 'YTOY04445-60070', '(HZJ78/79) JOINTS boîtier de direction assistée, jeu', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2154, 'YTOY04446-60130', '(HZJ78/79 Mk2) JOINTS pompe direction assistée, jeu', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2155, 'YTOY04465-60340', '(HZJ78/79) PLAQUETTES frein de disque, AV, set', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2156, 'YTOY04479-60070', '(HZJ78/79) REPARATION DETRIER frein à disque, AV, jeu', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2157, 'YTOY04483-60200', '(HZJ78/79) RESSORT A LAME, JUMELLE AR, jeu', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2158, 'YTOY04495-60070', '(PZ/HZJ75/78/79) MACHOIRES de frein, AR, jeu', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2159, 'YTOY04945-60030', '(HZJ78/79) CALES ANTI-GRICEMENT frein à disque, AV, jeu', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2160, 'YTOY04947-60090', '(HZJ78/79) FIXATION PLAQUETTES frein à disque, AV, jeu', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2161, 'YTOY09111-60092', '(HZJ78/79) CRIC, SANS ACCESSOIRES mécanique', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2162, 'YTOY11176-64010', 'SIEGE DINJECTEUR, rondelle cuivre PZ/HZ', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2163, 'YTOY11177-64010', '(HZJ78/79 avant 99/10) JOINT de siège dinjecteur', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2164, 'YTOY11328-17030', '(HZJ75/78/79) JOINT couvercle de pignons de distr.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2165, 'YTOY12157-10010', '(HZJ75/78/79) JOINT bouchon de vidange&niveau, pont AV&AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2166, 'YTOY12371-17080', '(HZJ78/79) ISOLANT daccouplement moteur, nº1, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2167, 'YTOY13568-19195', '(HZJ78/79 après 98/05) COURROIE DISTRIBUTION chaque100.000km', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2168, 'YTOY16100-19235', '(HZJ78/79) POMPE A EAU', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2169, 'YTOY16210-17070', '(HZJ78/79 après99/08) ACCOUP. HYDRAULIQUE ventil. radiateur', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2170, 'YTOY16361-17040', '(HZJ78/79) HELICE VENTILATEUR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2171, 'YTOY16401-67150', '(HZJ78/79) BOUCHON de radiateur', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2172, 'YTOY16611-17011', '(HZJ78/79) PLATINE réservoir radiateur', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2173, 'YTOY16711-17140', '(HZJ78/79 Mk1) BOUCLIER de ventilateur', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2174, 'YTOY17730-61010', 'SCHNORKEL epurateur dair, pr PZ>4999 HZ>9147, pce', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2175, 'YTOY17730-66010', '(HZJ78/79) PRE-EPURATEUR air, haut du schnorkel', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2176, 'YTOY17801-75010', 'FILTRE A AIR element, sec labable', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2177, 'YTOY19850-17020', '(HZJ78/79) BOUGIE DE PRECHAUFFAGE, 11-12V', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2178, 'YTOY23344-54010', '(HZJ78/79) JOINT vis purge, filtre decanteur', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2179, 'YTOY23380-17451', '(HZJ78/79 Mk1) CHAPEAU filtre à carburant', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2180, 'YTOY23382-51020', '(HZJ78/79 Mk2) CORPS FILTRE indicateur présence eau', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2181, 'YTOY23388-64470', '(HZJ78/79) VIS DE PURGE filtre décanteur gasoil', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2182, 'YTOY23390-64480', '(HZJ78/79 Mk1) ELEMENT DE FILTRE décanteur de gasoil', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2183, 'YTOY23654-64010', '(HZJ78/79) RONDELLE supérieure dinjecteur, aluminium', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2184, 'YTOY27370-75060', '(HZJ7#) PORTE-BALAIS dalternateur, set', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2185, 'YTOY27371-70300', '(LandCruiser) PORTE-BALAIS dalternateur, jeu', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2186, 'YTOY28226-54220', '(HZJ78/79) CONTACT DE DEMARREUR borne moteur', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2187, 'YTOY28521-17190', '(HZJ78/79) TEMPORISATEUR de préchauffage', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2188, 'YTOY28601-17020', '(HZJ78/79) RELAIS SECONDAIRE bougie préchauffage', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2189, 'YTOY31210-36330', '(HZJ78/79) MECANISM dembrayage, jeu', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2190, 'YTOY31478-30010', '(HZJ78/79) CAPUCHON vis de purge de frein AV&AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2191, 'YTOY33030-6A412', '(HZJ78/79) BOÎTE DE VITESSE complete', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2192, 'YTOY41231-60030', '(HZJ78/79 Mk1) ENTRETOISE palier de differentiel, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2193, 'YTOY41231-60040', '(HZJ78/79 Mk2) ENTRETOISE palier de differentiel, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2194, 'YTOY42181-60060', '(HZJ78/79) JOINT papier, differentiel carter pont, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2195, 'YTOY42181-60090', '(HZJ78/79) JOINT papier, differentiel carter pont, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2196, 'YTOY42323-60030', '(HZJ78/79) RONDELLE moyeu de roue conique AV&AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2197, 'YTOY42419-60011', '(HZJ78/79) VIS blocage roulement dessieu AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2198, 'YTOY42427-60021', '(HZJ78/79) ECROU blocage roulement dessieu AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2199, 'YTOY42428-60011', '(HZJ78/79) PLAQUE blocage roulement dessieu, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2200, 'YTOY42431-0K090', '(KUN15/LAN15) TAMBOUR DE FREIN AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2201, 'YTOY42431-60250', '(HZJ78/79) TAMBOUR DE FREIN AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2202, 'YTOY42610-60320', 'JANTE (5.50F-16) pour HZJ78/79, pce', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2203, 'YTOY43204-60032', '(HZJ78/79) JOINTS pivot de direction, jeu', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2204, 'YTOY43401-60081', '(HZJ78/79) FUSEE dessieu, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2205, 'YTOY43422-60070', '(HZJ78/79) JOINT EXTERIEUR papier, moyeu, AV&AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2206, 'YTOY43423-35010', '(HZJ78/79) CAPUCHON papier, cache poussières, moyeu AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2207, 'YTOY43512-60141', '(HZJ78/79) DISQUE DE FREIN AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2208, 'YTOY43521-60011', '(HZJ78/79) ECROU de réglage, roulement de moyeu AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2209, 'YTOY45457-60010', '(HZJ78/79) JOINT CACHE POUSSIERE rotule, réglable RHDrive', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2210, 'YTOY45458-60010', '(HZJ78/79) JOINT CACHE POUSSIERE rotule, réglable LHDrive', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2211, 'YTOY45472-60020', '(HZJ7#) BOUTON joint barre direction', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2212, 'YTOY45473-35010', '(HZJ78/79) SIEGE', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2213, 'YTOY45700-69165', '(HZJ78/79 Mk1) AMORTISSEUR de direction', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2214, 'YTOY46410-60860', '(HZJ79) CABLE frein à main, LHDrive', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2215, 'YTOY47062-60020', '(HZJ78/79) RATTRAPAGE auto.&ressort rappel, AR, GA', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2216, 'YTOY47324-60330', '(HZJ78/79) TUYAU frein au cylindre, AR, DR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2217, 'YTOY47447-60010', '(HZJ78/79) GOUPILLE de maintien, mâchoires frein, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2218, 'YTOY47449-30020', '(HZJ78/79) CUVETTE de mainien, mâchoires de frein', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2219, 'YTOY47491-20011', '(HZJ78/79) BOUCHON DORIFICE oblong, frein AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2220, 'YTOY47492-28010', '(HZJ78/79) BOUCHON DORIFICE oblong, frein AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2221, 'YTOY47547-60020', '(HZJ78/79) BLEEDER PLUG Front disk brake & Rear drum', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2222, 'YTOY47616-35030', '(HZJ78/79) CABLE frein à main, intérieur tambour', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2223, 'YTOY47643-60010', '(HZJ78/79) LEVIER REGLAGE AUTOMATIQUE frein AR, DR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2224, 'YTOY47644-60010', '(HZJ78/79) LEVIER DE REGLAGE automatique, frein AR, GA', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2225, 'YTOY48061-60050', '(HZJ78/79) SILENT BLOC bras inférieur au châssis', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2226, 'YTOY48131-6B340', '(HZJ78/79) RESSORT A SPIRALE suspension, renforcée, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2227, 'YTOY48247-36720', '(HZJ79) BOULON centrage pour 8 lames, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2228, 'YTOY48284-37130', '(HZJ78/79) BOULON centrage, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2229, 'YTOY48304-60110', '(HZJ78/79) BUTEE DE PONT AV, DR & GA', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2230, 'YTOY48306-60160', '(HZJ78/79) BUTEE DE PONT AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2231, 'YTOY48310-60101', '(HZJ78/79) BOUDIN butée interne ressort, AV, DR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2232, 'YTOY48310-60111', '(HZJ78/79) BOUDIN butée interne ressort, AV, GA', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2233, 'YTOY48509-60040', '(HZJ78/79) RONDELLE Nº2 siège silent-bloc, amortisseur, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2234, 'YTOY48511-69675', '(HZJ78/79 Mk2) AMORTISSEUR AV, DR & GA', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2235, 'YTOY48531-69855', '(HZJ78/79 Mk2) AMORTISSEUR AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2236, 'YTOY48531-80725', 'AMORTISSEUR, AR, pr LH202, par pièce', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2237, 'YTOY48702-60050', '(HZJ78/79/80) SILENT BLOC bras inférieur au pont AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2238, 'YTOY48706-60030', '(HZJ78/79) SILENT BLOC barre panhard, AV, DR, latéral', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2239, 'YTOY48706-60070', '(HZJ78/79) SILENT BLOC barre panhard, AV, GA, latéral', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2240, 'YTOY48802-60090', '(HZJ78/79) ROTULE barre stabilisatrice, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2241, 'YTOY48802-60120', '(HZJ78/79 apres 09/04) ROTULE barre stabilisatrice, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2242, 'YTOY48815-0K050', 'SILENT BLOC barre stabilisatrice, AV, pr LAN15, pce', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2243, 'YTOY48815-26020', '(HZJ78/79) SILENT BLOC central barre stabilisatrice, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2244, 'YTOY48815-60170', '(HZJ78/79) SILENT BLOC barre stabilisatrice, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2245, 'YTOY48817-30010', '(HZJ78/79) SILENT BLOC tige barre stabilisatrice, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2246, 'YTOY48823-60030', '(HZJ78/79) ETRIER barre stabilisatrice, AV&AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2247, 'YTOY48824-60130', '(HZJ78/79) PLATINE barre stabilisatrice, nº1, AV, DR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2248, 'YTOY48829-60080', '(HZJ78/79) PLATINE barre stabilisatrice, nº1, AV, GA', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2249, 'YTOY53382-60011', '(HZJ78/79 Mk1) BUTEE de capot moteur, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2250, 'YTOY53382-60030', '(HZJ78/79 Mk2) BUTEE de capot moteur, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2251, 'YTOY53384-60030', '(HZJ78/79 Mk1) BUTEE capot, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2252, 'YTOY53384-60060', '(HZJ78/79 Mk2) BUTEE capot, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2253, 'YTOY53455-14020', '(HZJ78/79 Mk1) AGRAFFE tige support capot moteur', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2254, 'YTOY53455-60020', '(HZJ78/79 Mk2) AGRAFFE tige support capot moteur', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2255, 'YTOY56111-60A80', '(HZJ78/79 Mk2 après 2009/07) PARE BRISE verre collé', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2256, 'YTOY56114-60020', '(HZJ78/79 Mk2 après 2009/07) ARRETOIR pare-brise collé', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2257, 'YTOY56121-60120', '(HZJ78/HZJ79) JOINT DETANCHEITE pare-brise', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2258, 'YTOY56121-60140', '(HZJ78/79 Mk2après 2009/07) JOINT DETANCH. pare-brise collé', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2259, 'YTOY68617-20011', '(HZJ78/79) GOUPILLE darrêt porte, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2260, 'YTOY68617-90K00', '(HZJ78/79) GOUPILLE darrêt porte, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2261, 'YTOY69041-60011', '(HZJ78) BUTEE porte, supérieure, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2262, 'YTOY69058-60141', '(HZJ75/79) BOUCHON A CLE réservoir carburant', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2263, 'YTOY69212-60030', '(HZJ75/78) POIGNEE porte intérieur, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2264, 'YTOY69759-20070', '(HZJ78/79) CLIP ARRET MECANISME porte intérieur, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2265, 'YTOY74310-60A60-B1', '(HZJ78/79) PARE-SOLEIL gris, GA', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2266, 'YTOY74320-60300-B0', 'PARE SOLEIL GAUCHE GRIS HZJ78/79 Mk1', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2267, 'YTOY74320-60301-B0', '(HZJ78/79, after 2007/01) PARE-SOLEIL gris, GA', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2268, 'YTOY77001-60552', '(HZJ78/79) RESERVOIR AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2269, 'YTOY77310-12150', '(HZJ78) CAPUCHON réservoir carburant, sans clé', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2270, 'YTOY77601-60261', '(HZJ78/79) SANGLE réservoir additionnel', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2271, 'YTOY77710-60190', '(HZJ78/79 Mk2) SOLENOIDE réservoir, commande princ.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2272, 'YTOY77720-60130', '(HZJ78/79) SOLENOIDE réservoir, retour comm.', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2273, 'YTOY81130-60C40', '(HZJ78/79 Mk2) BLOC DE PHARE DR pour LHDrive', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2274, 'YTOY81170-60C00', '(HZJ78/79 Mk2) BLOC DE PHARE GA pour RHDrive', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2275, 'YTOY81170-60C10', '(HZJ78/79 Mk2) BLOC DE PHARE GA pour LHDrive', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2276, 'YTOY81511-60520', '(HZJ78/79 Mk1) CABOCHON de feu, AV, DR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2277, 'YTOY81521-60390', '(HZJ78/79 Mk2) CABOCHON de feu, AV, GA', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2278, 'YTOY81551-60451', '(HZJ79) CABOCHON de feu, DR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2279, 'YTOY81551-90K09', '(HZJ78) CABOCHON de feu, DR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2280, 'YTOY81561-90K09', '(HZJ78) CABOCHON de feu, GA', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2281, 'YTOY83320-69335', '(HZJ78/79) SONDE jauge à combustible, réservoir AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2282, 'YTOY83320-69355', 'Fuel gauge assy for FRONT main tank', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2283, 'YTOY84312-32010', '(HZJ78/79) DOIGT DE CONTACT avertisseur sonore', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2284, 'YTOY84461-60080', '(HZJ78/79 Mk1) CONTACTEUR sonde deau, fuel décanteur', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2285, 'YTOY85212-60031', 'Blade Assy, windshield Wiper, LH&RH, HZJ 78/79, MK1&2', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2286, 'YTOY85212-BZ160', 'BALAI ESSUI GLACE   F651', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2287, 'YTOY85222-0K020', 'BALAI DESSUI GLACE GAUCHE  LAN25', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2288, 'YTOY85440-60140', '(HZJ78/79) SOLENOIDE commande roue AV motorice', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2289, 'YTOY88440-35010', '(HZJ78/79) POULIE courroie compressor climatiseur', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2290, 'YTOY90105-18006', '(HZJ78/79) VIS bras conducteur inférieur au chassis', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2291, 'YTOY90113-10003', '(HZJ78/79) VIS tambours de frein, tête fraisée, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2292, 'YTOY90116-10201', '(HZJ78/79) GOUJON moyeu de roue, AV&AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2293, 'YTOY90117-14002', '(HZJ78/79) ETRIER EN U paquet 8 lames, AR, 210mm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2294, 'YTOY90119-10765', '(HZJ78/79) VIS extrémité barre strabilisatrice, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2295, 'YTOY90119-16003', '(HZJ78/79) VIS bras inférieur au barre Panhard, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2296, 'YTOY90126-10006', '(HZJ78/79) GOUJON tuyau déchappement', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2297, 'YTOY90170-10039', '(HZJ78/79) RONDELLE moyeu de roue AV & AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2298, 'YTOY90170-18007', '(HZJ78/79) ECROU bras inférieur au chassis', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2299, 'YTOY90175-08008', '(HZJ78/79) ECROU PAPILLON couvercle filtre à air', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2300, 'YTOY90177-22003', '(HZJ78/79) ECROU engrenage, nez de pont, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2301, 'YTOY90179-10070', '(HZJ78/79) ECROU tuyau déchappement au collecteur', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2302, 'YTOY90179-14023', '(HZJ78/79) ECROU de cavalier, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2303, 'YTOY90179-16015', '(HZJ78/79) ECROU bras inférieur au barre Panhard, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2304, 'YTOY90179-22016', '(HZJ78/79) ECROU nez de pont, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2305, 'YTOY90189-04024', '(HZJ78/79) CAPUCHON bougie préchauffage', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2306, 'YTOY90201-10075', '(HZJ78/79) ECROU moyeu de roue AV & AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2307, 'YTOY90201-15012', '(HZJ78/79) RONDELLE boulon cavalier', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2308, 'YTOY90201-19011', '(HZJ78) RONDELLE bras inférieur au chassis', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2309, 'YTOY90213-07010', '(HZJ78/79) RONDELLE C, frein AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2310, 'YTOY90214-42030', '(HZJ78/79) RONDELLE DAPPUI DECROU rouleau Moyeu, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2311, 'YTOY90215-42025', '(HZJ78/79) RONDELLE FREIN DECROU moyeu de roue, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2312, 'YTOY90310-35010', '(HZJ78/79) JOINT SPI arbre de pont, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2313, 'YTOY90310-36003', '(HZJ78/79) JOINT SPI arbre de pont, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2314, 'YTOY90311-38047', '(HZJ78/79 avant 2002/02) JOINT SPI nez de pont, AV&AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2315, 'YTOY90311-41009', '(HZJ78/79 après 2002/02) JOINT SPI nez de pont, AV&AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2316, 'YTOY90311-45028', '(HZJ78/79 après 2006/09) JOINT SPI nez de pont, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2317, 'YTOY90311-48010', '(HZJ78/79) JOINT SPI sortie boîte transfert, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2318, 'YTOY90311-48022', '(HZJ78/79) JOINT SPI boîte de transfert, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2319, 'YTOY90311-48023', '(HZJ78/79 après 2003/03) JOINT SPI boîte de transfert, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2320, 'YTOY90311-62001', '(HZJ78/79) JOINT SPI moyeu de roue, AV & AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2321, 'YTOY90341-12012', '(HZJ78/79) BOUCHON VIDANGE huile de moteur', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2322, 'YTOY90341-18006', '(PZ-HZJ78/79) BOUCHON NIVEAU ponts, BV & transmission', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2323, 'YTOY90341-18057', '(PZ-HZJ78/79) BOUCHON VIDANGE ponts, BV & transmission', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2324, 'YTOY90363-12010', '(HZJ78/79) ROULEMENT arbre primaire dembrayage', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2325, 'YTOY90364-33011', '(HZJ78/79) ROULEMENT à rouleau, fusée dessieu', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2326, 'YTOY90381-35001', '(HZJ78/79) MANCHON fusée dessieu', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2327, 'YTOY90385-11021', '(HZJ78/79) SILENT BLOC DEXTREMITE barre stabil., AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2328, 'YTOY90385-13009', '(HZJ78/79) SILENT BLOC DEXTREMITE barre stabil., AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2329, 'YTOY90385-18021', '(HZJ78/79) SILENT BLOC inférieur lames jumelles, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2330, 'YTOY90385-18022', '(HZJ78/79) SILENT BLOC supérieur lamer jumelles, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2331, 'YTOY90385-19003', '(HZJ78/79) SILENT BLOC armortisseur, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2332, 'YTOY90385-T0009', 'SILENT-BLOC haut jumelle, AR, pr LAN15, pce', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2333, 'YTOY90389-14056', '(HZJ78/79) SILENT BLOC lames de ressort extérieur, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2334, 'YTOY90389-22003', '(HZJ78/79) SILENT BLOC lames de ressort intérier, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2335, 'YTOY90416-06004', '(HZJ78/79) GRAISSEUR croisillon arbre transmission, coudé', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2336, 'YTOY90430-12031', '(PZ/HZJ78/79) JOINT bouchon de vidage moteur', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2337, 'YTOY90430-12245', '(HZJ78/79) JOINT bouchon de vidage réservoir', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2338, 'YTOY90430-18008', '(HZJ78/79) JOINT bouchon de vidange & niveau', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2339, 'YTOY90467-12031-06', 'CLIP GARNITURE INTERIEUR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2340, 'YTOY90467-32002', '(HZJ78/79) CLIP cache poussiere rotule réglable', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2341, 'YTOY90468-08044', '(HZJ78/79) CLIP darrêt flexible de frein', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2342, 'YTOY90501-20012', '(HZJ78/79) RESSORT de maintien, mâchoires de frein AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2343, 'YTOY90506-12031', '(HZJ78/79) RESSORT TENDEUR levier ratrap., frein AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2344, 'YTOY90506-18031', '(HZJ78/79) RESSORT TENDEUR petit, mâchoires frein AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2345, 'YTOY90506-20033', '(HZJ78/79) RESSORT TENDEUR frein à main, levier coudée', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2346, 'YTOY90520-31007', '(HZJ78/79) CIRCLIPS arbre de transmission, AV, 2,4mm', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2347, 'YTOY90541-07023', '(HZJ78) SILENT BLOC DE BUTEE porte, bas, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2348, 'YTOY90541-09006', '(HZJ78/79) SILENT BLOC DE BUTEE porte, AV, DR & GA', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2349, 'YTOY90541-09069', '(HZJ78/79 Mk2) SILENT BLOC DE BUTEE capot moteur, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2350, 'YTOY90560-10044', '(HZJ78/79) ENTRETOISE extrémité barre stabilisatrice, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2351, 'YTOY90560-10275', '(HZJ78/79) ENTRETOISE extrémité barre stabil. AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2352, 'YTOY90915-TD004', 'FILTRE À HUILE, 2, 3 & 5l, jeu', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2353, 'YTOY90916-03089', '(HZJ78/79) THERMOSTAT', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2354, 'YTOY90917-06059', '(HZJ78/79) JOINT déchappement, sortie de collecteur', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2355, 'YTOY90942-02083', '(HZJ78/79) GOUJON de roue, AV & AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2356, 'YTOY90948-02126', '(HZJ78/79) RONDELLE inf.&sup., tige barre stabil., AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2357, 'YTOY90948-02127', '(HZJ78/79) RONDELLE intermediaire, tige barre stabil., AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2358, 'YTOY90948-02141', '(HZJ78/79) RONDELLE siège silent-bloc amort. N1, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2359, 'YTOY90948-03011', '(HZJ78/79) RONDELLE damortisseur, AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2360, 'YTOY90981-01037', '(HZJ78/79 Mk1) PHARE semi-scellé ampoule H4', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2361, 'YTOY94184-01001', '(HZJ78/79) ECROU extrémité barre strabilisatrice, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2362, 'YTOY94184-61200', '(HZJ78/79) ECROU direction & amortisseur, AV', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2363, 'YTOY96132-41500', '(HZJ78/79) COLLIER RAPIDE tuyau gasoil retour', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2364, 'YTOY96132-51100', '(HZJ78/79) COLLIER RAPIDE tuyau gasoil aller&reniflard', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2365, 'YTOY96160-00500', '(HZJ78/79) ANNEAU EN E frein AR', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2366, 'YTOY96451-00600', 'GRAISSEUR, droit, pr PZ/HZJ, pce', 0, 'FALSE')");
        db.execSQL("INSERT INTO Product (id, code, description, sud, is_batch_managed) VALUES (2367, 'YTOY96940-39855', 'FLEXIBLE DE FREIN AV PZ/HZ', 0, 'FALSE')");
        db.execSQL("COMMIT");
        db.close();

    }
    //endregion

}
