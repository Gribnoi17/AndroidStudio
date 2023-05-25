package ru.mirea.gribanovdv.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import ru.mirea.gribanovdv.firebaseauth.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        binding.buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(binding.textEmailAddress.getText().toString(), binding.textPassword.getText().toString());
            }
        });
        binding.buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                signOut();
            }
        });
        binding.buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(binding.textEmailAddress.getText().toString(), binding.textPassword.getText().toString());
            }
        });
        binding.buttonVerifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                sendEmailVerification();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            binding.emailUserAndVerified.setText(getString(R.string.emailpassword_status_fmt,

                    user.getEmail(), user.isEmailVerified()));
            binding.fireBaseUID.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            binding.buttonSignIn.setVisibility(View.GONE);
            binding.buttonCreateAccount.setVisibility(View.GONE);;
            binding.textEmailAddress.setVisibility(View.GONE);
            binding.textPassword.setVisibility(View.GONE);
            binding.buttonSignOut.setVisibility(View.VISIBLE);
            binding.buttonVerifyEmail.setVisibility(View.VISIBLE);
            binding.buttonVerifyEmail.setEnabled(!user.isEmailVerified());
        } else {
            binding.emailUserAndVerified.setText(R.string.signed_out);
            binding.fireBaseUID.setText(null);
            binding.buttonSignIn.setVisibility(View.VISIBLE);
            binding.buttonCreateAccount.setVisibility(View.VISIBLE);
            binding.textEmailAddress.setVisibility(View.VISIBLE);
            binding.textPassword.setVisibility(View.VISIBLE);
            binding.buttonSignOut.setVisibility(View.GONE);
            binding.buttonVerifyEmail.setVisibility(View.GONE);
        }
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        if (!task.isSuccessful()) {
                            binding.emailUserAndVerified.setText(R.string.auth_failed);
                        }
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {
        binding.buttonVerifyEmail.setEnabled(false);
        final FirebaseUser user = mAuth.getCurrentUser();
        Objects.requireNonNull(user).sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                binding.buttonVerifyEmail.setEnabled(true);
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "sendEmailVerification", task.getException());
                    Toast.makeText(MainActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}