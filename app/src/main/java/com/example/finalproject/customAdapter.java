package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class customAdapter extends BaseAdapter {


    Context context;
    String[] data;
    private static LayoutInflater inflater = null;
    HashMap<String, String> map;
   FirebaseAuth auth = FirebaseAuth.getInstance();
   ImageView image;

    public customAdapter(Context context, String[] data, HashMap<String, String> createdData) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        map=createdData;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data[position];
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row, null);
        TextView text = (TextView) vi.findViewById(R.id.mytext);
        image=vi.findViewById(R.id.image);
        if( !map.get(data[position]).equals(auth.getCurrentUser().getDisplayName()))
        {



        }
        else{

            image.setVisibility(View.INVISIBLE);
        }
        text.setText(data[position]);
        return vi;
    }
}
