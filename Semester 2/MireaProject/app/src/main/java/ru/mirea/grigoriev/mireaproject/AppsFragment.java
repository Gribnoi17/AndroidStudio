package ru.mirea.grigoriev.mireaproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.grigoriev.mireaproject.databinding.FragmentAppsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentAppsBinding binding;

    public AppsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppsFragment newInstance(String param1, String param2) {
        AppsFragment fragment = new AppsFragment();
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
        // Inflate the layout for this fragment
        binding = FragmentAppsBinding.inflate(inflater, container, false);

        List<String> apps = new ArrayList<>();
        List<PackageInfo> packages = getContext().getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo p = packages.get(i);
            if ((!isSystemPackage(p))) {
                String appName = p.applicationInfo.loadLabel(getContext().getPackageManager()).toString();
                apps.add(appName);
            }
        }

        String[] installedApps = new String[apps.size()];
        for (int i = 0; i < installedApps.length; i++){
            installedApps[i] = apps.get(i);
        }

        ArrayAdapter<String> appsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, installedApps);
        binding.appsList.setAdapter(appsAdapter);

        return binding.getRoot();
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
}