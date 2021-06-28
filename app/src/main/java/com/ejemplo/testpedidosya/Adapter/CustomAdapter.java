package com.ejemplo.testpedidosya.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ejemplo.testpedidosya.Pojo.Restaurant;
import com.ejemplo.testpedidosya.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter{
    Context c;
    ArrayList<Restaurant> restaurants;

    public CustomAdapter(Context c, ArrayList<Restaurant> restaurants) {
        this.c = c;
        this.restaurants = restaurants;
    }

    @Override
    public int getCount() {
        return restaurants.size();
    }

    @Override
    public Object getItem(int i) {
        return restaurants.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view= LayoutInflater.from(c).inflate(R.layout.model,viewGroup,false);
        }

        TextView nameTxt=view.findViewById(R.id.nameTxt);

        Restaurant r= (Restaurant) getItem(i);
        nameTxt.setText(r.getName());

        return view;
    }
}
