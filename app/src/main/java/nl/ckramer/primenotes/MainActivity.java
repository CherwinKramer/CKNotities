package nl.ckramer.primenotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import nl.ckramer.primenotes.helper.DBHelper;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        NoteAdapter.OnNoteClickListener,
        NoteAdapter.OnNoteLongClickListener{

    private static final String TAG = "MainActivity";
    Context mContext;
    List<Note> mNotes = new ArrayList<>();

    DBHelper dbHelper;

    RecyclerView recyclerView;
    RecyclerView.Adapter mNoteAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        FloatingActionButton fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null)
                        .show());

        dbHelper = new DBHelper(this);
        try {
            dbHelper.save(new Note("Title item " + dbHelper.getDao(Note.class).countOf(), "Size of items is showed in title"));
            mNotes = dbHelper.getAll();
        } catch (SQLException e){
            Log.e(TAG, "initListItems: error occurred", e);
        }

        initRecyclerView();
    }

    private void initRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mNoteAdapter = new NoteAdapter(mNotes, this, this);
        recyclerView.setAdapter(mNoteAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

    @Override
    public void onNoteClick(int position) {
        Toast.makeText(this, "Single clicked note: " + mNotes.get(position).getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoteLongClick(int position) {
        Note selectedNote = mNotes.get(position);

        new MaterialAlertDialogBuilder(mContext)
            .setTitle(mNotes.get(position).getTitle())
            .setMessage(R.string.delete_confirmation)
            .setPositiveButton(R.string.yes, (dialog, whichButton) -> {
                mNotes.remove(position);
                mNoteAdapter.notifyItemRemoved(position);

                Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_main), "Notitie verwijderd:  " + selectedNote.getTitle(), Snackbar.LENGTH_LONG);
                snackbar.setAction(R.string.item_deletion_undo, v -> {
                    mNotes.add(position, selectedNote);
                    mNoteAdapter.notifyItemInserted(position);
                });
                snackbar.addCallback(new Snackbar.Callback(){
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if(!mNotes.contains(selectedNote)){
                            try {
                                dbHelper.delete(selectedNote);
                            } catch (SQLException e){
                                return;
                            }
                        }
                    }
                });
                snackbar.show();
            })
            .setNegativeButton(R.string.no, null).show();
    }
}