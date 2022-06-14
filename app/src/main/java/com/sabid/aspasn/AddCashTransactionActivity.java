package com.sabid.aspasn;

import android.app.DatePickerDialog;
import android.database.Cursor;
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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AddCashTransactionActivity extends AppCompatActivity {
	String date, name, cashBankTitle, amountText, description, tableName, clientIdText;
	int invoiceId, accountId, cashBankId, quantity, amount;

	AutoCompleteTextView editAutoName;
	EditText editDate, editAmount, editDescription;
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
		ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add CashBank Transaction");

		editAutoName = findViewById(R.id.editAutoName);
		spinnerCashBankList = findViewById(R.id.spinnerCashBankList);
		spinnerUnpaidInvoices = findViewById(R.id.spinnerUnpaidInvoices);
		editDate = findViewById(R.id.editDate);
		editAmount = findViewById(R.id.editAmount);
        editDescription = findViewById(R.id.editDescription);
		btnConfirmCashReceiptTransaction = findViewById(R.id.btnConfirmCashReceiptTransaction);
		btnConfirmCashPaymentTransaction = findViewById(R.id.btnConfirmPaymentTransaction);

		DB = new DBHelper(this);
		loadEditAutoName();
		loadSpinnerCashBankList();

        final DateTimeFormatter dateFormat =  DateTimeFormatter.ofPattern("yyyy-M-d");
        LocalDate currentDate = LocalDate.now();
        date = currentDate.format(dateFormat);
        editDate.setText(date);
        final int year = currentDate.getYear();
        final int month = currentDate.getMonthValue();
        final int day = currentDate.getDayOfMonth();
		
		editDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DatePickerDialog datePickerDialog = new DatePickerDialog(AddCashTransactionActivity.this,
						R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year, int month, int day) {
								month = month + 1;
								String date = year + "-" + month + "-" + day;
								editDate.setText(date);
							}
						}, year, month -1, day);

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
        description = editDescription.getText().toString().trim();
        
		Cursor res = DB.getAccountId(name);
		while (res.moveToNext()) {
			accountId = res.getInt(0);
		}
		res.close();
		if (accountId == 0){
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
		if (name.isEmpty() || amountText.isEmpty() || accountId == 0) {
			Toast.makeText(this, "Input Required Fields", Toast.LENGTH_SHORT).show();
		} else {
			amount = Integer.parseInt(amountText);
			Log.d("AddCashTransaction Confirmed Values", tableName + date + invoiceId + accountId + cashBankId + amount);
			if (isReceipt) {
				tableName = "receipt";
				if (DB.insertCashTransaction(tableName, date, invoiceId, accountId, cashBankId, description, amount)) {
					editAutoName.setText("");
					editAmount.setText("");
                    editDescription.setText("");
					Toast.makeText(this, "Received " + amount + "Tk From " + name, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Cash Transaction failed.", Toast.LENGTH_LONG).show();
				}

			} else {
				tableName = "payment";
				if (DB.insertCashTransaction(tableName, date, invoiceId, accountId, cashBankId, description, amount)) {
					editAutoName.setText("");
					editAmount.setText("");
                    editDescription.setText("");
					Toast.makeText(this, "Paid " + amount + "Tk To " + name, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Cash Transaction failed.", Toast.LENGTH_LONG).show();
				}
			}
		}
	}

}
