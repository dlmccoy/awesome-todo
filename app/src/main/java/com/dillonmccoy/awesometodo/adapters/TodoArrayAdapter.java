package com.dillonmccoy.awesometodo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.dillonmccoy.awesometodo.R;
import com.dillonmccoy.awesometodo.models.TodoItem;
import com.dillonmccoy.awesometodo.utils.TodoSource;

import java.sql.SQLException;
import java.util.ArrayList;

public class TodoArrayAdapter extends ArrayAdapter<TodoItem> {
    TodoSource todoSource;

    public TodoArrayAdapter(Context context, ArrayList<TodoItem> items) {
        super(context, 0, items);
        todoSource = new TodoSource(getContext());
        try {
            todoSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final TodoItem item = getItem(position);

        Spinner spinner;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

            spinner = (Spinner) convertView.findViewById(R.id.prioritySpinner);

            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.priority_options, android.R.layout.simple_spinner_item);

            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);
        } else {
            spinner = (Spinner) convertView.findViewById(R.id.prioritySpinner);
        }

        // Set up the spinner and its onselect handler.
        spinner.setSelection(item.priority);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (item.priority != position) {
                    item.priority = position;

                    todoSource.saveItem(item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

        });

        // Lookup view for data population
        TextView taskText = (TextView) convertView.findViewById(R.id.taskText);

        // Populate the data into the template view using the data object
        taskText.setText(item.task);

        // Return the completed view to render on screen
        return convertView;
    }
}
