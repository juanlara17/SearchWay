package com.ingenieria.searchway;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Main extends ActionBarActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listaParametros;
    TextView    LabelEstado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LabelEstado = (TextView)findViewById(R.id.tvEstado);
        listaParametros = (ListView)findViewById(R.id.lvParametros);
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        List<String> listProviders = locationManager.getAllProviders();
        LocationProvider locationProvider = locationManager.getProvider(listProviders.get(0));
        int presicion = locationProvider.getAccuracy();
        boolean obtieneAltitud = locationProvider.supportsAltitude();
        int consumoRecursos = locationProvider.getPowerRequirement();

        items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        listaParametros.setAdapter(itemsAdapter);

        Criteria req = new Criteria();
        req.setAccuracy(Criteria.ACCURACY_FINE);
        req.setAltitudeRequired(true);

        String mejorProviderCriter = locationManager.getBestProvider(req, false);
        List<String> listProviderCriter = locationManager.getProviders(req, false);

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            mostrarAvisoGpsDeshabilitado();
        }

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mostrarUbicacionActual(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                LabelEstado.setText("Provider Status: " + i);
            }

            @Override
            public void onProviderEnabled(String s) {
                LabelEstado.setText("PROVIDER ON");
            }

            @Override
            public void onProviderDisabled(String s) {
                LabelEstado.setText("PROVIDER OFF");
            }
        };

        Button GoMap = (Button) findViewById(R.id.btnGoMap);
        GoMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplication(), MapsActivity.class);
                startActivity(i);
            }
        });

    }

    private void mostrarUbicacionActual(final Location loc) {
        Button Ingresar = (Button)findViewById(R.id.btnIngresar);
        Ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loc != null){
                    String Latitud = String.valueOf(loc.getLatitude()).toString();
                    itemsAdapter.add(Latitud);
                    String Longitud = String.valueOf(loc.getLongitude()).toString();
                    itemsAdapter.add(Longitud);
                    String Presicion = String.valueOf(loc.getAccuracy()).toString();
                    itemsAdapter.add(Presicion);
                }
            }
        });
    }


    private void mostrarAvisoGpsDeshabilitado() {
        Toast.makeText(this, "SU GPS NO SE ENCUENTRA ACTIVADO", Toast.LENGTH_SHORT).show();
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
}
