package nl.ckramer.primenotes;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import nl.ckramer.primenotes.adapter.RecyclerViewAdapter;
import nl.ckramer.primenotes.entity.Note;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Context mContext;
    private ArrayList<Note> mNotes = new ArrayList<>();

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
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mContext, mNotes);
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String text = mNotes.get(position).getTitle();
                Toast.makeText(MainActivity.this, text + " was clicked!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View itemView, int position) {
                new AlertDialog.Builder(mContext)
                    .setTitle(mNotes.get(position).getTitle())
                    .setMessage(R.string.delete_confirmation)
                    .setIcon(android.R.drawable.ic_delete)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if(adapter.removeDataFromDataSet(position)) {
                                Toast.makeText(mContext, getString(R.string.item_deleted_succesful, position), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, R.string.item_deleted_unsuccesful, Toast.LENGTH_SHORT).show();
                            }
                        }})
                    .setNegativeButton(R.string.no, null).show();
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }
}