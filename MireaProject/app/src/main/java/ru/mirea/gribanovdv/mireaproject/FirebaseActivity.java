package ru.mirea.gribanovdv.mireaproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.mirea.gribanovdv.mireaproject.databinding.FragmentFirebaseBinding;

public class FirebaseActivity  extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private FragmentFirebaseBinding binding;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentFirebaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(binding.emailEdit.getText());
                String password = String.valueOf(binding.passwordEdit.getText());
                signIn(email, password, view);
            }
        });
        binding.createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(binding.emailEdit.getText());
                String password = String.valueOf(binding.passwordEdit.getText());
                createAccount(email, password, view);
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

        } else {
            binding.emailText.setText(R.string.signed_out);
            binding.firebaseIDText.setText(null);
            binding.emailEdit.setVisibility(View.VISIBLE);
            binding.passwordEdit.setVisibility(View.VISIBLE);
            binding.signInBtn.setVisibility(View.VISIBLE);
            binding.createAccBtn.setVisibility(View.VISIBLE);
            binding.signOutBtn.setVisibility(View.GONE);
            binding.verifyBtn.setVisibility(View.GONE);
        }
    }

    private void createAccount(String email, String password, View view) {
        Log.d(TAG, "createAccount:" + email);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            loadMain(view);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure",
                                    task.getException());
                            Toast.makeText(FirebaseActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }


    private void signIn(String email, String password, View view) {
        Log.d(TAG, "signIn:" + email);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            loadMain(view);
                        } else {

                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            Toast.makeText(FirebaseActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        if (!task.isSuccessful()) {

                            binding.emailText.setText(R.string.auth_failed);
                        }


                    }
                });
    }

    public void loadMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
