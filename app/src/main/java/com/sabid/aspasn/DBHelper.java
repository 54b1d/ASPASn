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
		DB.execSQL("CREATE TABLE [accounts] ( [accountId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, [accountName] TEXT NOT NULL, [accountAddress] TEXT, [accountMobile] TEXT, [accountTypeId] INTEGER NOT NULL, FOREIGN KEY([accountTypeId]) REFERENCES [accountType]([accountTypeId]));");

		DB.execSQL("CREATE TABLE [accountType] ( [accountTypeId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [accountTypeName] TEXT NOT NULL);");

		DB.execSQL("CREATE TABLE [purchaseInvoice] ( [purchaseInvoiceId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [date] DATE NOT NULL, [accountId] INTEGER NOT NULL, [productId] INTEGER NOT NULL, [description] TEXT, [quantity] REAL NOT NULL, [rate] REAL NOT NULL, [amount] REAL NOT NULL, FOREIGN KEY([accountId]) REFERENCES [accounts]([accountId]), FOREIGN KEY([productId]) REFERENCES [products]([productId]));");

		DB.execSQL("CREATE TABLE [payment] ( [paymentId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, [date] DATE NOT NULL, [invoiceId] INTEGER, [cashBankId] INTEGER NOT NULL, [accountId] INTEGER NOT NULL, [description] TEXT, [amount] REAL NOT NULL, FOREIGN KEY([invoiceId]) REFERENCES [purchaseInvoice]([purchaseInvoiceId]), FOREIGN KEY([cashBankId]) REFERENCES [cashBankAccounts]([cashBankId]), FOREIGN KEY([accountId]) REFERENCES [accounts]([accountId]));");

		DB.execSQL("CREATE TABLE [salesInvoice] ( [salesInvoiceId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [date] DATE NOT NULL, [accountId] INTEGER NOT NULL, [productId] INTEGER NOT NULL, [description] TEXT, [quantity] REAL NOT NULL, [rate] REAL NOT NULL, [amount] REAL NOT NULL, FOREIGN KEY([accountId]) REFERENCES [accounts]([accountId]), FOREIGN KEY([productId]) REFERENCES [products]([productId]));");

		DB.execSQL("CREATE TABLE [receipt] ( [receiptId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [date] DATE NOT NULL, [invoiceId] INTEGER, [cashBankId] INTEGER NOT NULL, [accountId] INTEGER NOT NULL, [description] INTEGER, [amount] REAL NOT NULL, FOREIGN KEY([invoiceId]) REFERENCES [salesInvoice]([salesInvoiceId]), FOREIGN KEY([cashBankId]) REFERENCES [cashBankAccounts]([cashBankId]), FOREIGN KEY([accountId]) REFERENCES [accounts]([accountId]));");

		DB.execSQL("CREATE TABLE [products] ( [productId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [productName] TEXT NOT NULL, [productTypeId] INTEGER NOT NULL, [accountTypeId] INTEGER NOT NULL, FOREIGN KEY([productTypeId]) REFERENCES [productType]([productTypeId]), FOREIGN KEY([accountTypeId]) REFERENCES [accountType]([accountTypeId]));");

		DB.execSQL("CREATE TABLE [cashBankAccounts] ( [cashBankId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [cashBankTitle] TEXT NOT NULL UNIQUE, [accountTypeId] INTEGER NOT NULL, FOREIGN KEY([accountTypeId]) REFERENCES [accountType]([accountTypeId]));");

		DB.execSQL("CREATE TABLE [accountingPeriod] ( [accountingPeriodId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [accountingStartDate] DATE NOT NULL UNIQUE, [accountingEndDate] DATE NOT NULL UNIQUE);");

		DB.execSQL("CREATE TABLE [productType] ( [productTypeId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, [productTypeTitle] TEXT NOT NULL UNIQUE, [baseUnitName] TEXT NOT NULL, [interpretedUnitName] TEXT NOT NULL, [minWeightToInterpretUnit] INTEGER NOT NULL);");

		DB.execSQL("CREATE TABLE [cashBankAccountsBalance] ( [_id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, [cashBankId] INTEGER NOT NULL, [accountingPeriodId] INTEGER NOT NULL, [openingBalance] REAL NOT NULL, [closingBalance] REAL NOT NULL, FOREIGN KEY([cashBankId]) REFERENCES [cashBankAccounts]([cashBankId]), FOREIGN KEY([accountingPeriodId]) REFERENCES [accountingPeriod]([accountingPeriodId]));");

		DB.execSQL("CREATE TABLE [accountsBalance] ( [_id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, [accountId] INTEGER NOT NULL, [accountingPeriodId] INTEGER NOT NULL, [openingBalance] REAL NOT NULL, [closingBalance] REAL NOT NULL, FOREIGN KEY([accountId]) REFERENCES [accounts]([accountId]), FOREIGN KEY([accountingPeriodId]) REFERENCES [accountingPeriod]([accountingPeriodId]));");

		DB.execSQL("CREATE TABLE [productsBalance] ( [_id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [productId] INTEGER NOT NULL, [accountingPeriodId] INTEGER NOT NULL, [openingBalance] REAL NOT NULL, [closingBalance] REAL NOT NULL, FOREIGN KEY([productId]) REFERENCES [products]([productId]), FOREIGN KEY([accountingPeriodId]) REFERENCES [accountingPeriod]([accountingPeriodId]));");

		DB.execSQL("CREATE TABLE [journalEntry] ( [_id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [date] DATE NOT NULL, [fromAccountId] INTEGER NOT NULL, [toAccountId] INTEGER NOT NULL, [description] TEXT NOT NULL, [amount] REAL NOT NULL, FOREIGN KEY([fromAccountId]) REFERENCES [accounts]([accountId]), FOREIGN KEY([toAccountId]) REFERENCES [accounts]([accountId]));");

		DB.execSQL("CREATE TABLE [contraEntry] ( [_id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, [date] DATE NOT NULL, [fromCashBankAccountId] INTEGER NOT NULL, [toCashBankAccountId] INTEGER NOT NULL, [description] TEXT NOT NULL, [amount] REAL NOT NULL, FOREIGN KEY([fromCashBankAccountId]) REFERENCES [cashBankAccounts]([cashBankId]), FOREIGN KEY([toCashBankAccountId]) REFERENCES [cashBankAccounts]([cashBankId]));");
		
		
		DB.execSQL(
				"INSERT INTO [accountType] ('accountTypeName') VALUES ('Assets'), ('Liabilities'), ('Capital'), ('Accounts'), ('Incomes'), ('Expenses');");
		//Create default Cash accounts inside cashBankAccounts table which is always required
		DB.execSQL("INSERT INTO [cashBankAccounts] ('cashBankTitle', 'accountTypeId') VALUES ('Cash', '1');");
		//Create default Capital accounts required
		/*
		DB.execSQL(
				"INSERT INTO [capitalAccounts] ('capitalAccountTitle', 'accountTypeId') VALUES ('Owners Equity', '3'), ('Retained Earnings', '3');");
		*/
	}

	@Override
	public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
	}

	public Boolean insertAccountData(String name, String address, String mobile, int accountTypeId) {
		SQLiteDatabase DB = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("accountName", name);
		contentValues.put("accountAddress", address);
		contentValues.put("accountMobile", mobile);
		contentValues.put("accountTypeId", accountTypeId);
		long result = DB.insert("accounts", null, contentValues);
		return result != -1;
	}

	public Boolean insertExpenseAccount(String expenseName, int accountTypeId) {
		SQLiteDatabase DB = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("expenseTitle", expenseName);
		contentValues.put("accountTypeId", accountTypeId);
		long result = DB.insert("expense", null, contentValues);
		return result != -1;
	}

	public Boolean insertAccountCategory(String accountTypeName) {
		SQLiteDatabase DB = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("accountTypeName", accountTypeName);

		long result = DB.insert("accountType", null, contentValues);
		return result != -1;
	}
	
	public Boolean insertAccountingPeriod(String accountingStartDate, String accountingEndDate) {
		SQLiteDatabase DB = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("accountingStartDate", accountingStartDate);
		contentValues.put("accountingEndDate", accountingEndDate);

		long result = DB.insert("accountingPeriod", null, contentValues);
		return result != -1;
	}
    
    public Boolean insertProduct(String productName, int productTypeId, int accountTypeId) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("productName", productName);
        contentValues.put("productTypeId", productTypeId);
        contentValues.put("accountTypeId", accountTypeId);

        long result = DB.insert("products", null, contentValues);
        return result != -1;
	}

	public Boolean insertProductType(String productTypeTitle, String baseUnitName, String interpretedUnitName, int minWeightToInterpretUnit) {
		SQLiteDatabase DB = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("productTypeTitle", productTypeTitle);
        contentValues.put("baseUnitName", baseUnitName);
        contentValues.put("interpretedUnitName", interpretedUnitName);
        contentValues.put("minWeightToInterpretUnit", minWeightToInterpretUnit);

		long result = DB.insert("productType", null, contentValues);
		return result != -1;
	}
    
    public Boolean insertInvoice(String tableName, String date,int accountId, int productId, double quantity, double rate, double amount, String description) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        contentValues.put("accountId", accountId);
        contentValues.put("productId", productId);
        contentValues.put("quantity", quantity);
        contentValues.put("rate", rate);
        contentValues.put("amount", amount);
        contentValues.put("description", description);

        long result = DB.insert(tableName, null, contentValues);
        return result != -1;
	}

	public Boolean updateAccount(String name, String address, String mobile) {
		SQLiteDatabase DB = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("name", name);
		contentValues.put("address", address);
		contentValues.put("mobile", mobile);
		Cursor cursor = DB.rawQuery("Select * from accounts where name=?", new String[] { name });
		if (cursor.getCount() > 0) {
			long result = DB.update("accounts", contentValues, "name=?", new String[] { name });
			return result != -1;
		} else {
			return false;
		}
	}

	public Boolean deletedata(String name) {
		SQLiteDatabase DB = this.getWritableDatabase();
		Cursor cursor = DB.rawQuery("Select * from Userdetails where name=?", new String[] { name });
		if (cursor.getCount() > 0) {
			long result = DB.delete("Userdetails", "name=?", new String[] { name });
			return result != -1;
		} else {
			return false;
		}
	}
    
    public Cursor getTable(String tableName){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor Cursor = DB.rawQuery("select * from " + tableName, null);
		return Cursor;
    }

    public Cursor getJournalEntries() {
		SQLiteDatabase DB = this.getWritableDatabase();
		Cursor Cursor = DB.rawQuery("SELECT \n" +
				"       [main].[payment].[date], \n" +
				"       [main].[accounts].[accountName], \n" +
				"       [main].[accounts].[accountAddress], \n" +
				"       [main].[payment].[amount]\n" +
				"FROM   [main].[payment]\n" +
				"       INNER JOIN [main].[accounts] ON [main].[payment].[accountId] = [main].[accounts].[accountId]\n" +
				"ORDER  BY [main].[payment].[date] DESC;", null);
		return Cursor;
	}

    public Cursor getPayments() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor Cursor = DB.rawQuery("SELECT \n" +
                "       [main].[payment].[date], \n" +
                "       [main].[accounts].[accountName], \n" +
                "       [main].[accounts].[accountAddress], \n" +
                "       [main].[payment].[amount]\n" +
                "FROM   [main].[payment]\n" +
                "       INNER JOIN [main].[accounts] ON [main].[payment].[accountId] = [main].[accounts].[accountId]\n" +
                "ORDER  BY [main].[payment].[date] DESC;", null);
        return Cursor;
    }
    
    public Cursor getReceipts(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor Cursor = DB.rawQuery("SELECT [receipt].[date], [accounts].[accountName], [accounts].[accountAddress], [receipt].[amount] FROM   [main].[receipt]        INNER JOIN [main].[accounts] ON [main].[receipt].[accountId] = [main].[accounts].[accountId] ORDER  BY [main].[receipt].[date] DESC;", null);
        return Cursor;
	}
    
    public Cursor getPurchases(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor Cursor = DB.rawQuery("SELECT [purchaseInvoice].[date], [accounts].[accountName], [accounts].[accountAddress], [purchaseInvoice].[amount] FROM   [main].[purchaseInvoice]        INNER JOIN [main].[accounts] ON [main].[purchaseInvoice].[accountId] = [main].[accounts].[accountId] ORDER  BY [main].[purchaseInvoice].[date] DESC;", null);
        return Cursor;
	}
    
    public Cursor getSales(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor Cursor = DB.rawQuery("SELECT [salesInvoice].[date], [accounts].[accountName], [accounts].[accountAddress], [salesInvoice].[amount] FROM   [main].[salesInvoice]        INNER JOIN [main].[accounts] ON [main].[salesInvoice].[accountId] = [main].[accounts].[accountId] ORDER  BY [main].[salesInvoice].[date] DESC;", null);
        return Cursor;
    }

	public Cursor getAccountTypes() { //reformed
		SQLiteDatabase DB = this.getWritableDatabase();
		Cursor Cursor = DB.rawQuery("select * from accountType", null);
		return Cursor;
	}

	public Cursor getAccountTypeFor(String args) { //reformed
		SQLiteDatabase DB = this.getWritableDatabase();
		Cursor Cursor = DB.rawQuery("select * from accountType where accountTypeName = ?", new String[] { args });
		return Cursor;
	}

	public Cursor getProductTypeFor(String args) {
		SQLiteDatabase DB = this.getWritableDatabase();
		Cursor Cursor = DB.rawQuery("select * from productType where productTypeTitle = ?", new String[] { args });
		return Cursor;
	}
	
	public Cursor getProductTypes(){
		SQLiteDatabase DB = this.getWritableDatabase();
		Cursor Cursor = DB.rawQuery("select * from productType", null);
		return Cursor;
	}

	public Cursor getAccounts() {
		SQLiteDatabase DB = this.getWritableDatabase();
		Cursor Cursor = DB.rawQuery("select * from accounts", null);
		return Cursor;
	}
    
    public Cursor getAccountsOf(String accountTypeName) {
        SQLiteDatabase DB = this.getWritableDatabase();
		Cursor Cursor = DB.rawQuery("SELECT [accounts].* FROM [main].[accounts] INNER JOIN [main].[accountType] ON [main].[accountType].[accountTypeId] = [main].[accounts].[accountTypeId] WHERE accountTypeName = ?;", new String[]{accountTypeName});
        return Cursor;
	}

	public Cursor getAccountingPeriods() {
		SQLiteDatabase DB = this.getWritableDatabase();
		Cursor Cursor = DB.rawQuery("select * from accountingPeriod", null);
		return Cursor;
	}
	
	public Cursor getProducts() {
		SQLiteDatabase DB = this.getWritableDatabase();
		Cursor Cursor = DB.rawQuery("select * from products", null);
		return Cursor;
	}
    
    public Cursor getProductIdOf(String productName) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor Cursor = DB.rawQuery("select productId from products where productName=?", new String[] {productName});
        return Cursor;
	}

	public Cursor getCashBankAccounts() {
		SQLiteDatabase DB = this.getWritableDatabase();
		Cursor Cursor = DB.rawQuery("select * from cashBankAccounts", null);
		return Cursor;
	}

	public Cursor getCashBankAccountIdFor(String cashBankTitle) {
		SQLiteDatabase DB = this.getWritableDatabase();
		Cursor Cursor = DB.rawQuery("select cashBankId from cashBankAccounts where cashBankTitle = ?",
				new String[] { cashBankTitle });
		return Cursor;
	}

	public Cursor getExpenseAccounts() { //reformed
		SQLiteDatabase DB = this.getWritableDatabase();
		Cursor Cursor = DB.rawQuery("select * from expense", null);
		return Cursor;
	}

	public Cursor getAccountTypeId(String accountTypeName) { //reformed
		SQLiteDatabase DB = this.getWritableDatabase();
		Cursor cursor = DB.rawQuery("Select accountTypeId from accountType where accountTypeName=?",
				new String[] { accountTypeName });
		return cursor;
	}

	public Cursor getAccountId(String accountName) { // reformed
		SQLiteDatabase DB = this.getWritableDatabase();
		Cursor cursor = DB.rawQuery("Select accountId from accounts where accountName = ?",
				new String[] { accountName });
		return cursor;
	}

	public Cursor getUnpaidInvoicesOf(String accountId) {
		//todo where isPaid != paid
		SQLiteDatabase DB = this.getWritableDatabase();
		Cursor cursor = DB.rawQuery("Select salesInvoiceId, date, amount from salesInvoice where accountId = ?",
				new String[] { accountId });
		return cursor;
	}

	public boolean insertCashTransaction(String tableName, String date, int invoiceId, int accountId, int cashBankId,
                                         String description, int amount) {
		SQLiteDatabase DB = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("date", date);
		contentValues.put("invoiceId", invoiceId);
		contentValues.put("accountId", accountId);
		contentValues.put("cashBankId", cashBankId);
        contentValues.put("description", description);
		contentValues.put("amount", amount);

		long result = DB.insert(tableName, null, contentValues);
		return result != -1;
	}
}
