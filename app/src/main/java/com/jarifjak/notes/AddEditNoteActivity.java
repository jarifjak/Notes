package com.jarifjak.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditNoteActivity extends AppCompatActivity {

    @BindView(R.id.titleTV)
    AppCompatEditText titleTV;
    @BindView(R.id.descriptionTV)
    AppCompatEditText descriptionTV;
    @BindView(R.id.priorityNP)
    NumberPicker priorityNP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        if (intent.hasExtra(Constants.ADDNOTE_ID)) {

            setTitle("Edit Note");

            titleTV.setText(intent.getStringExtra(Constants.ADDNOTE_TITLE));
            descriptionTV.setText(intent.getStringExtra(Constants.ADDNOTE_DESCRIPTION));
            priorityNP.setValue(intent.getIntExtra(Constants.ADDNOTE_PRIORITY, 1));

        } else {

            setTitle("Add Note");

        }

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);

        priorityNP.setMinValue(1);
        priorityNP.setMaxValue(30);
    }


    private void saveNote() {

        String title = titleTV.getText().toString();
        String description = descriptionTV.getText().toString();
        int priority = priorityNP.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {

            Toast.makeText(this, "Please insert title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();

        data.putExtra(Constants.ADDNOTE_TITLE, title);
        data.putExtra(Constants.ADDNOTE_DESCRIPTION, description);
        data.putExtra(Constants.ADDNOTE_PRIORITY, priority);

        int id = getIntent().getIntExtra(Constants.ADDNOTE_ID, -1);

        if (id != -1) {

            data.putExtra(Constants.ADDNOTE_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.save_note:
                saveNote();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
