package com.example.uberproductive.controller;

import android.view.View;
import android.widget.TextView;

import com.example.uberproductive.R;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class NotesViewHolder extends RecyclerView.ViewHolder{
    public TextView title;
    public TextView content;
    public CardView wCardView;
    public View view;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.titles);
        content = itemView.findViewById(R.id.content);
        wCardView = itemView.findViewById(R.id.noteCard);
        view = itemView;
    }
}