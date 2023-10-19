package ru.mirea.grigoriev.mireaproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.mirea.grigoriev.mireaproject.databinding.ActivityAuthorizationBinding;

public class AuthorizationActivity extends AppCompatActivity {
    private static final String TAG = AuthorizationActivity.class.getSimpleName();

    private ActivityAuthorizationBinding binding;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthorizationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        String deviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);

        checkExcList();

        binding.buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(binding.editTextEmail.getText());
                String pass = String.valueOf(binding.editTextTextPassword.getText());
                signIn(email, pass);
            }
        });

        binding.buttonCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(binding.editTextEmail.getText());
                String pass = String.valueOf(binding.editTextTextPassword.getText());
                binding.textViewID.setText(String.format("Device_ID:%s", deviceId));
                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("UserID");
                database.push().setValue(deviceId);
                createAccount(email, pass);

            }
        });

        binding.buttonVerifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailVerification();
            }
        });
    }


    private void checkExcList() {
        List<String> exceptionList = new ArrayList<>();
        exceptionList.add("com.anydesk.anydeskandroid");
        exceptionList.add("com.anydesk.adcontrol.ad1");

        List<ApplicationInfo> packages = getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            if (exceptionList.contains(packageInfo.packageName)){
                Intent intent = new Intent(this, WarningActivity.class);
                startActivity(intent);
            }
            Log.d(TAG, "Installed package :" + packageInfo.packageName);
            //Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
            //Log.d(TAG, "Launch Activity :" + getPackageManager().getLaunchIntentForPackage(packageInfo.packageName));

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            binding.buttonVerifyEmail.setEnabled(!user.isEmailVerified());
        }
    }
    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail: success");
                            Toast.makeText(AuthorizationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                                // If sign in fails, display a message to the user.
                            Toast.makeText(AuthorizationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(AuthorizationActivity.this, MainActivity.class);
                            startActivity(intent);
                            updateUI(user);
                        } else {
                        // If sign in fails, display a message to the user.
                            Toast.makeText(AuthorizationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            Toast.makeText(AuthorizationActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
    // [END sign_in_with_email]
    }

    private void sendEmailVerification() {
        // Disable button
        binding.buttonVerifyEmail.setEnabled(false);
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        Objects.requireNonNull(user).sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    // [START_EXCLUDE]
                    // Re-enable button
                        binding.buttonVerifyEmail.setEnabled(true);
                        if (task.isSuccessful()) {
                            Toast.makeText(AuthorizationActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AuthorizationActivity.this, "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if (!task.isSuccessful()){
                            Toast.makeText(AuthorizationActivity.this, "Enter your email and passsword", Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
            // [END send_email_verification]
    }

}