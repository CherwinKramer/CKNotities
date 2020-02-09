package nl.ckramer.primenotes.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import nl.ckramer.primenotes.R;
import nl.ckramer.primenotes.entity.Note;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{

    private static final String TAG = "NoteAdapter";
    private List<Note> mNotes;
    private Context mContext;

    private OnNoteClickListener mOnNoteClickListener;
    private OnNoteLongClickListener mOnNoteLongClickListener;

    public NoteAdapter(List<Note> notes, OnNoteClickListener onNoteClickListener, OnNoteLongClickListener onNoteLongClickListener)  {
        this.mNotes = notes;
        this.mOnNoteClickListener = onNoteClickListener;
        this.mOnNoteLongClickListener = onNoteLongClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view, mOnNoteClickListener, mOnNoteLongClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Note note = mNotes.get(position);
        holder.title.setText(note.getTitle());
        holder.description.setText(note.getDescription());
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView title, description;
        OnNoteClickListener mOnNoteClickListener;
        
        public ViewHolder(View v, OnNoteClickListener onNoteClickListener, OnNoteLongClickListener onNoteLongClickListener){
            super(v);
            title = v.findViewById(R.id.item_title);
            description = v.findViewById(R.id.item_description);

            mOnNoteLongClickListener = onNoteLongClickListener;
            mOnNoteClickListener = onNoteClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: " + getAdapterPosition());
            mOnNoteClickListener.onNoteClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            Log.d(TAG, "onLongClick: " + getAdapterPosition());
            mOnNoteLongClickListener.onNoteLongClick(getAdapterPosition());
            return true;
        }
    }

    public interface OnNoteClickListener{
        void onNoteClick(int position);
    }

    public interface OnNoteLongClickListener {
        void onNoteLongClick(int position);
    }

//    public boolean removeDataFromDataSet(int position){
//        try {
//            mNotes.remove(position);
//            notifyItemRemoved(position);
//            notifyItemRangeChanged(position, mNotes.size());
//            return true;
//        } catch (Exception e){
//            return false;
//        }
//    }
}
