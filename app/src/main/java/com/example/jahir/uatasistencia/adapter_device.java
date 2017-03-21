package com.example.jahir.uatasistencia;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jahir on 20/03/2017.
 */

public class adapter_device extends BaseAdapter{
    public ArrayList<BluetoothDevice> celulares;
    public Context c;

    public adapter_device (Context c, ArrayList<BluetoothDevice> celulares){
        this.c=c;
        this.celulares = celulares;
    }
    @Override
    public int getCount() {
        return celulares.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BluetoothDevice x = celulares.get(position);
        convertView = LayoutInflater.from(c).inflate(R.layout.device_view,null);
        TextView ncel = (TextView) convertView.findViewById(R.id.ncel);
        TextView nmac = (TextView) convertView.findViewById(R.id.nmac);
        ncel.setText(x.getName());
        nmac.setText(x.getAddress());
        return convertView;
    }
}
