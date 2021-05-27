package com.example.uberproductive.controller;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.uberproductive.R;

public class NoteData extends AppCompatActivity {
    Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_data);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = getIntent();

        TextView wcontent = findViewById(R.id.noteDataContent);
        TextView wtitle = findViewById(R.id.noteDataTitle);
        wcontent.setMovementMethod(new ScrollingMovementMethod());

        wcontent.setText(data.getStringExtra("content"));
        wtitle.setText(data.getStringExtra("title"));
        wcontent.setBackgroundColor(getResources().getColor(R.color.gray,null));

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wintent = new Intent(view.getContext(),EditNote.class);
                wintent.putExtra("noteId",data.getStringExtra("noteId"));
                wintent.putExtra("title",data.getStringExtra("title"));
                wintent.putExtra("content",data.getStringExtra("content"));
                startActivity(wintent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
