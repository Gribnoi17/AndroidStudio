package ru.mirea.gribanovdv.mireaproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import ru.mirea.gribanovdv.mireaproject.databinding.ActivityFireBaseLoginBinding;
import ru.mirea.gribanovdv.mireaproject.sha.SHA256;

public class FireBaseLoginActivity extends AppCompatActivity {

    private ActivityFireBaseLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFireBaseLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        binding.buttonsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(binding.editemail.getText().toString(), binding.editpassword.getText().toString());
            }
        });
        binding.buttoncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(binding.editemail.getText().toString(), binding.editpassword.getText().toString());
                byte[] password = binding.editpassword.getText().toString().getBytes(StandardCharsets.UTF_8);
                byte[] hash = SHA256.hash(password);
                StringBuilder sb = new StringBuilder();
                for (byte b : hash) {
                    sb.append(String.format("%02X ", b));
                }
                binding.textView10.setText(sb.toString());
            }
        });
        binding.buttonverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmailVerification();
            }
        });
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

            binding.buttonverify.setEnabled(!user.isEmailVerified());
        } else {

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
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
// If sign in fails, display a message to the user.


                            Toast.makeText(FireBaseLoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
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
                            Intent intent = new Intent(FireBaseLoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            updateUI(user);
                        } else {
// If sign in fails, display a message to the user.



                            Toast.makeText(FireBaseLoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
// [START_EXCLUDE]

                        if (!task.isSuccessful()) {


                        }

// [END_EXCLUDE]

                    }
                });
// [END sign_in_with_email]
    }
    private void sendEmailVerification() {
// Disable button
        binding.buttonverify.setEnabled(false);
// Send verification email
// [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        Objects.requireNonNull(user).sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override

                    public void onComplete(@NonNull Task<Void> task) {

// [START_EXCLUDE]
// Re-enable button

                        binding.buttonverify.setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(FireBaseLoginActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {


                            Toast.makeText(FireBaseLoginActivity.this, "Failed to send verification email.",

                                    Toast.LENGTH_SHORT).show();

                        }

// [END_EXCLUDE]

                    }
                });
// [END send_email_verification]
    }
}