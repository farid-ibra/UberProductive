package com.example.uberproductive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberproductive.authentication.Login;
import com.example.uberproductive.controller.NotesViewHolder;
import com.example.uberproductive.controller.SplashScreen;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import com.example.uberproductive.model.NoteModel;
import com.example.uberproductive.controller.SaveNote;
import com.example.uberproductive.controller.NoteData;

public class NoteTaker extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    NavigationView navigationView;
    RecyclerView noteLists;
    DrawerLayout dl;

    FirestoreRecyclerAdapter<NoteModel, NotesViewHolder> noteAdapter;
    ActionBarDrawerToggle hamburgerIcon;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        setContentView(R.layout.note_taker);
        Toolbar tb = findViewById(R.id.tb1);
        setSupportActionBar(tb);
        noteLists = findViewById(R.id.noteRecyclerView);
        dl = findViewById(R.id.sideMenu);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        hamburgerIcon = new ActionBarDrawerToggle(this, dl, tb, R.string.open, R.string.close);
        dl.addDrawerListener(hamburgerIcon);
        hamburgerIcon.setDrawerIndicatorEnabled(true);
        hamburgerIcon.syncState();

        noteLists.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        View headerView = navigationView.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.userDisplayName);
        TextView userEmail = headerView.findViewById(R.id.userDisplayEmail);

        Query query = FirebaseFirestore.getInstance()
                .collection("data")
                .document(user.getUid())
                .collection("notes")
                .orderBy("title", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<NoteModel> allNotes = new FirestoreRecyclerOptions.Builder<NoteModel>()
                .setQuery(query, NoteModel.class)
                .build();

        noteAdapter = new FirestoreRecyclerAdapter<NoteModel, NotesViewHolder>(allNotes) {
            @Override
            protected void onBindViewHolder(@NonNull NotesViewHolder notesViewHolder, final int i, @NonNull final NoteModel noteModel) {
                notesViewHolder.title.setText(noteModel.getTitle());
                notesViewHolder.content.setText(noteModel.getContent());
                notesViewHolder.wCardView.setCardBackgroundColor(notesViewHolder.view.getResources().getColor(R.color.gray, null));
                final String documentId = noteAdapter.getSnapshots().getSnapshot(i).getId();

                notesViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(), NoteData.class);
                        i.putExtra("title", noteModel.getTitle());
                        i.putExtra("content", noteModel.getContent());
                        i.putExtra("noteId", documentId);
                        view.getContext().startActivity(i);
                    }
                });

                ImageView icon = notesViewHolder.view.findViewById(R.id.menuIcon);
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        final String documentId = noteAdapter.getSnapshots().getSnapshot(notesViewHolder.getAdapterPosition()).getId();
                        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                        popupMenu.setGravity(Gravity.END);

                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("data").document(user.getUid()).collection("notes").document(documentId);
                                documentReference.delete();
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });
            }

            @NonNull
            @Override
            public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view, parent, false);
                return new NotesViewHolder(view);
            }
        };
        noteLists.setAdapter(noteAdapter);

        if (user.isAnonymous()) {
            userEmail.setVisibility(View.GONE);
            username.setText("Temporary User");
        } else {
            userEmail.setText(user.getEmail());
            username.setText(user.getDisplayName());
        }

        FloatingActionButton floatingActionButton = findViewById(R.id.AddButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), SaveNote.class));
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        dl.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.goalTracker:
                startActivity(new Intent(this, GoalTracker.class));
                break;

            case R.id.login:
                if (user.isAnonymous()) {
                    startActivity(new Intent(this, Login.class));
                } else {
                    Toast.makeText(this, "Your are already logged in", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.logout:
                if (user.isAnonymous()) {
                    user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                            finishAffinity();
                        }
                    });
                }else {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                }
                break;

            case R.id.timer:
                startActivity(new Intent(this, PomodoroTimer.class));
                break;

            case R.id.notes:
                Toast.makeText(this, "You are in Notes", Toast.LENGTH_SHORT).show();
                break;

            default:
                Toast.makeText(this, "Blank", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter != null) {
            noteAdapter.stopListening();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }
}
