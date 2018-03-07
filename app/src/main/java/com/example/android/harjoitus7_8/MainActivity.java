package com.example.android.harjoitus7_8;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final int RC_SIGN_IN = 1;

    private EditText mDate;
    private EditText mSport;
    private EditText mDuration;
    private EditText mRpe;
    private EditText mSharpness;

    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter mTrainingAdapter;

    private String mUsername;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mTrainingDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = ANONYMOUS;

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mTrainingDatabaseReference = mFirebaseDatabase.getReference().child("entries");

        mDate = (EditText) findViewById(R.id.et_date);
        mSport = (EditText) findViewById(R.id.et_sport);
        mDuration = (EditText) findViewById(R.id.et_duration);
        mRpe = (EditText) findViewById(R.id.et_rpe);
        mSharpness = (EditText) findViewById(R.id.et_sharpness);

        mRecyclerView = (RecyclerView) findViewById(R.id.all_entries_list_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        FirebaseRecyclerOptions<TrainingEntry> options =
                new FirebaseRecyclerOptions.Builder<TrainingEntry>()
                        .setQuery(mTrainingDatabaseReference, TrainingEntry.class)
                        .build();


        mTrainingAdapter = new TrainingAdapter(options);
        mRecyclerView.setAdapter(mTrainingAdapter);

        /*new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                long id = (long) viewHolder.itemView.getTag();
                deleteEntry(id);
                mTrainingAdapter.swapCursor(getAllEntries());
            }
        }).attachToRecyclerView(mRecyclerView);*/

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TrainingEntry trainingEntry = dataSnapshot.getValue(TrainingEntry.class);
                mTrainingDatabaseReference.orderByKey();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        mTrainingDatabaseReference.addChildEventListener(mChildEventListener);
    }

    public void addNewEntry(View view) {
        if (mDate.getText().length() == 0 ||
                mDuration.getText().length() == 0 ||
                mRpe.getText().length() == 0 ||
                mSharpness.getText().length() == 0) {
            return;
        }

        float duration = 1;
        int durationMinutes = 0;
        int rpe = 1;
        int sharpness = 1;

        try {
            duration = Float.parseFloat(mDuration.getText().toString());
            rpe = Integer.parseInt(mRpe.getText().toString());
            sharpness = Integer.parseInt(mSharpness.getText().toString());
            durationMinutes = Math.round(duration * 60);

            // Validate input values
            if (durationMinutes > 0 && rpe > 0 && rpe <= 10 && sharpness > 0 && sharpness <= 10) {
                createNewEntry(mDate.getText().toString(), durationMinutes, rpe, sharpness, mSport.getText().toString());
                Toast toast = Toast.makeText(this, R.string.entry_created, Toast.LENGTH_LONG);
                toast.show();
            } else {
                // Give error message about inputs
            }
        } catch (Exception ex) {
            Log.e("error", ex.getMessage());
        }
    }

    private void createNewEntry(String date, int duration, int rpe, int sharpness, String sport) {
        TrainingEntry trainingEntry = new TrainingEntry(date, duration, rpe, sharpness, sport);
        mTrainingDatabaseReference.push().setValue(trainingEntry);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTrainingAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTrainingAdapter.stopListening();
    }
}
