package com.example.uberproductive.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.uberproductive.NoteTaker;
import com.example.uberproductive.R;

import java.util.HashMap;
import java.util.Map;

public class EditNote extends AppCompatActivity {
    Intent data;
    EditText editNoteTitle,editNoteContent;
    FirebaseFirestore firestore;
    ProgressBar progressBar;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firestore = firestore.getInstance();
        progressBar = findViewById(R.id.progressBar2);
        user = FirebaseAuth.getInstance().getCurrentUser();

        data = getIntent();

        editNoteContent = findViewById(R.id.editNoteContent);
        editNoteTitle = findViewById(R.id.editNoteTitle);

        String mTitle = data.getStringExtra("title");
        String mContent = data.getStringExtra("content");

        editNoteTitle.setText(mTitle);
        editNoteContent.setText(mContent);

        FloatingActionButton fab = findViewById(R.id.saveEditedNote);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String wTitle = editNoteTitle.getText().toString();
                String wContent = editNoteContent.getText().toString();

                if(wTitle.isEmpty() || wContent.isEmpty()){
                    Toast.makeText(EditNote.this, "Can't save note with empty field.", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // save to fireStore
                DocumentReference docref = firestore.collection("data").document(user.getUid()).collection("notes").document(data.getStringExtra("noteId"));

                Map<String,Object> noteData = new HashMap<>();
                noteData.put("title",wTitle);
                noteData.put("content",wContent);

                docref.update(noteData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditNote.this, "Changes saved", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), NoteTaker.class));
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditNote.this, "Error, try again", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }
}
