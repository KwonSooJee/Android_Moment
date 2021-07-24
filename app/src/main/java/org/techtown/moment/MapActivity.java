package org.techtown.moment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity {

    SupportMapFragment mapFragment;
    GoogleMap map;

    MarkerOptions myLocationMarker;

    Button mapStartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d("Map", "지도 준비됨.");
                map=googleMap;
            }
        });

        mapStartBtn=findViewById(R.id.mapStartBtn);
        mapStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationService();
            }
        });

    }


    /*========================MAP===============================*/
    class GPSListener implements LocationListener {
        public void onLocationChanged(Location location){
            showCurrentLocation(location);
            showToast("내 위치확인 요청함");
        }
    }

    public void startLocationService(){
        LocationManager manager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);

        long minTime=10000;
        float minDistance=0;
        GPSListener gpsListener=new GPSListener();

        try {
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    public void showCurrentLocation(Location location){

        double latitude=location.getLatitude();
        double longitude=location.getLongitude();

        String message = "내 위치 -> Latitude : "+ latitude + "\nLongitude:"+ longitude;
        Log.d("Map", message);

        LatLng curPoint = new LatLng(latitude,longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(curPoint,15));

        showToast("확인");

    }


    /*============================================================*/
    public void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}