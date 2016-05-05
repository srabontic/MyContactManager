package com.example.user.mycontactmanager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by User on 27-03-2016.
 */
public class PersonAdaptor  extends ArrayAdapter<Person> {
    Context context;
    int layoutResourceId;
    List<Person> personList;
    Typeface fontHeading;
    Typeface fontText;

    public PersonAdaptor(Context context, int layoutResourceId, List<Person> list){ //, Typeface fontHeading, Typeface fontText) {
        super(context, layoutResourceId, list);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.personList = list;
        this.fontHeading = fontHeading;
        this.fontText = fontText;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        PersonHolder holder;
        if(row == null)
        {
            // Uses the Android built in Layout Inflater to inflate (parse) the xml layout file.
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PersonHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtName = (TextView)row.findViewById(R.id.txtTitle);
            holder.txtPhone = (TextView)row.findViewById(R.id.txt_Phone);

            holder.txtName.setId(position);
            //holder.txtName.setTypeface(fontText);
            //holder.txtPhone.setTypeface(fontText);

            row.setTag(holder);
        }
        else
        {
            holder = (PersonHolder)row.getTag();
        }
        //Log.i("First Name", personList.get(position).getFirstName());
        holder.imgIcon.setImageResource(R.drawable.person);
        holder.txtName.setText( personList.get(position).getFirstName()+" "+personList.get(position).getLastName());
        holder.txtPhone.setText( personList.get(position).getPhoneNo() );

        return row;
    }
    static class PersonHolder
    {
        ImageView imgIcon;
        TextView txtName;
        TextView txtPhone;
    }
}
