package com.example.mapdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    Location currentLocation;
    FusedLocationProviderClient fusedClient;
    private static final int REQUEST_CODE = 101;
    FrameLayout map;
    GoogleMap gMap;
    Marker marker;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_maps);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.map), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        map = findViewById(R.id.map);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();

        fusedClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String loc = searchView.getQuery().toString();
                if (loc == null){
                    Toast.makeText(MapsActivity.this, "Location Not Found", Toast.LENGTH_SHORT).show();
                } else {
                    Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    try {
                        List<Address> addressList = geocoder.getFromLocationName(loc, 1);
                        if (addressList.size() > 0){
                            LatLng latLng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
                            if (marker != null){
                                marker.remove();
                            }
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(loc);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 6);
                            gMap.animateCamera(cameraUpdate);
                            marker = gMap.addMarker(markerOptions);
                        }
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getLocation(){
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;
                    SupportMapFragment supportMapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
      this.gMap = googleMap;
        // List of coordinates for multiple markers
        LatLng bbu_phnomPenh = new LatLng(11.572293039790866, 104.88406391444144);
        LatLng bbu_siemReap = new LatLng(13.348203866962697, 103.84636432622408);
        LatLng bbu_battambang = new LatLng(13.117112828104181, 103.1996662279123);
        LatLng bbu_TboungKhmum = new LatLng(11.990462771679471, 105.47901741550686);
        LatLng bbb_Takeo = new LatLng(10.99376655993759, 104.78378351084113);
        LatLng bbu_StungTreng = new LatLng(13.526985342434049, 105.97816469739136);
        LatLng bbu_Ratanakiri = new LatLng(13.744567927019496, 106.98507982438011);
        LatLng bbu_Sihanouk = new LatLng(10.633907559540587, 103.51239372617925);


        // Adding multiple markers with custom drawables
        gMap.addMarker(new MarkerOptions().position(bbu_phnomPenh).title("Marker in Phnom Penh")
                .icon(bitmapDescriptorFromVector(R.drawable.school)));
        gMap.addMarker(new MarkerOptions().position(bbu_siemReap).title("Marker in Siem Reap")
                .icon(bitmapDescriptorFromVector(R.drawable.school)));
        gMap.addMarker(new MarkerOptions().position(bbu_battambang).title("Marker in Battambang")
                .icon(bitmapDescriptorFromVector(R.drawable.school)));
        gMap.addMarker(new MarkerOptions().position(bbu_TboungKhmum).title("Marker in Tboung Khmum")
                .icon(bitmapDescriptorFromVector(R.drawable.school)));
        gMap.addMarker(new MarkerOptions().position(bbb_Takeo).title("Marker in Takeo")
                .icon(bitmapDescriptorFromVector(R.drawable.school)));
        gMap.addMarker(new MarkerOptions().position(bbu_StungTreng).title("Marker in Stung Treng")
                .icon(bitmapDescriptorFromVector(R.drawable.school)));
        gMap.addMarker(new MarkerOptions().position(bbu_Ratanakiri).title("Marker in Ratanakiri")
                .icon(bitmapDescriptorFromVector(R.drawable.school)));
        gMap.addMarker(new MarkerOptions().position(bbu_Sihanouk).title("Marker in Sihanouk")
                .icon(bitmapDescriptorFromVector(R.drawable.school)));


        // Move the camera to the first marker and set a zoom level
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bbu_phnomPenh, 7));
    }

    private BitmapDescriptor bitmapDescriptorFromVector(int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(this, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLocation();
            }
        }
    }
}