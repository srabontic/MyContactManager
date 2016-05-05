package com.example.user.mycontactmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity_add_contacts extends AppCompatActivity {
    String item;
    int newcontact=0;
    String oldFName;
    String oldLName;
    String oldPNo;
    String oldEmail;
    String oldDate;
    String EditMode="Add";
    boolean isEditFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_activity_add_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //show current date
        showCurrentDate();
        EditText txtFirstName = (EditText) findViewById(R.id.editText);
        EditText txtLastName = (EditText) findViewById(R.id.editText2);
        EditText txtPhoneNo = (EditText) findViewById(R.id.editText3);
        EditText txtEmailID = (EditText) findViewById(R.id.editText4);
        EditText txtDate = (EditText) findViewById(R.id.editText5);

        //get message from intent
        Bundle bundle = getIntent().getExtras();
        oldFName = bundle.getString("FName");
        oldLName = bundle.getString("LName");
        oldPNo = bundle.getString("PNo");
        oldEmail = bundle.getString("Email");
        oldDate = bundle.getString("Date");
        EditMode = bundle.getString("Edit");

        txtFirstName.setText(oldFName);
        txtLastName.setText(oldLName);
        txtPhoneNo.setText(oldPNo);
        txtEmailID.setText(oldEmail);
        if(oldDate != null)
            txtDate.setText(oldDate);

        if (oldFName.equals(null) || oldFName.length() <= 0 ){  //if edit mode or add mode
            Log.i("mode", "edit");
            isEditFlag = false;
        }
        else {
            isEditFlag = true;
        }
    }
//show current date in  date textview
    private void showCurrentDate() {
        EditText tv = (EditText) findViewById(R.id.editText5);

        String dt;

        Calendar cal = Calendar.getInstance();
        int yy = cal.get(Calendar.YEAR);
        int mm = cal.get(Calendar.MONTH);
        int dd = cal.get(Calendar.DAY_OF_MONTH);

        tv.setText(new StringBuilder().append(yy).append("-").append(mm + 1).append("-").append(dd));
    }

    //call on click is onclick event of buttons
    public void CallOnClick(View view) throws IOException {
        if(view.getId() == R.id.button2){
            try {
                callSaveButton(view);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(view.getId() == R.id.button3){
            callDeleteButton(view);
        }

    }

    private void callDeleteButton(View view) throws IOException {
        //delete contact from file on delete button click
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        FileOperations fileIO = new FileOperations();
                        try {
                            fileIO.deleteRecord( Environment.getExternalStorageDirectory(), oldFName, oldLName, oldPNo, oldEmail, oldDate);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        finish();
                        Toast.makeText(getBaseContext(), "Contact is deleted!", Toast.LENGTH_SHORT).show();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        EditText txtProduct10 = (EditText) findViewById(R.id.editText);
                        txtProduct10.requestFocus();
                        break;

                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("Are you sure you want to delete this contact?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    private void callSaveButton(View view) throws IOException {
        final String msg = "KKK";
        //add contacts on SAVE button click
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {

                    case DialogInterface.BUTTON_POSITIVE:
                        Log.i("SSSSSSS", msg);
                        EditText txtFirstName = (EditText) findViewById(R.id.editText);
                        EditText txtLastName = (EditText) findViewById(R.id.editText2);
                        EditText txtPhoneNo = (EditText) findViewById(R.id.editText3);
                        EditText txtEmail = (EditText) findViewById(R.id.editText4);
                        EditText txtDate = (EditText) findViewById(R.id.editText5);

                        String firstName = txtFirstName.getText().toString();
                        Log.i("TTTTTTTT", firstName);
                        String lastName = txtLastName.getText().toString();
                        String phoneNo = txtPhoneNo.getText().toString();
                        String email = txtEmail.getText().toString();
                        String dateEntered = txtDate.getText().toString();


                        FileOperations fileIO = new FileOperations();
                        //check if the fname is not null
                        if(firstName.equals(null) || firstName.length() <= 0){
                            Toast.makeText(getBaseContext(), "Please enter First Name !", Toast.LENGTH_SHORT).show();
                        }
                        else{

                            //if edit mode delete and add row in file
                            if(isEditFlag){
                                Log.i("edit flag", "isedit");
                                //delete selected record from file
                                try {
                                    fileIO.deleteRecord( Environment.getExternalStorageDirectory(), oldFName, oldLName, oldPNo, oldEmail, oldDate );
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                //create string
                                String lineToWrite = firstName + "|" + lastName + "|" + phoneNo + "|" + email + "|" + dateEntered + "|";

                                boolean flg = false;
                                try {
                                    flg = fileIO.saveToDisk( Environment.getExternalStorageDirectory(), lineToWrite );
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if(flg)
                                {
                                    Toast.makeText(getBaseContext(), "Contact is saved!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                            //if add mode add new record
                            else{
                                if(lastName.equals(null) || lastName.length() <= 0)
                                {
                                    lastName = " ";
                                }
                                if(phoneNo.equals(null) || phoneNo.length() <= 0)
                                {
                                    phoneNo = " ";
                                }
                                if(email.equals(null) || email.length() <= 0)
                                {
                                    email = " ";
                                }
                                if(dateEntered.equals(null) || dateEntered.length() <= 0)
                                {
                                    dateEntered = " ";
                                }
                                String lineToWrite = firstName + "|" + lastName + "|" + phoneNo + "|" + email + "|" + dateEntered + "|";
                                boolean flg = false;
                                try {
                                    flg = fileIO.saveToDisk( Environment.getExternalStorageDirectory(), lineToWrite );
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if(flg)
                                {
                                    Toast.makeText(getBaseContext(), "Contact is saved!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                            }
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        EditText txtProduct10 = (EditText) findViewById(R.id.editText);
                        txtProduct10.requestFocus();
                        break;

                }

            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("Are you sure you want to save this contact?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
