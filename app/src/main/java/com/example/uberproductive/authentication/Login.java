package com.example.uberproductive.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberproductive.controller.Dashboard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.uberproductive.R;

public class Login extends AppCompatActivity {
    EditText editEmail;
    EditText editPassword;
    Button loginButton;
    TextView forgotPasswordButton;
    TextView createAccount;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseUser user;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        getSupportActionBar().setTitle("Login to Uber Productive");

        editEmail = findViewById(R.id.email);
        editPassword = findViewById(R.id.LoginPassword);
        loginButton = findViewById(R.id.LoginButton);
        progressBar = findViewById(R.id.progressBar3);
        forgotPasswordButton = findViewById(R.id.forgotPassword);
        createAccount = findViewById(R.id.register);
        user = FirebaseAuth.getInstance().getCurrentUser();
        AlertDialog.Builder reset_alert;
        reset_alert = new AlertDialog.Builder(Login.this);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        LayoutInflater inflater;
        inflater = this.getLayoutInflater();

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Be careful!")
                .setMessage("Logging in with an existing account will delete all the temporary data. Create a new one to save them.")
                .setPositiveButton("Save data", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(),Register.class));
                        finish();
                    }
                }).setNegativeButton("Delete data", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                    }
                });
        dialog.show();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = editEmail.getText().toString();
                String mPassword = editPassword.getText().toString();

                if(mEmail.isEmpty() ){
                    editEmail.setError("Email is required");
                    editPassword.getText().clear();
                    return;
                }
                if(mPassword.isEmpty() ){
                    editPassword.setError("Password is required");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(mEmail,mPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if(firebaseAuth.getCurrentUser().isAnonymous()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            firestore.collection("data").document(user.getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Login.this, "All temporary data are deleted.", Toast.LENGTH_SHORT).show();
                                }
                            });

                            user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Login.this, "Temporary user is deleted.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        Toast.makeText(Login.this, "Login is successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, "Login failed. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        editPassword.getText().clear();
                    }
                });
            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = inflater.inflate(R.layout.reset_pop_up,null );
                reset_alert.setTitle("Reset password ?")
                        .setMessage("Enter your email address to get Password Resetting Link")
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText email = v.findViewById(R.id.resetpop);
                                if(email.getText().toString().isEmpty()){
                                    email.setError("Field is required");
                                    return;
                                }
                                firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Login.this, "Reset email sent. Check your email",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Login.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel", null)
                        .setView(v)
                        .create().show();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });
    }
}
