package com.example.manuelseguranavarro.festival;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Spinner listaCiudades;
    String [] stringCiudades = {"Elige la ciudad ","opcion1", "opcion2"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listaCiudades = (Spinner)findViewById(R.id.spinnerCiudades);

        //Pasamos la vista que es esta, despues el recurso que queremos ver y lo que vamos a mostrar
        ArrayAdapter<String> adaptadorSpinner = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,stringCiudades);
        listaCiudades.setAdapter(new AdaptadorMain(this,R.layout.support_simple_spinner_dropdown_item,stringCiudades));

        //Metodo para que al pulsar un item de la lista nos lleve a los datos de esa ciudad
        listaCiudades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 1:
                        Toast tosta = Toast.makeText(getApplicationContext(), stringCiudades[position], Toast.LENGTH_LONG);

                        tosta.show();
                        break;
                    case 2:
                        Toast tostada = Toast.makeText(getApplicationContext(), stringCiudades[position], Toast.LENGTH_LONG);
                        tostada.show();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class AdaptadorMain extends ArrayAdapter<String>{

        public AdaptadorMain(Context context, int resource, String[] objects) {
            super(context, resource, objects);
        }
        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater= getLayoutInflater();
            View row=inflater.inflate(R.layout.spinercuston, parent, false);
            TextView label=(TextView)row.findViewById(R.id.spinnerTitulo);
            label.setText(stringCiudades[position]);


            return row;
        }
    }
}
