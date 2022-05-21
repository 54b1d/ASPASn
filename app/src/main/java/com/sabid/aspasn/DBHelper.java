package com.sabid.aspasn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sabid on 4/27/22.
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "UserData.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        // Setup Database
        DB.execSQL("CREATE TABLE accountGroups (id INTEGER primary key UNIQUE," +
                "groupName TEXT NOT NULL UNIQUE);");
        DB.execSQL("CREATE TABLE accounts (id INTEGER primary key UNIQUE, name TEXT NOT NULL UNIQUE," +
                   "address	TEXT DEFAULT 'NA', mobile TEXT DEFAULT 0, accountGroup TEXT NOT NULL);");

        DB.execSQL("CREATE TABLE transactions(id INTEGER primary key UNIQUE, date	TEXT NOT NULL," +
                   "accountId	INTEGER NOT NULL, invoiceId INTEGER DEFAULT 0," +
                   "lineItemId	INTEGER DEFAULT 0, purchaseSale TEXT DEFAULT 'NA'," +
                   "quantity	NUMERIC DEFAULT 0, debit	NUMERIC DEFAULT 0, credit	NUMERIC DEFAULT 0);");
        DB.execSQL("CREATE TABLE invoices (id	INTEGER primary key UNIQUE);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
    }

    public Boolean insertuserdata(String name, String address, String mobile, int accountGroupId) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("address", address);
        contentValues.put("mobile", mobile);
        contentValues.put("accountGroup", accountGroupId);
        long result = DB.insert("accounts", null, contentValues);
        return result != -1;
    }

    public Boolean insertAccountCategory(String Categoryname) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("groupName", Categoryname);

        long result = DB.insert("accountGroups", null, contentValues);
        return result != -1;
    }

    public Boolean updateuserdata(String name, String contact, String dob) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("contact", contact);
        contentValues.put("dob", dob);
        Cursor cursor = DB.rawQuery("Select * from Userdetails where name=?", new String[] {name});
        if (cursor.getCount() > 0) {
            long result = DB.update("Userdetails", contentValues, "name=?", new String[] {name});
            return result != -1;
        } else {
            return false;
        }
    }

    public Boolean deletedata(String name) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Userdetails where name=?", new String[] {name});
        if (cursor.getCount() > 0) {
            long result = DB.delete("Userdetails", "name=?", new String[] {name});
            return result != -1;
        } else {
            return false;
        }
    }

    public Cursor getAccountCategories() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor Cursor = DB.rawQuery("select * from accountGroups", null);
        return Cursor;
    }

    public Cursor getAccounts() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor Cursor = DB.rawQuery("select * from accounts", null);
        return Cursor;
    }

    public Cursor getGroupId(String groupName) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select id from accountGroups where groupName=?", new String[]{groupName});
        return cursor;
    }

    public Cursor getAccountId(String accountName) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select id from accountGroups where groupName=?", new String[]{accountName});
        return cursor;
    }

    public boolean insertTransaction(String date, int accountId, int invoiceId, int lineItemId, String purchaseSale, int quantity, int debit, int credit) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        contentValues.put("accountId", accountId);
        contentValues.put("invoiceId", invoiceId);
        contentValues.put("lineItemId", lineItemId);
        contentValues.put("purchaseSale", purchaseSale);
        contentValues.put("quantity", quantity);
        contentValues.put("debit", debit);
        contentValues.put("credit", credit);
        long result = DB.insert("transactions", null, contentValues);
        return result != -1;
    }
}

