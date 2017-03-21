package com.example.jahir.uatasistencia;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
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
import java.util.ArrayList;
import java.util.Scanner;


public class importar extends AppCompatActivity {
    static final int REQUEST_CODE = 33337;
    File sdCard = Environment.getExternalStorageDirectory();
    ListView lvgrupos;
    ListView lvalumnos;
    ArrayAdapter<String> adgrupos;
    ArrayAdapter<String> adalumnos;
    ArrayList<grupo> g = new ArrayList<>();
    archivos fscanner = new archivos();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importar);
        lvgrupos = (ListView) findViewById(R.id.lvgrupos);
        lvalumnos = (ListView) findViewById(R.id.lvalumnos);
        adgrupos =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 0);
        adalumnos = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 0);
        lvgrupos.setAdapter(adgrupos);
        lvalumnos.setAdapter(adalumnos);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                Uri path = data.getData();
                try{
                    File f = new File(path.getPath());
                    InputStreamReader isr = new InputStreamReader(new FileInputStream(f));
                    Scanner sc = new Scanner(isr);

                    while(sc.hasNext()){
                        grupo g = new grupo();
                        g.materia = sc.nextLine();
                        g.grupo = sc.nextLine();
                        g.grado = sc.nextLine();
                        g.salon = sc.nextLine();
                        g.horas = sc.nextLine();
                        String asign = g.materia+"\n"+g.grupo+"\n"+g.grado+"\n"+g.salon+"\n"+g.horas;
                        adgrupos.add(asign);
                        while(sc.hasNext()){
                            alumno a = new alumno();
                            a.matricula = sc.nextLine();
                            String alumno = a.matricula;
                            if(alumno.equals("#")) break;
                            a.nombre = sc.nextLine();
                            alumno+="\n"+a.nombre+"\n"+asign;
                            adalumnos.add(alumno);
                            g.lalumno.add(a);
                        }
                        this.g.add(g);
                    }
                    isr.close();sc.close();
                    adgrupos.notifyDataSetChanged();
                    adalumnos.notifyDataSetChanged();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void importar(View v){
        Intent isamsung = new Intent("com.sec.android.app.myfiles.PICK_DATA");

        if(getPackageManager().resolveActivity(isamsung,0)!=null){
            Toast.makeText(this, "Seleccionar archivo (Samsung)", Toast.LENGTH_SHORT).show();
            isamsung.putExtra("CONTENT_TYPE", "text/plain");
            isamsung.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(isamsung, REQUEST_CODE);
        }else{
            Toast.makeText(this, "Seleccionar archivo", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/plain");
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    public void guardar_import(View v){
        fscanner.guardar_misgrupos(v.getContext(), g);
    }
}
