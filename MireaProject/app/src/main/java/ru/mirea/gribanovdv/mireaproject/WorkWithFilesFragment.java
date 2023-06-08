package ru.mirea.gribanovdv.mireaproject;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ru.mirea.gribanovdv.mireaproject.databinding.FragmentWorkWithFilesBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkWithFilesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkWithFilesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentWorkWithFilesBinding binding;
    public WorkWithFilesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkWithFilesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkWithFilesFragment newInstance(String param1, String param2) {
        WorkWithFilesFragment fragment = new WorkWithFilesFragment();
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

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
    //запись в файл
    public void writeFileToExternalStorage(String format) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, binding.editTextTextPersonName.getText() + format);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsoluteFile());
            OutputStreamWriter output = new OutputStreamWriter(fileOutputStream);
            output.write(String.valueOf(binding.editTextTextPersonName2.getText()));
            output.close();
        } catch (IOException e) {
            Log.w("ExternalStorage", "Error writing " + file, e);
        }
    }
    //роверяем внешнее хранилище на доступность чтения
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
    //чтение из файла
    public void readFileFromExternalStorage() {
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        String formatFile = String.valueOf(binding.editTextTextPersonName.getText());
        formatFile = "." + formatFile.substring(formatFile.length() - 3).toLowerCase();
        Log.d(TAG, formatFile);
        File file = new File(path, binding.editTextTextPersonName.getText() + formatFile);
        try {
            FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            List<String> lines = new ArrayList<String>();
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            Log.w("ExternalStorage", String.format("Read from file %s successful", lines));
            binding.editTextTextPersonName2.setText(lines.toString());
        } catch (Exception e) {
            Log.w("ExternalStorage", String.format("Read from file %s failed", e.getMessage()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWorkWithFilesBinding.inflate(getLayoutInflater());
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExternalStorageReadable()) readFileFromExternalStorage();
            }
        });
        binding.buttonTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExternalStorageWritable()) writeFileToExternalStorage(".txt");

            }
        });
        binding.buttonPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExternalStorageWritable()) writeFileToExternalStorage(".pdf");
            }
        });
        binding.buttonDOC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExternalStorageWritable()) writeFileToExternalStorage(".doc");
            }
        });
        binding.buttonRTF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExternalStorageWritable()) writeFileToExternalStorage(".rtf");
            }
        });


        return binding.getRoot();
    }
}