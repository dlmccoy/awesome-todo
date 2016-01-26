package com.dillonmccoy.awesometodo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dillonmccoy.awesometodo.R;

public class EditItemActivity extends AppCompatActivity {
    String currentText = "";

    private EditText editableText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up reference to the Save button and enable it.
        saveButton = (Button) findViewById(R.id.save_edit_btn);
        saveButton.setEnabled(true);

        // Retrieve the text to be edited and update the UI.
        currentText = getIntent().getStringExtra(MainActivity.ITEM_TEXT_EXTRA);
        editableText = (EditText) findViewById(R.id.editText);
        editableText.setText(currentText);

        // Move the cursor to the end of the EditText.
        editableText.requestFocus();

        // Add a listener for changes to the text box.
        editableText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                // Disable the Save button if the text box is empty.
                saveButton.setEnabled(!editableText.getText().toString().trim().isEmpty());
            }
        });
    }

    /**
     * Handle click to the save button.
     * @param v
     */
    public void onEditSave(View v) {

        // Prepare the result to return to the MainActivity.
        Intent result = new Intent();
        result.putExtra(MainActivity.ITEM_TEXT_EXTRA, editableText.getText().toString());
        setResult(RESULT_OK, result);
        finish();
    }

}
