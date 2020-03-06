package com.jarifjak.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jarifjak.notes.adapter.NoteAdapter;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NoteAdapter.MyListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private NoteAdapter adapter;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initializeRecyclerView();

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        noteViewModel.getAllNotes().observe(this, notes -> adapter.submitList(notes));
    }

    private void initializeRecyclerView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        adapter = new NoteAdapter();
        adapter.setOnNoteClickListener(this);
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note Deleted!", Toast.LENGTH_SHORT).show();
            }

        }).attachToRecyclerView(recyclerView);
    }

    @OnClick(R.id.addFloatingButton)
    public void onViewClicked() {

        Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_ADDNOTE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_ADDNOTE && resultCode == RESULT_OK) {

            String title = Objects.requireNonNull(data).getStringExtra(Constants.ADDNOTE_TITLE);
            String description = data.getStringExtra(Constants.ADDNOTE_DESCRIPTION);
            int priority = data.getIntExtra(Constants.ADDNOTE_PRIORITY, 1);

            noteViewModel.insert(new Note(title, description, priority));

            Toast.makeText(MainActivity.this, "Note Saved!", Toast.LENGTH_SHORT).show();

        } else if (requestCode == Constants.REQUEST_CODE_EDITNOTE && resultCode == RESULT_OK) {

            int id = Objects.requireNonNull(data).getIntExtra(Constants.ADDNOTE_ID, -1);

            if (id == -1) {

                Toast.makeText(MainActivity.this, "Note can not be updated!", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(Constants.ADDNOTE_TITLE);
            String description = data.getStringExtra(Constants.ADDNOTE_DESCRIPTION);
            int priority = data.getIntExtra(Constants.ADDNOTE_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            note.setId(id);

            noteViewModel.update(note);
            Toast.makeText(MainActivity.this, "Note Updated!", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(MainActivity.this, "Note not Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(MainActivity.this, "All Notes Deleted", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onNoteClick(Note note) {

        Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);

        intent.putExtra(Constants.ADDNOTE_ID, note.getId());                            //id is both used as flag and also to update in db we need id of that object
        intent.putExtra(Constants.ADDNOTE_TITLE, note.getTitle());
        intent.putExtra(Constants.ADDNOTE_DESCRIPTION, note.getDescription());
        intent.putExtra(Constants.ADDNOTE_PRIORITY, note.getPriority());

        startActivityForResult(intent, Constants.REQUEST_CODE_EDITNOTE);
    }
}
