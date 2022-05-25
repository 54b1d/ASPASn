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
    String tableName;

    @Override
    public void onCreate(SQLiteDatabase DB) {
        // Setup Database
        DB.execSQL("CREATE TABLE [clientEntity] ( [clientId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, [clientName] TEXT NOT NULL, [clientAddress] TEXT, [clientMobile] TEXT, [accountTypeId] INTEGER NOT NULL, FOREIGN KEY([accountTypeId]) REFERENCES [accountTypeEntity]([accountTypeId]));");

        DB.execSQL("CREATE TABLE [accountTypeEntity] ( [accountTypeId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [accountTypeName] TEXT NOT NULL);");

        DB.execSQL("CREATE TABLE [purchaseInvoiceEntity] ( [purchaseInvoiceId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [date] DATE NOT NULL, [clientId] INTEGER NOT NULL, [productId] INTEGER NOT NULL, [quantity] REAL NOT NULL, [rate] REAL NOT NULL, [amount] REAL NOT NULL, FOREIGN KEY([clientId]) REFERENCES [clientEntity]([clientId]), FOREIGN KEY([productId]) REFERENCES [inventoryEntity]([productId]));");

        DB.execSQL("CREATE TABLE [paymentEntity] ( [paymentId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, [date] DATE NOT NULL, [invoiceId] INTEGER, [cashBankId] INTEGER NOT NULL, [clientId] INTEGER NOT NULL, [amount] REAL NOT NULL, FOREIGN KEY([invoiceId]) REFERENCES [purchaseInvoiceEntity]([purchaseInvoiceId]), FOREIGN KEY([cashBankId]) REFERENCES [cashBankEntity]([cashBankId]), FOREIGN KEY([clientId]) REFERENCES [purchaseInvoiceEntity]([clientId]));");

        DB.execSQL("CREATE TABLE [salesInvoiceEntity] ( [salesInvoiceId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [date] DATE NOT NULL, [clientId] INTEGER NOT NULL, [productId] INTEGER NOT NULL, [quantity] REAL NOT NULL, [rate] REAL NOT NULL, [amount] REAL NOT NULL, FOREIGN KEY([clientId]) REFERENCES [clientEntity]([clientId]), FOREIGN KEY([productId]) REFERENCES [inventoryEntity]([productId]));");

        DB.execSQL("CREATE TABLE [receiptEntity] ( [receiptId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [date] DATE NOT NULL, [invoiceId] INTEGER, [cashBankId] INTEGER NOT NULL, [clientId] INTEGER NOT NULL, [amount] REAL NOT NULL, FOREIGN KEY([invoiceId]) REFERENCES [salesInvoiceEntity]([salesInvoiceId]), FOREIGN KEY([cashBankId]) REFERENCES [cashBankEntity]([cashBankId]), FOREIGN KEY([clientId]) REFERENCES [salesInvoiceEntity]([clientId]));");

        DB.execSQL("CREATE TABLE [inventoryEntity] ( [productId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [productName] TEXT NOT NULL, [unitName] TEXT NOT NULL);");

        DB.execSQL("CREATE TABLE [expenseEntity] ( [expenseId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, [expenseTitle] TEXT NOT NULL, [accountTypeId] INTEGER NOT NULL, FOREIGN KEY([accountTypeId]) REFERENCES [accountTypeEntity]([accountTypeId]));");

        DB.execSQL("CREATE TABLE [expenseEntryEntity] ( [expenseEntryId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [expenseDate] DATE NOT NULL, [expenseId] INTEGER NOT NULL, [amount] REAL NOT NULL, FOREIGN KEY([expenseId]) REFERENCES [expenseEntity]([expenseId]));");

        DB.execSQL("CREATE TABLE [cashBankEntity] ( [cashBankId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [cashBankTitle] TEXT NOT NULL UNIQUE, [accountTypeId] INTEGER NOT NULL, FOREIGN KEY([accountTypeId]) REFERENCES [accountTypeEntity]([accountTypeId]));");

        DB.execSQL("CREATE TABLE [capitalAccountsEntity] ( [capitalAccountId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [capitalAccountTitle] TEXT NOT NULL, [accountTypeId] INTEGER NOT NULL, FOREIGN KEY([accountTypeId]) REFERENCES [accountTypeEntity]([accountTypeId]));");

        DB.execSQL("CREATE TABLE [accountingPeriods] ( [accountingPeriodId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [accountingStartDate] DATE NOT NULL UNIQUE, [accountingEndDate] DATE NOT NULL UNIQUE);");

        //create predefined accounts that are required
        //Categories Accounts for A = L + OE + Revenue - Ecpenses
        DB.execSQL("INSERT INTO [accountTypeEntity] ('accountTypeName') VALUES ('Assets'), ('Liabilities'), ('Capital'), ('Clients'), ('Incomes'), ('Expenses');");
        //Create default Cash account inside cashBank table which is always required
        DB.execSQL("INSERT INTO [cashBankEntity] ('cashBankTitle', 'accountTypeId') VALUES ('Cash', '1');");
        //Create default Capital accounts required
        DB.execSQL("INSERT INTO [capitalAccountsEntity] ('capitalAccountTitle', 'accountTypeId') VALUES ('Owners Equity', '3'), ('Retained Earnings', '3');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
    }

    public Boolean insertClientData(String name, String address, String mobile, int accountTypeId) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("clientName", name);
        contentValues.put("clientAddress", address);
        contentValues.put("clientMobile", mobile);
        contentValues.put("accountTypeId", accountTypeId);
        long result = DB.insert("clientEntity", null, contentValues);
        return result != -1;
    }

    public Boolean insertExpenseAccount(String expenseName, int accountTypeId) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("expenseTitle", expenseName);
        contentValues.put("accountTypeId", accountTypeId);
        long result = DB.insert("expenseEntity", null, contentValues);
        return result != -1;
    }

    public Boolean insertAccountCategory(String accountTypeName) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountTypeName", accountTypeName);

        long result = DB.insert("accountTypeEntity", null, contentValues);
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

    public Cursor getAccountTypes() { //reformed
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor Cursor = DB.rawQuery("select * from accountTypeEntity", null);
        return Cursor;
    }

    public Cursor getAccountTypeFor(String args) { //reformed
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor Cursor = DB.rawQuery("select * from accountTypeEntity where accountTypeName = ?", new String[]{args});
        return Cursor;
    }

    public Cursor getAccounts() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor Cursor = DB.rawQuery("select * from clientEntity", null);
        return Cursor;
    }
    
    public Cursor getCashBankAccounts() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor Cursor = DB.rawQuery("select * from cashBankEntity", null);
        return Cursor;
    }
    
    public Cursor getCashBankAccountIdFor(String cashBankTitle) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor Cursor = DB.rawQuery("select cashBankId from cashBankEntity where cashBankTitle = ?", new String[]{cashBankTitle});
        return Cursor;
    }

    public Cursor getExpenseAccounts() { //reformed
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor Cursor = DB.rawQuery("select * from expenseEntity", null);
        return Cursor;
    }

    public Cursor getAccountTypeId(String accountTypeName) { //reformed
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select accountTypeId from accountTypeEntity where accountTypeName=?", new String[]{accountTypeName});
        return cursor;
    }

    public Cursor getClientId(String clientName) { // reformed
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select clientId from clientEntity where clientName = ?", new String[]{clientName});
        return cursor;
    }

    public Cursor getUnpaidInvoicesOf(String clientId) {
        //todo where isPaid != paid
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select salesInvoiceId, date, amount from salesInvoiceEntity where clientId = ?", new String[]{clientId});
        return cursor;
    }
 
    public boolean insertCashTransaction(String tableName, String date, int invoiceId, int clientId, int cashBankId, int amount) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        contentValues.put("invoiceId", invoiceId);
        contentValues.put("clientId", clientId);
        contentValues.put("cashBankId", cashBankId);
        contentValues.put("amount", amount);
        
        long result = DB.insert(tableName, null, contentValues);
        return result != -1;
    }
}

