package com.jarifjak.notes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.jarifjak.notes.model.Note;
import com.jarifjak.notes.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.ViewHolder> {

    private MyListener listener;

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {

        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {

            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {

            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getPriority() == newItem.getPriority();
        }
    };


    public Note getNoteAt(int position) {

        return getItem(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_model, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.titleTV.setText(getItem(position).getTitle());
        holder.descriptionTV.setText(getItem(position).getDescription());
        holder.priorityTV.setText(String.valueOf(getItem(position).getPriority()));
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.titleTV)
        AppCompatTextView titleTV;
        @BindView(R.id.descriptionTV)
        AppCompatTextView descriptionTV;
        @BindView(R.id.priorityTV)
        AppCompatTextView priorityTV;

        ViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.note_root)
        void onViewClicked() {

            listener.onNoteClick(getItem(getAdapterPosition()));
        }
    }


    public interface MyListener {
        void onNoteClick(Note note);
    }

    public void setOnNoteClickListener(MyListener listener) {

        this.listener = listener;
    }

}
