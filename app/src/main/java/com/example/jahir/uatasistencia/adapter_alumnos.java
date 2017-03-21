package com.example.jahir.uatasistencia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;
import static com.example.jahir.uatasistencia.R.id.foto;

/**
 * Created by Jahir on 14/03/2017.
 */

public class adapter_alumnos extends BaseAdapter
{
    public ArrayList<alumno> students;
    public Context c;
    public int x = 0;
    public adapter_alumnos(Context c, ArrayList<alumno> a){
        this.students = a;
        this.c = c;
    }
    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        File sdCard = Environment.getExternalStorageDirectory();
        alumno a = students.get(position);
        convertView = LayoutInflater.from(c).inflate(R.layout.item_view,null);
        TextView nombre = (TextView) convertView.findViewById(R.id.txtnombre_listview);
        ImageView foto = (ImageView) convertView.findViewById(R.id.foto);
        ConstraintLayout fondo = (ConstraintLayout) convertView.findViewById(R.id.fondo);
        File f = new File(sdCard.getAbsolutePath(),"AppAsistencia/Fotos/"+a.matricula+".png");
        if(f.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
            foto.setImageBitmap(myBitmap);
        }
        String asis = "";
        if(a.asistio){
            asis+="Presente: Si";
            fondo.setBackgroundColor(Color.parseColor("#BAD8EA"));
        }else {
            asis+="Presente: No";
            fondo.setBackgroundColor(Color.parseColor("#D4D4D4"));
        }
        nombre.setText("Matricula: \n"+a.matricula+"\nNombre: \n"+a.nombre+"\n"+asis);
        return convertView;
    }

}
