package ru.mirea.grigoriev.mireaproject;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.grigoriev.mireaproject.databinding.ActivityAuthorizationBinding;
import ru.mirea.grigoriev.mireaproject.databinding.ActivityWarningBinding;

public class WarningActivity extends AppCompatActivity {

    private String anydeskPackageOne = "com.anydesk.anydeskandroid";
    private String anydeskPackageTwo = "com.anydesk.adcontrol.ad1";

    private ActivityWarningBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWarningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        List<String> exceptionList = new ArrayList<>();
        exceptionList.add(anydeskPackageOne);
        exceptionList.add(anydeskPackageTwo);

        List<ApplicationInfo> packages = getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            if (!exceptionList.contains(packageInfo.packageName)){
                Intent intent = new Intent(this, AuthorizationActivity.class);
                startActivity(intent);
            }
        }
    }
}