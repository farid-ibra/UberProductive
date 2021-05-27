package com.example.uberproductive.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberproductive.controller.Dashboard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import com.example.uberproductive.R;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    EditText editUserName;
    EditText editUserEmail;
    EditText editUserPass;
    EditText editUserConfPass;
    Button register;
    TextView loginActivity;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    private static  final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^" +
            "(?=.*[a-z])" +         //minimum 1 lower case letter
            "(?=.*[A-Z])" +         //minimum 1 upper case letter
            "(?=.*[0-9])" +         //minimum 1 digit
            "(?=\\S+$)" +           //no white spaces allowed
            ".{8,}" +               //at least 8 characters
            "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        getSupportActionBar().setTitle("Connect to Uber Productive");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editUserName = findViewById(R.id.userName);
        editUserEmail = findViewById(R.id.userEmail);
        editUserPass = findViewById(R.id.password);
        editUserConfPass = findViewById(R.id.passwordConfirm);
        register = findViewById(R.id.register);
        loginActivity = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar4);

        firebaseAuth = FirebaseAuth.getInstance();

        loginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uUsername = editUserName.getText().toString();
                String uUserEmail = editUserEmail.getText().toString();
                String uUserPass = editUserPass.getText().toString();
                String uConfPass = editUserConfPass.getText().toString();

                if(uUserEmail.isEmpty() ){
                    editUserEmail.setError("Email is required");
                    return;
                }

                if(uUsername.isEmpty() ){
                    editUserName.setError("Username is required");
                    return;
                }

                if(uUserPass.isEmpty() ){
                    editUserPass.setError("Password is required");
                    return;
                }
                if(uConfPass.isEmpty()){
                    editUserConfPass.setError("Can not be empty");
                    return;
                }
                //check password strongness
                if(!PASSWORD_PATTERN.matcher(uUserPass).matches()){
                    editUserPass.getText().clear();
                    editUserConfPass.getText().clear();
                    editUserPass.setError("Password should contain minimum 1 lowercase, 1 uppercase, 1 digit and at least 6 characters");
                    return;
                }

                if(!uUserPass.equals(uConfPass)){
                    editUserConfPass.setError("Password doesn't match.");
                    editUserPass.getText().clear();
                    editUserConfPass.getText().clear();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                AuthCredential credential = EmailAuthProvider.getCredential(uUserEmail,uUserPass);
                firebaseAuth.getCurrentUser().linkWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(Register.this, "You are synchronized", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);

                        FirebaseUser usr = firebaseAuth.getCurrentUser();
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                .setDisplayName(uUsername)
                                .build();
                        usr.updateProfile(request);

                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "Failed to connect. Try again", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        editUserPass.getText().clear();
                        editUserConfPass.getText().clear();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this, Dashboard.class));
        finish();
        return super.onOptionsItemSelected(item);
    }
}
