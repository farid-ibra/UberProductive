package com.example.uberproductive;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberproductive.authentication.Login;
import com.example.uberproductive.controller.SplashScreen;
import com.example.uberproductive.model.GoalModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class GoalTracker extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout dl;
    ActionBarDrawerToggle hamburgerIcon;
    NavigationView nav_view;
    RecyclerView goalLists;
    FirebaseFirestore firestore;
    FirestoreRecyclerAdapter<GoalModel, GoalViewHolder> goalAdapter;
    FirebaseUser user;

    SharedPreferences sharedPreferences;

    public static final String STREAKS = "streaks";
    public static final String IS_CHECKED = "isChecked";
    public static final String MISSED_DAYS ="missedDays";
    public static final String LAST_TIME_CHECKED = "lastTimeChecked";
    public static final String IS_VISIBLE = "VisibleOrGone";

    int streaks1;
    int missedDays1;
    Boolean isChecked1;
    LocalDate lastTimeChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_tracker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        goalLists = findViewById(R.id.goallist);
        dl = findViewById(R.id.sideMenu);
        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

        hamburgerIcon = new ActionBarDrawerToggle(this, dl, toolbar, R.string.open, R.string.close);
        dl.addDrawerListener(hamburgerIcon);
        hamburgerIcon.setDrawerIndicatorEnabled(true);
        hamburgerIcon.syncState();

        View headerView = nav_view.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.userDisplayName);
        TextView userEmail = headerView.findViewById(R.id.userDisplayEmail);

        if (user.isAnonymous()) {
            userEmail.setVisibility(View.GONE);
            username.setText("Temporary User");
        }
        else {
            userEmail.setText(user.getEmail());
            username.setText(user.getDisplayName());
        }

        FloatingActionButton fab = findViewById(R.id.addGoalFloat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GoalTracker.this);
                builder.setTitle("Add title of your goal");
                final EditText input = new EditText(GoalTracker.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                builder.setView(input);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int a) {
                        String nTitle = input.getText().toString();
                        String nDate = LocalDate.now().toString();

                        if(nTitle.isEmpty()){
                            Toast.makeText(GoalTracker.this, "Can not save with an empty field", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //sharedPreferences allocated to the goalTitle
                        sharedPreferences = getSharedPreferences(nTitle, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putInt(STREAKS, 0);
                        editor.putBoolean(IS_CHECKED, false);
                        editor.putInt(MISSED_DAYS,0);
                        editor.putString(LAST_TIME_CHECKED, "1997-12-24");
                        editor.putInt(IS_VISIBLE,View.VISIBLE);
                        editor.apply();

                        // save goal to database
                        DocumentReference documentReference = firestore.collection("data").document(user.getUid()).collection("goals").document();
                        Map<String,Object> goalData = new HashMap<>();
                        goalData.put("title",nTitle);
                        goalData.put("dateAdded",nDate);
                        documentReference.set(goalData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(GoalTracker.this, "Goal is Added to Database", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(GoalTracker.this, "Error, try again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        Query query = firestore.collection("data").document(user.getUid()).collection("goals").orderBy("title", Query.Direction.DESCENDING);
        // query data > uuid > goals

        FirestoreRecyclerOptions<GoalModel> allGoals = new FirestoreRecyclerOptions.Builder<GoalModel>()
                .setQuery(query, GoalModel.class)
                .build();

        goalAdapter = new FirestoreRecyclerAdapter<GoalModel, GoalViewHolder>(allGoals) {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onBindViewHolder(@NonNull GoalViewHolder goalViewHolder, final int i, @NonNull final GoalModel goal) {
                goalViewHolder.mCardView.setCardBackgroundColor(goalViewHolder.view.getResources().getColor(R.color.gray, null));
                goalViewHolder.goalName.setText(goal.getTitle());
                goalViewHolder.dateAdded.setText(goal.getDateAdded());

                sharedPreferences = getSharedPreferences(goal.getTitle(), MODE_PRIVATE); //sharedPreferences allocated to the goalTitle
                SharedPreferences.Editor editor = sharedPreferences.edit();
                goalViewHolder.checkBox.setChecked(sharedPreferences.getBoolean(IS_CHECKED, false));
                goalViewHolder.streaks.setText(String.valueOf(sharedPreferences.getInt(STREAKS, 0)));
                goalViewHolder.missedDays.setText(sharedPreferences.getInt(MISSED_DAYS, 0)+ " missed days");
                goalViewHolder.mCardView.setVisibility(sharedPreferences.getInt(IS_VISIBLE,View.VISIBLE));

                LocalDate todaysDate = LocalDate.now();
                lastTimeChecked = LocalDate.parse(sharedPreferences.getString(LAST_TIME_CHECKED,"1997-12-24"));

                if(goalViewHolder.mCardView.getVisibility() == View.GONE
                                                                            &&
                        String.valueOf(lastTimeChecked.getDayOfMonth()) != String.valueOf(todaysDate.getDayOfMonth()) ) {

                    streaks1 = sharedPreferences.getInt(STREAKS, 0) +1;
                    if(LocalDate.parse(goal.getDateAdded()).until(todaysDate, ChronoUnit.DAYS)==0) {
                        missedDays1 = (int) (LocalDate.parse(goal.getDateAdded())
                                .until(todaysDate, ChronoUnit.DAYS) - streaks1 + 1);
                    }

                    if(LocalDate.parse(goal.getDateAdded()).until(todaysDate, ChronoUnit.DAYS)>0) {
                        missedDays1 = (int) (LocalDate.parse(goal.getDateAdded())
                                .until(todaysDate, ChronoUnit.DAYS) - streaks1);
                    }

                    isChecked1 = sharedPreferences.getBoolean(IS_CHECKED,false);

                    goalViewHolder.streaks.setText(String.valueOf(streaks1));
                    goalViewHolder.missedDays.setText(missedDays1 + " missed days");
                    goalViewHolder.checkBox.setChecked(isChecked1);
                    goalViewHolder.mCardView.setVisibility(View.VISIBLE);

                    editor.putInt(STREAKS, streaks1);
                    editor.putBoolean(IS_CHECKED, isChecked1);
                    editor.putInt(MISSED_DAYS,missedDays1);
                    editor.putInt(IS_VISIBLE, View.VISIBLE);
                    editor.apply();
                }

                goalViewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        final String documentId = goalAdapter.getSnapshots().getSnapshot(goalViewHolder.getAdapterPosition()).getId();
                        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                        popupMenu.setGravity(Gravity.END);

                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                DocumentReference documentReference = firestore.collection("data").document(user.getUid()).collection("goals").document(documentId);
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
            public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int vt) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_view, parent, false);
                return new GoalViewHolder(v);
            }
        };

        goalLists.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        goalLists.setAdapter(goalAdapter);
    }

    public class GoalViewHolder extends RecyclerView.ViewHolder {
        View view;
        CardView mCardView;
        TextView streaks;
        TextView goalName;
        TextView missedDays;
        CheckBox checkBox;
        TextView dateAdded;
        ImageView deleteButton;

        public GoalViewHolder(@NonNull View itemView) {
            super(itemView);
            streaks = itemView.findViewById(R.id.streaks);
            goalName = itemView.findViewById(R.id.goalName);
            missedDays = itemView.findViewById(R.id.missedDays);
            checkBox = itemView.findViewById(R.id.checkBox);
            mCardView = itemView.findViewById(R.id.goalCard);
            view = itemView;
            dateAdded = itemView.findViewById(R.id.dateAdded);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
                    sharedPreferences = getSharedPreferences(goalName.getText().toString(), MODE_PRIVATE); //sharedPreferences allocated to the goalTitle
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    if(on){
                    mCardView.setVisibility(View.GONE);
                    editor.putBoolean(IS_CHECKED, false);
                    editor.putString(LAST_TIME_CHECKED, String.valueOf(LocalDate.now()));
                    editor.putInt(IS_VISIBLE,View.GONE);
                    editor.apply();
                    }
                }
            });
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        dl.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.goalTracker:
                Toast.makeText(this, "You are in Goal Tracker", Toast.LENGTH_SHORT).show();
                break;

            case R.id.login:
                if (user.isAnonymous()) {
                    startActivity(new Intent(this, Login.class));
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
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
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                break;

            case R.id.notes:
                startActivity(new Intent(this, NoteTaker.class));
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                break;

            default:
                Toast.makeText(this, "Blank", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        goalAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (goalAdapter != null) {
            goalAdapter.stopListening();
        }
    }
}
