package com.jarifjak.notes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.jarifjak.notes.Note;
import com.jarifjak.notes.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<Note> notes = new ArrayList<>();
    private MyListener listener;

    public NoteAdapter() {
    }

    public void setNotes(List<Note> notes) {

        this.notes = notes;
        notifyDataSetChanged();
    }

    public Note getNoteAt(int position) {

        return notes.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_model, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.titleTV.setText(notes.get(position).getTitle());
        holder.descriptionTV.setText(notes.get(position).getDescription());
        holder.priorityTV.setText(String.valueOf(notes.get(position).getPriority()));
    }

    @Override
    public int getItemCount() {

        return notes.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.titleTV)
        AppCompatTextView titleTV;
        @BindView(R.id.descriptionTV)
        AppCompatTextView descriptionTV;
        @BindView(R.id.priorityTV)
        AppCompatTextView priorityTV;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.note_root)
        public void onViewClicked() {

            listener.onNoteClick(notes.get(getAdapterPosition()));
        }
    }


    public interface MyListener {
        void onNoteClick(Note note);
    }

    public void setOnNoteClickListener(MyListener listener) {

        this.listener = listener;
    }

}
