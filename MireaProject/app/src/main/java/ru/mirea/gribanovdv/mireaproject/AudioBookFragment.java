package ru.mirea.gribanovdv.mireaproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.mirea.gribanovdv.mireaproject.databinding.ActivityMainBinding;
import ru.mirea.gribanovdv.mireaproject.databinding.FragmentAudioBookBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AudioBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioBookFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentAudioBookBinding binding;
    public AudioBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AudioBookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AudioBookFragment newInstance(String param1, String param2) {
        AudioBookFragment fragment = new AudioBookFragment();
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
        binding = FragmentAudioBookBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(getContext(), PlayerService.class);
                getActivity().startForegroundService(serviceIntent);
            }
        });

        binding.buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().stopService(
                        new Intent(getContext(), PlayerService.class));
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}