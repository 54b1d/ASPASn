package com.sabid.aspasn;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class AddInvoiceActivity extends AppCompatActivity {
    String date, name, description, productName, quantityText, rateText, amountText, tableName, clientIdText;
    int productId, clientId;
    double quantity, rate, amount, tmpQuantity, tmpRate, tmpAmount;

    AutoCompleteTextView editAutoName;
    EditText editDate, editQuantity, editRate, editAmount, editDescription;
    Button btnConfirmPurchase, btnConfirmSale;
    Spinner spinnerProducts;
    ArrayList accounts, products, unpaidInvoices;
    ArrayAdapter adapter;
    Boolean isPurchase, isPayment;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invoice);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Invoice");

        editAutoName = findViewById(R.id.editAutoName);
        spinnerProducts = findViewById(R.id.spinnerProducts);
        editDate = findViewById(R.id.editDate);
        editQuantity = findViewById(R.id.editQuantity);
        editRate = findViewById(R.id.editRate);
        editAmount = findViewById(R.id.editAmount);
        editDescription = findViewById(R.id.editDescription);
        btnConfirmPurchase = findViewById(R.id.btnConfirmPurchase);
        btnConfirmSale = findViewById(R.id.btnConfirmSale);
        
		// todo AddInvoice: calculate rate based on amount and quantity
		
		// calculate amount based on quantity and rate inserted
        editRate.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (editRate.getText().toString().isEmpty()){
						tmpRate = 0;
					} else {
						tmpRate = Double.parseDouble(editRate.getText().toString());
					}
					if  (editQuantity.getText().toString().isEmpty()){
						tmpQuantity = 0;
					} else {
						tmpQuantity = Double.parseDouble(editQuantity.getText().toString());
					}
                    if(s.length() != 0 && tmpQuantity > 0){
					tmpAmount = tmpQuantity * tmpRate;
					// todo format editAmout to proper decimal value
                    editAmount.setText(Double.toString(tmpAmount));
					}
                }
            });
        
        DB = new DBHelper(this);
        loadEditAutoName();
        loadSpinnerProducts();

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
                    DatePickerDialog datePickerDialog = new DatePickerDialog(AddInvoiceActivity.this,
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

        btnConfirmPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addInvoiceConfirmed(isPurchase = true);
                }
            });

        btnConfirmSale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addInvoiceConfirmed(isPurchase = false);
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
        editAutoName.setThreshold(1);
        editAutoName.setAdapter(adapter);
        res.close();
    }

    public void loadSpinnerProducts() {
        products = new ArrayList<String>();
        Cursor res = DB.getProducts();
        if (res.getCount() == 0) {
            products.add("No Product");
        } else {
            while (res.moveToNext()) {
                //take productName only from 2nd(1) column
                products.add(res.getString(1));
            }
        }
        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, products);
        spinnerProducts.setAdapter(adapter);
        res.close();
    }

    public void addInvoiceConfirmed(Boolean isPurchase) {
        // String date, int accountId, int invoiceId, int lineItemId, String purchaseSale, Double quantity, Double debit, Double credit
        date = editDate.getText().toString();
        name = editAutoName.getText().toString();
        description = editDescription.getText().toString().trim();
        Cursor res = DB.getAccountId(name);
        while (res.moveToNext()) {
            clientId = res.getInt(0);
        }
        res.close();
        if (clientId == 0){
            editAutoName.setText("");
            editAutoName.setHint("Invalid Client Name");
        }
        productName = spinnerProducts.getSelectedItem().toString();
        Cursor cursorProductId = DB.getProductIdOf(productName);
        while (cursorProductId.moveToNext()) {
            productId = cursorProductId.getInt(0);
        }
        cursorProductId.close();
        amountText = editAmount.getText().toString();
        quantityText = editQuantity.getText().toString();
        rateText = editRate.getText().toString();
        if (name.isEmpty() || quantityText.isEmpty() || rateText.isEmpty() || amountText.isEmpty() || clientId == 0) {
            Toast.makeText(this, "Input Required Fields", Toast.LENGTH_SHORT).show();
        } else {
            quantity = Double.parseDouble(quantityText);
            rate = Double.parseDouble(rateText);
            amount = Double.parseDouble(amountText);
            Log.d("AddInvoice Confirmed Values", date + productId + clientId + quantityText + amount);
            if (isPurchase) {
                tableName = "purchaseInvoice";
                if (DB.insertInvoice(tableName, date, clientId, productId, quantity, rate, amount, description)) {
                    editAutoName.setText("");
                    editQuantity.setText("");
                    editRate.setText("");
                    editAmount.setText("");
                    Toast.makeText(this, "Purchased " + productName + " of " + amountText + "Tk", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Invoicing failed.", Toast.LENGTH_LONG).show();
                }

            } else {
                tableName = "salesInvoice";
                if (DB.insertInvoice(tableName, date, clientId, productId, quantity, rate, amount, description)) {
                    editAutoName.setText("");
                    editQuantity.setText("");
                    editRate.setText("");
                    editAmount.setText("");
                    Toast.makeText(this, "Sold " + productName + " of " + amountText + "Tk", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Invoicing failed.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
