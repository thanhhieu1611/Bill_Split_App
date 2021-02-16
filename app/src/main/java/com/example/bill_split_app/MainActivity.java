// Author: Dang Hieu
// Date: 02/08/2021
/* Description: This app splits the bill, including the tip, among the members of your party.
   This App open a screen that requests the restaurant bill, and the number of people in the group
   A Spinner Control asks display tip percentage choices: 10, 15, 20
   Calculate the tip based on the user selection, add it to total bill, and divide the total bill
   by the number in the party
   Display the tip amount, and the individual dollar amount each person should contribute
 */
/*
    Label: Restaurant Bill, number of people, Total Bill
    EditText: User Input of the bill, Number of people (set default value)
    TextView: Total Bill, Individual Dollar and Tip Amount each person
    Spinner Control: tip selections
*/

package com.example.bill_split_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    //Declare variables
    private double resBillVal;
    private int numGuestVal;
    private String tipPercentage;
    private String selection;

    //Declare objects
    private EditText resBill;
    private TextView totBill;
    private EditText numGuest;
    private Spinner tipSpn;
    private NumberFormat form;
    private EditText indiBill;
    private EditText indiTip;
    private EditText indiTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            //Instantiate objects
            resBill = findViewById(R.id.inputResBill);
            totBill = findViewById(R.id.txtTotalBill);
            numGuest = findViewById(R.id.inputNoOfGuests);
            tipSpn = findViewById(R.id.spnTipOptions);
            Button splitBtn = findViewById(R.id.btnSplitBill);
            indiBill = findViewById(R.id.txtIndiBill);
            indiTip = findViewById(R.id.txtIndiTip);
            indiTotal = findViewById(R.id.txtIndiTotal);

            //Passing initial values
            // Spinner object select first item of its array to be the default selected item
            tipPercentage = tipSpn.getSelectedItem().toString();
            //numGuest.setText("2");
            //numGuestVal = Integer.parseInt(numGuest.getText().toString());
            // Do not call Decimal Format directly
            // NumberFormat factory methods may return
            // subclasses other than DecimalFormat
            form = NumberFormat.getCurrencyInstance();

            //Events
            resBill.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    //Get user input and convert from String to relevant data type
                    //Check to see if the length of default string is empty or not
                    //This will helps us to prevent initializing a value for EditText here
                    //Which maybe not convenient for users
                    if(resBill.getText().toString().length() == 0){
                        Toast.makeText(MainActivity.this, "Please enter the restaurant bill.", Toast.LENGTH_LONG).show();
                    }else{
                        resBillVal = Double.parseDouble(resBill.getText().toString());
                        UpdateTotalBill(tipPercentage, resBillVal);
                    }
                }
            });

            numGuest.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(numGuest.getText().toString().length() == 0){
                        Toast.makeText(MainActivity.this, "Please enter the number of guests.", Toast.LENGTH_LONG).show();
                    }else{
                        numGuestVal = Integer.parseInt(numGuest.getText().toString());
                    }
                }
            });

            tipSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    tipPercentage = tipSpn.getSelectedItem().toString();
                    UpdateTotalBill(tipPercentage, resBillVal);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            splitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SplitBill();
                }
            });
        }
        catch(Exception ex){
            Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
    //Functions
    private void UpdateTotalBill(String tipPercentage, double resBillVal){
        //Need to convert from string to double before calculating
        //To return a double
        double totBillVal = resBillVal * (1.00 + (Double.parseDouble(tipPercentage) / 100));
        selection = form.format(totBillVal);
        totBill.setText(selection);
    }

    private void SplitBill(){
        if(resBill.getText().toString().length() == 0 || numGuest.getText().toString().length() == 0){
            Toast.makeText(MainActivity.this, "Please enter the restaurant bill and number of guests.", Toast.LENGTH_LONG).show();
        }else{
            double indiBillVal = resBillVal / Double.parseDouble(String.valueOf(numGuestVal));
            selection = form.format(indiBillVal);
            indiBill.setText(selection);

            double indiTipVal = (resBillVal * (Double.parseDouble(String.valueOf(tipPercentage)) / 100)) / Double.parseDouble(String.valueOf(numGuestVal));
            selection = form.format(indiTipVal);
            indiTip.setText(selection);

            double indiTotalVal = indiBillVal + indiTipVal;
            selection = form.format(indiTotalVal);
            indiTotal.setText(selection);
        }
    }
}