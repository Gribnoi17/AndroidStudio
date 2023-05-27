package ru.mirea.gribanovdv.osmmaps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import ru.mirea.gribanovdv.osmmaps.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 100;
    private MapView mapView = null;
    private ActivityMainBinding binding;
    private MyLocationNewOverlay locationNewOverlay;
    Marker[] markers = new Marker[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int coarseLocationPermissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int fineLocationPermissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (coarseLocationPermissionStatus == PackageManager.PERMISSION_GRANTED
                && fineLocationPermissionStatus == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION);
        }

        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mapView = binding.mapView;

        mapView.setZoomRounding(true);
        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();
        mapController.setZoom(15.0);
        GeoPoint startPoint = new GeoPoint(55.794229, 37.700772);
        mapController.setCenter(startPoint);

        locationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()),mapView);
        locationNewOverlay.enableMyLocation();
        mapView.getOverlays().add(this.locationNewOverlay);

        CompassOverlay compassOverlay = new CompassOverlay(getApplicationContext(), new InternalCompassOrientationProvider(getApplicationContext()), mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);

        final Context context = this.getApplicationContext();
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        mapView.getOverlays().add(scaleBarOverlay);

        for (int i = 0; i < markers.length; i++){
            markers[i] = new Marker(mapView);
            mapView.getOverlays().add(markers[i]);
            markers[i].setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.osm_ic_follow_me_on, null));
        }

        printMarker();

    }

    private void printMarker(){
        markers[0].setPosition(new GeoPoint(55.794229, 37.700772));
        markers[0].setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(getApplicationContext(),"МИРЭА - Российский технологический университет",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });;

        markers[1].setPosition(new GeoPoint(55.761173, 37.578433));
        markers[1].setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(getApplicationContext(),"Московский зоопарк» - парк площадью более 20 га в Москве",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        markers[2].setPosition(new GeoPoint(55.780218, 37.593084));
        markers[2].setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(getApplicationContext(),"Депо - тут можно вкусно покушать",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        markers[3].setPosition(new GeoPoint(55.731430, 37.603370));
        markers[3].setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(getApplicationContext(),"Парк Горького - мой самый любимый парк",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if (mapView != null) {
            mapView.onResume();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(getApplicationContext(),

                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        if (mapView != null) {
            mapView.onPause();
        }
    }
}