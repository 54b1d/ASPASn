package com.sabid.aspasn;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class AddCashTransactionActivity extends AppCompatActivity {
	String date, name, cashBankTitle, amountText, tableName, clientIdText;
	int invoiceId, clientId, cashBankId, quantity, amount;

	AutoCompleteTextView editAutoName;
	EditText editDate, editAmount;
	Button btnConfirmCashReceiptTransaction, btnConfirmCashPaymentTransaction;
	Spinner spinnerCashBankList, spinnerUnpaidInvoices;
	ArrayList accounts, cashBankEntity, unpaidInvoices;
	ArrayAdapter adapter;
	Boolean isReceipt, isPayment;
	DBHelper DB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_cash_transaction);
		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("New Cash Transaction");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});

		editAutoName = findViewById(R.id.editAutoName);
		spinnerCashBankList = findViewById(R.id.spinnerCashBankList);
		spinnerUnpaidInvoices = findViewById(R.id.spinnerUnpaidInvoices);
		editDate = findViewById(R.id.editDate);
		editAmount = findViewById(R.id.editAmount);
		btnConfirmCashReceiptTransaction = findViewById(R.id.btnConfirmCashReceiptTransaction);
		btnConfirmCashPaymentTransaction = findViewById(R.id.btnConfirmPaymentTransaction);

		DB = new DBHelper(this);
		loadEditAutoName();
		loadSpinnerCashBankList();

		Calendar calendar = Calendar.getInstance();
		final int year = calendar.get(Calendar.YEAR);
		final int month = calendar.get(Calendar.MONTH);
		final int day = calendar.get(Calendar.DAY_OF_MONTH);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		date = sdf.format(calendar.getTime());
		editDate.setText(date);
		editDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DatePickerDialog datePickerDialog = new DatePickerDialog(AddCashTransactionActivity.this,
						R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year, int month, int day) {
								month = month + 1;
								String date = day + "/" + month + "/" + year;
								editDate.setText(date);
							}
						}, year, month, day);

				datePickerDialog.show();
			}
		});

		btnConfirmCashReceiptTransaction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cashTransactionConfirmed(isReceipt = true);
			}
		});

		btnConfirmCashPaymentTransaction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cashTransactionConfirmed(isReceipt = false);
			}
		});

	}

	public void loadEditAutoName() {
		accounts = new ArrayList<String>();
		Cursor res = DB.getAccounts();
		if (res.getCount() == 0) {
			accounts.add("No Available Account");
		} else {
			while (res.moveToNext()) {
				//take category name only from 2nd(1) column
				accounts.add(res.getString(1));

			}
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, accounts);
		editAutoName.setThreshold(1);
		editAutoName.setAdapter(adapter);
		res.close();
	}

	public void loadSpinnerCashBankList() {
		cashBankEntity = new ArrayList<String>();
		Cursor res = DB.getCashBankAccounts();
		if (res.getCount() == 0) {
			cashBankEntity.add("CashBank Account");
		} else {
			while (res.moveToNext()) {
				//take cashBankTitle only from 2nd(1) column
				cashBankEntity.add(res.getString(1));
			}
		}
		adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,
				cashBankEntity);
		spinnerCashBankList.setAdapter(adapter);
		res.close();
	}

	public void loadSpinnerUnpaidInvoices(int clientId) {
		//todo cashTransaction load unpaid invoices
		// set default invoice id to 0, meaning its direct cash transaction
		invoiceId = 0;
		clientIdText = Integer.toString(clientId);
		unpaidInvoices = new ArrayList<String>();
		Cursor cursorUnpaidInvoices = DB.getUnpaidInvoicesOf(clientIdText);
		if (cursorUnpaidInvoices.getCount() == 0) {
			unpaidInvoices.add("No unpaid invoice");
		} else {
			while (cursorUnpaidInvoices.moveToNext()) {
				unpaidInvoices.add("ID: " + cursorUnpaidInvoices.getString(0) + ", Date: "
						+ cursorUnpaidInvoices.getString(1) + ", Amount: " + cursorUnpaidInvoices.getString(2));
			}
		}
		cursorUnpaidInvoices.close();
	}

	public void cashTransactionConfirmed(Boolean isReceipt) {
		// String date, int accountId, int invoiceId, int lineItemId, String purchaseSale, Double quantity, Double debit, Double credit
		date = editDate.getText().toString();
		name = editAutoName.getText().toString();
		Cursor res = DB.getAccountId(name);
		while (res.moveToNext()) {
			clientId = res.getInt(0);
		}
		res.close();
		if (clientId == 0){
			editAutoName.setText("");
			editAutoName.setHint("Invalid Client Name");
		}
		cashBankTitle = spinnerCashBankList.getSelectedItem().toString();
		Cursor cursorCashBankAccountId = DB.getCashBankAccountIdFor(cashBankTitle);
		while (cursorCashBankAccountId.moveToNext()) {
			cashBankId = cursorCashBankAccountId.getInt(0);
		}
		cursorCashBankAccountId.close();
		invoiceId = 0; //todo addcashtransaction: get invoice id
		amountText = editAmount.getText().toString();
		if (name.isEmpty() || amountText.isEmpty() || clientId == 0) {
			Toast.makeText(this, "Input Required Fields", Toast.LENGTH_SHORT).show();
		} else {
			amount = Integer.parseInt(amountText);
			Log.d("AddCashTransaction Confirmed Values", tableName + date + invoiceId + clientId + cashBankId + amount);
			if (isReceipt) {
				tableName = "receiptEntity";
				if (DB.insertCashTransaction(tableName, date, invoiceId, clientId, cashBankId, amount)) {
					editAutoName.setText("");
					editAmount.setText("");
					Toast.makeText(this, "Received " + amount + "Tk From " + name, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Cash Transaction failed.", Toast.LENGTH_LONG).show();
				}

			} else {
				tableName = "paymentEntity";
				if (DB.insertCashTransaction(tableName, date, invoiceId, clientId, cashBankId, amount)) {
					editAutoName.setText("");
					editAmount.setText("");
					Toast.makeText(this, "Paid " + amount + "Tk To " + name, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Cash Transaction failed.", Toast.LENGTH_LONG).show();
				}
			}
		}
	}

}
