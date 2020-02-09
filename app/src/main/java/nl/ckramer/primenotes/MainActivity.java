package nl.ckramer.primenotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
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
import com.google.android.material.textfield.TextInputEditText;

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
        dbHelper = new DBHelper(this);
        try {
            mNotes = dbHelper.getAll();
        } catch (SQLException e){
            Log.e(TAG, "initListItems: error occurred", e);
        }

        initRecyclerView();
        FloatingActionButton fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(view -> initializeActionButton());
    }

    private void initializeActionButton() {
        LayoutInflater li = LayoutInflater.from(mContext);
        final View promptView = li.inflate(R.layout.note_prompt, null);

        TextInputEditText title = promptView.findViewById(R.id.editTitle);
        TextInputEditText description = promptView.findViewById(R.id.editDescription);

        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(mContext)
                .setTitle("Notitie aanmaken")
                .setView(promptView)
                .setCancelable(false)
                .setPositiveButton("Opslaan", (dialog, id) -> { })
                .setNegativeButton("Annuleren",
                        (dialog, id) -> {
                            dialog.cancel();
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if(validateNoteFields(title, description)){
                try {
                    Note savedNote = dbHelper.save(new Note(title.getText().toString(), description.getText().toString()));
                    mNotes.add(savedNote);
                    mNoteAdapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                } catch (SQLException e){
                    Log.e(TAG, "initializeActionButton: couldn't save note..", e);
                }
            } else {
                Log.d(TAG, "initializeActionButton: VALIDATION UNSUCCESSFULL");
            }
        });

    }

    private boolean validateNoteFields(TextInputEditText title, TextInputEditText description) {

        boolean success = true;
        if(TextUtils.isEmpty(title.getText())){
            title.setError(mContext.getResources().getString(R.string.validation_error));
            title.requestFocus();
            success = false;
        } else {
            title.setError(null);
        }

        if(TextUtils.isEmpty(description.getText())){
            description.setError(mContext.getResources().getString(R.string.validation_error));
            description.requestFocus();
            success = false;
        } else {
            description.setError(null);
        }
        return success;
    }

    private void initRecyclerView() {
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