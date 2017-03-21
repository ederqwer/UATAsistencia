package com.example.jahir.uatasistencia;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Scanner;

import static com.example.jahir.uatasistencia.R.id.lvalumnos;
import static com.example.jahir.uatasistencia.R.id.lvgrupos;

public class index extends AppCompatActivity {
    File sdCard = Environment.getExternalStorageDirectory();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        File frot = new File(sdCard.getAbsolutePath(),"/AppAsistencia");
        if(frot.mkdirs()){
            Toast.makeText(this, "Se ha creado directorio:\n/AppAsistencia\nPara el uso correcto de la APP", Toast.LENGTH_LONG).show();
            File fotos =new File(sdCard.getAbsolutePath(), "/AppAsistencia/Fotos");
            File reportes = new File(sdCard.getAbsolutePath(), "/AppAsistencia/Reportes");
            File bd_ini = new File(sdCard.getAbsolutePath(), "/AppAsistencia/Bd");
            fotos.mkdirs();reportes.mkdirs();bd_ini.mkdirs();
            File f = new File(sdCard.getAbsolutePath(),"/AppAsistencia/config.txt");
            File f2 = new File(sdCard.getAbsolutePath(), "/AppAsistencia/misgrupos.txt");
            File f3 = new File(sdCard.getAbsolutePath(), "/AppAsistencia/misalumnos.txt");
            try {
                OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(f));
                out.flush();
                out.close();
                out = new OutputStreamWriter(new FileOutputStream(f2));
                out.flush();
                out.close();
                out = new OutputStreamWriter(new FileOutputStream(f3));
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void grupos(View v){
        Intent i = new Intent(this, grupos.class);
        startActivity(i);
    }

    public void importar(View v){
        Intent i = new Intent(this, importar.class);
        startActivity(i);
    }
}
