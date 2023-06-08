package ru.mirea.gribanovdv.mireaproject;

import static com.yandex.runtime.Runtime.getApplicationContext;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yandex.mapkit.MapKitFactory;

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

import ru.mirea.gribanovdv.mireaproject.databinding.FragmentAudioBookBinding;
import ru.mirea.gribanovdv.mireaproject.databinding.FragmentEstablishmentsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EstablishmentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EstablishmentsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context mContext;

    private MapView mapView;
    private FragmentEstablishmentsBinding binding;
    private Marker[] markers = new Marker[4];
    private TextView textView;
    public EstablishmentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EstablishmentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EstablishmentsFragment newInstance(String param1, String param2) {
        EstablishmentsFragment fragment = new EstablishmentsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentEstablishmentsBinding.inflate(inflater, container, false);
        mContext = inflater.getContext();
        mapView = binding.mapView;
        mapView.setZoomRounding(true);
        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();
        mapController.setZoom(15.0);
        GeoPoint startPoint = new GeoPoint(55.794229, 37.700772);
        mapController.setCenter(startPoint);

        MyLocationNewOverlay locationNewOverlay = new MyLocationNewOverlay(new
                GpsMyLocationProvider(mContext), mapView);
        locationNewOverlay.enableMyLocation();
        mapView.getOverlays().add(locationNewOverlay);
        CompassOverlay compassOverlay = new CompassOverlay(mContext, new
                InternalCompassOrientationProvider(mContext), mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);

        final DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
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

        return binding.getRoot();
    }

    private void printMarker(){
        markers[0].setPosition(new GeoPoint(55.794229, 37.700772));
        markers[0].setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(mContext.getApplicationContext(),"МИРЭА - Российский технологический университет",
                        Toast.LENGTH_SHORT).show();
                binding.discText.setText("Начиная с момента своего образования, вуз всегда шел в ногу со временем и постоянно расширял спектр образовательных программ. ");

                return true;
            }
        });;

        markers[1].setPosition(new GeoPoint(55.761173, 37.578433));
        markers[1].setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(mContext.getApplicationContext(),"Московский зоопарк» - парк площадью более 20 га в Москве",
                        Toast.LENGTH_SHORT).show();
                binding.discText.setText("Gредставляет собой старую и новую территории, первая из которых была открыта в 1864 году с начала основания зоопарка.");
                return true;
            }
        });

        markers[2].setPosition(new GeoPoint(55.780218, 37.593084));
        markers[2].setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(mContext.getApplicationContext(),"Депо - тут можно вкусно покушать",
                        Toast.LENGTH_SHORT).show();
                binding.discText.setText("Отличный формат, классно реализованная гастро-идея. Для встреч с друзьями на нейтральной территории – самое оно!");
                return true;
            }
        });

        markers[3].setPosition(new GeoPoint(55.731430, 37.603370));
        markers[3].setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(mContext.getApplicationContext(),"Парк Горького - мой самый любимый парк",
                        Toast.LENGTH_SHORT).show();
                binding.discText.setText("В честь Дня защиты детей в Парке Горького впервые состоится московский детский фестиваль искусств НЕБО.");
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(mContext,
                PreferenceManager.getDefaultSharedPreferences(mContext));
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(mContext,
                PreferenceManager.getDefaultSharedPreferences(mContext));
        if (mapView != null) {
            mapView.onPause();
        }
    }
}