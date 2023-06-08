package ru.mirea.gribanovdv.mireaproject;

import static android.content.Context.SENSOR_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.mirea.gribanovdv.mireaproject.databinding.FragmentCompasBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompasFragment extends Fragment implements SensorEventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int temp = 0;

    private TextView azimuthTextView;
    private TextView directionText;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    public CompasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompasFragment newInstance(String param1, String param2) {
        CompasFragment fragment = new CompasFragment();
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
        View view =inflater.inflate(R.layout.fragment_compas, container, false);

        sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        azimuthTextView = view.findViewById(R.id.textViewAzimuth);
        directionText = view.findViewById(R.id.directionText);

        return view;
    }
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float valueAzimuth = Math.round(event.values[0]);
            azimuthTextView.setText("Azimuth: " + valueAzimuth);
            if (valueAzimuth > 0 && valueAzimuth < 3.0 && temp == 1){
                directionText.setText("Направление: Юг");
            }
            if (valueAzimuth > 3.1 && valueAzimuth < 6.5 && temp == 1){
                directionText.setText("Направление: Юго-Запад");
            }
            if (valueAzimuth > 6.5 && valueAzimuth < 9.0 && temp == 1){
                directionText.setText("Направление: Запад");
            }
            if (valueAzimuth < 0 && valueAzimuth >- 3.0 && temp == 1) {
                directionText.setText("Направление: Юг");
            }
            if (valueAzimuth < -3.1 && valueAzimuth > -6.5 && temp == 1){
                directionText.setText("Направление: Юго-Восток");
            }
            if (valueAzimuth < -6.5 && valueAzimuth > -9.0 && temp == 1){
                directionText.setText("Направление: Восток");
            }
            if (valueAzimuth > 0 && valueAzimuth < 3.0){
                directionText.setText("Направление: Север");
                temp = 0;
            }
            if (valueAzimuth > 3.1 && valueAzimuth < 6.5){
                directionText.setText("Направление: Северо-Запад");
                temp = 0;
            }
            if (valueAzimuth > 6.5 && valueAzimuth < 9.0){
                directionText.setText("Направление: Запад");
                temp = 1;
            }
            if (valueAzimuth < 0 && valueAzimuth >- 3.0){
                directionText.setText("Направление: Север");
                temp = 0;
            }
            if (valueAzimuth < -3.1 && valueAzimuth > -6.5){
                directionText.setText("Направление: Северо-Восток");
                temp = 0;
            }
            if (valueAzimuth < -6.5 && valueAzimuth > -9.0){
                directionText.setText("Направление: Восток");
                temp = 1;
            }



        }
    }

    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}