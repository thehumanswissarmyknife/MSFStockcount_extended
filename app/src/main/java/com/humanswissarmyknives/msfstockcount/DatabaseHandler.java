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
    }

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

    void addCountedItem(CountedItem countedItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (countedItem.getId() == 0) {
            countedItem.setId(getMaxCountItemId() + 1);
        }

        ContentValues values = new ContentValues();
        values.put(KEY_COUNTED_ID, countedItem.getId());
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
                        cursor.getInt(cursor.getColumnIndex(KEY_PRODUCT_BATCHMANAGED)));      // sud
                productList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return productList;
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
        ArrayList<CountedItem> batchList = new ArrayList<>();

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

    private int getMaxCountItemId() {
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

    private int getMaxBatchId() {
        int maxId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX( " + KEY_BATCH_ID + " ) FROM " + TABLE_BATCHES, null);

        cursor.moveToLast();
        maxId = cursor.getInt(0);

        cursor.close();
        return maxId;
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

    private int getMaxWarehouseId() {
        int maxId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX( " + KEY_WAREHOUSE_ID + " ) FROM " + TABLE_WAREHOUSES, null);

        cursor.moveToLast();
        maxId = cursor.getInt(0);

        cursor.close();
        return maxId;
    }

    private int getMaxReportingListId() {
        int maxId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX( " + KEY_LIST_ID + " ) FROM " + TABLE_REPORTINGLISTS, null);

        cursor.moveToLast();
        maxId = cursor.getInt(0);

        cursor.close();
        return maxId;
    }

}
