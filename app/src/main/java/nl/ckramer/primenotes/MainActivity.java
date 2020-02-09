package nl.ckramer.primenotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import nl.ckramer.primenotes.adapter.NoteAdapter;
import nl.ckramer.primenotes.entity.Note;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener{

    private static final String TAG = "MainActivity";
    Context mContext;
    List<Note> mNotes = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerView.Adapter mNoteAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initListItems();

        mContext = this;
        FloatingActionButton fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action",
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initListItems(){
        Log.d(TAG, "initListItems: preparing list items.");
        for (int i = 0; i < 20; i++){
            String title = "Title text " + i;
            String description = "This is description text " + i + ", see and enjoy.";
            mNotes.add(new Note(Long.valueOf(i), title, description));
        }
        initRecyclerView();
    }
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: preparing recycler view.");
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mNoteAdapter = new NoteAdapter(mNotes, this);
        recyclerView.setAdapter(mNoteAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

    @Override
    public void onNoteClick(int position) {
        Log.d(TAG, "onNoteClick: OnNoteClick has been called!");
        Toast.makeText(this, "Selected note: " + mNotes.get(position).getTitle(), Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, NoteItemActivity.class);
//        intent.putExtra("noteRequest", mNoteRequestList.get(position));
//        startActivity(intent);
    }

    private void deleteNote(Note note) {
        mNotes.remove(note);
        mNoteAdapter.notifyDataSetChanged();
    }
}