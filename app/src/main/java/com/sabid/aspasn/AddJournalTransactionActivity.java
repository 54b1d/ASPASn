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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class AddJournalTransactionActivity extends AppCompatActivity {
    String date, nameFrom, nameTo, amountText, description, clientIdText;
    Button btnConfirmJournalEntry;
    EditText editDate, editDescription, editAmount;
    AutoCompleteTextView editAutoNameFrom, editAutoNameTo;
    DBHelper DB;
    ArrayList accounts, products, unpaidInvoices;
    ArrayAdapter adapter;
    int accountIdFrom, accountIdTo;
    double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal_transaction);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Direct Transaction");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _v) {
                onBackPressed();
            }
        });
        btnConfirmJournalEntry = findViewById(R.id.btnConfirmJournalEntry);
        editDate = findViewById(R.id.editDate);
        editAutoNameFrom = findViewById(R.id.editAutoNameFrom);
        editAutoNameTo = findViewById(R.id.editAutoNameTo);
        editDescription = findViewById(R.id.editDescription);
        editAmount = findViewById(R.id.editAmount);

        DB = new DBHelper(this);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        date = sdf.format(calendar.getTime());
        editDate.setText(date);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddJournalTransactionActivity.this,
                        R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = year + "/" + month + "/" + day;
                        editDate.setText(date);
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });

        btnConfirmJournalEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                journalEntryConfirmed();
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
                //take account name only from 2nd(1) column
                accounts.add(res.getString(1));

            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, accounts);
        editAutoNameFrom.setThreshold(1);
        editAutoNameTo.setThreshold(1);
        editAutoNameFrom.setAdapter(adapter);
        editAutoNameTo.setAdapter(adapter);
        res.close();
    }

    public void journalEntryConfirmed() {
        // String date, int accountId, int invoiceId, int lineItemId, String purchaseSale, Double quantity, Double debit, Double credit
        date = editDate.getText().toString();
        nameFrom = editAutoNameFrom.getText().toString();
        nameTo = editAutoNameTo.getText().toString();
        description = editDescription.getText().toString().trim();

        Cursor res = DB.getAccountId(nameFrom);
        while (res.moveToNext()) {
            accountIdFrom = res.getInt(0);
        }
        res.close();
        Cursor res2 = DB.getAccountId(nameTo);
        while (res2.moveToNext()) {
            accountIdTo = res2.getInt(0);
        }
        if (accountIdFrom == 0) {
            editAutoNameFrom.setText("");
            editAutoNameFrom.setHint("Invalid Client Name");
        }
        amountText = editAmount.getText().toString();
        if (nameFrom.isEmpty() || nameTo.isEmpty() || amountText.isEmpty() || accountIdFrom == 0 || accountIdTo == 0) {
            Toast.makeText(this, "Input Required Fields", Toast.LENGTH_SHORT).show();
        } else {
            amount = Integer.parseInt(amountText);
            Log.d("AddJournalTransaction Confirmed Values", date + accountIdFrom + accountIdTo + amount);

            if (DB.insertJournalTransaction(date, accountIdFrom, accountIdTo, description, amount)) {
                Toast.makeText(this, "From " + nameFrom + "To " + nameTo + amountText + "Tk", Toast.LENGTH_SHORT).show();
                editAutoNameFrom.setText("");
                editAutoNameTo.setText("");
                editAmount.setText("");
                editDescription.setText("");
            } else {
                Toast.makeText(this, "Transaction failed.", Toast.LENGTH_LONG).show();
            }
        }
    }
}