package com.psmsdb.lr16;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Contact> {

    Activity ctx;
    ArrayList<Contact> contacts;
    int resource;

    ListAdapter(@NonNull Activity context, @LayoutRes int resource, ArrayList<Contact> contacts) {
        super(context,resource,contacts);
        this.ctx = context;
        this.resource = resource;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = ctx.getLayoutInflater();

        View v = inflater.inflate(resource, null);
        TextView Name = v.findViewById(R.id.name);
        TextView phoneNumber = v.findViewById(R.id.phone_number);
        //TextView workNumber = v.findViewById(R.id.work_number);

        Name.setText(contacts.get(position).getName());
        phoneNumber.setText(contacts.get(position).getPhoneNumber());
        //workNumber.setText(contacts.get(position).getWorkNumber());

        return v;
    }
}