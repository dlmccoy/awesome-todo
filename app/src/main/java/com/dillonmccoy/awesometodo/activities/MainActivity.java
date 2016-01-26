package com.dillonmccoy.awesometodo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.dillonmccoy.awesometodo.R;
import com.dillonmccoy.awesometodo.adapters.TodoArrayAdapter;
import com.dillonmccoy.awesometodo.models.TodoItem;
import com.dillonmccoy.awesometodo.utils.TodoSource;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int EDIT_REQUEST = 1;
    public static final String ITEM_TEXT_EXTRA = "itemText";

    private ArrayList<TodoItem> items;
    private TodoArrayAdapter itemsAdapter;
    private ListView lvItems;
    private TodoItem currentEditingItem;
    private Button addItemBtn;
    private EditText newItemText;
    private TodoSource todoSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up reference to the Add Item button and text box.
        addItemBtn = (Button) findViewById(R.id.btnAddItem);
        addItemBtn.setEnabled(false);
        newItemText = (EditText) findViewById(R.id.etNewItem);

        todoSource = new TodoSource(this);
        try {
            todoSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Fetch the persisted items.
        readItems();

        // Set up and store references to the UI components.
        lvItems = (ListView)findViewById(R.id.lvItems);
        itemsAdapter = new TodoArrayAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);
        setUpListViewListener();
        setUpNewItemListener();
    }

    private void setUpNewItemListener() {
        newItemText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                addItemBtn.setEnabled(!newItemText.getText().toString().trim().isEmpty());
            }
        });
    }
    /**
     * Function to set up listeners for the ListView.
     */
    private void setUpListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // Remove the item from the database.
                todoSource.deleteItem(items.get(position));

                // Remove the item from the local ArrayList and re-render.
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();

                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editItemIntent = new Intent(MainActivity.this, EditItemActivity.class);
                editItemIntent.putExtra(ITEM_TEXT_EXTRA, items.get(position).task);
                currentEditingItem = items.get(position);
                startActivityForResult(editItemIntent, EDIT_REQUEST);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onAddItem(View v) {
        String itemText = newItemText.getText().toString();

        // Don't save empty items.
        if (itemText.isEmpty()) {
            return;
        }

        newItemText.setText("");

        TodoItem newItem = todoSource.addTodoItem(itemText);
        itemsAdapter.add(newItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void readItems() {
        items = todoSource.getAllTodos();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EDIT_REQUEST:
                if (resultCode == RESULT_OK) {

                    // Update the changed element and save to the database.
                    currentEditingItem.task = data.getStringExtra(ITEM_TEXT_EXTRA);
                    todoSource.saveItem(currentEditingItem);

                    // Update the UI
                    itemsAdapter.notifyDataSetChanged();

                    currentEditingItem = null;
                }
                break;
        }
    }

}
