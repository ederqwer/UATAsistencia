package com.example.jahir.uatasistencia;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class grupos extends AppCompatActivity {
    ArrayList<grupo> g = new ArrayList<>();
    archivos fscanner = new archivos();
    File sdCard = Environment.getExternalStorageDirectory();
    Spinner sp_grupos;
    ListView lvalumnos;
    ArrayAdapter<String> ad_sp_grupos;
    adapter_alumnos adapter;
    ArrayList<BluetoothDevice> celulares;
    ArrayList<String> map_mac = new ArrayList<>();
    int mode = 0;
    BroadcastReceiver receiver = new  BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            receiverused = true;
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(map_mac.contains(device.getAddress())==false && mode == 0){
                    celulares.add(device);
                    ad_spdialog.notifyDataSetChanged();
                }else if(mode==1){
                    if(adapter!=null){
                        for (alumno x:
                             adapter.students) {
                            if(x.mac.equals(device.getAddress()) && x.asistio == false){
                                x.asistio = true;
                                x.asistencias++;
                                fscanner.guardar_misgrupos(context,g);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }
        }
    };
    BluetoothAdapter btAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos);
        sp_grupos= (Spinner) findViewById(R.id.spgrupos);
        ad_sp_grupos = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,0);
        sp_grupos.setAdapter(ad_sp_grupos);
        lvalumnos = (ListView) findViewById(R.id.alumnos_grupos);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        celulares = new ArrayList<>();
        File lastDay = new File(sdCard.getAbsolutePath(),"AppAsistencia/lastDay.txt");
        if(lastDay.exists()){
            try{
                InputStreamReader isr =new InputStreamReader(new FileInputStream(lastDay));
                Scanner lee = new Scanner(isr);
                String day = "";
                if(lee.hasNext()){
                    day = lee.nextLine();
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                    Calendar hoy = Calendar.getInstance();
                    hoy.setTime(format.parse(day));
                    for (grupo x:
                            g) {
                        for (alumno y:
                             x.lalumno) {
                            y.asistio = false;
                        }
                    }
                }

            }catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        try{
            File f = new File(sdCard.getAbsolutePath(),"AppAsistencia/misgrupos.txt");
            InputStreamReader isr = new InputStreamReader(new FileInputStream(f));
            Scanner sc = new Scanner(isr);

            while(sc.hasNext()){
                grupo g = new grupo();
                g.materia = sc.nextLine();
                g.grupo = sc.nextLine();
                g.grado = sc.nextLine();
                g.salon = sc.nextLine();
                g.horas = sc.nextLine();
                while(sc.hasNext()){
                    alumno a = new alumno();
                    a.matricula = sc.nextLine();
                    if(a.matricula.equals("#")) break;
                    a.nombre = sc.nextLine();
                    a.mac = sc.nextLine();
                    a.asistencias = Integer.parseInt(sc.nextLine());
                    g.lalumno.add(a);
                    map_mac.add(a.mac);
                }
                Collections.sort(g.lalumno, new Comparator<alumno>() {
                    @Override
                    public int compare(alumno o1, alumno o2) {
                        return o1.nombre.compareTo(o2.nombre);
                    }
                });
                this.g.add(g);
            }
            isr.close();sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(g, new Comparator<grupo>() {
            @Override
            public int compare(grupo o1, grupo o2) {
                return (o1.grado+o1.grupo).compareTo(o2.grado+o2.grupo);
            }
        });
        for (int i = 0; i < g.size(); i++) {
            ad_sp_grupos.add(g.get(i).grado+"-"+g.get(i).grupo+" "+g.get(i).materia);
        }
        sp_grupos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter= new adapter_alumnos(view.getContext(), g.get(position).lalumno);
                lvalumnos.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        lvalumnos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actions_list(position, view.getContext());
            }
        });
    }
    boolean receiverused = false;
    @Override
    protected void onDestroy() {
        if(receiverused) unregisterReceiver(receiver);
        super.onDestroy();
    }


    adapter_device ad_spdialog;
    ImageView foto;
    public void actions_list(final int i, Context c){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View promptView = layoutInflater.inflate(R.layout.dialog_view, null);
        final Dialog alertD = new Dialog(c);
        alertD.setContentView(promptView);
        alertD.setTitle("Editar Alumno");
        final EditText matricula = (EditText) promptView.findViewById(R.id.txtdialogmat) ;
        final EditText nombre = (EditText) promptView.findViewById(R.id.txtdialognom);
        final EditText mac = (EditText) promptView.findViewById(R.id.txtdialogmac);
        Button btnAdd1 = (Button) promptView.findViewById(R.id.bt1);
        Button btnAdd2 = (Button) promptView.findViewById(R.id.bt2);
        Button btnRem3 = (Button) promptView.findViewById(R.id.bt3);
        Button btscan = (Button) promptView.findViewById(R.id.btscan);
        foto = (ImageView) promptView.findViewById(R.id.dialogfoto);
        File f = new File(sdCard.getAbsolutePath(),"AppAsistencia/Fotos/"+adapter.students.get(i).matricula+".png");
        if(f.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
            foto.setImageBitmap(myBitmap);
        }
        final Spinner macs  = (Spinner) promptView.findViewById(R.id.spdialog);
        matricula.setText(adapter.students.get(i).matricula);
        nombre.setText(adapter.students.get(i).nombre);
        mac.setText(adapter.students.get(i).mac);
        celulares.clear();
        ad_spdialog = new adapter_device(c,celulares);
        macs.setAdapter(ad_spdialog);

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_camara_auto(v);
            }
        });
        //Guardar Cambios
        btnAdd1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(nombre.getText().toString().equals("")||
                        matricula.getText().toString().equals("")||
                        mac.getText().toString().equals("")) {
                    Toast.makeText(v.getContext(), "No se admiten campos vacios", Toast.LENGTH_SHORT).show();
                    return;
                }
                alumno x = adapter.students.get(i);
                x.nombre = nombre.getText().toString();
                x.matricula = matricula.getText().toString();
                map_mac.remove(x.mac);
                x.mac = mac.getText().toString();
                map_mac.add(x.mac);
                fscanner.guardar_misgrupos(v.getContext(),g);
                try {
                    File dir = new File(sdCard.getAbsolutePath(), "/AppAsistencia/Fotos");
                    String fileName = x.matricula + ".png";
                    File outFile = new File(dir, fileName);
                    Bitmap bmp = ((BitmapDrawable) foto.getDrawable()).getBitmap();
                    FileOutputStream outStream = new FileOutputStream(outFile);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    outStream.flush();
                    outStream.close();
                }catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                alertD.cancel();
            }
        });
        //Cancel Button
        btnAdd2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertD.cancel();
            }
        });
        //bluetooth scan - Spinner
        btscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code Bluetooth Scanner
                mode = 0;
                celulares.clear();
                ad_spdialog.notifyDataSetChanged();
                Toast.makeText(v.getContext(), "Buscando Bluetooths...", Toast.LENGTH_SHORT).show();
                if(btAdapter.isEnabled()==false) btAdapter.enable();
                if(btAdapter.isDiscovering()) btAdapter.cancelDiscovery();
                btAdapter.startDiscovery();
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(receiver, filter);
            }
        });
        //Subir mac al editText
        macs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView aux = (TextView) macs.getSelectedView().findViewById(R.id.nmac);
                String dir = aux.getText().toString();
                mac.setText(dir);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //eliminar
        btnRem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alumno y = adapter.students.get(i);
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Confirmar")
                        .setMessage("Esta seguro de eliminar al alumno:\n{"
                        +y.nombre+"} del grupo?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.students.remove(i);
                                adapter.notifyDataSetChanged();
                                alertD.cancel();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        alertD.show();
    }

    Intent i_camera;
    static final int CAMERA_PIC_REQUEST = 1337;
    public Uri mImageCaptureUri;
    public void bt_camara_auto(View v){
        i_camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        i_camera.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,mImageCaptureUri);
        startActivityForResult(i_camera,CAMERA_PIC_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if(resultCode == Activity.RESULT_OK && requestCode==CAMERA_PIC_REQUEST){
                Bundle ext = data.getExtras();
                Bitmap bmp = (Bitmap) ext.get("data");
                foto.setImageBitmap(bmp);
            }}catch(Exception e){}
    }

    public void pasarLista(View v){
        mode = 1;
        celulares.clear();
        Toast.makeText(v.getContext(), "Buscando Bluetooths...", Toast.LENGTH_SHORT).show();
        if(btAdapter.isEnabled()==false) btAdapter.enable();
        if(btAdapter.isDiscovering()) btAdapter.cancelDiscovery();
        btAdapter.startDiscovery();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }
}
