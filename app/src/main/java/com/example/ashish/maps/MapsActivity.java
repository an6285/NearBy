package com.example.ashish.maps;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ArrayList<String> names=new ArrayList<String>();
        ArrayList<Double> latitudes=new ArrayList<Double>();
        ArrayList<Double> longitudes=new ArrayList<Double>();
        names=Main2Activity.names;
        latitudes=Main2Activity.latitudes;
        longitudes=Main2Activity.longitudes;

        MarkerOptions m1=new MarkerOptions();
        double your_lat=getIntent().getDoubleExtra("latitude",0);
        double your_long=getIntent().getDoubleExtra("longitude",0);
        LatLng ll=new LatLng(your_lat,your_long);
        m1.title("Your Position");
        m1.position(ll);
        m1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mMap.addMarker(m1);

        int len=names.size();
        for(int i=0;i<len;i++)
        {
            MarkerOptions mo=new MarkerOptions();
            double lat=latitudes.get(i);
            double longi=longitudes.get(i);
            LatLng latLng=new LatLng(lat,longi);
            mo.title(names.get(i));
            mo.position(latLng);
            //mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mMap.addMarker(mo);
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 18.0f));
        // Add a marker in Sydney and move the camera
        //LatLng faridabad = new LatLng(28.4089,77.3178);
        //mMap.addMarker(new MarkerOptions().position(faridabad).title("Marker in Faridabad"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(faridabad));
    }
}
