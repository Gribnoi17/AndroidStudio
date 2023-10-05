package ru.mirea.grigoriev.mireaproject;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;

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

import ru.mirea.grigoriev.mireaproject.databinding.ActivityMapBinding;

public class MapActivity extends AppCompatActivity {

    private MapView mapView = null;
    MyLocationNewOverlay locationNewOverlay;
    private static final int REQUEST_CODE_PERMISSION = 100;
    private boolean isWork = false;
    private ActivityMapBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int coarseLocPermissionStatus = ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION);
        int fineLocPermissionStatus = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        if (coarseLocPermissionStatus == PackageManager.PERMISSION_GRANTED && fineLocPermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        }
        else {
            ActivityCompat.requestPermissions(MapActivity.this, new String[] {ACCESS_COARSE_LOCATION,
                    ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
        }
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mapView = binding.mapView;
        mapView.setZoomRounding(true);
        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();
        mapController.setZoom(15.0);
        GeoPoint startPoint = new GeoPoint(55.751639, 37.618937);
        mapController.setCenter(startPoint);

        MyLocationNewOverlay locationNewOverlay = new MyLocationNewOverlay(new
                GpsMyLocationProvider(getApplicationContext()), mapView);
        locationNewOverlay.enableMyLocation();
        mapView.getOverlays().add(locationNewOverlay);

        CompassOverlay compassOverlay = new CompassOverlay(getApplicationContext(), new
                InternalCompassOrientationProvider(getApplicationContext()), mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);

        final Context context = this.getApplicationContext();
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        mapView.getOverlays().add(scaleBarOverlay);

        // Останкинская телебашня маркер
        Marker towerMarker = new Marker(mapView);
        towerMarker.setPosition(new GeoPoint(55.819653, 37.611689));
        towerMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(getApplicationContext(),"Останкинская телебашня\nАдрес: ул. Академика Королёва,15,к.2",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mapView.getOverlays().add(towerMarker);
        towerMarker.setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.marker_default, null));
        towerMarker.setTitle("Останкинская телебашня");

        // ВДНХ маркер
        Marker VDNKHMarker = new Marker(mapView);
        VDNKHMarker.setPosition(new GeoPoint(55.826281, 37.637581));
        VDNKHMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(getApplicationContext(),"ВДНХ\nАдрес: проспект Мира, 119",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mapView.getOverlays().add(VDNKHMarker);
        VDNKHMarker.setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.marker_default, null));
        VDNKHMarker.setTitle("ВДНХ");

        //Московский планетарий маркер
        Marker planetariumMarker = new Marker(mapView);
        planetariumMarker.setPosition(new GeoPoint(55.761280, 37.583493));
        planetariumMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(getApplicationContext(),"Московский планетарий\nАдрес: Садовая-Кудринская улица, 5, стр. 1",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mapView.getOverlays().add(planetariumMarker);
        planetariumMarker.setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.marker_default, null));
        planetariumMarker.setTitle("Планетарий");

        //Ресторан "АйДаБаран" маркер
        Marker restaurantMarker = new Marker(mapView);
        restaurantMarker.setPosition(new GeoPoint(55.682673, 37.663142));
        restaurantMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(getApplicationContext(),"Ресторан АйДаБаран\nАдрес: проспект Андропова, 22",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mapView.getOverlays().add(restaurantMarker);
        restaurantMarker.setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.marker_default, null));
        restaurantMarker.setTitle("АйДаБаран");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Configuration.getInstance().save(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if (mapView != null) {
            mapView.onPause();
        }
    }
}
