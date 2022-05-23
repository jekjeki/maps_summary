package com.example.maps_test_3;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.maps_test_3.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
     * If Google Play services is not installe d on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng binus = new LatLng(-6.219522841576231, 106.99977548138189);
        mMap.addMarker(new MarkerOptions().position(binus).title("Marker in binus"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(binus,20f));

        enableLocation();

        //map long click
        setMapLongClick(mMap);

        //pointOfInterest method
        pointOfInterest(mMap);

        //styling map -> google map style
        try {
            boolean styleMap = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this,R.raw.style_maps)
            );
        } catch (Resources.NotFoundException r){

        }

        //latitude longitude polyline
        LatLng k1 = new LatLng(-6.21809363756656, 106.99897081873);
        LatLng k2 = new LatLng(-6.219266865024267, 107.00158865455754);
        LatLng k3 = new LatLng(-6.220930710384173, 107.00168521407575);
        LatLng k4 = new LatLng(-6.2206960658446935, 106.99900300523606);

        //polylines
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(k1)
                .add(k2)
                .add(k3)
                .add(k4)
                .color(Color.WHITE);

        Polyline polyline = mMap.addPolyline(polylineOptions);

        //array latitude longitude
        ArrayList<LatLng> points = new ArrayList<>();
        points.add(new LatLng(-6.221751965496759, 107.00398118513057));
        points.add(new LatLng(-6.222125262828559, 107.0034018280212));
        points.add(new LatLng(-6.22179462806238, 107.0030906917958));

        // latitude longitude polygon
        LatLng g1 = new LatLng(-6.221282677046304, 107.00299413227758);
        LatLng g2 = new LatLng(-6.2215919808448215, 107.00483949195926);
        LatLng g3 = new LatLng(-6.222989178493766, 107.00256497886323);

        PolygonOptions polygonOptions = new PolygonOptions()
                .add(g1,g2,g3)
                .strokeColor(Color.WHITE)
                .fillColor(Color.GREEN)
                .addHole(points);

        Polygon polygon = mMap.addPolygon(polygonOptions);

        //circle
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(-6.220045460011761, 107.00444252511409))
                .radius(20)
                .fillColor(Color.CYAN)
                .strokeColor(Color.MAGENTA);

        Circle circle = mMap.addCircle(circleOptions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_maps,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //add marker -> setMapLongClick
    private void setMapLongClick(GoogleMap googleMap){

        //proses menambahkan marker
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                String snippet = String.format(Locale.getDefault(),
                        "Lat: "+latLng.latitude+" Long: "+latLng.longitude);

                //urutan objek -> objek (new marker), position, title, snippet
                googleMap.addMarker(new MarkerOptions().position(latLng).title(snippet)
                        );
            }
        });
    }




    //point of interest
    private void pointOfInterest(GoogleMap googleMap){
        googleMap.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(@NonNull PointOfInterest poi) {
                mMap.clear();
                googleMap.addMarker(new MarkerOptions().position(poi.latLng).title("place: "+poi.name
                +"place id: "+poi.placeId));
            }
        });
    }

    private void enableLocation(){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableLocation();
                }
                break;
        }
    }
}