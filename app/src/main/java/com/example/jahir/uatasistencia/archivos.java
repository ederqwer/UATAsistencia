package com.example.jahir.uatasistencia;

import android.content.Context;
import android.os.Environment;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Jahir on 21/03/2017.
 */

public class archivos {
    File sdCard = Environment.getExternalStorageDirectory();
    public archivos(){}
    public void guardar_misgrupos(Context c, ArrayList<grupo> g){
        try{
            File f = new File(sdCard.getAbsolutePath(),"AppAsistencia/misgrupos.txt");
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(f));
            for (int i = 0; i < g.size(); i++) {
                out.write(g.get(i).materia+"\n");out.flush();
                out.write(g.get(i).grupo+"\n");out.flush();
                out.write(g.get(i).grado+"\n");out.flush();
                out.write(g.get(i).salon+"\n");out.flush();
                out.write(g.get(i).horas+"\n");out.flush();
                for (int j = 0; j < g.get(i).lalumno.size(); j++) {
                    out.write(g.get(i).lalumno.get(j).matricula+"\n");out.flush();
                    out.write(g.get(i).lalumno.get(j).nombre+"\n");out.flush();
                    out.write(g.get(i).lalumno.get(j).mac+"\n");out.flush();
                    out.write(g.get(i).lalumno.get(j).asistencias+"\n");out.flush();
                }
                out.write("#\n");out.flush();
            }
            out.close();
            Toast toast = Toast.makeText(c,"Guardado con exito", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }catch (IOException e){
            Toast.makeText(c, "Bug: "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
