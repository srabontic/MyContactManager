package com.example.user.mycontactmanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.view.View.OnClickListener;

public class MainActivity_contact_list extends AppCompatActivity {

    PersonAdaptor adapter;
    private final Context context = this;
    private ListView listView1;
    private TextView textName;
    private static int noOfClicksName=0;
    String message = "here";
    String message1 = "there";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_contact_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            createPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Typeface typeFace2=Typeface.createFromAsset(getAssets(),"fonts/Segoe-UI-Symbol.ttf");
        textName = (TextView)findViewById(R.id.textName);
        textName.setTypeface(typeFace2);

    }
//this method creats path for the file
    private void createPath() throws IOException{

        FileOperations fileIO = new FileOperations();
        fileIO.createPath(Environment.getExternalStorageDirectory());
        //Toast.makeText(MainActivity_contact_list.this,message1, Toast.LENGTH_LONG).show();
        Log.i("Create Path: ", Environment.getExternalStorageDirectory().toString());

    }

    private void showListViewItemEdit() {
        ListView list=(ListView) findViewById(R.id.listView1);
        final ArrayList<String> list1 = new ArrayList<String>(Arrays.asList(list.toString()));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                /*TextView textView=(TextView) viewClicked;
                String message="You Clicked On: "+textView.getText().toString();
                Toast.makeText(MainActivity_contact_list.this,message, Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), MainActivity_add_contacts.class);
                i.putExtra("entereddata",textView.getText().toString());
                startActivity(i);*/
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity_contact_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    //on clicking add button on action bar
        int id = item.getItemId();
        if (id == R.id.action_add_contact) {
            Intent intent = new Intent(this, MainActivity_add_contacts.class);
            Bundle bundle = new Bundle();
            bundle.putString("FName", "");
            bundle.putString("LName", "");
            bundle.putString("PNo", "");
            bundle.putString("Email", "");
            bundle.putString("EditMode", "Add");
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //on resume (is a sub routine of create)
    protected void onResume(){
        super.onResume();

        try {
            createPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            makeTable();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //adapter.notifyDataSetChanged();

        textName.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                ++noOfClicksName;
                // calls the sorting function
                try {
                    sortListByName();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //creating custom ArrayAdaptor list
    private void makeTable() throws  IOException{
        Typeface fontHeading = Typeface.createFromAsset(getAssets(),"fonts/Segoe-UI-Symbol.ttf");
        Typeface fontBody = Typeface.createFromAsset(getAssets(),"fonts/Segoe-Regular.ttf");
        //Toast.makeText(MainActivity_contact_list.this,message, Toast.LENGTH_LONG).show();
        final List<Person> listPerson = readContact();
        Log.i("Show Name", listPerson.get(0).getLastName());
        adapter = new PersonAdaptor(this, R.layout.listview_row, listPerson);

        listView1 = (ListView)findViewById(R.id.listView1);

        listView1.setAdapter(adapter);

        //sortListByName();

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // Call Edit function
                Log.i("sort", "pos:" + position);
                try {
                    actionEdit(position);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        registerForContextMenu(listView1);
        //return true;

    }

    //send the contact details on selection of any rows in listview
    private void actionEdit(int position) throws IOException {
        final List<Person> listPerson = readContact();
        Person p = listPerson.get(position);

        Intent editintent = new Intent(MainActivity_contact_list.this, MainActivity_add_contacts.class);
        Bundle bundle = new Bundle();
        bundle.putString("FName", p.getFirstName() );
        bundle.putString("LName", p.getLastName() );
        bundle.putString("PNo", p.getPhoneNo() );
        bundle.putString("Email", p.getEmail());
        bundle.putString("Date", p.getDate());
        bundle.putString("EditMode", "Edit");
        editintent.putExtras(bundle);
        startActivity(editintent);
    }

    private void sortListByName() throws IOException {
        List<Person> personList = readContact();
        Collections.sort(personList, new Comparator<Person>() {
            @Override
            public int compare(Person lhs, Person rhs) {
                if (noOfClicksName % 2 == 0)
                    return lhs.getFirstName().compareTo(rhs.getFirstName());
                else
                    return rhs.getFirstName().compareTo(lhs.getFirstName());
            }
        });
        FileOperations fileIO = new FileOperations();
        File file = new File( Environment.getExternalStorageDirectory(), "Contact.txt");
        fileIO.writeList(file, personList);

        adapter.sort(new Comparator<Person>() {
            @Override
            public int compare(Person lhs, Person rhs) {
                if(noOfClicksName%2==0)
                    return lhs.getFirstName().compareTo(rhs.getFirstName());
                else
                    return rhs.getFirstName().compareTo(lhs.getFirstName());
            }
        });
    }

    public List<Person> readContact() throws IOException{
        FileOperations fileIO = new FileOperations();

        List<Person> personList = fileIO.readFile( Environment.getExternalStorageDirectory());
//        Log.i("First Name", personList.get(0).getFirstName());
        return personList;
    }

}
